import java.awt.Font;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;


public class LabelAppearanceJButton extends JButton {

	public LabelAppearanceJButton(String str) {
		super(str);
		this.setFocusPainted(false);
		this.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
		this.setContentAreaFilled(false);
		this.setBorderPainted(false);
		this.setOpaque(false);
		this.setFont(new Font("Helvetica", Font.BOLD, 14));
		this.setVisible(true);
	}





}
