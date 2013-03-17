import java.awt.Color;
import java.text.NumberFormat;
import java.util.Locale;


public interface SHPRCConstants {
	public static final int N_SUID_DIGITS = 7;
	public static final Color LIGHT_GREY = new Color(153, 153, 153);
	public static final Color[] COLORS = { new Color(255, 204, 222), 
			new Color(204, 255, 254), new Color(204, 255, 204), 
			new Color(204, 204, 255), new Color(244, 204, 244)};

	
	public static final int INVALID_NUMBER_INPUT = -1;
	public static final int CANCEL_INPUT = 0;
	public static final Color HIGHLIGHT_YELLOW = new Color(240, 230, 140);
	
	/* indices for the totals array */
	public static final int SUBTOTAL = 0, CREDIT = 1, PT_SUBSIDY = 2, TOTAL = 3;
	
	public static final String PURCHASE_PANE = "Purchase Pane";
	public static final String ADMIN_PANE = "Admin Pane";
	
	public static final String MANAGE_CATEGORIES = "Manage Categories";
	public static final String MANAGE_PRODUCTS = "Manage Products";
	public static final String MANAGE_AFFILIATIONS_AND_CREDIT = "Manage Affiliations and Credit";
	
	public NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance(Locale.US);

}