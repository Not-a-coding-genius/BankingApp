package com.Visa.My_First_Spring;


import com.Visa.My_First_Spring.entity.User;
import com.Visa.My_First_Spring.repository.UserRepository;
import com.Visa.My_First_Spring.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/otp")
public class OtpController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    // Endpoint to send OTP email to user by email
    @PostMapping("/send")
    public ResponseEntity<String> sendOtp(@RequestParam String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }

        User user = userOpt.get();

        // Generate OTP and send email
        emailService.generateAndSendOtp(user);

        return ResponseEntity.ok("OTP sent to " + email);
    }
}

