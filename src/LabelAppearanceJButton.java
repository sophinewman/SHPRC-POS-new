import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;


public class LabelAppearanceJButton extends JButton implements SHPRCConstants {
	
	private int productID;

	public LabelAppearanceJButton(String str, int productID) {
		super(str);
		this.productID = productID;
		this.setFocusPainted(false);
		this.setBorder(BorderFactory.createEmptyBorder(1, 4, 1, 4));
		this.setContentAreaFilled(false);
		this.setBorderPainted(false);
		this.setOpaque(false);
		this.setFont(new Font("Helvetica", Font.BOLD, 16));
		this.setVisible(true);
	}
	
	public void setHighlight() {
		this.setBackground(HIGHLIGHT_YELLOW);
		this.setOpaque(true);
	}

	public int getProductID() {
		return productID;
	}



}
