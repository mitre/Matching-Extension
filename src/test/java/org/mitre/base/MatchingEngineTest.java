/**
 *
 */
package org.mitre.base;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class MatchingEngineTest {

	private static final Logger log = LoggerFactory.getLogger(MatchingEngineTest.class);

	@Test
	public void testDefaultConstructor() {
		MatchingEngine me = new MatchingEngine();

		assertEquals(0, me.getAllTrades().size());
		assertEquals(0, me.getBuyBook().size());
		assertEquals(0, me.getSellBook().size());

		log.info("{}", me);
	}

	@Test
	public void testCustomConstructor() {
		OrderBook ob = new OrderBook();
		MatchingEngine me = new MatchingEngine(ob);

		assertEquals(0, me.getAllTrades().size());
		assertEquals(0, me.getBuyBook().size());
		assertEquals(0, me.getSellBook().size());

		log.info("{}", me);
	}

	@Test
	public void testMutateOrderBook() {
		OrderBook ob = new OrderBook();
		MatchingEngine me = new MatchingEngine(ob);

		assertEquals(0, me.getAllTrades().size());
		assertEquals(0, me.getBuyBook().size());
		assertEquals(0, me.getSellBook().size());

		log.info("{}", me);

		int topBuy, topSell = 0;
		topBuy = ob.addBuyOrder(new Order());
		topBuy = ob.addBuyOrder(new Order());
		topSell = ob.addSellOrder(new Order());

		log.info("{}", me);

		assertEquals(0, me.getAllTrades().size());
		assertEquals(2, me.getBuyBook().size());
		assertEquals(1, me.getSellBook().size());

		ob.removeBuyOrder(topBuy);
		ob.removeSellOrder(topSell);

		log.info("{}", me);

		assertEquals(0, me.getAllTrades().size());
		assertEquals(1, me.getBuyBook().size());
		assertEquals(0, me.getSellBook().size());
	}

	@Test
	public void testMatchUpdater() {
		OrderBook ob = new OrderBook();
		MatchingEngine me = new MatchingEngine(ob);

		assertEquals(0, me.getAllTrades().size());
		assertEquals(0, me.getBuyBook().size());
		assertEquals(0, me.getSellBook().size());

		log.info("{}", me);

		int topBuy, topSell = 0;
		topBuy = ob.addBuyOrder(new Order());
		topBuy = ob.addBuyOrder(new Order());
		topSell = ob.addSellOrder(new Order());
		
		
	
	}

}
