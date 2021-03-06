package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.swing.JPanel;

import com.xeiam.xchart.Chart;
import com.xeiam.xchart.ChartColor;
import com.xeiam.xchart.Series;
import com.xeiam.xchart.SeriesColor;
import com.xeiam.xchart.SeriesLineStyle;
import com.xeiam.xchart.SeriesMarker;
import com.xeiam.xchart.XChartPanel;

import javax.swing.BoxLayout;
import javax.swing.JButton;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AverageEffPanel extends JPanel{

	private static final long serialVersionUID = 1L;
	
	private List<Date> xData;
	private List<Float> yData;
	private XChartPanel chartPanel;
	
	public AverageEffPanel() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		
		xData = new ArrayList<>();
		yData = new ArrayList<>();
		chartPanel = buildChartPanel();
		
		JPanel controlPanel = new JPanel();
		controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.X_AXIS));
		add(controlPanel);
		
		JButton btnSelectTime = new JButton("Change time range");
		btnSelectTime.setToolTipText("change the time range to plot data");
		btnSelectTime.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				summarize();
			}
		});
		add(btnSelectTime);
		
		JButton btnSummarize = new JButton("Summarize");
		btnSummarize.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				summarize();
			}
		});
		add(btnSummarize);

		
		//add(chartPanel);
		
		
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(600,400);
	}
		
	private XChartPanel buildChartPanel() {
		Chart chart = new Chart(800,600); 

	    DateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
		chart.setChartTitle("Average Efficiency " + dft.format(new Date()));
//	    chart.setXAxisTitle("time");
//	    chart.setYAxisTitle("average efficiency");
//	    chart.getStyleManager().setPlotBackgroundColor(ChartColor.getAWTColor(ChartColor.GREY));
	    chart.getStyleManager().setPlotGridLinesColor(new Color(255, 255, 255));
	    chart.getStyleManager().setChartBackgroundColor(Color.WHITE);
	    //chart.getStyleManager().setLegendBackgroundColor(Color.PINK);
	    chart.getStyleManager().setLegendVisible(false);
	    chart.getStyleManager().setChartFontColor(Color.BLACK); //title color
	    chart.getStyleManager().setChartTitleBoxBackgroundColor(Color.WHITE);
	    chart.getStyleManager().setChartTitleBoxVisible(true);
	    //chart.getStyleManager().setChartTitleBoxBorderColor(Color.BLACK);
	    chart.getStyleManager().setPlotGridLinesVisible(false);

	    chart.getStyleManager().setAxisTickPadding(5);
	    chart.getStyleManager().setAxisTickMarkLength(5);
	    chart.getStyleManager().setPlotPadding(0);
	    
	    chart.getStyleManager().setMarkerSize(5);

	    chart.getStyleManager().setChartTitleFont(new Font("Monospaced", 1, 20));
//	    chart.getStyleManager().setLegendFont(new Font("Serif", 0, 18));
//	    chart.getStyleManager().setLegendPosition(StyleManager.LegendPosition.InsideSE);
//	    chart.getStyleManager().setLegendSeriesLineLength(12);
	    chart.getStyleManager().setAxisTitleFont(new Font("SansSerif", 2, 18));
	    chart.getStyleManager().setAxisTickLabelsFont(new Font("Serif", 0, 11));
	    //chart.getStyleManager().setDatePattern("HH:mm");
	    chart.getStyleManager().setDecimalPattern("#0.00");
	    chart.getStyleManager().setLocale(Locale.getDefault());

	    //add dummy data
	    DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S"); 
		Date time = null;
		try {
			time = sdf.parse("2015-01-13 08:08:08.0");
		} catch (ParseException e) {
			e.printStackTrace();
		}
	    
	    xData.add(time);
		yData.add(0.5f);
			    
	    Series series = chart.addSeries("Efficiency Series", xData, yData);
	    series.setLineColor(SeriesColor.BLUE);
	    series.setMarkerColor(Color.ORANGE);
	    series.setMarker(SeriesMarker.CIRCLE);
	    series.setLineStyle(SeriesLineStyle.SOLID);
	 
	    xData.clear(); yData.clear();
	    
		return new XChartPanel(chart);
	}

	public void updatePlot(Date time, float data) {	
		xData.add(time);
		yData.add(data);
		
		chartPanel.updateSeries("Efficiency Series", xData, yData);
	}
	
	public void summarize() {
		float min = Collections.min(yData);
		float max = Collections.max(yData);
		float avg = 0.0f;  //6min
		
		for(int i=0; i<yData.size(); i++) {
			avg += yData.get(i);
		}
		
		System.out.println("min: " + min + " max: " + max + " avg: " + avg/yData.size());
		
	}
}
