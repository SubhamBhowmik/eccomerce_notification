package com.example.eccomerce_notification.service;

import com.example.eccomerce_notification.event.NotificationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationRouter {

    private final EmailService emailService;


    public void route(NotificationEvent event) throws Exception {
        switch (event.getType()) {
            case ORDER_PLACED    -> emailService.send(event, "order-placed");
            case ORDER_SHIPPED   -> emailService.send(event, "order-shipped");
            case ORDER_DELIVERED -> emailService.send(event, "order-delivered");
            case ORDER_CANCELLED -> emailService.send(event, "order-cancelled");
            case WELCOME         -> emailService.send(event, "welcome");
            case PASSWORD_RESET  -> emailService.send(event, "password-reset");
            case FLASH_SALE,
                 BIG_OFFER       -> emailService.send(event, "big-offer");
            case ABANDONED_CART  -> emailService.send(event, "abandoned-cart");
            case CAMPAIGN_BLAST  -> emailService.sendBulkCampaign(event);
            case OTP -> emailService.send(event, "otp");
            default -> throw new RuntimeException("Unknown type: " + event.getType());
        }
    }
}
