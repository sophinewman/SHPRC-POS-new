import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
/**
 * SHPRC-POS
 * PurchaseProductJButton.java
 * Creates a JButton formatted for the purchased product list and stores the associated product.
 * 
 * @author Sophi Newman
 * @version 1.0 03/17/2013
 */

public class PurchaseProductJButton extends JButton implements SHPRCConstants {

	/* The product this button corresponds to */
	private Product product;
	
	/**
	 * Class constructor.
	 * @param str the text to be displayed
	 * @param product the product the button will store
	 */
	public PurchaseProductJButton(String str, Product product) {
		super(str);
		this.product = product;
		this.setFocusPainted(false);
		this.setBorder(BorderFactory.createEmptyBorder(1, 4, 1, 4));
		this.setContentAreaFilled(false);
		this.setBorderPainted(false);
		this.setOpaque(false);
		this.setFont(new Font("Helvetica", Font.BOLD, 16));
		this.setVisible(true);
	}
	

	/**
	 * Sets whether or not a button should be highlighted.
	 * @param on whether the button should be highlighted
	 */
	public void setHighlight(boolean on) {
		if (on) {
			this.setBackground(HIGHLIGHT_YELLOW);
			this.setOpaque(true);
		} else {
			this.setBackground(Color.WHITE);
		}
	}

	
	/**
	 * Returns the product associated with the button.
	 * @return the product associated with the button
	 */
	public Product getProduct() {
		return product;
	}



}
