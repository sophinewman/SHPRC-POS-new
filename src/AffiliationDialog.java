import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;


public class AffiliationDialog extends JDialog implements ActionListener {
	private JTextField nameField;
	private JTextField creditDollars;
	private JTextField creditCents;
	private JRadioButton subsidyOn;
	private JRadioButton subsidyOff;
	private JButton cancelButton;
	private JButton saveButton;
	private DialogController controller;
	private boolean updateMode = false;

	

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
	
	public void populateFields(String name, int credit, boolean testSubsidized) {
			nameField.setText(name);
			String creditString = Integer.toString(credit);
			setMoneyFields(creditDollars, creditCents, creditString);
			JRadioButton button = testSubsidized ? subsidyOn : subsidyOff;
			button.setSelected(true);

	}
	
	private void drawNamePane() {
		JPanel productNamePane = new JPanel();
		getContentPane().add(productNamePane, BorderLayout.NORTH);

		productNamePane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JLabel nameLabel = new JLabel("<html><u>Affiliation Name:</u></html> ");
		productNamePane.add(nameLabel);

		nameField = new JTextField(12);
		productNamePane.add(nameField);
	}

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


	
	public void close() {
		this.dispose();
	}

	public void inputError (String errorMessage) {
		JOptionPane.showMessageDialog(this, errorMessage, "Input Error", JOptionPane.ERROR_MESSAGE);
	}

	

}




