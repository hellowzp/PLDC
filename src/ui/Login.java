package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import util.SqlServerUtil;

import java.awt.Font;
import java.awt.Insets;
import java.util.Map;


public class Login extends JFrame {
	private static final long serialVersionUID = 1L;
	private JTextField nameField;
	private JPasswordField passwordField;
	
	private Map<String, String[]> users = SqlServerUtil.getAllUsers();

	public Login() {
		setTitle("DataMatic User Motivation System");
		getContentPane().setLayout(null);
		
		setBounds(500, 200, 400, 300);
		
		JLabel logo = new JLabel(new ImageIcon("res/icons/logo_mini.png"));
		logo.setBounds(80, 32, 218, 65);
		getContentPane().add(logo);
		
		JLabel lblUserName = new JLabel("User name");
		lblUserName.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblUserName.setBounds(80, 116, 71, 18);
		getContentPane().add(lblUserName);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblPassword.setBounds(80, 145, 71, 18);
		getContentPane().add(lblPassword);
		
		nameField = new JTextField();
		nameField.setBounds(161, 117, 141, 20);
		getContentPane().add(nameField);
		nameField.setColumns(10);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(161, 146, 141, 20);
		getContentPane().add(passwordField);
		
		JButton btnAdmin = new JButton("Administrator");
		btnAdmin.setMargin(new Insets(1,0,1,0));
		btnAdmin.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnAdmin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String name = nameField.getText();
				String[] uinfo = users.get(name);
				if(uinfo==null) {
					JOptionPane.showMessageDialog(Login.this, "This user name does not exist yet.");
				}else{
					String pwd = new String(passwordField.getPassword());
					if( pwd.equals( uinfo[0])) {
						if(uinfo[1].equals("1")) {
							JOptionPane.showMessageDialog(Login.this, "OK");
						}else{
							JOptionPane.showMessageDialog(Login.this, "You don't have administration permission");
						}
					}else{
						JOptionPane.showMessageDialog(Login.this, "Wrong password");
					}
				}		
			}
		});
		btnAdmin.setBounds(80, 185, 109, 25);
		getContentPane().add(btnAdmin);
		
		JButton btnLogIn = new JButton("Log in");
		btnLogIn.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnLogIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String name = nameField.getText();
				String[] uinfo = users.get(name);
				if(uinfo==null) {
					JOptionPane.showMessageDialog(Login.this, "This user name does not exist yet.");
				}else{
					String pwd = new String(passwordField.getPassword());
					if( pwd.equals( uinfo[0])) {
						int auth = Integer.valueOf(uinfo[1]);
						Login.this.dispose();
						new MainWindow(5000, auth);
					}else{
						JOptionPane.showMessageDialog(Login.this, "Wrong password");
					}
				}				
			}
		});
		btnLogIn.setBounds(205, 185, 100, 25);
		getContentPane().add(btnLogIn);
		
		setVisible(true);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
}
