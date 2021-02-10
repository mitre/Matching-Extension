package org.mitre.base;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrderTest {

	// logger
	private static final Logger log = LoggerFactory.getLogger(OrderTest.class);


	@Test
	public void testDefaultConstructor() {
		Order ord = new Order();

		// assert default constructor
		assertEquals(0, ord.getSize().intValue());
		assertEquals("", ord.getContract());
		assertEquals("", ord.getAgent());

		log.info("Created object: {}", ord);
	}

	@Test
	public void testCustomConstructor() {
		Order ord = new Order(3, "test-contract", "buyer-sam");

		// assert default constructor
		assertEquals(3, ord.getSize().intValue());
		assertEquals("test-contract", ord.getContract());
		assertEquals("buyer-sam", ord.getAgent());

		log.info("Created object: {}", ord);
	}

	@Test
	public void testCompare() {
		Order cord = new Order(3, "comp-contract", "comp-buyer-sam");
		Order cord2 = new Order(1, "test-contract4", "buyer-sam13");

		log.info("Created object: {}", cord);
		log.info("Created object: {}", cord2);

		assertEquals(-1, Order.compare(cord, cord2));
	}
}
