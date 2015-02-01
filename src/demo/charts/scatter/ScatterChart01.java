package demo.charts.scatter;

import com.xeiam.xchart.Chart;
import com.xeiam.xchart.StyleManager;
import com.xeiam.xchart.StyleManager.ChartType;
import com.xeiam.xchart.StyleManager.LegendPosition;
import com.xeiam.xchart.SwingWrapper;
import demo.charts.ExampleChart;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class ScatterChart01
  implements ExampleChart
{
  public static void main(String[] args)
  {
    ExampleChart exampleChart = new ScatterChart01();
    Chart chart = exampleChart.getChart();
    new SwingWrapper(chart).displayChart();
  }

  public Chart getChart()
  {
    Set xData = new HashSet();
    Set yData = new HashSet();
    Random random = new Random();
    int size = 1000;
    for (int i = 0; i < size; i++) {
      xData.add(Double.valueOf(random.nextGaussian() / 1000.0D));
      yData.add(Double.valueOf(-1000000.0D + random.nextGaussian()));
    }

    Chart chart = new Chart(800, 600);
    chart.getStyleManager().setChartType(StyleManager.ChartType.Scatter);

    chart.getStyleManager().setChartTitleVisible(false);
    chart.getStyleManager().setLegendPosition(StyleManager.LegendPosition.InsideSW);
    chart.getStyleManager().setMarkerSize(16);

    chart.addSeries("Gaussian Blob", xData, yData);

    return chart;
  }
}