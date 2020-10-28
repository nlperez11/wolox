package com.wolox.app;

import com.wolox.service.SyncService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.io.IOException;

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

		ApplicationContext context = SpringApplication.run(WoloxChallengeApplication.class, args);
		SyncService syncService = context.getBean(SyncService.class);
		try {
			syncService.synchronizeUsers();
			syncService.synchronizeAlbums();
			syncService.synchronizePhotos();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			SpringApplication.exit(context);
		}

	}

}
