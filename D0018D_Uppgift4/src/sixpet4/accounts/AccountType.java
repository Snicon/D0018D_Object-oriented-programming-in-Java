package sixpet4.accounts;

/**
 * An enum used for the different types of accounts, 
 * 
 * @author Sixten Peterson, sixpet-4
 * @version 3.0 (Assignment 3, pre-feedback)
 */
public enum AccountType {
	CREDIT("Kreditkonto"),
	SAVINGS("Sparkonto");
	
	private final String displayName; // Display name is the name we will display in the GUI, for instance in the ComboBox used when creating an account.
	
	/**
	 * The usual constructor, assigns the display name to the field
	 * @param displayName the display name that will be used in the GUI
	 */
	AccountType(String displayName) { // The usual constructor
		this.displayName = displayName;
	}
	
	@Override
	public String toString() {
		return displayName;
	}
}
