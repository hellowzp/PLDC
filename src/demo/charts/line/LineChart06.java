package demo.charts.line;

import com.xeiam.xchart.Chart;
import com.xeiam.xchart.Series;
import com.xeiam.xchart.SeriesLineStyle;
import com.xeiam.xchart.SeriesMarker;
import com.xeiam.xchart.StyleManager;
import com.xeiam.xchart.SwingWrapper;
import demo.charts.ExampleChart;
import java.awt.Color;

public class LineChart06
  implements ExampleChart
{
  public static void main(String[] args)
  {
    ExampleChart exampleChart = new LineChart06();
    Chart chart = exampleChart.getChart();
    new SwingWrapper(chart).displayChart();
  }

  public Chart getChart()
  {
    int[] xData = { 0, 1, 2, 3, 4, 5, 6 };

    int[] yData1 = { 100, 100, 100, 60, 10, 10, 10 };

    int[] errdata = { 50, 20, 10, 52, 9, 2, 1 };

    Chart chart = new Chart(800, 600);

    chart.getStyleManager().setYAxisLogarithmic(true);

    chart.getStyleManager().setYAxisMin(0.08D);

    chart.getStyleManager().setYAxisMax(1000.0D);

    chart.getStyleManager().setErrorBarsColor(Color.black);

    Series series1 = chart.addSeries("Error bar\ntest data", xData, yData1, errdata);

    series1.setLineStyle(SeriesLineStyle.SOLID);

    series1.setMarker(SeriesMarker.DIAMOND);

    series1.setMarkerColor(Color.MAGENTA);

    return chart;
  }
}