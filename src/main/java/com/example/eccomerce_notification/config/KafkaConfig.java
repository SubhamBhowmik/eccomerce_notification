package com.example.eccomerce_notification.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic orderEventsTopic() {
        return TopicBuilder.name("order-events")
                .partitions(3).replicas(3).build();
    }

    @Bean
    public NewTopic authEventsTopic() {
        return TopicBuilder.name("auth-events")
                .partitions(1).replicas(3).build();
    }

    @Bean
    public NewTopic offerEventsTopic() {
        return TopicBuilder.name("offer-events")
                .partitions(3).replicas(3).build();
    }

    @Bean
    public NewTopic campaignEventsTopic() {
        return TopicBuilder.name("campaign-events")
                .partitions(3).replicas(3).build();
    }

    @Bean
    public NewTopic dlqTopic() {
        return TopicBuilder.name("notifications-dlq")
                .partitions(1).replicas(1).build();
    }
}