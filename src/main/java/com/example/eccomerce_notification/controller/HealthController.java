package com.example.eccomerce_notification.controller;

import com.resend.Resend;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;
@Slf4j
@RestController
@RequestMapping("/api/notification")
@RequiredArgsConstructor
public class HealthController {
    @Value("${resend.api.key}")
    private String resendApiKey;

    @GetMapping("/ping")
    public ResponseEntity<?> ping() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "service", "eccomerce-notification",
                "timestamp", LocalDateTime.now().toString()
        ));
    }

    @GetMapping("/test-email")
    public ResponseEntity<?> testEmail() {
        try {
            Resend resend = new Resend(resendApiKey);

            CreateEmailOptions params = CreateEmailOptions.builder()
                    .from("My Ecommerce Store <onboarding@resend.dev>")
                    .to("czsubham@gmail.com")
                    .subject("Test Email ✅")
                    .html("<h1>Email is working!</h1><p>Resend is configured correctly.</p>")
                    .build();

            CreateEmailResponse response = resend.emails().send(params);
            log.info("Test email sent: {}", response.getId());

            return ResponseEntity.ok(Map.of(
                    "message", "Email sent successfully ✅",
                    "id", response.getId()
            ));
        } catch (Exception e) {
            log.error("Test email failed: {}", e.getMessage());
            return ResponseEntity.status(500)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
