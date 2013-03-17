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

	
	/*
	 * 	private int affiliationID;
	private String affiliationName;
	private int credit;
	private boolean qualifiesForPregnancyTest;
	 */
	public AffiliationDialog (DialogController controller, JDialog parent, String title, Boolean updateMode) {
		super(parent, title);

		this.controller = controller;
		
		this.updateMode = updateMode;

		this.setLayout(new BorderLayout(5,5));

		drawNamePane();
		drawCreditAndSubsidyPane();
		drawControlButtonPane();

		this.pack();
		this.setLocationRelativeTo(parent);
		
	}
	
	public void populateFields(String name, int price, int cost, Category category) {
		if (updateMode) {
			nameField.setText(name);

		}
	}
	
	private void drawNamePane() {
		JPanel productNamePane = new JPanel();
		this.add(productNamePane, BorderLayout.NORTH);

		productNamePane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JLabel nameLabel = new JLabel("Affiliation Name: ");
		productNamePane.add(nameLabel);

		nameField = new JTextField(12);
		productNamePane.add(nameField);
	}

	private void drawControlButtonPane() {
		JPanel controlButtonPane = new JPanel();
		this.add(controlButtonPane, BorderLayout.SOUTH);
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
		this.add(creditAndSubsidyPane, BorderLayout.CENTER);
		creditAndSubsidyPane.setLayout(new FlowLayout(FlowLayout.CENTER, 2, 5));

		creditAndSubsidyPane.add(new JLabel(" "));
		JLabel lblCategory = new JLabel("Category:");
		creditAndSubsidyPane.add(lblCategory);
		

		JLabel lblPrice = new JLabel("Price: $");
		creditAndSubsidyPane.add(lblPrice);

		creditDollars = new JTextField("00", 2);
		creditAndSubsidyPane.add(creditDollars);

		JLabel label = new JLabel(".");
		creditAndSubsidyPane.add(label);

		creditCents = new JTextField("00", 2);
		creditAndSubsidyPane.add(creditCents);

		
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




