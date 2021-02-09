/**
 *
 */
package org.mitre.base;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


/**
 *
 *
 */
@Component
public class OrderBook {

	// logger
	private static final Logger log = LoggerFactory.getLogger(OrderBook.class);

	// order books
	private HashMap<String, Order> buyBook;
	private HashMap<String, Order> sellBook;

	// rule set
	private String ruleSet;


	// default constructor
	public OrderBook() {
		setRuleSet("");
		log.info("OrderBook instantiated, NO rule set");
	}

	// custom constructor
	public OrderBook(String rule) {
		// TODO: insert rule set constructor here
		setRuleSet(rule);
		log.info("OrderBook instantiated, chosen rule set: {}", this.ruleSet);
	}


	/**
	 * @return the buyBook
	 */
	public Map<String, Order> getBuyBook() {
		return buyBook;
	}


	/**
	 * @return the sellBook
	 */
	public Map<String, Order> getSellBook() {
		return sellBook;
	}

	/**
	 * @return both buy and sell books
	 */
	public Map<String, Map<String, Order>> getBooks() {
		Map<String, Map<String, Order>> ret = new HashMap<>();
		ret.put("BUY", getBuyBook());
		ret.put("SELL", getSellBook());
		return ret;
	}


	/**
	 * @return the ruleSet
	 */
	public String getRuleSet() {
		return ruleSet;
	}


	/**
	 * @param ruleSet the ruleSet to set
	 */
	public void setRuleSet(String ruleSet) {
		this.ruleSet = ruleSet;
	}

}
