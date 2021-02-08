package org.mitre.base;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 *
 */
@Component
public class Order {

	// order size
	private int size = 0;

	// name of contract
	private String contract = "";

	// time submitted
	private Date dt = new Date();


	// constructor
	@Autowired
	public Order(int size, String contract) {
		setSize(size);
		setContract(contract);
		setDt(new Date(System.currentTimeMillis()));
	}


	@Autowired
	public int getSize() {
		return size;
	}

	@Autowired
	public void setSize(int size) {
		this.size = size;
	}


	@Autowired
	public String getContract() {
		return contract;
	}

	@Autowired
	public void setContract(String contract) {
		this.contract = contract;
	}


	@Autowired
	public Date getDt() {
		return dt;
	}

	@Autowired
	public void setDt(Date dt) {
		this.dt = dt;
	}

}