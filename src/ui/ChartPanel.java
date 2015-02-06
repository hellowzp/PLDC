package ui;

import javax.swing.JPanel;

import com.xeiam.xchart.XChartPanel;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class ChartPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private XChartPanel chartPanel;
	private JTextField textField;
	private JTextField textField_1;
	private JLabel lblEndTime;
	private JTextField textField_2;
	private JTextField textField_3;
	
	public ChartPanel() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JPanel controlPanel = new JPanel();
		controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.X_AXIS));
		JLabel lblStartTime = new JLabel("Start time");
		controlPanel.add(lblStartTime);
		
		add(chartPanel);
		add(controlPanel);
		
		textField_1 = new JTextField();
		controlPanel.add(textField_1);
		textField_1.setColumns(4);
		
		textField = new JTextField();
		controlPanel.add(textField);
		textField.setColumns(4);
		
		lblEndTime = new JLabel("End time");
		controlPanel.add(lblEndTime);
		
		textField_2 = new JTextField();
		textField_2.setText("16");
		controlPanel.add(textField_2);
		textField_2.setColumns(10);
		
		textField_3 = new JTextField();
		controlPanel.add(textField_3);
		textField_3.setColumns(10);

	}

}
