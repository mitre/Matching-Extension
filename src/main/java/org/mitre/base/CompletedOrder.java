package org.mitre.base;

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
	// contract name
	private String contract;
	// time of order close
	private Instant closeDt;

	// agent that is buying
	private String buyAgent;
	// agent that is selling
	private String sellAgent;


	// default constructor
	public CompletedOrder() {
		setSize(0);
		setContract("none");
		setCloseDt(new Date(System.currentTimeMillis()).toInstant());
		setBuyAgent("testBuyer");
		setSellAgent("testSeller");
		log.info("Constructed default CompletedOrder");
	}

	// custom constructor
	public CompletedOrder(int size, String contract, String buyAgent, String sellAgent) {
		setSize(size);
		setContract(contract);
		setCloseDt(new Date(System.currentTimeMillis()).toInstant());
		setBuyAgent(buyAgent);
		setSellAgent(sellAgent);
		log.info("Constructed CompletedOrder: {} SIZE of {} CONTRACT between AGENTS {} and {}",
				   size, contract, buyAgent, sellAgent);
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
	public void setSize(int size) {
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
	public void setContract(String contract) {
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
	public void setCloseDt(Instant closeDt) {
		this.closeDt = closeDt;
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
	public void setBuyAgent(String buyAgent) {
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
	public void setSellAgent(String sellAgent) {
		this.sellAgent = sellAgent;
	}

	/**
	 * @return string representation of class
	 */
	@Override
	public String toString() {
		return "CompletedOrder@ " + getCloseDt().toString() + " of SIZE="
				+ getSize() + " and CONTRACT=" + getContract()
				+ " between SELLER=" + getSellAgent() + " and BUYER="
				+ getBuyAgent();
	}
}
