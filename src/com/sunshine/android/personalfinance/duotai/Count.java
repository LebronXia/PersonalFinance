package com.sunshine.android.personalfinance.duotai;

import java.io.Serializable;

public class Count implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private String type;
	private String beizhu;
	private String date;
	private double money;
	private double sum;
	
	
//	public Count() {
//		super();
//	}
	public Count(String name, String type, String beizhu, double money) {
		super();
		this.name = name;
		this.type = type;
		this.beizhu = beizhu;
		this.money = money;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getBeizhu() {
		return beizhu;
	}


	public void setBeizhu(String beizhu) {
		this.beizhu = beizhu;
	}


	public double getMoney() {
		return money;
	}


	public void setMoney(double money) {
		this.money = money;
	}


	public double getSum() {
		return sum;
	}


	public void setSum(double sum) {
		this.sum = sum;
	}

	
	
}
