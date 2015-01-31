import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.xeiam.xchart.Chart;
import com.xeiam.xchart.QuickChart;
import com.xeiam.xchart.SwingWrapper;

import model.Worker;

public class DataProcessor {
	private List<Worker> workers;
	private String table;
	private int dataID;
	
	public DataProcessor(String table, int dataID) {
		workers = new ArrayList<>(20);
		this.table = table;
		this.dataID = dataID;
	}
		
	public List<Worker> getWorkers() {
		return workers;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public int getDataID() {
		return dataID;
	}

	public void setDataID(int dataID) {
		this.dataID = dataID;
	}

	public void processData() {
		ResultSet res = SqlServerUtil.getData(table, dataID);
		int rows = 0; 
		try {
			while(res.next()) {				
				rows++;
				
				System.out.println(res.getRow() + " " + res.getInt("ID") + " " + res.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		dataID += rows;
		System.out.println(dataID);
	}
	
	public static void main(String[] args) {
		DataProcessor dp = new DataProcessor("pldc170", 1);
		dp.processData();
		dp.processData();
		
		double[] xData = new double[] { 0.0, 1.0, 2.0 };
	    double[] yData = new double[] { 2.0, 1.0, 0.0 };
	 
	    // Create Chart
	    Chart chart = QuickChart.getChart("Sample Chart", "X", "Y", "y(x)", xData, yData);
	 
	    // Show it
	    new SwingWrapper(chart).displayChart();
	}
	
}
