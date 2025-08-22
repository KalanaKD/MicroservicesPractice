package com.kd.aiservices;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRabbit
public class AiservicesApplication {

	public static void main(String[] args) {
		SpringApplication.run(AiservicesApplication.class, args);
	}

}
