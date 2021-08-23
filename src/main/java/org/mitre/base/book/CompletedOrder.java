package org.mitre.base.book;

import java.time.Instant;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CompletedOrder {

  private final Logger log = LoggerFactory.getLogger(CompletedOrder.class);

  // order size
  private Integer size;
  // order price
  private Float price;
  // contract name
  private String contract;
  // time of order close
  private Instant closeDt;
  // integer time of order close
  private Integer closeDtInt;
  // if we are using integer
  private Boolean usingInstant;

  // agent that is buying
  private String buyAgent;
  // agent that is selling
  private String sellAgent;

  // file output header
  private static final String FILE_HEADER = "time, contract, size, price, buyAgent, sellAgent\n";

  // default constructor
  public CompletedOrder() {
    setSize(0);
    setPrice(0.0f);
    setContract("none");
    setCloseDt(new Date(System.currentTimeMillis()).toInstant());
    setBuyAgent("testBuyer");
    setSellAgent("testSeller");
    log.debug("Constructed default CompletedOrder.");
  }

  // custom constructor
  public CompletedOrder(Integer size, Float price, String contract, String buyAgent, String sellAgent) {
    setSize(size);
    setPrice(price);
    setContract(contract);
    setCloseDt(new Date(System.currentTimeMillis()).toInstant());
    setBuyAgent(buyAgent);
    setSellAgent(sellAgent);
    log.debug("Constructed UTC.now() close CompletedOrder.");
  }

  // custom constructor with time stamp
  public CompletedOrder(Integer size, Float price, String contract, String buyAgent, String sellAgent, Instant close) {
    setSize(size);
    setPrice(price);
    setContract(contract);
    setCloseDt(close);
    setBuyAgent(buyAgent);
    setSellAgent(sellAgent);
    log.debug("Constructed Instant close CompletedOrder.");
  }

  // custom constructor with Integer time stamp
  public CompletedOrder(Integer size, Float price, String contract, String buyAgent, String sellAgent, Integer close) {
    setSize(size);
    setPrice(price);
    setContract(contract);
    setCloseDtInt(close);
    setBuyAgent(buyAgent);
    setSellAgent(sellAgent);
    log.debug("Constructed Integer close CompletedOrder.");
  }

  /**
   * @return the size
   */
  public Integer getSize() {
    return size;
  }

  /**
   * @param size the size to set
   */
  private void setSize(int size) {
    this.size = size;
  }

  /**
   * @return the contract
   */
  public String getContract() {
    return contract;
  }

  /**
   * @param contract the contract to set
   */
  private void setContract(String contract) {
    this.contract = contract;
  }

  /**
   * @return the closeDt
   */
  public Instant getCloseDt() {
    return closeDt;
  }

  /**
   * @param closeDt the closeDt to set
   */
  private void setCloseDt(Instant closeDt) {
    this.closeDt = closeDt;
    setUsingInstant(Boolean.TRUE);
  }

  /**
   * @return the closeDtInt
   */
  public Integer getCloseDtInt() {
    return closeDtInt;
  }

  /**
   * @param closeDtInt the closeDtInt to set
   */
  public void setCloseDtInt(Integer closeDtInt) {
    this.closeDtInt = closeDtInt;
    setUsingInstant(Boolean.FALSE);
  }

  /**
   * @return the usingInstant
   */
  public Boolean getUsingInstant() {
    return usingInstant;
  }

  /**
   * @param usingInstant the usingInstant to set
   */
  public void setUsingInstant(Boolean usingInstant) {
    this.usingInstant = usingInstant;
  }

  /**
   * @return the buyAgent
   */
  public String getBuyAgent() {
    return buyAgent;
  }

  /**
   * @param buyAgent the buyAgent to set
   */
  private void setBuyAgent(String buyAgent) {
    this.buyAgent = buyAgent;
  }

  /**
   * @return the sellAgent
   */
  public String getSellAgent() {
    return sellAgent;
  }

  /**
   * @param sellAgent the sellAgent to set
   */
  private void setSellAgent(String sellAgent) {
    this.sellAgent = sellAgent;
  }

  /**
   * @return the price
   */
  public Float getPrice() {
    return price;
  }

  /**
   * @param price the price to set
   */
  private void setPrice(Float price) {
    this.price = price;
  }

  /**
   * @return
   */
  public String toFile() {
    String time = "";
    if (getUsingInstant().booleanValue()) {
      time = getCloseDt().toString();
    } else {
      time = getCloseDtInt().toString();
    }

    return time + ", " + getContract() + ", " + getSize() + ", " + getPrice() + ", " + getBuyAgent() + ", "
        + getSellAgent() + "\n";
  }

  /**
   * @return the fILE_HEADER
   */
  public static String getHeader() {
    return FILE_HEADER;
  }

  /**
   * @return string representation of class
   */
  @Override
  public String toString() {
    return "CompletedOrder@ " + getCloseDt().toString() + " of SIZE=" + getSize() + " and CONTRACT=" + getContract()
        + " @ $" + getPrice() + " between SELLER=" + getSellAgent() + " and BUYER=" + getBuyAgent();
  }

}
