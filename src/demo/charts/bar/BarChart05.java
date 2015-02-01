package demo.charts.bar;

import com.xeiam.xchart.Chart;
import com.xeiam.xchart.ChartBuilder;
import com.xeiam.xchart.StyleManager;
import com.xeiam.xchart.SwingWrapper;
import demo.charts.ExampleChart;
import java.util.ArrayList;
import java.util.Arrays;

public class BarChart05
  implements ExampleChart
{
  public static void main(String[] args)
  {
    ExampleChart exampleChart = new BarChart05();
    Chart chart = exampleChart.getChart();
    new SwingWrapper(chart).displayChart();
  }

  public Chart getChart()
  {
    Chart chart = new ChartBuilder().chartType(StyleManager.ChartType.Bar).width(800).height(600).title("Temperature vs. Color").xAxisTitle("Color").yAxisTitle("Temperature").theme(StyleManager.ChartTheme.GGPlot2).build();
    chart.addSeries("fish", new ArrayList(Arrays.asList(new String[] { "Blue", "Red", "Green", "Yellow" })), new ArrayList(Arrays.asList(new Number[] { Integer.valueOf(-40), Integer.valueOf(30), Integer.valueOf(20), Integer.valueOf(60) })));
    chart.addSeries("worms", new ArrayList(Arrays.asList(new String[] { "Blue", "Red", "Green", "Yellow" })), new ArrayList(Arrays.asList(new Number[] { Integer.valueOf(50), Integer.valueOf(10), Integer.valueOf(-20), Integer.valueOf(40) })));

    return chart;
  }
}