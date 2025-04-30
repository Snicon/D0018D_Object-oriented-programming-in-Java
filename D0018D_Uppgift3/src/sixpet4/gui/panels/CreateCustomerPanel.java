package sixpet4.gui.panels;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import sixpet4.BankLogic;

/**
 * This panel houses all the Swing components and event listeners required to create a new customer
 * 
 * @author Sixten Peterson, sixpet-4
 * @version 3.0 (Assignment 3, pre-feedback)
 */
public class CreateCustomerPanel extends BasePanel {
	private static final long serialVersionUID = -7066597786198413646L;

	private BankLogic bankLogic; // Instance of bank logic
    
	// Just some simple labels below:
    private JLabel lblFirstName = new JLabel("Förnamn:");
    private JLabel lblLastName = new JLabel("Efternamn:");
    private JLabel lblPersonnummer = new JLabel("Personnummer:");
    
    // Just some simple text fields below:
    private JTextField txtFirstName = new JTextField(15);
    private JTextField txtLastName = new JTextField(15);
    private JTextField txtPersonnummer = new JTextField(15);
    
    private JButton btnSubmit = new JButton("Skapa kund"); // Simple button

    /**
     * Constructor, takes in a title and an instance of BankLogic
     * @param title
     * @param bankLogic
     */
	public CreateCustomerPanel(String title, BankLogic bankLogic) {
		super(title);
		
		this.bankLogic = bankLogic;
		initializeUI(); // Initializes the UI
	}
	
	/**
	 * Initializes the UI in this case this includes layouting, adding components and event listeners.
	 */
    public void initializeUI() {
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50)); // Add padding around the panel
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Add space around components
        gbc.anchor = GridBagConstraints.WEST; // Align components to the left

        // Add components to the panel with GridBagLayout
        gbc.gridx = 0; // Column 0
        gbc.gridy = 0; // Row 0
        add(lblFirstName, gbc);
        
        gbc.gridx = 1; // Column 1, Row 0
        add(txtFirstName, gbc);
        
        gbc.gridx = 0; // Column 0
        gbc.gridy = 1; // Row 1
        add(lblLastName, gbc);
        
        gbc.gridx = 1; // Column 1, Row 1
        add(txtLastName, gbc);
        
        gbc.gridx = 0; // Column 0
        gbc.gridy = 2; // Row 2
        add(lblPersonnummer, gbc);
        
        gbc.gridx = 1; // Column 1, Row 2
        add(txtPersonnummer, gbc);
        
        gbc.gridx = 0; // Column 0
        gbc.gridy = 3; // Row 3
        gbc.gridwidth = 2; // Span across two columns
        gbc.anchor = GridBagConstraints.CENTER; // Center the button
        add(btnSubmit, gbc); // Adding the button to the panel
        
        // Add action listener for the submit button
        btnSubmit.addActionListener(e -> handleSubmit());
    }
    
    /**
     * Event handler for when the submit button is pressed
     */
    private void handleSubmit() {
    	// Getting the values from the text fields
        String firstName = txtFirstName.getText();
        String lastName = txtLastName.getText();
        String personnummer = txtPersonnummer.getText();
        
        boolean created = bankLogic.createCustomer(firstName, lastName, personnummer); // Creating the customer
        
        if(!created) {
        	JOptionPane.showMessageDialog(this, "Kunde inte skapa användaren, är du säker på att du fyllt i alla fält och att personnummret inte redan används?", "Misslyckades", JOptionPane.ERROR_MESSAGE);
        } else {
        	JOptionPane.showMessageDialog(this, "Användaren skapades!", "Lyckades", JOptionPane.INFORMATION_MESSAGE);
            clearInput(); // Clears all the input for better UX
        }
        
        // Line below is for debugging
        // System.out.println("Customer Created: " + firstName + " " + lastName + ", Personnummer: " + personnummer);
    }
    
    /**
     * Clears the input/text fields
     */
    private void clearInput() {
        txtFirstName.setText("");
        txtLastName.setText("");
        txtPersonnummer.setText("");
    }

}
