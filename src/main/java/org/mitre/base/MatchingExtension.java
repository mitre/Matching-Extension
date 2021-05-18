/**
 *
 */
package org.mitre.base;

import org.nlogo.api.Argument;
import org.nlogo.api.Context;
import org.nlogo.api.DefaultClassManager;
import org.nlogo.api.ExtensionException;
import org.nlogo.api.PrimitiveManager;
import org.nlogo.api.Reporter;
import org.nlogo.core.ExtensionObject;
import org.nlogo.core.Syntax;
import org.nlogo.core.SyntaxJ;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author srohrer
 *
 */
@Component
public class MatchingExtension extends DefaultClassManager {

  // logger
  private final Logger logger = LoggerFactory.getLogger(MatchingExtension.class);

  // build the netlogo extension
  @Autowired
  private LogoMatching nlogoExtension = new LogoMatching();

  /**
   *
   */
  public MatchingExtension() {
    setNlogoExtension(new LogoMatching());
    logger.debug("Constructed MatchingExtension with no OrderBook or MatchingEngine");
  }

  /**
   *
   * @param ob
   * @param me
   */
  public MatchingExtension(OrderBook ob, MatchingEngine me) {
    setNlogoExtension(new LogoMatching(ob, me));
    logger.debug("Constructed MatchingExtension with OrderBook or MatchingEngine");
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
      return "matching";
    }

    /**
     *
     */
    @Override
    public String getNLTypeName() {
      return "";
    }

    /**
     *
     */
    @Override
    public String toString() {
      return "LogoMatching: " + getOrderBook().toString() + " " + getMatchingEngine().toString();
    }

    /**
     *
     */
    @Override
    public String dump(boolean readable, boolean exporting, boolean reference) {
      return this.toString();
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
   *
   * @author srohrer
   *
   */
  public class DefaultMatcher implements Reporter {

    @Override
    public Syntax getSyntax() {
      return SyntaxJ.reporterSyntax(new int[] { Syntax.ListType() }, Syntax.WildcardType());
    }

    @Override
    public Object report(Argument[] args, Context context) throws ExtensionException {
      return new LogoMatching();
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
    primManager.addPrimitive("create-default", new DefaultMatcher());
  }

}
