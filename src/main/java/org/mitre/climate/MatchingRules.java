package org.mitre.climate;

import org.mitre.base.CompletedOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MatchingRules {

	// logger
	private static final Logger log = LoggerFactory.getLogger(MatchingRules.class);


	public MatchingRules() {
		// use time-order priority matching method here
		log.info("Constructed default MatchingRules");
	}

	/*
	 * @param:
	 * @param:
	 *
	 * @return: Completed Order object
	 *
	 *  - mutates: buy and sell OrderBook
	 *
	 */
	public CompletedOrder matchUpdate() {
		return null;

	}
}
