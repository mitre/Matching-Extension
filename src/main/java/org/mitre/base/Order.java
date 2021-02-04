package org.mitre.base;

import java.util.Date;


public class Order {

	// order size
	private int size = 0;

	// name of contract
	private String contract = "";

	// time submitted
	private Date dt = new Date();


	// constructor
	public Order(int size, String contract) {
		setSize(size);
		setContract(contract);
		setDt(new Date(System.currentTimeMillis()));
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