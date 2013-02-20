import java.awt.Color;
import java.awt.Font;

import javax.swing.JButton;


public class ProductJButton extends JButton {
	private Color[] colors = {Color.BLACK, new Color(204, 255, 254),
			new Color(204, 204, 255), new Color(244, 204, 244), 
			new Color(189, 205, 222), new Color(204, 255, 204)};

	private int productID;
	
	public ProductJButton (String str, int productID) {
		super(str);
		this.productID = productID;
		this.setFont(new Font("Helvetica", Font.BOLD, 14));
		this.setBackground(colors[productID/100]);
	}
	
	public int getProductID() {
		return productID;
	}

}
