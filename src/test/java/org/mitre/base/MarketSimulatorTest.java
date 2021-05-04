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
public class MarketSimulatorTest {

  @Test
  public void testDefault() {
    MarketSimulator ms = new MarketSimulator();
    OrderBook ob = new OrderBook();
    MatchingEngine me = new MatchingEngine();
    
    assertEquals(ob.getBuyCount(), ms.getOrderBook().getBuyCount());
    assertEquals(ob.getSellCount(), ms.getOrderBook().getSellCount());
    assertEquals(me.getAllTrades(), ms.getMatchEngine().getAllTrades());
  }

  @Test
  public void testCustom() {
    OrderBook ob = new OrderBook();
    MatchingEngine me = new MatchingEngine();
    MarketSimulator ms = new MarketSimulator(ob, me);

    assertEquals(ob.getBuyCount(), ms.getOrderBook().getBuyCount());
    assertEquals(ob.getSellCount(), ms.getOrderBook().getSellCount());
    assertEquals(me.getAllTrades(), ms.getMatchEngine().getAllTrades());
    
    assertEquals(ob, ms.getOrderBook());
    assertEquals(me, ms.getMatchEngine());
  }

}
