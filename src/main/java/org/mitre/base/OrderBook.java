/**
 *
 */
package org.mitre.base;

import java.util.HashMap;
import java.util.Map;

/**
 * @author srohrer
 *
 */
public class OrderBook {

	// order books
	HashMap<String, Order> buyBook = new HashMap<>();
	HashMap<String, Order> sellBook = new HashMap<>();

	// rule set
	String ruleSet = "";

	// constructor
	public OrderBook() {
		// TODO: insert rule set constructor here
		ruleSet = "climate";
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

}
