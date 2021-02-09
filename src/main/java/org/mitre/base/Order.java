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

	// logger
	private static final Logger log = LoggerFactory.getLogger(Order.class);

	// order size
	private int size;

	// name of contract
	private String contract;

	// time submitted
	private Date dt;


	// default constructor
	public Order() {
		setSize(0);
		setContract("");
		setDt(new Date(System.currentTimeMillis()));
		log.info("Built default order");
	}

	// custom constructor
	public Order(int size, String contract) {
		setSize(size);
		setContract(contract);
		setDt(new Date(System.currentTimeMillis()));
		log.info("Build custom Order: size = {}, contract = {}", size, contract);
	}


	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}


	public String getContract() {
		return contract;
	}

	public void setContract(String contract) {
		this.contract = contract;
	}


	public Date getDt() {
		return dt;
	}

	public void setDt(Date dt) {
		this.dt = dt;
	}

}