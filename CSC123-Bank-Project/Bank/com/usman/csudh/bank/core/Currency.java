package com.usman.csudh.bank.core;
import java.io.Serializable;
public class Currency implements Serializable {

	private static final long serialVersionUID = 1L;
	private String currencyCode;
	private String currencyName;
	private double exRate;

	public Currency(String currencyCode, String currencyName, double exRate) {
		this.currencyCode = currencyCode;
		this.currencyName = currencyName;
		this.exRate = exRate;
	}

	public String getCurrencyName() {
		return currencyName;
	}

	public String getCurrencyCode() {
		return currencyCode.toUpperCase();
	}

	public double getExRate() {
		return exRate;
	}

}
