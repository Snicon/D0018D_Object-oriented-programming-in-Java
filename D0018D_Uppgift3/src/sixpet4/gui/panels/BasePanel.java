package sixpet4.gui.panels;

import javax.swing.JPanel;

/**
 * This class is the super/parent class of all the panel classes inside of the sixpet4.gui.panels package. It is used to allow for data refreshing as well as setting and getting panel titles inside of the GUI class.
 * 
 * @author Sixten Peterson, sixpet-4
 * @version 3.0 (Assignment 3, pre-feedback)
 */
public abstract class BasePanel extends JPanel {
	private static final long serialVersionUID = 3212845959852249233L;
	private String title; // Title of the panel, used as part of the frame title
	
	/**
	 * Constructor, takes in title
	 * @param title The title of the panel used for the frame title when this panel is active
	 */
	public BasePanel(String title) {
		this.title = title;
	}
	
	/**
	 * Gets the panel title
	 * @return
	 */
	public String getTitle() {
		return this.title;
	}
	
	/**
	 * I didn't want to force an implementation of refresh hence why this method is not abstract. But by adding this here I can override it in each individual panel depending on the panels needs.
	 * The refresh method is used to re-fetch data. This is needed because data might update and the GUI then also needs to update to reflect this.
	 */
	public void refresh() {}
}
