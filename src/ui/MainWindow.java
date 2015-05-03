package ui;

import model.DBParser;
import model.Repository;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import util.SqlServerUtil;

public class MainWindow extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
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
	
	JTabbedPane tabbedPanel;
	Repository repos = Repository.getInstance();
		
	private long period;
	private boolean runMonitor = false;
	
	private Thread monitoringThread = new Thread(new Runnable() {
		
		private String TABLE_NAME = "[PLDC].[dbo]." + new SimpleDateFormat("[M/d/yyyy]").format(new Date());
		private DBParser pro = new DBParser(TABLE_NAME);

		@Override
		public void run() {
			while(true) {
				if(runMonitor) {
					pro.processData();
					updateDisplay();
				}
							
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}			
		}
	});
	
	public MainWindow(long period, int auth) {		
		this.period = period;		
		setTitle("Datamatic @ Micro Automation Industries");
		
		tabbedPanel = new JTabbedPane(JTabbedPane.TOP);
		getContentPane().add(tabbedPanel, BorderLayout.CENTER);
		
		switch(auth) {
			case 1:
				tabbedPanel.addTab("Assignment", new AssignmentPanel(this));	
			case 2:
				tabbedPanel.addTab("Average Efficiency", new AverageEfficiencyPanel(this));		
				tabbedPanel.addTab("Product Monitor", new ProductMonitor());
			case 3:
			default:
				tabbedPanel.addTab("Public Display", new IndividualEfficiencyPanel(this));
				if(auth>1) {
					repos.init("init from file");
					runMonitor = true;
				}
		}
		
//		tabbedPanel.setEnabledAt(1, false);
//		tabbedPanel.setEnabledAt(2, false);
		
		tabbedPanel.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if(runMonitor)
					updateDisplay();
			}
		});
		
//		setBounds(200, 30, 800, 600);
		pack();
//		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		
		monitoringThread.start();
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.out.println("program exits!");
				SqlServerUtil.closeConnection();
				Repository.LOGGER.close();
				System.exit(0);
			}
		});
				
//		JMenuBar menuBar = new JMenuBar();
//		setJMenuBar(menuBar);
//		
//		JMenu mnOption = new JMenu("Option");
//		menuBar.add(mnOption);
//		
//		JMenuItem mntmUpdateProducts = new JMenuItem("Update products");
//		mntmUpdateProducts.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//		
//			}
//		});
//		mnOption.add(mntmUpdateProducts);
//		
//		JMenu mnAbout = new JMenu("About");
//		menuBar.add(mnAbout);
		
	}
		
	public long getPeriod() {
		return period;
	}

	public void setPeriod(long period) {
		this.period = period;
	}
	
	public void updateDisplay(){
		int index = tabbedPanel.getSelectedIndex();
		((RealTimeDisplay)tabbedPanel.getComponentAt(index)).updateDisplay();
	}
	
	public void startMonitoring(){
		runMonitor = true;
//		timer.schedule(task, 1000, period);	
//		tabbedPanel.setEnabledAt(1, true);
//		tabbedPanel.setEnabledAt(2, true);
		tabbedPanel.setSelectedIndex(1);
	}
	
	public void stopMonitoring() {
//		timer.cancel();
		runMonitor = false;
	}
	
	public static void main(String[] args) {	
		
		new Login();

//		System.out.println(FileChooser.saveAsFile());
		
//		final JFrame f = new JFrame("Assignment");
//		f.getContentPane().add(new ProductMonitor());
//		f.setBounds(200, 50, 800, 640);
//		f.setDefaultCloseOperation(EXIT_ON_CLOSE);
//		f.setVisible(true);
		
		
//		SwingUtilities.invokeLater(new Runnable() {		
//			@Override
//			public void run() {
//				new MainWindow(1000);
//			}
//		});				
	}
}
