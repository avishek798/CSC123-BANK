package com.usman.csudh.bank;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.usman.csudh.bank.core.Account;
import com.usman.csudh.bank.core.AccountClosedException;
import com.usman.csudh.bank.core.Bank;
import com.usman.csudh.bank.core.InsufficientBalanceException;
import com.usman.csudh.bank.core.NoSuchAccountException;
import com.usman.csudh.bank.core.CurrencyConversionException;
import com.usman.csudh.util.UIManager;

public class MainBank {

	// All messages are declared as constants to make it easier to change. Also, to
	// ensure future proofing in case the application need to be made available
	// in more than one languages
	public static final String MSG_ACCOUNT_OPENED = "%nAccount opened, account number is: %s%n%n";
	public static final String MSG_ACCOUNT_CLOSED = "%nAccount number %s has been closed, balance is %s%n%n";
	public static final String MSG_ACCOUNT_NOT_FOUND = "%nAccount number %s not found! %n%n";
	public static final String MSG_FIRST_NAME = "Enter first name:  ";
	public static final String MSG_LAST_NAME = "Enter last name:  ";
	public static final String MSG_SSN = "Enter Social Security Number:  ";
	public static final String MSG_ACCOUNT_NAME = "Enter account name:  ";
	public static final String MSG_ACCOUNT_OD_LIMIT = "Enter overdraft limit:  ";
	public static final String MSG_ACCOUNT_CREDIT_LIMIT = "Enter credit limit:  ";
	public static final String MSG_AMOUNT = "Enter amount: ";
	public static final String MSG_ACCOUNT_NUMBER = "Enter account number: ";
	public static final String MSG_ACCOUNT_ACTION = "%n%s was %s, account balance is: %s%n%n";
	public static final String MSG_CURRENCYFILE_NOT_FOUND = "** Currency file could not be loaded, "
			+ "Currency Conversion Service and Foreign Currency accounts are not available **%n";
	public static final String MSG_CURRENCY_CONVERSION = "The exchange rate is %s and you will get %s %s.%n";
	public static final String MSG_CURRENCY_SELL = "The currency you are selling: %n";
	public static final String MSG_AMMOUNT_SELL = "The ammount you are selling: %n";
	public static final String MSG_CURRENCY_BUY = "The currency you are buying: %n";
	public static final String MSG_ACCOUNT_CURRENCY = "Account Currency: ";

	// Declare main menu and prompt to accept user input
	public static final String[] menuOptions = { "Open Checking Account%n", "Open Saving Account%n", "List Accounts%n",
			"Account Statement%n", "Show Account Information%n", "Deposit Funds%n", "Withdraw Funds%n",
			"Currency Conversion%n", "Close an Account%n", "Exit%n" };
	public static final String MSG_PROMPT = "%nEnter choice: ";

	// Declare streams to accept user input / provide output
	InputStream in;
	OutputStream out;

	// Constructor
	public MainBank(InputStream in, OutputStream out) {
		this.in = in;
		this.out = out;
	}

	// Main method.
	public static void main(String[] args) {

		new MainBank(System.in, System.out).run();

	}

	// The core of the program responsible for providing user experience.
	public void run() {

		int option = 0;

		UIManager ui = new UIManager(this.in, this.out, menuOptions, MSG_PROMPT);

		try {

			do {
				try {
					Bank.readBankData();
				} catch (ClassNotFoundException e) {
					this.handleException(ui, e);
				}
				Bank.readCurrencyFile();

				option = ui.getMainOption(); // Render main menu

				switch (option) {
				case 1:

					// Compact statement to accept user input, open account, and print the result
					// including the account number

					try {
						if (Bank.fileloaded()) {
							ui.print(MSG_ACCOUNT_OPENED,
									new Object[] { Bank.openCheckingAccount(ui.readToken(MSG_FIRST_NAME),
											ui.readToken(MSG_LAST_NAME), ui.readToken(MSG_SSN),
											ui.readDouble(MSG_ACCOUNT_OD_LIMIT), ui.readToken(MSG_ACCOUNT_CURRENCY))
											.getAccountNumber() });

						} else {
							ui.print(MSG_ACCOUNT_OPENED, new Object[] { Bank
									.openCheckingAccount(ui.readToken(MSG_FIRST_NAME), ui.readToken(MSG_LAST_NAME),
											ui.readToken(MSG_SSN), ui.readDouble(MSG_ACCOUNT_OD_LIMIT), "USD")
									.getAccountNumber() });
						}
					} catch (CurrencyConversionException e) {
						this.handleException(ui, e);
					}

					break;
				case 2:

					// Compact statement to accept user input, open account, and print the result
					// including the account number
					try {
						if (Bank.fileloaded()) {
							ui.print(MSG_ACCOUNT_OPENED, new Object[] { Bank
									.openSavingAccount(ui.readToken(MSG_FIRST_NAME), ui.readToken(MSG_LAST_NAME),
											ui.readToken(MSG_SSN), ui.readToken(MSG_ACCOUNT_CURRENCY))
									.getAccountNumber() });
						} else {
							ui.print(MSG_ACCOUNT_OPENED,
									new Object[] { Bank
											.openSavingAccount(ui.readToken(MSG_FIRST_NAME),
													ui.readToken(MSG_LAST_NAME), ui.readToken(MSG_SSN), "USD")
											.getAccountNumber() });
						}
					} catch (CurrencyConversionException e) {
						this.handleException(ui, e);
					}
					break;

				case 3:

					// Get bank to print list of accounts to the output stream provided as method
					// argument
					Bank.listAccounts(this.out);
					break;

				case 4:

					// find account and get the account to print transactions to the output stream
					// provided in method arguments
					try {
						Bank.printAccountTransactions(ui.readInt(MSG_ACCOUNT_NUMBER), this.out);
					} catch (NoSuchAccountException e1) {
						this.handleException(ui, e1);

					}

					break;

				case 5:

					try {
						Bank.showAccInformation(ui.readInt(MSG_ACCOUNT_NUMBER), this.out);
					} catch (NoSuchAccountException e1) {
						this.handleException(ui, e1);

					}

					break;
				case 6:
					// find account, deposit money and print result

					try {
						int accountNumber = ui.readInt(MSG_ACCOUNT_NUMBER);
						Bank.makeDeposit(accountNumber, ui.readDouble(MSG_AMOUNT));
						ui.print(MSG_ACCOUNT_ACTION,
								new Object[] { "Deposit", "successful", Bank.getBalance(accountNumber) });
					} catch (NoSuchAccountException | AccountClosedException e) {
						this.handleException(ui, e);

					}
					break;

				case 7:
					// find account, withdraw money and print result
					try {
						int accountNumber = ui.readInt(MSG_ACCOUNT_NUMBER);
						Bank.makeWithdrawal(accountNumber, ui.readDouble(MSG_AMOUNT));
						ui.print(MSG_ACCOUNT_ACTION,
								new Object[] { "Withdrawal", "successful", Bank.getBalance(accountNumber) });

					} catch (NoSuchAccountException | InsufficientBalanceException e) {
						this.handleException(ui, e);

					}
					break;
				case 8:

					try {
						String sellCurrency = ui.readToken(MSG_CURRENCY_SELL);
						Double sellAmount = ui.readDouble(MSG_AMMOUNT_SELL);
						String buyCurrency = ui.readToken(MSG_CURRENCY_BUY);

						ui.print(MSG_CURRENCY_CONVERSION,
								Bank.currencyConversion(sellCurrency, buyCurrency, sellAmount));
					} catch (CurrencyConversionException e) {
						this.handleException(ui, e);
					}

					break;
				case 9:
					// find account and close it

					try {
						int accountNumber = ui.readInt(MSG_ACCOUNT_NUMBER);
						Bank.closeAccount(accountNumber);
						ui.print(MSG_ACCOUNT_CLOSED, new Object[] { accountNumber, Bank.getBalance(accountNumber) });

					} catch (NoSuchAccountException e) {
						this.handleException(ui, e);

					}
					break;
				case 10:
					Bank.writeBankData();

				}

			} while (option != menuOptions.length);

		} catch (IOException e) {
			e.printStackTrace();

		}
	}

	private void handleException(UIManager ui, Exception e) throws IOException {
		ui.print(e.getMessage(), new Object[] {});
	}

}
