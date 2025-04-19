package com.valchev.athletiq.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    @Autowired
    private JavaMailSender emailSender;

    public void sendPasswordResetEmail(String to, String token) {
        String resetUrl = "http://localhost:8100/reset-password?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("athletiq50@gmail.com");
        message.setTo(to);
        message.setSubject("Password Reset Request");
        message.setText("Please click on the link below to reset your password:\n\n" + resetUrl);

        try {
            emailSender.send(message);
            log.info("Email sent successfully {}, to {}", message, to);
        } catch (Exception e) {

            log.info("Failed to send email: {}", e.getMessage());
        }
    }
}
