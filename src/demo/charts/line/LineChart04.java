package demo.charts.line;

import com.xeiam.xchart.Chart;
import com.xeiam.xchart.Series;
import com.xeiam.xchart.SeriesColor;
import com.xeiam.xchart.SeriesLineStyle;
import com.xeiam.xchart.SeriesMarker;
import com.xeiam.xchart.SwingWrapper;
import demo.charts.ExampleChart;

public class LineChart04
  implements ExampleChart
{
  public static void main(String[] args)
  {
    ExampleChart exampleChart = new LineChart04();
    Chart chart = exampleChart.getChart();
    new SwingWrapper(chart).displayChart();
  }

  public Chart getChart()
  {
    Chart chart = new Chart(800, 600);

    chart.setChartTitle("LineChart04");
    chart.setXAxisTitle("X");
    chart.setYAxisTitle("Y");
    chart.getStyleManager().setLegendVisible(false);

    for (int i = 0; i < 200; i++) {
      Series series = chart.addSeries("A" + i, new double[] { Math.random() / 1000.0D, Math.random() / 1000.0D }, new double[] { Math.random() / -1000.0D, Math.random() / -1000.0D });
      series.setLineColor(SeriesColor.BLUE);
      series.setLineStyle(SeriesLineStyle.SOLID);
      series.setMarker(SeriesMarker.CIRCLE);
      series.setMarkerColor(SeriesColor.BLUE);
    }

    return chart;
  }
}