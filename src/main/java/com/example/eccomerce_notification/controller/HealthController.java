package com.example.eccomerce_notification.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;
@RestController
@RequestMapping("/api/notification")
@RequiredArgsConstructor
public class HealthController {
    @Autowired
    private JavaMailSender mailSender;

    @Value("${app.email.from}")
    private String fromEmail;

    @GetMapping("/test-email")
    public ResponseEntity<?> testEmail() {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo("czsubham@gmail.com");
            message.setSubject("Test Email from Notification Service");
            message.setText("Email is working!");
            mailSender.send(message);
            return ResponseEntity.ok(Map.of("message", "Email sent successfully ✅"));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // ✅ Ping endpoint
    @GetMapping("/ping")
    public ResponseEntity<?> ping() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "service", "eccomerce-notification",
                "timestamp", LocalDateTime.now().toString()
        ));
    }
}
