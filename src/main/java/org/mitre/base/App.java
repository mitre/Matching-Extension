package org.mitre.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 *
 *
 */
@SpringBootApplication
public class App
{

	private static final Logger log = LoggerFactory.getLogger(App.class);

	@Autowired
	private static OrderBook nb;

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
		log.info("started org.mitre.base spring boot application");

		nb = new OrderBook();
		log.info("{}", nb);
	}

}
