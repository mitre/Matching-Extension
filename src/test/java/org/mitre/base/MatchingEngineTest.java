/**
 *
 */
package org.mitre.base;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.TimeUnit;

import org.apache.commons.math3.util.Precision;
import org.junit.Test;

/**
 *
 */
public class MatchingEngineTest {

  @Test
  public void testDefaultConstructor() {
    MatchingEngine me = new MatchingEngine();

    assertEquals(0, me.getAllTrades().size());
    assertEquals(0, me.getBuyBook().size());
    assertEquals(0, me.getSellBook().size());
  }

  @Test
  public void testCustomConstructor() {
    OrderBook ob = new OrderBook();
    MatchingEngine me = new MatchingEngine(ob);

    assertEquals(0, me.getAllTrades().size());
    assertEquals(0, me.getBuyBook().size());
    assertEquals(0, me.getSellBook().size());
  }

  @Test
  public void testCustomConstructorTemplate() {
    OrderBook ob = new OrderBook();
    MatchingEngine meFlood = new MatchingEngine(ob, "floods");
    MatchingEngine meSpectrum = new MatchingEngine(ob, "spectrum");

    assertEquals(0.005f, meFlood.getSpreadTol(), Precision.EPSILON);
    assertEquals(0.001f, meSpectrum.getSpreadTol(), Precision.EPSILON);
  }

  @Test
  public void testMutateOrderBook() {
    OrderBook ob = new OrderBook();
    MatchingEngine me = new MatchingEngine(ob);

    assertEquals(0, me.getAllTrades().size());
    assertEquals(0, me.getBuyBook().size());
    assertEquals(0, me.getSellBook().size());

    int topBuy, topSell = 0;
    topBuy = ob.addBuyOrder(new Order());
    topBuy = ob.addBuyOrder(new Order());
    topSell = ob.addSellOrder(new Order());

    assertEquals(0, me.getAllTrades().size());
    assertEquals(2, me.getBuyBook().size());
    assertEquals(1, me.getSellBook().size());

    ob.removeBuyOrder(topBuy);
    ob.removeSellOrder(topSell);

    assertEquals(0, me.getAllTrades().size());
    assertEquals(1, me.getBuyBook().size());
    assertEquals(0, me.getSellBook().size());
  }

  @Test
  public void testMatchUpdater() throws Exception {
    OrderBook ob = new OrderBook();

    ob.addBuyOrder(new Order(3, 0.345f, "GC1!", "sam"));
    ob.addBuyOrder(new Order(5, 0.341f, "GC1!", "fred"));
    ob.addSellOrder(new Order(4, 0.349f, "GC1!", "bob"));
    ob.addSellOrder(new Order(2, 0.347f, "GC1!", "sam"));
    TimeUnit.MILLISECONDS.sleep(100);
    ob.addBuyOrder(new Order(5, 0.344f, "GC1!", "mike"));

    MatchingEngine me = new MatchingEngine(ob);

    assertEquals(0, me.getAllTrades().size());
    assertEquals(3, me.getBuyBook().size());
    assertEquals(2, me.getSellBook().size());

    me.matchUpdate();
    assertEquals(3, me.getAllTrades().size());
    assertEquals(2, me.getBuyBook().size());
    assertEquals(0, me.getSellBook().size());

    OrderBook ob2 = new OrderBook();
    ob2.addSellOrder(new Order(3, 0.348f, "GC1!", "sam"));
    ob2.addSellOrder(new Order(5, 0.349f, "GC1!", "fred"));
    ob2.addBuyOrder(new Order(4, 0.345f, "GC1!", "bob"));
    ob2.addBuyOrder(new Order(3, 0.347f, "GC1!", "sam"));
    TimeUnit.MILLISECONDS.sleep(100);
    ob2.addSellOrder(new Order(5, 0.354f, "GC1!", "mike"));

    MatchingEngine me2 = new MatchingEngine(ob2);

    assertEquals(0, me2.getAllTrades().size());
    assertEquals(2, me2.getBuyBook().size());
    assertEquals(3, me2.getSellBook().size());

    me2.matchUpdate();
    assertEquals(2, me2.getAllTrades().size());
    assertEquals(0, me2.getBuyBook().size());
    assertEquals(2, me2.getSellBook().size());
  }

  @Test
  public void testMatchUpdateDiffContracts() {
    OrderBook ob = new OrderBook();

    ob.addBuyOrder(new Order(3, 0.345f, "contr0", "buyer-test"));
    ob.addSellOrder(new Order(3, 0.3445f, "contr1", "seller-sam"));

    MatchingEngine me = new MatchingEngine(ob);

    assertEquals(0, me.getAllTrades().size());
    assertEquals(1, me.getBuyBook().size());
    assertEquals(1, me.getSellBook().size());

    me.matchUpdate();
    assertEquals(0, me.getAllTrades().size());
    assertEquals(1, me.getBuyBook().size());
    assertEquals(1, me.getSellBook().size());
  }

  @Test
  public void testMatchUpdateNoTrades() {
    OrderBook ob = new OrderBook();

    ob.addBuyOrder(new Order(3, 0.340f, "contr1", "buyer-test"));
    ob.addSellOrder(new Order(3, 0.3465f, "contr1", "seller-sam"));

    MatchingEngine me = new MatchingEngine(ob);

    assertEquals(0, me.getAllTrades().size());
    assertEquals(1, me.getBuyBook().size());
    assertEquals(1, me.getSellBook().size());

    me.matchUpdate();
    assertEquals(0, me.getAllTrades().size());
    assertEquals(1, me.getBuyBook().size());
    assertEquals(1, me.getSellBook().size());
  }

  @Test
  public void testMatchUpdateNoOrders() {
    OrderBook ob = new OrderBook();
    MatchingEngine me = new MatchingEngine(ob);

    assertEquals(0, me.getAllTrades().size());
    assertEquals(0, me.getBuyBook().size());
    assertEquals(0, me.getSellBook().size());

    me.matchUpdate();
    assertEquals(0, me.getAllTrades().size());
    assertEquals(0, me.getBuyBook().size());
    assertEquals(0, me.getSellBook().size());

    ob.addBuyOrder(new Order(3, 0.345f, "contr0", "buyer-test"));
    assertEquals(0, me.getAllTrades().size());
    assertEquals(1, me.getBuyBook().size());
    assertEquals(0, me.getSellBook().size());

    me.matchUpdate();
    assertEquals(0, me.getAllTrades().size());
    assertEquals(1, me.getBuyBook().size());
    assertEquals(0, me.getSellBook().size());

    ob.removeBuyOrder(1);
    ob.addSellOrder(new Order(3, 0.345f, "contr1", "seller-test"));
    assertEquals(0, me.getAllTrades().size());
    assertEquals(0, me.getBuyBook().size());
    assertEquals(1, me.getSellBook().size());

    me.matchUpdate();
    assertEquals(0, me.getAllTrades().size());
    assertEquals(0, me.getBuyBook().size());
    assertEquals(1, me.getSellBook().size());
  }

  @Test
  public void testMatchUpdateNetLogoEmpty() {
    OrderBook ob = new OrderBook();
    MatchingEngine me = new MatchingEngine(ob);

    me.matchUpdate();

    assertEquals(Float.NaN, me.getSpread(), Precision.EPSILON);
    assertEquals(0, me.countBuys().intValue());
    assertEquals(0, me.countSells().intValue());
    assertEquals(0, me.countTrades().intValue());
    assertEquals(Float.NaN, me.getMarketMean(), Precision.EPSILON);
    assertEquals(Float.NaN, me.bestBuyPrice(), Precision.EPSILON);
    assertEquals(Float.NaN, me.bestSellPrice(), Precision.EPSILON);
    assertEquals(Float.NaN, me.lastFillPrice(), Precision.EPSILON);
  }

  @Test
  public void testMatchUpdateNetLogoMarket() {
    OrderBook ob = new OrderBook();
    MatchingEngine me = new MatchingEngine(ob);

    me.matchUpdate();

    assertEquals(Float.NaN, me.getSpread(), Precision.EPSILON);
    assertEquals(0, me.countBuys().intValue());
    assertEquals(0, me.countSells().intValue());
    assertEquals(0, me.countTrades().intValue());
    assertEquals(Float.NaN, me.getMarketMean(), Precision.EPSILON);
    assertEquals(Float.NaN, me.bestBuyPrice(), Precision.EPSILON);
    assertEquals(Float.NaN, me.bestSellPrice(), Precision.EPSILON);
    assertEquals(Float.NaN, me.lastFillPrice(), Precision.EPSILON);

    ob.addBuyOrder(new Order(3, 0.347f, "GC1!", "sam"));
    ob.addBuyOrder(new Order(5, 0.341f, "GC1!", "fred"));
    ob.addSellOrder(new Order(4, 0.349f, "GC1!", "bob"));
    ob.addSellOrder(new Order(2, 0.347f, "GC1!", "sam"));
    ob.addBuyOrder(new Order(5, 0.344f, "GC1!", "mike"));

    me.matchUpdate();

    assertEquals(0.0f, me.getSpread(), Precision.EPSILON);
    assertEquals(2, me.countBuys().intValue());
    assertEquals(0, me.countSells().intValue());
    assertEquals(3, me.countTrades().intValue());
    assertEquals(0.342f, me.getMarketMean(), Precision.EPSILON);
    assertEquals(0.347f, me.bestBuyPrice(), Precision.EPSILON);
    assertEquals(0.347f, me.bestSellPrice(), Precision.EPSILON);
    assertEquals(0.347f, me.lastFillPrice(), Precision.EPSILON);

    ob.addSellOrder(new Order(3, 0.348f, "GC1!", "sam"));
    ob.addSellOrder(new Order(5, 0.349f, "GC1!", "fred"));
    ob.addBuyOrder(new Order(4, 0.345f, "GC1!", "bob"));
    ob.addBuyOrder(new Order(3, 0.347f, "GC1!", "sam"));
    ob.addSellOrder(new Order(5, 0.354f, "GC1!", "mike"));

    me.matchUpdate();

    assertEquals(-0.001f, me.getSpread(), Precision.EPSILON);
    assertEquals(4, me.countBuys().intValue());
    assertEquals(3, me.countSells().intValue());
    assertEquals(3, me.countTrades().intValue());
    assertEquals(0.347f, me.getMarketMean(), Precision.EPSILON);
    assertEquals(0.347f, me.bestBuyPrice(), Precision.EPSILON);
    assertEquals(0.348f, me.bestSellPrice(), Precision.EPSILON);
    assertEquals(0.347f, me.lastFillPrice(), Precision.EPSILON);
  }

  @Test
  public void testToString() {
    MatchingEngine me = new MatchingEngine();
    assertEquals("MatchingEngine: FLOOD_CLIMATE size(COMPLETED_TRADES)=0 size(BUY_BOOK)=0 size(SELL_BOOK)=0",
        me.toString());
  }
}
