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

public class AdminDialog extends JDialog implements SHPRCConstants, ActionListener, ListSelectionListener {

	private DialogController dialogController;
	private JPanel selectedPane;
	private JList infoList;
	private JButton addButton;
	private JButton updateButton;
	private JButton deleteButton;
	private JButton backButton;
	private String task;


	public AdminDialog (DialogController dialogController, JFrame frame, String task, ArrayList<Affiliation> affiliationList, ArrayList<Product> productList, ArrayList<Category> categoryList) {
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

	private JPanel drawContentPane(String task, Object[] objects) {
		JPanel productPane;
		productPane = new JPanel();
		productPane.setLayout(new BorderLayout(0, 0));

		productPane.add(drawTitlePanel(task), BorderLayout.NORTH);

		productPane.add(drawOptionButtons(), BorderLayout.SOUTH);

		productPane.add(drawListPane(objects), BorderLayout.CENTER);

		return productPane;
	}


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

		drawInfoList(listPane, objects);

		listPane.add(new JLabel("    "), BorderLayout.WEST);

		listPane.add(new JLabel("    "), BorderLayout.EAST);
		return listPane;
	}

	private void drawInfoList(JPanel listPane, Object[] objects) {
		infoList = new JList(objects);
		JScrollPane scrollPane = new JScrollPane(infoList);
		infoList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		infoList.addListSelectionListener(this);
		listPane.add(scrollPane, BorderLayout.CENTER);
	}

	private JPanel drawOptionButtons() {
		JPanel optionButtonPanel = new JPanel();



		backButton = new JButton("Back");
		backButton.addActionListener(this);
		optionButtonPanel.add(backButton);



		return optionButtonPanel;

	}


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

	public void close() {
		this.dispose();
	}

	public void valueChanged(ListSelectionEvent event) {
		dialogController.itemSelected(infoList.getSelectedValue());
	}

	public void enableUpdateAndDelete() {
		updateButton.setEnabled(true);
		deleteButton.setEnabled(true);
	}
	
	public int confirmDecision(String str) {
		return JOptionPane.showConfirmDialog(this, str, "Consent Is Active", JOptionPane.YES_NO_OPTION);
	}




}
