import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Component;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Box;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 * Displays a total and change due for a given purchase.
 * 
 * @author Sophi Newman
 * @version 1.0 03/20/2013
 */


public class TotalDialog extends JDialog implements ActionListener {

	/* The controller that controls the TotalDialog */
	private PurchaseController controller;
	
	private Box verticalBox; // holds dialog components
	private JPanel controlButtonPane; // displays control buttons
	private JPanel mainPane; // displays the main content
	
	private JTextField tendered; // accepts cash tendered entry
	private HelveticaJButton cancelButton, submitButton, continueButton; // control program flow


	/**
	 * Class constructor.
	 * @param controller the controller to be passed in
	 * @param frame the parent container
	 * @param total the string representing the purchase total
	 */
	public TotalDialog (PurchaseController controller, JFrame frame, String total) {
		super(frame, "Purchase Total"/*, true*/);

		this.controller = controller;

		mainPane = new JPanel(new BorderLayout(5,5));
		drawDummyLabels();
		drawTotalAndTendered(total);
		drawControlButtons();

		this.add(mainPane);
		this.pack();
		this.setLocationRelativeTo(frame);
		this.setVisible(true);
	}

	
	/**
	 * Draws control buttons.
	 */
	private void drawControlButtons() {
		controlButtonPane = new JPanel(new FlowLayout(FlowLayout.CENTER));

		cancelButton = new HelveticaJButton("Cancel", 14);
		cancelButton.addActionListener(this);
		controlButtonPane.add(cancelButton);

		controlButtonPane.add(new JLabel("   "));

		submitButton = new HelveticaJButton("Submit", 14);
		submitButton.addActionListener(this);
		controlButtonPane.add(submitButton);

		mainPane.add(controlButtonPane, BorderLayout.SOUTH);
	}
	
	
	/**
	 * Draws the total label and tendered field.
	 * @param total the string representing the purchase total
	 */
	private void drawTotalAndTendered(String total) {
		verticalBox = Box.createVerticalBox();
		mainPane.add(verticalBox, BorderLayout.CENTER);
		HelveticaJLabel totalLabel = new HelveticaJLabel("<html>Client's total is: <u>" + total + "</u></html>", 16);
		totalLabel.setHorizontalAlignment(SwingConstants.CENTER);
		totalLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		verticalBox.add(totalLabel);
		verticalBox.add(new JLabel("   "));

		JPanel tenderedPane = new JPanel(new FlowLayout(5));
		tenderedPane.add(new HelveticaJLabel("Cash tendered: ", 16));
		tendered = new JTextField(5);
		tendered.addActionListener(this);
		tenderedPane.add(tendered);
		tenderedPane.setAlignmentX(Component.CENTER_ALIGNMENT);
		verticalBox.add(tenderedPane);
	}
	

	/**
	 * Adds padding labels to layout.
	 */
	private void drawDummyLabels() {
		mainPane.add(new JLabel("   "), BorderLayout.WEST);
		mainPane.add(new JLabel("   "), BorderLayout.EAST);
		mainPane.add(new JLabel(" "), BorderLayout.NORTH);
	}


	/**
	 * Shows how much change is due to the client.
	 * @param change the string representation of the change to be given
	 */
	public void showChangeMessage(String change) {
		controlButtonPane.removeAll();
		verticalBox.setVisible(false);

		drawContinueButton();
		drawChangeLabel(change);

		this.validate();
		this.setVisible(true);
	}
	
	
	/**
	 * Draws the change label
	 * @param change the string representation of the change to be given
	 */
	private void drawChangeLabel(String change) {
		HelveticaJLabel changeLabel = new HelveticaJLabel("<html>Change due: <u>" + change + "</u></html>", 16);
		changeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		changeLabel.setHorizontalAlignment(SwingConstants.CENTER);

		mainPane.add(changeLabel, BorderLayout.CENTER);
	}
	
	
	/**
	 * Draws the continue button. 
	 */
	private void drawContinueButton() {
		continueButton = new HelveticaJButton("Continue", 14);
		continueButton.addActionListener(this);
		getRootPane().setDefaultButton(continueButton); // enables pressing "Return" or "Enter" to close dialog.
		controlButtonPane.add(continueButton);
	}

	
	/**
	 * Handles all ActionEvents by triggering the appropriate response of the controller.
	 */
	@Override
	public void actionPerformed(ActionEvent event) {
		Object src = event.getSource();
		if (src == cancelButton) {
			this.dispose();
			
		} else if (src == submitButton || src == tendered) {
			if (! controller.cashTenderedAction(tendered.getText(), this)) {
				JOptionPane.showMessageDialog(this, "Invalid entry. Please try again.", "Input Error", JOptionPane.ERROR_MESSAGE);
			}
			
		} else if (src == continueButton) {
			controller.commitPurchase();
			this.dispose();
		}
	}

}
