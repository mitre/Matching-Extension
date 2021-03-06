/**
 *
 */
package org.mitre.base;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.time.Instant;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import org.mitre.base.book.CompletedOrder;
import org.mitre.base.book.Order;
import org.mitre.base.book.OrderBook;

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

    MatchingEngine me = new MatchingEngine(ob, "floods");

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
    assertEquals(0, me2.getAllTrades().size());
    assertEquals(2, me2.getBuyBook().size());
    assertEquals(3, me2.getSellBook().size());
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
    MatchingEngine me = new MatchingEngine(ob, "floods");

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

    assertEquals(0.000999987125f, me.getSpread(), Precision.EPSILON);
    assertEquals(2, me.countBuys().intValue());
    assertEquals(1, me.countSells().intValue());
    assertEquals(6, me.countTrades().intValue());
    assertEquals(0.347f, me.getMarketMean(), Precision.EPSILON);
    assertEquals(0.347f, me.bestBuyPrice(), Precision.EPSILON);
    assertEquals(0.348f, me.bestSellPrice(), Precision.EPSILON);
    assertEquals(0.347f, me.lastFillPrice(), Precision.EPSILON);
  }

  @Test
  public void testStuckOrder() {
    OrderBook ob = new OrderBook();
    MatchingEngine me = new MatchingEngine(ob);

    me.matchUpdate();

    ob.addSellOrder(new Order(2, 0.000f, "SPX", "sam"));
    ob.addBuyOrder(new Order(5, 0.344f, "SPX", "mike"));

    me.matchUpdate();

    assertEquals(1, me.getBuyBook().size());
    assertEquals(0, me.getSellBook().size());
    assertEquals(1, me.getAllTrades().size());
  }

  @Test
  public void testNetLogo1() {
    OrderBook ob = new OrderBook();
    MatchingEngine me = new MatchingEngine(ob);

    ob.addSellOrder(new Order(43, 0.33726883f, "SPX", "2"));
    ob.addBuyOrder(new Order(15, 5.456093f, "SPX", "3"));
    me.matchUpdate();

    assertEquals(0, me.getBuyBook().size());
    assertEquals(1, me.getSellBook().size());
    assertEquals(1, me.getAllTrades().size());

    CompletedOrder co = me.getAllTrades().get(0);
    assertEquals(2.896681f, co.getPrice(), Precision.EPSILON);
    assertEquals(Integer.valueOf(15), co.getSize());
  }

  @Test
  public void testNetLogoSimple() {
    OrderBook ob = new OrderBook();
    MatchingEngine me = new MatchingEngine(ob);

    ob.addBuyOrder(new Order(15, 5.456093f, "SPX", "3"));
    ob.addBuyOrder(new Order(20, 5.3952384f, "SPX", "4"));
    ob.addSellOrder(new Order(43, 0.33726883f, "SPX", "2"));

    assertEquals(2, me.getBuyBook().size());
    assertEquals(1, me.getSellBook().size());
    assertEquals(0, me.getAllTrades().size());

    me.matchUpdate();

    assertEquals(2, me.getAllTrades().size());
  }

  @Test
  public void testNetLogoBug() {
    OrderBook ob = new OrderBook();
    MatchingEngine me = new MatchingEngine(ob);

    ob.addBuyOrder(new Order(15, 5.456093f, "SPX", "3"));
    ob.addBuyOrder(new Order(20, 5.3952384f, "SPX", "4"));
    ob.addBuyOrder(new Order(4, 5.0603933f, "SPX", "0"));
    ob.addBuyOrder(new Order(37, 4.954774f, "SPX", "2"));
    ob.addBuyOrder(new Order(80, 3.0970328f, "SPX", "4"));
    ob.addBuyOrder(new Order(66, 2.6074696f, "SPX", "2"));
    ob.addBuyOrder(new Order(40, 2.3769927f, "SPX", "1"));
    ob.addBuyOrder(new Order(82, 1.9347206f, "SPX", "1"));
    ob.addBuyOrder(new Order(85, 1.5234574f, "SPX", "1"));
    ob.addBuyOrder(new Order(27, 1.3527801f, "SPX", "2"));
    ob.addBuyOrder(new Order(89, 0.92889565f, "SPX", "2"));
    ob.addBuyOrder(new Order(4, 0.46273425f, "SPX", "4"));
    ob.addBuyOrder(new Order(6, -0.47480747f, "SPX", "4"));

    ob.addSellOrder(new Order(43, 0.33726883f, "SPX", "2"));
    ob.addSellOrder(new Order(76, 1.8909931f, "SPX", "4"));
    ob.addSellOrder(new Order(86, 2.688896f, "SPX", "3"));
    ob.addSellOrder(new Order(18, 2.7763693f, "SPX", "0"));
    ob.addSellOrder(new Order(63, 3.1890047f, "SPX", "0"));
    ob.addSellOrder(new Order(28, 4.1222425f, "SPX", "1"));
    ob.addSellOrder(new Order(81, 4.507014f, "SPX", "1"));
    ob.addSellOrder(new Order(51, 5.127572f, "SPX", "4"));
    ob.addSellOrder(new Order(16, 5.664279f, "SPX", "4"));
    ob.addSellOrder(new Order(95, 6.8820467f, "SPX", "2"));
    ob.addSellOrder(new Order(12, 7.0235133f, "SPX", "3"));
    ob.addSellOrder(new Order(13, 7.07225f, "SPX", "0"));
    ob.addSellOrder(new Order(1, 7.404298f, "SPX", "0"));

    assertEquals(13, me.getBuyBook().size());
    assertEquals(13, me.getSellBook().size());
    assertEquals(0, me.getAllTrades().size());

    me.matchUpdate();

    assertEquals(8, me.getBuyBook().size());
    assertEquals(11, me.getSellBook().size());
    assertEquals(7, me.getAllTrades().size());
  }

  @Test
  public void testFileLoggerNone() throws IOException {
    OrderBook ob = new OrderBook();
    MatchingEngine me = new MatchingEngine(ob);

    ob.addBuyOrder(new Order(15, 5.456093f, "SPX", "3"));
    ob.addBuyOrder(new Order(20, 5.3952384f, "SPX", "4"));
    ob.addSellOrder(new Order(43, 0.33726883f, "SPX", "2"));

    assertEquals(2, me.getBuyBook().size());
    assertEquals(1, me.getSellBook().size());
    assertEquals(0, me.getAllTrades().size());
    me.matchUpdate();
    assertEquals(2, me.getAllTrades().size());

    me.logTradesToFile();
  }

  @Test
  public void testFileLoggerInstant() throws IOException {
    OrderBook ob = new OrderBook();
    Instant tm = new Date(System.currentTimeMillis()).toInstant();
    MatchingEngine me = new MatchingEngine(ob);

    ob.addBuyOrder(new Order(15, 5.456093f, "SPX", "3", tm));
    ob.addBuyOrder(new Order(20, 5.3952384f, "SPX", "4", tm));
    ob.addSellOrder(new Order(43, 0.33726883f, "SPX", "2", tm));

    assertEquals(2, me.getBuyBook().size());
    assertEquals(1, me.getSellBook().size());
    assertEquals(0, me.getAllTrades().size());
    me.matchUpdate();
    assertEquals(2, me.getAllTrades().size());

    me.logTradesToFile();
  }

  @Test
  public void testFileLoggerInteger() throws IOException {
    OrderBook ob = new OrderBook();
    MatchingEngine me = new MatchingEngine(ob);

    ob.addBuyOrder(new Order(15, 5.456093f, "SPX", "3", Integer.valueOf(8)));
    ob.addBuyOrder(new Order(20, 5.3952384f, "SPX", "4", Integer.valueOf(3)));
    ob.addSellOrder(new Order(43, 0.33726883f, "SPX", "2", Integer.valueOf(4)));

    assertEquals(2, me.getBuyBook().size());
    assertEquals(1, me.getSellBook().size());
    assertEquals(0, me.getAllTrades().size());
    me.matchUpdate();
    assertEquals(2, me.getAllTrades().size());

    me.logTradesToFile();
  }

  @Test
  public void testToString() {
    MatchingEngine me = new MatchingEngine();
    assertEquals("MatchingEngine: GENERIC size(COMPLETED_TRADES)=0 size(BUY_BOOK)=0 size(SELL_BOOK)=0",
        me.toString());
  }
}
