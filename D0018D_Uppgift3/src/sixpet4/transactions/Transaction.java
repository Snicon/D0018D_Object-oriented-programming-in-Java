package sixpet4.transactions;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * The transaction class is the "blueprint" for objects representing a financial transaction associated with an account.
 * It keeps track of transaction type, amount, balance after transaction, and time stamp.
 * 
 * Transactions are immutable after creation.
 * 
 * @author Sixten Peterson, sixpet-4
 * @version 3.0 (Assignment 3, pre-feedback)
 */
public class Transaction {
	private final BigDecimal amount; 		// The amount of money in the transaction (negative for withdrawal, positive for deposit)
	private final BigDecimal balanceAfter; 	// The account balance after the transaction
	private final LocalDateTime dateTime; 	// The date and time of the transaction

    /**
     * Creates a new transaction with the specified type, amount, and resulting balance.
     * The transaction time stamp is automatically set to the current time.
     * 
     * @param type The type of transaction (Withdraw, or Deposit as of Assignment 2).
     * @param amount The amount involved in the transaction.
     * @param balanceAfter The balance after the transaction took place.
     */
	public Transaction(BigDecimal amount, BigDecimal balanceAfter) {
		this.amount = amount;
		this.balanceAfter = balanceAfter;
		this.dateTime = LocalDateTime.now(); // Time stamp, set at creation in constructor for simplicity
	}
	
    /**
     * Gets the transaction amount.
     *
     * @return The amount of the transaction.
     */
	public BigDecimal getAmount() {
		return amount;
	}
	
    /**
     * Gets the balance after the transaction.
     *
     * @return The balance after the transaction.
     */
	public BigDecimal getBalanceAfter() {
		return balanceAfter;
	}
	
    /**
     * Gets the date and time of the transaction.
     *
     * @return The transaction time stamp.
     */
	public LocalDateTime getDateTime() {
		return dateTime;
	}
	
    /**
     * Returns a formatted string representation of the transaction.
     * The format follows "yyyy-MM-dd HH:mm:ss" for time stamps and uses Swedish currency formatting.
     *
     * @return A formatted string representing the transaction.
     */
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); // Nice formatting for LocalDateTime, copied from Canvas: https://canvas.ltu.se/courses/23269/assignments/177590?module_item_id=444280
        String dateStr = dateTime.format(formatter); // Format the date and time
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.of("SV", "SE")); // Formatting for Swedish currency
        String amountStr = currencyFormat.format(amount); // Amount formatted for Swedish currency
        String balanceStr = currencyFormat.format(balanceAfter); // Balance after transaction formatted as Swedish currency
        return String.format("%s;%s;%s", amountStr, balanceStr, dateStr); // LOG: Changed the format to include semicolons for easier parsing in other areas of the application.
    }
}
