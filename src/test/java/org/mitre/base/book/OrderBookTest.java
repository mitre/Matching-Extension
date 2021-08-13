package org.mitre.base.book;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class OrderBookTest {

  @Test
  public void testDefaultConstructor() {
    OrderBook nb = new OrderBook();

    assertEquals(0, nb.getBuyCount().intValue());
    assertEquals(0, nb.getSellCount().intValue());
  }

  @Test
  public void testCustomConstructor() {
    OrderBook nb = new OrderBook("climate-derivatives");

    assertEquals(0, nb.getBuyCount().intValue());
    assertEquals(0, nb.getSellCount().intValue());
  }

  @Test
  public void testAddOrder() {
    OrderBook nb = new OrderBook();

    assertEquals(0, nb.getBuyCount().intValue());
    assertEquals(0, nb.getSellCount().intValue());

    nb.addBuyOrder(new Order());
    nb.addBuyOrder(new Order());
    nb.addSellOrder(new Order());

    assertEquals(2, nb.getBuyCount().intValue());
    assertEquals(1, nb.getSellCount().intValue());
  }

  @Test
  public void testRemoveOrder() {
    OrderBook nb = new OrderBook();

    assertEquals(0, nb.getBuyBookSize().intValue());
    assertEquals(0, nb.getSellBookSize().intValue());

    int topBuy, topSell = 0;
    topBuy = nb.addBuyOrder(new Order());
    topBuy = nb.addBuyOrder(new Order());
    topSell = nb.addSellOrder(new Order());

    assertEquals(2, nb.getBuyBookSize().intValue());
    assertEquals(1, nb.getSellBookSize().intValue());

    nb.removeBuyOrder(topBuy);
    nb.removeSellOrder(topSell);

    assertEquals(1, nb.getBuyBookSize().intValue());
    assertEquals(0, nb.getSellBookSize().intValue());
  }

  @Test
  public void testToString() {
    OrderBook ob = new OrderBook();
    assertEquals("OrderBook with SIZE(buyBook)=0 SIZE(sellBook)=0 BUY_COUNTER=0 SELL_COUNTER=0", ob.toString());
  }

}
