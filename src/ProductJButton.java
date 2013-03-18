import java.awt.Color;
import java.awt.Font;

import javax.swing.JButton;
/**
 * SHPRC-POS
 * ProductJButton.java
 * Creates a JButton formatted for displaying a product; also stores the product in question.
 * 
 * @author Sophi Newman
 * @version 1.0 03/17/2013
 */

public class ProductJButton extends JButton implements SHPRCConstants {

	/* The product this button corresponds to */
	private Product product;
	
	/**
	 * Class constructor.
	 * @param str the name of the product
	 * @param product the stored representation of the product
	 */
	public ProductJButton (String str, Product product) {
		super(str);
		this.product = product;
		this.setFont(new Font("Helvetica", Font.BOLD, 14));
		this.setBackground(getColor());
	}
	
	/**
	 * Returns a background color based on the product's categoryID
	 * @return a background color
	 */
	private Color getColor() {
		return COLORS[(product.getCategoryID()) % COLORS.length];
	}
	
	/**
	 * Returns the product corresponding to the button.
	 * @return the product corresponding to the button
	 */
	public Product getProduct() {
		return product;
	}

}
