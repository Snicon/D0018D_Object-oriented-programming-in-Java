package sixpet4.gui.panels;

import javax.swing.JPanel;

import sixpet4.BankLogic;

/**
 * This class is the super/parent class of all the panel classes inside of the sixpet4.gui.panels package. It is used to allow for data refreshing as well as setting and getting panel titles inside of the GUI class.
 * 
 * @author Sixten Peterson, sixpet-4
 * @version 4.0 (Assignment 4, pre-feedback)
 */
public abstract class BasePanel extends JPanel {
	private static final long serialVersionUID = 3212845959852249233L;
	private String title; // Title of the panel, used as part of the frame title
	private BankLogic bankLogic; // The BankLogic object
	
	/**
	 * Constructor, takes in title
	 * @param title The title of the panel used for the frame title when this panel is active
	 */
	public BasePanel(String title, BankLogic bankLogic) {
		this.title = title;
		this.bankLogic = bankLogic;
	}
	
	/**
	 * Gets the panel title
	 * @return the panel title as a string
	 */
	public String getTitle() {
		return this.title;
	}
	
	/**
	 * Gets the bankLogic
	 * @return the bankLogic object
	 */
	protected BankLogic getBankLogic() {
		return this.bankLogic;
	}
	
	/**
	 * In short this method updates the reference to bankLogic. This method would typically be called after the GUI class has gotten a completely new instance of bankLogic which in turn requires the bankLogic reference to be updated in the panel classes.
	 * @param bankLogic
	 */
	public void updateBankLogic(BankLogic bankLogic) {
        this.bankLogic = bankLogic; // Setting the bankLogic instance variable
        refresh(); // Forcing a refresh to make sure the data is up to date after the updatedBankLogic
	}
	
	/**
	 * I didn't want to force an implementation of refresh hence why this method is not abstract. But by adding this here I can override it in each individual panel depending on the panels needs.
	 * The refresh method is used to re-fetch data. This is needed because data might update and the GUI then also needs to update to reflect this.
	 */
	public void refresh() {}
}
