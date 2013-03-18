
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ArrayList;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.LineBorder;
/**
 * SHPRC-POS
 * PurchaseView.java
 * Serves as the view in the MVC framework and displays products to be added to a purchase and the products in a purchase.
 * 
 * @author Sophi Newman
 * @version 1.0 03/17/2013
 */

public class PurchaseView implements ActionListener, SHPRCConstants {
	
	/* The controller that connects the PurchaseView with a PurchaseModel */
	private PurchaseController controller;

	private JFrame frame; // The main frame
	private CardLayout cardLayout = new CardLayout(); // The CardLayout that contains the purchase and admin views
	
	private JPanel adminPane; // Displays the administrative view
	private JPanel adminButtonPane; // Contains the administrative buttons
	private HelveticaJButton purchaseViewButton; // Triggers shift to purchase view
	private HelveticaJButton manageProductsButton; // Triggers product admin launch
	private HelveticaJButton manageAffiliationsButton; // Triggers affiliation admin launch
	private HelveticaJButton manageCategoriesButton; // Triggers category admin launch
	
	private JPanel purchasePane; // Displays the purchase view
	private JPanel productButtonPane; // Contains the product selection buttons
	private JTextField suidField; // Allows for entering a client's SUID
	private JComboBox affiliationComboBox; // Allows for selecting an affiliation
	private HelveticaJButton enterClientButton; // Triggers cilent setting
	
	private HelveticaJButton adminViewButton; // Triggers shift to admin view
	private HelveticaJButton voidButton; // Triggers purchase void
	
	private JPanel purchaseList; // Displays the purchase product list
	private JPanel productListBox; // Vertical container for purchase products
	private JPanel priceListBox; // Vertical container for purchase product prices
	private JPanel totalPane; // Displays the purchase totals
	private JPanel totalLabelBox; // Vertical container for purchase total labels
	private JPanel totalPriceBox; // Vertical container for price labels
	
	private JPanel purchaseButtonPane; // Displays the buttons for purchase updates
	private HelveticaJButton quantityButton; //  Triggers product quantity update
	private HelveticaJButton clearButton; // Triggers deletion of all purchase products
	private HelveticaJButton deleteButton; // Triggers product deletion
	
	private HelveticaJButton submitButton; // Triggers purchase submission
	
	
	/**
	 * Class constructor.
	 * @param controller the MVC framework controller to be passed in
	 * @param productList all available products for purchase
	 * @param affiliations all community affiliations
	 */
	public PurchaseView(PurchaseController controller, ArrayList<Product> productList, ArrayList<Affiliation> affiliations) {
		this.controller = controller;
		drawFrame();
		drawPurchasePane(productList, affiliations);
		drawAdminPane();
		frame.add(purchasePane, PURCHASE_PANE);
		frame.add(adminPane, ADMIN_PANE);
		frame.addWindowListener(new ClosingListener());
		frame.setVisible(true);
	}
	
	/**
	 * Verifies a user's decision.
	 * @param message the message to be displayed
	 * @return the decision result integer
	 */
	public int confirmDecision(String message) {
		return JOptionPane.showConfirmDialog(frame, message, "Consent Is Active", JOptionPane.YES_NO_OPTION);
	}

	
	/**
	 * Displays an error to the user.
	 * @param errorMessage the message to be displayed
	 */
	public void displayError (String errorMessage) {
		JOptionPane.showMessageDialog(purchasePane, errorMessage, "Input Error", JOptionPane.ERROR_MESSAGE);
	}

	
	/**
	 * Displays a purchase.
	 * @param purchaseProducts the products in the purchase
	 * @param totals the totals of the purchase
	 */
	public void displayPurchase (HashMap<Product, Integer> purchaseProducts, int[] totals) {
		controller.selectProductToModify(null);
		enableModifierButtons(false);
		clearContainers();
		ArrayList<PurchaseProductJButton> buttons = new ArrayList<PurchaseProductJButton>();
		purchaseList.repaint();
		Iterator it = purchaseProducts.entrySet().iterator();
		for (int i = 0; it.hasNext(); i++) {
			Map.Entry<Product, Integer> pair = (Entry<Product, Integer>) it.next();
			Product product = pair.getKey();
			String productName = product.getName();
			int productPrice = product.getPrice();
			int productID = product.getProductID();
			int quantity = pair.getValue();
			String totalPriceString = CURRENCY_FORMAT.format(productPrice*quantity/100.0);
	
			PurchaseProductJButton productButton = new PurchaseProductJButton(productName, product);
			productButton.addActionListener(this);
			buttons.add(productButton);
			productListBox.add(productButton);
	
			HelveticaJLabel totalPriceLabel = new HelveticaJLabel(totalPriceString, 16);
			totalPriceLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
			priceListBox.add(totalPriceLabel);
	
			if (quantity > 1) {
				String priceString = CURRENCY_FORMAT.format(productPrice/100.0);
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
			HelveticaJLabel label = new HelveticaJLabel(CURRENCY_FORMAT.format(totals[i]/100.0), 14);
			totalPriceBox.add(label);
			label.setAlignmentX(Component.RIGHT_ALIGNMENT);
		}
		HelveticaJLabel totalLabel = new HelveticaJLabel(CURRENCY_FORMAT.format(totals[TOTAL]/100.0), 16);
		totalLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
		totalPriceBox.add(totalLabel);
		validateBoxes();
	
	}

	
	/**
	 * Shows a TotalDialog.
	 * @param total the string representation of a total
	 */
	public void displayTotalDialog(String total) {
		TotalDialog totalDialog = new TotalDialog(controller, frame, total);
		totalDialog.setVisible(true);
	}

	
	/**
	 * Toggles whether the product modifier buttons are active
	 * @param state whether to be enabled
	 */
	public void enableModifierButtons(boolean state) {
		quantityButton.setEnabled(state);
		deleteButton.setEnabled(state);
	}
	

	/**
	 * Asks the user to enter a quantity of a product to be added.
	 * @return the quantity to be added
	 */
	public int getQuantity() {
		String inputValue = JOptionPane.showInputDialog(purchasePane, "Specify a quantity: ");
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

	
	/**
	 * Returns the root JFrame.
	 * @return the root JFrame
	 */
	public JFrame getRootFrame() {
		return frame;
	}


	/**
	 * Switches the main view.
	 * @param view the view to be displayed
	 */
	public void switchView(String view) {
		cardLayout.show(frame.getContentPane(), view);
	}

	
	/**
	 * Resets the client fields.
	 */
	public void resetClientField() {
		suidField.setText("");
		affiliationComboBox.setSelectedIndex(0);
	}

	
	/**
	 * Extends WindowAdapter abstract adapter class to allow special handling on window closing.
	 * http://stackoverflow.com/questions/10470104/what-is-the-difference-between-listeners-and-adapters
	 */
	public class ClosingListener extends WindowAdapter {
		/**
		 * Triggers confirm exit action on window closing.
		 */
	    @Override
	    public void windowClosing(WindowEvent e) {
	        controller.confirmExit();
	    }
	}

	
	/**
	 * Closes the view window.
	 */
	public void closeWindow() {
		frame.dispose();
	}
	

	/**
	 * Highlights or de-highlights the given button
	 * @param button the button to be highlighted
	 * @param on whether to be highlighted
	 */
	public void highlightProductToModify(PurchaseProductJButton button, boolean on) {
		button.setHighlight(on);
	}

	
	/**
	 * Handles all ActionEvents by triggering the appropriate response by the controller.
	 */
	@Override
	public void actionPerformed(ActionEvent event) {
		Object src = event.getSource();
		
		if (src instanceof ProductJButton) {
			Product product = ((ProductJButton) src).getProduct();
			controller.addProduct(product);
			
		} else if (src instanceof PurchaseProductJButton) {
			controller.selectProductToModify((PurchaseProductJButton) src);
			
		} else if (src == enterClientButton) {
			String suid = suidField.getText();
			Affiliation affiliation = (Affiliation) affiliationComboBox.getSelectedItem();
			controller.setClient(suid, affiliation.getAffiliationID());
				
		} else if (src == deleteButton) {
			controller.deleteProduct();
			controller.selectProductToModify(null);
			
		} else if (src == quantityButton) {
			controller.changeQuantity();
			controller.selectProductToModify(null);
			
		} else if (src == clearButton) {
			controller.clear();
			
		} else if (src == submitButton) {
			controller.submitPurchase();
			
		} else if (src == adminViewButton) {
			controller.switchToAdmin();
			
		} else if (src == purchaseViewButton) {
			controller.switchToPurchase();
		
		} else if (src == manageProductsButton) {
			controller.adminDialog(MANAGE_PRODUCTS);
			
		} else if (src == manageAffiliationsButton) {
			controller.adminDialog(MANAGE_AFFILIATIONS_AND_CREDIT);
			
		} else if (src == manageCategoriesButton) {
			controller.adminDialog(MANAGE_CATEGORIES);
		}
	}

	
	/**
	 * Initializes the frame.
	 */
	private void drawFrame() {
		frame = new JFrame("SHPRC Point-of-Sale");
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setSize(new Dimension(820, 600));
		frame.setResizable(false);
		frame.setLayout(cardLayout);
	}

	
	/**
	 * Draws the administrative view pane.
	 */
	private void drawAdminPane() {
		adminPane = new JPanel();
		adminPane.setSize(new Dimension(820, 600));
		adminPane.setLayout(null);
		drawAdminButtonPane();

		drawLeftAdminButtons();
	}
	
	
	/**
	 * Draws administrative button pane.
	 */
	private void drawAdminButtonPane () {
		adminButtonPane = new JPanel();
		adminButtonPane.setSize(600, 100);
		adminButtonPane.setLocation(110, 200);
		adminButtonPane.setLayout(new GridLayout(1, 3, 2, 2));
		drawAdminButtons();
		adminPane.add(adminButtonPane);
		
	}
	
	
	/**
	 * Draws administrative button.
	 */
	private void drawAdminButtons() {
		manageProductsButton = new HelveticaJButton("<html><center>Manage Products</center></html>", 16);
		manageProductsButton.addActionListener(this);
		adminButtonPane.add(manageProductsButton);

		manageCategoriesButton = new HelveticaJButton("<html><center>Manage Categories</center></html>", 16);
		manageCategoriesButton.addActionListener(this);
		adminButtonPane.add(manageCategoriesButton);
		
		manageAffiliationsButton =  new HelveticaJButton("<html><center>Manage Affiliations,<p>Credits, and Subsidy</center></html>", 16);
		manageAffiliationsButton.addActionListener(this);
		adminButtonPane.add(manageAffiliationsButton);
	}
	
	
	/**
	 * Draws the lefthand buttons of the admin pane.
	 */
	private void drawLeftAdminButtons() {
		purchaseViewButton = new HelveticaJButton("Purchase View", 13);
		purchaseViewButton.setBounds(20, 528, 175, 31);
		purchaseViewButton.addActionListener(this);
		adminPane.add(purchaseViewButton);
	}
	
	
	/**
	 * Draws the purchase pane.
	 * @param productList the products available for purchase
	 * @param affiliations the available affiliations
	 */
	private void drawPurchasePane(ArrayList<Product> productList, ArrayList<Affiliation> affiliations) {
		purchasePane = new JPanel();
		purchasePane.setSize(new Dimension(820, 600));
		purchasePane.setLayout(null);

		productButtonPane = new JPanel();
		productButtonPane.setLocation(20, 20);
		productButtonPane.setSize(new Dimension(440, 440));
		productButtonPane.setLayout(new GridLayout(0, 4, 2, 2)); // "0, 4, 2, 2" specifies unbounded (0) rows with 4 columns and x and y padding of 2.
		drawProductButtons(productList);
		purchasePane.add(productButtonPane);

		drawClientComponents(affiliations);

		drawPurchaseComponents();

		drawLeftPurchaseButtons();
		
		purchasePane.setVisible(true);
		
	}

	
	/**
	 * Draws lefthand buttons of the purchase pane. 
	 */
	private void drawLeftPurchaseButtons() {
		adminViewButton = new HelveticaJButton("Administrator View", 13);
		adminViewButton.setBounds(20, 528, 175, 31);
		adminViewButton.addActionListener(this);
		purchasePane.add(adminViewButton);

		voidButton = new HelveticaJButton("Void", 13);
		voidButton.setToolTipText("Coming soon!");
		voidButton.setEnabled(false);
		voidButton.setBounds(205, 528, 130, 31);
		purchasePane.add(voidButton);
	}
	
	
	/**
	 * Draws the buttons for all available products for purchase.
	 * @param productList the available products for purchase
	 */
	private void drawProductButtons(ArrayList<Product> productList) {
		NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance(Locale.US);
		for (Product p : productList) {
			ProductJButton pjb = new ProductJButton("<html><center>"+CURRENCY_FORMAT.format(p.getPrice()/100.0)+
					"<br>"+ p.getName()+"</center></html>", p);
			pjb.addActionListener(this);
			productButtonPane.add(pjb);
		}
	}
	
	
	/**
	 * Draws the purchase components.
	 */
	private void drawPurchaseComponents() {
		purchaseButtonPane = new JPanel();
		purchaseButtonPane.setBounds(480, 150, 320, 31);
		purchaseButtonPane.setLayout(new GridLayout(1, 3, 2, 0) );
		drawPurchaseModifierButtons(purchaseButtonPane);
		purchasePane.add(purchaseButtonPane);
		
		purchaseList = new JPanel();
		purchaseList.setBounds(480, 187, 320, 336);
		purchasePane.add(purchaseList);
		purchaseList.setBorder(BorderFactory.createLineBorder(LIGHT_GREY));
		purchaseList.setLayout(new BorderLayout());
		purchaseList.setVisible(true);

		productListBox = new JPanel();
		productListBox.setLayout(new BoxLayout(productListBox, BoxLayout.Y_AXIS));
		purchaseList.add(productListBox, BorderLayout.WEST);


		priceListBox = new JPanel();
		priceListBox.setLayout(new BoxLayout(priceListBox, BoxLayout.Y_AXIS));
		purchaseList.add(priceListBox, BorderLayout.EAST);

		totalPane = new JPanel();
		totalPane.setLayout(new BorderLayout());
		totalLabelBox = new JPanel();
		totalLabelBox.setLayout(new BoxLayout(totalLabelBox, BoxLayout.Y_AXIS));
		totalPriceBox = new JPanel();
		totalPriceBox.setLayout(new BoxLayout(totalPriceBox, BoxLayout.Y_AXIS));
		totalPane.add(totalLabelBox, BorderLayout.WEST);
		totalPane.add(totalPriceBox, BorderLayout.EAST);
		purchaseList.add(totalPane, BorderLayout.SOUTH);

		HelveticaJLabel purchaseLabel = new HelveticaJLabel("PURCHASE", 18);
		purchaseLabel.setBorder(BorderFactory.createEmptyBorder(6, 1, 3, 1));
		purchaseLabel.setBounds(0, 10, 320, 19);
		purchaseLabel.setHorizontalAlignment(SwingConstants.CENTER);
		purchaseList.add(purchaseLabel, BorderLayout.NORTH);

		submitButton = new HelveticaJButton("SUBMIT", 13);
		submitButton.addActionListener(this);
		
		submitButton.setBounds(480, 528, 320, 31);
		purchasePane.add(submitButton);
	}
	

	/**
	 * Draws the purchase modifier buttons for the purchase view.
	 * @param pane the JPanel to which to add the modifier buttons
	 */
	private void drawPurchaseModifierButtons (JPanel pane) {
		quantityButton = new HelveticaJButton("CHANGE QTY", 12);
		quantityButton.setBorder(new LineBorder(LIGHT_GREY));
		quantityButton.setToolTipText("Click a product in purchase to enable.");
		quantityButton.addActionListener(this);
		quantityButton.setEnabled(false);
		pane.add(quantityButton);
	
		deleteButton = new HelveticaJButton("DELETE ITEM", 12);
		deleteButton.setBorder(new LineBorder(LIGHT_GREY));
		deleteButton.setToolTipText("Click a product in purchase to enable.");
		deleteButton.addActionListener(this);
		deleteButton.setEnabled(false);
		pane.add(deleteButton);
	
		clearButton = new HelveticaJButton("CLEAR ALL", 12);
		clearButton.setBorder(new LineBorder(LIGHT_GREY));
		clearButton.addActionListener(this);
		pane.add(clearButton);
	}

	
	/**
	 * Draws the client components for the purchase view.
	 * @param affiliations all available affiliations
	 */
	private void drawClientComponents(ArrayList<Affiliation> affiliations) {
		JPanel clientPane = new JPanel();
		clientPane.setLayout(null);
		clientPane.setBorder(new LineBorder(LIGHT_GREY));
		clientPane.setBounds(480, 20, 320, 123);
		
		HelveticaJLabel clientInfoLabel = new HelveticaJLabel("CLIENT INFO", 16);

		clientInfoLabel.setBounds(89, 10, 141, 16);
		clientInfoLabel.setHorizontalAlignment(SwingConstants.CENTER);
		clientPane.add(clientInfoLabel);

		HelveticaJLabel suidLabel = new HelveticaJLabel("SUID", 14);
		suidLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		suidLabel.setBounds(19, 46, 61, 16);
		clientPane.add(suidLabel);

		HelveticaJLabel affiliationLabel = new HelveticaJLabel("Affiliation", 14);
		affiliationLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		affiliationLabel.setBounds(0, 82, 80, 16);
		clientPane.add(affiliationLabel);

		suidField = new JTextField(10);
		suidField.setBounds(90, 40, 120, 27);
		suidField.setFont(new Font("Helvetica", Font.PLAIN, 12));
		clientPane.add(suidField);

		affiliationComboBox = new JComboBox(affiliations.toArray());
		affiliationComboBox.setBounds(90, 78, 120, 27);
		affiliationComboBox.setFont(new Font("Helvetica", Font.PLAIN, 12));
		clientPane.add(affiliationComboBox);

		enterClientButton = new HelveticaJButton("ENTER", 12);
		enterClientButton.setBounds(219, 56, 80, 29);
		enterClientButton.addActionListener(this);
		clientPane.add(enterClientButton);
		
		purchasePane.add(clientPane);
	}

	
	/**
	 * Removes all components from vertical boxes.
	 */
	private void clearContainers() {
		priceListBox.removeAll();
		productListBox.removeAll();
		totalLabelBox.removeAll();
		totalPriceBox.removeAll();
	}

	
	/**
	 * Validates all vertical boxes.
	 */
	private void validateBoxes() {
		totalPriceBox.validate();
		purchaseList.validate();
		totalLabelBox.validate();
		productListBox.validate();
		priceListBox.validate();
	}

	

}
