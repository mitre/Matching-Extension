package org.mitre.base;

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;


public class MarketPropertiesParserTest {

	@Test
	public void testDefaultConstructor() {
		MarketPropertiesParser mpp = new MarketPropertiesParser();

		assertEquals(Collections.emptySet(), mpp.getProp().keySet());
	}

	@Test
	public void testCustomConstructor() {
		MarketPropertiesParser mpp = new MarketPropertiesParser("floods");
		MarketPropertiesParser mp2 = new MarketPropertiesParser("spectrum");

		Set<String> ans = new HashSet<>();
		ans.add("name");
		ans.add("venue");
		ans.add("url");
		ans.add("tolerance");

		assertEquals(ans, mpp.getProp().keySet());
		assertEquals(ans, mp2.getProp().keySet());
	}

	@Test
	public void testFields() {
		MarketPropertiesParser mpp = new MarketPropertiesParser("floods");
		MarketPropertiesParser mp2 = new MarketPropertiesParser("spectrum");

		assertEquals("FLOOD_CLIMATE", mpp.getProp().getProperty("name"));
		assertEquals("SPECTRUM_SECONDARY", mp2.getProp().getProperty("name"));

		assertEquals("https://www.cmegroup.com/trading/weather/", mpp.getProp().getProperty("url"));
		assertEquals("", mp2.getProp().getProperty("url"));
	}

}
