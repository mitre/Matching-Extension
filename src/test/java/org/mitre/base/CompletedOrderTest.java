package org.mitre.base;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CompletedOrderTest {

	// logger
	private static final Logger log = LoggerFactory.getLogger(CompletedOrderTest.class);


	@Test
	public void testDefaultConstructor() {
		CompletedOrder ord = new CompletedOrder();

		// assert default constructor
		assertEquals(0, ord.getSize().intValue());
		assertEquals("none", ord.getContract());
		assertEquals("testBuyer", ord.getBuyAgent());
		assertEquals("testSeller", ord.getSellAgent());

		log.info("Created object: {}", ord);
	}

	@Test
	public void testCustomConstructor() {
		CompletedOrder ord = new CompletedOrder(5, "test-contract", "buyer-sam", "seller-sanith");

		// assert default constructor
		assertEquals(5, ord.getSize().intValue());
		assertEquals("test-contract", ord.getContract());
		assertEquals("buyer-sam", ord.getBuyAgent());
		assertEquals("seller-sanith", ord.getSellAgent());

		log.info("Created object: {}", ord);
	}

}
