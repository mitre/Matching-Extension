package org.mitre.base;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrderBookTest {

	private static final Logger log = LoggerFactory.getLogger(OrderBookTest.class);

	@Test
	public void testDefaultConstructor() {
		OrderBook nb = new OrderBook();
		log.info("Created object: {}", nb);

		assertEquals("", nb.getRuleSet());
		assertEquals(0, nb.getBuyCount().intValue());
		assertEquals(0, nb.getSellCount().intValue());
	}

	@Test
	public void testCustomConstructor() {
		OrderBook nb = new OrderBook("climate-derivatives");
		log.info("Created object: {}", nb);

		assertEquals("climate-derivatives", nb.getRuleSet());
		assertEquals(0, nb.getBuyCount().intValue());
		assertEquals(0, nb.getSellCount().intValue());
	}

	@Test
	public void testAddOrder() {
		OrderBook nb = new OrderBook();
		log.info("Created object: {}", nb);

		assertEquals("", nb.getRuleSet());
		assertEquals(0, nb.getBuyCount().intValue());
		assertEquals(0, nb.getSellCount().intValue());

		nb.addBuyOrder(new Order());
		nb.addBuyOrder(new Order());
		nb.addSellOrder(new Order());

		assertEquals("", nb.getRuleSet());
		assertEquals(2, nb.getBuyCount().intValue());
		assertEquals(1, nb.getSellCount().intValue());
	}

	@Test
	public void testRemoveOrder() {
		OrderBook nb = new OrderBook();
		log.info("Created object: {}", nb);

		assertEquals("", nb.getRuleSet());
		assertEquals(0, nb.getBuyBookSize().intValue());
		assertEquals(0, nb.getSellBookSize().intValue());

		int topBuy, topSell = 0;
		topBuy = nb.addBuyOrder(new Order());
		topBuy = nb.addBuyOrder(new Order());
		topSell = nb.addSellOrder(new Order());

		assertEquals("", nb.getRuleSet());
		assertEquals(2, nb.getBuyBookSize().intValue());
		assertEquals(1, nb.getSellBookSize().intValue());

		nb.removeBuyOrder(topBuy);
		nb.removeSellOrder(topSell);

		assertEquals("", nb.getRuleSet());
		assertEquals(1, nb.getBuyBookSize().intValue());
		assertEquals(0, nb.getSellBookSize().intValue());
	}

}
