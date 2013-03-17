import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;


public class ProductDialog extends JDialog implements ActionListener {
	private JTextField nameField;
	private JTextField priceDollars;
	private JTextField priceCents;
	private JTextField costDollars;
	private JTextField costCents;
	private JButton cancelButton;
	private JButton saveButton;
	private JComboBox categoriesBox;
	private DialogController controller;
	private boolean updateMode = false;

	public ProductDialog (DialogController controller, JDialog parent, String title, Object[] categories, Boolean updateMode) {
		super(parent, title);

		this.controller = controller;
		
		this.updateMode = updateMode;

		this.setLayout(new BorderLayout(5,5));

		drawNamePane();
		drawCategoryAndPricePane(categories);
		drawControlButtonPane();

		this.pack();
		this.setLocationRelativeTo(parent);
		
	}
	
	public void populateFields(String name, int price, int cost, Category category) {
		if (updateMode) {
			nameField.setText(name);
			categoriesBox.setSelectedItem(category);
			String priceString = Integer.toString(price);
			setMoneyFields(priceDollars, priceCents, priceString);
			String costString = Integer.toString(cost);
			setMoneyFields(costDollars, costCents, costString);

		}
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

	private void drawNamePane() {
		JPanel productNamePane = new JPanel();
		this.add(productNamePane, BorderLayout.NORTH);

		productNamePane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JLabel nameLabel = new JLabel("Product Name:");
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

	private void drawCategoryAndPricePane(Object[] categories) {
		JPanel categoryAndPricePane = new JPanel();
		this.add(categoryAndPricePane, BorderLayout.CENTER);
		categoryAndPricePane.setLayout(new FlowLayout(FlowLayout.CENTER, 2, 5));

		categoryAndPricePane.add(new JLabel(" "));
		JLabel lblCategory = new JLabel("Category:");
		categoryAndPricePane.add(lblCategory);

		categoriesBox = new JComboBox(categories);
		categoryAndPricePane.add(categoriesBox);

		categoryAndPricePane.add(new JLabel(" "));

		JLabel lblPrice = new JLabel("Price: $");
		categoryAndPricePane.add(lblPrice);

		priceDollars = new JTextField("00", 2);
		categoryAndPricePane.add(priceDollars);

		JLabel label = new JLabel(".");
		categoryAndPricePane.add(label);

		priceCents = new JTextField("00", 2);
		categoryAndPricePane.add(priceCents);
		categoryAndPricePane.add(new JLabel(" "));
		
		categoryAndPricePane.add(new JLabel(" "));

		JLabel costLabel = new JLabel("Cost: $");
		categoryAndPricePane.add(costLabel);

		costDollars = new JTextField("00", 2);
		categoryAndPricePane.add(costDollars);

		categoryAndPricePane.add(new JLabel("."));

		costCents = new JTextField("00", 2);
		categoryAndPricePane.add(costCents);
		categoryAndPricePane.add(new JLabel(" "));
	}

	public void actionPerformed(ActionEvent event) {

		Object src = event.getSource();
		if (src == saveButton) {
			controller.createNewProduct(nameField.getText(), priceDollars.getText(), priceCents.getText(), 
					costDollars.getText(), costCents.getText(), (Category)categoriesBox.getSelectedItem(), updateMode);
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




