package com.wolox.app;

import com.wolox.service.SyncService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	private static Logger logger = LoggerFactory.getLogger(WoloxChallengeApplication.class);

	public static void main(String[] args) {

        ApplicationContext context = SpringApplication.run(WoloxChallengeApplication.class, args);
        SyncService syncService = context.getBean(SyncService.class);
        logger.info("Starting process for Synchronize data from external api");
        try {
        	logger.info("Synchronize Users");
            syncService.synchronizeUsers();

			logger.info("Synchronize Albums");
            syncService.synchronizeAlbums();

			logger.info("Synchronize Photos");
            syncService.synchronizePhotos();
        } catch (IOException | InterruptedException e) {
            logger.error("The Synchronize process has failed, the app is shutdown");
            SpringApplication.exit(context);
        }
        logger.info("The process of synchronize data has finished successfully");
        logger.info("The api is ready for receive requests");
    }

}
