/**
 *
 */
package org.mitre.base;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;


/**
 *
 *
 */
@Component
public class OrderBook {

	// logger
	private static final Logger log = LoggerFactory.getLogger(OrderBook.class);

	// order books
	private static HashMap<Integer, Order> buyBook;
	private static HashMap<Integer, Order> sellBook;

	// rule set
	private static String ruleSet;


	// default constructor
	public OrderBook() {
		setRuleSet("");
		initBooks();
		log.info("OrderBook instantiated, NO rule set");
	}

	// custom constructor
	public OrderBook(String rule) {
		// TODO: insert rule set constructor here
		setRuleSet(rule);
		initBooks();
		log.info("OrderBook instantiated, chosen rule set: {}", rule);
	}


	/**
	 * initialize buyBook and sellBook
	 */
	public static void initBooks() {
		OrderBook.buyBook = Maps.newHashMap();
		OrderBook.sellBook = Maps.newHashMap();
	}

	/**
	 * @return the buyBook
	 */
	public Map<Integer, Order> getBuyBook() {
		return buyBook;
	}

	/**
	 * @return the sellBook
	 */
	public Map<Integer, Order> getSellBook() {
		return sellBook;
	}

	/**
	 * @return both buy and sell books
	 */
	public Map<String, Map<Integer, Order>> getBooks() {
		Map<String, Map<Integer, Order>> ret = new HashMap<>();
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
	public static void setRuleSet(String ruleSet) {
		OrderBook.ruleSet = ruleSet;
	}

	/**
	 * @return string representation of OrderBook
	 */
	@Override
	public String toString() {
		return "OrderBook with RULE=" + getRuleSet() + " SIZE(buyBook)="
					+ getBuyBook().size() + " SIZE(sellBook)="
					+ getSellBook().size();
	}
}
