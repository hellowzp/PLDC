package demo.charts.area;

import com.xeiam.xchart.Chart;
import com.xeiam.xchart.ChartBuilder;
import com.xeiam.xchart.StyleManager;
import com.xeiam.xchart.StyleManager.ChartType;
import com.xeiam.xchart.StyleManager.LegendPosition;
import com.xeiam.xchart.SwingWrapper;
import demo.charts.ExampleChart;

public class AreaChart01
  implements ExampleChart
{
  public static void main(String[] args)
  {
    ExampleChart exampleChart = new AreaChart01();
    Chart chart = exampleChart.getChart();
    new SwingWrapper(chart).displayChart();
  }

  public Chart getChart()
  {
    Chart chart = new ChartBuilder().chartType(StyleManager.ChartType.Area).width(800).height(600).title(getClass().getSimpleName()).xAxisTitle("X").yAxisTitle("Y").build();
    chart.addSeries("a", new double[] { 0.0D, 3.0D, 5.0D, 7.0D, 9.0D }, new double[] { -3.0D, 5.0D, 9.0D, 6.0D, 5.0D });
    chart.addSeries("b", new double[] { 0.0D, 2.0D, 4.0D, 6.0D, 9.0D }, new double[] { -1.0D, 6.0D, 4.0D, 0.0D, 4.0D });
    chart.addSeries("c", new double[] { 0.0D, 1.0D, 3.0D, 8.0D, 9.0D }, new double[] { -2.0D, -1.0D, 1.0D, 0.0D, 1.0D });

    chart.getStyleManager().setLegendPosition(StyleManager.LegendPosition.InsideNW);
    chart.getStyleManager().setAxisTitlesVisible(false);

    return chart;
  }
}