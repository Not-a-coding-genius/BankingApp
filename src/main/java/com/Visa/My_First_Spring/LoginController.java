package com.Visa.My_First_Spring;

import com.Visa.My_First_Spring.entity.User;
import com.Visa.My_First_Spring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import java.util.Optional;

@Controller
public class LoginController {
    
    @Autowired
    private UserService userService;
    
    // Display the Login page
    @GetMapping("/Login")
    public String showLoginPage() {
        return "Login"; // This will resolve to Login.jsp
    }
    
    // Handle Login form submission
    @PostMapping("/Login")
    public String processLogin(
            @RequestParam String email, 
            @RequestParam String password,
            Model model,
            RedirectAttributes redirectAttributes,
            HttpSession session) {
        
        System.out.println("Login attempt - Email: " + email);
        
        // Basic validation
        if (email == null || email.trim().isEmpty()) {
            model.addAttribute("error", "Email is required");
            return "Login";
        }
        
        if (password == null || password.trim().isEmpty()) {
            model.addAttribute("error", "Password is required");
            return "Login";
        }
        
        // Email format validation
        if (!userService.isValidEmail(email)) {
            model.addAttribute("error", "Please enter a valid email address");
            return "Login";
        }
        
        // Authenticate user
        Optional<User> userOpt = userService.authenticateUser(email, password);
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            
            // Check if email is verified
            if (!user.isEmailVerified()) {
                model.addAttribute("error", "Please verify your email address before logging in. Check your inbox for verification instructions.");
                model.addAttribute("showResendLink", true);
                model.addAttribute("userEmail", email);
                return "Login";
            }
            
            // Check if account is active
            if (!user.isAccountActive()) {
                model.addAttribute("error", "Your account has been deactivated. Please contact support.");
                return "Login";
            }
            
            // Successful Login - Store user in session
            session.setAttribute("loggedInUser", user);
            session.setAttribute("userId", user.getId());
            session.setAttribute("userEmail", user.getEmail());
            session.setAttribute("userName", user.getFullName());
            
            redirectAttributes.addFlashAttribute("success", "Login successful! Welcome back, " + user.getFirstName() + ".");
            return "redirect:/dashboard"; // Redirect to dashboard
        } else {
            // Failed Login
            model.addAttribute("error", "Invalid email or password. Please try again.");
            return "Login";
        }
    }
    
    // Resend verification email
    @PostMapping("/resend-verification")
    public String resendVerificationEmail(
            @RequestParam String email,
            RedirectAttributes redirectAttributes) {
        
        if (userService.resendVerificationEmail(email)) {
            redirectAttributes.addFlashAttribute("success", 
                "Verification email sent! Please check your inbox and spam folder.");
        } else {
            redirectAttributes.addFlashAttribute("error", 
                "Unable to send verification email. The email may already be verified or doesn't exist.");
        }
        
        return "redirect:/Login";
    }
    
    // Logout functionality
    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        // Clear session
        session.invalidate();
        
        redirectAttributes.addFlashAttribute("success", "You have been successfully logged out.");
        return "redirect:/Login";
    }
    
    // Dashboard page (after successful Login)
  /* @GetMapping("/dashboard")
    public String showDashboard(Model model, HttpSession session) {
        // Check if user is logged in
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        
        if (loggedInUser == null) {
            return "redirect:/Login";
        }
        
        // Add user data to model
        model.addAttribute("user", loggedInUser);
        model.addAttribute("message", "Welcome to your dashboard, " + loggedInUser.getFirstName() + "!");
        
        return "dashboard"; //This will resolve to dashboard.jsp
 
    } */
    
    // Forgot password page
    @GetMapping("/forgot-password")
    public String showForgotPasswordPage() {
        return "forgot-password"; // This will resolve to forgot-password.jsp
    }
    
    // Handle forgot password form submission
    @PostMapping("/forgot-password")
    public String processForgotPassword(
            @RequestParam String email,
            Model model,
            RedirectAttributes redirectAttributes) {
        
        if (email == null || email.trim().isEmpty()) {
            model.addAttribute("error", "Email is required");
            return "forgot-password";
        }
        
        if (!userService.isValidEmail(email)) {
            model.addAttribute("error", "Please enter a valid email address");
            return "forgot-password";
        }
        
        if (userService.generatePasswordResetToken(email)) {
            redirectAttributes.addFlashAttribute("success", 
                "Password reset instructions have been sent to your email address.");
        } else {
            model.addAttribute("error", "No account found with this email address.");
            return "forgot-password";
        }
        
        return "redirect:/Login";
    }
    
    // Reset password page
    @GetMapping("/reset-password")
    public String showResetPasswordPage(@RequestParam String token, Model model) {
        // Validate token exists
        if (token == null || token.trim().isEmpty()) {
            model.addAttribute("error", "Invalid reset token.");
            return "Login";
        }
        
        model.addAttribute("token", token);
        return "reset-password"; // This will resolve to reset-password.jsp
    }
    
    // Handle reset password form submission
    @PostMapping("/reset-password")
    public String processResetPassword(
            @RequestParam String token,
            @RequestParam String password,
            @RequestParam String confirmPassword,
            Model model,
            RedirectAttributes redirectAttributes) {
        
        // Validation
        if (password == null || password.trim().isEmpty()) {
            model.addAttribute("error", "Password is required");
            model.addAttribute("token", token);
            return "reset-password";
        }
        
        if (confirmPassword == null || confirmPassword.trim().isEmpty()) {
            model.addAttribute("error", "Password confirmation is required");
            model.addAttribute("token", token);
            return "reset-password";
        }
        
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match");
            model.addAttribute("token", token);
            return "reset-password";
        }
        
        if (!userService.isValidPassword(password)) {
            model.addAttribute("error", "Password must be at least 8 characters long and contain uppercase, lowercase, numbers, and special characters");
            model.addAttribute("token", token);
            return "reset-password";
        }
        
        // Reset password
        if (userService.resetPassword(token, password)) {
            redirectAttributes.addFlashAttribute("success", 
                "Password reset successful! You can now login with your new password.");
            return "redirect:/Login";
        } else {
            model.addAttribute("error", "Invalid or expired reset token. Please request a new password reset.");
            return "Login";
        }
    }
    
    // Email verification handler
    @GetMapping("/verify-email")
    public String verifyEmail(
            @RequestParam String token,
            RedirectAttributes redirectAttributes) {
        
        System.out.println("Email verification attempt with token: " + token);
        
        if (userService.verifyEmail(token)) {
            redirectAttributes.addFlashAttribute("success", 
                "Email verified successfully! You can now sign in to your account.");
            return "redirect:/Login";
        } else {
            redirectAttributes.addFlashAttribute("error", 
                "Invalid or expired verification token. Please request a new verification email.");
            return "redirect:/SignUp";
        }
    }
    
    // Social Login handler (placeholder for future implementation)
    @GetMapping("/social-Login")
    public String handleSocialLogin(
            @RequestParam String provider,
            RedirectAttributes redirectAttributes) {
        
        System.out.println("Social Login attempt with provider: " + provider);
        
        // TODO: Implement social Login logic here
        // For now, just simulate success
        redirectAttributes.addFlashAttribute("info", 
            "Social Login with " + provider + " is not yet implemented.");
        
        return "redirect:/Login";
    }
    
    // Check login status (for AJAX calls)
    @GetMapping("/check-login-status")
    public String checkLoginStatus(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        
        if (loggedInUser != null) {
            model.addAttribute("loggedIn", true);
            model.addAttribute("user", loggedInUser);
        } else {
            model.addAttribute("loggedIn", false);
        }
        
        return "login-status"; // Return JSON response or fragment
    }
}