package com.example.eccomerce_notification.repository;

import com.example.eccomerce_notification.model.NotificationLog;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface NotificationLogRepository
        extends MongoRepository<NotificationLog, String> {
    Optional<NotificationLog> findByEventId(String eventId);
}