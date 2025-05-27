package com.Visa.My_First_Spring.service;

import com.Visa.My_First_Spring.entity.User;
import com.Visa.My_First_Spring.entity.VerificationToken;
import com.Visa.My_First_Spring.repository.UserRepository;
import com.Visa.My_First_Spring.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private VerificationTokenRepository verificationTokenRepository;
    
    @Autowired
    private EmailService emailService;
    
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    // User Registration
 // Add this enhanced logging to your UserService.registerUser method
    public boolean registerUser(String firstName, String lastName, String email, 
                               String phone, String password, boolean newsletterSubscription) {
        try {
            System.out.println("=== Starting user registration process ===");
            System.out.println("Email: " + email);
            
            // Check if user already exists
            System.out.println("Checking if user exists...");
            if (userRepository.existsByEmail(email)) {
                System.out.println("User already exists with email: " + email);
                return false;
            }
            System.out.println("User does not exist, proceeding with registration");
            
            // Hash password
            System.out.println("Hashing password...");
            String hashedPassword = passwordEncoder.encode(password);
            System.out.println("Password hashed successfully");
            
            // Create new user
            System.out.println("Creating new user object...");
            User newUser = new User(firstName, lastName, email, phone, hashedPassword, newsletterSubscription);
            System.out.println("User object created: " + newUser.getEmail());
            
            // Save user to database
            System.out.println("Attempting to save user to database...");
            User savedUser = userRepository.save(newUser);
            System.out.println("User saved successfully with ID: " + savedUser.getId());
            
            // Generate verification token
            System.out.println("Generating verification token...");
            String verificationToken = generateVerificationToken(email, VerificationToken.TokenType.EMAIL_VERIFICATION);
            System.out.println("Verification token generated: " + verificationToken);
            
            // Send verification email
            System.out.println("Attempting to send verification email...");
            emailService.sendVerificationEmail(email, firstName, verificationToken);
            System.out.println("Verification email sent successfully");
            
            System.out.println("=== User registration completed successfully ===");
            return true;
            
        } catch (Exception e) {
            System.err.println("=== ERROR in user registration ===");
            System.err.println("Error message: " + e.getMessage());
            System.err.println("Error class: " + e.getClass().getSimpleName());
            e.printStackTrace(); // This will show the full stack trace
            return false;
        }
    }
    
    // User Authentication
    public Optional<User> authenticateUser(String email, String rawPassword) {
        Optional<User> userOpt = userRepository.findActiveUserByEmail(email);
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (passwordEncoder.matches(rawPassword, user.getPassword())) {
                // Update last login
                user.setLastLogin(LocalDateTime.now());
                userRepository.save(user);
                return Optional.of(user);
            }
        }
        
        return Optional.empty();
    }
    
    // Check if user exists
    public boolean userExists(String email) {
        return userRepository.existsByEmail(email);
    }
    
    // Get user by email
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    // Email Verification
    public boolean verifyEmail(String token) {
        try {
            Optional<VerificationToken> tokenOpt = verificationTokenRepository.findValidToken(token, LocalDateTime.now());
            
            if (tokenOpt.isPresent()) {
                VerificationToken verificationToken = tokenOpt.get();
                
                // Verify user email
                userRepository.verifyUserByEmail(verificationToken.getEmail());
                
                // Mark token as used
                verificationTokenRepository.markTokenAsUsed(token, LocalDateTime.now());
                
                return true;
            }
            
            return false;
        } catch (Exception e) {
            System.err.println("Error verifying email: " + e.getMessage());
            return false;
        }
    }
    
    // Resend Verification Email
    public boolean resendVerificationEmail(String email) {
        try {
            Optional<User> userOpt = userRepository.findByEmail(email);
            
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                
                // Don't send if already verified
                if (user.isEmailVerified()) {
                    return false;
                }
                
                // Generate new verification token
                String verificationToken = generateVerificationToken(email, VerificationToken.TokenType.EMAIL_VERIFICATION);
                emailService.sendVerificationEmail(email, user.getFirstName(), verificationToken);
                
                return true;
            }
            
            return false;
        } catch (Exception e) {
            System.err.println("Error resending verification email: " + e.getMessage());
            return false;
        }
    }
    
    // Generate Password Reset Token
    public boolean generatePasswordResetToken(String email) {
        try {
            if (userRepository.existsByEmail(email)) {
                String resetToken = generateVerificationToken(email, VerificationToken.TokenType.PASSWORD_RESET);
                Optional<User> userOpt = userRepository.findByEmail(email);
                
                if (userOpt.isPresent()) {
                    emailService.sendPasswordResetEmail(email, userOpt.get().getFirstName(), resetToken);
                    return true;
                }
            }
            
            return false;
        } catch (Exception e) {
            System.err.println("Error generating password reset token: " + e.getMessage());
            return false;
        }
    }
    
    // Reset Password
    public boolean resetPassword(String token, String newPassword) {
        try {
            Optional<VerificationToken> tokenOpt = verificationTokenRepository.findValidToken(token, LocalDateTime.now());
            
            if (tokenOpt.isPresent()) {
                VerificationToken verificationToken = tokenOpt.get();
                
                if (verificationToken.getTokenType() == VerificationToken.TokenType.PASSWORD_RESET) {
                    // Hash new password
                    String hashedPassword = passwordEncoder.encode(newPassword);
                    
                    // Update user password
                    userRepository.updatePasswordByEmail(verificationToken.getEmail(), hashedPassword);
                    
                    // Mark token as used
                    verificationTokenRepository.markTokenAsUsed(token, LocalDateTime.now());
                    
                    return true;
                }
            }
            
            return false;
        } catch (Exception e) {
            System.err.println("Error resetting password: " + e.getMessage());
            return false;
        }
    }
    
    // Generate Verification Token
    private String generateVerificationToken(String email, VerificationToken.TokenType tokenType) {
        // Generate unique token
        String token = UUID.randomUUID().toString();
        
        // Set expiration time based on token type
        int expirationHours = (tokenType == VerificationToken.TokenType.EMAIL_VERIFICATION) ? 24 : 1;
        
        // Save token to database
        VerificationToken verificationToken = new VerificationToken(token, email, tokenType, expirationHours);
        verificationTokenRepository.save(verificationToken);
        
        return token;
    }
    
    // Validation Methods
    public boolean isValidEmail(String email) {
    	System.out.println("Function Email!");
        return email != null && email.matches("^[\\w\\.-]+@[\\w\\.-]+\\.[a-zA-Z]{2,}$");
    }
    
    public boolean isValidPhone(String phone) {
        String cleanPhone = phone.replaceAll("[^0-9]", "");
        return cleanPhone.length() >= 10 && cleanPhone.length() <= 15;
    }
    
    public boolean isValidPassword(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }
        
        return password.matches(".*[A-Z].*") && // Uppercase letter
               password.matches(".*[a-z].*") && // Lowercase letter
               password.matches(".*[0-9].*") && // Digit
               password.matches(".*[^A-Za-z0-9].*"); // Special character
    }
    
    // Cleanup expired tokens (scheduled task)
    public void cleanupExpiredTokens() {
        verificationTokenRepository.deleteExpiredTokens(LocalDateTime.now());
        verificationTokenRepository.deleteOldUsedTokens(LocalDateTime.now().minusDays(7));
    }
}