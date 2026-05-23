package com.example.eccomerce_notification.service;

import com.example.eccomerce_notification.event.NotificationEvent;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.springframework.mail.javamail.MimeMessageHelper;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${app.email.from}") private String fromEmail;
    @Value("${app.email.from-name}") private String fromName;

    public void send(NotificationEvent event, String template) throws Exception {
        Context ctx = new Context();
        ctx.setVariable("username", event.getUsername());
        ctx.setVariable("email", event.getUserEmail());
        if (event.getData() != null) {
            event.getData().forEach(ctx::setVariable);
        }
        String html = templateEngine.process("emails/" + template, ctx);
        sendMime(event.getUserEmail(), getSubject(event), html);
    }

    public void sendBulkCampaign(NotificationEvent event) {
        String subject = (String) event.getData().get("subject");
        String html = (String) event.getData().get("htmlContent");
        try {
            sendMime(event.getUserEmail(), subject, html);
        } catch (Exception e) {
            log.error("Campaign email failed: {} | {}", event.getUserEmail(), e.getMessage());
            throw new RuntimeException(e);
        }
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

    private void sendMime(String to, String subject, String html) throws Exception {
        MimeMessage msg = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg, true, "UTF-8");
        helper.setFrom(fromEmail, fromName);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(html, true);
        mailSender.send(msg);
        log.info("Email sent → {}", to);
    }
}
