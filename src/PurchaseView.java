
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class PurchaseView implements ActionListener, SHPRCConstants {
	private PurchaseController controller;
	private ArrayList<Product> productList;
	private JFrame frame;
	private JPanel buttonPanel;
	private JTextField textField;
	private JComboBox affiliationComboBox;
	private JButton enterButton;
	private JButton quantityButton;
	private JButton clearButton;
	private JButton deleteButton;
	private JButton submitButton;
	private JButton adminViewButton;
	private JButton voidButton;
	private JPanel purchaseList; 
	private JPanel productListBox;
	private JPanel priceListBox;
	private JPanel totalPanel;



	public PurchaseView(PurchaseController controller, ArrayList<Product> productList) {
		this.controller = controller;
		this.productList = productList;
		drawComponents();
		frame.setVisible(true);

	}

	private void drawComponents() {
		frame = new JFrame("SHPRC Point-of-Sale");
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setSize(new Dimension(820, 600));
		frame.setResizable(false);
		frame.getContentPane().setLayout(null);

		buttonPanel = new JPanel();
		buttonPanel.setLocation(20, 20);
		buttonPanel.setSize(new Dimension(440, 440));
		buttonPanel.setLayout(new GridLayout(0, 4, 2, 2));
		drawProductButtons();
		frame.add(buttonPanel);

		drawClientComponents();

		drawPurchaseComponents();

		drawLeftButtons();
		
		frame.validate();
	}

	private void drawLeftButtons() {
		adminViewButton = new JButton("Administrator View");
		adminViewButton.setFont(new Font("Helvetica", Font.BOLD, 13));
		adminViewButton.setBounds(20, 528, 175, 31);
		frame.getContentPane().add(adminViewButton);

		voidButton = new JButton("Void");
		voidButton.setFont(new Font("Helvetica", Font.BOLD, 13));
		voidButton.setBounds(205, 528, 130, 31);
		frame.getContentPane().add(voidButton);
	}
	
	
	private void drawProductButtons() {
		NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
		for (Product p : productList) {
			ProductJButton pjb = new ProductJButton("<html><center>"+currencyFormat.format(p.getPrice()/100.0)+
					"<br>"+ p.getName()+"</center></html>", p.getProductID());
			pjb.addActionListener(this);
			buttonPanel.add(pjb);
		}
	}
	
	private void drawPurchaseComponents() {
		JPanel purchaseButtonPanel = new JPanel();
		purchaseButtonPanel.setBounds(480, 150, 320, 31);
		purchaseButtonPanel.setLayout(null);
		frame.add(purchaseButtonPanel);
		drawPurchaseModifierButtons(purchaseButtonPanel);

		purchaseList = new JPanel();
		purchaseList.setBounds(480, 187, 320, 336);
		frame.add(purchaseList);
		purchaseList.setBorder(BorderFactory.createLineBorder(LIGHT_GREY));
		purchaseList.setLayout(new BorderLayout());
		purchaseList.setVisible(true);

		productListBox = new JPanel();
		productListBox.setLayout(new BoxLayout(productListBox, BoxLayout.Y_AXIS));
		purchaseList.add(productListBox, BorderLayout.WEST);


		priceListBox = new JPanel();
		priceListBox.setLayout(new BoxLayout(priceListBox, BoxLayout.Y_AXIS));
		purchaseList.add(priceListBox, BorderLayout.EAST);

		totalPanel = new JPanel();
		totalPanel.setLayout(new BorderLayout());
		purchaseList.add(totalPanel, BorderLayout.SOUTH);

		HelveticaJLabel purchaseLabel = new HelveticaJLabel("PURCHASE", 18);
		purchaseLabel.setBorder(BorderFactory.createEmptyBorder(6, 1, 3, 1));
		purchaseLabel.setBounds(0, 10, 320, 19);
		purchaseLabel.setHorizontalAlignment(SwingConstants.CENTER);
		purchaseList.add(purchaseLabel, BorderLayout.NORTH);

		submitButton = new JButton("SUBMIT");
		submitButton.setFont(new Font("Helvetica", Font.BOLD, 13));
		submitButton.addActionListener(this);
		
		submitButton.setBounds(480, 528, 320, 31);
		frame.add(submitButton);
	}

	private void drawClientComponents() {
		JPanel clientPanel = new JPanel();
		clientPanel.setLayout(null);
		clientPanel.setBorder(new LineBorder(LIGHT_GREY));
		clientPanel.setBounds(480, 20, 320, 123);
		
		HelveticaJLabel clientInfoLabel = new HelveticaJLabel("CLIENT INFO", 16);

		clientInfoLabel.setBounds(89, 10, 141, 16);
		clientInfoLabel.setHorizontalAlignment(SwingConstants.CENTER);
		clientPanel.add(clientInfoLabel);

		HelveticaJLabel suidLabel = new HelveticaJLabel("SUID", 14);
		suidLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		suidLabel.setBounds(19, 46, 61, 16);
		clientPanel.add(suidLabel);

		HelveticaJLabel affiliationLabel = new HelveticaJLabel("Affiliation", 14);
		affiliationLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		affiliationLabel.setBounds(0, 82, 80, 16);
		clientPanel.add(affiliationLabel);

		textField = new JTextField(10);
		textField.setBounds(90, 40, 120, 27);
		textField.setFont(new Font("Helvetica", Font.PLAIN, 12));
		clientPanel.add(textField);

		affiliationComboBox = new JComboBox(AFFILIATIONS);
		affiliationComboBox.setBounds(90, 78, 120, 27);
		affiliationComboBox.setFont(new Font("Helvetica", Font.PLAIN, 12));
		clientPanel.add(affiliationComboBox);

		enterButton = new JButton("ENTER");
		enterButton.setFont(new Font("Helvetica", Font.BOLD, 12));
		enterButton.setBounds(219, 56, 80, 29);
		enterButton.addActionListener(this);
		clientPanel.add(enterButton);
		
		frame.add(clientPanel);
	}

	private void drawPurchaseModifierButtons (JPanel panel) {
		quantityButton = new JButton("QUANTITY");
		quantityButton.setFont(new Font("Helvetica", Font.BOLD, 12));
		quantityButton.setBounds(0, 0, 103, 31);
		panel.add(quantityButton);

		deleteButton = new JButton("DELETE");
		deleteButton.setFont(new Font("Helvetica", Font.BOLD, 12));
		deleteButton.setBounds(109, 0, 103, 31);
		deleteButton.addActionListener(this);
		panel.add(deleteButton);

		clearButton = new JButton("CLEAR");
		clearButton.setFont(new Font("Helvetica", Font.BOLD, 12));
		clearButton.setBounds(217, 0, 103, 31);
		panel.add(clearButton);
	}

	public void inputError (String errorMessage) {
		JOptionPane.showMessageDialog(frame, errorMessage, "Input Error", JOptionPane.ERROR_MESSAGE);
	}

	public void actionPerformed(ActionEvent event) {
		Object src = event.getSource();
		if (src == enterButton) {
			String suid = (String) textField.getText();
			String affiliation = (String) affiliationComboBox.getSelectedItem();
			controller.setClient(suid, affiliation);

		} else if (src instanceof ProductJButton) {
			int productID = ((ProductJButton) src).getProductID();
			controller.addProduct(productID);
		} else if (src == deleteButton) {
			controller.highlightPurchaseProducts();
		}
	}

	public void displayPurchase (HashMap<Product, Integer> purchaseProducts, int total) {
		priceListBox.removeAll();
		productListBox.removeAll();
		totalPanel.removeAll();
		purchaseList.repaint();
		ArrayList<LabelAppearanceJButton> buttons = new ArrayList<LabelAppearanceJButton>();
		NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
		Iterator it = purchaseProducts.entrySet().iterator();
		for (int i = 0; it.hasNext(); i++) {
			Map.Entry<Product, Integer> pair = (Entry<Product, Integer>) it.next();
			Product product = pair.getKey();
			String productName = product.getName();
			int productPrice = product.getPrice();
			int quantity = pair.getValue();
			String totalPriceString = currencyFormat.format(productPrice*quantity/100.0);

			LabelAppearanceJButton productButton = new LabelAppearanceJButton(productName);
			productButton.addActionListener(this);
			buttons.add(productButton);
			productListBox.add(productButton);

			HelveticaJLabel totalPriceLabel = new HelveticaJLabel(totalPriceString, 16);
			priceListBox.add(totalPriceLabel);

			if (quantity > 1) {
				String priceString = currencyFormat.format(productPrice/100.0);
				String str = quantity + " @ " + priceString;
				JLabel quantityLabel = new HelveticaJLabel("      " + str, 14);
				productListBox.add(quantityLabel);
				JLabel dummyLabel = new HelveticaJLabel(" ", 14); //creates an invisible JLabel to maintain spacing
				priceListBox.add(dummyLabel);
			} 

		}
		productListBox.validate();
		priceListBox.validate();
		totalPanel.add(new HelveticaJLabel("TOTAL", 16), BorderLayout.WEST);
		HelveticaJLabel totalLabel = new HelveticaJLabel(currencyFormat.format(total/100.0), 16);
		totalPanel.add(totalLabel, BorderLayout.EAST);
		purchaseList.validate();
	}

	public int getQuantity() {
		String inputValue = JOptionPane.showInputDialog(frame, "Specify a quantity: ");
		if (inputValue == null) {
			return 0;
		}
		try {
			int quantity = Integer.parseInt(inputValue);
			return quantity;
		}
		catch (NumberFormatException e) {
			return INVALID_NUMBER_INPUT;
		}
	}

	public void highlightProducts() {
		for (Component c: productListBox.getComponents()) {
			if (c instanceof LabelAppearanceJButton) {
				((LabelAppearanceJButton) c).setContentAreaFilled(true);
			}
		}
	}
	
}
