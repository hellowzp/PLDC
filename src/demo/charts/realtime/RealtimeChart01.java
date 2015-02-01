package demo.charts.realtime;

import com.xeiam.xchart.Chart;
import com.xeiam.xchart.XChartPanel;
import demo.charts.ExampleChart;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class RealtimeChart01 implements ExampleChart {
	private List<Double> yData;
	public static final String SERIES_NAME = "series1";

	public static void main(String[] args) {
		final RealtimeChart01 realtimeChart01 = new RealtimeChart01();
		final XChartPanel chartPanel = realtimeChart01.buildPanel();

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame frame = new JFrame("XChart");
				frame.setDefaultCloseOperation(3);
				frame.add(chartPanel);

				frame.pack();
				frame.setVisible(true);
			}
		});
		TimerTask chartUpdaterTask = new TimerTask() {
			public void run() {
				realtimeChart01.updateData();
				chartPanel.updateSeries("series1",
						realtimeChart01.getyData());
			}
		};
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(chartUpdaterTask, 0L, 500L);
	}

	public XChartPanel buildPanel() {
		return new XChartPanel(getChart());
	}

	public Chart getChart() {
		this.yData = getRandomData(5);

		Chart chart = new Chart(500, 400);
		chart.setChartTitle("Sample Real-time Chart");
		chart.setXAxisTitle("X");
		chart.setYAxisTitle("Y");
		chart.addSeries("series1", null, this.yData);

		return chart;
	}

	private List<Double> getRandomData(int numPoints) {
		List data = new ArrayList();
		for (int i = 0; i < numPoints; i++) {
			data.add(Double.valueOf(Math.random() * 100.0D));
		}
		return data;
	}

	public void updateData() {
		List<Double> newData = getRandomData(1);

		this.yData.addAll(newData);

		while (this.yData.size() > 20)
			this.yData.remove(0);
	}

	public List<Double> getyData() {
		return this.yData;
	}
}