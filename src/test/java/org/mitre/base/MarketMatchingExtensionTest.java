/**
 *
 */
package org.mitre.base;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * @author srohrer
 *
 */
public class MarketMatchingExtensionTest {

  @Test
  public void testDefault() {
    MarketMatchingExtension mme = new MarketMatchingExtension();
    OrderBook ob = new OrderBook();
    MatchingEngine me = new MatchingEngine();

    assertEquals(ob.getBuyCount(), mme.getNlogoExtension().getOrderBook().getBuyCount());
    assertEquals(ob.getSellCount(), mme.getNlogoExtension().getOrderBook().getSellCount());
    assertEquals(me.getAllTrades(), mme.getNlogoExtension().getMatchingEngine().getAllTrades());
  }

  @Test
  public void testCustom() {
    OrderBook ob = new OrderBook();
    MatchingEngine me = new MatchingEngine();
    MarketMatchingExtension mme = new MarketMatchingExtension(ob, me);

    assertEquals(ob.getBuyCount(), mme.getNlogoExtension().getOrderBook().getBuyCount());
    assertEquals(ob.getSellCount(), mme.getNlogoExtension().getOrderBook().getSellCount());
    assertEquals(me.getAllTrades(), mme.getNlogoExtension().getMatchingEngine().getAllTrades());

    assertEquals(ob, mme.getNlogoExtension().getOrderBook());
    assertEquals(me, mme.getNlogoExtension().getMatchingEngine());
  }

  @Test
  public void testNetLogoFuncs() {
    OrderBook ob = new OrderBook();
    MatchingEngine me = new MatchingEngine();
    MarketMatchingExtension mme = new MarketMatchingExtension(ob, me);

    assertEquals(ob, mme.getNlogoExtension().getOrderBook());
    assertEquals(me, mme.getNlogoExtension().getMatchingEngine());

    mme.clearAll();
    assertEquals(ob.getBuyCount(), mme.getNlogoExtension().getOrderBook().getBuyCount());
    assertEquals(ob.getSellCount(), mme.getNlogoExtension().getOrderBook().getSellCount());
    assertEquals(me.getAllTrades(), mme.getNlogoExtension().getMatchingEngine().getAllTrades());
  }

  @Test
  public void testLogoMatching1() {
    OrderBook ob = new OrderBook();
    MatchingEngine me = new MatchingEngine();
    MarketMatchingExtension mme = new MarketMatchingExtension(ob, me);

    assertEquals(ob, mme.getNlogoExtension().getOrderBook());
    assertEquals(me, mme.getNlogoExtension().getMatchingEngine());

    assertEquals("matcher", mme.getNlogoExtension().getNLTypeName());
    assertEquals("market-matching", mme.getNlogoExtension().getExtensionName());
  }
}
