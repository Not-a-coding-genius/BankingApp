package com.Visa.My_First_Spring;

import com.Visa.My_First_Spring.entity.User;
import com.Visa.My_First_Spring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
public class SignUpController {

    @Autowired
    private UserService userService;

    private void preserveFormData(Model model, String firstName, String lastName, 
            String email, String phone, boolean newsletter) {
        System.out.println(">>> preserveFormData() called");
        model.addAttribute("firstName", firstName != null ? firstName : "");
        model.addAttribute("lastName", lastName != null ? lastName : "");
        model.addAttribute("email", email != null ? email : "");
        model.addAttribute("phone", phone != null ? phone : "");
        model.addAttribute("newsletter", newsletter);
    }

    // Display the SignUp page
    @GetMapping("/SignUp")
    public String showSignUpPage() {
        System.out.println(">>> showSignUpPage() called");
        return "SignUp"; // This will resolve to SignUp.jsp
    }

    // Handle SignUp form submission
    @PostMapping("/SignUp")
    public String processSignUp(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String email,
            @RequestParam String phone,
            @RequestParam String password,
            @RequestParam String confirmPassword,
            @RequestParam(required = false) String agreeTerms,
            @RequestParam(required = false) String newsletter,
            Model model,
            RedirectAttributes redirectAttributes) {
        System.out.println(">>> processSignUp() called");
        System.out.println(">>> Controller hit");
        
        System.out.println(">>> SignUp attempt details:");
        System.out.println("FirstName: " + firstName);
        System.out.println("LastName: " + lastName);
        System.out.println("Email: " + email);
        System.out.println("Phone: " + phone);
        
        // Basic validation
        if (firstName == null || firstName.trim().isEmpty()) {
            model.addAttribute("error", "First name is required");
            preserveFormData(model, firstName, lastName, email, phone, newsletter != null);
            return "SignUp";
        }
        
        if (lastName == null || lastName.trim().isEmpty()) {
            model.addAttribute("error", "Last name is required");
            preserveFormData(model, firstName, lastName, email, phone, newsletter != null);
            return "SignUp";
        }
        
        if (email == null || email.trim().isEmpty()) {
            model.addAttribute("error", "Email is required");
            preserveFormData(model, firstName, lastName, email, phone, newsletter != null);
            return "SignUp";
        }
        
        if (phone == null || phone.trim().isEmpty()) {
            model.addAttribute("error", "Phone number is required");
            preserveFormData(model, firstName, lastName, email, phone, newsletter != null);
            return "SignUp";
        }
        
        if (password == null || password.trim().isEmpty()) {
            model.addAttribute("error", "Password is required");
            preserveFormData(model, firstName, lastName, email, phone, newsletter != null);
            return "SignUp";
        }
        
        if (confirmPassword == null || confirmPassword.trim().isEmpty()) {
            model.addAttribute("error", "Password confirmation is required");
            preserveFormData(model, firstName, lastName, email, phone, newsletter != null);
            return "SignUp";
        }
        
        System.out.println(">>> Validating email format");
        if (!userService.isValidEmail(email)) {
            model.addAttribute("error", "Please enter a valid email address");
            preserveFormData(model, firstName, lastName, email, phone, newsletter != null);
            return "SignUp";
        }
        
        System.out.println(">>> Validating phone format");
        System.out.println("userService.isValidPhone(phone) returns:");
        System.out.println(userService.isValidPhone(phone));
        if (!userService.isValidPhone(phone)) {
            model.addAttribute("error", "Please enter a valid phone number (10-15 digits)");
            preserveFormData(model, firstName, lastName, email, phone, newsletter != null);
            return "SignUp";
        }
        
        System.out.println(">>> Validating password format");
        System.out.println("userService.isValidPassword(password) returns:");
        System.out.println(userService.isValidPassword(password));
        if (!userService.isValidPassword(password)) {
            model.addAttribute("error", "Password must be at least 8 characters long and contain uppercase, lowercase, numbers, and special characters");
            preserveFormData(model, firstName, lastName, email, phone, newsletter != null);
            return "SignUp";
        }
        
        System.out.println(">>> Comparing password and confirmPassword");
        System.out.println("password.equals(confirmPassword) returns:");
        System.out.println(password.equals(confirmPassword));
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match");
            preserveFormData(model, firstName, lastName, email, phone, newsletter != null);
            return "SignUp";
        }
        
        System.out.println(">>> Checking agreeTerms checkbox value");
        System.out.println("agreeTerms.equals(\"on\") returns:");
        System.out.println(agreeTerms != null && agreeTerms.equals("on"));
        if (agreeTerms == null || !agreeTerms.equals("on")) {
            model.addAttribute("error", "You must agree to the Terms of Service and Privacy Policy");
            preserveFormData(model, firstName, lastName, email, phone, newsletter != null);
            return "SignUp";
        }
        
        System.out.println(">>> Checking if user exists");
        System.out.println("userService.userExists(email) returns:");
        System.out.println(userService.userExists(email));
        if (userService.userExists(email)) {
            model.addAttribute("error", "An account with this email already exists. Please use a different email or sign in.");
            preserveFormData(model, firstName, lastName, email, phone, newsletter != null);
            return "SignUp";
        }
        
        System.out.println(">>> Checking newsletter subscription checkbox");
        System.out.println("newsletter.equals(\"on\") returns:");
        System.out.println(newsletter != null && newsletter.equals("on"));
        boolean newsletterSubscription = newsletter != null && newsletter.equals("on");
        
        System.out.println(">>> About to call userService.registerUser() with params:");
        System.out.println("firstName: " + firstName);
        System.out.println("lastName: " + lastName);
        System.out.println("email: " + email);
        System.out.println("phone: " + phone);
        System.out.println("newsletterSubscription: " + newsletterSubscription);
        
        if (userService.registerUser(firstName, lastName, email, phone, password, newsletterSubscription)) {
            redirectAttributes.addFlashAttribute("success", 
                "Account created successfully! Please check your email (" + email + ") for verification instructions before signing in.");
            return "redirect:/Login";
        } else {
            model.addAttribute("error", "Registration failed. Please try again.");
            preserveFormData(model, firstName, lastName, email, phone, newsletter != null);
            return "SignUp";
        }
    }

    // Check email availability (AJAX endpoint)
    @GetMapping("/check-email")
    @ResponseBody
    public Map<String, Object> checkEmailAvailability(@RequestParam String email) {
        System.out.println(">>> checkEmailAvailability() called");
        
        Map<String, Object> response = new HashMap<>();
        
        if (email == null || email.trim().isEmpty()) {
            response.put("available", false);
            response.put("message", "Email is required");
            return response;
        }
        
        System.out.println(">>> Validating email format for: " + email);
        if (!userService.isValidEmail(email)) {
            response.put("available", false);
            response.put("message", "Please enter a valid email address");
            return response;
        }
        
        System.out.println(">>> Checking if user exists for email: " + email);
        if (userService.userExists(email)) {
            response.put("available", false);
            response.put("message", "This email is already registered");
            
            Optional<User> userOpt = userService.getUserByEmail(email);
            if (userOpt.isPresent() && !userOpt.get().isEmailVerified()) {
                response.put("needsVerification", true);
                response.put("message", "This email is registered but not verified. Check your email for verification instructions.");
            }
        } else {
            response.put("available", true);
            response.put("message", "Email is available");
        }
        
        return response;
    }

    // Social SignUp handler (placeholder for future implementation)
    @GetMapping("/social-SignUp")
    public String handleSocialSignUp(
            @RequestParam String provider,
            RedirectAttributes redirectAttributes) {
        System.out.println(">>> handleSocialSignUp() called");
        System.out.println("Social SignUp attempt with provider: " + provider);
        
        redirectAttributes.addFlashAttribute("info", 
            "Social SignUp with " + provider + " is not yet implemented.");
        
        return "redirect:/SignUp";
    }
}
