package demo.charts.bar;

import com.xeiam.xchart.Chart;
import com.xeiam.xchart.ChartBuilder;
import com.xeiam.xchart.StyleManager;
import com.xeiam.xchart.StyleManager.ChartType;
import com.xeiam.xchart.SwingWrapper;
import demo.charts.ExampleChart;

public class BarChart04
  implements ExampleChart
{
  public static void main(String[] args)
  {
    ExampleChart exampleChart = new BarChart04();
    Chart chart = exampleChart.getChart();
    new SwingWrapper(chart).displayChart();
  }

  public Chart getChart()
  {
    Chart chart = new ChartBuilder().chartType(StyleManager.ChartType.Bar).width(800).height(600).title("XFactor vs. Age").xAxisTitle("Age").yAxisTitle("XFactor").build();
    chart.addSeries("female", new double[] { 10.0D, 20.0D, 30.0D, 40.0D, 50.0D }, new double[] { 50.0D, 10.0D, 20.0D, 40.0D, 35.0D });
    chart.addSeries("male", new double[] { 10.0D, 20.0D, 30.0D, 40.0D, 50.0D }, new double[] { 40.0D, 30.0D, 20.0D, 0.0D, 60.0D });

    chart.getStyleManager().setYAxisMin(5.0D);
    chart.getStyleManager().setYAxisMax(70.0D);

    return chart;
  }
}