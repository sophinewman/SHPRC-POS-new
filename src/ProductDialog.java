import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

/**
 * Draws the dialog view for updating, adding, and deleting products in administrator view.
 * 
 * @author Sophi Newman
 * @version 1.0 03/20/2013
 */

public class ProductDialog extends JDialog implements ActionListener {
	
	/* The MVC controller that connects the view to the back end */
	private DialogController controller;
	
	/* Whether the user is updating a product or adding a new one */
	private boolean updateMode = false;
	
	/* The text field where the product's name is specified */
	private JTextField nameField;
	
	/* The text field where the number of dollars in a price is specified */
	private JTextField priceDollars;
	
	/* The text field where the number of cents in a price is specified */
	private JTextField priceCents;
	
	/* The text field where the number of dollars in a cost is specified */
	private JTextField costDollars;
	
	/* The text field where the number of cents in a cost is specified */
	private JTextField costCents;
	
	/* The dropdown menu displaying different product categories*/
	private JComboBox categoriesBox;
	
	
	/* Dialog control buttons */
	private JButton cancelButton;
	private JButton saveButton;


	/**
	 * Class constructor.
	 * @param controller the MVC framework controller to be passed in
	 * @param parent the parent container
	 * @param title the title of the dialog
	 * @param categories the product categories
	 * @param updateMode whether update mode is on
	 */
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
	
	/**
	 * Sets the fields to the values specified.
	 * @param name the product name to be set
	 * @param price the price in cents of the product
	 * @param cost the cost in cents of the product
	 * @param category the product category to which it belongs
	 */
	public void populateFields(String name, int price, int cost, Category category) {
			nameField.setText(name);
			categoriesBox.setSelectedItem(category);
			String priceString = Integer.toString(price);
			setMoneyFields(priceDollars, priceCents, priceString);
			String costString = Integer.toString(cost);
			setMoneyFields(costDollars, costCents, costString);

	}
	
	/**
	 * Processes the string representation of money in cents into a dollars string and a cents
	 * string and sets these values in the view.
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
	 * Draws the JPanel that holds the name field
	 */
	private void drawNamePane() {
		JPanel productNamePane = new JPanel();
		this.add(productNamePane, BorderLayout.NORTH);

		productNamePane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JLabel nameLabel = new JLabel("Product Name:");
		productNamePane.add(nameLabel);

		nameField = new JTextField(12);
		productNamePane.add(nameField);
	}

	
	/**
	 * Draws the JPanel that holds the control buttons
	 */
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

	
	/**
	 * Draws the pane that displays price/cost and category entry fields/dropdowns.
	 * @param categories
	 */
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

	
	/**
	 * Handles all ActionEvents by triggering the appropriate response by the controller.
	 */
	@Override
	public void actionPerformed(ActionEvent event) {
		Object src = event.getSource();
		if (src == saveButton) {
			if (!updateMode) {
			controller.createNewProduct(nameField.getText(), priceDollars.getText(), priceCents.getText(), 
					costDollars.getText(), costCents.getText(), (Category)categoriesBox.getSelectedItem());
			} else {
				controller.updateProduct(nameField.getText(), priceDollars.getText(), priceCents.getText(),
						costDollars.getText(), costCents.getText(), (Category)categoriesBox.getSelectedItem());
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




