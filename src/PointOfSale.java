import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * The class that runs the application.
 * 
 * @author Sophi Newman
 * @version 1.0 03/20/2013
 */

public class PointOfSale {
	
	public static void main(String[] args) {
		RuntimeDatabase rDB = null;
		
		try {
			rDB = new RuntimeDatabase();
		} catch (ClassNotFoundException e) {
			showErrorDialog("<html>Database driver (SQLite JDBC) could not be found.<p>Program will now exit.</html>", true);		}
		
		if (!rDB.initRuntimeDatabase()) {
			showErrorDialog("<html>Database initialization failed.<p>Program will now exit.</html>", true);
		}
		
		try {
			// Set program to "Metal" or "Cross Platform" look and feel
			UIManager.setLookAndFeel(
					UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (UnsupportedLookAndFeelException e) {
			showErrorDialog("<html>This Java Swing look and feel (\"Metal\") is not supported." +
					"<p>Program will use system default.</html>", false);
		} catch (ClassNotFoundException e) {
			showErrorDialog("<html>The Java Swing look and feel class (\"Metal\") could not be found." +
					"<p>Program will use system default.</html>", false);
		} catch (InstantiationException e) {
			showErrorDialog("<html>The Java Swing look and feel class (\"Metal\") could not be instantiated." +
					"<p>Program will use system default.</html>", false);
		} catch (IllegalAccessException e) {
			showErrorDialog("<html>The Java Swing look and feel class (\"Metal\") could not be accessed." +
			"<p>Program will use system default.</html>", false);
		}

		PurchaseModel model = new PurchaseModel(rDB);
		PurchaseController controller = new PurchaseController(model, rDB);
	}
	
	/**
	 * Displays an error dialog and, if necessary, exits program.
	 * @param message message to be displayed
	 * @param exit whether to exit the program
	 */
	private static void showErrorDialog(String message, boolean exit) {
		JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
		if (exit) {
			System.exit(1);
		}
	}

}
