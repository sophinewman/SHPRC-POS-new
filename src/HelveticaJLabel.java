import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
/**
 * Creates a JLabel with the font pre-set to bold Helvetica of the specified size.
 * Used for convenience; has no additional functionality.
 * 
 * @author Sophi Newman
 * @version 1.0 03/20/2013
 */


public class HelveticaJLabel extends JLabel {
	/**
	 * Class constructor.
	 * @param str the text to be displayed
	 * @param size the desired font size
	 */
	public HelveticaJLabel(String str, int size) {
		super(str);
		this.setBorder(BorderFactory.createEmptyBorder(1, 4, 1, 4));
		this.setFont(new Font("Helvetica", Font.BOLD, size));
	}
}
