import java.awt.Color;
import java.awt.Font;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import util.SqlServerUtil;

import com.xeiam.xchart.Chart;
import com.xeiam.xchart.ChartColor;
import com.xeiam.xchart.QuickChart;
import com.xeiam.xchart.Series;
import com.xeiam.xchart.SeriesColor;
import com.xeiam.xchart.SeriesLineStyle;
import com.xeiam.xchart.SeriesMarker;
import com.xeiam.xchart.StyleManager;
import com.xeiam.xchart.SwingWrapper;

import model.Repository;
import model.Worker;

public class DataProcessor {
	private String table;
	private int dataID;
	
	private List<String> timeAxis = new LinkedList<>();
	private List<Float> efficiences = new LinkedList<>();
	private List<Worker> workers = Repository.loadWorkers();
	
	private Timer timer = new Timer(true);
	private long period;
	
	public DataProcessor(String table, long period) {
		this.table = table;
		this.dataID = 0;
		this.period = period;
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
	
	public void setPeriod(long period) {
		this.period = period;
	}

	public void processData() {
		ResultSet res = SqlServerUtil.getData(table, dataID);
		try {
			while(res.next()) {				
				dataID = res.getInt(12); //"ID"
				Timestamp ts = res.getTimestamp(1);  //"DATA_TIME"
				int workStation = res.getInt(2); //"UNIT_CODE"
				int workId = res.getInt(3);   //"WORKER_ID"
				String barcode = res.getString(11); //"BARCODE"

				int counters = res.getInt(4); //"COUNTERS"
				int status = res.getInt(5);   //"STATUS"
				int error = res.getInt(6);    //"ERROR"
				
				String dateTime = res.getString(1);
				String date = dateTime.substring(0, 10);
				String time = dateTime.substring(11, 16);
				
				System.out.println(res.getRow() + " " + ts + " " + barcode + " " + workId);
							
				//delegate the task to the worker
				Worker worker = workId>0 ? workers.get(workId) : Repository.getLastWorkerAtStation(workStation);
				worker.addTimeLine(ts.getTime(), workStation, barcode);
				
				timeAxis.add(time);
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public void processDataWithTimer(int period) {
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				processData();				
			}		
		};
		timer.schedule(task, 1000, period);
	}
	
	public static void main(String[] args) {
		DataProcessor dp = new DataProcessor("pldc170", 2000);
		dp.processDataWithTimer(20);
		
		Chart chart = new Chart(800, 600);

	    Collection<Date> xData = new ArrayList<Date>();
	    Collection<Double> yData = new ArrayList<Double>();

	    DateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
	    Date date = null;
	    for (int i = 1; i <= 10; i++)
	    {
	      try {
	        date = sdf.parse(i + ".10.2008");
	        System.out.println(date);
	      } catch (ParseException e) {
	        e.printStackTrace();
	      }
	      xData.add(date);
	      yData.add(Double.valueOf(Math.random() * i));
	    }

	    chart.setChartTitle("LineChart03");
	    chart.setXAxisTitle("X");
	    chart.setYAxisTitle("Y");
	    chart.getStyleManager().setPlotBackgroundColor(ChartColor.getAWTColor(ChartColor.GREY));
	    chart.getStyleManager().setPlotGridLinesColor(new Color(255, 255, 255));
	    chart.getStyleManager().setChartBackgroundColor(Color.WHITE);
	    chart.getStyleManager().setLegendBackgroundColor(Color.PINK);
	    chart.getStyleManager().setChartFontColor(Color.MAGENTA);
	    chart.getStyleManager().setChartTitleBoxBackgroundColor(new Color(0, 222, 0));
	    chart.getStyleManager().setChartTitleBoxVisible(true);
	    chart.getStyleManager().setChartTitleBoxBorderColor(Color.BLACK);
	    chart.getStyleManager().setPlotGridLinesVisible(false);

	    chart.getStyleManager().setAxisTickPadding(20);

	    chart.getStyleManager().setAxisTickMarkLength(15);

	    chart.getStyleManager().setPlotPadding(20);

	    chart.getStyleManager().setChartTitleFont(new Font("Monospaced", 1, 24));
	    chart.getStyleManager().setLegendFont(new Font("Serif", 0, 18));
	    chart.getStyleManager().setLegendPosition(StyleManager.LegendPosition.InsideSE);
	    chart.getStyleManager().setLegendSeriesLineLength(12);
	    chart.getStyleManager().setAxisTitleFont(new Font("SansSerif", 2, 18));
	    chart.getStyleManager().setAxisTickLabelsFont(new Font("Serif", 0, 11));
	    chart.getStyleManager().setDatePattern("dd-MMM");
	    chart.getStyleManager().setDecimalPattern("#0.000");
	    chart.getStyleManager().setLocale(Locale.GERMAN);

	    Series series = chart.addSeries("Fake Data", xData, yData);
	    series.setLineColor(SeriesColor.BLUE);
	    series.setMarkerColor(Color.ORANGE);
	    series.setMarker(SeriesMarker.CIRCLE);
	    series.setLineStyle(SeriesLineStyle.SOLID);
	 
	    // Create Chart
	    //Chart chart = QuickChart.getChart("Sample Chart", "X", "Y", "y(x)", xData, yData);
	 
	    // Show it
	    new SwingWrapper(chart).displayChart();
	}
	
}
