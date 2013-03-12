import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.Font;

public class AdminDialog {
	
	JDialog dialog;
	private JPanel selectedPane;
	private JList productList;
	
	private JButton saveButton;
	
	private String[] dummyList = {"hello", "hi", "greetings", "salutations", "sup","hello", "hi", "greetings",
			"salutations","sup","hello", "hi", "greetings", "salutations", "sup", "greetings", "salutations"};
	
	private JPanel drawProductPane() {
		JPanel productPane;
		productPane = new JPanel();
		productPane.setLayout(new BorderLayout(0, 0));
		
		productPane.add(drawTitlePanel("All Products"), BorderLayout.NORTH);
		
		productPane.add(drawOptionButtons(), BorderLayout.SOUTH);

		
		
		JPanel listPane = new JPanel();
		productPane.add(listPane, BorderLayout.CENTER);
		listPane.setLayout(new BorderLayout(0, 0));
		
		JPanel listButtonPane = new JPanel();
		listPane.add(listButtonPane, BorderLayout.SOUTH);
		listButtonPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JButton button_1 = new JButton("Update");
		button_1.setFont(new Font("Helvetica", Font.PLAIN, 11));
		listButtonPane.add(button_1);
		
		JButton btnAddNewProduct = new JButton("Add New Product");
		btnAddNewProduct.setFont(new Font("Helvetica", Font.PLAIN, 11));
		listButtonPane.add(btnAddNewProduct);
		
		JList productList = new JList(dummyList);
		JScrollPane scrollPane = new JScrollPane(productList);
		productList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listPane.add(scrollPane, BorderLayout.CENTER);
		
		listPane.add(new JLabel("    "), BorderLayout.WEST);
		
		listPane.add(new JLabel("    "), BorderLayout.EAST);
		
		return productPane;
	}
	
	private JPanel drawOptionButtons() {
		JPanel optionButtonPanel = new JPanel();

		optionButtonPanel.setAlignmentX(Component.RIGHT_ALIGNMENT);
		optionButtonPanel.setLayout(new BorderLayout(0, 0));
		
		Box verticalBox = Box.createVerticalBox();
		optionButtonPanel.add(verticalBox, BorderLayout.SOUTH);
		
		verticalBox.add(new JLabel("  "));
		
		Box horizontalBox = Box.createHorizontalBox();
		horizontalBox.setAlignmentX(Component.RIGHT_ALIGNMENT);
		
		verticalBox.add(horizontalBox);
		
		JButton button = new JButton("Cancel");
		horizontalBox.add(button);
		

		horizontalBox.add(new JLabel("  "));
		
		saveButton = new JButton("Save Changes");
		horizontalBox.add(saveButton);
		
		verticalBox.add(new JLabel("  "));
		
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
	
	public AdminDialog(String str, JFrame frame) {
		dialog = new JDialog(frame, "Hello");
		dialog.setContentPane(drawProductPane());
	
	}

}
