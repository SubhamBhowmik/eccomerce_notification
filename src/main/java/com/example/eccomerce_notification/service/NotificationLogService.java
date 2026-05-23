package com.example.eccomerce_notification.service;



import com.example.eccomerce_notification.event.NotificationEvent;
import com.example.eccomerce_notification.model.NotificationLog;
import com.example.eccomerce_notification.repository.NotificationLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationLogService {

    private final NotificationLogRepository repository;

    public void logSuccess(NotificationEvent event) {
        NotificationLog log = new NotificationLog();
        log.setEventId(event.getEventId());
        log.setUserId(event.getUserId());
        log.setEmail(event.getUserEmail());
        log.setType(event.getType().name());
        log.setStatus("SENT");
        log.setSentAt(LocalDateTime.now());
        repository.save(log);
    }

    public void logFailure(NotificationEvent event, String error) {
        NotificationLog log = new NotificationLog();
        log.setEventId(event.getEventId());
        log.setUserId(event.getUserId());
        log.setEmail(event.getUserEmail());
        log.setType(event.getType().name());
        log.setStatus("FAILED");
        log.setErrorMessage(error);
        repository.save(log);
    }

    public void logDead(NotificationEvent event) {
        repository.findByEventId(event.getEventId())
                .ifPresent(l -> {
                    l.setStatus("DEAD");
                    l.setRetryCount(3);
                    repository.save(l);
                });
    }
}