import java.awt.Color;


public interface SHPRCConstants {
	public static final int N_SUID_DIGITS = 7;
	public static final Color LIGHT_GREY = new Color(153, 153, 153);
	public static final Color[] COLORS = {LIGHT_GREY, new Color(204, 255, 254),
			new Color(204, 204, 255), new Color(244, 204, 244), 
			new Color(255, 204, 222), new Color(204, 255, 204),
			new Color(240, 230, 140)};

	
	public static final int INVALID_NUMBER_INPUT = -1;
	public static final int CANCEL_INPUT = 0;
	public static final Color HIGHLIGHT_YELLOW = new Color(240, 230, 140);
	
	/* indices for the totals array */
	public static final int SUBTOTAL = 0, CREDIT = 1, PT_SUBSIDY = 2, TOTAL = 3;
	
	public static final String PURCHASE_PANE = "Purchase Pane";
	public static final String ADMIN_PANE = "Admin Pane";

}