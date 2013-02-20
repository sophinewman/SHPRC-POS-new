/**
 * SHPRC-POS
 * Product.java
 * Stores information about a product.
 * 
 * @author Sophi Newman
 * @version 0.1 2/8/13
 */

public class Product {

	/* The unique ID number associated with the given product */
	private int productID;
	
	/* The retail price for the given product */
	private int price;
	
	/* The name of the given product */
	private String name;
	
	/* The ID number of the category to which the product belongs */
	private int categoryID;

	
	
	/**
	 * Class constructor.
	 * @param productID the productID of the product to be created
	 * @param price the cost of the product in cents
	 * @param name
	 * @param categoryID
	 */
	public Product(int productID, int price, String name, int categoryID) {
		this.productID = productID;
		this.price = price;
		this.name = name;
		this.categoryID = categoryID;

	}
	

	/**
	 * Returns the price in cents of the product.
	 * @return the price of the product
	 */
	public int getPrice() {
		return price;
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
	 * Returns the name of the product.
	 * @return the name of the product.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * For debugging purposes only.
	 * @return the string representation of the Product
	 */
	@Override
	public String toString() {
		return "Name: " + name + " Category ID: " + categoryID + " Price: "+ price;
	}

}
