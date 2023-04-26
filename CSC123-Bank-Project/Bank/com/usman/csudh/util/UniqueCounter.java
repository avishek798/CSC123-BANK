package com.usman.csudh.util;

import java.io.Serializable;

public class UniqueCounter implements Serializable{
	private static final long serialVersionUID = 1L;
	private  int counterState = 1000;
	private  static UniqueCounter self=new UniqueCounter();
	
	private UniqueCounter() {}
	
	public static UniqueCounter getInstance() {
		
		return self;
	}
	public  int nextValue() {
		return counterState++;
	}

}
