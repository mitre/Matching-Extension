/**
 *
 */
package org.mitre.base;

import org.nlogo.api.DefaultClassManager;
import org.nlogo.api.ExtensionException;
import org.nlogo.api.PrimitiveManager;
import org.nlogo.core.ExtensionObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author srohrer
 *
 */
@Component
public class MarketMatchingExtension extends DefaultClassManager {

  // logger
  private final Logger logger = LoggerFactory.getLogger(MarketMatchingExtension.class);

  // for running the market matching order books
  @Autowired
  private OrderBook orderBook = new OrderBook();

  // for running the matching engine
  @Autowired
  private MatchingEngine matchEngine = new MatchingEngine();

  /**
   *
   */
  public MarketMatchingExtension() {
    logger.debug("Constructed MarketMatchingExtension with no OrderBook or MatchingEngine");
  }

  /**
   * @param ob, new OrderBook
   * @param me, new MatchingEngine
   */
  public MarketMatchingExtension(OrderBook ob, MatchingEngine me) {
    setOrderBook(ob);
    setMatchEngine(me);
    logger.debug("Constructed MarketMatchingExtension with OrderBook and MatchingEngine");
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

  ///////////////////////////////////////////////////////////////////////////
  // ---------------------------- NetLogo Methods ------------------------ //
  ///////////////////////////////////////////////////////////////////////////

  /**
   * NetLogo type for matching engine to implement
   */
  private static class LogoMatching implements ExtensionObject {

    @Override
    public String dump(boolean readable, boolean exporting, boolean reference) {
      return null;
    }

    @Override
    public String getExtensionName() {
      return "market-matching";
    }

    @Override
    public String getNLTypeName() {
      return "matcher";
    }

    @Override
    public boolean recursivelyEqual(Object obj) {
      return false;
    }

  }

  /**
   * reset order book and matching engine
   */
  @Override
  public void clearAll() {
    setOrderBook(new OrderBook());
    setMatchEngine(new MatchingEngine());
  }

  /**
   * load primitives
   */
  @Override
  public void load(PrimitiveManager primManager) throws ExtensionException {
    primManager.addPrimitive("make-order", null);
  }

}
