package ui;

import model.DBParser;
import model.Product;
import model.Repository;
import model.Worker;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.xeiam.xchart.Chart;
import com.xeiam.xchart.Series;
import com.xeiam.xchart.SeriesColor;
import com.xeiam.xchart.SeriesLineStyle;
import com.xeiam.xchart.SeriesMarker;
import com.xeiam.xchart.StyleManager;
import com.xeiam.xchart.XChartPanel;

import util.AssignmentCSVReaderWriter;
import util.FileChooser;
import util.ProductCSVReader;
import util.SqlServerUtil;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;

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
	
	private JTabbedPane tabbedPanel;
	private Repository repos = Repository.getInstance();
	
	private static final String ICON_PATH = System.getProperty("user.dir") + "/res/icons/";
	private static final String TABLE_NAME = "[PLDC].[dbo]." + new SimpleDateFormat("[M/d/yyyy]").format(new Date());
	
	private long period;
	private boolean runMonitor = false;
	private final Thread monitoringThread = new Thread(new Runnable() {
		private final DBParser pro = new DBParser(TABLE_NAME);

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
				tabbedPanel.addTab("Assignment", new AssignmentPanel());	
			case 2:
				tabbedPanel.addTab("Average Efficiency", new AverageEfficiencyPanel());		
				tabbedPanel.addTab("Product Monitor", new ProductMonitor());
			case 3:
			default:
				tabbedPanel.addTab("Public Display", new IndividualEfficiencyPanel());	
		}
		
//		tabbedPanel.setEnabledAt(1, false);
//		tabbedPanel.setEnabledAt(2, false);
		
		tabbedPanel.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				updateDisplay();
			}
		});
		
//		setBounds(200, 30, 800, 600);
		pack();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		
		monitoringThread.start();
		if(auth>1) {
			repos.initProducts("");
			runMonitor = true;
		}
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.out.println("program exits!");
				SqlServerUtil.closeConnection();
				Repository.LOGGER.close();
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
	
	private class AssignmentPanel extends JPanel implements RealTimeDisplay{
		
		private static final long serialVersionUID = 1L;
		
		private JComboBox<String> seriesCombox, productCombox, stageCombox, workerCombox;
		private JComboBox<Integer> stationCombox;
		private JTable tableView;
		private Vector<String> tableColumns = new Vector<>(12);
		private Vector<Vector<Object>> modelData = new Vector<>(20);
		
		private List<ProductCSVReader> proSeries = new ArrayList<ProductCSVReader>();
		
		private final DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
	        private static final long serialVersionUID = 1L;

			@Override
	        public Component getTableCellRendererComponent(JTable table, Object value, 
	        			boolean isSelected, boolean hasFocus, int row, int column) {
	            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	            setForeground(Color.blue);
	            setHorizontalAlignment(JLabel.CENTER);
	            return this;
	        }
	    };	
		
	    private AssignmentPanel() {
			setLayout(null);
			setPreferredSize(new Dimension(800,640));
			
			JLabel lblStation = new JLabel("Station");
			lblStation.setFont(new Font("SimSun", Font.PLAIN, 14));
			lblStation.setBounds(30, 25, 54, 15);
			add(lblStation);
			
			stationCombox = new JComboBox<Integer>(getStationList());
			stationCombox.setBounds(85, 23, 42, 21);
			add(stationCombox);
			
			JLabel lblSeries = new JLabel("Series");
			lblSeries.setFont(new Font("SimSun", Font.PLAIN, 14));
			lblSeries.setBounds(150, 25, 45, 15);
			add(lblSeries);
			
			String folder = System.getProperty("user.dir") + "/res/series";
			seriesCombox = new JComboBox<String>(getSeriesList(folder));
			seriesCombox.setSelectedIndex(-1);
			seriesCombox.setBounds(200, 22, 50, 21);
			add(seriesCombox);
			
			JLabel lblProduct = new JLabel("Product");
			lblProduct.setFont(new Font("SimSun", Font.PLAIN, 14));
			lblProduct.setBounds(277, 25, 54, 15);
			add(lblProduct);
			
			productCombox = new JComboBox<String>();
			productCombox.setBounds(335, 23, 110, 21);
			add(productCombox);
			
			JLabel lblStage = new JLabel("Stage");
			lblStage.setFont(new Font("SimSun", Font.PLAIN, 14));
			lblStage.setBounds(470, 25, 40, 15);
			add(lblStage);
			
			stageCombox = new JComboBox<String>();
			stageCombox.setBounds(513, 23, 92, 21);
			add(stageCombox);
			
			JLabel lblWorker = new JLabel("Worker");
			lblWorker.setFont(new Font("SimSun", Font.PLAIN, 14));
			lblWorker.setBounds(625, 25, 46, 15);
			add(lblWorker);
			
			workerCombox = new JComboBox<String>();
			workerCombox.setBounds(677, 23, 78, 21);
			add(workerCombox);
			
			JLabel logo = new JLabel(new ImageIcon(ICON_PATH + "logo.png"));
			logo.setBounds(800, 100, 541, 162);
			add(logo);
			
			final JButton btnAddRow = new JButton("Add");
			btnAddRow.setBounds(680, 54, 75, 23); 
			add(btnAddRow);
			
			final JButton btnDeleteRow = new JButton("Delete");
			btnDeleteRow.setBounds(470, 54, 75, 23);
			add(btnDeleteRow);
			
			final JButton btnUpdateRow = new JButton("Update");
			btnUpdateRow.setBounds(575, 54, 75, 23);
			add(btnUpdateRow);
			
			btnUpdateRow.setEnabled(false);
			btnDeleteRow.setEnabled(false);
			
			JButton save = new JButton("Save as CSV");
			save.setBounds(30, 590, 100, 28);
			save.setMargin(new Insets(1,2,1,2));
			save.setFont(new Font("Tahoma", Font.PLAIN, 14));
			save.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String path = FileChooser.saveAsFile();
					if(path!=null) {
						AssignmentCSVReaderWriter.writeTableToCSV(tableView, path);
						JOptionPane.showMessageDialog( AssignmentPanel.this,"Saved successfully.");
					}else{
						JOptionPane.showMessageDialog( AssignmentPanel.this,"Please give a file name to save as.");
					}
				}
			});
			add(save);
			
			JButton load = new JButton("Load from CSV");
			load.setBounds(150, 590, 100, 28);
			load.setMargin(new Insets(1,2,1,2));
			load.setFont(new Font("Tahoma", Font.PLAIN, 14));
			load.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String path = FileChooser.chooseCSVFile();
					if(path != null) {
						Vector<Vector<String>> data = AssignmentCSVReaderWriter.generateTableFromCSV(tableView, path);
						int i = 0, j = 0;
						for(Vector<String> row : data) {
							j = 0;
							for(String s : row) {
								if(j==0 || j==6) modelData.get(i).set(j, Integer.valueOf(s));
								else if(j==5 || j==11) modelData.get(i).set(j, Boolean.valueOf(s));
								else {
									if(s.equals("null")) modelData.get(i).set(j, null); 
									else modelData.get(i).set(j,s);
								}
								j++;
							}
							i++;
						}					
					}
					
					updateTable();
				}
			});
			add(load);
			
			final JButton btnStart = new JButton("Start monitoring");
			btnStart.setFont(new Font("Tahoma", Font.PLAIN, 14));
			btnStart.setBounds(392, 590, 110, 28);
			btnStart.setMargin(new Insets(1,2,1,2));
			add(btnStart);
			
			final JButton btnStop = new JButton("Stop monitoring");
			btnStop.setFont(new Font("Tahoma", Font.PLAIN, 14));
			btnStop.setBounds(517, 590, 110, 28);
			btnStop.setMargin(new Insets(1,2,1,2));
			add(btnStop);
			
			JButton btnClear = new JButton("Clear Messages");
			btnClear.setFont(new Font("Tahoma", Font.PLAIN, 14));
			btnClear.setBounds(642, 590, 110, 28);
			btnClear.setMargin(new Insets(1,2,1,2));
			btnClear.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					SqlServerUtil.clearMessages();
				}
			});
			add(btnClear);
			
			tableView = new JTable(new MyTableModel());
//	        tableView.setPreferredScrollableViewportSize(new Dimension(720, 400));
	        tableView.setFillsViewportHeight(true);
	        tableView.setRowHeight(22);
//	        tableView.setFont(new Font("SimSun", Font.PLAIN, 14));
	        tableView.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
			tableView.setDefaultRenderer(String.class, renderer);
			
			tableView.setCellSelectionEnabled(true);
			tableView.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
				@Override
				//fired when cell is selected or modified 
				public void valueChanged(ListSelectionEvent e) {
					int row = tableView.getSelectedRow();
					int col = tableView.getSelectedColumn() / 6 * 6;
//					System.out.println(row + " " + col);
					if(row>=0 && col>=0) {
						stationCombox.setSelectedItem(modelData.get(row).get(col));
						seriesCombox.setSelectedItem(modelData.get(row).get(col+1));
						productCombox.setSelectedItem(modelData.get(row).get(col+2));
						stageCombox.setSelectedItem(modelData.get(row).get(col+3));
						workerCombox.setSelectedItem(modelData.get(row).get(col+4));
						
						btnUpdateRow.setEnabled(true);
						btnDeleteRow.setEnabled(true);
					}else{
						btnUpdateRow.setEnabled(false);
						btnDeleteRow.setEnabled(false);
					}
				}
			});
			
			setColumnPreferredWidth();
	        
			JScrollPane scrollPane = new JScrollPane(tableView);
//			scrollPane.setPreferredSize(new Dimension(720, 400));
			scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
			//this will determine the bounds of the containing table
			scrollPane.setBounds(30, 100, 725, 465);
			add(scrollPane);
			
			seriesCombox.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					int selection = seriesCombox.getSelectedIndex();
					if(selection>=0) {
						comboxResetItems(productCombox, proSeries.get(selection).getProductList());
					}
				}
			});
			
			productCombox.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					String pro = (String) productCombox.getSelectedItem();
					if(pro==null) return;
					int selection = seriesCombox.getSelectedIndex();
					comboxResetItems(stageCombox,proSeries.get(selection).getSupportedStages(pro));
				}
			});
			
			stageCombox.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
//					System.out.println(arg0.paramString());
					String stageCode = (String) stageCombox.getSelectedItem();
					if(stageCode==null) return;
					int selection = seriesCombox.getSelectedIndex();
					comboxResetItems(workerCombox, proSeries.get(selection).getQualifiedWorkers(stageCode));
				}
			});
			
			btnAddRow.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Object series = seriesCombox.getSelectedItem();
					Object product = productCombox.getSelectedItem();
					Object stage = stageCombox.getSelectedItem();
					Object worker = workerCombox.getSelectedItem();
					Object station = stationCombox.getSelectedItem();
					
					if(series==null || product==null) {
						JOptionPane.showMessageDialog(AssignmentPanel.this,"Please select at least the series, product and stage first");
						return;
					}
					
					for(Vector<Object> row : modelData) {
						if(product.equals(row.get(2)) && stage.equals(row.get(3))) {
							int ret = JOptionPane.showConfirmDialog(AssignmentPanel.this,"This stage has already been assigned, do you want to assignment it to another work station?");
							if(ret==JOptionPane.YES_OPTION) {
								row.set(1, null);
								row.set(2, null);
								row.set(3, null);
								row.set(4, null);
								row.set(5, Boolean.FALSE);
								break;
							}else{
								return;
							}
						}
						
						if(product.equals(row.get(8)) && stage.equals(row.get(9))) {
							int ret = JOptionPane.showConfirmDialog(AssignmentPanel.this,"This stage has already been assigned, do you want to assignment it to another work station?");
							if(ret==JOptionPane.YES_OPTION) {
								row.set(7, null);
								row.set(8, null);
								row.set(9, null);
								row.set(10, null);
								row.set(11, Boolean.FALSE);
								break;
							}else{
								return;
							}
						}
					}
					
					int stationID = (Integer) station;
					int row = -1;
					if(stationID<21) {
						row = stationID-1;
						modelData.get(row).set(1, series);
						modelData.get(row).set(2, product);
						modelData.get(row).set(3, stage);
						modelData.get(row).set(4, worker);					
					}else{
						row = stationID-21;
						modelData.get(row).set(7, series);
						modelData.get(row).set(8, product);
						modelData.get(row).set(9, stage);
						modelData.get(row).set(10, worker);
					}
				
					int stationIndex = stationCombox.getSelectedIndex();
					stationCombox.setSelectedIndex(stationIndex<39 ? stationIndex+1 : 0);

					updateTable();
					
				}
			});
			
			btnUpdateRow.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
				}
			});
			
			btnDeleteRow.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					int row = tableView.getSelectedRow();
					int col = tableView.getSelectedColumn();
					if(row == -1 || col == -1){
						JOptionPane.showMessageDialog(AssignmentPanel.this,"Please select the row you want to delete first");
					}else{
						if(col<6) {
							modelData.get(row).set(1, null);
							modelData.get(row).set(2, null);
							modelData.get(row).set(3, null);
							modelData.get(row).set(4, null);
							modelData.get(row).set(5, Boolean.FALSE);
						} else {
							modelData.get(row).set(7, null);
							modelData.get(row).set(8, null);
							modelData.get(row).set(9, null);
							modelData.get(row).set(10, null);
							modelData.get(row).set(11, Boolean.FALSE);
						}
						
						updateTable();
					}
				}
			});
			
			btnStart.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					List<Product> products = new ArrayList<Product>();
					for(ProductCSVReader r : proSeries) {
						products.addAll(r.getProducts());
					}
					repos.initProducts(products);
					
					//assign ws and worker for each stage
					for(Vector<Object> row : modelData) {
						if(row.indexOf(null)>5 || row.indexOf(null)==-1) { //doesn't contain null, a complete row
							Product pro = repos.getProduct((String) row.get(2));
							System.out.println("assign ws and worker for each stage: " + (String)row.get(3) + " ws: " + (Integer)row.get(0));
							Product.Stage stage = pro.getStage((String)row.get(3));
							stage.assignWorkStation( (Integer)row.get(0) );
							stage.assignWorker( repos.getWorkerID( (String)row.get(4)) );						
						}
						
						if(row.indexOf(null,6)==-1) {
							Product pro = repos.getProduct((String) row.get(8));
							Product.Stage stage = pro.getStage((String)row.get(9));
							System.out.println("ws: " + (Integer)row.get(6));
							stage.assignWorkStation( (Integer)row.get(6) );
							stage.assignWorker( repos.getWorkerID( (String)row.get(10)) );
						}						
					}
						
					((ProductMonitor)tabbedPanel.getComponentAt(2)).updateStages();
					MainWindow.this.startMonitoring();
					for(int i=1; i<=40; i++) {
						SqlServerUtil.sendMessage(i, "Worker ID = 0");
					}
					
				}
			});
			
			btnStop.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					MainWindow.this.stopMonitoring();
				}
			});
		}
	        
	    private void setColumnPreferredWidth() {
	    	tableView.getColumnModel().getColumn(0).setPreferredWidth(40);
			tableView.getColumnModel().getColumn(1).setPreferredWidth(40);
			tableView.getColumnModel().getColumn(2).setPreferredWidth(90);
			tableView.getColumnModel().getColumn(3).setPreferredWidth(70);
			tableView.getColumnModel().getColumn(4).setPreferredWidth(60);
			tableView.getColumnModel().getColumn(5).setPreferredWidth(40);
			tableView.getColumnModel().getColumn(6).setPreferredWidth(40);
			tableView.getColumnModel().getColumn(7).setPreferredWidth(40);
			tableView.getColumnModel().getColumn(8).setPreferredWidth(90);
			tableView.getColumnModel().getColumn(9).setPreferredWidth(70);
			tableView.getColumnModel().getColumn(10).setPreferredWidth(60);
			tableView.getColumnModel().getColumn(11).setPreferredWidth(40);	
	    }
	    
	    private void updateTable() {
	    	((DefaultTableModel)tableView.getModel()).setDataVector(modelData, tableColumns);
			setColumnPreferredWidth();
	    }
		
		private Integer[] getStationList() {
			Integer[] stations = new Integer[40];
			for(int i=0; i<40; i++) 
				stations[i] = i+1;
			return stations;
		}
	    
	    private String[] getSeriesList(String folderPath) {
//	    	System.out.println(folderPath);
	    	File folder = new File(folderPath);
			if(!folder.isDirectory()) {
//				System.out.println("Folder needed instead!");
				return null;
			} 
			
			File[] files = folder.listFiles();
			for(File f : files) {
				String path = f.getAbsolutePath();
				int length = path.length();
				if(length>4 && path.substring(length-4, length).equalsIgnoreCase(".csv")) {
				    ProductCSVReader proReader = new ProductCSVReader(path);
				    proSeries.add(proReader);
				}
			}
			
			String[] seriesList = new String[proSeries.size()];
			for(int i=0; i<proSeries.size(); i++)
				seriesList[i] = proSeries.get(i).getSeriesName();
			return seriesList;
		}
		
		private void comboxResetItems(JComboBox<String> combox, List<String> list) {
			//remove will also fire the action listener
			if(combox.getItemCount()>0)
				combox.removeAllItems();
			if(list==null) {
//				System.out.println(list);
				return;
			}
			for(String s : list) {
				combox.addItem(s);
			}
			combox.setSelectedIndex(0);
//			System.out.println(list.toString());
		}
		
		@Override
		public void updateDisplay() {
			// TODO Auto-generated method stub
			
		}
	      
	    private class MyTableModel extends DefaultTableModel {
			
	    	private static final long serialVersionUID = 1L;		
			
	    	private MyTableModel() {
				tableColumns.add("Station");
				tableColumns.add("Series");
				tableColumns.add("Product");
				tableColumns.add("Stage");
				tableColumns.add("Worker");
				tableColumns.add("Active");
				tableColumns.add("Station");
				tableColumns.add("Series");
				tableColumns.add("Product");
				tableColumns.add("Stage");
				tableColumns.add("Worker");
				tableColumns.add("Active");
				
				for(int i=0; i<20; i++) {
					Vector<Object> row = new Vector<Object>(12);
					row.setSize(12);
					row.set(0, Integer.valueOf(i+1));
					row.set(5, Boolean.FALSE);
					row.set(6, Integer.valueOf(i+21));
					row.set(11, Boolean.FALSE);
					modelData.add(row);
				}				
			}

			@Override
			public int getRowCount() {
				return modelData.size();
			}

			@Override
			public int getColumnCount() {
				return tableColumns.size();
			}
			
			@Override
			@SuppressWarnings({ "unchecked", "rawtypes" })
			public Class getColumnClass(int columnIndex) {
				if(columnIndex==5 || columnIndex==11)
					return Boolean.class;
				return String.class;
			}
			
			@Override
			public String getColumnName(int column) {
				return tableColumns.get(column);
			}

			@Override
			public Object getValueAt(int rowIndex, int columnIndex) {
				return modelData.get(rowIndex).get(columnIndex);
			}	
			
			public boolean isCellEditable(int row, int col) {
	            if(col==5 || col==11)
	            	return true;
	            return false;
	        }
	        
	        public void setValueAt(Object value, int row, int col) {
	        	modelData.get(row).setElementAt(value,col);
	            fireTableCellUpdated(row, col);
	        }	    	
	    }
	}

	private class AverageEfficiencyPanel extends JPanel implements RealTimeDisplay{

		private static final long serialVersionUID = 1L;
		
		private XChartPanel chartPanel;
		private Chart chart;
		
		private boolean autoMode = true;
		private boolean showIndividual = false;
		
		private long startTime;
		private long endTime;
		
		private JSlider startSlider;
		private JSlider endSlider;
		
		private JComboBox<String> workerCombox;
		
		public AverageEfficiencyPanel() {
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
						
			chartPanel = buildChartPanel();
			startTime = 0;
			endTime = Long.MAX_VALUE;
			
			JPanel controlPanel = new JPanel();
			controlPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
			
			JCheckBox indi = new JCheckBox("Show individual", false);
			indi.setFont(new Font("Serif", Font.PLAIN, 14));
			indi.setEnabled(true);
						
			String[] workList = new String[repos.getWorkers().size()];
//			for(int i=0; i<workList.length; i++) {
//				workList[i] = repos.getWorkers().get(i).getID() + "";
//			}
			int i = 0;
			for(Worker w : repos.getWorkers()){
				workList[i] = w.getName();
				i++;
			}
			workerCombox = new JComboBox<String>(workList);
			workerCombox.setMinimumSize(new Dimension(65, 16));
			workerCombox.setMaximumSize(new Dimension(70, 16));
			
			workerCombox.setEditable(false);
			workerCombox.setEnabled(false);
			workerCombox.setSelectedIndex(0);
			
			JCheckBox autoModeBox = new JCheckBox("Full time");
			autoModeBox.setFont(new Font("Serif", Font.PLAIN, 14));
			
			controlPanel.add(indi);
			controlPanel.add(workerCombox);
			controlPanel.add(autoModeBox);
			
			JLabel stL = new JLabel("Start time");
			JLabel etL = new JLabel("End time");
			stL.setFont(new Font("Serif", Font.PLAIN, 14));
			etL.setFont(new Font("Serif", Font.PLAIN, 14));
			
			startSlider = new JSlider(0, 100);
			endSlider = new JSlider(0, 100);
			startSlider.setMinorTickSpacing(1);
			startSlider.setValue(0);
			endSlider.setMinorTickSpacing(1);
			endSlider.setValue(100);
			
			controlPanel.add(stL);
			controlPanel.add(startSlider);
			controlPanel.add(etL);
			controlPanel.add(endSlider);
			
			autoModeBox.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					JCheckBox check = (JCheckBox) e.getSource();
					autoMode = check.isSelected();
					startSlider.setEnabled(!autoMode);
					endSlider.setEnabled(!autoMode);
					startSlider.setFocusable(!autoMode);
					endSlider.setFocusable(!autoMode);					
					startSlider.setValue(0);
					endSlider.setValue(100);
					
					if(autoMode) {
						startTime = Long.MIN_VALUE;
						endTime = Long.MAX_VALUE;						
					}
					
					updateDisplay();
				}
			});
			autoModeBox.setSelected(true);

			indi.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent event) {
					JCheckBox check = (JCheckBox) event.getSource();
					showIndividual = check.isSelected();
					workerCombox.setEnabled(showIndividual);
					updateDisplay();
				}
			});
			
			workerCombox.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					updateDisplay();
				}
			});
									
			ChangeListener cl = new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent e) {
					if(autoMode) return;
					else updateDisplay();
				}
			};
			
			startSlider.addChangeListener(cl);
			endSlider.addChangeListener(cl);
		
			add(chartPanel);
			add(controlPanel);
			
		}
		
		private XChartPanel buildChartPanel() {
			Chart chart = new Chart(800,600); 

		    DateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
			chart.setChartTitle("Average Efficiency " + dft.format(new Date()));
//		    chart.setXAxisTitle("time");
//		    chart.setYAxisTitle("average efficiency");
			
			StyleManager style = chart.getStyleManager();
//		    style.setPlotBackgroundColor(ChartColor.getAWTColor(ChartColor.GREY));
		    style.setPlotGridLinesColor(new Color(255, 255, 255));
		    style.setChartBackgroundColor(Color.WHITE);
		    //style.setLegendBackgroundColor(Color.PINK);
		    style.setLegendVisible(false);
		    style.setChartFontColor(Color.BLACK); //title color
		    style.setChartTitleBoxBackgroundColor(Color.WHITE);
		    style.setChartTitleBoxVisible(true);
		    //style.setChartTitleBoxBorderColor(Color.BLACK);
		    style.setPlotGridLinesVisible(true);

		    style.setAxisTickPadding(5);
		    style.setAxisTickMarkLength(5);
		    style.setPlotPadding(0);
		    
		    style.setMarkerSize(5);

		    style.setChartTitleFont(new Font("Serif", Font.PLAIN, 20));
//		    style.setLegendFont(new Font("Serif", 0, 18));
//		    style.setLegendPosition(StyleManager.LegendPosition.InsideSE);
//		    style.setLegendSeriesLineLength(12);
		    style.setAxisTitleFont(new Font("SansSerif", 2, 16));
		    style.setAxisTickLabelsFont(new Font("Serif", 0, 12));
		    style.setDatePattern("HH:mm");
		    style.setDecimalPattern("#0.000");
		    style.setLocale(Locale.getDefault());

		    List<Date> xData = repos.getXDate();
			List<Float> yData = repos.getYEfficiency();			
		    
		    //add dummy data
		    DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S"); 
			Date time = null;
			try {
				time = sdf.parse("2015-01-13 08:08:08.0");
			} catch (ParseException e) {
				e.printStackTrace();
			}
		    
		    xData.add(time);
			yData.add(0.5f);
				    
		    Series series = chart.addSeries("Efficiency Series", xData, yData);
		    series.setLineColor(SeriesColor.BLUE);
		    series.setMarkerColor(Color.ORANGE);
		    series.setMarker(SeriesMarker.CIRCLE);
		    series.setLineStyle(SeriesLineStyle.SOLID);
		 
		    xData.clear(); yData.clear();
		    
			this.chart = chart;
		    return new XChartPanel(chart);
		}

		@Override
		public Dimension getPreferredSize() {
			return new Dimension(600,400);
		}
		
		private void calculateTimeInterval() {
			List<Date> date = repos.getXDate(); 
			long startTime = date.get(0).getTime();
			long endTime = ((LinkedList<Date>)date).getLast().getTime();
			
			this.startTime = startTime + startSlider.getValue() * (endTime-startTime) / 100;
			this.endTime = this.startTime + endSlider.getValue() * (endTime-this.startTime) / 100;
		}
		
		@Override
		public void updateDisplay() {		
			if(repos.getXDate().isEmpty()) return;
			if(!autoMode) calculateTimeInterval();
			
			System.out.println("MainWindow.updateDisplay(): time " + startTime + " " + endTime);
			
			List<Date> date = new LinkedList<Date>();
			List<Float> effi = new LinkedList<Float>();
			
			if(showIndividual && !((String) workerCombox.getSelectedItem()).equals("Work ID") ) {
				int id = Integer.valueOf( (String)workerCombox.getSelectedItem() );
				Worker worker = repos.getWorker(id);
				List<Date> individualDate = worker.getTimelineList();
				List<Float> individualEffi = worker.getEfficiencyList();
				
				for(int i=0; i<individualDate.size(); i++) {
					long time = individualDate.get(i).getTime();
					if(time>=startTime && time<=endTime) {
						date.add(new Date(time));
						effi.add(new Float(individualEffi.get(i)));
					}					
				}	
				
				chart.setChartTitle(repos.getWorkerName(id) + " Efficiency "
						+ new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
			}else{
				for(int i=0; i<repos.getXDate().size(); i++) {
					long time = repos.getXDate().get(i).getTime();
					if(time>=startTime && time<=endTime) {
						date.add(new Date(time));
						effi.add(new Float(repos.getYEfficiency().get(i)));
					}					
				}	
				
				chart.setChartTitle("Average Efficiency "
						+ new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
			}
			
			if(date.size()>0) //otherwise it will stop the program
				chartPanel.updateSeries("Efficiency Series", date, effi);
			
//			synchronized (repos) {
//				int fromIndex = 0;
//				int toIndex = repos.getXDate().size()-1;
//				for(int i=0; i<repos.getXDate().size(); i++) {
//					if(this.startTime>repos.getXDate().get(i).getTime())
//						fromIndex++;
//					else
//						break;
//				}
//				for(int i=toIndex; i>=0; i--) {
//					if(this.endTime<repos.getXDate().get(i).getTime())
//						toIndex--;
//					else
//						break;
//				}
//				date = repos.getXDate().subList(fromIndex, toIndex+1);
//				effi = repos.getYEfficiency().subList(fromIndex, toIndex+1);
//			}
			
		}
	}

	public class IndividualEfficiencyPanel extends JPanel implements RealTimeDisplay {

		private static final long serialVersionUID = 1L;
		private JTable tableView;
		private MyTableModel tableModel;
				
		private final Vector<String> columnNames = new Vector<>(4);
		private final Vector<Vector<Object>> modelData = new Vector<>();
		
		private IndividualEfficiencyPanel popupPanel = null;
		
		private final DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            private static final long serialVersionUID = 1L;

			@Override
            public Component getTableCellRendererComponent(JTable table, Object
                value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);
                setForeground(Color.blue);
                setFont(new Font("Serif",Font.PLAIN, 18));
//              if((Float)(table.getModel().getValueAt(row, 2))<0.6f) 
//              	setForeground(Color.red);
                setHorizontalAlignment(JLabel.CENTER);
                return this;
            }
        };
		
		public IndividualEfficiencyPanel() {
			tableModel = new MyTableModel();
			tableView = new JTable(tableModel);
			tableView.setFillsViewportHeight(true);
//			tableView.setFont(new Font("Serif", Font.PLAIN, 16));
			tableView.setRowHeight(40);
			
//			tableView.setBounds(5, 5, this.getWidth(), this.getHeight());
//			tableView.setAutoCreateRowSorter(true);
			setRenderer();
				        
			JScrollPane scrollPane = new JScrollPane(tableView);
			
			tableView.getTableHeader().setFont(new Font("Serif",Font.PLAIN,18));
			tableView.getTableHeader().setPreferredSize(new Dimension(scrollPane.getWidth(), 40));
//			tableView.getTableHeader().setAlignmentY(SwingConstants.CENTER);

			this.setLayout(null);
			
			JLabel logo = new JLabel(new ImageIcon(ICON_PATH + "logo_mini.png"));
			logo.setBounds(130, 20, 217, 65);
			logo.setForeground(getBackground());
			this.add(logo);
			
			int height = ( repos.getWorkers().size()/2 +  repos.getWorkers().size()%2 ) * 40 + 42;
			scrollPane.setBounds(150, 100, 1000, height);
			this.add(scrollPane);
			
			final JButton btnPop = new JButton("Detach");
			btnPop.setFont(new Font("Serif",Font.PLAIN,16));
			btnPop.setBounds(1050, scrollPane.getLocation().y + scrollPane.getSize().height + 30, 100, 30);
			this.add(btnPop);
			btnPop.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(final ActionEvent e) {
					// TODO Auto-generated method stub					
					SwingUtilities.invokeLater(new Runnable() {		
						@Override
						public void run() {
							final JFrame frame = new JFrame("Public Display @ Individual Efficiency");
//							frame.setBounds(200, 30, 800, 600);
							popupPanel = new IndividualEfficiencyPanel();
							popupPanel.setPreferredSize(getSize());
							Component btn = popupPanel.getComponent((popupPanel.getComponentCount()-1));
							popupPanel.remove(btn); 
							frame.add(popupPanel);
							frame.pack();
//							frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
							frame.setVisible(true);		
							
//							btnPop.setEnabled(false);							
							frame.addWindowListener(new WindowAdapter() {
								public void windowClosed(WindowEvent e) {
									frame.dispose();
									popupPanel = null;
//									btnPop.setEnabled(true);
								}
							});
						}
					});	
				}
			});
		}
		
		private void setRenderer() {
			for(int i=0; i<columnNames.size(); i++) {
				tableView.getColumnModel().getColumn(i).setCellRenderer(renderer);
			}
		}
		
		//retrieve data from the repository and update
		@Override
		public void updateDisplay() {
			tableModel.updateTable();
			if(popupPanel!=null)
				popupPanel.tableModel.updateTable();
		}
		
		private class MyTableModel extends DefaultTableModel {
			
			private static final long serialVersionUID = 1L;
			
			private MyTableModel() {				
				columnNames.add("Rank");
				columnNames.add("Name");
				columnNames.add("Efficiency");
				columnNames.add("Rank");
				columnNames.add("Name");
				columnNames.add("Efficiency");
				
				updateModel();
			}
			
			public int getColumnCount() {
	            return columnNames.size();
	        }
	 
	        public int getRowCount() {
	            return modelData==null?0:modelData.size();
	        }
	 
	        public String getColumnName(int col) {
	            return columnNames.get(col);
	        }
	 
	        public Object getValueAt(int row, int col) {
	        	return modelData.get(row).get(col);
	        }
	 
	        @SuppressWarnings({ "unchecked", "rawtypes" })
			public Class getColumnClass(int c) {
//	            return getValueAt(0, c).getClass();
	        	if(c==0 || c==3) return Integer.class;
	        	else return String.class;
	        }
	 
	        public boolean isCellEditable(int row, int col) {
	            return false;
	        }
	        
	        public void setValueAt(Object value, int row, int col) {
	        	modelData.get(row).setElementAt(value,col);
//	        	System.out.println("Updating Cell");
	            fireTableCellUpdated(row, col);
	        }
	        
	        private void updateTable(){
	        	updateModel();
	        	setDataVector(modelData, columnNames);
	        	setRenderer();
	        }
	        
	        private void updateModel(){
	        	modelData.clear();
	        	for(Worker w : repos.getWorkers()){
	        		Vector<Object> row = new Vector<>(3);
	        		row.setSize(3);
//	        		row.add(w.getID());
	        		row.set(1,w.getName());
//	        		row.add(w.getWorkStateString());
//	        		row.add(Math.random());
	        		row.set(2,w.getAvgEfficiency());
	        		modelData.add(row);
	        		
//	        		row.clear(); //will free the data referenced by all collections
	        	}
	        	
	        	Collections.sort(modelData, new Comparator<Vector<Object>>(){
					@Override
					public int compare(Vector<Object> o1, Vector<Object> o2) {
						//only 2 elements added at the moment
						//descending order
						return ((Float)o2.get(2)).compareTo((Float)o1.get(2));
					}        		
	        	});
	        	
	        	for(int i=1; i<=modelData.size(); i++) {
	        		modelData.get(i-1).set(0, Integer.valueOf(i));
	        		float f = (float) modelData.get(i-1).get(2);
	        		int ff = (int) (f * 1000);
	        		modelData.get(i-1).set(2,ff/10+"."+ff%10+"%");        		
	        	}
	        	
	        	int halfSize = modelData.size()/2;
	        	int remainder = modelData.size()%2;
	        	for(int i=0; i<halfSize; i++) {
	        		modelData.get(i).addAll(modelData.get(i+halfSize+remainder));
	        	}        	
	        	modelData.setSize(halfSize);
	        }    	
		}
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
