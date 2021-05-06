/**
 *
 */
package org.mitre.base;

import java.util.List;

import org.nlogo.api.DefaultClassManager;
import org.nlogo.api.ExtensionException;
import org.nlogo.api.ExtensionManager;
import org.nlogo.api.ImportErrorHandler;
import org.nlogo.api.PrimitiveManager;
import org.nlogo.core.ExtensionObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author srohrer
 *
 */
@SpringBootApplication
public class MarketSimulator extends DefaultClassManager {

  // logger
  private final Logger logger = LoggerFactory.getLogger(MarketSimulator.class);

  // for running the market simulator
  @Autowired
  private OrderBook orderBook = new OrderBook();

  @Autowired
  private MatchingEngine matchEngine = new MatchingEngine();

  /**
   *
   */
  public MarketSimulator() {
    logger.debug("Constructed MarketSimulator with no OrderBook or MatchingEngine");
  }

  /**
   * @param ob, new OrderBook
   * @param me, new MatchingEngine
   */
  public MarketSimulator(OrderBook ob, MatchingEngine me) {
    setOrderBook(ob);
    setMatchEngine(me);
    logger.debug("Constructed MarketSimulator with OrderBook and MatchingEngine");
  }

  /**
   * @return the orderBook
   */
  public OrderBook getOrderBook() {
    return orderBook;
  }

  /**
   * @param orderBook the orderBook to set
   */
  private void setOrderBook(OrderBook orderBook) {
    this.orderBook = orderBook;
  }

  /**
   * @return the matchEngine
   */
  public MatchingEngine getMatchEngine() {
    return matchEngine;
  }

  /**
   * @param matchEngine the matchEngine to set
   */
  private void setMatchEngine(MatchingEngine matchEngine) {
    this.matchEngine = matchEngine;
  }

  /**
   *
   * @param args
   */
  public static void main(String[] args) {
    SpringApplication.run(MarketSimulator.class, args);
  }

  @Override
  public void clearAll() {
    setOrderBook(new OrderBook());
    setMatchEngine(new MatchingEngine());
  }

  @Override
  public StringBuilder exportWorld() {
    return new StringBuilder();
  }

  @Override
  public void importWorld(List<String[]> lines, ExtensionManager reader, ImportErrorHandler handler)
      throws ExtensionException {
    // TODO Auto-generated method stub

  }

  @Override
  public ExtensionObject readExtensionObject(ExtensionManager reader, String typeName, String value)
      throws ExtensionException {
    // TODO Auto-generated method stub
    return null;
  }

  /**
   *
   */
  @Override
  public void runOnce(ExtensionManager em) throws ExtensionException {
    setOrderBook(new OrderBook());
    setMatchEngine(new MatchingEngine());

  }

  @Override
  public void load(PrimitiveManager primManager) throws ExtensionException {
    // TODO Auto-generated method stub

  }

}
