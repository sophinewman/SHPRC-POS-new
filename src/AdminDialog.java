import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.ArrayList;

/**
 * SHPRC-POS
 * AdminDialog.java
 * Displays administrative tools for changing the back end.
 * 
 * @author Sophi Newman
 * @version 1.0 03/17/2013
 */

public class AdminDialog extends JDialog implements SHPRCConstants, ActionListener, ListSelectionListener {

	/* The DialogController object that relays information between the back end (a RuntimeDatabase) and the AdminDialog. */
	private DialogController dialogController;
	
	/* The String that specifies which administrative task will be carried out. Must be equal to one of three constants
	 * specified in SHPRCConstants, MANAGE_CATEGORIES, MANAGE_PRODUCTS, or MANAGE_AFFILIATIONS_AND_CREDIT */
	private String task;
	
	/* The JPanel to be displayed, determined and populated by which administrative task is specified. */
	private JPanel selectedPane; 
	
	/* The JList of Affiliation, Category, or Product objects displayed by the program. */
	private JList objectList;
	
	private JButton addButton; // The JButton that triggers adding an object to the back end.
	private JButton updateButton; // The JButton that triggers updating an object in the back end.
	private JButton deleteButton; // The JButton that triggers deleting an object in the back end.
	
	private JButton backButton; // The JButton that triggers closing the dialog and returning.
	

	/**
	 * Class constructor.
	 * @param dialogController the MVC framework controller to be passed in
	 * @param frame the parent Swing Container
	 * @param task the admin task to be carried out
	 * @param affiliationList all of the affiliations in the back end
	 * @param productList all of the products in the back end
	 * @param categoryList all of the categories in the back end
	 */
	public AdminDialog (DialogController dialogController, JFrame frame, String task, ArrayList<Affiliation> affiliationList, 
							ArrayList<Product> productList, ArrayList<Category> categoryList) {
		super(frame, task, false); // creates a JDialog whose parent is the frame, title is the task, and _is not_ modal
		this.task = task;
		this.dialogController = dialogController;
		setPreferredSize(new Dimension(400, 300));
		if (task == MANAGE_PRODUCTS) {
			selectedPane = drawContentPane(task, productList.toArray());
		} else if (task == MANAGE_CATEGORIES) {
			selectedPane = drawContentPane(task, categoryList.toArray());
		} else if (task == MANAGE_AFFILIATIONS_AND_CREDIT) {
			selectedPane = drawContentPane(task, affiliationList.toArray());
		}
		this.setContentPane(selectedPane);
		this.pack();
		this.setLocationRelativeTo(frame);
		this.setVisible(true);

	}

	
	/**
	 * Draws the main components of the dialog.
	 * @param task the admin task to be carried out
	 * @param objects all objects of the type corresponding to the task
	 * @return a JPanel populated with content
	 */
	private JPanel drawContentPane(String task, Object[] objects) {
		JPanel productPane;
		productPane = new JPanel();
		productPane.setLayout(new BorderLayout(0, 0));

		productPane.add(drawTitlePanel(task), BorderLayout.NORTH);

		productPane.add(drawOptionButtons(), BorderLayout.SOUTH);

		productPane.add(drawListPane(objects), BorderLayout.CENTER);

		return productPane;
	}


	/**
	 * Draws a JPanel with a list of objects corresponding to a task and control buttons.
	 * @param objects all objects of the type corresponding to the task
	 * @return a JPanel populated with the object JList and control JButtons
	 */
	private JPanel drawListPane (Object[] objects) {
		JPanel listPane = new JPanel();

		listPane.setLayout(new BorderLayout(0, 0));

		JPanel listButtonPane = new JPanel();
		listPane.add(listButtonPane, BorderLayout.SOUTH);
		listButtonPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		updateButton = new JButton("Update");
		updateButton.setFont(new Font("Helvetica", Font.PLAIN, 11));
		updateButton.setEnabled(false);
		updateButton.addActionListener(this);
		listButtonPane.add(updateButton);
		
		deleteButton = new JButton("Delete");
		deleteButton.setFont(new Font("Helvetica", Font.PLAIN, 11));
		deleteButton.setEnabled(false);
		deleteButton.addActionListener(this);
		listButtonPane.add(deleteButton);

		addButton = new JButton("Add New");
		addButton.setFont(new Font("Helvetica", Font.PLAIN, 11));
		addButton.addActionListener(this);
		listButtonPane.add(addButton);

		drawObjectList(listPane, objects);

		listPane.add(new JLabel("    "), BorderLayout.WEST);

		listPane.add(new JLabel("    "), BorderLayout.EAST);
		return listPane;
	}

	
	/**
	 * Draws the JList based on the given objects.
	 * @param listPane the parent container
	 * @param objects all objects of the type corresponding to the task
	 */
	private void drawObjectList(JPanel listPane, Object[] objects) {
		objectList = new JList(objects);
		JScrollPane scrollPane = new JScrollPane(objectList);
		objectList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		objectList.addListSelectionListener(this);
		listPane.add(scrollPane, BorderLayout.CENTER);
	}

	
	/**
	 * Draws the control option buttons in a JPanel.
	 * @return the JPanel with buttons
	 */
	private JPanel drawOptionButtons() {
		JPanel optionButtonPanel = new JPanel();

		backButton = new JButton("Back");
		backButton.addActionListener(this);
		optionButtonPanel.add(backButton);

		return optionButtonPanel;

	}


	/**
	 * Populates a JPanel with the specified title.
	 * @param title the title displayed above the object JList
	 * @return a JPanel dispalying the specified title
	 */
	private JPanel drawTitlePanel(String title) {
		JPanel titlePanel = new JPanel();

		titlePanel.setLayout(new BorderLayout(0, 0));

		JLabel titleLabel = new JLabel(title);
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

		titlePanel.add(titleLabel, BorderLayout.CENTER);
		titlePanel.add(new JLabel("  "), BorderLayout.NORTH);
		titlePanel.add(new JLabel("  "), BorderLayout.SOUTH);

		return titlePanel;
	}

	
	/**
	 * Handles all ActionEvents by triggering the appropriate response by the controller.
	 */
	@Override
	public void actionPerformed(ActionEvent event) {
		Object src = event.getSource();
		if (src == addButton) {
			if (task == MANAGE_PRODUCTS) {
				dialogController.addProduct();

			} else if (task == MANAGE_CATEGORIES) {
				dialogController.addCategory();

			} else if (task == MANAGE_AFFILIATIONS_AND_CREDIT) {
				dialogController.addAffiliation();
			}

		} else if (src == updateButton) {
			if (task == MANAGE_PRODUCTS) {
				dialogController.updateProduct();

			} else if (task == MANAGE_CATEGORIES) {
				dialogController.updateCategory();

			} else if (task == MANAGE_AFFILIATIONS_AND_CREDIT) {
				dialogController.updateAffiliation();
			}
		} else if (src == deleteButton) {
			if (task == MANAGE_PRODUCTS) {
				dialogController.deleteProduct();

			} else if (task == MANAGE_CATEGORIES) {
				dialogController.deleteCategory();

			} else if (task == MANAGE_AFFILIATIONS_AND_CREDIT) {
				dialogController.deleteAffiliation();
			}

		} else if (src == backButton) {
			dialogController.exit();

		}
	}

	
	/**
	 * Handles the event generated by an item in the JList being selected. 
	 * Triggers appropriate response by the controller.
	 */
	@Override
	public void valueChanged(ListSelectionEvent event) {
		dialogController.itemSelected(objectList.getSelectedValue());
	}


	/**
	 * Closes the dialog.
	 */
	public void close() {
		this.dispose();
	}

	
	/**
	 * Enables the update and delete functions.
	 */
	public void enableUpdateAndDelete() {
		updateButton.setEnabled(true);
		deleteButton.setEnabled(true);
	}
	
	
	/**
	 * Displays a JOptionPane confirming a user's action.
	 * @param messsage the message to be displayed
	 * @return the JOptionPane constant corresponding to user's decision
	 */
	public int confirmDecision(String message) {
		return JOptionPane.showConfirmDialog(this, message, "Consent Is Active", JOptionPane.YES_NO_OPTION);
	}

}
