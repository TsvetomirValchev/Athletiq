package com.valchev.athletiq.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    @Autowired
    private final JavaMailSender emailSender;

    @Value("${app.web.reset-url}")
    private String webResetUrl;

    @Value("${app.mobile.reset-scheme}")
    private String mobileResetScheme;

    public void sendPasswordResetEmail(String recipient, String token, String clientType) {
        String resetUrl;

        boolean isMobileRequest = "mobile".equalsIgnoreCase(clientType);

        if (isMobileRequest) {
            resetUrl = mobileResetScheme + "://reset-password?token=" + token;
        } else {
            resetUrl = webResetUrl + "?token=" + token;
        }


        SimpleMailMessage message = createSimpleMailMessage(recipient, resetUrl);

        try {
            emailSender.send(message);
            log.info("Email sent successfully to {}", recipient);
        } catch (Exception e) {
            log.error("Failed to send email: {}", e.getMessage(), e);
        }
    }

    private SimpleMailMessage createSimpleMailMessage(String recipient, String resetUrl) {
        String htmlContent = "Reset your password by clicking the following link: " + resetUrl;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("athletiq50@gmail.com");
        message.setTo(recipient);
        message.setSubject("Password Reset Request");
        message.setText(htmlContent);
        return message;
    }
}
