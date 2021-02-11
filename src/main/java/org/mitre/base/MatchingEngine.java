package org.mitre.base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import javafx.util.Pair;


@Component
public class MatchingEngine {

	// logger
	private static final Logger log = LoggerFactory.getLogger(MatchingEngine.class);

	// order books
	private static HashMap<Integer, Order> buyBook;
	private static HashMap<Integer, Order> sellBook;

	// record of matches
	private static ArrayList<CompletedOrder> trades;


	// default constructor
	public MatchingEngine() {
		initBooks();
		initTrades();
		// use time-order priority matching method here
		log.info("Constructed default MatchingEngine for weather derivatives");
	}

	// custom constructor
	public MatchingEngine(OrderBook ob) {
		setBuyBook(ob.getBuyBook());
		setSellBook(ob.getSellBook());
		initTrades();
		log.info("Constructed custom MatchingEngine. BUY_BOOK={} SELL_BOOK={}",
					buyBook, sellBook);
	}

	/**
	 * initialize empty buy and sell books
	 */
	private static void initBooks() {
		MatchingEngine.buyBook = Maps.newHashMap();
		MatchingEngine.sellBook = Maps.newHashMap();
	}

	/**
	 * initialize empty list
	 */
	private static void initTrades() {
		MatchingEngine.trades = Lists.newArrayList();
	}


	/**
	 * comparator for list of Pair<Integer, Order>
	 */
	private static Comparator<Pair<Integer, Order>> cmp =
				(lhs, rhs) -> Order.compare(lhs.getValue(), rhs.getValue());


	/**
	 * search for matches between buy and sell book, then
	 *     log completed trades to completed trades log
	 */
	public void matchUpdate() {
		List<Pair<Integer, Order>> buyOrders;
		List<Pair<Integer, Order>> sellOrders;

		// dump HashMaps into lists
		buyOrders = buyBook.entrySet().stream().map(e -> new Pair<Integer,
						Order>(e.getKey(), e.getValue())).collect(Collectors.toList());
		sellOrders = sellBook.entrySet().stream().map(e -> new Pair<Integer,
						Order>(e.getKey(), e.getValue())).collect(Collectors.toList());

		// sort lists by time and order size
		log.info("BUY ORDER BEFORE {}", buyOrders);
		log.info("SELL ORDER BEFORE {}", sellOrders);
		Collections.sort(buyOrders, cmp);
		Collections.sort(sellOrders, cmp);
		log.info("BUY ORDER AFTER {}", buyOrders);
		log.info("SELL ORDER AFTER {}", sellOrders);


		// search for matches

		// log completed trades to trades list


		// remove matches from order books
	}

	/**
	 * @param: Order to add to trades list
	 */
	public static void addTrade(CompletedOrder order) {
		MatchingEngine.trades.add(order);
	}

	/**
	 * @return: all CompletedOrders in trades
	 */
	public List<CompletedOrder> getAllTrades() {
		return MatchingEngine.trades;
	}

	/**
	 * @return: a single CompletedOrder by index
	 */
	public static CompletedOrder getTrade(Integer idx){
		return MatchingEngine.trades.get(idx);
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
	 * @param buyBook the buyBook to set
	 */
	public static void setBuyBook(Map<Integer, Order> buyBook) {
		MatchingEngine.buyBook = (HashMap<Integer, Order>) buyBook;
	}

	/**
	 * @param sellBook the sellBook to set
	 */
	public static void setSellBook(Map<Integer, Order> sellBook) {
		MatchingEngine.sellBook = (HashMap<Integer, Order>) sellBook;
	}

	/**
	 * print the MatchingEngine to string
	 */
	@Override
	public String toString() {
		return "MatchingEngine: size(COMPLETED_TRADES)=" + getAllTrades().size()
				   + " size(BUY_BOOK)=" + getBuyBook().size()
				   + " size(SELL_BOOK)=" + getSellBook().size();
	}

}
