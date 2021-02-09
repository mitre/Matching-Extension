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

	private static final Logger logger = LoggerFactory.getLogger(App.class);

	@Autowired
	private OrderBook nb;

	public void findSell() {
		// do something
		nb.getBooks();
	}
	
	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
		logger.info("started org.mitre.base spring boot application");

		//findSell();
	}

}
