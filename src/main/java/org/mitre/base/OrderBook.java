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

	// order number counters
	private static Integer buyCount;
	private static Integer sellCount;

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
		OrderBook.buyCount = 0;
		OrderBook.sellCount = 0;
	}

	/**
	 * @return order book number if successful
	 */
	public Integer addBuyOrder(Order order) {
		OrderBook.buyBook.put(incBuyCount(), order);
		return getBuyCount();
	}

	/**
	 * @return order book number if successful
	 */
	public Integer addSellOrder(Order order) {
		OrderBook.sellBook.put(incSellCount(), order);
		return getSellCount();
	}

	/**
	 * @return order that is removed
	 */
	public Order removeBuyOrder(Integer idx) {
		return OrderBook.buyBook.remove(idx);
	}

	/**
	 * @return order that is removed
	 */
	public Order removeSellOrder(Integer idx) {
		return OrderBook.sellBook.remove(idx);
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
	 * @return current buy counter
	 */
	public Integer getBuyCount() {
		return buyCount;
	}

	/**
	 * @return buy count incremented
	 */
	public static Integer incBuyCount() {
		return ++OrderBook.buyCount;
	}

	/**
	 * @return current sell counter
	 */
	public Integer getSellCount() {
		return sellCount;
	}

	/**
	 * @return sell count incremented
	 */
	public static Integer incSellCount() {
		return ++OrderBook.sellCount;
	}

	/**
	 * @return current size of buy book
	 */
	public Integer getBuyBookSize() {
		return OrderBook.buyBook.size();
	}

	/**
	 * @return current size of sell book
	 */
	public Integer getSellBookSize() {
		return OrderBook.sellBook.size();
	}

	/**
	 * @return string representation of OrderBook
	 */
	@Override
	public String toString() {
		return "OrderBook with RULE=" + getRuleSet() + " SIZE(buyBook)="
					+ getBuyBookSize() + " SIZE(sellBook)="
					+ getSellBookSize() + " BUY_COUNTER=" + getBuyCount()
					+ " SELL_COUNTER=" + getSellCount();
	}

}
