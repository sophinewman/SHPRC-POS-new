import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JOptionPane;



public class PurchaseController implements SHPRCConstants {
	private PurchaseModel[] lastThreePurchases = new PurchaseModel[3];
	private PurchaseModel model;
	private PurchaseView view;
	private RuntimeDatabase rDB;

	private Product selectedProduct;

	public PurchaseController(PurchaseModel model, RuntimeDatabase rDB) {
		this.model = model;
		view = new PurchaseView(this, rDB.getProductList(), rDB.getAffiliations());
		this.rDB = rDB;

	}


	public boolean addProduct(int productID) {

		int quantity;
		if (productID == 100 || productID == 102 || productID == 200 ) {
			quantity = view.getQuantity();
			if (quantity == 0) {
				return true;
			}
			if (quantity == INVALID_NUMBER_INPUT || quantity < 0) {
				view.inputError("Please enter a positive whole number.");
				return false;
			}
		} else {
			quantity = 1;
		}

		model.addProduct(rDB.getProduct(productID), quantity);
		view.displayPurchase(model.getPurchaseProducts(), model.getTotals());
		return true;
	}

	public boolean setClient(String suidStr, int affiliationID) {
		if (!suidStr.matches("[0-9]+") || suidStr.length() < N_SUID_DIGITS) {
			view.inputError("Please enter a valid SUID.");
			return false;
		}

		int suid = Integer.parseInt(suidStr.substring(suidStr.length() - N_SUID_DIGITS));
		model.setCurrentClient(suid, affiliationID);
		view.displayPurchase(model.getPurchaseProducts(), model.getTotals());
		return true;

	}

	public void deleteProduct() {
		if (selectedProduct != null) {
			model.removeProduct(selectedProduct);
			view.displayPurchase(model.getPurchaseProducts(), model.getTotals());
		}
	}

	public void selectProductToModify(LabelAppearanceJButton button) {
		if (button == null) {
			selectedProduct = null;
		} else {
			if (selectedProduct == null) {
				Product product = rDB.getProduct(button.getProductID());
				selectedProduct = product;
				view.enableModifierButtons(true);
				view.highlightProductToModify(button);
			}
		}

	}

	public void submitPurchase() {
		if (model.getCurrentClient() != null && model.getPurchaseProducts().size() > 0) {
			view.displayTotalDialog(CURRENCY_FORMAT.format(model.tallyPurchaseTotal()/100.0));
		} else {
			view.inputError("You must specify a client and products to submit a purchase.");
		}
	}

	public void changeQuantity() {
		if (selectedProduct != null) {
			int newQuantity = view.getQuantity();
			if (newQuantity == INVALID_NUMBER_INPUT || newQuantity < 0) {
				view.inputError("Please enter a positive whole number.");
			} else {
				model.removeProduct(selectedProduct);
				model.addProduct(selectedProduct, newQuantity);
				view.displayPurchase(model.getPurchaseProducts(), model.getTotals());
			}
		}
	}

	public void clear() {
		if (view.confirmDecision("<html>Are you sure you want to clear this order?<br>This cannot be undone.</html>") 
				== JOptionPane.YES_OPTION) {
			clearProducts();
			view.displayPurchase(model.getPurchaseProducts(), model.getTotals());
		}
	}

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

	public void switchToAdmin() {
		if (view.confirmDecision("<html>Would you like to switch to Administrator View?<br>This will clear your current purchase.</html>") == JOptionPane.YES_OPTION) {
			view.switchView(ADMIN_PANE);
		}
	}

	public void switchToPurchase() {
		if (view.confirmDecision("Would you like to switch to Purchase View?") == JOptionPane.YES_OPTION) {
			rDB.initRuntimeDatabase();
			model = new PurchaseModel(rDB);
			view.closeWindow();
			view = new PurchaseView(this, rDB.getProductList(), rDB.getAffiliations());
			view.switchView(PURCHASE_PANE);
		}
	}

	public void adminDialog(String task) {
		DialogController dialogController = new DialogController(rDB, view.getRootFrame(), task);
	}

	public boolean cashTendered(String tendered, TotalDialog dialog) {

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

		centsTendered *= 100;
		int changeDue = (int)centsTendered - model.tallyPurchaseTotal();
		if (changeDue < 0 ) {
			return false;
		}
		dialog.dispose();
		dialog.showChangeMessage(CURRENCY_FORMAT.format(changeDue/100.0));
		return true;
	}

	private void updateLastPurchases() {
		lastThreePurchases[LEAST_RECENT] = lastThreePurchases[MIDDLE];
		lastThreePurchases[MIDDLE] = lastThreePurchases[MOST_RECENT];
		lastThreePurchases[MOST_RECENT] = model;
	}

	public void commitPurchase() {
		System.out.println("in PurchaseController: commitPurchase()");
		if (rDB.writePurchase(model) ) {
			updateLastPurchases();
			model = new PurchaseModel(rDB);
			view.displayPurchase(model.getPurchaseProducts(), model.getTotals());
			view.resetClient();
		}
	}
}



