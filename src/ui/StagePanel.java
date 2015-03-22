package ui;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;

import javax.swing.JLabel;

import model.Product;
import model.Repository;
import model.Worker;

import javax.swing.JButton;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;

import javax.swing.JTextField;

import util.SqlServerUtil;

public class StagePanel extends JPanel{
	
	private static final long serialVersionUID = 1L;

	private static final String ICON_PATH = System.getProperty("user.dir") + "/res/icons/";
	private static final Icon YELLOW_ICON = new ImageIcon(ICON_PATH+"yi.png");
	private static final Icon GREEN_ICON = new ImageIcon(ICON_PATH+"gi.png");
	private static final Icon RED_ICON = new ImageIcon(ICON_PATH+"ri.png");
	
	private Product.Stage stage;
	private JLabel lblStageName, lblWorkname, lblStdtime, accuStock, completionTime, bom, workerIcon, timeIcon;
	private JTextField textField;
	
	public StagePanel(int index, Product.Stage stage) {
//		setBorder(new LineBorder(Color.LIGHT_GRAY));
		setPreferredSize(new Dimension(130, 160));
		TitledBorder border = BorderFactory.createTitledBorder("Station "+index);
		border.setTitleJustification(TitledBorder.CENTER);
		setBorder(border);
		
		this.stage = stage;
		
		setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("5px"),
				ColumnSpec.decode("85px:grow"),
				ColumnSpec.decode("30px:grow"),
				FormFactory.GLUE_COLSPEC,},
			new RowSpec[] {
				FormFactory.LINE_GAP_ROWSPEC,
				RowSpec.decode("14dlu"),
				FormFactory.NARROW_LINE_GAP_ROWSPEC,
				RowSpec.decode("16px"),
				FormFactory.NARROW_LINE_GAP_ROWSPEC,
				RowSpec.decode("16px"),
				FormFactory.NARROW_LINE_GAP_ROWSPEC,
				RowSpec.decode("16px"),
				FormFactory.NARROW_LINE_GAP_ROWSPEC,
				RowSpec.decode("16px"),
				FormFactory.NARROW_LINE_GAP_ROWSPEC,
				RowSpec.decode("16dlu"),}));
		
		lblStageName = new JLabel("Stage code");
		lblStageName.setToolTipText("Stage code");
		add(lblStageName, "2, 2, fill, center");
		
		lblWorkname = new JLabel("Worker name");
		lblWorkname.setToolTipText("Worker name");
		add(lblWorkname, "2, 4, fill, center");
		
		workerIcon = new JLabel(RED_ICON);
		add(workerIcon, "3, 4");
		
		lblStdtime = new JLabel("Standard time");
		lblStdtime.setToolTipText("Standard time in second");
		add(lblStdtime, "2, 6, fill, center");
		
		timeIcon = new JLabel(RED_ICON);
		add(timeIcon, "3, 6");
		
		bom = new JLabel("BOM");
		bom.setToolTipText("Click to see the details if this station is already assigned");
		bom.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		bom.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(StagePanel.this.stage!=null)
					popupBOM(e.getXOnScreen(), e.getYOnScreen());
			}
		});
		add(bom, "2, 8, fill, fill");
		
//		mtrIcon = new JLabel(RED_ICON);
//		add(mtrIcon, "3, 8");
		
		accuStock = new JLabel("Pro. Amount 0");
		accuStock.setToolTipText("Accumulated stock");;
//		accuStock.setHorizontalAlignment(SwingConstants.CENTER);
		add(accuStock, "2, 10, fill, center");
		
		completionTime = new JLabel("N");
		completionTime.setToolTipText("Last completion time");
//		completionTIme.setAlignmentY(JLabel.CENTER_ALIGNMENT);
//		completionTIme.setText("<html><div style=\"text-align: center;\">8 Sec</html>");
		completionTime.setHorizontalAlignment(SwingConstants.CENTER);
		add(completionTime, "3, 10, fill, center");

		textField = new JTextField();
		add(textField, "2, 12, fill, default");
//		textField.setColumns(10);

		JButton btnSend = new JButton("Send");
		add(btnSend, "3, 12");
		btnSend.setMargin(new Insets(0, 0, 0, 0));
		btnSend.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(StagePanel.this.stage == null) return;
				String msg = textField.getText();
				int ws = StagePanel.this.stage.getWorkStation();
				SqlServerUtil.sendMessage(ws, msg);
			}
		});

	}
	
	public void updateDisplay() {
		if(stage==null) return;
		
		lblStageName.setText(stage.getCode());
		Worker w = Repository.getInstance().getWorker(stage.getWorkerID());
		lblWorkname.setText(w.getName()); 
		
		lblStdtime.setText(stage.getStandardTime()/1000 + " Sec");
		float effi = w.getAvgEfficiency();
		if(effi<0.7)
			timeIcon.setIcon(RED_ICON);
		else if( w.getAvgEfficiency()>1.3f)
			timeIcon.setIcon(YELLOW_ICON);
		else
			timeIcon.setIcon(GREEN_ICON);
		
//		double r = Math.random();
//		timeIcon.setIcon(r>0.5?GREEN_ICON:RED_ICON);
		
		if(w.isOnWork() ) { // && w.equals( Repository.getInstance().getLastWorkerAtStation(stage.getWorkStation()) )
			workerIcon.setIcon(GREEN_ICON);
		}else{
			workerIcon.setIcon(RED_ICON);
		}
		
		accuStock.setText("Pro. Amount " + w.getCompletedStages());
		long t = w.getLastCompletionTime();
		if(t>0) {
			completionTime.setText( t/1000 + "S");
		}else{
			completionTime.setText( "N" );
		}
		
	}
	
	public Product.Stage getStage() {
		return stage;
	}
	
	public void setStage(Product.Stage stage) {
		this.stage = stage;
	}
	
	private void popupBOM(final int x, final int y) {
		Worker w = Repository.getInstance().getWorker(stage.getWorkerID());
		if(w.getScannedMaterials().isEmpty()) return;
		
		String[] columns = {"Material", "Stock"};
		Map<String,Float> bomMap = stage.getBOM();
		String[][] bomInventory = new String[bomMap.size()][2]; 
		int i = 0;
		for(String s: bomMap.keySet()) {
			bomInventory[i][0] = s;
			int scannedSource = w.getScannedMaterials().get(s);
			float consumption = w.getCompletedStages() * stage.getBOM().get(s);
			bomInventory[i][1] = scannedSource - consumption + ""; 
			i++;
		}
		
		JTable table = new JTable(bomInventory, columns);
		table.getColumnModel().getColumn(0).setPreferredWidth(150);
		table.getColumnModel().getColumn(0).setPreferredWidth(50);		
		final JScrollPane pane = new JScrollPane(table);
		
		SwingUtilities.invokeLater(new Runnable() {		
			@Override
			public void run() {
				JFrame f = new JFrame("BOM");
				f.getContentPane().add(pane);
				f.setBounds(x,y,200,200);
				f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				f.setVisible(true);
			}
		});
		
	}
	
	public void setMessage(String msg) {
		textField.setText(msg);
	}

}
