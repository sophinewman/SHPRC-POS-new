import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;


public class CategoryDialog extends JDialog implements ActionListener {
	
	/* The MVC controller that connects the view to the back end */
	private DialogController controller;
	
	/* Whether the user is updating an affiliation or adding a new one */
	private boolean updateMode = false;
	
	/* The text field where the category's name is specified */
	private JTextField nameField;
	
	/* Dialog control buttons */
	private JButton cancelButton;
	private JButton saveButton;
	

	/**
	 * Class constructor
	 * @param controller the MVC framework controller to be passed in
	 * @param parent the parent container
	 * @param title the title of the dialog
	 * @param updateMode whether update mode is on
	 */
	public CategoryDialog (DialogController controller, JDialog parent, String title, boolean updateMode) {
		super(parent, title);

		this.controller = controller;
		
		this.updateMode = updateMode;

		getContentPane().setLayout(new BorderLayout(5,5));

		drawNamePane();
		drawControlButtonPane();

		this.pack();
		this.setLocationRelativeTo(parent);
		
	}
	
	/**
	 * Sets the fields to the values specified.
	 * @param name the category name to be set
	 */
	public void populateFields(String name) {
			nameField.setText(name);
	}
	
	
	/**
	 * Draws the JPanel that holds the name field
	 */
	private void drawNamePane() {
		JPanel productNamePane = new JPanel();
		getContentPane().add(productNamePane, BorderLayout.NORTH);

		productNamePane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JLabel nameLabel = new JLabel("Category Name: ");
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
	 * Handles all ActionEvents by triggering the appropriate response by the controller.
	 */
	@Override
	public void actionPerformed(ActionEvent event) {
		Object src = event.getSource();
		if (src == saveButton) {
			if (!updateMode) {
				controller.createNewCategory(nameField.getText());
			} else {
				controller.updateCategory(nameField.getText());
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




