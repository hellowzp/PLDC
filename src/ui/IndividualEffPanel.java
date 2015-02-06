package ui;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class IndividualEffPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTable tableView;
	private TableModel tableModel;
	
	public IndividualEffPanel() {
		tableModel = new MyTableModel();
		tableView = new JTable(tableModel);
		tableView.setFillsViewportHeight(true);
		
		JButton updateBtn = new JButton("Update");
		updateBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Vector<Vector<Object>> newDataVector = new Vector<>();
				Vector<Object> row1 = new Vector<>();
				row1.add("1");
				row1.add("Wang");
				row1.add("hiu");
				row1.add((float)Math.random());
				newDataVector.add(row1);
				newDataVector.add(row1);
				newDataVector.add(row1);
				((MyTableModel)tableModel).updateTable(newDataVector);
			}
		});
		
		JScrollPane scrollPane = new JScrollPane(tableView);
		
		this.add(scrollPane,BorderLayout.CENTER);
		this.add(updateBtn,BorderLayout.SOUTH);
	}
	
	
	private class MyTableModel extends DefaultTableModel {
		
		private static final long serialVersionUID = 1L;
		
		private Vector<String> columnNames = new Vector<>(4);
		private Vector<Vector<Object>> modelData = new Vector<>();
		
		private MyTableModel() {
//			super(dataVector,columnNames);
			System.out.println("customizing model");
			
			columnNames.add("Worker ID");
			columnNames.add("Worker Name");
			columnNames.add("Worker State");
			columnNames.add("Worker Efficiency");
			
			Vector<Object> row1 = new Vector<>();
			row1.add("1");
			row1.add("Wang");
			row1.add("Working");
			row1.add(0.99f);
			modelData.add(row1);
			modelData.add(row1);
			modelData.add(row1);
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
        	System.out.println("retrieving Cell");
        	return modelData.get(row).get(col);
        }
 
        @SuppressWarnings({ "unchecked", "rawtypes" })
		public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }
 
        public boolean isCellEditable(int row, int col) {
            return false;
        }
        
        public void setValueAt(Object value, int row, int col) {
        	modelData.get(row).setElementAt(value,col);
        	System.out.println("Updating Cell");
            fireTableCellUpdated(row, col);
        }
        
        private void updateTable(Vector<Vector<Object>> newData) {
    		modelData = newData;
        	setDataVector(newData, columnNames);
    		System.out.println("Updating");
    	}
    	
	}

}
