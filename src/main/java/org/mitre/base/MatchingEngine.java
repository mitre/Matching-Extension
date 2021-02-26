package org.mitre.base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.math3.util.Precision;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;


@Component
public class MatchingEngine {

	// logger
	private final Logger log = LoggerFactory.getLogger(MatchingEngine.class);

	// spread tolerance
	private static final Float SPREAD_TOL = 0.005f;

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
		log.debug("Constructed default MatchingEngine for weather derivatives");
	}

	// custom constructor
	public MatchingEngine(OrderBook ob) {
		setBuyBook(ob.getBuyBook());
		setSellBook(ob.getSellBook());
		initTrades();
		log.debug("Constructed custom MatchingEngine for weather derivatives. BUY_BOOK={} SELL_BOOK={}",
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
	 *  Pair implementation
	 */
	private class Pair<T, V>{
		private T key;
		private V value;

		public Pair(T t1, V v1){
			this.key = t1;
			this.value = v1;
		}

		public T getKey() {
			return this.key;
		}

		public V getValue() {
			return this.value;
		}
	}


	/**
	 * comparator for list of Pair<Integer, Order>
	 */
	private static Comparator<Pair<Integer, Order>> cmp =
				(lhs, rhs) -> Order.compare(lhs.getValue(), rhs.getValue());


	/**
	 *  @param: sorted lists of Pairs of order books (buy/sell)
	 *  @return matches of trades that will be executed
	 *
	 *  matching part of the clearing house algorithm
	 */
	private ArrayList<Pair<Integer, Integer>> findMatches(List<Pair<Integer, Order>> buyOrders,
                                                          List<Pair<Integer, Order>> sellOrders){
		// pair is <buyBook index, sellBook index>
		ArrayList<Pair<Integer, Integer>> matches = Lists.newArrayList();

		// guaranteed to have something in each.. or no matches possible
		Iterator<Pair<Integer, Order>> buyItr = buyOrders.iterator();
		Iterator<Pair<Integer, Order>> sellItr = sellOrders.iterator();

		// find the first pair of each book
		Pair<Integer, Order> tmpB = buyItr.next();
		Pair<Integer, Order> tmpS = sellItr.next();

		// now record index and order from each book to match
		Integer sellIdx = tmpS.getKey();
		Integer buyIdx = tmpB.getKey();
		Order sellOrd = tmpS.getValue();
		Order buyOrd = tmpB.getValue();

		// find initial leftover size for itrs
		Integer buyLeftover = buyOrd.getSize();
		Integer sellLeftover = sellOrd.getSize();

		// actual matching loop
		while (!buyLeftover.equals(0) && !sellLeftover.equals(0)) {
			// first check the contracts are the same
			// now see if the orders prices are within margin
			if (sellOrd.getPrice() - buyOrd.getPrice() < SPREAD_TOL){
				// check size and decide update buy or sell if not total fill
				if (buyLeftover < sellLeftover) {
					sellLeftover -= buyLeftover;
					buyLeftover = 0;
				} else if (buyLeftover > sellLeftover) {
					buyLeftover -= sellLeftover;
					sellLeftover = 0;
				} else {
					// equal size
					buyLeftover = 0;
					sellLeftover = 0;
				}
				// add to matches
				matches.add(new Pair<>(buyIdx, sellIdx));
			} else {
				// if mismatched contracts there is an error, return empty
				return Lists.newArrayList();
			}

			// update for next iteration
			if (sellLeftover.equals(0) && sellItr.hasNext()) {
				// sell order is finished
				tmpS = sellItr.next();
				sellIdx = tmpS.getKey();
				sellOrd = tmpS.getValue();
				sellLeftover = sellOrd.getSize();
			}
			if (buyLeftover.equals(0) && buyItr.hasNext()) {
				// buy order is finished
				tmpB = buyItr.next();
				buyIdx = tmpB.getKey();
				buyOrd = tmpB.getValue();
				buyLeftover = buyOrd.getSize();
			}
		}

		// pair is <buyBook index, sellBook index>
		return matches;
	}


	/**
	 *  @param: matches list
	 *
	 *  parse matches list into CompletedOrder objects in trades
	 */
	private void logTrades(ArrayList<Pair<Integer, Integer>> matches) {
		// record the last order size fill
		Integer fillSize = 0;
		Integer unfilledBuySize = 0;
		Integer unfilledSellSize = 0;

		// iterate though the matches logging them
		for (Pair<Integer, Integer> el : matches) {
			// pick the minimum size to take out of the order books and remove
			//   assume same contract so take from buy book (sellBook should work)
			//   get buyBook agent
			Order buy = buyBook.get(el.getKey());
			Order sell = sellBook.get(el.getValue());

			// verify same contract is being traded
			if (buy.getContract().equals(sell.getContract())) {
				// find fill size
				// basic fill
				fillSize = Math.min(buy.getSize(), sell.getSize());

				// leftover unfilled buy orders
				if (!unfilledBuySize.equals(0)) {
					fillSize = Math.min(unfilledBuySize, sell.getSize());
					unfilledBuySize = 0;
				} else {
					unfilledBuySize = Math.max(buy.getSize() - fillSize, 0);
				}

				// leftover unfilled sell orders
				if (!unfilledSellSize.equals(0)) {
					fillSize = Math.min(buy.getSize(), unfilledSellSize);
					unfilledSellSize = 0;
				} else {
					unfilledSellSize = Math.max(sell.getSize() - fillSize, 0);
				}

				// save order contract
				String orderContract = "";
				orderContract = buy.getContract();

				// append to trade log of completed orders
				this.addTrade(new CompletedOrder(fillSize,
								Precision.round((buy.getPrice() + sell.getPrice())/2, 6),
								orderContract, buy.getAgent(), sell.getAgent()));
			}
		}
	}


	/**
	 * @param: matches list
	 *
	 * remove filled orders based on matches list from order books
	 */
	private void removeFilledOrders(ArrayList<Pair<Integer, Integer>> matches) {
		for (Pair<Integer, Integer> el : matches) {
			// do not use OrderBook .remove*() as that is for agent,
			//    and not the MatchingEngine
			// see if same contract
			Order buy = buyBook.get(el.getKey());
			Order sell = sellBook.get(el.getValue());
			if (!buy.getContract().equals(sell.getContract())) {
				continue;
			}

			// see if there is a full fill, if not mutate the order
			//    that was not filled
			int buySize = buy.getSize();
			int sellSize = sell.getSize();

			if (buySize < sellSize) {
				// remove buyBook entry
				buyBook.remove(el.getKey());
				// set sellBook entry to sellSize - buySize
				sellBook.get(el.getValue()).setSize(sellSize - buySize);
			} else if (buySize > sellSize) {
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
	 *
	 * search for matches between buy and sell book, then
	 *     log completed trades to completed trades log
	 *
	 */
	public void matchUpdate() {
		// dump HashMaps into lists
		List<Pair<Integer, Order>> buyOrders = buyBook.entrySet().stream().map(e -> new Pair<Integer,
						Order>(e.getKey(), e.getValue())).collect(Collectors.toList());
		List<Pair<Integer, Order>> sellOrders = sellBook.entrySet().stream().map(e -> new Pair<Integer,
						Order>(e.getKey(), e.getValue())).collect(Collectors.toList());

		// sort lists by time and order size, as long as same contract
		Collections.sort(buyOrders, Collections.reverseOrder(cmp));
		Collections.sort(sellOrders, cmp);

		// check that both have elements
		if (buyOrders.isEmpty() || sellOrders.isEmpty()) {
			return;
		}

		// find the matches
		ArrayList<Pair<Integer, Integer>> matches = findMatches(buyOrders, sellOrders);

		// log completed trades to trades list
		logTrades(matches);

		// remove matches from order books
		removeFilledOrders(matches);
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
