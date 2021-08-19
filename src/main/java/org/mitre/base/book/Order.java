package org.mitre.base.book;

import java.time.Instant;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 *
 *
 */
@Component
public class Order {

  private final Logger log = LoggerFactory.getLogger(Order.class);

  // order size
  private Integer size;
  // order price
  private Float price;
  // name of contract
  private String contract;
  // time submitted
  private Instant dt;
  // integer time submitted
  private Integer dtInt;
  // if using Instant
  private Boolean usingInstant;

  // agent submitting
  private String agent;

  // default constructor
  public Order() {
    setSize(0);
    setPrice(0.0f);
    setContract("");
    setAgent("");
    setDtInst(new Date(System.currentTimeMillis()).toInstant());
    log.debug("Constructed default Order");
  }

  // custom constructor
  public Order(Integer size, Float price, String contract, String agent) {
    setSize(size);
    setPrice(price);
    setContract(contract);
    setAgent(agent);
    setDtInst(new Date(System.currentTimeMillis()).toInstant());
    log.debug("Constructed custom Order: {} SIZE of {} CONTRACT @ {} from {} AGENT", size, contract, price, agent);
  }

  public Order(int size, Float price, String contract, String agent, Instant inst) {
    setSize(size);
    setPrice(price);
    setContract(contract);
    setAgent(agent);
    setDtInst(inst);
    log.debug("Constructed custom Order: {} SIZE of {} CONTRACT @ {} from {} AGENT at {}", size, contract, price, agent,
        inst);
  }

  public Order(int size, Float price, String contract, String agent, Integer inst) {
    setSize(size);
    setPrice(price);
    setContract(contract);
    setAgent(agent);
    setDtInt(inst);
    log.debug("Constructed custom Order: {} SIZE of {} CONTRACT @ {} from {} AGENT at {}", size, contract, price, agent,
        inst);
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
  public Integer getSize() {
    return size;
  }

  /**
   * @param size
   */
  public void setSize(int size) {
    this.size = size;
  }

  /**
   * @return
   */
  public String getContract() {
    return contract;
  }

  /**
   * @param contract
   */
  private void setContract(String contract) {
    this.contract = contract;
  }

  /**
   * @return
   */
  public Instant getDtInst() {
    return dt;
  }

  /**
   *
   * @return
   */
  public Integer getDtInt() {
    return dtInt;
  }

  /**
   * @param dt
   */
  private void setDtInst(Instant dt) {
    this.dt = dt;
    this.dtInt = null;
    setUsingInstant(Boolean.TRUE);
  }

  /**
   *
   * @param dt
   */
  private void setDtInt(Integer dt) {
    this.dtInt = dt;
    this.dt = null;
    setUsingInstant(Boolean.FALSE);
  }

  /**
   * @return the usingInstant
   */
  private Boolean getUsingInstant() {
    return usingInstant;
  }

  /**
   * @param usingInstant the usingInstant to set
   */
  private void setUsingInstant(Boolean usingInstant) {
    this.usingInstant = usingInstant;
  }

  /**
   * @return the agent
   */
  public String getAgent() {
    return agent;
  }

  /**
   * @param agent the agent to set
   */
  private void setAgent(String agent) {
    this.agent = agent;
  }

  /**
   * @return integer whether to move left or right in sort
   */
  public static int compare(Order lhs, Order rhs) {
    // if not the same contract don't move
    if (!lhs.getContract().equals(rhs.getContract())) {
      throw new UnsupportedOperationException("Must compare Orders of same contract");
    }

    // if using both Instant and Integer throw error
    if (!lhs.getUsingInstant().equals(rhs.getUsingInstant())) {
      throw new UnsupportedOperationException("Must compare Orders of same time quantization");
    }

    // sort by price then time then size
    if (lhs.getPrice() < rhs.getPrice()) {
      return -1;
    } else if (lhs.getPrice() > rhs.getPrice()) {
      return 1;
    } else {
      // price is equal, compare size
      if (lhs.getSize() < rhs.getSize()) {
        return -1;
      } else if (lhs.getSize() > rhs.getSize()) {
        return 1;
      } else {
        // price and size are equal, compare time
        if (lhs.getDtInst().isBefore(rhs.getDtInst())) {
          return -1;
        } else if (lhs.getDtInst().isAfter(rhs.getDtInst())) {
          return 1;
        } else {
          return 0;
        }
      }
    }
  }

  /**
   * @return string representation of class
   */
  @Override
  public String toString() {
    return "Order of SIZE=" + getSize() + " and CONTRACT=" + getContract() + " @ $" + getPrice() + " from AGENT="
        + getAgent();
  }

}