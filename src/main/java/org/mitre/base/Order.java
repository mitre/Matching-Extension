package org.mitre.base;

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

	private static final Logger log = LoggerFactory.getLogger(Order.class);

	// order size
	private static Integer size;
	// name of contract
	private static String contract;
	// time submitted
	private static Date dt;

	// agent submitting
	private static String agent;


	// default constructor
	public Order() {
		setSize(0);
		setContract("");
		setAgent("");
		setDt(new Date(System.currentTimeMillis()));
		log.info("Constructed default Order");
	}

	// custom constructor
	public Order(int size, String contract, String agent) {
		setSize(size);
		setContract(contract);
		setAgent(agent);
		setDt(new Date(System.currentTimeMillis()));
		log.info("Constructed custom Order: {} SIZE of {} CONTRACt from {} AGENT",
				   size, contract, agent);
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
	public static void setSize(int size) {
		Order.size = size;
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
	public static void setContract(String contract) {
		Order.contract = contract;
	}

	/**
	 * @return
	 */
	public Date getDt() {
		return dt;
	}

	/**
	 * @param dt
	 */
	public static void setDt(Date dt) {
		Order.dt = dt;
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
	public static void setAgent(String agent) {
		Order.agent = agent;
	}

	/**
	 * @return string representation of class
	 */
	@Override
	public String toString() {
		return "Order@ " + getDt().toString() + " of SIZE="
				+ getSize() + " and CONTRACT=" + getContract()
				+ " from AGENT=" + getAgent();
	}

}