package com.example.eccomerce_notification.service;

import com.example.eccomerce_notification.event.NotificationEvent;
import com.resend.Resend;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Slf4j
@Service
public class EmailService {

    private final TemplateEngine templateEngine;

    @Value("${resend.api.key}")
    private String resendApiKey;

    @Value("${app.email.from}")
    private String fromEmail;

    @Value("${app.email.from-name}")
    private String fromName;

    public EmailService(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public void send(NotificationEvent event, String template) throws Exception {
        Context ctx = new Context();
        ctx.setVariable("username", event.getUsername());
        ctx.setVariable("email", event.getUserEmail());
        if (event.getData() != null) {
            event.getData().forEach(ctx::setVariable);
        }
        String html = templateEngine.process("emails/" + template, ctx);
        sendEmail(event.getUserEmail(), getSubject(event), html);
    }

    public void sendBulkCampaign(NotificationEvent event) {
        String subject = (String) event.getData().get("subject");
        String html    = (String) event.getData().get("htmlContent");
        try {
            sendEmail(event.getUserEmail(), subject, html);
        } catch (Exception e) {
            log.error("Campaign email failed: {} | {}",
                    event.getUserEmail(), e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void sendEmail(String to, String subject, String html)
            throws Exception {
        Resend resend = new Resend(resendApiKey);

        CreateEmailOptions params = CreateEmailOptions.builder()
                .from(fromName + " <" + fromEmail + ">")
                .to(to)
                .subject(subject)
                .html(html)
                .build();

        CreateEmailResponse response = resend.emails().send(params);
        log.info("Email sent → {} | id: {}", to, response.getId());
    }

    private String getSubject(NotificationEvent event) {
        return switch (event.getType()) {
            case ORDER_PLACED    -> "Order Confirmed! 🎉";
            case ORDER_SHIPPED   -> "Your order is on the way! 🚚";
            case ORDER_DELIVERED -> "Order Delivered! ⭐ How was it?";
            case ORDER_CANCELLED -> "Your order has been cancelled";
            case WELCOME         -> "Welcome to our store! 🛍️";
            case PASSWORD_RESET  -> "Reset your password";
            case FLASH_SALE,
                 BIG_OFFER       -> "Exclusive offer just for you! 🔥";
            case ABANDONED_CART  -> "You left something behind! 🛒";
            default              -> "Notification from our store";
        };
    }
}
