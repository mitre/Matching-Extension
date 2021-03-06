/**
 *
 */
package org.mitre.base.book;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;

/**
 *
 *
 */
@Component
public class OrderBook {

  // logger
  private final Logger log = LoggerFactory.getLogger(OrderBook.class);

  // order books
  private HashMap<Integer, Order> buyBook;
  private HashMap<Integer, Order> sellBook;

  // order number counters
  private Integer buyCount;
  private Integer sellCount;

  // default constructor
  public OrderBook() {
    initBooks();
    log.debug("OrderBook instantiated, NO rule set");
  }

  // custom constructor
  public OrderBook(String rule) {
    initBooks();
    log.debug("OrderBook instantiated, chosen rule set: {}", rule);
  }

  /**
   * initialize buyBook and sellBook
   */
  public void initBooks() {
    this.buyBook = Maps.newHashMap();
    this.sellBook = Maps.newHashMap();
    this.buyCount = 0;
    this.sellCount = 0;
  }

  /**
   * @return order book number if successful
   */
  public Integer addBuyOrder(Order order) {
    this.buyBook.put(incBuyCount(), order);
    return getBuyCount();
  }

  /**
   * @return order book number if successful
   */
  public Integer addSellOrder(Order order) {
    this.sellBook.put(incSellCount(), order);
    return getSellCount();
  }

  /**
   * @return order that is removed
   */
  public Order removeBuyOrder(Integer idx) {
    return this.buyBook.remove(idx);
  }

  /**
   * @return order that is removed
   */
  public Order removeSellOrder(Integer idx) {
    return this.sellBook.remove(idx);
  }

  /**
   * @return the buyBook
   */
  public Map<Integer, Order> getBuyBook() {
    return buyBook;
  }

  /**
   * @return the sellBook
   */
  public Map<Integer, Order> getSellBook() {
    return sellBook;
  }

  /**
   * @return current buy counter
   */
  public Integer getBuyCount() {
    return buyCount;
  }

  /**
   * @return buy count incremented
   */
  private Integer incBuyCount() {
    return ++this.buyCount;
  }

  /**
   * @return current sell counter
   */
  public Integer getSellCount() {
    return sellCount;
  }

  /**
   * @return sell count incremented
   */
  private Integer incSellCount() {
    return ++this.sellCount;
  }

  /**
   * @return current size of buy book
   */
  public Integer getBuyBookSize() {
    return this.buyBook.size();
  }

  /**
   * @return current size of sell book
   */
  public Integer getSellBookSize() {
    return this.sellBook.size();
  }

  /**
   * @return string representation of OrderBook
   */
  @Override
  public String toString() {
    return "OrderBook with SIZE(buyBook)=" + getBuyBookSize() + " SIZE(sellBook)=" + getSellBookSize() + " BUY_COUNTER="
        + getBuyCount() + " SELL_COUNTER=" + getSellCount();
  }

}
