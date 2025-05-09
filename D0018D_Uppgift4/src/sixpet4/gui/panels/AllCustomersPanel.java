package sixpet4.gui.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.filechooser.FileNameExtensionFilter;

import sixpet4.BankLogic;
import sixpet4.files.FileHandler;
import sixpet4.files.FileType;
import sixpet4.gui.GUI;
import sixpet4.gui.tablemodels.UneditableTableModel;
import sixpet4.transactions.Transaction;

/**
 * This panel includes most of the application functionality and is responsible for managing all data in the application.
 * 
 * @author Sixten Peterson, sixpet-4
 * @version 4.0 (Assignment 4, pre-feedback)
 */
public class AllCustomersPanel extends BasePanel {
	
	private static final long serialVersionUID = 5791282685305128313L;
	private GUI gui; // Instance of GUI, required to switch panel using the buttons 
	
	private String personalNumber = null; // The personal number of the selected customer is stored here for UX purposes, this way the data can be refreshed without the tables being "reset"
	private int accountNumber = -1; // The currently selected account number, -1 when there is no selection.
	
	// Below are constants used for the table header:
	private static String CUSTOMER_COL_FIRSTNAME = "Förnamn";
	private static String CUSTOMER_COL_LASTNAME = "Efternamn";
	private static String CUSTOMER_COL_PERSONAL_NUMBER = "Personnummer";
	
	private static String ACCOUNT_COL_NUMBER = "Kontonummer";
	private static String ACCOUNT_COL_TYPE = "Kontotyp";
	private static String ACCOUNT_COL_BALANCE = "Saldo";
	private static String ACCOUNT_COL_INTEREST_RATE = "Räntesats";
	
	private static String TRANSACTION_COL_AMOUNT = "Summa";
	private static String TRANSACTION_COL_BALANCE = "Balans efter transaktion";
	private static String TRANSACTION_COL_DATE = "Datum";
	
	// Instance variables of tables, panels and scroll panes below:
	private JTable customerTable;
	private JTable accountTable;
	private JTable transactionTable;
	
	private JPanel customerPanel;
	private JPanel accountPanel;
	private JPanel transactionPanel;
	
	private JScrollPane customerPane;
	private JScrollPane accountPane;
	private JScrollPane transactionPane;
	
	/**
	 * Constructor, like all other panels it takes in a title which is used change the title of the frame in the GUI class upon panel "activation". Also takes in instances of BankLogic and GUI for data fetching and panel swapping.
	 * @param title The title that will be used for the frame when this panel is active
	 * @param bankLogic An instance of BankLogic
	 * @param gui An instance of GUI
	 */
	public AllCustomersPanel(String title, BankLogic bankLogic, GUI gui) {
		super(title, bankLogic); // LOG: Moved bankLogic member from this class to the super class (BasePanel)
		
		this.gui = gui;
		initializeUI(); // Initializing the GUI
	}
	
	/**
	 * Initializes the GUI, in this case this includes creating scroll panes, sub panels (including setting size) and adding the sub panels to the panel
	 */
	private void initializeUI() {
    	
        // Creating the scroll panels used for displaying the different tables. This allows the user to scroll upon big data amounts
    	customerPane = new JScrollPane();
    	accountPane = new JScrollPane();
    	transactionPane = new JScrollPane();
    	
    	// Creating all the sub panels that are displayed in the AllCustomersPanel
    	createCustomerPanel();
    	createAccountPanel();
    	createTransactionPanel();
    	
    	// Setting the preferred sizes for the sub panels, making the GUI look MUCH nicer
    	customerPanel.setPreferredSize(new Dimension(400, 725));
    	accountPanel.setPreferredSize(new Dimension(420, 725));
    	transactionPanel.setPreferredSize(new Dimension(420, 725));
    	
    	// Adding the sub panels and deciding on where they should be placed layout wise
        add(customerPanel, BorderLayout.WEST);
        add(accountPanel, BorderLayout.CENTER);
        add(transactionPanel, BorderLayout.EAST);
    }
    
    /**
     * Creates the customer sub panel, its components and adds listeners to the relevant components
     */
    private void createCustomerPanel() {
    	customerPanel = new JPanel(); // Creating the sub panel
    	customerPanel.setLayout(new BorderLayout()); // Setting the layout to BorderLayout because its simple and works well for this use case
    	
    	// Creating a panel that will house the buttons related to the customer table
    	JPanel customerBtnPanel = new JPanel();
    	
    	// Creating all the buttons
    	JButton btnCreateCustomer = new JButton("Skapa ny kund");
    	JButton btnEditCustomer = new JButton("Ändra namn");
    	JButton btnDeleteCustomer = new JButton("Radera kund");
    	
    	// Adding event listeners to the buttons
    	btnCreateCustomer.addActionListener(event -> gui.switchPanel("CreateCustomer")); // Switch panel to CreateCustomer when the button is pressed
    	btnEditCustomer.addActionListener(event -> changeCustomerName());
    	btnDeleteCustomer.addActionListener(event -> deleteCustomer());
    	
    	// Adding all the buttons to the panel
    	customerBtnPanel.add(btnCreateCustomer);
    	customerBtnPanel.add(btnEditCustomer);
    	customerBtnPanel.add(btnDeleteCustomer);
    	
    	// adding the customer sub panels to the customer sub panel
    	customerPanel.add(customerPane, BorderLayout.CENTER);
    	customerPanel.add(customerBtnPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Creates the account sub panel, its components and adds listeners to the relevant components.
     */
    private void createAccountPanel() {
    	accountPanel = new JPanel(); // Creating sub panel
    	accountPanel.setLayout(new BorderLayout()); // Setting the layout to BorderLayout because its simple and works well for this use case
    	
    	// Creating a panel that will house the buttons related to the account table
    	JPanel accountBtnPanel = new JPanel();
    	
    	// Creating all the buttons
    	JButton btnCreateAccount = new JButton("Skapa nytt konto");
    	JButton btnDeleteAccount = new JButton("Avsluta konto");
    	
    	// Adding all the buttons to the panel
    	accountBtnPanel.add(btnCreateAccount);
    	accountBtnPanel.add(btnDeleteAccount);
    	
    	// Adding event listeners to the buttons
    	btnCreateAccount.addActionListener(event -> gui.switchPanel("CreateAccount"));
    	btnDeleteAccount.addActionListener(event -> terminateAccount());
    	
    	// adding the account sub panels to the account sub panel
    	accountPanel.add(accountPane, BorderLayout.CENTER);
    	accountPanel.add(accountBtnPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Creates the transaction sub panel, its components and adds listeners to the relevant components.
     */
    private void createTransactionPanel() {
    	transactionPanel = new JPanel(); // Creating sub panel
    	transactionPanel.setLayout(new BorderLayout()); // Setting the layout to BorderLayout because its simple and works well for this use case
    	
    	// Creating a panel that will house the buttons related to the transaction table
    	JPanel transactionBtnPanel = new JPanel();
    	
    	// Creating all the buttons
    	JButton btnDeposit = new JButton("Sätt in");
    	JButton btnWithdraw = new JButton("Ta ut");
    	JButton btnAccountStatement = new JButton("Exportera kontoutdrag");
    	
    	// Adding all the buttons to the panel
    	transactionBtnPanel.add(btnDeposit);
    	transactionBtnPanel.add(btnWithdraw);
    	transactionBtnPanel.add(btnAccountStatement);
    	
    	// Adding event listeners to the buttons
    	btnDeposit.addActionListener(event -> deposit());
    	btnWithdraw.addActionListener(event -> withdraw());
    	btnAccountStatement.addActionListener(event -> exportAccountStatement());
    	
    	// adding the transaction sub panels to the transaction sub panel
    	transactionPanel.add(transactionPane, BorderLayout.CENTER);
    	transactionPanel.add(transactionBtnPanel, BorderLayout.SOUTH);
    }
	
    /**
     * Populates the customer table with customer data
     */
    private void populateCustomerTable() {
        String[] columnNames = { CUSTOMER_COL_FIRSTNAME, CUSTOMER_COL_LASTNAME, CUSTOMER_COL_PERSONAL_NUMBER }; // The column names for the customer table
        List<String[]> customerData = super.getBankLogic().getAllCustomers(); // Gets all the customer data
        
        String[][] formattedCustomerData = new String[customerData.size()][columnNames.length]; // Creating a 2D array that will house the customer data in a correctly formatted format for populating the table
        for (int i = 0; i < customerData.size(); i++) {
            formattedCustomerData[i] = customerData.get(i); // adding data to the formatted 2D array
        }
        
        UneditableTableModel tableModel = new UneditableTableModel(formattedCustomerData, columnNames); // Creating new instance of the UneditableTableModel that prevents editing the table data
        
        customerTable = new JTable(tableModel); // Sets the custom UneditableTableModel
        customerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Makes sure only one row can be selected at a time
        customerPane.setViewportView(customerTable); // Sets the table in the scroll pane
        
        // Selection listener below: Sets the personal number instance variable when there is one selected
        customerTable.getSelectionModel().addListSelectionListener(event -> {
        	if (!event.getValueIsAdjusting()) { // Making sure the selection has been processed
        		int selectedRow = customerTable.getSelectedRow(); // Gets the selected row
        		if (selectedRow != -1) { // If a row has been selected
        			String personalNumber = (String) customerTable.getValueAt(selectedRow, 2); // Getting personal number
        			this.personalNumber = personalNumber; // Sets the personal number instance variable
        		} else {
        			this.personalNumber = null; // No row selected, personal number is null
        		}
        		populateAccountTable(); // Populate the account table with data
        	}
        });
    }
    
    /**
     * Populates the account table with the account data of the selected customer
     */
    private void populateAccountTable() {
        String[] columnNames = { ACCOUNT_COL_NUMBER, ACCOUNT_COL_TYPE, ACCOUNT_COL_BALANCE, ACCOUNT_COL_INTEREST_RATE }; // The column names for the account table
        
        String[][] data = {}; // No data
        
        if (personalNumber != null) { // Making sure there is a personal number, otherwise we cannot fetch any accounts.
        	List<String> customerData = super.getBankLogic().getCustomer(personalNumber); // Gets the customer data which includes all the customer accounts
        	
        	if (customerData.size() > 1) { // Meaning that there are indeed any accounts tied to the customer
        		List<String> accountData = customerData.subList(1, customerData.size()); // Gets only the part of the customer data that includes account data and excludes the first customer data part.
        		String[][] formattedData = new String[accountData.size()][columnNames.length];
        		for (int i = 0; i < accountData.size(); i++) {
        			String[] accountInfo = accountData.get(i).split(";"); // Splitting the string into multiple parts for the table
        			formattedData[i] = accountInfo;
        		}
        		
        		data = formattedData; // Sets the data to formatted data. Will remain empty if no accounts are tied to the customer
        	}
        }
        
        UneditableTableModel tableModel = new UneditableTableModel(data, columnNames);
        
        accountTable = new JTable(tableModel); // Sets the custom UneditableTableModel
        accountTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Makes sure only one row can be selected at a time
        accountPane.setViewportView(accountTable); // Set the table in the scroll pane
        
        // Selection listener below: Sets the account number instance variable when there is one selected
        accountTable.getSelectionModel().addListSelectionListener(event -> {
        	if(!event.getValueIsAdjusting()) {
        		int selectedRow = accountTable.getSelectedRow();
        		if (selectedRow != -1) {
        			int accountNumber = Integer.valueOf((String) accountTable.getValueAt(selectedRow, 0)); // Getting account number
        			this.accountNumber = accountNumber; // Setting account number
        		} else {
        			this.accountNumber = -1; // No account selected
        		}
        		populateTransactionTable(); // Populate the transaction table with data
        	}
        });
    }
    
    /**
     * Populates the transaction table with the transaction data of the selected account
     */
    private void populateTransactionTable() {
        String[] columnNames = { TRANSACTION_COL_AMOUNT, TRANSACTION_COL_BALANCE, TRANSACTION_COL_DATE }; // The column names for the account table
        
        String[][] data = {}; // Defaults to no data, will be overwritten later if there are transaction data
        
        if (personalNumber != null && accountNumber != -1) {
        	List<String> transactionData = super.getBankLogic().getTransactions(personalNumber, accountNumber); // Gets all the transaction data
        	
        	if(transactionData != null) {
            	String[][] formattedData = new String[transactionData.size()][columnNames.length]; // Formatting the data in a 2D array for the tables sake
            	for (int i = 0; i < transactionData.size(); i++) {
            		String[] transactionInfo = transactionData.get(i).split(";"); // Splitting the string into multiple parts for the table
            		formattedData[i] = transactionInfo;
            	}
            	
            	data = formattedData; // Sets the data to formatted data. Will remain empty if no transactions are tied to the account
        	}
        }
        
        UneditableTableModel tableModel = new UneditableTableModel(data, columnNames); // Creating new instance of the UneditableTableModel that prevents editing the table data
        
        transactionTable = new JTable(tableModel); // Sets the custom UneditableTableModel
        transactionTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Makes sure only one row can be selected at a time
        transactionPane.setViewportView(transactionTable); // Set the table in the scroll pane
    }
    
    /**
     * Deletes the selected customer (and closes all its accounts). Also shows a message dialog to inform the user of the deletion.
     */
    private void deleteCustomer() {    	
    	if (personalNumber != null) { // Making sure a customer is selected
    		StringBuilder message = new StringBuilder(); // Using a string builder because its nicer to work with for these "complicated" strings
    		List<String> deletedData = super.getBankLogic().deleteCustomer(personalNumber); // Deleting the customer and storing all the data from the method call
    		
    		if (deletedData != null) { // deleteCustomer will return null if the customer wasn't found
    			message.append("Följande kund har raderats:\n")
    				   .append(deletedData.get(0));
    			
    			// If one or more accounts have been closed
    			if (deletedData.size() > 1) {
    				message.append("\n\n").append("Följande konto(n) har avslutats:\n");
    				for (int i = 1; i < deletedData.size(); i++) {
    					message.append(deletedData.get(i)).append("\n");
    				}
    			}
    			refresh();
        		JOptionPane.showMessageDialog(this, message, "Kund raderad", JOptionPane.INFORMATION_MESSAGE);
    		}
    	}
    }
    
    /**
     * Changes the selected customers name
     */
    private void changeCustomerName() {
    	if (personalNumber != null) { // Making sure a customer is selected
    		StringBuilder message = new StringBuilder();
    		List<String> customerData = super.getBankLogic().getCustomer(personalNumber); // Getting the customer data
    		
    		if (customerData != null) { // Making sure we actually got some customer data
    			message.append("Vad ska bli det nya förnamnet för följande kund?:\n")
    				   .append(customerData.get(0))
    				   .append("\n\nOBS: Du kan lämna fältet tomt om namnet inte ska ändras.");
    			
    			String newFirstName = JOptionPane.showInputDialog(message); // Asks for the new first name and stores it in a variable
    			
    			message.replace(20, 23, "efter"); // Doing some string magic to avoid copy and pasting the string. Hacky but works.
    			
    			String newLastName = JOptionPane.showInputDialog(message); // Asks for the new last name and stores it in a variable
    			
    			if (newFirstName != null && newLastName != null) { // If the input dialog was cancelled the value will be null, hence why we are checking for it.
    				boolean nameChanged = super.getBankLogic().changeCustomerName(newFirstName, newLastName, personalNumber); // Changing the name and storing how it went in the nameChanged variable
    				
    				if (nameChanged) { // If successfully changed name
    					refresh(); // Refreshing the GUI data to reflect the name change
        				String result = "Följande uppgifter är nya: " + newFirstName + " " + newLastName;
        				
        				JOptionPane.showMessageDialog(this, result, "Kundnamn ändrat", JOptionPane.INFORMATION_MESSAGE);
    				} else {
    					JOptionPane.showMessageDialog(null, "Misslyckades med att byta namn. Är du säker på att du skrivit in namnen korrekt?", "Misslyckades", JOptionPane.ERROR_MESSAGE);
    				}
    			} else {
    				JOptionPane.showMessageDialog(null, "Misslyckades med att byta namn. Du avbröt en eller flera delar av processen.", "Misslyckades", JOptionPane.ERROR_MESSAGE);
    			}
    		}
    	}
    }
    
    /**
     * Terminates the selected account and shows the terminated account information in a message dialog.
     */
    private void terminateAccount() {
    	if (personalNumber != null && accountNumber != -1) {
    		String accountData = super.getBankLogic().closeAccount(personalNumber, accountNumber); // Closing the account and storing the data
    		
    		if (accountData == null) {
    			JOptionPane.showMessageDialog(this, "Misslyckades med att avsluta kontot. Vänligen säkerställ att du markerat såväl kund som konto i tabellerna, annars vet programmet inte vilket konto som ska avslutas.", "Misslyckades", JOptionPane.ERROR_MESSAGE);
    		} else {
    			StringBuilder result = new StringBuilder();
    			result.append("Följande konto har avslutats:\n")
    				  .append(accountData);
    			
    			JOptionPane.showMessageDialog(this, result, "Konto avslutats", JOptionPane.INFORMATION_MESSAGE);
    			refresh();
    		}
    	}
    }
    
    /**
     * Deposits money into the selected account
     */
    private void deposit() {
    	int amount = 0; // The amount to deposit, will update below 
    	
    	String amountStr = JOptionPane.showInputDialog(this, "Hur mycket pengar önskas sättas in?", "Pågående insättning...", JOptionPane.QUESTION_MESSAGE);
    	
    	if (amountStr != null) {
    		try {
    			amount = Integer.parseInt(amountStr);
    		} catch (NumberFormatException exception) {
    			JOptionPane.showMessageDialog(this, "Ett fel uppstod vid tolkningen av summan pengar, vänligen försök igen.", "Misslyckad insättning", JOptionPane.ERROR_MESSAGE);
    		}
    	} else {
    		JOptionPane.showMessageDialog(this, "Du avbröt insättningen. Ingen transaktion har sparats.", "Insättning avbruten", JOptionPane.ERROR_MESSAGE);
    	}
    	
    	if (personalNumber != null && accountNumber != -1) {
        	boolean successful = super.getBankLogic().deposit(personalNumber, accountNumber, amount);
        	
        	if (successful) {
        		JOptionPane.showMessageDialog(this, "Insättningen om " + amount + " har registrerats.", "Lyckad insättning", JOptionPane.INFORMATION_MESSAGE);
        		refresh(); // Re-fetching data
        	} else {
        		JOptionPane.showMessageDialog(this, "Insättningen misslyckades, är du säker på att du skrivit ett positivt tal större än 0?", "Misslyckad insättning", JOptionPane.ERROR_MESSAGE);
        	}
    	} else {
    		JOptionPane.showMessageDialog(this, "Kunde inte sätta in några pengar eftersom kund och/eller konto inte kunde hittas. Är du säker på att du valt dessa i tabellen ovan? de är båda obligatoriska.", "Misslyckad insättning", JOptionPane.ERROR_MESSAGE);
    	}
    }
     
    /**
     * Withdraws money from the selected account
     */
    private void withdraw() {
    	int amount = 0; // The amount to withdraw, will update below
    	
    	String amountStr = JOptionPane.showInputDialog(this, "Hur mycket pengar önskas tas ut?", "Pågående uttag...", JOptionPane.QUESTION_MESSAGE);
    	
    	if (amountStr != null) {
    		try {
    			amount = Integer.parseInt(amountStr);
    		} catch (NumberFormatException exception) {
    			JOptionPane.showMessageDialog(this, "Ett fel uppstod vid tolkningen av summan pengar, vänligen försök igen.", "Misslyckat uttag", JOptionPane.ERROR_MESSAGE);
    		}
    	} else {
    		JOptionPane.showMessageDialog(this, "Du avbröt uttaget. Ingen transaktion har sparats.", "Uttag avbrutet", JOptionPane.ERROR_MESSAGE);
    	}
    	
    	if (personalNumber != null && accountNumber != -1) {
        	boolean successful = super.getBankLogic().withdraw(personalNumber, accountNumber, amount);
        	
        	if (successful) {
        		JOptionPane.showMessageDialog(this, "Uttaget om " + amount + " har registrerats.", "Lyckat uttag", JOptionPane.INFORMATION_MESSAGE);
        		refresh(); // Re-fetching data
        	} else {
        		JOptionPane.showMessageDialog(this, "Uttaget misslyckades, är du säker på att du skrivit ett positivt tal större än 0?\nKontrollera även gärna saldot på kontot.", "Misslyckat uttag", JOptionPane.ERROR_MESSAGE);
        	}
    	} else {
    		JOptionPane.showMessageDialog(this, "Kunde inte ta ut några pengar eftersom kund och/eller konto inte kunde hittas. Är du säker på att du valt dessa i tabellen ovan? de är båda obligatoriska.", "Misslyckat uttag", JOptionPane.ERROR_MESSAGE);
    	}
    }
    
    // TODO: Comment
    private void exportAccountStatement() {
    	if (personalNumber != null && accountNumber != -1) {
        	FileHandler fileHandler = new FileHandler(); // Creating new instance of FileHandler
        	
        	JFileChooser fileChooser = fileHandler.getPreparedFileChooser(FileType.ACCOUNT_STATEMENT); // Gets a fileChooser object that has been prepared with the correct filters and default selection
        	int returnValue = fileChooser.showSaveDialog(this); // Shows a save dialog
    		
            if (returnValue == JFileChooser.APPROVE_OPTION) { // A file was chosen and approved
                File selectedFile = fileChooser.getSelectedFile(); // Getting the selected file
                
                fileHandler.setFile(selectedFile); // Setting the file in the fileHandler
                List<String> transactions = super.getBankLogic().getTransactions(personalNumber, accountNumber);
                fileHandler.exportAccountStatement(transactions); // Exporting all bank data
            }
    	} else {
    		JOptionPane.showMessageDialog(this, "Hoppsan! Du måste välja en kund och ett konto för att kunna exportera ett kontoutdrag.", "Misslyckades", JOptionPane.ERROR_MESSAGE);
    	}
    }

    /**
     * Re-popuklates the tables with fresh data, making sure all data is up to date. Will be called upon "activating" the panel or after data manipulation has been done
     */
	@Override
	public void refresh() {
		populateCustomerTable();
		populateAccountTable();
		populateTransactionTable();
	}
}
