package sixpet4.accounts;

import java.math.BigDecimal;

/**
 * SavingsAccount class is the "blueprint" for savings account objects. It extends Account and currently houses a constructor handling the fields related to the
 * super class. Additionally it keeps track of free withdrawal as well as withdrawal fee rate. There is an implementation of withdrawal to meet the requirements
 * of the account type in assignment 2.
 * 
 * @author Sixten Peterson, sixpet-4
 * @version 2.0 (Assignment 2, pre-feedback)
 */
public class SavingsAccount extends Account {
	private static final String ACCOUNT_TYPE = "Sparkonto";
	
	private boolean hasFreeWithdrawal = true; 	// Defaults to true because no withdrawals have been made upon account creation
	private BigDecimal withdrawalFeeRate = new BigDecimal("0.02"); 	// Default withdrawal fee rate
	
	public SavingsAccount() {
		super(ACCOUNT_TYPE, new BigDecimal("2.4"));
	}

	/**
	 * Processes a withdrawal, takes free withdrawals into account when withdrawing.
	 * @param amount the amount to withdraw
	 * @return true if successful, false if unsuccessful.
	 */
	@Override
	public boolean withdraw(BigDecimal amount) {
		if(amount.compareTo(BigDecimal.ZERO) <= 0) {
			return false; // Amount must be >0 , return false per the requirements of the assignment (1)
		}
		
		BigDecimal withdrawalFee = hasFreeWithdrawal ? BigDecimal.ZERO : amount.multiply(withdrawalFeeRate);
		BigDecimal totalAmountToWithdraw = amount.add(withdrawalFee);
		
	    if (totalAmountToWithdraw.compareTo(getBalance()) > 0) { // Withdrawal amount is greater than the balance on the account
	        if (withdrawalFee.equals(BigDecimal.ZERO)) {
	            hasFreeWithdrawal = true; // If no fee was applied, reset the free withdrawal
	        }
	        return false; // Insufficient funds
	    }
	    
	    if (hasFreeWithdrawal && withdrawalFee.equals(BigDecimal.ZERO)) { // Free withdrawal was used
	    	hasFreeWithdrawal = false; // Mark free withdrawal as used
	    }
		
		this.setBalance(this.getBalance().subtract(totalAmountToWithdraw));
		this.registerTransaction(totalAmountToWithdraw.negate()); // Register transaction on the account via helper method
		
		return true;
	}

}
