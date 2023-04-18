package com.usman.csudh.bank.core;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import com.usman.csudh.util.UniqueCounter;

public class Account implements Serializable {

	private static final long serialVersionUID = 1L;
	private String accountName;
	private Customer accountHolder;
	private Currency accCurrency;
	private ArrayList<Transaction> transactions;

	private boolean open = true;
	private int accountNumber;

	protected Account(String name, Customer customer, Currency accCurrency) {
		accountName = name;
		accountHolder = customer;
		this.accCurrency = accCurrency;
		transactions = new ArrayList<Transaction>();
		accountNumber = UniqueCounter.getInstance().nextValue();
	}
	
	public String getAccountHolderName() {
		return accountHolder.getFirstName()+" "+accountHolder.getLastName();
	}
	
	public String getAccountHolderSSN() {
		return accountHolder.getSSN();
	}

	public String getAccountName() {
		return accountName;
	}

	public Customer getAccountHolder() {
		return accountHolder;
	}

	public double getBalance() {
		double workingBalance = 0;

		for (Transaction t : transactions) {
			if (t.getType() == Transaction.CREDIT)
				workingBalance += t.getAmount();
			else
				workingBalance -= t.getAmount();
		}

		return workingBalance;
	}
	
	public String getCurrencyName() {
		return accCurrency.getCurrencyName();
	}

	public double getCurrencyBalance() {

		if (accCurrency.getCurrencyCode().equalsIgnoreCase("USD")) {
			return getBalance() / accCurrency.getExRate();
		} else {
			return getBalance() * accCurrency.getExRate();
		}
	}

	public void deposit(double amount) throws AccountClosedException {
		double balance = getBalance();
		if (!isOpen() && balance >= 0) {
			throw new AccountClosedException("\nAccount is closed with positive balance, deposit not allowed!\n\n");
		}
		transactions.add(new Transaction(Transaction.CREDIT, amount));
	}

	public void withdraw(double amount) throws InsufficientBalanceException {

		double balance = getBalance();

		if (!isOpen() && balance <= 0) {
			throw new InsufficientBalanceException("\nThe account is closed and balance is: " + balance + "\n\n");
		}

		transactions.add(new Transaction(Transaction.DEBIT, amount));
	}

	public void close() {
		open = false;
	}

	public boolean isOpen() {
		return open;
	}

	public int getAccountNumber() {
		return accountNumber;
	}

	public String toString() {
		
		if (accCurrency.getCurrencyCode().equalsIgnoreCase("USD")) {
		String aName = accountNumber + "(" + accountName + ")" + " : " + accountHolder.toString() + " : "
				+ accCurrency.getCurrencyCode() + " : " + String.format("%.2f", getBalance()) + " : " + String.format("%.2f", getCurrencyBalance()) + " : "
				+ (open ? "Account Open" : "Account Closed");
		return aName;

		}
		else {
			String aName = accountNumber + "(" + accountName + ")" + " : " + accountHolder.toString() + " : "
					+ accCurrency.getCurrencyCode() + " : " + String.format("%.2f",getCurrencyBalance()) + " : " + String.format("%.2f", getBalance()) + " : "
					+ (open ? "Account Open" : "Account Closed");
			return aName;

		}
	}

	public void printTransactions(OutputStream out) throws IOException {

		out.write("\n\n".getBytes());
		out.write("------------------\n".getBytes());

		for (Transaction t : transactions) {
			out.write(t.toString().getBytes());
			out.write((byte) 10);
		}
		out.write("------------------\n".getBytes());
		out.write(("Balance: " + getBalance() + "\n\n\n").getBytes());
		out.flush();

	}
}
