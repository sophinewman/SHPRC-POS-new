

public class PurchaseController implements SHPRCConstants {
	private PurchaseModel model;
	private PurchaseView view;
	private RuntimeDatabase rDB;
	
	public PurchaseController(PurchaseModel model, RuntimeDatabase rDB) {
		this.model = model;
		view = new PurchaseView(this, rDB.getProductList());
		this.rDB = rDB;
		
	}
	
	public void addProduct(int productID) {

		int quantity;
		if (productID == 100 || productID == 102 || productID == 200 ) {
			quantity = view.getQuantity();
		} else {
			quantity = 1;
		}
		
		model.addProduct(rDB.getProduct(productID), quantity);
		view.displayPurchase(model.getPurchaseProducts(), model.tallyPurchaseTotal());
	}
	
	public boolean setClient(String suidStr, String affiliation) {
		if (!suidStr.matches("[0-9]+") || suidStr.length() < N_SUID_DIGITS) {
			view.inputError("Please enter a valid SUID.");
			return false;
		}
		if (affiliation.equals(AFFILIATIONS[0])) {
			view.inputError("Please select a valid community affiliation.");
			return false;
		}
		
		int suid = Integer.parseInt(suidStr.substring(suidStr.length() - N_SUID_DIGITS));
		model.setCurrentClient(suid, 1001);
		System.out.println(model.getCurrentClient());
		view.displayPurchase(model.getPurchaseProducts(), model.tallyPurchaseTotal());
		return true;
		
	}

}
