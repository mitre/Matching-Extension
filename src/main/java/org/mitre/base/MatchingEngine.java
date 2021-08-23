package org.mitre.base;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import org.apache.commons.math3.util.Precision;
import org.mitre.base.book.CompletedOrder;
import org.mitre.base.book.MarketPropertiesParser;
import org.mitre.base.book.Order;
import org.mitre.base.book.OrderBook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Component
public class MatchingEngine {

  // logger
  private final Logger log = LoggerFactory.getLogger(MatchingEngine.class);

  // configuration properties, default to flood
  private static final String DEFAULT_MARKET = "floods";

  @Autowired
  private Properties props = new MarketPropertiesParser(DEFAULT_MARKET).getProp();

  // spread tolerance
  private Float spreadTol;

  // order books
  private HashMap<Integer, Order> buyBook;
  private HashMap<Integer, Order> sellBook;

  // sorted order books
  private List<Pair<Integer, Order>> buyBookSorted;
  private List<Pair<Integer, Order>> sellBookSorted;

  // record of matches
  private ArrayList<CompletedOrder> trades;

  // default constructor
  public MatchingEngine() {
    initBooks();
    initTrades();
    initProps(DEFAULT_MARKET);
    log.debug("Constructed default MatchingEngine");
  }

  // custom constructor
  public MatchingEngine(OrderBook ob) {
    setBuyBook(ob.getBuyBook());
    setSellBook(ob.getSellBook());
    initTrades();
    initSortedBooks();
    initProps(DEFAULT_MARKET);
    log.debug("Constructed OrderBook custom MatchingEngine. BUY_BOOK={} SELL_BOOK={}", buyBook, sellBook);
  }

  // custom constructor 2
  public MatchingEngine(OrderBook ob, String template) {
    setBuyBook(ob.getBuyBook());
    setSellBook(ob.getSellBook());
    initTrades();
    initSortedBooks();
    initProps(template);
    log.debug("Constructed OrderBook, config custom MatchingEngine. BUY_BOOK={} SELL_BOOK={}  CONFIG={}", buyBook,
        sellBook, props);
  }

  /**
   * initialize properties file
   */
  private void initProps(String template) {
    props = new MarketPropertiesParser(template).getProp();

    if (props.containsKey("tolerance")) {
      spreadTol = Float.parseFloat(props.getProperty("tolerance"));
    }
  }

  /**
   * initialize empty buy and sell books
   */
  private void initBooks() {
    this.buyBook = Maps.newHashMap();
    this.sellBook = Maps.newHashMap();
  }

  /**
   * initialize empty list
   */
  private void initTrades() {
    this.trades = Lists.newArrayList();
  }

  /**
   * set sorted books to empty
   */
  private void initSortedBooks() {
    this.buyBookSorted = new ArrayList<>();
    this.sellBookSorted = new ArrayList<>();
  }

  /**
   * Pair implementation
   */
  private class Pair<T, V> {
    private T key;
    private V value;

    public Pair(T t1, V v1) {
      this.key = t1;
      this.value = v1;
    }

    public T getKey() {
      return this.key;
    }

    public V getValue() {
      return this.value;
    }

    @Override
    public String toString() {
      return getValue().toString();
    }
  }

  /**
   * comparator for list of Pair<Integer, Order>
   */
  private static Comparator<Pair<Integer, Order>> cmp = (lhs, rhs) -> Order.compare(lhs.getValue(), rhs.getValue());

  /**
   * @param: sorted lists of Pairs of order books (buy/sell)
   * @return matches of trades that will be executed
   *
   *         matching part of the clearing house algorithm
   */
  private ArrayList<Pair<Integer, Integer>> findMatches(List<Pair<Integer, Order>> buyOrders,
      List<Pair<Integer, Order>> sellOrders) {
    // pair is <buyBook index, sellBook index>
    ArrayList<Pair<Integer, Integer>> matches = Lists.newArrayList();

    // guaranteed to have something in each.. or no matches possible
    Iterator<Pair<Integer, Order>> buyItr = buyOrders.iterator();
    Iterator<Pair<Integer, Order>> sellItr = sellOrders.iterator();

    // find the first pair of each book
    Pair<Integer, Order> tmpB = buyItr.next();
    Pair<Integer, Order> tmpS = sellItr.next();

    // now record index and order from each book to match
    Integer sellIdx = tmpS.getKey();
    Integer buyIdx = tmpB.getKey();
    Order sellOrd = tmpS.getValue();
    Order buyOrd = tmpB.getValue();
    Integer buyLeftover = buyOrd.getSize();
    Integer sellLeftover = sellOrd.getSize();

    // actual matching loop
    while (!buyLeftover.equals(0) && !sellLeftover.equals(0)) {
      // now see if the orders prices are within margin
      if ((buyOrd.getContract().equals(sellOrd.getContract()))
          && (sellOrd.getPrice() - buyOrd.getPrice() < spreadTol)) {
        if (buyLeftover < sellLeftover) {
          sellLeftover -= buyLeftover;
          buyLeftover = 0;
        } else if (buyLeftover > sellLeftover) {
          buyLeftover -= sellLeftover;
          sellLeftover = 0;
        } else {
          buyLeftover = 0;
          sellLeftover = 0;
        }
        matches.add(new Pair<>(buyIdx, sellIdx));
      } else {
        return matches;
      }

      // update for next iteration
      if (sellLeftover.equals(0) && sellItr.hasNext()) {
        // sell order is finished
        tmpS = sellItr.next();
        sellIdx = tmpS.getKey();
        sellOrd = tmpS.getValue();
        sellLeftover = sellOrd.getSize();
      }
      if (buyLeftover.equals(0) && buyItr.hasNext()) {
        // buy order is finished
        tmpB = buyItr.next();
        buyIdx = tmpB.getKey();
        buyOrd = tmpB.getValue();
        buyLeftover = buyOrd.getSize();
      }
    }

    // pair is <buyBook index, sellBook index>
    return matches;
  }

  /**
   * @param: matches list
   *
   *                 parse matches list into CompletedOrder objects in trades
   */
  private void logTrades(ArrayList<Pair<Integer, Integer>> matches) {
    // iterate though the matches logging them
    for (Pair<Integer, Integer> el : matches) {
      Order buy = getBuyBook().get(el.getKey());
      Order sell = getSellBook().get(el.getValue());

      if (buy.getUsingInstant().booleanValue()) {
        this.addTrade(new CompletedOrder(Math.min(buy.getSize(), sell.getSize()),
            Precision.round((buy.getPrice() + sell.getPrice()) / 2, 6), buy.getContract(), buy.getAgent(),
            sell.getAgent()));
      } else {
        this.addTrade(new CompletedOrder(Math.min(buy.getSize(), sell.getSize()),
            Precision.round((buy.getPrice() + sell.getPrice()) / 2, 6), buy.getContract(), buy.getAgent(),
            sell.getAgent(), Math.max(buy.getDtInt(), sell.getDtInt()) + 1));
      }

      removeFilledOrders(el);
    }
  }

  /**
   * @param: matches list
   *
   *                 remove filled orders based on matches list from order books
   */
  private void removeFilledOrders(Pair<Integer, Integer> match) {
    // save the buy and sell sizes
    int buySize = getBuyBook().get(match.getKey()).getSize();
    int sellSize = getSellBook().get(match.getValue()).getSize();

    if (buySize < sellSize) {
      getBuyBook().remove(match.getKey());
      getSellBook().get(match.getValue()).setSize(sellSize - buySize);
    } else if (buySize > sellSize) {
      getSellBook().remove(match.getValue());
      getBuyBook().get(match.getKey()).setSize(buySize - sellSize);
    } else {
      getBuyBook().remove(match.getKey());
      getSellBook().remove(match.getValue());
    }
  }

  /**
   *
   * search for matches between buy and sell book, then log completed trades to
   * completed trades log
   *
   */
  public void matchUpdate() {
    if (getBuyBook().isEmpty() || getSellBook().isEmpty()) {
      return;
    }

    List<Pair<Integer, Order>> buyOrders = getBuyBook().entrySet().stream()
        .map(e -> new Pair<Integer, Order>(e.getKey(), e.getValue())).collect(Collectors.toList());
    List<Pair<Integer, Order>> sellOrders = getSellBook().entrySet().stream()
        .map(e -> new Pair<Integer, Order>(e.getKey(), e.getValue())).collect(Collectors.toList());

    Collections.sort(buyOrders, Collections.reverseOrder(cmp));
    Collections.sort(sellOrders, cmp);

    setBuyBookSorted(buyOrders);
    setSellBookSorted(sellOrders);

    logTrades(findMatches(buyOrders, sellOrders));
  }

  /**
   * @return the spreadTol
   */
  public Float getSpreadTol() {
    return spreadTol;
  }

  /**
   * @param: Order to add to trades list
   */
  public void addTrade(CompletedOrder order) {
    this.trades.add(order);
  }

  /**
   * @return: all CompletedOrders in trades
   */
  public List<CompletedOrder> getAllTrades() {
    return this.trades;
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
   * @param buyBook the buyBook to set
   */
  private void setBuyBook(Map<Integer, Order> buyBook) {
    this.buyBook = (HashMap<Integer, Order>) buyBook;
  }

  /**
   * @param sellBook the sellBook to set
   */
  private void setSellBook(Map<Integer, Order> sellBook) {
    this.sellBook = (HashMap<Integer, Order>) sellBook;
  }

  /**
   * @return the buyBookSorted
   */
  public List<Pair<Integer, Order>> getBuyBookSorted() {
    return buyBookSorted;
  }

  /**
   * @param buyBookSorted the buyBookSorted to set
   */
  private void setBuyBookSorted(List<Pair<Integer, Order>> buyBookSorted) {
    this.buyBookSorted = buyBookSorted;
  }

  /**
   * @return the sellBookSorted
   */
  public List<Pair<Integer, Order>> getSellBookSorted() {
    return sellBookSorted;
  }

  /**
   * @param sellBookSorted the sellBookSorted to set
   */
  private void setSellBookSorted(List<Pair<Integer, Order>> sellBookSorted) {
    this.sellBookSorted = sellBookSorted;
  }

  /**
   * print the MatchingEngine to string
   */
  @Override
  public String toString() {
    return "MatchingEngine: " + props.getProperty("name") + " size(COMPLETED_TRADES)=" + getAllTrades().size()
        + " size(BUY_BOOK)=" + getBuyBook().size() + " size(SELL_BOOK)=" + getSellBook().size();
  }

  /**
   *
   * @param in
   * @return
   */
  private Float roundF(Float in) {
    return Precision.round(in, 3);
  }

  ///////////////////////////////////////////////////////////////////////////
  // ----------------------- NetLogo Plotting Methods -------------------- //
  ///////////////////////////////////////////////////////////////////////////

  /**
   *
   * @return
   */
  public Float getSpread() {
    if (!getBuyBookSorted().isEmpty() && !getSellBookSorted().isEmpty()) {
      return Math
          .abs(getSellBookSorted().get(0).getValue().getPrice() - getBuyBookSorted().get(0).getValue().getPrice());
    } else {
      return Float.NaN;
    }
  }

  /**
   *
   * @return
   */
  public Integer countBuys() {
    return getBuyBook().size();
  }

  /**
   *
   * @return
   */
  public Integer countSells() {
    return getSellBook().size();
  }

  /**
   *
   * @return
   */
  public Integer countTrades() {
    return getAllTrades().size();
  }

  /**
   *
   * @return
   */
  public Float getMarketMean() {
    Integer totalSize = 0;
    Float marketSum = 0.0f;

    // sum all bids/offers
    for (Order ord : getBuyBook().values()) {
      marketSum += (ord.getPrice() * ord.getSize());
      totalSize += ord.getSize();
    }
    for (Order ord : getSellBook().values()) {
      marketSum += (ord.getPrice() * ord.getSize());
      totalSize += ord.getSize();
    }

    if (totalSize.equals(0)) {
      return Float.NaN;
    }

    return roundF(marketSum / totalSize);
  }

  /**
   *
   * @return
   */
  public Float lastFillPrice() {
    if (!getAllTrades().isEmpty()) {
      return roundF(getAllTrades().get(getAllTrades().size() - 1).getPrice());
    } else {
      return Float.NaN;
    }
  }

  /**
   *
   * @return
   */
  public Float bestBuyPrice() {
    if (!getBuyBookSorted().isEmpty()) {
      return roundF(getBuyBookSorted().get(0).getValue().getPrice());
    } else {
      return Float.NaN;
    }
  }

  /**
   *
   * @return
   */
  public Float bestSellPrice() {
    if (!getSellBookSorted().isEmpty()) {
      return roundF(getSellBookSorted().get(0).getValue().getPrice());
    } else {
      return Float.NaN;
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  // ----------------------- NetLogo File Log Methods -------------------- //
  ///////////////////////////////////////////////////////////////////////////

  /**
   *
   */
  public void logTradesToFile() throws IOException {
    String fileName = "test-file.csv";
    Path path = Paths.get(fileName);
    if (!Files.exists(path)) {
      Files.createFile(path);
    }

    Files.write(path, CompletedOrder.getHeader().getBytes(), StandardOpenOption.APPEND);
    for (CompletedOrder co : trades) {
      Files.write(path, co.toFile().getBytes(), StandardOpenOption.APPEND);
    }
  }

}
