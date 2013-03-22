import java.awt.Color;
import java.text.NumberFormat;
import java.util.Locale;
/**
 * Enumerates a series of constants used throughout the program.
 * 
 * @author Sophi Newman
 * @version 1.0 03/20/2013
 */

public interface SHPRCConstants {
	
	/* The number of digits in an SUID */
	public static final int N_SUID_DIGITS = 7; 
	
	/* Colors used in PurchaseView */
	public static final Color LIGHT_GREY = new Color(153, 153, 153); 
	public static final Color HIGHLIGHT_YELLOW = new Color(240, 230, 140); 
	
	/* The colors used to set a ProductButton background */
	public static final Color[] COLORS = {  new Color(204, 255, 204) /*lime*/, 
		new Color(204, 255, 254) /*aqua*/, new Color(204, 204, 255) /*lavender*/, 
		new Color(244, 204, 244) /*pink*/, new Color(255, 204, 222) /*coral*/};
	
	/* The products typically purchased in quantity */
	public static final int MALE_CONDOM = 100;
	public static final int GLOW_CONDOM = 102;
	public static final int LUBE_FOIL = 200;
	
	/* Indices for array of recent purchases */
	public static final int MOST_RECENT = 0;
	public static final int MIDDLE = 1;
	public static final int LEAST_RECENT = 2;
	
	/* Sentinel values for number input */
	public static final int INVALID_NUMBER_INPUT = -1;
	public static final int CANCEL_INPUT = 0;

	
	/* Indices for PurchaseModel totals array */
	public static final int SUBTOTAL = 0, CREDIT = 1, PT_SUBSIDY = 2, TOTAL = 3;
	
	/* The string constants that specify which pane to be displayed */
	public static final String PURCHASE_PANE = "Purchase Pane";
	public static final String ADMIN_PANE = "Admin Pane";
	
	/* The string constants that specify administrative tasks */
	public static final String MANAGE_CATEGORIES = "Manage Categories";
	public static final String MANAGE_PRODUCTS = "Manage Products";
	public static final String MANAGE_AFFILIATIONS_AND_CREDIT = "Manage Affiliations and Credit";
	
	/* The NumberFormat used to format the product prices */
	public NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance(Locale.US);
	
	/* Minimum ID values */
	public static final int MINIMUM_AFFILIATION_ID = 1000;
	public static final int MINIMUM_CATEGORY_ID = 1;
	public static final int MINIMUM_PURCHASE_ID = 3000;
	public static final int MINIMUM_PRODUCT_ID_FACTOR = 100;

}