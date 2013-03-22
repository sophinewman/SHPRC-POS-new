import java.sql.SQLException;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
/**
 * Connects an administrator dialog view to a runtime database to allow for back end updates.
 * 
 * @author Sophi Newman
 * @version 1.0 03/20/2013
 */

public class DialogController {

	/* The runtime database access point */
	private RuntimeDatabase rDB;
	
	/* The view of the root dialog displaying a list of objects to be updated */
	private AdminDialog dialogView = null;
	
	/* The parent frame */
	private JFrame parentFrame;
	
	/* The selected affiliation, category, or product */
	private Object selectedObject;
	
	/* The object editor dialog box view */
	private JDialog subDialog;
	
	/* Which administrative task to be carried out (affiliation, category, or product) */
	private String adminTask;
	

	/**
	 * Class constructor.
	 * @param rDB the runtime database back end
	 * @param parentFrame the parent frame
	 * @param adminTask which task to be carried out
	 */
	public DialogController (RuntimeDatabase rDB, JFrame parentFrame, String adminTask) {
		this.rDB = rDB;
		this.parentFrame = parentFrame;
		this.adminTask = adminTask;
		dialogView = new AdminDialog(this, parentFrame, adminTask, rDB.getAffiliations(), rDB.getProductList(), rDB.getCategoryList());
	}

	/**
	 * Saves whatever object the user has selected form the list
	 * @param obj the object to be saved
	 */
	public void itemSelected(Object obj) {
		selectedObject = obj;
		dialogView.enableUpdateAndDelete();
	}

	/**
	 * Brings up a dialog to allow the user to add a product.
	 */
	public void addProduct() {
		subDialog = new ProductDialog(this, dialogView, "Update Product", rDB.getCategoryList().toArray(), false);
		subDialog.setVisible(true);
	}


	/**
	 * Brings up a dialog to allow the user to update a product.
	 */
	public void updateProduct() {
		subDialog = new ProductDialog(this, dialogView, "Update Product", rDB.getCategoryList().toArray(), true);

		Product product = (Product) selectedObject;
		int categoryID = product.getCategoryID();
		Category category = rDB.getCategory(categoryID);

		((ProductDialog) subDialog).populateFields(product.getName(), product.getPrice(), product.getCost(), category);
		subDialog.setVisible(true);

	}

	
	/**
	 * Brings up a dialog to allow the user to delete a product. 
	 */
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


	/**
	 * Brings up a dialog to allow the user to add an affiliation.
	 */
	public void addAffiliation() {
		subDialog = new AffiliationDialog(this, dialogView, "Add Affiliation", false);
		subDialog.setVisible(true);
	}

	
	/**
	 * Brings up a dialog to allow the user to update of an affiliation. 
	 */
	public void updateAffiliation() {
		subDialog = new AffiliationDialog(this, dialogView, "Update Affiliation", true);

		Affiliation affiliation = (Affiliation) selectedObject;
		((AffiliationDialog)subDialog).populateFields(affiliation.getName(), affiliation.getCredit() * -1, affiliation.qualifiesForPregnancyTest());

		subDialog.setVisible(true);

	}

	
	/**
	 * Brings up a dialog to allow the user to delete an affiliation. 
	 */
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

	
	/**
	 * Brings up a dialog to allow the user to add a category. 
	 */
	public void addCategory() {
		subDialog = new CategoryDialog(this, dialogView, "Add Category", false);
		subDialog.setVisible(true);

	}

	
	/**
	 * Brings up a dialog to allow the user to update a category. 
	 */
	public void updateCategory() {
		subDialog = new CategoryDialog(this, dialogView, "Update Category", true);

		Category category = (Category) selectedObject;
		((CategoryDialog)subDialog).populateFields(category.getCategoryName());

		subDialog.setVisible(true);

	}

	
	/**
	 * Brings up a dialog to allow the user to delete a category.
	 */
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

	
	/**
	 * Validates entered dollars and cents strings and returns the concatenation of the two.
	 * @param dollars the dollars string
	 * @param cents the cents string
	 * @return a valid concatenated dollars + cents string
	 */
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
	

	/**
	 * Attempts to write a new category to the database.
	 * @param name the name of the new category
	 */
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
	

	/**
	 * Attempts to update an existing category in the database.
	 * @param name the name of the category
	 */
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

	
	/**
	 * Attempts to add a new product to the database
	 * @param name the name of the product to be added
	 * @param priceDollars the string representing the dollars in the price
	 * @param priceCents the string representing the cents in the price
	 * @param costDollars the string representing the dollars in the cost
	 * @param costCents the string representing the cents in the cost
	 * @param category the product category
	 */
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

	
	/**
	 * Attempts to update a product in the database.
	 * @param name the name of the product to be updated
	 * @param priceDollars the string representing the dollars in the price
	 * @param priceCents the string representing the cents in the price
	 * @param costDollars the string representing the dollars in the cost
	 * @param costCents the string representing the cents in the cost
	 * @param category the product category
	 */
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
	

	/**
	 * Attempts to add a new affiliation to the database.
	 * @param name the name of the affiliation to be added
	 * @param dollars the string representing the dollars of credit to be allotted
	 * @param cents the string representing the cents of credit to be allotted
	 * @param subsidyOn whether this affiliation receives a free pregnancy test
	 */
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

	
	/**
	 * Attempts to update an affiliation in the database.
	 * @param name the name of the affiliation to be updated
	 * @param dollars the string representing the dollars of credit to be allotted
	 * @param cents the string representing the cents of credit to be allotted
	 * @param subsidyOn whether this affiliation receives a free pregnancy test
	 */
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

	
	/**
	 * Closes the dialog. 
	 */
	public void exit() {
		dialogView.close();
	}

	
	/**
	 * Resets the object lists after update. 
	 */
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
