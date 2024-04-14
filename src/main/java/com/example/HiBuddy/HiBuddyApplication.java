package com.example.HiBuddy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class HiBuddyApplication {

	public static void main(String[] args) {
		SpringApplication.run(HiBuddyApplication.class, args);
	}

}
