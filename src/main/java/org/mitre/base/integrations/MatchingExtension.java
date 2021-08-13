/**
 *
 */
package org.mitre.base.integrations;

import org.mitre.base.MatchingEngine;
import org.mitre.base.book.Order;
import org.mitre.base.book.OrderBook;
import org.nlogo.api.Argument;
import org.nlogo.api.Context;
import org.nlogo.api.DefaultClassManager;
import org.nlogo.api.ExtensionException;
import org.nlogo.api.LogoException;
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
      setMatchingEngine(new MatchingEngine(getOrderBook()));
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
      return getMatchingEngine().toString();
    }

    /**
     *
     */
    @Override
    public String dump(boolean readable, boolean exporting, boolean reference) {
      return toString();
    }

    /**
     *
     */
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
   */
  public class DefaultMatcher implements Reporter {
    @Override
    public Syntax getSyntax() {
      return SyntaxJ.reporterSyntax(new int[] { Syntax.ListType() }, Syntax.WildcardType());
    }

    @Override
    public Object report(Argument[] args, Context context) throws ExtensionException {
      LogoMatching newLogoMatch = new LogoMatching();
      setNlogoExtension(newLogoMatch);
      return newLogoMatch;
    }
  }

  /**
   *
   * @param args
   * @return
   * @throws LogoException
   * @throws ExtensionException
   */
  private Order orderFromArgs(Argument[] args) throws LogoException, ExtensionException {
    return new Order(Integer.valueOf(args[1].getIntValue()), Float.valueOf((float) args[2].getDoubleValue()),
        String.valueOf(args[3].getString()), String.valueOf(args[4].getString()));
  }

  /**
   *
   */
  public class AddBuyOrder implements Reporter {
    @Override
    public Syntax getSyntax() {
      return SyntaxJ.reporterSyntax(new int[] { Syntax.WildcardType(), Syntax.NumberType(), Syntax.NumberType(),
          Syntax.StringType(), Syntax.StringType() }, Syntax.WildcardType());
    }

    @Override
    public Object report(Argument[] args, Context context) throws ExtensionException {
      Order newOrder = orderFromArgs(args);
      ((LogoMatching) args[0].get()).getOrderBook().addBuyOrder(newOrder);
      return new Object();
    }
  }

  /**
  *
  */
  public class AddSellOrder implements Reporter {
    @Override
    public Syntax getSyntax() {
      return SyntaxJ.reporterSyntax(new int[] { Syntax.WildcardType(), Syntax.NumberType(), Syntax.NumberType(),
          Syntax.StringType(), Syntax.StringType() }, Syntax.WildcardType());
    }

    @Override
    public Object report(Argument[] args, Context context) throws ExtensionException {
      Order newOrder = orderFromArgs(args);
      ((LogoMatching) args[0].get()).getOrderBook().addSellOrder(newOrder);
      return new Object();
    }
  }

  /**
   *
   */
  public class MatchUpdate implements Reporter {
    @Override
    public Syntax getSyntax() {
      return SyntaxJ.reporterSyntax(new int[] { Syntax.WildcardType() }, Syntax.WildcardType());
    }

    @Override
    public Object report(Argument[] args, Context context) throws ExtensionException {
      ((LogoMatching) args[0].get()).getMatchingEngine().matchUpdate();
      return new Object();
    }
  }

  /**
  *
  */
  public class PriceTicker implements Reporter {
    @Override
    public Syntax getSyntax() {
      return SyntaxJ.reporterSyntax(new int[] { Syntax.WildcardType() }, Syntax.WildcardType());
    }

    @Override
    public Object report(Argument[] args, Context context) throws ExtensionException {
      return Double.valueOf(((LogoMatching) args[0].get()).getMatchingEngine().lastFillPrice().doubleValue());
    }
  }

  /**
   *
   */
  public class BestBid implements Reporter {
    @Override
    public Syntax getSyntax() {
      return SyntaxJ.reporterSyntax(new int[] { Syntax.WildcardType() }, Syntax.WildcardType());
    }

    @Override
    public Object report(Argument[] args, Context context) throws ExtensionException {
      return Double.valueOf(((LogoMatching) args[0].get()).getMatchingEngine().bestBuyPrice().doubleValue());
    }
  }

  /**
  *
  */
  public class BestOffer implements Reporter {
    @Override
    public Syntax getSyntax() {
      return SyntaxJ.reporterSyntax(new int[] { Syntax.WildcardType() }, Syntax.WildcardType());
    }

    @Override
    public Object report(Argument[] args, Context context) throws ExtensionException {
      return Double.valueOf(((LogoMatching) args[0].get()).getMatchingEngine().bestSellPrice().doubleValue());
    }
  }

  /**
  *
  */
  public class MarketMean implements Reporter {
    @Override
    public Syntax getSyntax() {
      return SyntaxJ.reporterSyntax(new int[] { Syntax.WildcardType() }, Syntax.WildcardType());
    }

    @Override
    public Object report(Argument[] args, Context context) throws ExtensionException {
      return Double.valueOf(((LogoMatching) args[0].get()).getMatchingEngine().getMarketMean().doubleValue());
    }
  }

  /**
  *
  */
  public class Spread implements Reporter {
    @Override
    public Syntax getSyntax() {
      return SyntaxJ.reporterSyntax(new int[] { Syntax.WildcardType() }, Syntax.WildcardType());
    }

    @Override
    public Object report(Argument[] args, Context context) throws ExtensionException {
      return Double.valueOf(((LogoMatching) args[0].get()).getMatchingEngine().getSpread().doubleValue());
    }
  }

  /**
  *
  */
  public class CountTrades implements Reporter {
    @Override
    public Syntax getSyntax() {
      return SyntaxJ.reporterSyntax(new int[] { Syntax.WildcardType() }, Syntax.WildcardType());
    }

    @Override
    public Object report(Argument[] args, Context context) throws ExtensionException {
      return Double.valueOf(((LogoMatching) args[0].get()).getMatchingEngine().countTrades());
    }
  }

  /**
  *
  */
  public class CountBids implements Reporter {
    @Override
    public Syntax getSyntax() {
      return SyntaxJ.reporterSyntax(new int[] { Syntax.WildcardType() }, Syntax.WildcardType());
    }

    @Override
    public Object report(Argument[] args, Context context) throws ExtensionException {
      return Double.valueOf(((LogoMatching) args[0].get()).getMatchingEngine().countBuys());
    }
  }

  /**
  *
  */
  public class CountOffers implements Reporter {
    @Override
    public Syntax getSyntax() {
      return SyntaxJ.reporterSyntax(new int[] { Syntax.WildcardType() }, Syntax.WildcardType());
    }

    @Override
    public Object report(Argument[] args, Context context) throws ExtensionException {
      return Double.valueOf(((LogoMatching) args[0].get()).getMatchingEngine().countSells());
    }
  }

  /**
   *
   */
  public class DumpOrderBooks implements Reporter {
    @Override
    public Syntax getSyntax() {
      return SyntaxJ.reporterSyntax(new int[] { Syntax.WildcardType() }, Syntax.WildcardType());
    }

    @Override
    public Object report(Argument[] args, Context context) throws ExtensionException {
      String buyBook = ((LogoMatching) args[0].get()).getMatchingEngine().getBuyBookSorted().toString();
      String sellBook = ((LogoMatching) args[0].get()).getMatchingEngine().getSellBookSorted().toString();
      return "BuyBook: " + buyBook + "\nSellBook: " + sellBook;
    }
  }

  /**
   * load primitives
   */
  @Override
  public void load(PrimitiveManager primManager) throws ExtensionException {
    // constructor in netlogo
    primManager.addPrimitive("create-default", new DefaultMatcher());

    // debug classes to use
    primManager.addPrimitive("dump-order-books", new DumpOrderBooks());

    // populate the order books and edit them
    primManager.addPrimitive("add-bid", new AddBuyOrder());
    primManager.addPrimitive("add-offer", new AddSellOrder());

    // run the match updater
    primManager.addPrimitive("match-update", new MatchUpdate());

    // plotting methods
    primManager.addPrimitive("price-ticker", new PriceTicker());
    primManager.addPrimitive("best-bid", new BestBid());
    primManager.addPrimitive("best-offer", new BestOffer());
    primManager.addPrimitive("market-mean", new MarketMean());
    primManager.addPrimitive("spread", new Spread());
    primManager.addPrimitive("count-trades", new CountTrades());
    primManager.addPrimitive("count-bids", new CountBids());
    primManager.addPrimitive("count-offers", new CountOffers());
  }

  /**
   * reset order book and matching engine
   */
  @Override
  public void clearAll() {
    setNlogoExtension(null);
  }

}
