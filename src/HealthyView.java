import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import net.miginfocom.swing.MigLayout;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.RowSpec;

/**
 * Experimental class for learning WindowBuilder.
 * Biggest takeaway: all automatically generated code will have to be completely 
 * overhauled to be readable and clean.
 * @author Sophi Newman
 *
 */

public class HealthyView extends JFrame {
	public HealthyView() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBackground(Color.WHITE);
		getContentPane().setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.UNRELATED_GAP_COLSPEC,
				ColumnSpec.decode("left:226px:grow"),
				FormFactory.DEFAULT_COLSPEC,
				ColumnSpec.decode("center:283px"),
				ColumnSpec.decode("max(0dlu;default)"),
				ColumnSpec.decode("right:226px:grow"),
				FormFactory.UNRELATED_GAP_COLSPEC,
				ColumnSpec.decode("1px"),},
			new RowSpec[] {
				FormFactory.PARAGRAPH_GAP_ROWSPEC,
				RowSpec.decode("39px"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("20px"),
				FormFactory.UNRELATED_GAP_ROWSPEC,
				RowSpec.decode("32px"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("116px:grow"),
				RowSpec.decode("default:grow"),}));
		
		JLabel lblNewLabel = new JLabel("Healthy Eating Made Easy");
		lblNewLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 33));
		getContentPane().add(lblNewLabel, "1, 2, 7, 1, center, center");
		
		JLabel lblHungryPeople = new JLabel("Hungry People");
		lblHungryPeople.setFont(new Font("Lucida Grande", Font.PLAIN, 21));
		getContentPane().add(lblHungryPeople, "2, 6, center, center");
		
		Component rigidArea = Box.createRigidArea(new Dimension(20, 20));
		getContentPane().add(rigidArea, "3, 6");
		
		JLabel lblDeliciousVegetables = new JLabel("Delicious Vegetables");
		lblDeliciousVegetables.setFont(new Font("Lucida Grande", Font.PLAIN, 21));
		getContentPane().add(lblDeliciousVegetables, "4, 6");
		
		Component rigidArea_1 = Box.createRigidArea(new Dimension(20, 20));
		getContentPane().add(rigidArea_1, "5, 6");
		
		JLabel lblPickASource = new JLabel("Pick a source of fiber");
		lblPickASource.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
		getContentPane().add(lblPickASource, "6, 6, center, default");
		
		JPanel panel_3 = new JPanel();
		getContentPane().add(panel_3, "2, 8, 1, 2, fill, fill");
		panel_3.setLayout(null);
		
		
		ProductJButton btnBobbyIsA = new ProductJButton("Bobby is a CJB", 9);
		btnBobbyIsA.setBounds(8, 24, 92, 20);
		btnBobbyIsA.setHorizontalAlignment(SwingConstants.LEFT);
		btnBobbyIsA.setHorizontalTextPosition( SwingConstants.LEFT );
		setLabelAppearance(btnBobbyIsA);
		panel_3.add(btnBobbyIsA);
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, "4, 8, 1, 2, center, fill");
		panel.setLayout(new GridLayout(0, 3, 0, 0));
		
		JButton btnRadishes = new JButton("radish");
		panel.add(btnRadishes);
		
		JButton btnPickles = new JButton("tomato");
		panel.add(btnPickles);
		
		JButton btnNewButton = new JButton("cucumber");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//tell the controller to add this veggie
				//the controller tells the model what to update
			}
		});
		
		JButton btnChayote = new JButton("chayote");
		panel.add(btnChayote);
		
		JButton btnBroccoli = new JButton("broccoli");
		panel.add(btnBroccoli);
		
		JButton btnCauliflower = new JButton("cauliflower");
		panel.add(btnCauliflower);
		
		JButton btnCabbage = new JButton("cabbage");
		panel.add(btnCabbage);
		
		JButton btnSquash = new JButton("squash");
		panel.add(btnSquash);
		
		JButton btnAsparagus = new JButton("asparagus");
		panel.add(btnAsparagus);
		panel.add(btnNewButton);
		
		JButton btnCarrots = new JButton("carrot");
		panel.add(btnCarrots);
		
		JButton btnSpinach = new JButton("spinach");
		panel.add(btnSpinach);
		
		JButton btnCorn = new JButton("corn");
		btnCorn.setBackground(new Color(204, 255, 204));
		panel.add(btnCorn);
		
		JPanel panel_2 = new JPanel();
		getContentPane().add(panel_2, "6, 8, center, fill");
		
		JLabel label = new JLabel("What would you like to eat? ");
		panel_2.add(label);
		
		textField = new JTextField();
		textField.setText("flax seed");
		textField.setColumns(10);
		panel_2.add(textField);
		
		JButton button = new JButton("Feed me!");
		panel_2.add(button);
		
		JPanel panel_1 = new JPanel();
		getContentPane().add(panel_1, "6, 9, fill, fill");
		panel_1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JButton btnCancel = new JButton("CANCEL");
		panel_1.add(btnCancel);
		
		JButton btnDelete = new JButton("DELETE");
		panel_1.add(btnDelete);
		
		JButton btnSubmit = new JButton("SUBMIT");
		panel_1.add(btnSubmit);
	}
	
	JFrame foo = new JFrame();
	
	//Makes a JButton look like a JLabel
	private void setLabelAppearance (JButton button) {
		button.setFocusPainted(false);
		button.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
		button.setContentAreaFilled(false);
		button.setBorderPainted(false);
		button.setOpaque(false);
	}
	private JTextField textField;
}
