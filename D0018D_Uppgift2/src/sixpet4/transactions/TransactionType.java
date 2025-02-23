package sixpet4.transactions;

/**
 * Enum representing the two types of transactions available in the system.
 * These types are used to categorize the nature of a financial transaction.
 * 
 * <p> The possible transaction types are:
 * <ul>
 *     <li>{@link #DEPOSIT} - Represents a deposit transaction.</li>
 *     <li>{@link #WITHDRAW} - Represents a withdrawal transaction.</li>
 * </ul>
 * </p>
 * 
 * @author Sixten Peterson, sixpet-4
 * @version 2.0 (assignment 2, pre-feedback)
 */
public enum TransactionType {
    /**
     * Represents a deposit transaction where money is added to an account.
     */
	DEPOSIT,
	
    /**
     * Represents a withdrawal transaction where money is taken out of an account.
     */
	WITHDRAW
}
