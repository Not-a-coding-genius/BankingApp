package com.Visa.My_First_Spring.service;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.Visa.My_First_Spring.entity.OtpToken;
import com.Visa.My_First_Spring.entity.User;
import com.Visa.My_First_Spring.repository.OtpTokenRepository;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {
    
    @Autowired
    private JavaMailSender mailSender;
    
    @Autowired
    private OtpTokenRepository otpTokenRepository;
    
    @Value("${spring.mail.username}")
    private String fromEmail;
    
    @Value("${app.base-url}")
    private String baseUrl;
    
    // Send verification email
    public void sendVerificationEmail(String toEmail, String firstName, String verificationToken) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("Verify Your Email Address");
            
            String verificationUrl = baseUrl + "/verify-email?token=" + verificationToken;
            
            String htmlContent = buildVerificationEmailTemplate(firstName, verificationUrl);
            helper.setText(htmlContent, true);
            
            mailSender.send(message);
            System.out.println("Verification email sent to: " + toEmail);
            
        } catch (MessagingException e) {
            System.err.println("Failed to send verification email: " + e.getMessage());
            // Fallback to simple text email
            sendSimpleVerificationEmail(toEmail, firstName, verificationToken);
        }
    }
    
    // Send password reset email
    public void sendPasswordResetEmail(String toEmail, String firstName, String resetToken) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("Reset Your Password");
            
            String resetUrl = baseUrl + "/reset-password?token=" + resetToken;
            
            String htmlContent = buildPasswordResetEmailTemplate(firstName, resetUrl);
            helper.setText(htmlContent, true);
            
            mailSender.send(message);
            System.out.println("Password reset email sent to: " + toEmail);
            
        } catch (MessagingException e) {
            System.err.println("Failed to send password reset email: " + e.getMessage());
            // Fallback to simple text email
            sendSimplePasswordResetEmail(toEmail, firstName, resetToken);
        }
    }
    
    // Send welcome email after successful verification
    public void sendWelcomeEmail(String toEmail, String firstName) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("Welcome to Our Platform!");
            
            String htmlContent = buildWelcomeEmailTemplate(firstName);
            helper.setText(htmlContent, true);
            
            mailSender.send(message);
            System.out.println("Welcome email sent to: " + toEmail);
            
        } catch (MessagingException e) {
            System.err.println("Failed to send welcome email: " + e.getMessage());
        }
    }
    
    // Fallback simple verification email
    private void sendSimpleVerificationEmail(String toEmail, String firstName, String verificationToken) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Verify Your Email Address");
            
            String verificationUrl = baseUrl + "/verify-email?token=" + verificationToken;
            String text = "Dear " + firstName + ",\n\n" +
                         "Thank you for registering with us! Please click the link below to verify your email address:\n\n" +
                         verificationUrl + "\n\n" +
                         "This link will expire in 24 hours.\n\n" +
                         "If you didn't create this account, please ignore this email.\n\n" +
                         "Best regards,\nThe Team";
            
            message.setText(text);
            mailSender.send(message);
            
        } catch (Exception e) {
            System.err.println("Failed to send simple verification email: " + e.getMessage());
        }
    }
    
    // Fallback simple password reset email
    private void sendSimplePasswordResetEmail(String toEmail, String firstName, String resetToken) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Reset Your Password");
            
            String resetUrl = baseUrl + "/reset-password?token=" + resetToken;
            String text = "Dear " + firstName + ",\n\n" +
                         "You requested to reset your password. Please click the link below to reset it:\n\n" +
                         resetUrl + "\n\n" +
                         "This link will expire in 1 hour.\n\n" +
                         "If you didn't request this, please ignore this email.\n\n" +
                         "Best regards,\nThe Team";
            
            message.setText(text);
            mailSender.send(message);
            
        } catch (Exception e) {
            System.err.println("Failed to send simple password reset email: " + e.getMessage());
        }
    }
    
    // Build HTML template for verification email
    private String buildVerificationEmailTemplate(String firstName, String verificationUrl) {
        return "<!DOCTYPE html>" +
               "<html>" +
               "<head>" +
               "<meta charset='UTF-8'>" +
               "<style>" +
               "body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }" +
               ".container { max-width: 600px; margin: 0 auto; padding: 20px; }" +
               ".header { background-color: #007bff; color: white; padding: 20px; text-align: center; }" +
               ".content { padding: 30px 20px; }" +
               ".button { display: inline-block; padding: 12px 30px; background-color: #28a745; color: white; text-decoration: none; border-radius: 5px; font-weight: bold; }" +
               ".footer { background-color: #f8f9fa; padding: 20px; text-align: center; font-size: 12px; color: #666; }" +
               "</style>" +
               "</head>" +
               "<body>" +
               "<div class='container'>" +
               "<div class='header'>" +
               "<h1>Welcome to Our Platform!</h1>" +
               "</div>" +
               "<div class='content'>" +
               "<h2>Hi " + firstName + ",</h2>" +
               "<p>Thank you for registering with us! We're excited to have you on board.</p>" +
               "<p>To complete your registration, please verify your email address by clicking the button below:</p>" +
               "<p style='text-align: center; margin: 30px 0;'>" +
               "<a href='" + verificationUrl + "' class='button'>Verify Email Address</a>" +
               "</p>" +
               "<p>Or copy and paste this link into your browser:</p>" +
               "<p style='word-break: break-all; background-color: #f8f9fa; padding: 10px; border-radius: 3px;'>" +
               verificationUrl +
               "</p>" +
               "<p><strong>Important:</strong> This verification link will expire in 24 hours.</p>" +
               "<p>If you didn't create this account, please ignore this email.</p>" +
               "</div>" +
               "<div class='footer'>" +
               "<p>This is an automated message, please do not reply to this email.</p>" +
               "</div>" +
               "</div>" +
               "</body>" +
               "</html>";
    }
    
    // Build HTML template for password reset email
    private String buildPasswordResetEmailTemplate(String firstName, String resetUrl) {
        return "<!DOCTYPE html>" +
               "<html>" +
               "<head>" +
               "<meta charset='UTF-8'>" +
               "<style>" +
               "body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }" +
               ".container { max-width: 600px; margin: 0 auto; padding: 20px; }" +
               ".header { background-color: #dc3545; color: white; padding: 20px; text-align: center; }" +
               ".content { padding: 30px 20px; }" +
               ".button { display: inline-block; padding: 12px 30px; background-color: #dc3545; color: white; text-decoration: none; border-radius: 5px; font-weight: bold; }" +
               ".footer { background-color: #f8f9fa; padding: 20px; text-align: center; font-size: 12px; color: #666; }" +
               "</style>" +
               "</head>" +
               "<body>" +
               "<div class='container'>" +
               "<div class='header'>" +
               "<h1>Password Reset Request</h1>" +
               "</div>" +
               "<div class='content'>" +
               "<h2>Hi " + firstName + ",</h2>" +
               "<p>You requested to reset your password. No worries, it happens to everyone!</p>" +
               "<p>Click the button below to reset your password:</p>" +
               "<p style='text-align: center; margin: 30px 0;'>" +
               "<a href='" + resetUrl + "' class='button'>Reset Password</a>" +
               "</p>" +
               "<p>Or copy and paste this link into your browser:</p>" +
               "<p style='word-break: break-all; background-color: #f8f9fa; padding: 10px; border-radius: 3px;'>" +
               resetUrl +
               "</p>" +
               "<p><strong>Important:</strong> This reset link will expire in 1 hour for security reasons.</p>" +
               "<p>If you didn't request this password reset, please ignore this email or contact support if you have concerns.</p>" +
               "</div>" +
               "<div class='footer'>" +
               "<p>This is an automated message, please do not reply to this email.</p>" +
               "</div>" +
               "</div>" +
               "</body>" +
               "</html>";
    }
    
    // Build HTML template for welcome email
    private String buildWelcomeEmailTemplate(String firstName) {
        return "<!DOCTYPE html>" +
               "<html>" +
               "<head>" +
               "<meta charset='UTF-8'>" +
               "<style>" +
               "body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }" +
               ".container { max-width: 600px; margin: 0 auto; padding: 20px; }" +
               ".header { background-color: #28a745; color: white; padding: 20px; text-align: center; }" +
               ".content { padding: 30px 20px; }" +
               ".button { display: inline-block; padding: 12px 30px; background-color: #007bff; color: white; text-decoration: none; border-radius: 5px; font-weight: bold; }" +
               ".footer { background-color: #f8f9fa; padding: 20px; text-align: center; font-size: 12px; color: #666; }" +
               "</style>" +
               "</head>" +
               "<body>" +
               "<div class='container'>" +
               "<div class='header'>" +
               "<h1>Welcome Aboard!</h1>" +
               "</div>" +
               "<div class='content'>" +
               "<h2>Hi " + firstName + ",</h2>" +
               "<p>Congratulations! Your email has been successfully verified and your account is now active.</p>" +
               "<p>You can now access all the features of our platform. Here's what you can do:</p>" +
               "<ul>" +
               "<li>Access your personalized dashboard</li>" +
               "<li>Update your profile settings</li>" +
               "<li>Explore all available features</li>" +
               "</ul>" +
               "<p style='text-align: center; margin: 30px 0;'>" +
               "<a href='" + baseUrl + "/dashboard' class='button'>Go to Dashboard</a>" +
               "</p>" +
               "<p>If you have any questions or need assistance, feel free to contact our support team.</p>" +
               "<p>Thank you for joining us!</p>" +
               "</div>" +
               "<div class='footer'>" +
               "<p>This is an automated message, please do not reply to this email.</p>" +
               "</div>" +
               "</div>" +
               "</body>" +
               "</html>";
    }
    
    // Generates OTP, saves it and sends OTP email
    public void generateAndSendOtp(User user) {
        String otp = generateOtp();
        LocalDateTime expiry = LocalDateTime.now().plusMinutes(10);

        // Save or update OTP token in DB
        OtpToken otpToken = otpTokenRepository.findByUser(user)
                .orElse(new OtpToken());
        otpToken.setUser(user);
        otpToken.setOtp(otp);
        otpToken.setExpiryDate(expiry);
        otpTokenRepository.save(otpToken);

        // Send OTP email
        sendOtpEmail(user.getEmail(), user.getFirstName(), otp);
    }

    private String generateOtp() {
        Random random = new Random();
        int number = 100000 + random.nextInt(900000);
        return String.valueOf(number);
    }
    
    public void sendOtpEmail(String toEmail, String firstName, String otp) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("Your One-Time Password (OTP)");

            String htmlContent = buildOtpEmailTemplate(firstName, otp);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            System.out.println("OTP email sent to: " + toEmail);

        } catch (MessagingException e) {
            System.err.println("Failed to send OTP email: " + e.getMessage());
            // Fallback to simple text email
            sendSimpleOtpEmail(toEmail, firstName, otp);
        }
    }

    private void sendSimpleOtpEmail(String toEmail, String firstName, String otp) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Your One-Time Password (OTP)");

            String text = "Dear " + firstName + ",\n\n" +
                          "Your OTP for completing the transaction is: " + otp + "\n\n" +
                          "If you did not request this, please contact support immediately.\n\n" +
                          "Best regards,\nThe Team";

            message.setText(text);
            mailSender.send(message);

        } catch (Exception e) {
            System.err.println("Failed to send simple OTP email: " + e.getMessage());
        }
    }

    private String buildOtpEmailTemplate(String firstName, String otp) {
        return "<!DOCTYPE html>" +
               "<html>" +
               "<head>" +
               "<meta charset='UTF-8'>" +
               "<style>" +
               "body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }" +
               ".container { max-width: 600px; margin: 0 auto; padding: 20px; }" +
               ".header { background-color: #007bff; color: white; padding: 20px; text-align: center; }" +
               ".content { padding: 30px 20px; font-size: 16px; }" +
               ".otp { font-size: 24px; font-weight: bold; color: #28a745; }" +
               ".footer { background-color: #f8f9fa; padding: 20px; text-align: center; font-size: 12px; color: #666; }" +
               "</style>" +
               "</head>" +
               "<body>" +
               "<div class='container'>" +
               "<div class='header'>" +
               "<h1>Transaction OTP</h1>" +
               "</div>" +
               "<div class='content'>" +
               "<p>Hi " + firstName + ",</p>" +
               "<p>To complete your transaction, please use the following One-Time Password (OTP):</p>" +
               "<p class='otp'>" + otp + "</p>" +
               "<p>This OTP is valid for 10 minutes.</p>" +
               "<p>If you did not initiate this transaction, please contact support immediately.</p>" +
               "</div>" +
               "<div class='footer'>" +
               "<p>This is an automated message, please do not reply to this email.</p>" +
               "</div>" +
               "</div>" +
               "</body>" +
               "</html>";
    }
    
}