package com.example.eccomerce_notification.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "notification_logs")
public class NotificationLog {
    @Id
    private String id;
    private String eventId;
    private String userId;
    private String email;
    private String type;
    private String status; // SENT, FAILED, DEAD
    private int retryCount = 0;
    private String errorMessage;
    private LocalDateTime sentAt;
    private LocalDateTime createdAt = LocalDateTime.now();
}