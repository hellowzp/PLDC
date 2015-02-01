package demo.charts.bar;

import com.xeiam.xchart.Chart;
import com.xeiam.xchart.ChartBuilder;
import com.xeiam.xchart.StyleManager;
import com.xeiam.xchart.StyleManager.ChartType;
import com.xeiam.xchart.StyleManager.LegendPosition;
import com.xeiam.xchart.SwingWrapper;
import demo.charts.ExampleChart;

public class BarChart01
  implements ExampleChart
{
  public static void main(String[] args)
  {
    ExampleChart exampleChart = new BarChart01();
    Chart chart = exampleChart.getChart();
    new SwingWrapper(chart).displayChart();
  }

  public Chart getChart()
  {
    Chart chart = new ChartBuilder().chartType(StyleManager.ChartType.Bar).width(800).height(600).title("Score Histogram").xAxisTitle("Score").yAxisTitle("Number").build();
    chart.addSeries("test 1", new double[] { 0.0D, 1.0D, 2.0D, 3.0D, 4.0D }, new double[] { 4.0D, 5.0D, 9.0D, 6.0D, 5.0D });

    chart.getStyleManager().setLegendPosition(StyleManager.LegendPosition.InsideNW);

    return chart;
  }
}