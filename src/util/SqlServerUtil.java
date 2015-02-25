package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class SqlServerUtil {
	
	static {
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			System.out.println("driver loaded!");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private static final Connection con = createConnection();
	
	public SqlServerUtil() {
		System.out.println("constructor");
	}
	
	private static Connection createConnection() {
		Connection con = null;
		try {
			String conString = "jdbc:sqlserver://localhost\\MSSQLSERVER;" +
							   "databaseName=PLDC;" +     //windows authentication
							   "integratedSecurity=true"; //driver + server + instance + db
			con = DriverManager.getConnection(conString); 
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("connected successfully!");
		return con;
	}

	public static ResultSet getData(String table, int ID) {
		PreparedStatement preStat = null;
		ResultSet res = null;
		String sql = "select * from " + table + " where ID > ? AND ID <= ?";
		try {
			preStat = con.prepareStatement(sql);
//			preStat.setString(1, table);
			preStat.setInt(1, ID);
			preStat.setInt(2, ID+20);
			res = preStat.executeQuery();
        } catch (SQLException e) {
			e.printStackTrace();
		}	
		return res;
	}
	
	public static ResultSet getWorkers() {
		PreparedStatement preStat = null;
		ResultSet res = null;
		String sql = "select * from Workers";
		try {
			preStat = con.prepareStatement(sql);
			res = preStat.executeQuery();
        } catch (SQLException e) {
			e.printStackTrace();
		}	
		return res;
	}
	
	public static Map<String,String> getAllUsers() {
		ResultSet res = null;
		Map<String,String> users = new HashMap<String,String>();
		try {
			Statement state = con.createStatement();
			res = state.executeQuery("Select * from UserData");
            while(res.next()) {
            	users.put(res.getString(2), res.getString(3));
            }
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		for (Map.Entry<String, String> entry : users.entrySet())
        {
              System.out.println(entry.getKey() + " " + entry.getValue());
        }
		return users;
	}
	
	public static ResultSet getAll() {
		ResultSet res = null;
		try {
			Statement state = con.createStatement();
			res = state.executeQuery("Select * from UserData");           
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return res;
	}
	
	public static void addUser(String user, String mail) {
		PreparedStatement preStat = null;
		String sql = "INSERT INTO UserData (Username, Email) VALUES (?,?)" ;
		try {
			preStat = con.prepareStatement(sql);
			preStat.setString(1,user);
			preStat.setString(2,mail);
			preStat.executeUpdate();;	
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
	
	public static void setScore(String user, int score) {
		PreparedStatement preStat = null;
		String sql = "UPDATE UserData SET Score = ? WHERE Username = ? " ;
		try {
			preStat = con.prepareStatement(sql);
			preStat.setInt(1,score);
			preStat.setString(2,user);
			preStat.executeUpdate();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

}
