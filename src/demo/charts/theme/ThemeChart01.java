package demo.charts.theme;

import com.xeiam.xchart.Chart;
import com.xeiam.xchart.StyleManager;
import com.xeiam.xchart.SwingWrapper;

import demo.charts.ExampleChart;

import java.util.ArrayList;
import java.util.Collection;

public class ThemeChart01
  implements ExampleChart
{
  public static void main(String[] args)
  {
    ExampleChart exampleChart = new ThemeChart01();
    Chart chart = exampleChart.getChart();
    new SwingWrapper(chart).displayChart();
  }

  public Chart getChart()
  {
    Chart chart = new Chart(800, 600);

		for (int i = 1; i <= 14; i++) {
			int b = 20;
			Collection<Integer> xData = new ArrayList<Integer>();
			Collection<Integer> yData = new ArrayList<Integer>();
			
			for (int x = 0; x <= b; x++) {
				xData.add(Integer.valueOf(2 * x - b));
				yData.add(Integer.valueOf(2 * i * x - i * b));
			}

			chart.setChartTitle("XChart Theme");
			chart.setXAxisTitle("X");
			chart.setYAxisTitle("Y");

			String seriesName = "y=" + 2 * i + "x-" + i * b + "b";
			chart.addSeries(seriesName, xData, yData);
			chart.getStyleManager().setMarkerSize(11);
    }

    return chart;
  }
}