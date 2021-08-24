/**
 *
 */
package org.mitre.base.integrations;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.mitre.base.MatchingEngine;
import org.mitre.base.book.OrderBook;
import org.mitre.base.integrations.MatchingExtension.AddBuyOrder;
import org.mitre.base.integrations.MatchingExtension.AddSellOrder;
import org.mitre.base.integrations.MatchingExtension.BestBid;
import org.mitre.base.integrations.MatchingExtension.BestOffer;
import org.mitre.base.integrations.MatchingExtension.CountBids;
import org.mitre.base.integrations.MatchingExtension.CountOffers;
import org.mitre.base.integrations.MatchingExtension.CountTrades;
import org.mitre.base.integrations.MatchingExtension.DefaultMatcher;
import org.mitre.base.integrations.MatchingExtension.DumpOrderBooks;
import org.mitre.base.integrations.MatchingExtension.LogTradesFile;
import org.mitre.base.integrations.MatchingExtension.LogoMatching;
import org.mitre.base.integrations.MatchingExtension.MarketMean;
import org.mitre.base.integrations.MatchingExtension.MatchUpdate;
import org.mitre.base.integrations.MatchingExtension.PriceTicker;
import org.mitre.base.integrations.MatchingExtension.Spread;
import org.nlogo.api.ExtensionException;
import org.nlogo.workspace.ExtensionPrimitiveManager;

/**
 * @author srohrer
 *
 */
public class MatchingExtensionTest {

  @Test
  public void testDefault() {
    MatchingExtension mme = new MatchingExtension();
    OrderBook ob = new OrderBook();
    MatchingEngine me = new MatchingEngine();

    assertEquals(ob.getBuyCount(), mme.getNlogoExtension().getOrderBook().getBuyCount());
    assertEquals(ob.getSellCount(), mme.getNlogoExtension().getOrderBook().getSellCount());
    assertEquals(me.getAllTrades(), mme.getNlogoExtension().getMatchingEngine().getAllTrades());
  }

  @Test
  public void testCustom() {
    OrderBook ob = new OrderBook();
    MatchingEngine me = new MatchingEngine();
    MatchingExtension mme = new MatchingExtension(ob, me);

    assertEquals(ob.getBuyCount(), mme.getNlogoExtension().getOrderBook().getBuyCount());
    assertEquals(ob.getSellCount(), mme.getNlogoExtension().getOrderBook().getSellCount());
    assertEquals(me.getAllTrades(), mme.getNlogoExtension().getMatchingEngine().getAllTrades());

    assertEquals(ob, mme.getNlogoExtension().getOrderBook());
    assertEquals(me, mme.getNlogoExtension().getMatchingEngine());
  }

  @Test
  public void testNetLogoFuncs() {
    OrderBook ob = new OrderBook();
    MatchingEngine me = new MatchingEngine();
    MatchingExtension mme = new MatchingExtension(ob, me);

    assertEquals(ob, mme.getNlogoExtension().getOrderBook());
    assertEquals(me, mme.getNlogoExtension().getMatchingEngine());
  }

  @Test
  public void testMatchingExtension() {
    OrderBook ob = new OrderBook();
    MatchingEngine me = new MatchingEngine();
    MatchingExtension mme = new MatchingExtension(ob, me);

    assertEquals(ob, mme.getNlogoExtension().getOrderBook());
    assertEquals(me, mme.getNlogoExtension().getMatchingEngine());

    assertEquals("", mme.getNlogoExtension().getNLTypeName());
    assertEquals("matching", mme.getNlogoExtension().getExtensionName());

    mme.clearAll();
    assertEquals(null, mme.getNlogoExtension());
  }

  @Test
  public void testLogoExtension() throws ExtensionException {
    LogoMatching lm = new LogoMatching();

    String ans = "MatchingEngine: GENERIC size(COMPLETED_TRADES)=0 size(BUY_BOOK)=0 size(SELL_BOOK)=0";
    assertEquals(ans, lm.dump(false, false, false));
    assertEquals(true, lm.recursivelyEqual(lm));

    OrderBook ob = new OrderBook();
    MatchingEngine me = new MatchingEngine();
    LogoMatching lm2 = new LogoMatching(ob, me);

    String ans2 = "MatchingEngine: GENERIC size(COMPLETED_TRADES)=0 size(BUY_BOOK)=0 size(SELL_BOOK)=0";
    assertEquals(ans2, lm2.dump(false, false, false));
    assertEquals(false, lm2.recursivelyEqual(lm));
    assertEquals(true, lm2.recursivelyEqual(lm2));

    LogoMatching lm3 = new LogoMatching(ob, new MatchingEngine());
    assertEquals(false, lm3.recursivelyEqual(lm2));
    assertEquals(true, lm3.recursivelyEqual(lm3));

    LogoMatching lm4 = new LogoMatching(new OrderBook(), me);
    assertEquals(false, lm4.recursivelyEqual(lm));
    assertEquals(false, lm4.recursivelyEqual(lm2));
    assertEquals(false, lm4.recursivelyEqual(lm3));

    MatchingExtension mme = new MatchingExtension(ob, me);
    mme.load(new ExtensionPrimitiveManager("test time"));
    assertEquals(MatchingExtension.class, mme.getClass());
  }

  @Test
  public void testDefaultMatcher() throws ExtensionException {
    MatchingExtension mme = new MatchingExtension();
    DefaultMatcher m = mme.new DefaultMatcher();

    assertEquals(DefaultMatcher.class, m.getClass());
    String ans = "Syntax(10,0,List(8),8191,None,None,false,OTPL,None,false,true)";
    assertEquals(ans, m.getSyntax().toString());

    String ans2 = "MatchingEngine: GENERIC size(COMPLETED_TRADES)=0 size(BUY_BOOK)=0 size(SELL_BOOK)=0";
    assertEquals(ans2, m.report(null, null).toString());
  }

  @Test
  public void testAddBuyOrder() throws ExtensionException {
    MatchingExtension mme = new MatchingExtension();
    AddBuyOrder ab = mme.new AddBuyOrder();

    assertEquals(AddBuyOrder.class, ab.getClass());
    String ans = "Syntax(10,0,List(8191, 1, 1, 4, 4, 1),8191,None,None,false,OTPL,None,false,true)";
    assertEquals(ans, ab.getSyntax().toString());
  }

  @Test
  public void testAddSellOrder() throws ExtensionException {
    MatchingExtension mme = new MatchingExtension();
    AddSellOrder ab = mme.new AddSellOrder();

    assertEquals(AddSellOrder.class, ab.getClass());
    String ans = "Syntax(10,0,List(8191, 1, 1, 4, 4, 1),8191,None,None,false,OTPL,None,false,true)";
    assertEquals(ans, ab.getSyntax().toString());
  }

  @Test
  public void testMatchUpdate() throws ExtensionException {
    MatchingExtension mme = new MatchingExtension();
    MatchUpdate ab = mme.new MatchUpdate();

    assertEquals(MatchUpdate.class, ab.getClass());
    String ans = "Syntax(10,0,List(8191),8191,None,None,false,OTPL,None,false,true)";
    assertEquals(ans, ab.getSyntax().toString());
  }

  @Test
  public void testDebugOutputs() throws ExtensionException {
    MatchingExtension mme = new MatchingExtension();
    DumpOrderBooks db = mme.new DumpOrderBooks();

    assertEquals(DumpOrderBooks.class, db.getClass());
    String ans = "Syntax(10,0,List(8191),8191,None,None,false,OTPL,None,false,true)";
    assertEquals(ans, db.getSyntax().toString());
  }

  @Test
  public void testLogTrades() throws ExtensionException {
    MatchingExtension mme = new MatchingExtension();
    LogTradesFile db = mme.new LogTradesFile();

    assertEquals(LogTradesFile.class, db.getClass());
    String ans = "Syntax(10,0,List(8191),8191,None,None,false,OTPL,None,false,true)";
    assertEquals(ans, db.getSyntax().toString());
  }

  @Test
  public void testPriceTicker() throws ExtensionException {
    MatchingExtension mme = new MatchingExtension();
    PriceTicker ab = mme.new PriceTicker();

    assertEquals(PriceTicker.class, ab.getClass());
    String ans = "Syntax(10,0,List(8191),8191,None,None,false,OTPL,None,false,true)";
    assertEquals(ans, ab.getSyntax().toString());
  }

  @Test
  public void testBestBid() throws ExtensionException {
    MatchingExtension mme = new MatchingExtension();
    BestBid ab = mme.new BestBid();

    assertEquals(BestBid.class, ab.getClass());
    String ans = "Syntax(10,0,List(8191),8191,None,None,false,OTPL,None,false,true)";
    assertEquals(ans, ab.getSyntax().toString());
  }

  @Test
  public void testBestOffer() throws ExtensionException {
    MatchingExtension mme = new MatchingExtension();
    BestOffer ab = mme.new BestOffer();

    assertEquals(BestOffer.class, ab.getClass());
    String ans = "Syntax(10,0,List(8191),8191,None,None,false,OTPL,None,false,true)";
    assertEquals(ans, ab.getSyntax().toString());
  }

  @Test
  public void testMarketMean() throws ExtensionException {
    MatchingExtension mme = new MatchingExtension();
    MarketMean ab = mme.new MarketMean();

    assertEquals(MarketMean.class, ab.getClass());
    String ans = "Syntax(10,0,List(8191),8191,None,None,false,OTPL,None,false,true)";
    assertEquals(ans, ab.getSyntax().toString());
  }

  @Test
  public void testSpread() throws ExtensionException {
    MatchingExtension mme = new MatchingExtension();
    Spread ab = mme.new Spread();

    assertEquals(Spread.class, ab.getClass());
    String ans = "Syntax(10,0,List(8191),8191,None,None,false,OTPL,None,false,true)";
    assertEquals(ans, ab.getSyntax().toString());
  }

  @Test
  public void testCountTrades() throws ExtensionException {
    MatchingExtension mme = new MatchingExtension();
    CountTrades ab = mme.new CountTrades();

    assertEquals(CountTrades.class, ab.getClass());
    String ans = "Syntax(10,0,List(8191),8191,None,None,false,OTPL,None,false,true)";
    assertEquals(ans, ab.getSyntax().toString());
  }

  @Test
  public void testCountBids() throws ExtensionException {
    MatchingExtension mme = new MatchingExtension();
    CountBids ab = mme.new CountBids();

    assertEquals(CountBids.class, ab.getClass());
    String ans = "Syntax(10,0,List(8191),8191,None,None,false,OTPL,None,false,true)";
    assertEquals(ans, ab.getSyntax().toString());
  }

  @Test
  public void testCountOffers() throws ExtensionException {
    MatchingExtension mme = new MatchingExtension();
    CountOffers ab = mme.new CountOffers();

    assertEquals(CountOffers.class, ab.getClass());
    String ans = "Syntax(10,0,List(8191),8191,None,None,false,OTPL,None,false,true)";
    assertEquals(ans, ab.getSyntax().toString());
  }
}
