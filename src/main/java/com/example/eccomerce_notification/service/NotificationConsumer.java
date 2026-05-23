package com.example.eccomerce_notification.service;

import com.example.eccomerce_notification.event.NotificationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationConsumer {

    private final NotificationRouter router;
    private final NotificationLogService logService;

    @RetryableTopic(
            attempts = "3",
            backoff = @Backoff(delay = 2000, multiplier = 2)
    )
    @KafkaListener(
            topics = {"order-events", "auth-events", "offer-events"},
            groupId = "notification-group"
    )
    public void consume(NotificationEvent event) {
        log.info("Received: {} for: {}", event.getType(), event.getUserEmail());
        try {
            router.route(event);
            logService.logSuccess(event);
        } catch (Exception e) {
            logService.logFailure(event, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @RetryableTopic(
            attempts = "3",
            backoff = @Backoff(delay = 5000, multiplier = 2)
    )
    @KafkaListener(
            topics = "campaign-events",
            groupId = "campaign-group"
    )
    public void consumeCampaign(NotificationEvent event) {
        log.info("Campaign for: {}", event.getUserEmail());
        try {
            router.route(event);
            logService.logSuccess(event);
        } catch (Exception e) {
            logService.logFailure(event, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @KafkaListener(
            topics = "notifications-dlq",
            groupId = "dlq-group"
    )
    public void consumeDlq(NotificationEvent event) {
        log.error("DEAD: {} for {}", event.getType(), event.getUserEmail());
        logService.logDead(event);
    }
}
