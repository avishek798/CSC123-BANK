package com.usman.csudh.bank.core;

import java.io.Serializable;

public class SavingAccount extends Account implements Serializable{
	private static final long serialVersionUID = 1L;

	public SavingAccount(Customer customer, Currency currency) {
		super("Saving", customer, currency);
	}



	//Withdrawals only allowed against positive balances 
	public void withdraw(double amount) throws InsufficientBalanceException {
		if (getBalance() - amount < 0) {
			throw new InsufficientBalanceException("Not enough funds to cover withdrawal");

		}
		super.withdraw(amount);

	}

}
