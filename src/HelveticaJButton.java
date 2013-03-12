import java.awt.Font;
import javax.swing.*;


public class HelveticaJButton extends JButton {

	public HelveticaJButton(String str, int size) {
		super(str);
		this.setFont(new Font("Helvetica", Font.BOLD, size));
	}
}
