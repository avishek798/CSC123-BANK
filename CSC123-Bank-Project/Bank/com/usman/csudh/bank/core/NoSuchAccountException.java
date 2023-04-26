package com.usman.csudh.bank.core;
import java.io.Serializable;
public class NoSuchAccountException extends Exception implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public NoSuchAccountException(String message) {
		super(message);
	}

}
