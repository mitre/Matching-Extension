package org.mitre.base;

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
	// name of contract
	private String contract;
	// time submitted
	private Instant dt;

	// agent submitting
	private String agent;


	// default constructor
	public Order() {
		setSize(0);
		setContract("");
		setAgent("");
		setDt(new Date(System.currentTimeMillis()).toInstant());
		log.info("Constructed default Order");
	}

	// custom constructor
	public Order(int size, String contract, String agent) {
		setSize(size);
		setContract(contract);
		setAgent(agent);
		setDt(new Date(System.currentTimeMillis()).toInstant());
		log.info("Constructed custom Order: {} SIZE of {} CONTRACT from {} AGENT",
				   size, contract, agent);
	}

	public Order(int size, String contract, String agent, Instant inst) {
		setSize(size);
		setContract(contract);
		setAgent(agent);
		setDt(inst);
		log.info("Constructed custom Order: {} SIZE of {} CONTRACT from {} AGENT at {}",
				   size, contract, agent, inst);
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
	public void setContract(String contract) {
		this.contract = contract;
	}

	/**
	 * @return
	 */
	public Instant getDt() {
		return dt;
	}

	/**
	 * @param dt
	 */
	public void setDt(Instant dt) {
		this.dt = dt;
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
	public void setAgent(String agent) {
		this.agent = agent;
	}

	/**
	 * @return integer whether to move left or right in sort
	 */
	public static int compare(Order lhs, Order rhs) {
		// if not the same contract don't move
		if (!lhs.getContract().equals(rhs.getContract())) {
			return 0;
		}

		// sort by time first then size
		if (lhs.getDt().isBefore(rhs.getDt())) {
			return -1;
		} else if (lhs.getDt().isAfter(rhs.getDt())) {
			return 1;
		} else {
			// in this case equal priority base on time
			if (lhs.getSize() < rhs.getSize()) {
				return -1;
			} else if (lhs.getSize() > rhs.getSize()) {
				return 1;
			} else {
				return 0;
			}
		}
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