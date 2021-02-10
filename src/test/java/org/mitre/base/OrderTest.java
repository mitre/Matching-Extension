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
		assertEquals(0, ord.getSize());
		assertEquals("", ord.getContract());
		assertEquals("", ord.getAgent());

		log.info("Created object: {}", ord);
	}

	@Test
	public void testCustomConstructor() {
		Order ord = new Order(3, "test-contract", "buyer-sam");

		// assert default constructor
		assertEquals(3, ord.getSize());
		assertEquals("test-contract", ord.getContract());
		assertEquals("buyer-sam", ord.getAgent());

		log.info("Created object: {}", ord);
	}
}
