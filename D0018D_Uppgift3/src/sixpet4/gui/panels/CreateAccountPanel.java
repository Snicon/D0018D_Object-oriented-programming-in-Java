package sixpet4.gui.panels;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import sixpet4.BankLogic;
import sixpet4.Customer;
import sixpet4.accounts.AccountType;
import sixpet4.gui.cmbmodels.AccountComboModel;

/**
 * This panel houses all the Swing components and event listeners required to create a new account
 * 
 * @author Sixten Peterson, sixpet-4
 * @version 3.0 (Assignment 3, pre-feedback)
 */
public class CreateAccountPanel extends BasePanel {
	private static final long serialVersionUID = -6762659367143151237L; // Shutting up the IDE warning.

	private BankLogic bankLogic; // Instance of BankLogic, needed to create an account and fetching customer data
	
	private List<Customer> customerList = new ArrayList<>(); // Allows for easier data access
    
	// Just some labels
    private JLabel lblAccountType = new JLabel("Kontotyp:");
    private JLabel lblCustomer = new JLabel("Kund:");
    
    // Creating an instance of the accountComboModel, basically all this does is filling the AccountComboBox with all possible account types from the AccountType enum, will be applied later
    AccountComboModel accountComboModel = new AccountComboModel();
    
    // JComboBox instance variables for further use below
    private JComboBox cmbAccountType = new JComboBox<>(accountComboModel);
    private JComboBox<String> cmbCustomer;
    
    // Buttons
    private JButton btnSubmit = new JButton("Skapa konto");

    /**
     * Constructor, takes in a title which is used in the GUI class as well as an instance of bankLogic
     * @param title The panel title, used in the frame title
     * @param bankLogic An instance of BankLogic
     */
	public CreateAccountPanel(String title, BankLogic bankLogic) {		
		super(title);
		
		this.bankLogic = bankLogic;
		initializeUI(); // Initializing UI
	}
	
	/**
	 * Initializes the UI in this case this includes layouting, adding components and event listeners.
	 */
    public void initializeUI() {
        setLayout(new GridBagLayout()); // Using the GridBagLayout for this panel, it was the only way I was successfully able to center stuff like I wanted, though a bit more complicated than other layouts in my experience 
        setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50)); // Adds padding around the panel
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Add space around components
        gbc.anchor = GridBagConstraints.WEST; // Aligns the components to the left

        // Adding components to the panel with GridBagLayout
        gbc.gridx = 0; // Column 0
        gbc.gridy = 0; // Row 0
        add(lblAccountType, gbc);
        
        gbc.gridx = 1; // Column 1, Row 0
        add(cmbAccountType, gbc);
        
        gbc.gridx = 0; // Column 0
        gbc.gridy = 1; // Row 1
        add(lblCustomer, gbc);
        
        gbc.gridx = 1; // Column 1, Row 1
        cmbCustomer = new JComboBox<>(); // Starts out empty, will be populated automatically whenever the panel is "activated" through the GUI class by calling the refresh method
        add(cmbCustomer, gbc);
        
        
        gbc.gridx = 0; // Column 0
        gbc.gridy = 2; // Row 2
        gbc.gridwidth = 2; // Span across two columns
        gbc.anchor = GridBagConstraints.CENTER; // Center the button because it looks nice
        add(btnSubmit, gbc);
        
        // Add action listener for the submit button
        btnSubmit.addActionListener(e -> handleSubmit());
    }
    
    /**
     * Fetches all customers and creates customer objects from the data for easier access to data
     * @return A List of all customer objects
     */
    private Customer[] getAllCustomers() {
        // Fetch all customer data from bankLogic, will be used to populate the combobox
        List<String[]> allCustomerData = bankLogic.getAllCustomers();
        
        // Create an array of Customer objects because objects are handy in Java
        Customer[] customers = new Customer[allCustomerData.size()];
        
        for (int i = 0; i < allCustomerData.size(); i++) {
            String[] customerInfo = allCustomerData.get(i);
            customers[i] = new Customer(customerInfo[0], customerInfo[1], customerInfo[2]); // Create a Customer object
        }
        
        return customers; // Return the array of Customer objects
    }
    
    /**
     * Populates the customer combo box by first making sure to clear all items and then adding all customers one by one
     */
    private void populateCMBCustomer() {
        cmbCustomer.removeAllItems(); // Removes all items from the combobox
        customerList.clear(); // Clears the customer list in the instance variables
        Customer[] customers = getAllCustomers(); // Gets an updated list of all customer
        
        for (Customer customer : customers) { // For each customer
            String displayName = customer.getFirstName() + " " + customer.getLastName() + " - " + customer.getpersonalIdentityNumber(); // a nice display name for the GUI show casing all information
            cmbCustomer.addItem(displayName); // Adding the display name to the combobox
            customerList.add(customer); // Adding customer to the list in the field variables, allows for easier data access upon handling the submit button
        }
    }
    
    /**
     * The event handler for when the submit button is pressed. 
     */
    private void handleSubmit() {
    	AccountType selectedAccountType = (AccountType) cmbAccountType.getSelectedItem(); // Casts the selected item in the account combobox to an AccountType enum
    	int selectedCustomerIndex = cmbCustomer.getSelectedIndex(); // The index in the combobox, since the id will be the same as for the list we can determine the selected customer and get customer data from the list this way
    	
    	if (selectedCustomerIndex >= 0 && selectedCustomerIndex < customerList.size()) { // Making sure the customer index is valid
    		Customer selectedCustomer = customerList.get(selectedCustomerIndex); // Getting the customer object matching the selected customer in the customer combobox
    		String personalNumber = selectedCustomer.getpersonalIdentityNumber(); // Getting the personal identity number
    		int accountCreated = -1; // Defaults to -1 indicating something went wrong, will be replaced inside the switch statement
    		
        	switch (selectedAccountType) {
	    		case AccountType.CREDIT:
	    			accountCreated = bankLogic.createCreditAccount(personalNumber); // Setting accountCreated to the output of the createCreditAccount call
	    			break;
	    		case AccountType.SAVINGS:
	    			accountCreated = bankLogic.createSavingsAccount(personalNumber); // Setting accountCreated to the output of the createSavingsAccount call
	    			break;
        	}
        	
        	if (accountCreated == -1) { // Failed to create account
        		JOptionPane.showMessageDialog(this, "Kontot skapdes inte eftersom den valda kunde inte kunde hittas. Kan den nyligen ha blivit raderad?", "Misslyckades", JOptionPane.ERROR_MESSAGE);
        	} else { // Account was successfully created
        		StringBuilder message = new StringBuilder();
        		message.append("Ett konto med följande kontnummer har skapats:\n")
        			   .append(accountCreated)
        			   .append("\n\nKontot tillhör följande kund:\n")
        			   .append(selectedCustomer.toString());
        		
        		JOptionPane.showMessageDialog(this, message, "Konto skapat", JOptionPane.INFORMATION_MESSAGE);
        	}
    	}
    }

    /**
     * This method is called whenever the panel is "activated". The behind this is that data may have updated since initialization of the panel. We there for want to fetch data every time the panel is viewed since there is no caching in this application, and boy am I glad there isn't.
     */
	@Override
	public void refresh() {
		populateCMBCustomer(); // Re-populates the customer combo box
	}
    
    
}
