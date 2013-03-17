import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;



public class HealthyView extends JDialog {
	private JTextField textField;
	private JTextField dollars;
	private JTextField cents;
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setVisible(true);
		HealthyView hv = new HealthyView(frame, "Add Product");
		hv.setVisible(true);
	}
	public HealthyView (JFrame parent, String title) {
		super(parent, title);
		
		Category[] categories = {new Category(5, "Barrier Methods")};
		
		this.setLayout(new BorderLayout());
		
		JPanel productNamePane = new JPanel();
		this.add(productNamePane, BorderLayout.NORTH);
		
		productNamePane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JLabel nameLabel = new JLabel("Product Name:");
		productNamePane.add(nameLabel);
		
		textField = new JTextField();
		productNamePane.add(textField);
		textField.setColumns(12);
		
		JPanel categoryAndPricePane = new JPanel();
		this.add(categoryAndPricePane, BorderLayout.CENTER);
		categoryAndPricePane.setLayout(new FlowLayout(FlowLayout.CENTER, 2, 5));
		
		JLabel lblCategory = new JLabel("Category:");
		categoryAndPricePane.add(lblCategory);
		
		JComboBox categoriesBox = new JComboBox(categories);
		categoryAndPricePane.add(categoriesBox);
		
		JLabel lblPrice = new JLabel("Price:");
		categoryAndPricePane.add(lblPrice);
		
		dollars = new JTextField();
		categoryAndPricePane.add(dollars);
		dollars.setText("00");
		dollars.setColumns(2);
		
		JLabel label = new JLabel(".");
		categoryAndPricePane.add(label);
		
		cents = new JTextField();
		categoryAndPricePane.add(cents);
		cents.setText("00");
		cents.setColumns(2);
		
		JPanel controlButtonPane = new JPanel();
		this.add(controlButtonPane, BorderLayout.SOUTH);
		controlButtonPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JButton btnCancel = new JButton("Cancel");
		controlButtonPane.add(btnCancel);
		
		controlButtonPane.add(new JLabel("   "));
		
		JButton btnAdd = new JButton("Add");
		controlButtonPane.add(btnAdd);
		
		this.pack();
		
	}
}


