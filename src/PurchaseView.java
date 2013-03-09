
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class PurchaseView implements ActionListener, SHPRCConstants {
	private PurchaseController controller;
	private ArrayList<Product> productList;
	private Vector<Affiliation> affiliations;
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
	private JPanel purchaseButtonPanel;
	private JPanel totalLabelBox;
	private JPanel totalPriceBox;
	private NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);


	public PurchaseView(PurchaseController controller, ArrayList<Product> productList, Vector<Affiliation> affiliations) {
		this.controller = controller;
		this.productList = productList;
		this.affiliations = affiliations;
		drawComponents();
		frame.setVisible(true);

	}

	private void drawComponents() {
		frame = new JFrame("SHPRC Point-of-Sale");
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setSize(new Dimension(820, 600));
		frame.setResizable(false);
		frame.setLayout(null);

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
		frame.add(adminViewButton);

		voidButton = new JButton("Void");
		voidButton.setFont(new Font("Helvetica", Font.BOLD, 13));
		voidButton.setBounds(205, 528, 130, 31);
		frame.add(voidButton);
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
		purchaseButtonPanel = new JPanel();
		purchaseButtonPanel.setBounds(480, 150, 320, 31);
		purchaseButtonPanel.setLayout(new GridLayout(1, 3, 2, 0) );
		drawPurchaseModifierButtons(purchaseButtonPanel);
		frame.add(purchaseButtonPanel);
		
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
		totalLabelBox = new JPanel();
		totalLabelBox.setLayout(new BoxLayout(totalLabelBox, BoxLayout.Y_AXIS));
		totalPriceBox = new JPanel();
		totalPriceBox.setLayout(new BoxLayout(totalPriceBox, BoxLayout.Y_AXIS));
		totalPanel.add(totalLabelBox, BorderLayout.WEST);
		totalPanel.add(totalPriceBox, BorderLayout.EAST);
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

		affiliationComboBox = new JComboBox(affiliations);
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
		quantityButton = new JButton("CHANGE QTY");
		quantityButton.setBorder(new LineBorder(LIGHT_GREY));
		quantityButton.setFont(new Font("Helvetica", Font.BOLD, 12));
		quantityButton.addActionListener(this);
		quantityButton.setEnabled(false);
		panel.add(quantityButton);

		deleteButton = new JButton("DELETE ITEM");
		deleteButton.setBorder(new LineBorder(LIGHT_GREY));
		deleteButton.setFont(new Font("Helvetica", Font.BOLD, 12));
		deleteButton.addActionListener(this);
		deleteButton.setEnabled(false);
		panel.add(deleteButton);

		clearButton = new JButton("CLEAR ALL");
		clearButton.setBorder(new LineBorder(LIGHT_GREY));
		clearButton.setFont(new Font("Helvetica", Font.BOLD, 12));
		clearButton.addActionListener(this);
		panel.add(clearButton);
	}

	public void inputError (String errorMessage) {
		JOptionPane.showMessageDialog(frame, errorMessage, "Input Error", JOptionPane.ERROR_MESSAGE);
	}

	public void actionPerformed(ActionEvent event) {
		Object src = event.getSource();
		if (src == enterButton) {
			String suid = (String) textField.getText();
			Affiliation affiliation = (Affiliation) affiliationComboBox.getSelectedItem();
			controller.setClient(suid, affiliation.getAffiliationID());

		} else if (src instanceof ProductJButton) {
			int productID = ((ProductJButton) src).getProductID();
			controller.addProduct(productID);
			
		} else if (src instanceof LabelAppearanceJButton) {
			controller.selectProductToModify((LabelAppearanceJButton) src);
			
		} else if (src == deleteButton) {
			controller.deleteProduct();
			controller.selectProductToModify(null);
			
		} else if (src == quantityButton) {
			controller.changeQuantity();
			controller.selectProductToModify(null);
			
		} else if (src == clearButton) {
			controller.clear();
		}
	}
	
	public void enableModifierButtons(boolean state) {
		quantityButton.setEnabled(state);
		deleteButton.setEnabled(state);
	}
	
	private void clearContainers() {
		priceListBox.removeAll();
		productListBox.removeAll();
		totalLabelBox.removeAll();
		totalPriceBox.removeAll();
	}

	public void displayPurchase (HashMap<Product, Integer> purchaseProducts, int[] totals) {
		controller.selectProductToModify(null);
		enableModifierButtons(false);
		clearContainers();
		ArrayList<LabelAppearanceJButton> buttons = new ArrayList<LabelAppearanceJButton>();
		purchaseList.repaint();
		Iterator it = purchaseProducts.entrySet().iterator();
		for (int i = 0; it.hasNext(); i++) {
			Map.Entry<Product, Integer> pair = (Entry<Product, Integer>) it.next();
			Product product = pair.getKey();
			String productName = product.getName();
			int productPrice = product.getPrice();
			int productID = product.getProductID();
			int quantity = pair.getValue();
			String totalPriceString = currencyFormat.format(productPrice*quantity/100.0);

			LabelAppearanceJButton productButton = new LabelAppearanceJButton(productName, productID);
			productButton.addActionListener(this);
			buttons.add(productButton);
			productListBox.add(productButton);

			HelveticaJLabel totalPriceLabel = new HelveticaJLabel(totalPriceString, 16);
			totalPriceLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
			priceListBox.add(totalPriceLabel);

			if (quantity > 1) {
				String priceString = currencyFormat.format(productPrice/100.0);
				String str = quantity + " @ " + priceString;
				JLabel quantityLabel = new HelveticaJLabel("      " + str, 14);
				productListBox.add(quantityLabel);
				JLabel dummyLabel = new HelveticaJLabel(" ", 14); //creates an invisible JLabel to maintain spacing
				dummyLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
				priceListBox.add(dummyLabel);
			} 

		}

		
		totalLabelBox.add(new HelveticaJLabel("SUBTOTAL", 14));
		totalLabelBox.add(new HelveticaJLabel("CREDIT APPLIED", 14));
		totalLabelBox.add(new HelveticaJLabel("PREGNANCY TEST SUBSIDY", 14));
		totalLabelBox.add(new HelveticaJLabel("TOTAL", 16));
		for (int i = SUBTOTAL; i < TOTAL; i++) {
			HelveticaJLabel label = new HelveticaJLabel(currencyFormat.format(totals[i]/100.0), 14);
			totalPriceBox.add(label);
			label.setAlignmentX(Component.RIGHT_ALIGNMENT);
		}
		HelveticaJLabel totalLabel = new HelveticaJLabel(currencyFormat.format(totals[TOTAL]/100.0), 16);
		totalLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
		totalPriceBox.add(totalLabel);
		validateBoxes();

	}
	
	private void validateBoxes() {
		totalPriceBox.validate();
		purchaseList.validate();
		totalLabelBox.validate();
		productListBox.validate();
		priceListBox.validate();
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
	
	public int confirmDecision(String str) {
		return JOptionPane.showConfirmDialog(frame, str, "Consent Is Active", JOptionPane.YES_NO_OPTION);
	}

	public void highlightProductToModify(LabelAppearanceJButton button) {
		button.setHighlight();
	}

}
