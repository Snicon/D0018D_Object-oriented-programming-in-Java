package sixpet4.accounts;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import sixpet4.transactions.Transaction;

/**
 * Account class is the "blueprint" for the account objects, currently housing a constructor keeping track of unique account numbers as well as a bunch of getter and setters.
 * Also handling formatting for account details.
 * 
 * @author Sixten Peterson, sixpet-4
 * @version 3.0 (Assignment 3, pre-feedback)
 */
public abstract class Account implements Serializable {
	private static final long serialVersionUID = 1;

	private static int lastAssignedNumber = 1000; 									// Start point for account numbers, will be updated for each new account.
	
	private BigDecimal balance = new BigDecimal("0.0");								// Default balance is 0 for every new account
	private BigDecimal interestRate;												// The interest rate of the account
	private int number;																// Account number
	private String type;															// The type of account
	private final List<Transaction> transactions = new ArrayList<Transaction>(); 	// The transactions of the account
	
	public Account(String type, BigDecimal interestRate) {
		// Setting account number
		lastAssignedNumber++;
		number = lastAssignedNumber;
		
		// initializing fields
		this.interestRate = interestRate;
		this.type = type;
	}
	
	/**
	 * Deposits any positive amount (>0) to the account. As of assignment 2 there are no special requirements for deposits in different account types.
	 * @param amount the amount to deposit
	 * @return true if successful, false if unsuccessful.
	 */
	public boolean deposit(BigDecimal amount) {
		// The deposit was too small, return false per the requirements of the assignment (1)
		if(amount.compareTo(BigDecimal.ZERO) <= 0) {
			return false;
		}
		
		this.setBalance(this.getBalance().add(amount));	// Using BigDecimal to add the BigDecimal amount to the new account amount
		this.registerTransaction(amount); // Adds the transaction to the list of transactions for the account
		
		return true;
	}
	
	/**
	 * Withdraws the specified amount from the account. Implementations must consider the specifications and requirements of the account type.
	 * @param amount
	 * @return
	 */
	public abstract boolean withdraw(BigDecimal amount);
	
	/**
	 * Gets the current balance of the account.
	 * @return The current balance of the account
	 */
	public BigDecimal getBalance() {
		return balance;
	}
	
	/**
	 * Sets a new balance for the account. Protected to stop external logic from changing account balance, this method should only be used
	 * during implementation of withdrawal and deposits as of assignment 2.
	 * @param balance the new balance to be set
	 */
	protected void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	
	/**
	 * Calculates the interest of the account.
	 * @return The account interest as a BigDecimal
	 */
	public BigDecimal calculateInterest() {
		return this.getBalance().multiply(this.getInterestRate()).divide(new BigDecimal(100));
	}

	/**
	 * Gets the interest rate of the account.
	 * @return The interest rate of the account.
	 */
	public BigDecimal getInterestRate() {
		return interestRate;
	}

	/**
	 * Sets the interest rate of the account.
	 * @param interestRate the new interest rate to be used.
	 */
	public void setInterestRate(BigDecimal interestRate) {
		this.interestRate = interestRate;
	}

	/**
	 * Gets the account number.
	 * @return The account number of the account.
	 */
	public int getNumber() {
		return number;
	}

	/**
	 * Gets the type of the account.
	 * @return The account type.
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * Gets all the transactions of the account.
	 * @return List of transactions
	 */
	public List<Transaction> getTransactions() {
		return List.copyOf(this.transactions);
	}
	
	/**
	 * Registers a transaction of any type.
	 * @param type the type of transactions, either "Withdraw" or "Deposit".
	 * @param amount the amount of the transaction, positive for deposits and negative for withdrawals.
	 */
	protected void registerTransaction(BigDecimal amount) {
		this.transactions.add(new Transaction(amount, this.getBalance()));
	}
	
	/**
	 * Returns a nicely formatted string representation of the account, including account number, balance,
	 * account type, and interest rate. The balance is formatted as Swedish currency (SEK), and the interest rate 
	 * is formatted as a percentage with one decimal place.
	 * @return a formatted string representing the account details
	 */
	@Override
	public String toString() {
		// The following 4 lines are copied from the provided material in Canvas (assignment 1) and modified for the context of already existing code: See reference here: https://canvas.ltu.se/courses/23269/assignments/177589
		String balanceStr = NumberFormat.getCurrencyInstance(Locale.of("SV", "SE")).format(this.balance);
		NumberFormat percentFormat = NumberFormat.getPercentInstance(Locale.of("SV", "SE"));
		percentFormat.setMaximumFractionDigits(1); // Anger att vi vill ha max 1 decimal		
		String interestRateStr = percentFormat.format(this.getInterestRate().divide(new BigDecimal("100"))); // Turns the interest rate into a nicely formatted string
		
		return String.format("%s;%s;%s;%s", this.getNumber(), this.getType(), balanceStr, interestRateStr); // LOG: Changed the format to include semicolons for easier parsing in other areas of the application. Also changed the order of the data.
	}

	public static void setLastAssignedNumber(int lastAssignedNumber) {
		Account.lastAssignedNumber = lastAssignedNumber;
	}
	
	public static int getLastAssignedNumber() {
		return lastAssignedNumber;
	}
}
