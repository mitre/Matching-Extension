/**
 *
 */
package org.mitre.base;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

/**
 *
 */
public class MatchingEngineTest {

	@Test
	public void testDefaultConstructor() {
		MatchingEngine me = new MatchingEngine();

		assertEquals(0, me.getAllTrades().size());
		assertEquals(0, me.getBuyBook().size());
		assertEquals(0, me.getSellBook().size());
	}

	@Test
	public void testCustomConstructor() {
		OrderBook ob = new OrderBook();
		MatchingEngine me = new MatchingEngine(ob);

		assertEquals(0, me.getAllTrades().size());
		assertEquals(0, me.getBuyBook().size());
		assertEquals(0, me.getSellBook().size());
	}

	@Test
	public void testMutateOrderBook() {
		OrderBook ob = new OrderBook();
		MatchingEngine me = new MatchingEngine(ob);

		assertEquals(0, me.getAllTrades().size());
		assertEquals(0, me.getBuyBook().size());
		assertEquals(0, me.getSellBook().size());

		int topBuy, topSell = 0;
		topBuy = ob.addBuyOrder(new Order());
		topBuy = ob.addBuyOrder(new Order());
		topSell = ob.addSellOrder(new Order());

		assertEquals(0, me.getAllTrades().size());
		assertEquals(2, me.getBuyBook().size());
		assertEquals(1, me.getSellBook().size());

		ob.removeBuyOrder(topBuy);
		ob.removeSellOrder(topSell);

		assertEquals(0, me.getAllTrades().size());
		assertEquals(1, me.getBuyBook().size());
		assertEquals(0, me.getSellBook().size());
	}

	@Test
	public void testMatchUpdater() throws Exception {
		OrderBook ob = new OrderBook();

		ob.addBuyOrder(new Order(3, 0.345f, "contr1", "buyer-sanith"));
		ob.addBuyOrder(new Order(5, 0.341f, "contr1", "buyer-sam"));
		ob.addSellOrder(new Order(4, 0.349f, "contr1", "seller-matt"));
		ob.addSellOrder(new Order(2, 0.347f, "contr1", "seller-sam"));
		TimeUnit.MILLISECONDS.sleep(100);
		ob.addBuyOrder(new Order(5, 0.344f, "contr1", "buyer-matt"));

		MatchingEngine me = new MatchingEngine(ob);

		assertEquals(0, me.getAllTrades().size());
		assertEquals(3, me.getBuyBook().size());
		assertEquals(2, me.getSellBook().size());

		me.matchUpdate();
		assertEquals(3, me.getAllTrades().size());
		assertEquals(2, me.getBuyBook().size());
		assertEquals(0, me.getSellBook().size());
	
	}

}
