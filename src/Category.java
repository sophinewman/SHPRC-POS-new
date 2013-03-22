/**
 * Stores information about a merchandise category, which is used to group similar items.
 * 
 * @author Sophi Newman
 * @version 1.0 03/20/2013
 */

public class Category {
	
	/* The unique integer specifying the category */
	private int categoryID;
	
	/* The name of the category */
	private String categoryName;
	
	/**
	 * Class constructor.
	 * @param categoryID the unique numeric identifier associated with the category
	 * @param categoryName the category name
	 */
	public Category (int categoryID, String categoryName) {
		this.categoryID = categoryID;
		this.categoryName = categoryName;
	}

	/**
	 * Returns the categoryID.
	 * @return the categoryID
	 */
	public int getCategoryID() {
		return categoryID;
	}

	/**
	 * Returns the category name.
	 * @return the categoryName
	 */
	public String getCategoryName() {
		return categoryName;
	}
	
	/**
	 * Returns a string representing the category. Used in JList display.
	 * @return a string representing the category
	 */
	@Override
	public String toString() {
		return categoryName + " (" + categoryID +")";
	}

}
