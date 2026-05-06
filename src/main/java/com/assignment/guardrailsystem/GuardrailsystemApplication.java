package com.assignment.guardrailsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GuardrailsystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(GuardrailsystemApplication.class, args);
	}

}
