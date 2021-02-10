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

		assertEquals("", nb.getRuleSet());

		log.info("Created object: {}", nb);
	}

	@Test
	public void testCustomConstructor() {
		OrderBook nb = new OrderBook("climate-derivatives");

		assertEquals("climate-derivatives", nb.getRuleSet());

		log.info("Created object: {}", nb);
	}

}
