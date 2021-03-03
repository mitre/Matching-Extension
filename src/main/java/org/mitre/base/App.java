package org.mitre.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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


	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
		log.info("started org.mitre.base spring boot application");

		OrderBook nb = new OrderBook();
		log.info("{}", nb);

		MatchingEngine me = new MatchingEngine(nb);
		log.info("{}", me);

		MatchingEngine me2 = new MatchingEngine(nb, "spectrum");
		log.info("{}", me2);

		MatchingEngine me3 = new MatchingEngine(nb, "floods");
		log.info("{}", me3);
	}

}
