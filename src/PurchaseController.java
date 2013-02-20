
public class PurchaseController {
	private PurchaseModel model;
	private PurchaseView view;
	private RuntimeDatabase rDB;
	
	public PurchaseController(PurchaseModel model, RuntimeDatabase rDB) {
		this.model = model;
		view = new PurchaseView(this, rDB.getProductList());
		this.rDB = rDB;
		
	}
	
	public void addItem(int productID) {
		model.addProduct(rDB.getProduct(productID), 1);
		System.out.println("adding " + productID);
	}

}
