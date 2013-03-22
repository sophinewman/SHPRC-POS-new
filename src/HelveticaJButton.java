import java.awt.Font;
import javax.swing.*;

/**
 * Creates a JButton with the font pre-set to bold Helvetica of the specified size.
 * Used for convenience; has no additional functionality.
 * 
 * @author Sophi Newman
 * @version 1.0 03/17/2013
 */

public class HelveticaJButton extends JButton {

	/**
	 * Class constructor.
	 * @param str the text to be displayed
	 * @param size the desired font size
	 */
	public HelveticaJButton(String str, int size) {
		super(str);
		this.setFont(new Font("Helvetica", Font.BOLD, size));
	}
}
