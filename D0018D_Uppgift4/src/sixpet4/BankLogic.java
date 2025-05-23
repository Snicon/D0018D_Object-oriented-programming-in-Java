package sixpet4;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import sixpet4.accounts.Account;
import sixpet4.accounts.CreditAccount;
import sixpet4.accounts.SavingsAccount;
import sixpet4.transactions.Transaction;

/**
 * The BankLogic class do most of the business logic of the application. All changes in data states are done in this class. This includes:
 * <ul>
 * 		<li>Fetching data on all stored customers</li>
 * 		<li>Creating customers</li>
 * 		<li>Getting data on an individual customer</li>
 * 		<li>Changing the name of an individual customer</li>
 * 		<li>Creating a savings account</li>
 * 		<li>Getting data on a customers account</li>
 * 		<li>Depositing funds</li>
 * 		<li>Withdrawal of funds</li>
 * 		<li>Closing an individual account</li>
 * 		<li>Deleting a customer along with said customers accounts</li>
 * 	</ul>
 * 
 * Additionally there are helper methods for formatting currency as well as finding objects to ease the process of implementing the business logic.
 * 
 * @author Sixten Peterson, sixpet-4
 * @version 3.0 (assignment 3, pre-feedback)
 */
public class BankLogic {

	// ArrayList storing all the customer objects in the bank application
	private ArrayList<Customer> customers;
	
	/**
	 * Empty constructor used when creating an "empty application", no customer data etc
	 */
	public BankLogic() {
		customers = new ArrayList<Customer>();
	}
	
	/**
	 * One parameter constructor used for when we want BankLogic to be filled with customer data
	 * @param customers
	 */
	public BankLogic(ArrayList<Customer> customers) {
		this.customers = customers;
	}
	
	/**
	 * Makes a list of all customers formatted as arrays of strings by iterating over the existing customers.
	 * @return all customer data as strings in an ArrayList.
	 */
	public List<String[]> getAllCustomers() {
		List<String[]> allCustomerData = new ArrayList<String[]>();
		
		for(Customer customer : customers) { // Log: Changed how the List works to improve data handling in other classes
			String[] customerInfo = new String[3]; // Each customer consists of 3 pieces of information: first name, last name and personal number
			customerInfo[0] = customer.getFirstName();
			customerInfo[1] = customer.getLastName();
			customerInfo[2] = customer.getpersonalIdentityNumber();
			allCustomerData.add(customerInfo);
		}
		
		return List.copyOf(allCustomerData); // LOG: Changed to copyOf based on feedback from Assignment 1
	}
	
	/**
	 * Makes a list of all customers in the application.
	 * @return List of customers
	 */
	public List<Customer> getAllCustomerObjects() {
		return List.copyOf(customers);
	}
	
	/**
	 * Creates a customer given that the personalNumber doesn't already exist in the customer data.
	 * @param name the first name of the new customer
	 * @param surname the surname of the new customer
	 * @param pNo Personal number of the new customer
	 * @return True if successfully added the customer, False if it failed to add the customer (most likely due to duplicate personal number).
	 */
	public boolean createCustomer(String name, String surname, String pNo) {
		// If the helper method finds a customer with the specified personal number then return false as personal numbers are unique.
		if(findCustomerByPNo(pNo) != null) {
			return false;
		}
		
		if (name.equals("") || surname.equals("") | pNo.equals("")) {
			return false;
		}
		
		Customer newCustomer = new Customer(name, surname, pNo);
		return customers.add(newCustomer); // Returns true if the collection changed as a result of the call, AKA if added to the ArrayList. See the following answer on StackOverflow as a reference: https://stackoverflow.com/a/40954763
	}
	
	/**
	 * Gets info on the specified customer based on the provided personal number
	 * @param pNo Personal number of the customer
	 * @return Customer data as an ArrayList of strings
	 */
	public List<String> getCustomer(String pNo) {	
		// Find the customer, will be null if not found.
		Customer customer = findCustomerByPNo(pNo);
		
		// Handle if the user is not found, return null per the assignment requirement.
		if (customer == null) {
			return null;
		}
		
		// ArrayList for storing the data
		ArrayList<String> customerData = new ArrayList<String>();
		
		// Add the relevant data to the ArrayList
		customerData.add(customer.toString());
		for (Account account : customer.getAccounts()) {
			customerData.add(account.toString());
		}
		
		// Finally return the ArrayList
		return List.copyOf(customerData); // LOG: Changed to copyOf based on feedback from Assignment 1
	}
	
	/**
	 * Changes the name of the specified customer based on the provided personal number and new names. Empty strings are ignored and won't change the name.
	 * @param name the new first name of the customer or an empty string
	 * @param surname the new surname of the customer or an empty string
	 * @param pNo personal number of the customer
	 * @return True if successful, false if unsuccessful.
	 */
	public boolean changeCustomerName(String name, String surname, String pNo) {
		// Try to find the customer by their personal number... Upon failing, null is returned. Be sure to handle.
		Customer customer = findCustomerByPNo(pNo);
		
		// Can't change a name without being given a name, return false per assignment requirement.
		if (name.equals("") && surname.equals("")) {
			return false;
		}
		
		// If customer wasn't found, return false per assignment requirement.
		if (customer == null) {
			return false;
		}
		
		if (!name.equals("")) { // Only set a name if the param isn't empty
			customer.setFirstName(name);
		}
		
		if (!surname.equals("")) { // Only set a name if the param isn't empty
			customer.setLastName(surname);
		}
		
		return true;
	}
	
	/**
	 * Creates a savings account and assigns it to a customer based on the provided personal number.
	 * @param pNo the personal number of the customer that will be holding the savings account.
	 * @return -1 if customer was not found, account number if the account was created and assigned to the customer.
	 */
	public int createSavingsAccount(String pNo) {
		Customer customer = findCustomerByPNo(pNo);
		
		// Customer not found, handle null
		if (customer == null) {
			return -1;
		}
		
		// Create a new account and assign it to the customer
		SavingsAccount account = new SavingsAccount();
		customer.addAccount(account);
		
		// Return the account number per the assignment requirements
		return account.getNumber();
	}
	
	/**
	 * Creates a credit account and assigns it to a customer based on the provided personal number.
	 * @param pNo the personal number of the customer that will be holding the credit account.
	 * @return -1 if customer was not found, account number if the account was created and assigned to the customer.
	 */
	public int createCreditAccount(String pNo) {
		Customer customer = findCustomerByPNo(pNo);
		
		// Customer not found, handle null
		if (customer == null) {
			return -1;
		}
		
		// Create a new account and assign it to the customer
		CreditAccount account = new CreditAccount();
		customer.addAccount(account);
		
		return account.getNumber();
	}
	
	/**
	 * Gets a list of all transactions for a given customer account.
	 * @param pNo the personal number of the customer that will be holding the credit account.
	 * @param accountId the number of the account to retrieve transactions from. 
	 * @return A list containing all the transaction data for the account if successful, null if no account was found or an empty list if no transactions are recorded.
	 */
	public List<String> getTransactions(String pNo, int accountId) {
		List<String> transactionData = new ArrayList<String>();
		
		Account account = findAccountByPNoAndNum(pNo, accountId);
		
		// Account not found, return null per the requirements of the assignment (2)
		if(account == null) {
			return null;
		}
		
		// Get the transactions from the account
		List<Transaction> transactions = account.getTransactions();
		
		// In case of empty transaction list, return a copy of the empty transaction list.
		if (transactions.isEmpty()) {
			return List.copyOf(transactionData);
		}
		
		// Add each transaction into the transactionData list
		transactions.forEach( (transaction) -> { transactionData.add(transaction.toString()); } ); // Wanted to try something new, never worked with lambdas before. Please see this page for reference: https://www.w3schools.com/java/ref_arraylist_foreach.asp
		
		return List.copyOf(transactionData);
	}

	
	/**
	 * Gets information on the account (by account number) of the specified customer (by personal number) 
	 * @param pNo the personal number of the customer holding the account.
	 * @param accountId the account number identifying the account
	 * @return The account data as a string if successful, null if unsuccessful.
	 */
	public String getAccount(String pNo, int accountId) {
		Account account = findAccountByPNoAndNum(pNo, accountId);
		
		if (account == null) {
			return null;
		}
		
		return account.toString();
	}
	
	/**
	 * Deposits any sum larger than 0 (integers) to the specified account of the specified user. Supports different account types as of assignment 2.
	 * @param pNo the personal number of the customer holding the account to deposit to.
	 * @param accountId the unique identifier of the account to deposit to.
	 * @param amount the amount to deposit.
	 * @return True if successful, false if account was not found or the deposit amount was too low (0 or below).
	 */
	public boolean deposit(String pNo, int accountId, int amount) {
		Account account = findAccountByPNoAndNum(pNo, accountId);
		
		// Account not found, return false per the requirements of the assignment
		if(account == null) {
			return false;
		}
		
		BigDecimal convertedAmount = new BigDecimal(amount);			// Convert int to BigDecimal, needed for VG
		
		return account.deposit(convertedAmount); // Using BigDecimal to add the converted BigDecimal to the new account amount
	}
	
	/**
	 * Withdraws any sum larger than 0 (integers) to the specified account of the specified user. Supports different account types as of assignment 2.
	 * @param pNo personal number of the customer holding the account to withdraw from.
	 * @param accountId the unique identifier of the account to withdraw from.
	 * @param amount
	 * @return true if successful, false if unsuccessful
	 */
	public boolean withdraw(String pNo, int accountId, int amount) {
		Account account = findAccountByPNoAndNum(pNo, accountId);
		
		// Account not found, return false per the requirements of the assignment
		if(account == null) {
			return false;
		}
		
		BigDecimal convertedAmount = new BigDecimal(amount); // Convert int to BigDecimal as BigDecimal is needed for VG
		
		return account.withdraw(convertedAmount);
	}
	
	/**
	 * Closes the specified account of the specified customer.
	 * @param pNo the personal number of the customer holding the account to close.
	 * @param accountId the unique identifier (account number) of the account to close.
	 * @return Null if failed/unsuccessful. String containing account number, balance, type and interest if successful.
	 */
	public String closeAccount(String pNo, int accountId) {
		Account account = findAccountByPNoAndNum(pNo, accountId);
		Customer customer = findCustomerByPNo(pNo);
		
		
		// Account not found
		if (account == null) {
			return null;
		}
		
		// Customer not found
		if (customer == null) {
			return null;
		}
		
		// Formatting all the data required for output in accordance with the assignment requirements
		String accountNumberStr = Integer.toString(account.getNumber());
		String balanceStr = formatCurrency(account.getBalance());
		String accountType = account.getType();
		String interestCalculation = formatCurrency(account.calculateInterest());
		
		// Removes the account from the customers accounts
		customer.removeAccount(account);
		
		return String.format("%s %s %s %s", accountNumberStr, balanceStr, accountType, interestCalculation);
	}
	
	/**
	 * Closes all the accounts of the customer before finally removing the customer.
	 * @param pNo the personal number of the customer to be deleted.
	 * @return ArrayList containing data on the customer (personal number, name) as well as all the closed accounts (account number, account balance, account type, account interest)
	 */
	public List<String> deleteCustomer(String pNo) {
		// Find customer
		Customer customer = findCustomerByPNo(pNo);
		
		// Customer not found
		if (customer == null) {
			return null;
		}
		
		List<String> deletedCustomerData = new ArrayList<String>();
		
		deletedCustomerData.add(customer.toString());
		
		// Make a copy of the accounts ArrayList for the removal process.
		List<Account> accountsToClose = new ArrayList<>(customer.getAccounts());
		
		/* Closing all the accounts. Originally I tried to use an enhanced for loop without copying the ArrayList (smart I know...),
		 * resulting in a ConcurrentModifcationException. Read more about how I solved it in the following GitHub Gist using an online
		 * resource as a reference: https://gist.github.com/Snicon/be90eec527874828940b33992079dcff
		 */
		for (Account account : accountsToClose) {
			String closedAccount  = this.closeAccount(pNo, account.getNumber());
			
			// Realistically we shouldn't be getting null at any point but just in case (I for one am not perfect) we do we wouldn't want to display null to the end-user. This should probably be improved with a logging system in a real world use case to catch bugs but thats beyond the scope of the course.
			if (closedAccount != null) {
				deletedCustomerData.add(closedAccount);
			}
		}
		
		// Finally remove the customer
		customers.remove(customer);
		
		return deletedCustomerData;
	}

	/*
	 * 
	 * HELPER METHODS
	 * 
	 * */
	
	/**
	 * Helper method for formatting currency to Swedish Krona (SEK), simply input any BigDecimal.
	 * @param unformattedValue an unformatted BigDecimal to format
	 * @return A nicely formatted string in the Swedish locale
	 */
	private String formatCurrency(BigDecimal unformattedValue) {
		return NumberFormat.getCurrencyInstance(Locale.of("SV", "SE")).format(unformattedValue);
	}
	
	/**
	 * Helper method for finding an account by the account holders personal number as well as the accountId.
	 * @param pNo the personal number of the customer holding the account we are looking for.
	 * @param accountId the unique identifier (account number) of the account we are looking for.
	 * @return The account object if the customer and account was found. Null if something goes wrong (customer or account not found)
	 */
	private Account findAccountByPNoAndNum (String pNo, int accountId) {
		Customer customer = findCustomerByPNo(pNo);
		
		// If customer was not found return null, handle null case
		if(customer == null) {
			return null;
		}
		
		for (Account account : customer.getAccounts()) {
			if (account.getNumber() == accountId) {
				return account;
			}
		}
		
		// No account matching the accountId was found... returning null.
		return null;
	}
	
	/**
	 * Helper method for finding a customer by their personal number. Also useful for checking if a personal number already is in use upon creating a new customer.
	 * @param pNo the personal number of the customer we are looking for.
	 * @return Customer object if a customer with the specified personal number was found. Null if there is no customer with the specified personal number.
	 */
	private Customer findCustomerByPNo(String pNo) {
		for (Customer customer : customers) {
			if(Objects.equals(customer.getpersonalIdentityNumber(), pNo)) {
				return customer;
			}
		}
		
		return null;
	}
	
}
