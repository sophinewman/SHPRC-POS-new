import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

/**
 * Draws the dialog view for updating, adding, and deleting affiliations in administrator view.
 * 
 * @author Sophi Newman
 * @version 1.0 03/20/2013
 */

public class AffiliationDialog extends JDialog implements ActionListener {
	
	/* The MVC controller that connects the view to the back end */
	private DialogController controller;
	
	/* Whether the user is updating an affiliation or adding a new one */
	private boolean updateMode = false;
	
	/* The text field where the affiliation's name is specified */
	private JTextField nameField;
	
	/* The text field where the number of dollars the affiliation receives is specified */
	private JTextField creditDollars;
	
	/* The text field where the number of cents the affiliation receives is specified */
	private JTextField creditCents;
	
	/* Buttons for toggling the pregnancy test on and off */
	private JRadioButton subsidyOn;
	private JRadioButton subsidyOff;
	
	/* Dialog control buttons */
	private JButton cancelButton;
	private JButton saveButton;
	

	/**
	 * Class constructor.
	 * @param controller the MVC framework controller to be passed in
	 * @param parent the parent container
	 * @param title the title of the dialog
	 * @param updateMode whether update mode is on
	 */
	public AffiliationDialog (DialogController controller, JDialog parent, String title, boolean updateMode) {
		super(parent, title);

		this.controller = controller;
		
		this.updateMode = updateMode;

		getContentPane().setLayout(new BorderLayout(5,5));

		drawNamePane();
		drawCreditAndSubsidyPane();
		drawControlButtonPane();

		this.pack();
		this.setLocationRelativeTo(parent);
		
	}
	
	
	/**
	 * Sets the fields to the values specified.
	 * @param name the affiliation name
	 * @param credit the credit in cents the affiliation receives
	 * @param testSubsidized whether the affiliation receives a pregnancy test subsidy
	 */
	public void populateFields(String name, int credit, boolean testSubsidized) {
			nameField.setText(name);
			String creditString = Integer.toString(credit);
			setMoneyFields(creditDollars, creditCents, creditString);
			JRadioButton button = testSubsidized ? subsidyOn : subsidyOff;
			button.setSelected(true);
	}
	
	
	/**
	 * Draws the JPanel that holds the name field
	 */
	private void drawNamePane() {
		JPanel productNamePane = new JPanel();
		getContentPane().add(productNamePane, BorderLayout.NORTH);

		productNamePane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JLabel nameLabel = new JLabel("<html><u>Affiliation Name:</u></html> ");
		productNamePane.add(nameLabel);

		nameField = new JTextField(12);
		productNamePane.add(nameField);
	}

	
	/**
	 * Draws the JPanel that holds the control buttons
	 */
	private void drawControlButtonPane() {
		JPanel controlButtonPane = new JPanel();
		getContentPane().add(controlButtonPane, BorderLayout.SOUTH);
		controlButtonPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(this);
		controlButtonPane.add(cancelButton);

		controlButtonPane.add(new JLabel("   "));

		saveButton = new JButton("Save");
		saveButton.addActionListener(this);
		controlButtonPane.add(saveButton);
	}

	
	/**
	 * Draws the JPanel that displays the credit and subsidy fields / buttons
	 */
	private void drawCreditAndSubsidyPane() {
		JPanel creditAndSubsidyPane = new JPanel();
		getContentPane().add(creditAndSubsidyPane, BorderLayout.CENTER);
		creditAndSubsidyPane.setLayout(new FlowLayout(FlowLayout.CENTER, 2, 5));
		
		creditAndSubsidyPane.add(new JLabel("  "));
		JLabel lblPrice = new JLabel("<html><u>Credit:</u></html> $");
		creditAndSubsidyPane.add(lblPrice);

		creditDollars = new JTextField("00", 2);
		creditDollars.setHorizontalAlignment(SwingConstants.RIGHT);
		creditAndSubsidyPane.add(creditDollars);
		creditAndSubsidyPane.add(new JLabel("."));
		creditCents = new JTextField("00", 2);
		creditAndSubsidyPane.add(creditCents);
		
		ButtonGroup group = new ButtonGroup();
		subsidyOn = new JRadioButton();
		subsidyOff = new JRadioButton();
		subsidyOff.setSelected(true);
		group.add(subsidyOn);
		group.add(subsidyOff);
		
		creditAndSubsidyPane.add(new JLabel("  "));
		creditAndSubsidyPane.add(new JLabel("<html><u>Pregnancy Test Subsidy:</u></html>"));
		creditAndSubsidyPane.add(new JLabel(" On:"));
		creditAndSubsidyPane.add(subsidyOn);
		creditAndSubsidyPane.add(new JLabel(" Off:"));
		creditAndSubsidyPane.add(subsidyOff);
		creditAndSubsidyPane.add(new JLabel("  "));
		
	}
	
	
	/**
	 * Processes the string representation of credit into a dollars string and a cents string
	 * and sets these values in the view.
	 * @param dollarsField the text field where dollars are entered
	 * @param centsField the text field where cents are entered
	 * @param moneyString the string representation to be set
	 */
	private void setMoneyFields (JTextField dollarsField, JTextField centsField, String moneyString) {
		String dollarsString;
		String centsString;
		if (moneyString.length() >= 2) {
			dollarsString = moneyString.substring(0, moneyString.length()-2);
			centsString = moneyString.substring(moneyString.length()-2, moneyString.length());
		} else {
			dollarsString = "";
			centsString = "0" + moneyString;
		}
		if (dollarsString.equals("")) {
			dollarsString = "00";
		}
		if (centsString.equals("")) {
			centsString = "00";
		}
		if (centsString.length() == 1) {
			centsString += "0";
		}
		dollarsField.setText(dollarsString);
		centsField.setText(centsString);
	}

	
	/**
	 * Handles all ActionEvents by triggering the appropriate response by the controller.
	 */
	@Override
	public void actionPerformed(ActionEvent event) {
		Object src = event.getSource();
		if (src == saveButton) {
			if (!updateMode) {
				controller.createNewAffiliation(nameField.getText(), creditDollars.getText(), creditCents.getText(), subsidyOn.isSelected());
			} else {
				controller.updateAffiliation(nameField.getText(), creditDollars.getText(), creditCents.getText(), subsidyOn.isSelected());
			}
		} else if (src == cancelButton) {
			close();
		}
	}

	
	/**
	 * Closes the dialog box.
	 */
	public void close() {
		this.dispose();
	}

	/**
	 * Displays an error message to the user.
	 * @param errorMessage the message to be displayed
	 */
	public void inputError (String errorMessage) {
		JOptionPane.showMessageDialog(this, errorMessage, "Input Error", JOptionPane.ERROR_MESSAGE);
	}

	

}




