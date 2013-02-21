import java.awt.Color;
import java.awt.Font;

import javax.swing.JButton;


public class ProductJButton extends JButton implements SHPRCConstants {


	private int productID;
	
	public ProductJButton (String str, int productID) {
		super(str);
		this.productID = productID;
		this.setFont(new Font("Helvetica", Font.BOLD, 14));
		this.setBackground(getColor());
	}
	
	private Color getColor() {
		if (productID/100 >= COLORS.length) {
			return LIGHT_GREY;
			
		}
		return COLORS[productID/100];
	}
	
	public int getProductID() {
		return productID;
	}

}
