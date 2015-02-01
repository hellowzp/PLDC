package demo.charts.line;

import com.xeiam.xchart.Chart;
import com.xeiam.xchart.Series;
import com.xeiam.xchart.SeriesLineStyle;
import com.xeiam.xchart.SeriesMarker;
import com.xeiam.xchart.StyleManager;
import com.xeiam.xchart.StyleManager.LegendPosition;
import com.xeiam.xchart.SwingWrapper;
import demo.charts.ExampleChart;
import java.awt.Color;

public class LineChart05
  implements ExampleChart
{
  public static void main(String[] args)
  {
    ExampleChart exampleChart = new LineChart05();
    Chart chart = exampleChart.getChart();
    new SwingWrapper(chart).displayChart();
  }

  public Chart getChart()
  {
    Chart chart = new Chart(800, 600);

    chart.setChartTitle("LineChart05");
    chart.setXAxisTitle("X");
    chart.setYAxisTitle("Y");
    chart.getStyleManager().setLegendPosition(StyleManager.LegendPosition.InsideSW);

    double[] xData = { 0.0D, 1.0D, 2.0D, 3.0D, 4.0D, 5.0D, 6.0D };
    double[] yData = { 106.0D, 44.0D, 26.0D, 10.0D, 7.5D, 3.4D, 0.88D };
    double[] yData2 = { 102.0D, 49.0D, 23.600000000000001D, 11.300000000000001D, 5.4D, 2.6D, 1.25D };

    Series series = chart.addSeries("A", xData, yData);
    series.setLineStyle(SeriesLineStyle.NONE);
    series.setMarker(SeriesMarker.DIAMOND);
    series.setMarkerColor(Color.BLACK);

    Series series2 = chart.addSeries("B", xData, yData2);
    series2.setMarker(SeriesMarker.NONE);
    series2.setLineStyle(SeriesLineStyle.DASH_DASH);
    series2.setLineColor(Color.BLACK);

    chart.getStyleManager().setYAxisLogarithmic(true);

    chart.getStyleManager().setYAxisMin(0.01D);
    chart.getStyleManager().setYAxisMax(1000.0D);

    chart.getStyleManager().setXAxisMin(2.0D);
    chart.getStyleManager().setXAxisMax(7.0D);

    return chart;
  }
}