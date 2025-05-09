package sixpet4;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import sixpet4.accounts.Account;

/**
 * Customer class is the "blueprint" for creating customer objects. As commonly seen in OOP applications the class contains a bunch of getters and setters as well as a constructor.
 * I've opted to keep the accounts closely coupled to the Customer class due to simplicity.
 * 
 * @author Sixten Peterson, sixpet-4
 * @version 3.0 (assignment 3, pre-feedback)
 */
public class Customer implements Serializable {

	private static final long serialVersionUID = 1;
	private String firstName;										// First name of the account holder
	private String lastName;										// Last name of the account holder
	private final String personalIdentityNumber;					// Swedish personal identity number (social security), this is never allowed to change
	private ArrayList<Account> accounts = new ArrayList<Account>();	// ArrayList containing all the accounts belonging to the user
	
	public Customer(String firstName, String lastName, String personalIdentityNumber) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.personalIdentityNumber = personalIdentityNumber;
	}
	
	/**
	 * Get the first name of the customer
	 */
	public String getFirstName() {
		return firstName;
	}
	
	/**
	 * Set the first name of the customer
	 * @param firstName the new first name that will be set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	/**
	 * Get the last name of the customer
	 * @return The last name of the customer
	 */
	public String getLastName() {
		return lastName;
	}
	
	/**
	 * Set the last name of the customer
	 * @param lastName the last name to be set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	/**
	 * Get the personal identity number of the customer
	 * @return The personal identity number
	 */
	public String getpersonalIdentityNumber() {
		return personalIdentityNumber;
	}
	
	/**
	 * Get the customers account(s).
	 * @return The accounts
	 */
	public List<Account> getAccounts() {
		return List.copyOf(this.accounts);
	}
	
	/**
	 * Removes the passed in account of the customer
	 * @param accounts the account object to remove from the accounts list
	 */
	public void removeAccount(Account account) {
		accounts.remove(account);
	}
	
	/**
	 * Adds an account to the customer
	 * @param account the account to add to the accounts list
	 */
	public void addAccount(Account account) {
		accounts.add(account);
	}
	
	@Override
	public String toString() {;
		return String.format("%s %s %s", personalIdentityNumber, firstName, lastName);
	}
}
