/*
 * Copyright (c) 2021 David Bergin
 */
package net.davidbergin.logger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

/**
 * Application entry point when run using SpringBoot.
 */
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(Application.class, args);
		SpringApplication.exit(ctx, () -> new Processor().run(args));
	}
	
}
