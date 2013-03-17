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
			rDB.deleteProductByName(toDelete.getName());
			reset();
		}
	}


	public void addAffiliation() {

	}

	public void updateAffiliation() {

	}

	public void deleteAffiliation() {

	}

	public void addCategory() {

	}

	public void updateCategory() {

	}

	public void deleteCategory() {

	}

	public void exit() {
		dialogView.close();
	}

	public void save() {
		System.out.println("Saving!");
	}

	public void itemSelected(Object obj) {
		selectedObject = obj;
		dialogView.enableUpdateAndDelete();
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

	private boolean validProductName(String name) {
		return rDB.validProductName(name);
	}

	public void createNewProduct(String name, String priceDollars, String priceCents, String costDollars, String costCents, Category category, boolean updateMode) {
		String priceString = getMoneyString(priceDollars, priceCents);
		String costString = getMoneyString(costDollars, costCents);
		if (priceString == null || costString == null) {
			((ProductDialog) subDialog).inputError("Invalid price input.");
			return;
		}
		if (updateMode) {
			rDB.deleteProductByName(name);
		}
		if (!validProductName(name)) {
			((ProductDialog) subDialog).inputError("A product with that name already exists.");
			return;
		}
		if (!rDB.addProduct(name, Integer.parseInt(priceString), Integer.parseInt(costString), category.getCategoryID())) {
			((ProductDialog) subDialog).inputError("Product entry failed.");
		}
		reset();
		((ProductDialog) subDialog).close();
	}
	
	private void reset() {
		rDB.initRuntimeDatabase();
		dialogView.dispose();
		dialogView  = new AdminDialog(this, parentFrame, adminTask, rDB.getAffiliations(), rDB.getProductList(), rDB.getCategoryList());
	}

}
