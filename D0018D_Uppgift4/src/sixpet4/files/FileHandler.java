package sixpet4.files;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import sixpet4.Customer;
import sixpet4.accounts.Account;

/**
 * The FileHandler class is responsible for importing and exporting data to and from the application.
 * 
 * @author Sixten Peterson, sixpet-4
 * @version 4.0 (Assignment 4, pre-feedback)
 */
public class FileHandler {
	
	private final String FILE_TOKEN = "Bankapp_sixpet4"; // A token/stamp to identify that the file has indeed come from this application. 
	private final double FILE_VERSION = 1.0; // The version of the file formatting, could be use for better backwards compatibility in the future
	private File file;// The file that will be written or read from depending on the action taken by the user. Defaults to null in the parameterless constructor.
	private ArrayList<Customer> readCustomers; // Storing the customers that have been read by the file handler here, null if no customers have been read.
	
	/**
	 * Basic parameterless constructor, sets the file to null as no file is provided
	 */
	public FileHandler() {
		this.file = null;
		this.readCustomers = null;
	}
	
	/**
	 * Basic one parameter constructor, sets the file
	 * @param file
	 */
	public FileHandler(File file) {
		this.file = file;
		this.readCustomers = null;
	}
	
	/**
	 * Gets the file
	 * @return the file
	 */
	public File getFile() {
		return this.file;
	}
	
	/**
	 * Sets the file to the provided file
	 * @param file the file
	 */
	public void setFile(File file) {
		this.file = file;
	}
	
	/**
	 * Gets the customers that have been read by the FileHandler
	 * @return An ArrayList of customers if any customers have been read. Null if no customers have been read.
	 */
	public ArrayList<Customer> getReadCustomers() {
		return this.readCustomers;
	}
	
	/**
	 * Gets a nicely prepared JFileChooser object. Depending on the FileType provided the default file selection and the FileNameExtensionFilter will change.
	 * @param fileType The type of file that the user will be choosing in the JFileChooser object that is returned
	 * @return The JFileChooser object
	 */
	public JFileChooser getPreparedFileChooser(FileType fileType) {
		JFileChooser fileChooser = new JFileChooser(); // New instance of JFileChooser, used as a dialog where the user can select a file
		
		// Setting default file in the chooser below
		Path projectPath = Paths.get("").toAbsolutePath(); // Getting the project root
		File defaultDir = new File(projectPath.toFile(), "sixpet4_files"); // Creating a file path that specifies the correct folder for file storage by default
		
		// Initializing to null here so I can update them in the if and else blocks
		File defaultFile = null;
        FileNameExtensionFilter filter = null;
		
		if (fileType == FileType.ACCOUNT_STATEMENT) {
			defaultFile = new File(defaultDir, "kontoutdrag.txt");
	        filter = new FileNameExtensionFilter("Textfiler (*.txt)", "txt"); // Making a new FileNameExtensionFilter for .txt
		} else {
			defaultFile = new File(defaultDir, "bankdata.dat");
			filter = new FileNameExtensionFilter("Datafiler (*.dat)", "dat"); // Making a new FileNameExtensionFilter for .dat
		}
		
		// Setting filter for file extensions and the preselected/default file below
		fileChooser.setSelectedFile(defaultFile); // Setting the default/selected file in the fileChooser
        fileChooser.setFileFilter(filter); // Setting the filter to the fileChooser
		
		return fileChooser;
	}
	
	/**
	 * Imports bank data from a compatible file that has been exported from the program. In short ObjectInputStream is used to de-serialize customer objects, it also reads the amount of customer objects to de-serialize and the last assigned account number. 
	 */
	public void importBankData() {
		ObjectInputStream input = null; // Setting the object to null here, will be replaced inside the try block
		
		try {
			if (this.file != null) {
				input = new ObjectInputStream(new FileInputStream(this.file)); // Creating a new instance of ObjectInputStream and adding the FileInputStream based on the file instance variable.
				
				String fileToken = (String) input.readObject(); // Reading the file token
				double fileVersion = input.readDouble(); // Reading the file version
				
				if (fileToken.equals(FILE_TOKEN) && fileVersion == FILE_VERSION) { // Making sure the file is compatible (no pint in trying to read a file following a different structure or that isn't exported from this program.
					
					Account.setLastAssignedNumber(input.readInt()); // Reading and setting the lastAssignedNumber to the Account object
					
					int customerAmount = input.readInt(); // Reading the amount of customers to read
					
					if (customerAmount > 0) {
						ArrayList<Customer> customers = new ArrayList<Customer>(); // Making new ArrayList of customers
						
						for (int i = 0; i < customerAmount; i++) { // Looping to read all customers until all customers have been read
							Customer customer = (Customer) input.readObject(); // Reading a customer
							customers.add(customer); // Adding the newly read customer to the ArrayList
						}
						
						readCustomers = customers; // Setting the readCustomers instance variable so we can access the data inside of GUI
					} else {
						readCustomers = null; // No customers were read, setting to null.
					}
					

				}
			} else {
				JOptionPane.showMessageDialog(null, "Filen verkar inte vara kompatibel med programmet, är du säker på att du valt rätt fil?", "Fel vid importering", JOptionPane.ERROR_MESSAGE);
			}
		} catch(Exception exception) {
			// Just catching, if something goes wrong with import the user will be notified outside of this method
		} finally { // Closing time
			if (input != null) { // No point in closing null, so if input isn't null we close
				try {
					input.close(); // Closing
				} catch (IOException e) {}
			}
		}
	}
	
	/**
	 * Exporting bank data into the file of choice that has been set in the instance variable, won't do anything if no file was set.
	 * @param customers An ArrayList of the customers to export, these customers also holds accounts and transactions
	 */
	public void exportBankData(List<Customer> customers) {
		ObjectOutputStream output = null; // Setting the object to null here, will be replaced inside the try block. It's only here so we can close the damn thing in the finally block
		
		try {
			if (this.file != null) {
				output = new ObjectOutputStream(new FileOutputStream(this.file)); // Creating a new instance of ObjectOutputStream and adding the FileOutputStream based on the file instance variable.
				
				// Writing data to help with identifying the compatibility during import
				output.writeObject(FILE_TOKEN);
				output.writeDouble(FILE_VERSION);
				
		        output.writeInt(Account.getLastAssignedNumber()); // Getting and writing the last assigned account number
				
				output.writeInt(customers.size()); // Writing the amount of customer objects to read
				
				for (Customer customer : customers) { // writing each customer to the file
					output.writeObject(customer);
				}
			} else {
				JOptionPane.showMessageDialog(null, "Är du säker att du valt en fil?", "Fel uppstod", JOptionPane.ERROR_MESSAGE);
			}
		} catch (Exception exception) {
			JOptionPane.showMessageDialog(null, "Något gick fel under exportering.", "Fel uppstod", JOptionPane.ERROR_MESSAGE);
		} finally {
			if (output != null) {
				try {
					output.close(); // Closing the ObjectOutputStream if it isn't null
				} catch (IOException e) {}
			}
		}
	}
	
	/**
	 * Exports an account statement show casing all the transactions of the account in a nicley formatted manner.
	 * @param transactions the transactions to export into the account statement
	 */
	public void exportAccountStatement(List<String> transactions) {
		FileWriter fileWriter = null; // Initializing the fileWriter to null here and setting it inside of try in order to allow me to close it in finally
		try {
			fileWriter = new FileWriter(this.file); // Making new instance of FileWriter in order to be able to write to a file
			LocalDateTime dateTime = LocalDateTime.now(); // Getting the todays date
			fileWriter.write("Kontoutdrag " + dateTime.getDayOfMonth() + "/" + dateTime.getMonthValue() + "/" + dateTime.getYear()); // Writing a line detailing the date
			
			fileWriter.write("\n\n"); // White space
			String transactionHeaders = String.format("%-20s %-20s %-20s", "Summa", "Saldo efter", "Tidpunkt"); // Nice header, makes the file easier to read for the user
			
			if (transactions.size() > 0) { // If there are any transactions
				fileWriter.write(transactionHeaders); // Writing the nice header to the file
				
				for (String transactionString : transactions) { // Looping over each transaction
					String[] transactionData = transactionString.split(";"); // Splitting the string to get each part of the transaction.
					
					if (transactionData.length == 3) { // A transaction should by the current implementation never have less or more than 3 data parts
						String transaction = String.format("%n%-20s %-20s %-20s", transactionData[0], transactionData[1], transactionData[2]); // Formatting string
						
						fileWriter.write(transaction); // Writing formatted string to the file
					}
				}
			} else { // There are no transactions
				fileWriter.write("Det finns inga transaktioner för detta konto."); // No transaction has been made, writing a user friendly message in the file to inform the user that no transaction has been made for the account
			}
			
		} catch (IOException exception) {
			JOptionPane.showInputDialog(null, "Något gick fel under exportering av kontoutdrag... \nSe följande: " + exception.getMessage(), "Fel uppstod!", JOptionPane.ERROR_MESSAGE);
		} finally {
			if (fileWriter != null) { // No reason to close null, we are therefore making sure the fileWriter isn't null
				try {
					fileWriter.close(); // Closing the fileWriter
				} catch (IOException e) {} // Catching any exceptions that occur
			}
		}
	}
}
