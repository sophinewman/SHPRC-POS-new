/**
 * SHPRC-POS
 * Product.java
 * Stores information about a product.
 * 
 * @author Sophi Newman
 * @version 1.0 03/17/2013
 */

public class Product implements SHPRCConstants{

	/* The unique ID number associated with the given product */
	private int productID;
	
	/* The retail price for the given product */
	private int price;
	
	/* The cost to the seller of the given product */
	private int cost;

	/* The productName of the given product */
	private String productName;
	
	/* The ID number of the category to which the product belongs */
	private int categoryID;
	
	/* The name of the category to which the product belongs */
	private String categoryName;

	
	/**
	 * Class constructor.
	 * @param productID the productID of the product to be created
	 * @param price the retail price of the product in cents
	 * @param cost the cost to seller of the product in cents
	 * @param productName the name of the product
	 * @param categoryID the categoryID of the product to be created
	 * @param categoryName the name of the category to which the product belongs
	 */
	public Product(int productID, int price, int cost, String productName, int categoryID, String categoryName) {
		this.productID = productID;
		this.price = price;
		this.cost = cost;
		this.productName = productName;
		this.categoryID = categoryID;
		this.categoryName = categoryName;

	}
	

	/**
	 * Returns the price in cents of the product.
	 * @return the price of the product
	 */
	public int getPrice() {
		return price;
	}
	
	
	/**
	 * Returns the cost in cents of the product.
	 * @return the cost of the product
	 */
	public int getCost() {
		return cost;
	}

	/**
	 * Returns the unique product ID associated with the product.
	 * @return the productID of the product
	 */
	public int getProductID() {
		return productID;
	}
	
	
	/**
	 * Returns the merchandise category ID of the product.
	 * @return the categoryID of the product
	 */
	public int getCategoryID() {
		return categoryID;
	}
	
	/**
	 * Returns the productName of the product.
	 * @return the productName of the product.
	 */
	public String getName() {
		return productName;
	}
	
	/**
	 * Returns a string representation of the product. Used in JList display.
	 * @return the string representation of the product
	 */
	@Override
	public String toString() {
		return "<html><u>" + productName + ":</u> " + CURRENCY_FORMAT.format(price/100.0) +
			", <i>" + categoryName + "</i></html>";
	}

}
