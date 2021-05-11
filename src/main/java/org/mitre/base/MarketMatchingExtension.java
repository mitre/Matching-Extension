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

  // build the netlogo extension
  @Autowired
  private LogoMatching nlogoExtension = new LogoMatching();

  /**
   *
   */
  public MarketMatchingExtension() {
    setNlogoExtension(new LogoMatching());
    logger.debug("Constructed MarketMatchingExtension with no OrderBook or MatchingEngine");
  }

  /**
   *
   * @param ob
   * @param me
   */
  public MarketMatchingExtension(OrderBook ob, MatchingEngine me) {
    setNlogoExtension(new LogoMatching(ob, me));
    logger.debug("Constructed MarketMatchingExtension with OrderBook or MatchingEngine");
  }

  ///////////////////////////////////////////////////////////////////////////
  // ---------------------------- NetLogo Methods ------------------------ //
  ///////////////////////////////////////////////////////////////////////////

  /**
   * @return the nlogoExtension
   */
  public LogoMatching getNlogoExtension() {
    return nlogoExtension;
  }

  /**
   * @param the nlogoExtension
   */
  private void setNlogoExtension(LogoMatching lm) {
    nlogoExtension = lm;
  }

  /**
   * NetLogo type for matching engine to implement
   */
  @Component
  public static class LogoMatching implements ExtensionObject {

    private OrderBook orderBook;
    private MatchingEngine matchingEngine;

    /**
     *
     */
    public LogoMatching() {
      setOrderBook(new OrderBook());
      setMatchingEngine(new MatchingEngine());
    }

    /**
     *
     * @param ob
     * @param me
     */
    public LogoMatching(OrderBook ob, MatchingEngine me) {
      setOrderBook(ob);
      setMatchingEngine(me);
    }

    /**
     *
     */
    @Override
    public String getExtensionName() {
      return "market-matching";
    }

    /**
     *
     */
    @Override
    public String getNLTypeName() {
      return "matcher";
    }

    /**
     *
     */
    @Override
    public String dump(boolean readable, boolean exporting, boolean reference) {
      return null;
    }

    @Override
    public boolean recursivelyEqual(Object obj) {
      LogoMatching lm = (LogoMatching) obj;
      return (getMatchingEngine() == lm.getMatchingEngine()) && (getOrderBook() == lm.getOrderBook());
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
     * @return the matchingEngine
     */
    public MatchingEngine getMatchingEngine() {
      return matchingEngine;
    }

    /**
     * @param matchingEngine the matchingEngine to set
     */
    private void setMatchingEngine(MatchingEngine matchingEngine) {
      this.matchingEngine = matchingEngine;
    }

  }

  /**
   * reset order book and matching engine
   */
  @Override
  public void clearAll() {
    setNlogoExtension(new LogoMatching());
  }

  /**
   * load primitives
   */
  @Override
  public void load(PrimitiveManager primManager) throws ExtensionException {
    primManager.addPrimitive("make-order", null);
  }

}
