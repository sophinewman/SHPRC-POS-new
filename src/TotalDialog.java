import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Box;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class TotalDialog extends JDialog implements ActionListener {
	
	private PurchaseController controller;
	private Box verticalBox;
	private JPanel controlButtonPane;
	JPanel contentPane;
	JTextField tendered;
	HelveticaJButton cancelButton, submitButton, continueButton;
	
	public TotalDialog (PurchaseController controller, JFrame frame, String total) {
		super(frame, "Purchase Total"/*, true*/);
		
		this.controller = controller;
		
		contentPane = new JPanel(new BorderLayout(5,5));
		
		contentPane.add(new JLabel("   "), BorderLayout.WEST);
		contentPane.add(new JLabel("   "), BorderLayout.EAST);
		contentPane.add(new JLabel(" "), BorderLayout.NORTH);
		
		getContentPane().add(contentPane);
		
		verticalBox = Box.createVerticalBox();
		contentPane.add(verticalBox, BorderLayout.CENTER);
		HelveticaJLabel helveticaJLabel = new HelveticaJLabel("<html>Client's total is: <u>" + total + "</u></html>", 16);
		helveticaJLabel.setHorizontalAlignment(SwingConstants.CENTER);
		helveticaJLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		verticalBox.add(helveticaJLabel);
		verticalBox.add(new JLabel("   "));
		
		JPanel tenderedPane = new JPanel(new FlowLayout(5));
		tenderedPane.add(new HelveticaJLabel("Cash tendered: ", 16));
		tendered = new JTextField(5);
		tendered.addActionListener(this);
		tenderedPane.add(tendered);
		tenderedPane.setAlignmentX(Component.CENTER_ALIGNMENT);
		verticalBox.add(tenderedPane);
		
		controlButtonPane = new JPanel(new FlowLayout(FlowLayout.CENTER));
		
		cancelButton = new HelveticaJButton("Cancel", 14);
		cancelButton.addActionListener(this);
		controlButtonPane.add(cancelButton);

		controlButtonPane.add(new JLabel("   "));

		submitButton = new HelveticaJButton("Submit", 14);
		submitButton.addActionListener(this);
		controlButtonPane.add(submitButton);
		
		contentPane.add(controlButtonPane, BorderLayout.SOUTH);
		
		this.pack();
		this.setLocationRelativeTo(frame);
		
		this.setVisible(true);
	}
	
	public void showChangeMessage(String change) {
		controlButtonPane.removeAll();
		verticalBox.setVisible(false);
		continueButton = new HelveticaJButton("Continue", 14);
		controlButtonPane.add(continueButton);
		HelveticaJLabel label = new HelveticaJLabel("<html>Change due: <u>" + change + "</u></html>", 16);
		label.setAlignmentX(Component.CENTER_ALIGNMENT);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(label, BorderLayout.CENTER);
		this.validate();
		this.setVisible(true);
	}

	public void actionPerformed(ActionEvent event) {
		Object src = event.getSource();
		if (src == cancelButton) {
			this.dispose();
		} else if (src == submitButton || src == tendered) {
			if (! controller.cashTendered(tendered.getText(), this)) {
				JOptionPane.showMessageDialog(this, "Invalid entry. Please try again.", "Input Error", JOptionPane.ERROR_MESSAGE);
			}
		} else if (src == continueButton) {
			controller.commitPurchase();
			this.dispose();
		}
	}

}
