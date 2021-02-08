/**
 *
 */
package org.mitre.base;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 *
 */
@Component
public class OrderBook {

	// order books
	private HashMap<String, Order> buyBook = new HashMap<>();
	private HashMap<String, Order> sellBook = new HashMap<>();

	// rule set
	private String ruleSet = "";

	// constructor
	public OrderBook() {
		// TODO: insert rule set constructor here
		setRuleSet("climate");
	}


	/**
	 * @return the buyBook
	 */
	@Autowired
	public Map<String, Order> getBuyBook() {
		return buyBook;
	}

	/**
	 * @return the sellBook
	 */
	@Autowired
	public Map<String, Order> getSellBook() {
		return sellBook;
	}

	/**
	 * @return both buy and sell books
	 */
	@Autowired
	public Map<String, Map<String, Order>> getBooks() {
		Map<String, Map<String, Order>> ret = new HashMap<>();
		ret.put("BUY", getBuyBook());
		ret.put("SELL", getSellBook());
		return ret;
	}


	/**
	 * @return the ruleSet
	 */
	@Autowired
	public String getRuleSet() {
		return ruleSet;
	}


	/**
	 * @param ruleSet the ruleSet to set
	 */
	@Autowired
	public void setRuleSet(String ruleSet) {
		this.ruleSet = ruleSet;
	}

}
