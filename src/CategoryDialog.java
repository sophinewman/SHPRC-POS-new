import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;


public class CategoryDialog extends JDialog implements ActionListener {
	private JTextField nameField;
	private JButton cancelButton;
	private JButton saveButton;
	private DialogController controller;
	private boolean updateMode = false;

	

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
	
	public void populateFields(String name) {
			nameField.setText(name);

	}
	
	private void drawNamePane() {
		JPanel productNamePane = new JPanel();
		getContentPane().add(productNamePane, BorderLayout.NORTH);

		productNamePane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JLabel nameLabel = new JLabel("Category Name: ");
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


	
	public void close() {
		this.dispose();
	}


	public void inputError (String errorMessage) {
		JOptionPane.showMessageDialog(this, errorMessage, "Input Error", JOptionPane.ERROR_MESSAGE);
	}
	

}




