

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
	private JPanel purchaseList; 



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
		frame.getContentPane().add(buttonPanel);
		frame.validate();

		JPanel clientPanel = new JPanel();
		clientPanel.setLayout(null);
		clientPanel.setBorder(new LineBorder(LIGHT_GREY));
		clientPanel.setBounds(480, 20, 320, 123);
		drawClientComponents(clientPanel);
		frame.getContentPane().add(clientPanel);

		JPanel purchaseButtonPanel = new JPanel();
		purchaseButtonPanel.setBounds(480, 150, 320, 31);
		purchaseButtonPanel.setLayout(null);
		frame.getContentPane().add(purchaseButtonPanel);
		drawPurchaseModifierButtons(purchaseButtonPanel);

		JPanel purchasePanel = new JPanel();
		purchasePanel.setBorder(new LineBorder(LIGHT_GREY));
		purchasePanel.setBounds(480, 187, 320, 336);
		frame.getContentPane().add(purchasePanel);
		purchasePanel.setLayout(null);

		purchaseList = new JPanel();
		purchaseList.setBounds(5, 35, 310, 295);
		purchaseList.setVisible(true);
		purchaseList.setLayout(new BoxLayout(purchaseList, BoxLayout.Y_AXIS));
		//purchaseList.setLayout(null);
		purchasePanel.add(purchaseList);

		JLabel purchaseLabel = new JLabel("PURCHASE");
		purchaseLabel.setBounds(0, 10, 320, 19);
		purchaseLabel.setFont(new Font("Helvetica", Font.BOLD, 16));
		purchaseLabel.setHorizontalAlignment(SwingConstants.CENTER);
		purchasePanel.add(purchaseLabel);

		submitButton = new JButton("SUBMIT");
		submitButton.setFont(new Font("Helvetica", Font.BOLD, 13));
		submitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("Submit hit!");
			}
		});
		submitButton.setBounds(480, 528, 320, 31);
		frame.getContentPane().add(submitButton);

		JButton adminViewButton = new JButton("Administrator View");
		adminViewButton.setFont(new Font("Helvetica", Font.BOLD, 13));
		adminViewButton.setBounds(20, 528, 175, 31);
		frame.getContentPane().add(adminViewButton);

		JButton voidButton = new JButton("Void");
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

	private void drawClientComponents(JPanel panel) {
		JLabel clientInfoLabel = new JLabel("CLIENT INFO");
		clientInfoLabel.setFont(new Font("Helvetica", Font.BOLD, 16));
		clientInfoLabel.setBounds(89, 10, 141, 16);
		clientInfoLabel.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(clientInfoLabel);

		JLabel suidLabel = new JLabel("SUID");
		suidLabel.setFont(new Font("Helvetica", Font.BOLD, 12));
		suidLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		suidLabel.setBounds(19, 46, 61, 16);
		panel.add(suidLabel);

		JLabel affiliationLabel = new JLabel("Affiliation");
		affiliationLabel.setFont(new Font("Helvetica", Font.BOLD, 12));
		affiliationLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		affiliationLabel.setBounds(0, 82, 80, 16);
		panel.add(affiliationLabel);

		textField = new JTextField(10);
		textField.setBounds(90, 40, 120, 27);
		textField.setFont(new Font("Helvetica", Font.PLAIN, 12));
		panel.add(textField);

		affiliationComboBox = new JComboBox(AFFILIATIONS);
		affiliationComboBox.setBounds(90, 78, 120, 27);
		affiliationComboBox.setFont(new Font("Helvetica", Font.PLAIN, 12));
		panel.add(affiliationComboBox);

		enterButton = new JButton("ENTER");
		enterButton.setFont(new Font("Helvetica", Font.BOLD, 12));
		enterButton.setBounds(219, 56, 80, 29);
		enterButton.addActionListener(this);
		panel.add(enterButton);
	}


	private void drawPurchaseModifierButtons (JPanel panel) {
		quantityButton = new JButton("QUANTITY");
		quantityButton.setFont(new Font("Helvetica", Font.BOLD, 12));
		quantityButton.setBounds(0, 0, 103, 31);
		panel.add(quantityButton);

		deleteButton = new JButton("DELETE");
		deleteButton.setFont(new Font("Helvetica", Font.BOLD, 12));
		deleteButton.setBounds(109, 0, 103, 31);
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
		}
	}

	public void displayPurchase (HashMap<Product, Integer> purchaseProducts, int total) {
		purchaseList.removeAll();
		purchaseList.repaint();
		ArrayList<LabelAppearanceJButton> buttons = new ArrayList<LabelAppearanceJButton>();
		NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
		Iterator it = purchaseProducts.entrySet().iterator();
		for (int i = 0; it.hasNext(); i++) {
			Map.Entry<Product, Integer> pair = (Entry<Product, Integer>) it.next();
			Product product = pair.getKey();
			int quantity = pair.getValue();
			String text = product.getName() + "        " + currencyFormat.format(quantity*product.getPrice()/100.0);
			LabelAppearanceJButton button = new LabelAppearanceJButton(text);
			button.addActionListener(this);
			buttons.add(button);
		}
		for (LabelAppearanceJButton button : buttons) {
			purchaseList.add(button);
		}
		purchaseList.add(new LabelAppearanceJButton("-----"));
		purchaseList.add(new LabelAppearanceJButton("TOTAL"));
		purchaseList.add(new LabelAppearanceJButton(currencyFormat.format(total/100.0)));
		purchaseList.validate();
	}
	
	public int getQuantity() {
		String inputValue = JOptionPane.showInputDialog("Specify a quantity: ");
		return Integer.parseInt(inputValue);
	}
}
