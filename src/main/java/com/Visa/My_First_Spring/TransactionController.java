package com.Visa.My_First_Spring;

import com.Visa.My_First_Spring.entity.Account;
import com.Visa.My_First_Spring.entity.OtpToken;
import com.Visa.My_First_Spring.entity.Transaction;
import com.Visa.My_First_Spring.entity.TransactionView;
import com.Visa.My_First_Spring.entity.TransactionType;
import com.Visa.My_First_Spring.entity.User;
import com.Visa.My_First_Spring.repository.*;
import com.Visa.My_First_Spring.service.EmailService;
import com.Visa.My_First_Spring.service.TransactionService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import java.time.ZoneId;
import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/banking/transactions/transfer")
public class TransactionController {

    private final AccountRepository accountRepository;
    private final TransactionService transactionService;
    private final OtpTokenRepository otpTokenRepository;
    private final EmailService emailService;

    @Autowired
    public TransactionController(AccountRepository accountRepository,
                                 TransactionService transactionService,
                                 OtpTokenRepository otpTokenRepository,
                                 EmailService emailService) {
        this.accountRepository = accountRepository;
        this.transactionService = transactionService;
        this.otpTokenRepository = otpTokenRepository;
        this.emailService = emailService;
    }

    @GetMapping("/new")
    public String showTransactionForm(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");

        if (user == null) {
            return "redirect:/login";
        }

        List<Account> accounts = accountRepository.findByUser(user);

        model.addAttribute("transaction", new Transaction());
        model.addAttribute("accounts", accounts);
        model.addAttribute("transactionTypes", TransactionType.values());
        model.addAttribute("user", user);

        return "transaction-form";
    }

    // Step 1: On submit, generate OTP & send email, show OTP input form instead of processing directly
    @PostMapping("/submit")
    public String submitTransaction(@ModelAttribute("transaction") @Valid Transaction transaction,
                                    BindingResult bindingResult,
                                    Model model,
                                    HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }

        List<Account> accounts = accountRepository.findByUser(user);
        model.addAttribute("accounts", accounts);
        model.addAttribute("transactionTypes", TransactionType.values());
        model.addAttribute("user", user);

        if (bindingResult.hasErrors()) {
            model.addAttribute("errorMessage", "Please correct the errors in the form.");
            return "transaction-form";
        }

        // Save transaction temporarily in session for later processing
        session.setAttribute("pendingTransaction", transaction);

        // Generate OTP code (e.g., 6 digit random)
        String otpCode = String.format("%06d", (int)(Math.random() * 1000000));

        // Save or update OTP for user in DB, expires in 10 minutes
        OtpToken otpToken = otpTokenRepository.findByUser(user).orElse(new OtpToken());
        otpToken.setUser(user);
        otpToken.setOtp(otpCode);
        otpToken.setExpiryDate(LocalDateTime.now().plusMinutes(10));
        otpTokenRepository.save(otpToken);

        // Send OTP email
        emailService.sendOtpEmail(user.getEmail(), user.getFirstName(), otpCode);

        // Show OTP verification form
        model.addAttribute("otpSent", true);
        model.addAttribute("otp", new OtpToken());  // for form binding if needed

        return "transaction-otp-form";  // new JSP page to enter OTP
    }

    // Step 2: OTP verification endpoint
    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestParam("otp") String otp,
                            Model model,
                            HttpSession session) {

        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }

        List<Account> accounts = accountRepository.findByUser(user);
        model.addAttribute("accounts", accounts);
        model.addAttribute("transactionTypes", TransactionType.values());
        model.addAttribute("user", user);

        OtpToken otpToken = otpTokenRepository.findByUser(user).orElse(null);
        if (otpToken == null || !otpToken.getOtp().equals(otp)) {
            model.addAttribute("errorMessage", "Invalid OTP. Please try again.");
            model.addAttribute("otpSent", true);
            return "transaction-otp-form";
        }

        if (otpToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            model.addAttribute("errorMessage", "OTP expired. Please submit the transaction again.");
            return "transaction-form";
        }

        // OTP valid, remove OTP token
        otpTokenRepository.delete(otpToken);

        // Retrieve the pending transaction from session
        Transaction pendingTransaction = (Transaction) session.getAttribute("pendingTransaction");
        if (pendingTransaction == null) {
            model.addAttribute("errorMessage", "No pending transaction found. Please submit again.");
            return "transaction-form";
        }

        try {
            transactionService.processTransaction(pendingTransaction);
            model.addAttribute("successMessage", "Transaction processed successfully!");
            session.removeAttribute("pendingTransaction");
            model.addAttribute("transaction", new Transaction()); // reset form
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("transaction", pendingTransaction);
            return "transaction-form";
        }

        return "transaction-form";
    }

    @GetMapping("/history")
    public String viewTransactionHistory(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }

        List<Account> accounts = accountRepository.findByUser(user);
        List<Transaction> userTransactions = transactionService.getTransactionsForUser(accounts);

        List<TransactionView> transactionViews = userTransactions.stream().map(tx -> {
            TransactionView view = new TransactionView();
            view.setId(tx.getId());
            view.setTransactionType(tx.getTransactionType());
            view.setFromAccount(tx.getFromAccount());
            view.setToAccount(tx.getToAccount());
            view.setAmount(tx.getAmount());
            view.setReferenceNote(tx.getReferenceNote());
            view.setTransactionStatus(tx.getTransactionStatus());
            view.setTransactionHash(tx.getTransactionHash());

            if (tx.getCompletedAt() != null) {
            	Date completedDate = Date.from(tx.getCompletedAt().atZone(ZoneId.systemDefault()).toInstant());
            	view.setCompletedAt(completedDate);

            } else {
                view.setCompletedAt(null);
            }
            return view;
        }).collect(Collectors.toList());
       

        model.addAttribute("transactions", transactionViews);
        model.addAttribute("user", user);

        return "transaction-history"; // The name of your JSP view file (transaction-history.jsp)
    }


}
