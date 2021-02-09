package org.mitre.base;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 *
 */
//@Component
public class Order {

	// order size
	//@Autowired
	private int size;

	// name of contract
	//@Autowired
	private String contract;

	// time submitted
	//@Autowired
	private Date dt;


	// default constructor
	//@Autowired
	public Order() {
		setSize(0);
		setContract("");
		setDt(new Date(System.currentTimeMillis()));
	}

	// args constructor
	//@Autowired
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