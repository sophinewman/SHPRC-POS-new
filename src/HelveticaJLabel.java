import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;


public class HelveticaJLabel extends JLabel {
	public HelveticaJLabel(String str, int size) {
		super(str);
		this.setBorder(BorderFactory.createEmptyBorder(1, 4, 1, 4));
		this.setFont(new Font("Helvetica", Font.BOLD, size));
	}

}
