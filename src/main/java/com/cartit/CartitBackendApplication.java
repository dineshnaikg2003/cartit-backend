package com.cartit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class CartitBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(CartitBackendApplication.class, args);
	}

}
