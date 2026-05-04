package com.tutorial.bootdemo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

@Slf4j
@SpringBootApplication
public class BootDemoApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(BootDemoApplication.class);
		app.addListeners((org.springframework.boot.context.event.ApplicationReadyEvent event) -> {
			Environment env = event.getApplicationContext().getEnvironment();
			log.info("BootDemoApplication started successfully");
			log.info("Server port: {}", env.getProperty("server.port", "8080"));
			log.info("Active profiles: {}", (Object) env.getActiveProfiles());
		});
		app.run(args);
	}

}
