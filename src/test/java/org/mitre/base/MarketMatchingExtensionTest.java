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

    assertEquals(ob.getBuyCount(), mme.getOrderBook().getBuyCount());
    assertEquals(ob.getSellCount(), mme.getOrderBook().getSellCount());
    assertEquals(me.getAllTrades(), mme.getMatchEngine().getAllTrades());
  }

  @Test
  public void testCustom() {
    OrderBook ob = new OrderBook();
    MatchingEngine me = new MatchingEngine();
    MarketMatchingExtension mme = new MarketMatchingExtension(ob, me);

    assertEquals(ob.getBuyCount(), mme.getOrderBook().getBuyCount());
    assertEquals(ob.getSellCount(), mme.getOrderBook().getSellCount());
    assertEquals(me.getAllTrades(), mme.getMatchEngine().getAllTrades());

    assertEquals(ob, mme.getOrderBook());
    assertEquals(me, mme.getMatchEngine());
  }

  @Test
  public void testNetLogoFuncs() {
    OrderBook ob = new OrderBook();
    MatchingEngine me = new MatchingEngine();
    MarketMatchingExtension mme = new MarketMatchingExtension(ob, me);

    assertEquals(ob, mme.getOrderBook());
    assertEquals(me, mme.getMatchEngine());

    mme.clearAll();
    assertEquals(ob.getBuyCount(), mme.getOrderBook().getBuyCount());
    assertEquals(ob.getSellCount(), mme.getOrderBook().getSellCount());
    assertEquals(me.getAllTrades(), mme.getMatchEngine().getAllTrades());

    assertEquals(new StringBuilder().toString(), mme.exportWorld().toString());
  }
}
