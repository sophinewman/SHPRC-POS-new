import java.text.NumberFormat;
import java.util.Locale;

/**
 * SHPRC-POS
 * Product.java
 * Stores information about a product.
 * 
 * @author Sophi Newman
 * @version 0.1 2/8/13
 */

public class Product implements SHPRCConstants{

	/* The unique ID number associated with the given product */
	private int productID;
	
	/* The retail price for the given product */
	private int price;
	
	private int cost;
	
	/**
	 * @return the cost
	 */
	public int getCost() {
		return cost;
	}

	/* The productName of the given product */
	private String productName;
	
	/* The ID number of the category to which the product belongs */
	private int categoryID;
	
	private String categoryName;

	
	
	/**
	 * Class constructor.
	 * @param productID the productID of the product to be created
	 * @param price the cost of the product in cents
	 * @param productName
	 * @param categoryID
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
	 * For debugging purposes only.
	 * @return the string representation of the Product
	 */
	@Override
	public String toString() {
		
		return "<html><u>" + productName + ":</u> " + CURRENCY_FORMAT.format(price/100.0) + ", <i>" + categoryName + "</i></html>";
	}

}
