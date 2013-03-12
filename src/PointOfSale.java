import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;


public class PointOfSale {
	
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PurchaseModel[] previousPurchases = new PurchaseModel[3];
		RuntimeDatabase rDB = null;
		try {
			rDB = new RuntimeDatabase();
		}
		catch (ClassNotFoundException e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}
		if (!rDB.initRuntimeDatabase()) {
			System.err.println("Database initialization failed.");
			System.exit(1);
		}
		
		try {
			// Set cross-platform Java L&F (also called "Metal")
			UIManager.setLookAndFeel(
					UIManager.getCrossPlatformLookAndFeelClassName());
		} 
		catch (UnsupportedLookAndFeelException e) {
			// handle exception
		}
		catch (ClassNotFoundException e) {
			// handle exception
		}
		catch (InstantiationException e) {
			// handle exception
		}
		catch (IllegalAccessException e) {
			// handle exception
		}

		//testPurchaseAndClient(rDB);

		PurchaseModel model = new PurchaseModel(rDB);
		PurchaseController controller = new PurchaseController(model, rDB);

	}

}
