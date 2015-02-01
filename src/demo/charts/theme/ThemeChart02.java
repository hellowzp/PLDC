package demo.charts.theme;

import com.xeiam.xchart.Chart;
import com.xeiam.xchart.ChartBuilder;
import com.xeiam.xchart.StyleManager;
import com.xeiam.xchart.SwingWrapper;
import demo.charts.ExampleChart;

public class ThemeChart02
  implements ExampleChart
{
  public static void main(String[] args)
  {
    ExampleChart exampleChart = new ThemeChart02();
    Chart chart = exampleChart.getChart();
    new SwingWrapper(chart).displayChart();
  }

  public Chart getChart()
  {
    Chart chart = new ChartBuilder().width(800).height(600).theme(StyleManager.ChartTheme.GGPlot2).title("GGPlot2 Theme").xAxisTitle("X").yAxisTitle("Y").build();

    chart.addSeries("vertical", new double[] { 1.0D, 1.0D }, new double[] { -10.0D, 10.0D });
    chart.addSeries("horizontal", new double[] { -10.0D, 10.0D }, new double[] { 0.0D, 0.0D });

    return chart;
  }
}