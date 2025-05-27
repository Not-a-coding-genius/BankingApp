package com.Visa.My_First_Spring;

import com.Visa.My_First_Spring.entity.Account;
import com.Visa.My_First_Spring.entity.AccountStatus;
import com.Visa.My_First_Spring.entity.User;
import com.Visa.My_First_Spring.service.AccountService;
import com.Visa.My_First_Spring.service.UserService;
import com.Visa.My_First_Spring.repository.AccountRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalTime;
import java.util.List;

@Controller
public class BankingDashboardController {

    @Autowired
    private UserService userService;

    @Autowired
    private AccountRepository accountRepository;
    
    @Autowired
    private AccountService accountService;

    @GetMapping("/dashboard")
    public String showDashboard(HttpSession session, Model model) {
        System.out.println("showDashboard() called");

        String email = (String) session.getAttribute("userEmail");
        System.out.println("Session userEmail: " + email);

        if (email == null) {
            System.out.println("No userEmail found in session. Redirecting to login.");
            return "redirect:/login";
        }

        User user;
        try {
            user = userService.getUserByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
            System.out.println("User found: " + user);
        } catch (UsernameNotFoundException e) {
            System.out.println("Exception: " + e.getMessage());
            throw e;
        }
        int hour = LocalTime.now().getHour();
        String greeting;
        if (hour < 12) greeting = "Morning";
        else if (hour < 17) greeting = "Afternoon";
        else greeting = "Evening";
        List<Account> accounts = accountRepository.findByUser(user);
        System.out.println("Accounts found: " + accounts.size());

        model.addAttribute("user", user);
        model.addAttribute("accounts", accounts);
        model.addAttribute("greeting", greeting);
        System.out.println("Attributes added to model");

        return "dashboard";
    }
    
    @GetMapping("create")
    public String showCreateAccountForm(Model model,HttpSession session) {
    	String email = (String) session.getAttribute("userEmail");
    	User user = userService.getUserByEmail(email).orElse(null);
    	model.addAttribute("user", user);
        return "create"; // name of your JSP or Thymeleaf page for the form
    }
    
    @PostMapping("/create")
    public String createAccount(
            @RequestParam("accountType") String accountType,
            @RequestParam("initialDeposit") Double initialDeposit,
            @RequestParam("status") String statusStr,
            HttpSession session,
            Model model
    ) {
        String email = (String) session.getAttribute("userEmail");
        if (email == null) {
            return "redirect:/login";
        }

        User user = userService.getUserByEmail(email).orElse(null);
        if (user == null) {
            return "redirect:/login";
        }

        if (initialDeposit < 1000) {
            model.addAttribute("errorMessage", "Initial deposit must be at least ₹1,000.00.");
            model.addAttribute("user", user);
            model.addAttribute("accounts", accountRepository.findByUser(user));
            return "dashboard";
        }

        AccountStatus status;
        try {
            status = AccountStatus.valueOf(statusStr);
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", "Invalid account status.");
            model.addAttribute("user", user);
            model.addAttribute("accounts", accountRepository.findByUser(user));
            return "dashboard";
        }

        try {
            accountService.createAccount(user, accountType, initialDeposit, status);
            model.addAttribute("successMessage", "Account created successfully!");
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Failed to create account: " + e.getMessage());
        }

        model.addAttribute("user", user);
        model.addAttribute("accounts", accountRepository.findByUser(user));
        return "dashboard";
    }
}

