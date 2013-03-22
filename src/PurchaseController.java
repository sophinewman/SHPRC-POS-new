import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;

/**
 * Serves as the controller in the MVC framework of a purchase and manages the main program execution.
 * 
 * @author Sophi Newman
 * @version 1.0 03/20/2013
 */

public class PurchaseController implements SHPRCConstants {

	/* The runtime database access point */
	private RuntimeDatabase rDB;

	/* The data representation of the current purchase */
	private PurchaseModel model;

	/* The GUI display of the current purchase */
	private PurchaseView view;

	/* The currently selected to-be-purchased product button */
	private PurchaseProductJButton selectedProductButton;

	/* The data representation of the last three purchases made. Used for voiding. */
	private PurchaseModel[] lastThreePurchases = new PurchaseModel[3];


	/**
	 * Class constructor.
	 * @param model the data representation of a purchase
	 * @param rDB the runtime database access point
	 */
	public PurchaseController(PurchaseModel model, RuntimeDatabase rDB) {
		this.model = model;
		view = new PurchaseView(this, rDB.getProductList(), rDB.getAffiliations());
		this.rDB = rDB;

	}

	/**
	 * Sets the current client based on the specified information.
	 * @param suidStr the string representation of the client's SUID
	 * @param affiliationID the numeric representation of the client's affiliation
	 */
	public void setClient(String suidStr, int affiliationID) {
		if (!suidStr.matches("[0-9]+") || suidStr.length() < N_SUID_DIGITS) {
			view.displayError("Please enter a valid SUID.");
		}

		int suid = Integer.parseInt(suidStr.substring(suidStr.length() - N_SUID_DIGITS));
		model.setCurrentClient(suid, affiliationID);
		view.displayPurchase(model.getPurchaseProducts(), model.getTotals());
	}


	/**
	 * Adds a product to a purchase, prompting for specific quantity if necessary.
	 * @param product the product to be added to the purchase
	 */
	public void addProduct(Product product) {
		int productID = product.getProductID();
		int quantity;
		if (productID == MALE_CONDOM || productID == GLOW_CONDOM || productID == LUBE_FOIL ) {
			quantity = view.getQuantity();
			if (quantity == 0) {
				return;
			}
			if (quantity == INVALID_NUMBER_INPUT || quantity < 0) {
				view.displayError("Please enter a positive whole number.");
				return;
			}
		} else {
			quantity = 1;
		}

		model.addProduct(product, quantity);
		view.displayPurchase(model.getPurchaseProducts(), model.getTotals());
	}


	/**
	 * Changes the quantity of the product currently selected for modification.
	 */
	public void changeQuantity() {
		if (selectedProductButton != null) {
			Product selectedProduct = selectedProductButton.getProduct();

			int newQuantity = view.getQuantity();
			if (newQuantity == INVALID_NUMBER_INPUT || newQuantity < 0) {
				view.displayError("Please enter a positive whole number.");
			} else {
				model.removeProduct(selectedProduct);
				model.addProduct(selectedProduct, newQuantity);
				view.displayPurchase(model.getPurchaseProducts(), model.getTotals());
			}
		}
	}


	/**
	 * Deletes from the purchase the product currently selected for modification.
	 */
	public void deleteProduct() {
		if (selectedProductButton != null) {
			Product selectedProduct = selectedProductButton.getProduct();
			model.removeProduct(selectedProduct);
			view.displayPurchase(model.getPurchaseProducts(), model.getTotals());

		}
	}


	/**
	 * Sets the product currently selected for modification through a reference to its 
	 * corresponding purchase button.
	 * @param button the button corresponding to the product to be modified
	 */
	public void selectProductToModify(PurchaseProductJButton button) {
		if (button == null) {
			selectedProductButton = null;
		} else {
			if (selectedProductButton != null) {
				view.highlightProductToModify(selectedProductButton, false);
			}
			selectedProductButton = button;
			view.enableModifierButtons(true);
			view.highlightProductToModify(button, true);

		}
	}


	/**
	 * Removes all products from an order.
	 */
	public void clear() {
		if (view.confirmDecision("<html>Are you sure you want to clear this order?<br>This cannot be undone.</html>") 
				== JOptionPane.YES_OPTION) {
			clearProducts();
			view.displayPurchase(model.getPurchaseProducts(), model.getTotals());
		}
	}

	/**
	 * Private helper that iterates through the purchase items and deletes them from the model.
	 */
	private void clearProducts() {
		HashMap<Product, Integer> products = model.getPurchaseProducts();
		ArrayList<Product> toDelete = new ArrayList<Product>();
		for (Product p: products.keySet() ) { 
			toDelete.add(p);
		}
		for (Product p: toDelete) {
			model.removeProduct(p);
		}
	}

	/**
	 * Switches displayed pane to the purchase view.
	 */
	public void switchToPurchase() {
		if (view.confirmDecision("Would you like to switch to Purchase View?") == JOptionPane.YES_OPTION) {
			rDB.initRuntimeDatabase();
			model = new PurchaseModel(rDB);
			view.closeWindow();
			view = new PurchaseView(this, rDB.getProductList(), rDB.getAffiliations());
			view.switchView(PURCHASE_PANE);
		}
	}

	/**
	 * Switches displayed pane to the administrator view.
	 */
	public void switchToAdmin() {
		if (view.confirmDecision("<html>Administrator View is intended only for<br>those trained and allowed to use it." +
		"<br><br>Switching will clear your current purchase.<br><br>Would you like to continue?</html>")
		== JOptionPane.YES_OPTION) {
			view.switchView(ADMIN_PANE);
		}
	}

	/**
	 * Creates and displays the administrative dialog corresponding to the given task.
	 * @param task the administrative task to be carried out
	 */
	public void adminDialog(String task) {
		DialogController dialogController = new DialogController(rDB, view.getRootFrame(), task);
	}

	/**
	 * Begins the process of writing a purchase by displaying the total to the user.
	 */
	public void submitPurchase() {
		if (model.getCurrentClient() != null && model.getPurchaseProducts().size() > 0) {
			view.displayTotalDialog(CURRENCY_FORMAT.format(model.tallyPurchaseTotal()/100.0));
		} else {
			view.displayError("You must specify a client and products to submit a purchase.");
		}
	}

	
	/**
	 * Allows the user to select a purchase to void and then voids it
	 */
	public void voidPurchase() {
		if (lastThreePurchases[0] == null) {
			view.displayError("No purchases to void.");
		} else {
			PurchaseModel purchaseToVoid = view.displayVoidChoices(lastThreePurchases);
			if (purchaseToVoid != null) {
				if (!rDB.voidPurchase(purchaseToVoid)) {
					view.displayError("Purchase could not be voided.");
				} else {
					view.displayMessage("Purchase successfully voided.");
				}
			}
			
		}
	}

	/**
	 * Continues the process of writing a purchase by displaying the change due
	 * to the client.
	 * @param tendered the string representation of the cash tendered
	 * @param dialog the preexisting dialog to be updated
	 * @return cash successfully tendered
	 */
	public boolean cashTenderedAction (String tendered, TotalDialog dialog) {
		double centsTendered;

		if (tendered.equals("") && model.tallyPurchaseTotal() == 0) {
			centsTendered = 0;

		} else {
			try {
				centsTendered = Double.parseDouble(tendered);
			} catch (NumberFormatException e) {
				return false;
			}
		}

		centsTendered *= 100; //converts to money as an integer of cents

		int changeDue = (int)centsTendered - model.tallyPurchaseTotal();
		if (changeDue < 0 ) { //occurs if the client does not pay enough
			return false;
		}

		dialog.showChangeMessage(CURRENCY_FORMAT.format(changeDue/100.0));

		return true;
	}

	/**
	 * Final step in writing a purchase; sends to runtime database for writing.
	 */
	public void commitPurchase() {
		if (rDB.writePurchase(model) ) {
			updateLastPurchases();
			model = new PurchaseModel(rDB);
			view.displayPurchase(model.getPurchaseProducts(), model.getTotals());
			view.resetClientField();
		}
	}

	/**
	 * Pushes most recent purchase to the top of the array storing the last 3 purchases.
	 */
	private void updateLastPurchases() {
		lastThreePurchases[LEAST_RECENT] = lastThreePurchases[MIDDLE];
		lastThreePurchases[MIDDLE] = lastThreePurchases[MOST_RECENT];
		lastThreePurchases[MOST_RECENT] = model;
	}

	/**
	 * Confirms decision to exit, closes the database, and exits the program.
	 */
	public void confirmExit() {
		int decision = view.confirmDecision("Are you sure you want to exit? \n Any unsumbitted transaction will be lost.");
		if (decision == JOptionPane.YES_OPTION) {
			int exitCondition = 0;
			try {
				rDB.closeDatabase();
			} catch (SQLException e){
				view.displayError("The database could not be closed.");
				exitCondition = 1;
			}
			view.closeWindow();
			System.exit(exitCondition);
		}
	}
}



