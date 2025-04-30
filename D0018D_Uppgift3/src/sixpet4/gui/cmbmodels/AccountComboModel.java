package sixpet4.gui.cmbmodels;

import javax.swing.DefaultComboBoxModel;

import sixpet4.accounts.AccountType;

/**
 * Basically this is a Model used by Account Combo Box. By having this model its easier to use the same combo box data in another combo box.
 * 
 * @author Sixten Peterson, sixpet-4
 * @version 3.0 (Assignment 3, pre-feedback)
 */
public class AccountComboModel extends DefaultComboBoxModel<AccountType> {
	
	private static final long serialVersionUID = -943499646768646378L;

	/**
	 * Constructor, adds the AccountType enum values
	 */
	public AccountComboModel() {
		super(AccountType.values());
	}
}