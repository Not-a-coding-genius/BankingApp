package com.Visa.My_First_Spring;

import com.Visa.My_First_Spring.entity.Account;
import com.Visa.My_First_Spring.entity.AccountStatus;
import com.Visa.My_First_Spring.entity.AccountType;
import com.Visa.My_First_Spring.service.AccountService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/banking/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    // View account details
    @GetMapping("/{id}")
    public String showAccountDetails(@PathVariable Long id, Model model, HttpSession session) {
        Account account = accountService.getAccountById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid account ID: " + id));

        model.addAttribute("account", account);
        model.addAttribute("user", account.getUser());
        model.addAttribute("types", AccountType.values());
        model.addAttribute("statuses", AccountStatus.values());

        return "account-details"; // Corresponds to account-details.jsp
    }

    // Update account (type and status)
    @PostMapping("/{id}/update")
    public String updateAccount(@PathVariable Long id,
                                @RequestParam AccountType accountType,
                                @RequestParam AccountStatus status,
                                RedirectAttributes redirectAttributes) {

        accountService.updateAccount(id, accountType, status);
        redirectAttributes.addFlashAttribute("successMessage", "Account updated successfully.");
        return "redirect:/banking/accounts/" + id;
    }

    // Delete account
    @PostMapping("/{id}/delete")
    public String deleteAccount(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        accountService.deleteAccount(id);
        redirectAttributes.addFlashAttribute("successMessage", "Account deleted successfully.");
        return "redirect:/dashboard";
    }
}

