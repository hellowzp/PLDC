package ui;

import model.DataProcessor;
import model.Repository;
import model.Worker;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.sun.xml.internal.messaging.saaj.util.transform.EfficientStreamingTransformer;
import com.xeiam.xchart.Chart;
import com.xeiam.xchart.Series;
import com.xeiam.xchart.SeriesColor;
import com.xeiam.xchart.SeriesLineStyle;
import com.xeiam.xchart.SeriesMarker;
import com.xeiam.xchart.StyleManager;
import com.xeiam.xchart.XChartPanel;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import util.CSVFileChooserFilter;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

public class MainWindow extends JFrame implements RealTimeDisplay {
	
	private static final long serialVersionUID = 1L;
	
	private JTabbedPane tabbedPanel;
	private Repository repos = Repository.getInstance();
	
	private long period;
	private final Timer timer = new Timer(true);
	private final TimerTask task = new TimerTask() {
		final DataProcessor pro = new DataProcessor("pldc170");
		@Override
		public void run() {
			pro.processData();
			updateDisplay();				
		}		
	};
	
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

	public MainWindow(long period) {		
		tabbedPanel = new JTabbedPane(JTabbedPane.TOP);
		getContentPane().add(tabbedPanel, BorderLayout.CENTER);
		
		tabbedPanel.addTab("Average Efficiency", new AverageEfficiencyPanel());		
		tabbedPanel.addTab("Individual Efficiency", new IndividualEfficiencyPanel());		
		tabbedPanel.addTab("Product", new ProductMonitorPanel());
		
		tabbedPanel.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JTabbedPane sourcePanel = (JTabbedPane) e.getSource();
				int index = sourcePanel.getSelectedIndex(); //starting from 0
				System.out.println(sourcePanel.getComponentAt(index));
				((RealTimeDisplay)sourcePanel.getComponentAt(index)).updateDisplay();
			}
		});
		
		this.period = period;		
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnOption = new JMenu("Option");
		menuBar.add(mnOption);
		
		JMenuItem mntmUpdateProducts = new JMenuItem("Update products");
		mntmUpdateProducts.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("updating products");
				JFileChooser fc = new JFileChooser(System.getProperty("user.dir"));
				fc.setAcceptAllFileFilterUsed(false);
				fc.addChoosableFileFilter(new CSVFileChooserFilter());
				fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				fc.setMultiSelectionEnabled(true);
				int selection = fc.showOpenDialog(fc);
				if(selection==JFileChooser.APPROVE_OPTION) {
					File[] files = fc.getSelectedFiles();
					System.out.println(files.length);
				}
			}
		});
		mnOption.add(mntmUpdateProducts);
		
		JMenu mnAbout = new JMenu("About");
		menuBar.add(mnAbout);
	}
		
	public long getPeriod() {
		return period;
	}

	public void setPeriod(long period) {
		this.period = period;
	}
	
	@Override
	public void updateDisplay(){
		int index = tabbedPanel.getSelectedIndex();
		((RealTimeDisplay)tabbedPanel.getComponentAt(index)).updateDisplay();
	}
	
	public void startUpdating(){
		timer.schedule(task, 1000, period);		
	}
	
	public void stopUpdating() {
		timer.cancel();
	}

	public class AverageEfficiencyPanel extends JPanel implements RealTimeDisplay{

		private static final long serialVersionUID = 1L;		
		private XChartPanel chartPanel;		
		private long startTime;
		private long endTime;
		
		public AverageEfficiencyPanel() {
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
						
			chartPanel = buildChartPanel();
			startTime = 0;
			endTime = System.currentTimeMillis();
			
			JPanel controlPanel = new JPanel();
			controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.X_AXIS));
				
			JLabel stL = new JLabel("Start time");
			JLabel etL = new JLabel("End time");
			final JSlider st = new JSlider(0, 100);
			final JSlider et = new JSlider(0, 100);
			controlPanel.add(stL);
			controlPanel.add(st);
			controlPanel.add(etL);
			controlPanel.add(et);
			st.addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent evt) {					
					int start = st.getValue();
					int end = et.getValue();
					resetTimeInteval(start,end);
					updateDisplay();
				}
			});
			et.addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent evt) {
					updateDisplay();
				}
			});
			
			JButton btnSummarize = new JButton("Summarize");
			btnSummarize.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
//					summarize();
				}
			});
			controlPanel.add(btnSummarize);
		
			add(chartPanel);
			add(controlPanel);
			
		}
		
		private void resetTimeInteval(int startSlider, int endSlider) {
			List<Date> date = repos.getXDate(); 
			long startTime = date.get(0).getTime();
			long endTime = ((LinkedList<Date>)date).getLast().getTime();
			
			this.startTime = startTime + startSlider * (endTime-startTime) / 100;
			this.endTime = this.startTime + endSlider * (endTime-this.startTime) / 100;
			updateDisplay();					
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
		    
			return new XChartPanel(chart);
		}
		
		@Override
		public Dimension getPreferredSize() {
			return new Dimension(600,400);
		}
		
		@Override
		public void updateDisplay() {			
			int fromIndex = 0;
			int toIndex = repos.getXDate().size()-1;
			for(int i=0; i<repos.getXDate().size(); i++) {
				if(this.startTime>repos.getXDate().get(i).getTime())
					fromIndex++;
				else
					break;
			}
			for(int i=toIndex; i>=0; i--) {
				if(this.endTime<repos.getXDate().get(i).getTime())
					toIndex--;
				else
					break;
			}
			List<Date> date = repos.getXDate().subList(fromIndex, toIndex+1);
			List<Float> efficiency = repos.getYEfficiency().subList(fromIndex, toIndex+1);
			chartPanel.updateSeries("Efficiency Series", date, efficiency);
		}
		
	}

	public class IndividualEfficiencyPanel extends JPanel implements RealTimeDisplay {

		private static final long serialVersionUID = 1L;
		private JTable tableView;
		private MyTableModel tableModel;
		
		private final Vector<String> columnNames = new Vector<>(4);
		private final Vector<Vector<Object>> modelData = new Vector<>();
		
		private final DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            private static final long serialVersionUID = 1L;

			@Override
            public Component getTableCellRendererComponent(JTable table, Object
                value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);
                setForeground(Color.blue);
//              if((Float)(table.getModel().getValueAt(row, 2))<0.6f) 
//              	setForeground(Color.red);
                setHorizontalAlignment(JLabel.CENTER);
//                setFont(getFont().deriveFont(50f));
                return this;
            }
        };
		
		public IndividualEfficiencyPanel() {
			tableModel = new MyTableModel();
			tableView = new JTable(tableModel);
			tableView.setFillsViewportHeight(true);
			tableView.setFont(new Font("Serif", Font.PLAIN, 14));
//			tableView.setBounds(5, 5, this.getWidth(), this.getHeight());
//			tableView.setAutoCreateRowSorter(true);
			setRenderer();
				        
			JScrollPane scrollPane = new JScrollPane(tableView);
			scrollPane.setPreferredSize(new Dimension(600,400));
			
			this.add(scrollPane,BorderLayout.CENTER);
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
	        	if(c==2 || c==4) return Float.class;
	        	return String.class;
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
	        		Vector<Object> row = new Vector<>(4);
	        		row.add(w.getName());
	        		row.add(w.getEfficiency());
	        		modelData.add(row);	        		
//	        		row.clear(); //will free the data referenced by all collections
	        	}
	        	
	        	Collections.sort(modelData, new Comparator<Vector<Object>>(){
					@Override
					public int compare(Vector<Object> o1, Vector<Object> o2) {
						//only 2 elements added at the moment
						//descending order
						return ((Float)o2.get(1)).compareTo((Float)o1.get(1));
					}        		
	        	});
	        	
	        	for(int i=1; i<=modelData.size(); i++) {
	        		modelData.get(i-1).add(0, Integer.valueOf(i));
	        	}
	        	
	        	int halfSize = modelData.size()/2;
	        	int remainder = modelData.size()%2;
	        	for(int i=0; i<halfSize; i++) {
	        		modelData.get(i).addAll(modelData.get(i+halfSize+remainder));
	        	}        	
	        	modelData.setSize(halfSize+remainder);
	        }    	
		}
	}
	
	public class ProductMonitorPanel extends JPanel implements RealTimeDisplay{

		private static final long serialVersionUID = 1L;
		
		public ProductMonitorPanel(){
			
		}

		@Override
		public void updateDisplay() {
			
		}

	}
	
	public static void main(String[] args) {	
		
		final JFrame frame = new JFrame("Assignment");
		frame.setBounds(200, 50, 800, 640);
		frame.getContentPane().add(new FinalAssignment());
		frame.setVisible(true);
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
//		final MainWindow frame = new MainWindow(1000);
//		frame.setBounds(200, 30, 800, 600);
//		frame.pack();
//		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
//		
//		frame.repos.initProducts(System.getProperty("user.dir")+"/res/products.csv");
//	
//		SwingUtilities.invokeLater(new Runnable() {		
//			@Override
//			public void run() {
//				frame.setVisible(true);
//			}
//		});		
//		
//		frame.startUpdating();
	}
}
