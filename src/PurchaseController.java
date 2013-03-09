import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JOptionPane;



public class PurchaseController implements SHPRCConstants {
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
			HashMap<Product, Integer> products = model.getPurchaseProducts();
			ArrayList<Product> toDelete = new ArrayList<Product>();
			for (Product p: products.keySet() ) { 
				toDelete.add(p);
			}
			for (Product p: toDelete) {
				model.removeProduct(p);
			}
			view.displayPurchase(model.getPurchaseProducts(), model.getTotals());
		}
	}
}



