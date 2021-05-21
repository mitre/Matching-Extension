/**
 *
 */
package org.mitre.base;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.mitre.base.MatchingExtension.AddBuyOrder;
import org.mitre.base.MatchingExtension.AddSellOrder;
import org.mitre.base.MatchingExtension.DefaultMatcher;
import org.mitre.base.MatchingExtension.GetBuyBook;
import org.mitre.base.MatchingExtension.GetSellBook;
import org.mitre.base.MatchingExtension.GetTrades;
import org.mitre.base.MatchingExtension.LogoMatching;
import org.mitre.base.MatchingExtension.MatchUpdate;
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

    String ans = "MatchingEngine: FLOOD_CLIMATE size(COMPLETED_TRADES)=0 size(BUY_BOOK)=0 size(SELL_BOOK)=0";
    assertEquals(ans, lm.dump(false, false, false));
    assertEquals(true, lm.recursivelyEqual(lm));

    OrderBook ob = new OrderBook();
    MatchingEngine me = new MatchingEngine();
    LogoMatching lm2 = new LogoMatching(ob, me);

    String ans2 = "MatchingEngine: FLOOD_CLIMATE size(COMPLETED_TRADES)=0 size(BUY_BOOK)=0 size(SELL_BOOK)=0";
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

    String ans2 = "MatchingEngine: FLOOD_CLIMATE size(COMPLETED_TRADES)=0 size(BUY_BOOK)=0 size(SELL_BOOK)=0";
    assertEquals(ans2, m.report(null, null).toString());
  }

  @Test
  public void testAddBuyOrder() throws ExtensionException {
    MatchingExtension mme = new MatchingExtension();
    AddBuyOrder ab = mme.new AddBuyOrder();

    assertEquals(AddBuyOrder.class, ab.getClass());
    String ans = "Syntax(10,0,List(8191, 1, 1, 4, 4),8191,None,None,false,OTPL,None,false,true)";
    assertEquals(ans, ab.getSyntax().toString());
  }

  @Test
  public void testAddSellOrder() throws ExtensionException {
    MatchingExtension mme = new MatchingExtension();
    AddSellOrder ab = mme.new AddSellOrder();

    assertEquals(AddSellOrder.class, ab.getClass());
    String ans = "Syntax(10,0,List(8191, 1, 1, 4, 4),8191,None,None,false,OTPL,None,false,true)";
    assertEquals(ans, ab.getSyntax().toString());
  }

  @Test
  public void testGetBuyBook() throws ExtensionException {
    MatchingExtension mme = new MatchingExtension();
    GetBuyBook ab = mme.new GetBuyBook();

    assertEquals(GetBuyBook.class, ab.getClass());
    String ans = "Syntax(10,0,List(8191),8191,None,None,false,OTPL,None,false,true)";
    assertEquals(ans, ab.getSyntax().toString());
  }

  @Test
  public void testGetSellBook() throws ExtensionException {
    MatchingExtension mme = new MatchingExtension();
    GetSellBook ab = mme.new GetSellBook();

    assertEquals(GetSellBook.class, ab.getClass());
    String ans = "Syntax(10,0,List(8191),8191,None,None,false,OTPL,None,false,true)";
    assertEquals(ans, ab.getSyntax().toString());
  }

  @Test
  public void testGetTrades() throws ExtensionException {
    MatchingExtension mme = new MatchingExtension();
    GetTrades ab = mme.new GetTrades();

    assertEquals(GetTrades.class, ab.getClass());
    String ans = "Syntax(10,0,List(8191),8191,None,None,false,OTPL,None,false,true)";
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
}
