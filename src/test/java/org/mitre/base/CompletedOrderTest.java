package org.mitre.base;

import static org.junit.Assert.assertEquals;

import java.time.Instant;
import java.util.Date;

import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class CompletedOrderTest {

	@Test
	public void testDefaultConstructor() {
		CompletedOrder ord = new CompletedOrder();

		// assert default constructor
		assertEquals(0, ord.getSize().intValue());
		assertEquals(0.0f, ord.getPrice().floatValue(), Precision.EPSILON);
		assertEquals("none", ord.getContract());
		assertEquals("testBuyer", ord.getBuyAgent());
		assertEquals("testSeller", ord.getSellAgent());
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
	}

	@Test
	public void testToString() {
		Instant tm = new Date(System.currentTimeMillis()).toInstant();
		CompletedOrder ord = new CompletedOrder(5, 1.342f, "test", "buyer", "seller", tm);

		assertEquals("CompletedOrder@ " + tm.toString() + " of SIZE=5 and CONTRACT=test @ $1.342 between SELLER=seller and BUYER=buyer",
						ord.toString());
	}
}
