package com.wolox.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {
		"com.wolox.controller",
		"com.wolox.service",
		"com.wolox.resources",
		"com.wolox.models",
		"com.wolox.config"})
@EnableJpaRepositories("com.wolox.repository")
public class WoloxChallengeApplication {

	public static void main(String[] args) {
		SpringApplication.run(WoloxChallengeApplication.class, args);
	}

}
