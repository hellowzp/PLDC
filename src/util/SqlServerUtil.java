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
		System.out.println("sql util constructor");
	}
	
	private static Connection createConnection() {
		Connection con = null;
		try {
			String conString = "jdbc:sqlserver://PLDC-PC\\SQLEXPRESS:1433;" +
							   "databaseName=PLDC;" +     // windows authentication
							   "integratedSecurity=true"; // driver//serverName\\instance:port (port and instance not required if default)
			con = DriverManager.getConnection(conString); 
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("connected successfully!");
		return con;
	}
	
	public static void closeConnection() {
		try {
			if(!con.isClosed()) 
				con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void addIDKey(String table) {
		Statement st = null;
		try {
			st = con.createStatement();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		String sql = "IF COL_LENGTH('" + table + "', 'ID') IS NULL BEGIN ALTER TABLE " 
				   + table + " ADD ID INT IDENTITY(1,1) PRIMARY KEY END;";
		try {
			int count = st.executeUpdate(sql);
			System.out.println(count);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void clearMessages() {
		Statement st = null;
		try {
			st = con.createStatement();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		String sql = "delete from [PLDC].[dbo].[PLDC_MESSAGE];";
		try {
			int count = st.executeUpdate(sql);
			System.out.println(count);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static ResultSet getData(String table, int ID) {
		PreparedStatement preStat = null;
		ResultSet res = null;
		String sql = "select * from " + table + " where ID > ?"; // AND ID <= ?" ;
		try {
			preStat = con.prepareStatement(sql);
			preStat.setInt(1, ID);
//			preStat.setInt(2, ID+20);
			res = preStat.executeQuery();
        } catch (SQLException e) {
			e.printStackTrace();
		}	
		return res;
	}
	
	public static void sendMessage(int ws, String msg) {
		PreparedStatement preStat = null;
		String sql = "insert into [PLDC].[dbo].[PLDC_MESSAGE] (DATE_TIME, UNIT_CODE, MESSAGE) VALUES (getdate(), ? , ?)" ;
		try {
			preStat = con.prepareStatement(sql);
			preStat.setInt(1, ws);
			preStat.setString(2, msg);
			preStat.executeUpdate();
        } catch (SQLException e) {
			e.printStackTrace();
		}	
	}
	
	public static ResultSet getWorkers() {
		PreparedStatement preStat = null;
		ResultSet res = null;
		String sql = "select * from [PLDC].[dbo].[Workers]";
		try {
			preStat = con.prepareStatement(sql);
			res = preStat.executeQuery();
        } catch (SQLException e) {
			e.printStackTrace();
		}	
		return res;
	}
	
	public static Map<String,String[]> getAllUsers() {
		ResultSet res = null;
		Map<String,String[]> users = new HashMap<String,String[]>();
		try {
			Statement state = con.createStatement();
			res = state.executeQuery("Select * from [PLDC].[dbo].[Users]");
            while(res.next()) {
            	String name = res.getString(2);
            	String[] uinfo =  {res.getString(3), res.getString(5)};
            	users.put(name, uinfo );
            }
		} catch (SQLException e1) {
			e1.printStackTrace();
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
