package demo.charts.bar;

import com.xeiam.xchart.Chart;
import com.xeiam.xchart.ChartBuilder;
import com.xeiam.xchart.SwingWrapper;
import com.xeiam.xchart.StyleManager;
import demo.charts.ExampleChart;

public class BarChart03
  implements ExampleChart
{
  public static void main(String[] args)
  {
    ExampleChart exampleChart = new BarChart03();
    Chart chart = exampleChart.getChart();
    new SwingWrapper(chart).displayChart();
  }

  public Chart getChart()
  {
    Chart chart = new ChartBuilder().chartType(StyleManager.ChartType.Bar).width(800).height(600).title("Score vs. Age").xAxisTitle("Age").yAxisTitle("Score").build();
    chart.addSeries("males", new double[] { 10.0D, 20.0D, 30.0D, 40.0D }, new double[] { 40.0D, -30.0D, -20.0D, -60.0D });

    return chart;
  }
}