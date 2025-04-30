package sixpet4.gui.tablemodels;

import javax.swing.table.DefaultTableModel;

/**
 * Turns out tables by default have their cells, editable. I was in fact not happy, I therefore created this custom TableModel to stop this behavior as I couldn't find another way to do so.
 * 
 * @author Sixten Peterson, sixpet-4
 * @version 3.0 (Assignment 3, pre-feedback)
 */
public class UneditableTableModel extends DefaultTableModel {
    private static final long serialVersionUID = 645671235468494509L;

	public UneditableTableModel(Object[][] data, String[] columnNames) { // Sets the data on initialization
        super(data, columnNames);
    }
	
    // Below is used to stop users from editing cells
	@Override
	public boolean isCellEditable(int row, int column) {
		return false; // => not editable
	}
}
