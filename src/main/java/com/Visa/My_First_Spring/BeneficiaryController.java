package com.Visa.My_First_Spring;

import com.Visa.My_First_Spring.entity.Beneficiary;
import com.Visa.My_First_Spring.entity.User;
import com.Visa.My_First_Spring.service.BeneficiaryService;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/beneficiaries")
public class BeneficiaryController {

    @Autowired
    private BeneficiaryService beneficiaryService;

    // Show all beneficiaries for a specific account
    @GetMapping("/manage")
    public String manageBeneficiaries(@RequestParam("accountId") Long accountId, Model model, HttpSession session) {
        model.addAttribute("accountId", accountId);
        model.addAttribute("beneficiaries", beneficiaryService.getBeneficiariesByAccountId(accountId));
        User user = (User) session.getAttribute("loggedInUser");
        model.addAttribute("user", user);
        return "beneficiary-manage";
    }

    // Add a new beneficiary
    @PostMapping("/add")
    public String addBeneficiary(@RequestParam Long accountId,
                                 @RequestParam String name,
                                 @RequestParam String relationship,
                                 @RequestParam int age,
                                 RedirectAttributes redirectAttributes) {
        try {
            beneficiaryService.addBeneficiary(accountId, name, relationship, age);
            redirectAttributes.addFlashAttribute("successMessage", "Beneficiary added successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/beneficiaries/manage?accountId=" + accountId;
    }

    // Show edit form
    @GetMapping("/edit")
    public String editBeneficiaryForm(@RequestParam Long id, Model model,HttpSession session) {
        Beneficiary beneficiary = beneficiaryService.getById(id);
        model.addAttribute("beneficiary", beneficiary);
        User user = (User) session.getAttribute("loggedInUser");
        model.addAttribute("user", user);
        return "beneficiary-edit";
    }

    // Update beneficiary
    @PostMapping("/update")
    public String updateBeneficiary(@ModelAttribute Beneficiary beneficiary) {
        beneficiaryService.updateBeneficiary(beneficiary);
        return "redirect:/beneficiaries/manage?accountId=" + beneficiary.getAccountId();
    }

    // Delete beneficiary
    @GetMapping("/delete")
    public String deleteBeneficiary(@RequestParam Long id, @RequestParam Long accountId) {
        beneficiaryService.deleteBeneficiary(id);
        return "redirect:/beneficiaries/manage?accountId=" + accountId;
    }
}
