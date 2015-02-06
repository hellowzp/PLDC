package ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import javax.swing.SwingUtilities;

public class TableTest extends JFrame{
	
	private static final long serialVersionUID = 1L;
	
	//�������
	private JTable table;
	private DefaultTableModel model;
	private JButton deleteButton;
	private JPanel panel;
	
	public TableTest() {
		
		//��ʼ�����
		panel = new JPanel();
		String[] columnNames = {"���","�û���","����"};
		String[][]data={{"1","zhangsan","123456"},{"2","lisi","4567"},{"3","lisi","4567"}};
		model = new DefaultTableModel(data, columnNames);
		table = new JTable(model);
		deleteButton = new JButton("ɾ��");
		panel = new JPanel();
		JScrollPane scrollPane = new JScrollPane(table);
		
		//������
		panel.add(scrollPane,BorderLayout.CENTER);
		panel.add(deleteButton,BorderLayout.SOUTH);
		
		this.add(panel);
		//���ô��ڵĻ�������.
		this.setVisible(true);
		this.setSize(500,500);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//����¼�������
		deleteButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				//��ȡҪɾ������,û��ѡ����-1
				int row = table.getSelectedColumn();
				if(row == -1){
					JOptionPane.showMessageDialog(TableTest.this,"��ѡ��Ҫɾ������!");
				}else{
					model.removeRow(row-1);
				}
			}
		});
	}

public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				new TableTest();
			}
		});
	}
}
