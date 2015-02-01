package demo.charts.scatter;

import com.xeiam.xchart.Chart;
import com.xeiam.xchart.SwingWrapper;
import demo.charts.ExampleChart;

public class ScatterChart03
  implements ExampleChart
{
  public static void main(String[] args)
  {
    ExampleChart exampleChart = new ScatterChart03();
    Chart chart = exampleChart.getChart();
    new SwingWrapper(chart).displayChart();
  }

  public Chart getChart()
  {
    Chart chart = new Chart(800, 600);

    chart.setChartTitle("Single Point");
    chart.setXAxisTitle("X");
    chart.setYAxisTitle("Y");

    chart.addSeries("single point (1,1)", new double[] { 1.0D }, new double[] { 1.0D });

    return chart;
  }
}