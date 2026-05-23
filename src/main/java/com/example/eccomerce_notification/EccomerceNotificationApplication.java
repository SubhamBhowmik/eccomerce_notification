package com.example.eccomerce_notification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableKafka
@EnableScheduling
public class EccomerceNotificationApplication {

	public static void main(String[] args) {
		SpringApplication.run(EccomerceNotificationApplication.class, args);
	}

}
