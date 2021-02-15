package org.mitre.base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
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
	private final Logger log = LoggerFactory.getLogger(MatchingEngine.class);

	// spread tolerance
	private final Float SPREAD_TOL = 0.005f;

	// order books
	private HashMap<Integer, Order> buyBook;
	private HashMap<Integer, Order> sellBook;

	// record of matches
	private ArrayList<CompletedOrder> trades;


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
		log.info("Constructed custom this. BUY_BOOK={} SELL_BOOK={}",
					buyBook, sellBook);
	}

	/**
	 * initialize empty buy and sell books
	 */
	private void initBooks() {
		this.buyBook = Maps.newHashMap();
		this.sellBook = Maps.newHashMap();
	}

	/**
	 * initialize empty list
	 */
	private void initTrades() {
		this.trades = Lists.newArrayList();
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
		// dump HashMaps into lists
		List<Pair<Integer, Order>> buyOrders = buyBook.entrySet().stream().map(e -> new Pair<Integer,
						Order>(e.getKey(), e.getValue())).collect(Collectors.toList());
		List<Pair<Integer, Order>> sellOrders = sellBook.entrySet().stream().map(e -> new Pair<Integer,
						Order>(e.getKey(), e.getValue())).collect(Collectors.toList());

		// sort lists by time and order size, as long as same contract
		Collections.sort(buyOrders, cmp);
		Collections.sort(sellOrders, cmp);

		// pair is <buyBook idx, sellBook idx>
		ArrayList<Pair<Integer, Integer>> matches = Lists.newArrayList();

		Iterator<Pair<Integer, Order>> buyItr = buyOrders.iterator();
		Iterator<Pair<Integer, Order>> sellItr = sellOrders.iterator();
		Order sellOrd = new Order();
		Order buyOrd = new Order();
		Integer sellIdx = 0;
		Integer buyIdx = 0;
		Integer buyLeftover = 0;
		Integer sellLeftover = 0;

		// actual matching loop
		while (buyItr.hasNext() || sellItr.hasNext()) {
			if (buyItr.hasNext() && buyLeftover.equals(0)) {
				Pair<Integer, Order> tmp = buyItr.next();
				buyIdx = tmp.getKey();
				buyOrd = tmp.getValue();
			}
			if (sellItr.hasNext() && sellLeftover.equals(0)) {
				Pair<Integer, Order> tmp = sellItr.next();
				sellIdx = tmp.getKey();
				sellOrd = tmp.getValue();
			}

			// first check the contracts are the same
			if (!buyOrd.getContract().equalsIgnoreCase(sellOrd.getContract())) {
				continue;
			}

			// now see if the orders prices are within margin
			if (Math.abs(buyOrd.getPrice() - sellOrd.getPrice()) < SPREAD_TOL){
				// check size and decide update buy or sell if not total fill
				/*if (buyOrd.getSize() < sellOrd.getSize()) {
					buyLeftover = 0;
					sellLeftover = sellOrd.getSize() - buyOrd.getSize();
				} else if (buyOrd.getSize() > sellOrd.getSize()) {
					sellLeftover = 0;
					buyLeftover = buyOrd.getSize() - sellOrd.getSize();
				} else {
					// equal size
					buyLeftover = 0;
					sellLeftover = 0;
				}*/

				// add to matches
				matches.add(new Pair<>(buyIdx, sellIdx));
			}
		}

		// log completed trades to trades list
		for (Pair<Integer, Integer> el : matches) {
			// pick the minimum size to take out of the order books and remove
			//   assume same contract so take from buy book (sellBook should work)
			//   get buyBook agent
			Order buy = buyBook.get(el.getKey());
			Order sell = sellBook.get(el.getValue());
			//   get sellBook agent
			trades.add(new CompletedOrder(Math.min(buy.getSize(), sell.getSize()),
							(buy.getPrice() + sell.getPrice())/2,
							buy.getContract(), buy.getAgent(), sell.getAgent()));
		}

		// remove matches from order books
		for (Pair<Integer, Integer> el : matches) {
			// do not use OrderBook .remove*() as that is for agent,
			//    and not the MatchingEngine
			// see if there is a full fill, if not mutate the order
			//    that was not filled
			if (!buyBook.containsKey(el.getKey())) {
				continue;
			}
			if (!sellBook.containsKey(el.getValue())) {
				continue;
			}

			int buySize = buyBook.get(el.getKey()).getSize();
			int sellSize = sellBook.get(el.getValue()).getSize();

			if (buySize < sellSize) {
				// remove buyBook entry
				buyBook.remove(el.getKey());
				// set sellBook entry to sellSize - buySize
				sellBook.get(el.getValue()).setSize(sellSize - buySize);
			} else if (buySize > sellSize){
				// remove sellBook entry
				sellBook.remove(el.getValue());
				// set buyBook entry to buySize - sellSize
				buyBook.get(el.getKey()).setSize(buySize - sellSize);
			} else {
				// in this case the match is equal
				buyBook.remove(el.getKey());
				sellBook.remove(el.getValue());
			}
		}
	}


	/**
	 * @param: Order to add to trades list
	 */
	public void addTrade(CompletedOrder order) {
		this.trades.add(order);
	}

	/**
	 * @return: all CompletedOrders in trades
	 */
	public List<CompletedOrder> getAllTrades() {
		return this.trades;
	}

	/**
	 * @return: a single CompletedOrder by index
	 */
	public CompletedOrder getTrade(Integer idx){
		return this.trades.get(idx);
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
	public void setBuyBook(Map<Integer, Order> buyBook) {
		this.buyBook = (HashMap<Integer, Order>) buyBook;
	}

	/**
	 * @param sellBook the sellBook to set
	 */
	public void setSellBook(Map<Integer, Order> sellBook) {
		this.sellBook = (HashMap<Integer, Order>) sellBook;
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
