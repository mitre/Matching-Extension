package org.mitre.base;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CompletedOrder {

	private static final Logger log = LoggerFactory.getLogger(CompletedOrder.class);

	// order size
	private static int size;
	// contract name
	private static String contract;
	// time of order close
	private static Date closeDt;

	// agent that is buying
	private static String buyAgent;
	// agent that is selling
	private static String sellAgent;


	// default constructor
	public CompletedOrder() {
		setSize(0);
		setContract("none");
		setCloseDt(new Date(System.currentTimeMillis()));
		setBuyAgent("testBuyer");
		setSellAgent("testSeller");
		log.info("Constructed default CompletedOrder");
	}

	// custom constructor
	public CompletedOrder(int size, String contract, String buyAgent, String sellAgent) {
		setSize(size);
		setContract(contract);
		setCloseDt(new Date(System.currentTimeMillis()));
		setBuyAgent(buyAgent);
		setSellAgent(sellAgent);
		log.info("Constructed CompletedOrder: {} SIZE of {} CONTRACT between AGENTS {} and {}",
				   size, contract, buyAgent, sellAgent);
	}


	/**
	 * @return the size
	 */
	public int getSize() {
		return size;
	}

	/**
	 * @param size the size to set
	 */
	public static void setSize(int size) {
		CompletedOrder.size = size;
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
	public static void setContract(String contract) {
		CompletedOrder.contract = contract;
	}

	/**
	 * @return the closeDt
	 */
	public static Date getCloseDt() {
		return closeDt;
	}

	/**
	 * @param closeDt the closeDt to set
	 */
	public static void setCloseDt(Date closeDt) {
		CompletedOrder.closeDt = closeDt;
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
	public static void setBuyAgent(String buyAgent) {
		CompletedOrder.buyAgent = buyAgent;
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
	public static void setSellAgent(String sellAgent) {
		CompletedOrder.sellAgent = sellAgent;
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
