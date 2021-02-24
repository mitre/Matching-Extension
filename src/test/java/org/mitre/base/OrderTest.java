package org.mitre.base;

import static org.junit.Assert.assertEquals;

import java.time.Instant;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class OrderTest {

	@Test
	public void testDefaultConstructor() {
		Order ord = new Order();

		// assert default constructor
		assertEquals(0, ord.getSize().intValue());
		assertEquals(0.0f, ord.getPrice().floatValue(), Precision.EPSILON);
		assertEquals("", ord.getContract());
		assertEquals("", ord.getAgent());
	}

	@Test
	public void testCustomConstructor() {
		Order ord = new Order(3, 4.732f, "test-contract", "buyer-sam");

		// assert default constructor
		assertEquals(3, ord.getSize().intValue());
		assertEquals(4.732f, ord.getPrice().floatValue(), Precision.EPSILON);
		assertEquals("test-contract", ord.getContract());
		assertEquals("buyer-sam", ord.getAgent());
	}

	@Test
	public void testCustomConstructorTime() {
		Instant tm = new Date(System.currentTimeMillis()).toInstant();
		Order ord = new Order(3, 4.732f, "test-contract", "buyer-sam", tm);

		// assert default constructor
		assertEquals(3, ord.getSize().intValue());
		assertEquals("test-contract", ord.getContract());
		assertEquals(4.732f, ord.getPrice().floatValue(), Precision.EPSILON);
		assertEquals("buyer-sam", ord.getAgent());
		assertEquals(tm, ord.getDt());
	}

	@Test
	public void testCompare() {
		Order cord = new Order(3, 0.983f, "comp-contract", "comp-buyer-sam");
		Order cord2 = new Order(1, 0.983f, "test-contract4", "buyer-sam13");

		// these are different contracts so should be different
		try {
			assertEquals(0, Order.compare(cord, cord2));
		} catch (UnsupportedOperationException e) {
			assertEquals("Must compare Orders of same contract", e.getMessage());
		}
	}

	@Test
	public void testCompareSize() {
		Instant tm = new Date(System.currentTimeMillis()).toInstant();
		Order cord = new Order(3, 0.983f, "comp-contract", "comp-buyer-sam", tm);
		Order cord2 = new Order(1, 0.983f, "comp-contract", "buyer-sam13", tm);
		Order cord3 = new Order(1, 0.983f, "comp-contract", "buyer-sam15", tm);

		// these are same contract and same time, order by size
		assertEquals(1, Order.compare(cord, cord2));
		assertEquals(-1, Order.compare(cord2, cord));
		assertEquals(1, Order.compare(cord, cord3));
		assertEquals(-1, Order.compare(cord3, cord));
		assertEquals(0, Order.compare(cord2, cord3));
		assertEquals(0, Order.compare(cord3, cord2));
	}

	@Test
	public void testCompareTime() throws Exception {
		Order cord = new Order(3, 0.983f, "comp-contract", "comp-buyer-sam");
		TimeUnit.MILLISECONDS.sleep(100);
		Order cord2 = new Order(3, 0.983f, "comp-contract", "buyer-sam13");

		// these are same contract and different time, order by time
		assertEquals(-1, Order.compare(cord, cord2));
		assertEquals(1, Order.compare(cord2, cord));
	}

	@Test
	public void testComparePrice() {
		Instant tm = new Date(System.currentTimeMillis()).toInstant();
		Order cord = new Order(3, 0.981f, "comp-contract", "comp-buyer-sam", tm);
		Order cord2 = new Order(3, 0.989f, "comp-contract", "buyer-sam13", tm);
		Order cord3 = new Order(3, 0.985f, "comp-contract", "buyer-sam15", tm);

		// these are same contract and same time, order by size
		assertEquals(-1, Order.compare(cord, cord2));
		assertEquals(1, Order.compare(cord2, cord));
		assertEquals(-1, Order.compare(cord, cord3));
		assertEquals(1, Order.compare(cord3, cord));
		assertEquals(1, Order.compare(cord2, cord3));
		assertEquals(-1, Order.compare(cord3, cord2));
	}

	@Test
	public void testToString() {
		Instant tm = new Date(System.currentTimeMillis()).toInstant();
		Order o = new Order(4, 1.132f, "ordtest", "buyer", tm);
		assertEquals("Order@ " + tm.toString() + " of SIZE=4 and CONTRACT=ordtest @ $1.132 from AGENT=buyer",
						o.toString());
	}
}
