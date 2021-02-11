package org.mitre.base;

import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.util.Precision;
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
		assertEquals(0.0f, ord.getPrice().floatValue(), Precision.EPSILON);
		assertEquals("none", ord.getContract());
		assertEquals("testBuyer", ord.getBuyAgent());
		assertEquals("testSeller", ord.getSellAgent());

		log.info("Created object: {}", ord);
	}

	@Test
	public void testCustomConstructor() {
		CompletedOrder ord = new CompletedOrder(5, 1.342f, "test-contract", "buyer-sam", "seller-sanith");

		// assert default constructor
		assertEquals(5, ord.getSize().intValue());
		assertEquals(1.342f, ord.getPrice().floatValue(), Precision.EPSILON);
		assertEquals("test-contract", ord.getContract());
		assertEquals("buyer-sam", ord.getBuyAgent());
		assertEquals("seller-sanith", ord.getSellAgent());

		log.info("Created object: {}", ord);
	}

}
