import java.sql.SQLException;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;


public class RunnableTester {


	/**
	 * Runs some preliminary tests to verify that database intialization is
	 * working successfully and that the Purchase and Client classes are
	 * behaving as expected.
	 * @param args not used.
	 */
	public static void main(String[] args) {
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

//		testPurchaseAndClient(rDB);

		PurchaseModel testPurchase = new PurchaseModel(rDB);
		PurchaseController controller = new PurchaseController(testPurchase, rDB);

		try {
			rDB.closeDatabase();
		}
		catch (SQLException e) {
			System.err.println(e.getMessage());
		}




	}

	private static void testPurchaseAndClient(RuntimeDatabase rDB) {
		System.out.println(rDB.getPregnancyTestProduct());

		boolean print = rDB.qualifiesForPregnancyTestSubsidy(1000);
		System.out.println("Frosh qualifies for Pregnancy Test Subsidy: " + print);

		Client sophi = rDB.lookupClient(5573646);
		System.out.println("Sophi: " + sophi);

		PurchaseModel purchase = new PurchaseModel(rDB);
		Product condom = rDB.getProduct(100);
		purchase.addProduct(condom, 15);
		int total = purchase.tallyPurchaseTotal();
		System.out.println("total for 15 condoms before client set: " + total);
		purchase.addProduct(condom, 10);
		purchase.setCurrentClient(5573646, sophi.getAffiliation());
		total = purchase.tallyPurchaseTotal();
		System.out.println("total for 10 condoms after client set: " + total);
		purchase.setCurrentClient(5573666, 1006);
		total = purchase.tallyPurchaseTotal();
		System.out.println("total for 10 condoms after new client set: " + total);

	}

}
