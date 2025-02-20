package sixpet4;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Account class is the "blueprint" for the account objects, currently housing a constructor keeping track of unique account numbers as well as a bunch of getter and setters.
 * Also handling formatting for account details.
 * 
 * @author Sixten Peterson, sixpet-4
 * @version 1.0 (Assignment 1)
 */
public class Account {
	private static int lastAssignedNumber = 1000; 				// Start point for account numbers, will be updated for each new account.
	
	private BigDecimal balance = new BigDecimal(0); 			// Default balance
	private BigDecimal interestRate = new BigDecimal("2.4"); 	// Default interest rate
	private int number;											// Account number
	private final String type = "Sparkonto"; 					// As of right now an account will always be of type "Sparkonto" as no other types exist.
	
	// Constructor for creation
	public Account() {
		lastAssignedNumber++;
		number = lastAssignedNumber;
	}
	
	/**
	 * Gets the current balance of the account.
	 * @return The current balance of the account
	 */
	public BigDecimal getBalance() {
		return balance;
	}


	/**
	 * Sets a new balance for the account.
	 * @param balance the new balance to be set
	 */
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	/**
	 * Gets the interest rate of the account (default is 2.4%).
	 * @return The interest rate of the account.
	 */
	public BigDecimal getInterestRate() {
		return interestRate;
	}

	/**
	 * Sets a new interest rate for the account.
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
	 * Sets the account number.
	 * @param number the new account number that will be set
	 */
	public void setNumber(int number) {
		this.number = number;
	}

	/**
	 * Gets the last assigned account number.
	 * @return last assigned account number.
	 */
	public static int getLastAssignedNumber() {
		return lastAssignedNumber;
	}

	/**
	 * Sets the last assigned number.
	 * @param lastAssignedNumber the account number that was last assigned by the constructor.
	 */
	public static void setLastAssignedNumber(int lastAssignedNumber) {
		Account.lastAssignedNumber = lastAssignedNumber;
	}

	/**
	 * Gets the type of the account.
	 * @return The account type.
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * Calculates the interest of the account.
	 * @return The account interest as a BigDecimal
	 */
	public BigDecimal calculateInterest() {
		return this.balance.multiply(interestRate).divide(new BigDecimal(100));
	}
	
	/**
	 * Handles the formatting of account details.
	 * @return A string consisting of the account details according to the assignment requirements.
	 */
	private String getFromattedAccountDetails() {
		// The following 4 lines are copied from the provided material in Canvas and modified for the context of already existing code: See reference here: https://canvas.ltu.se/courses/23269/assignments/177589
		String balanceStr = NumberFormat.getCurrencyInstance(Locale.of("SV", "SE")).format(balance);
		NumberFormat percentFormat = NumberFormat.getPercentInstance(Locale.of("SV", "SE"));
		percentFormat.setMaximumFractionDigits(1); // Anger att vi vill ha max 1 decimal
		String percentStr = percentFormat.format(interestRate.divide(new BigDecimal(100))); // Modified to support BigDecimal in accordance with the requirement for VG
		
		return String.format("%s %s %s %s", number, balanceStr, type, percentStr);
	}
	
	@Override
	public String toString() {
		return getFromattedAccountDetails();
	}
}
