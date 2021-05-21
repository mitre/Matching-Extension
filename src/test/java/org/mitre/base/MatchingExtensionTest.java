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
public class MatchingExtensionTest {

  @Test
  public void testDefault() {
    MatchingExtension mme = new MatchingExtension();
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
    MatchingExtension mme = new MatchingExtension(ob, me);

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
    MatchingExtension mme = new MatchingExtension(ob, me);

    assertEquals(ob, mme.getNlogoExtension().getOrderBook());
    assertEquals(me, mme.getNlogoExtension().getMatchingEngine());
  }

  @Test
  public void testLogoMatching1() {
    OrderBook ob = new OrderBook();
    MatchingEngine me = new MatchingEngine();
    MatchingExtension mme = new MatchingExtension(ob, me);

    assertEquals(ob, mme.getNlogoExtension().getOrderBook());
    assertEquals(me, mme.getNlogoExtension().getMatchingEngine());

    assertEquals("", mme.getNlogoExtension().getNLTypeName());
    assertEquals("matching", mme.getNlogoExtension().getExtensionName());
  }
}
