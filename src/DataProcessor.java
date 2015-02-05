import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import ui.DataPlot;
import util.SqlServerUtil;
import model.Repository;
import model.Worker;

public class DataProcessor {
	private String tableName;
	private int dataID;
	
	//this will be called before the main method
	private static final List<Worker> workers = Repository.getWorkers();
	//http://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html
	//DateFormat sdf = new SimpleDateFormat("HH:mm:ss.S"); //time style
	//java does not support static local variable
	private static final DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
	
	private DataPlot plot = new DataPlot(); 
	
	private Timer timer = new Timer(true);
	private long period;
	
	public DataProcessor(String table, long period) {
		this.tableName = table;
		this.dataID = 0;
		this.period = period;
		
		System.out.println("constructor");
	}
		
	public List<Worker> getWorkers() {
		return workers;
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
	
	public void setPeriod(long period) {
		this.period = period;
	}

	public void processData() {
		ResultSet res = SqlServerUtil.getData(tableName, dataID);

		try {
			if(!res.next()) {
				for(int i=0; i<workers.size(); i++) {
					workers.get(i).printTimeLine();;
				}
				timer.cancel();
				plot.summarize();
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		try {
			while(res.next()) {				
				dataID = res.getInt(12); //"ID"
				
				Timestamp ts = res.getTimestamp(1);  //"DATA_TIME"
//				Date date = res.getDate(1);
//				Time time = res.getTime(1);
				
//				String dateTime = res.getString(1); 
//				String dstr = dateTime.substring(0, 10);
//				String tstr = dateTime.substring(11, 16);
				
//				System.out.println(ts + " " + date + " " + time + " " + date.getTime());
			
				int workStation = res.getInt(2); //"UNIT_CODE"
				int workId = res.getInt(3);   //"WORKER_ID"
				String barcode = res.getString(11); //"BARCODE"
				
				System.out.println(ts + " " + workStation + " " + workId + " " + barcode);

//				int counters = res.getInt(4); //"COUNTERS"
//				int status = res.getInt(5);   //"STATUS"
//				int error = res.getInt(6);    //"ERROR"
							
				//delegate the task to the worker
				//Worker worker = workId>0 ? workers.get(workId) : Repository.getLastWorkerAtStation(workStation);
				Worker worker = null;
				if(workId>0) {  //normal event
					worker = workers.get(workId-1);
				}else{
					worker = Repository.getLastWorkerAtStation(workStation);
				}
				
				if(worker==null) { //work station 17 status = 1 and count =1 (count = 3?  at 324)
					
				}else{
					worker.addTimeLineEvent(ts.getTime(), workStation, barcode);
				}
				 
				Date time = null;
				try {
					time = sdf.parse(ts.toString());
				} catch (ParseException e) {
					e.printStackTrace();
				}
			    
				float avgEfficiency = 0.0f;
				int j =0;
				for(int i=0; i<workers.size(); i++) {
					Worker w = workers.get(i);
					if(w.isWorking()) {
						avgEfficiency += w.getEfficiency();
						j++;
					}
				}
				
				if(avgEfficiency>0.0f) { //filter
					plot.updatePlot(time, avgEfficiency/j );				
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public void processDataWithTimer() {
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				processData();				
			}		
		};
		timer.schedule(task, 1000, period);
	}
	
	public static void main(String[] args) {
		System.out.println("main\nstart worker size: " + workers.size() + " " + workers);
		
		DataProcessor dp = new DataProcessor("pldc170", 200);
		dp.processDataWithTimer();
				
	}
	
}
