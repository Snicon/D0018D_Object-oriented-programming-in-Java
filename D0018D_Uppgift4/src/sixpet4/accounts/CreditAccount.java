package sixpet4.accounts;

import java.math.BigDecimal;

/**
 * CreditAccount class is the "blueprint" for credit account objects. It extends Account and currently houses a constructor handling the fields related to the
 * super class. Additionally it keeps track of credit debt limit and interest. There are implementations of withdrawal and getting interest rate to meet the
 * requirements of the account type in assignment 2.
 * 
 * @author Sixten Peterson, sixpet-4
 * @version 2.0 (Assignment 2, pre-feedback)
 */
public class CreditAccount extends Account {
	private static final long serialVersionUID = 2;

	private static final String ACCOUNT_TYPE = "Kreditkonto";
	
	private final BigDecimal creditLimit = new BigDecimal("-5000"); // Default credit limit is 5000 according to the assignment (2), currently no need to make this mutable
	private final BigDecimal creditDebtInterestRate = new BigDecimal("5"); // Default credit debt interest rate is 5% according to the assignment (2), currently no need to make this mutable

	public CreditAccount() {
		super(ACCOUNT_TYPE, new BigDecimal("1.1")); // Default credit account interest rate according to the assignment (2) is 1.1
	}

	/**
	 * Processes a withdrawal, ensuring the account does not exceed its credit limit.
	 * @param amount the amount to withdraw
	 * @return true if successful, false if unsuccessful.
	 */
	@Override
	public boolean withdraw(BigDecimal amount) {
		BigDecimal newBalance = getBalance().subtract(amount); // The new balance after withdrawal
		
		if (amount.compareTo(BigDecimal.ZERO) <= 0) {
			return false; // Amount must be positive
		}
		
		if (newBalance.compareTo(creditLimit) < 0) {
			return false; // Cannot go past the credit limit
		}
		
		// Process successful withdrawal
		this.setBalance(newBalance);
		this.registerTransaction(amount.negate()); // Register transaction on the account via helper method
		return true;
	}
	
	
	/**
	 * Returns the interest rate for the credit account. If the balance is negative (credit debt),
	 * the higher credit debt interest rate applies. Otherwise, the standard account interest rate is used.
	 * @return the applicable interest rate as a BigDecimal
	 */
	@Override
	public BigDecimal getInterestRate() {
		return (this.getBalance().compareTo(BigDecimal.ZERO) < 0) ? creditDebtInterestRate : super.getInterestRate();
	}
	
}
