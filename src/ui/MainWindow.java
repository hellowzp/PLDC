package ui;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import java.awt.BorderLayout;

import javax.swing.JPanel;

public class MainWindow extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	private AverageEffPanel avgPanel;
	private IndividualEffPanel indPanel;
	private ProductMonitorPanel productPanel;
	
	static{  	
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}  	
    }


	public MainWindow() {
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
		avgPanel = new AverageEffPanel();
		tabbedPane.addTab("Average Efficiency", null, avgPanel, null);
		
		indPanel = new IndividualEffPanel();
		tabbedPane.addTab("Individual Efficiency", null, indPanel, null);
		
		productPanel = new ProductMonitorPanel();
		tabbedPane.addTab("Product", null, productPanel, "Product stages tracking");

		
	}

	
	public static void main(String[] args) {
		
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				JFrame frame = new MainWindow();
				frame.setBounds(200, 30, 800, 600);
				frame.pack();
				frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
				frame.setVisible(true);
			}
		});
		
	}

}
