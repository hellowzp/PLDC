package model;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import util.SqlServerUtil;
import model.Repository;

public class DBParser {
	private String tableName;
	private int dataID;
		
	public DBParser(String table) {
		this.tableName = table;
		this.dataID = 0;				
		SqlServerUtil.addIDKey(table);	
	}

	public String getTable() {
		return tableName;
	}

	public void setTable(String table) {
		this.tableName = table;
	}

	public int getDataID() {
		return dataID;
	}

	public void setDataID(int dataID) {
		this.dataID = dataID;
	}
	
	
	public void processData() {
		ResultSet res = SqlServerUtil.getData(tableName, dataID);
		
		try {
			while(res.next()) {				
				dataID = res.getInt(12); //"ID"
				
				Timestamp ts = res.getTimestamp(1);  //"DATE_TIME"
//				Date date = res.getDate(1);
//				Time time = res.getTime(1);
				
//				String dateTime = res.getString(1); 
//				String dstr = dateTime.substring(0, 10);
//				String tstr = dateTime.substring(11, 16);
				
//				System.out.println(ts + " " + date + " " + time + " " + date.getTime());
			
				int workStation = res.getInt(2); //"UNIT_CODE"
				int workId = res.getInt(3);   //"WORKER_ID"
				String barcode = res.getString(11); //"BARCODE"				

//				int counters = res.getInt(4); //"COUNTERS"
//				int status = res.getInt(5);   //"STATUS"
				int error = res.getInt(6);    //"ERROR"			
				if(error>0) continue;         //log it
				
				//delegate the task to the repository
				Repository.getInstance().readEvent(ts,workStation,workId,barcode);
				
//				System.out.println(ts + " " + workStation + " " + workId + " " + barcode);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}	
}
