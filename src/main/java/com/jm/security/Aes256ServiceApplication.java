package com.jm.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.jm.security.api"})
public class Aes256ServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(Aes256ServiceApplication.class, args);
	}
}
