import java.sql.SQLException;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;


public class DialogController {

	private RuntimeDatabase rDB;
	private AdminDialog dialogView = null;
	private JFrame parentFrame;
	private Object selectedObject;
	private JDialog subDialog;
	private String adminTask;

	public DialogController (RuntimeDatabase rDB, JFrame parentFrame, String adminTask) {
		this.rDB = rDB;
		this.parentFrame = parentFrame;
		this.adminTask = adminTask;
		dialogView = new AdminDialog(this, parentFrame, adminTask, rDB.getAffiliations(), rDB.getProductList(), rDB.getCategoryList());
	}

	public void itemSelected(Object obj) {
		selectedObject = obj;
		dialogView.enableUpdateAndDelete();
	}

	public void addProduct() {
		subDialog = new ProductDialog(this, dialogView, "Update Product", rDB.getCategoryList().toArray(), false);
		subDialog.setVisible(true);
	}


	public void updateProduct() {

		subDialog = new ProductDialog(this, dialogView, "Update Product", rDB.getCategoryList().toArray(), true);

		Product product = (Product) selectedObject;
		int categoryID = product.getCategoryID();
		Category category = rDB.getCategory(categoryID);


		((ProductDialog) subDialog).populateFields(product.getName(), product.getPrice(), product.getCost(), category);
		subDialog.setVisible(true);

	}

	public void deleteProduct() {
		int decision = dialogView.confirmDecision("Are you sure you want to delete this product?");
		if (decision == JOptionPane.YES_OPTION) {
			Product toDelete = (Product)selectedObject;
			boolean successfulDelete = rDB.deleteProduct(toDelete.getProductID());
			if (successfulDelete) {
				reset();
			}

		}
	}


	public void addAffiliation() {
		subDialog = new AffiliationDialog(this, dialogView, "Add Affiliation", false);
		subDialog.setVisible(true);
	}

	public void updateAffiliation() {
		subDialog = new AffiliationDialog(this, dialogView, "Update Affiliation", true);

		Affiliation affiliation = (Affiliation) selectedObject;
		((AffiliationDialog)subDialog).populateFields(affiliation.getName(), affiliation.getCredit() * -1, affiliation.qualifiesForPregnancyTest());

		subDialog.setVisible(true);

	}

	public void deleteAffiliation() {
		int decision = dialogView.confirmDecision("Are you sure you want to delete this affiliation?");
		if (decision == JOptionPane.YES_OPTION) {
			Affiliation toDelete = (Affiliation)selectedObject;
			if (!rDB.deleteAffiliation(toDelete.getAffiliationID())) {
				System.err.println("Affiliation could not be deleted");
				return;
			}
			reset();
		}

	}

	public void addCategory() {
		subDialog = new CategoryDialog(this, dialogView, "Add Category", false);
		subDialog.setVisible(true);

	}

	public void updateCategory() {
		subDialog = new CategoryDialog(this, dialogView, "Update Category", true);

		Category category = (Category) selectedObject;
		((CategoryDialog)subDialog).populateFields(category.getCategoryName());

		subDialog.setVisible(true);

	}

	public void deleteCategory() {
		int decision = dialogView.confirmDecision("Are you sure you want to delete this category?");
		if (decision == JOptionPane.YES_OPTION) {
			Category toDelete = (Category)selectedObject;
			if (! rDB.deleteCategory(toDelete.getCategoryID())) {
				System.err.println("Category could not be deleted.");
				return;
			}
			reset();
		}
	}

	private String getMoneyString(String dollars, String cents) {
		if (cents.length() > 2) {
			return null;
		}
		String combination = dollars + cents;
		if (!combination.matches("[0-9]+")) {
			return null;
		}
		return combination;
	}

	public void createNewCategory(String name) {
		if (name.equals("")) {
			((CategoryDialog)subDialog).inputError("Please enter a category name.");
			return;
		}
		if (!rDB.validCategoryName(name)) {
			((CategoryDialog)subDialog).inputError("A category with that name already exists.");
			return;
		}
		if (!rDB.addCategory(name)) {
			((CategoryDialog)subDialog).inputError("Category entry failed.");
			return;
		}
		reset();
		((CategoryDialog) subDialog).close();

	}

	public void updateCategory(String name) {
		if (name.equals("")) {
			((CategoryDialog)subDialog).inputError("Please enter a category name.");
			return;
		}
		Category category = (Category)selectedObject;
		if (!rDB.updateCategory(name, category.getCategoryName(), category.getCategoryID())) {
			((CategoryDialog)subDialog).inputError("Category update failed.");
			return;
		}
		reset();
		((CategoryDialog) subDialog).close();
	}

	public void createNewProduct(String name, String priceDollars, String priceCents, String costDollars, String costCents, Category category) {
		String priceString = getMoneyString(priceDollars, priceCents);
		String costString = getMoneyString(costDollars, costCents);
		if (priceString == null || costString == null) {
			((ProductDialog) subDialog).inputError("Invalid price input.");
			return;
		}
		if (name.equals("")) {
			((ProductDialog) subDialog).inputError("Please enter a product name.");
			return;
		}
		if (!rDB.validProductName(name)) {
			((ProductDialog) subDialog).inputError("A product with that name already exists.");
			return;
		}
		if (!rDB.addProduct(name, Integer.parseInt(priceString), Integer.parseInt(costString), category.getCategoryID())) {
			((ProductDialog) subDialog).inputError("Product entry failed.");
			return;
		}
		reset();
		((ProductDialog) subDialog).close();
	}

	public void updateProduct(String name, String priceDollars, String priceCents, 
			String costDollars, String costCents, Category category) {
		String priceString = getMoneyString(priceDollars, priceCents);
		String costString = getMoneyString(costDollars, costCents);
		if (priceString == null || costString == null) {
			((ProductDialog) subDialog).inputError("Invalid price input.");
			return;
		}
		if (name.equals("")) {
			((ProductDialog) subDialog).inputError("Please enter a product name.");
			return;
		}
		Product toUpdate = (Product) selectedObject;
		if (!rDB.updateProduct(name, toUpdate.getName(), Integer.parseInt(priceString), 
				Integer.parseInt(costString), category.getCategoryID(), toUpdate.getProductID())) {
			((ProductDialog) subDialog).inputError("Product update failed.");
			return;
		}
		reset();
		((ProductDialog) subDialog).close();
	}

	public void createNewAffiliation(String name, String dollars, String cents, boolean subsidyOn) {
		String creditString = getMoneyString(dollars, cents);
		if (creditString == null) {
			((AffiliationDialog) subDialog).inputError("Invalid credit input.");
			return;
		} 
		if (name.equals("")) {
			((AffiliationDialog) subDialog).inputError("Please enter an affiliation name.");
			return;
		}
		if (!rDB.validAffiliationName(name)) {
			((AffiliationDialog) subDialog).inputError("An affiliation with that name already exists.");
			return;
		}
		if (!rDB.addAffiliation(name, Integer.parseInt(creditString), subsidyOn)) {
			((AffiliationDialog)subDialog).inputError("Affiliation entry failed.");
			return;
		}
		reset();
		((AffiliationDialog) subDialog).close();
	}

	public void updateAffiliation(String name, String dollars, String cents, boolean subsidyOn) {
		String creditString = getMoneyString(dollars, cents);
		if (creditString == null) {
			((AffiliationDialog) subDialog).inputError("Invalid credit input.");
			return;
		} 
		if (name.equals("")) {
			((AffiliationDialog) subDialog).inputError("Please enter an affiliation name.");
			return;
		}
		Affiliation toUpdate = (Affiliation) selectedObject;
		if (!rDB.updateAffiliation(name, toUpdate.getName(), Integer.parseInt(creditString), subsidyOn, toUpdate.getAffiliationID())) {
			((AffiliationDialog) subDialog).inputError("Affiliation update failed.");
			return;
		}
		reset();
		((AffiliationDialog) subDialog).close();
	}

	public void exit() {
		dialogView.close();
	}

	private void reset() {
		try {
			rDB.closeDatabase();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		rDB.initRuntimeDatabase();
		dialogView.dispose();
		dialogView  = new AdminDialog(this, parentFrame, adminTask, rDB.getAffiliations(), rDB.getProductList(), rDB.getCategoryList());
	}


}
