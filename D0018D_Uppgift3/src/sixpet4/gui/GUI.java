package sixpet4.gui;

import java.awt.CardLayout;
import java.awt.Component;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import sixpet4.BankLogic;
import sixpet4.gui.panels.AllCustomersPanel;
import sixpet4.gui.panels.BasePanel;
import sixpet4.gui.panels.CreateCustomerPanel;
import sixpet4.gui.panels.CreateAccountPanel;

/**
 * This class can best be explained as the start point of the application. It's core responsibility is the GUI which in different ways interacts with an instance of BankLogic.
 * The GUI as a whole is structured in a way that somewhat resembles a React (JavaScript) application would be structured, not really but also not that big of a stretch.
 * In general the idea is that this class manages a main frame where different panels are then displayed at different times, kind of like React components.
 * This is the top most "component" and hence a lot (but not all) of the state is kept track of here. These states (or instances, for example bankLogic) are then passed down to
 * other "components" (classes) such as the panel classes. More on all this in the written report.
 * 
 * @author Sixten Peterson, sixpet-4
 * @version 3.0 (Assignment 3, pre-feedback)
 */
public class GUI {
	// I'm obviously not paid enough to make this damn thing responsive so I've opted to keep the resolution to a low 1200x800 because most monitors support this while also giving a bit more room to work with than something smaller.
	private final int FRAME_WIDTH = 1280; // Width
	private final int FRAME_HEIGHT = 800; // Height
	
	private final String TITLE_PREFIX = "Bankapplikation av sixpet-4"; // The prefix of the frame title which is always displayed.
	
	// The titles below are re-used for multiple things but their main responsibility is for determining the title after the prefix shown above to use for when a specific panel is active
	private final String TITLE_CREATE_CUSTOMER = "Skapa ny kund";
	private final String TITLE_ALL_CUSTOMERS = "Alla kunder";
	private final String TITLE_CREATE_ACCOUNT = "Skapa nytt konto";
	
	// These are used for determining the panel to display in cases where a string is provided to switch panel. I like to see them as route names when dealing with routes in Laravel.
	private final String NAME_CREATE_CUSTOMER = "CreateCustomer";
	private final String NAME_ALL_CUSTOMERS = "AllCustomers";
	private final String NAME_CREATE_ACCOUNT = "CreateAccount";
	
	
	private String activePanel = "CreateCustomer"; // Helps determining the panel that is actively being displayed, defaults to CreateCustomer as at least one customer is required for full functionality of the application.
												   // There is probably a better way to do this however I sadly did not have enough time to do research and considerations due to unforeseen personal matters taking time away from studies
	
	// Just storing the panel instances for further use in different methods
	BasePanel createCustomerPanel;
	BasePanel allCustomersPanel;
	BasePanel createAccountPanel;
	
	// Same as above
	private JFrame mainFrame;
	private JPanel mainPanel;
	private BankLogic bankLogic;

	/**
	 * The application start point the GUI is started from here
	 * @param args
	 */
    public static void main(String[] args) {
    	// From my understanding after reading Java a Beginners Guide 7th Edition by Schildt this makes the Swing GUI run on a separate event-dispatching thread (EDT) which is desirable due to thread safety.
        SwingUtilities.invokeLater(() -> new GUI().createAndShowGUI()); // Creating a new instance of GUI and running the createAndShowGUI-method
    }
    
    /**
     * Creates a JFrame, different Swing panels and components. Responsible for the main frame/window to come to existence.
     */
    private void createAndShowGUI() {
    	bankLogic = new BankLogic(); // Creates a new instance of the BankLogic class which stores all customers, accounts etc. Also houses the methods that we will be using to manipulate, add and remove data in the application.
    	mainFrame = new JFrame(TITLE_PREFIX + " - " + TITLE_CREATE_CUSTOMER); // Creates a new JFrame which will act as the main window/frame of the application. Also sets a nice default title.
    	mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Makes sure the application actually exits when pressing the exit window button.
    	mainFrame.setSize(FRAME_WIDTH, FRAME_HEIGHT); // Sets the frame resolution/size
    	
    	mainPanel = new JPanel(new CardLayout()); // Creates a new JPanel utilizing the CardLayout for swapping between panels later.
    	
    	// Creating an instance of all the panels used by the application below.
    	createCustomerPanel = new CreateCustomerPanel(TITLE_CREATE_CUSTOMER, bankLogic);
    	allCustomersPanel = new AllCustomersPanel(TITLE_ALL_CUSTOMERS, bankLogic, this);
    	createAccountPanel = new CreateAccountPanel(TITLE_CREATE_ACCOUNT, bankLogic);
    	
    	// Setting all the component names below to keep track of the different panels which is very important when swapping between panels and refreshing (re-fetching up to date data) panels.
    	createCustomerPanel.setName(NAME_CREATE_CUSTOMER);
    	allCustomersPanel.setName(NAME_ALL_CUSTOMERS);
    	createAccountPanel.setName(NAME_CREATE_ACCOUNT);
    	
    	// Adding all the different panels to the mainPanel
    	mainPanel.add(createCustomerPanel, createCustomerPanel.getName());
    	mainPanel.add(allCustomersPanel, allCustomersPanel.getName());
    	mainPanel.add(createAccountPanel, createAccountPanel.getName());
    	
    	createMenuBar(); // Helper method creating the menubar and all menu items for navigation.
    	
    	mainFrame.setResizable(false);
    	mainFrame.setLocationRelativeTo(null);
        mainFrame.add(mainPanel); // Adding the mainPanel to the frame in order to display stuff.
        mainFrame.setVisible(true); // Making it visible so we have something to look at.
    }
    
    /**
     * Creates and sets the menu bar used by the program for navigation. Consists of two JMenus: Customers & Accounts. Along with multiple MenuItems. 
     */
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar(); // New instance of JMenuBar, JMenus are added to this menu bar.
        
        // Creating the Customer JMenu and JMenuItems.
        JMenu customerMenu = new JMenu("Kunder");
        JMenuItem createCustomerMenuItem = new JMenuItem("Skapa ny kund");
        JMenuItem allCustomersMenuItem = new JMenuItem(TITLE_ALL_CUSTOMERS);
        
        // Creating the Account JMenu and JMenuItems.
        JMenu accountMenu = new JMenu("Konton");
        JMenuItem createAccountMenuItem = new JMenuItem(TITLE_CREATE_ACCOUNT);
        
        // Adding the JMenuItems to the JMenus.
        customerMenu.add(createCustomerMenuItem);
        customerMenu.add(allCustomersMenuItem);
        accountMenu.add(createAccountMenuItem);
        
        // Adding the JMenus to the JMenuBar.
        menuBar.add(customerMenu);
        menuBar.add(accountMenu);
        
        // Setting the JMenuBar of the frame to the previously created JMenuBar.
        mainFrame.setJMenuBar(menuBar);
        
        // Adding listeners and event handling with the help of lambdas. These are responsible for how the menu switches the active panel in the frame.
        createCustomerMenuItem.addActionListener(e -> switchPanel(createCustomerPanel));
        allCustomersMenuItem.addActionListener(e -> switchPanel(allCustomersPanel));
        createAccountMenuItem.addActionListener(e -> switchPanel(createAccountPanel));
    }
    
    /**
     * While a bit unintuitive this method is used for "externally" switching the active panel. Since outside classes don't have access to the instances of the panels this was a work around to still allow for panel switching outside of the GUI class without having to make the individual panels public which I felt was bad encapsulation for what we are trying to achive.
     * @param panelName The name of the panel to switch to, this will be used to determine the panel object to use. Case sensitive.
     */
    public void switchPanel(String panelName) {
    	// Very basic switch statement that provides the correct parameter to the private switchPanel-method. In hind sight using an enum for the constants might've been better developer experience to avoid incorrect casing or spelling mistakes in the panel classes that call this method.
    	switch (panelName) {
    		case NAME_CREATE_CUSTOMER -> switchPanel(createCustomerPanel);
    		case NAME_ALL_CUSTOMERS -> switchPanel(allCustomersPanel);
    		case NAME_CREATE_ACCOUNT -> switchPanel(createAccountPanel);
    	}
    }
    
    /**
     * Switches the active panel to another panel. This determines what is shown in the frame to the user. Also updates frame title and refreshes data (re-fetching up to date data to display in the GUI)
     * @param panel The panel object to switch to
     */
    private void switchPanel(BasePanel panel) {
        CardLayout cardLayout = (CardLayout) (mainPanel.getLayout()); // Gets the layout object that is needed for showing another panel.
        cardLayout.show(mainPanel, panel.getName()); // Shows the panel provided in the parameter of the method.
        mainFrame.setTitle(TITLE_PREFIX + " - " + panel.getTitle()); // Updates the title to match the active panel.
        
        this.activePanel = panel.getName(); // Setting the active panel, used in the refreshCurrentPanel.
        refreshCurrentPanel(); // Causes a data refresh in the active panel.
    }
    
    /**
     * Causes re-fetching of data in the active panel by calling the refresh method in the active panel object.
     */
    private void refreshCurrentPanel() {
        for (Component component : mainPanel.getComponents()) { // Loops through the components in mainPanel in order to find the active panel
            if (component.getName() != null && component.getName().equals(this.activePanel)) { // Checks if the components name matches the name of the active panel.
                if (component instanceof BasePanel) { // Also makes sure that the component is an instance of BasePanel, meaning it is guaranteed to have the refresh() method that is used to re-fetch data.
                    ((BasePanel) component).refresh(); // Calls the refresh() method on the correct panel.
                }
                break; // Exit the loop once the correct panel is found, no reason to continue and hog up resources
            }
        }
    }
    
}
