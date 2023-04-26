package com.usman.csudh.bank.core;
import java.io.Serializable;
public class InsufficientBalanceException extends Exception implements Serializable {
	
	private static final long serialVersionUID = 1L;


	public InsufficientBalanceException(String message) {
		super(message);
	}

}
