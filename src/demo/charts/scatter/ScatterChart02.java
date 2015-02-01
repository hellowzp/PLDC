package demo.charts.scatter;

import com.xeiam.xchart.Chart;
import com.xeiam.xchart.StyleManager;
import com.xeiam.xchart.StyleManager.ChartType;
import com.xeiam.xchart.StyleManager.LegendPosition;
import com.xeiam.xchart.SwingWrapper;
import demo.charts.ExampleChart;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ScatterChart02
  implements ExampleChart
{
  public static void main(String[] args)
  {
    ExampleChart exampleChart = new ScatterChart02();
    Chart chart = exampleChart.getChart();
    new SwingWrapper(chart).displayChart();
  }

  public Chart getChart()
  {
    List xData = new ArrayList();
    List yData = new ArrayList();
    Random random = new Random();
    int size = 400;
    for (int i = 0; i < size; i++) {
      double nextRandom = random.nextDouble();
      xData.add(Double.valueOf(Math.pow(10.0D, nextRandom * 10.0D)));
      yData.add(Double.valueOf(1000000000.0D + nextRandom));
    }

    Chart chart = new Chart(800, 600);
    chart.setChartTitle("Logarithmic Data");
    chart.getStyleManager().setChartType(StyleManager.ChartType.Scatter);
    chart.getStyleManager().setXAxisLogarithmic(true);

    chart.getStyleManager().setLegendPosition(StyleManager.LegendPosition.InsideN);

    chart.addSeries("logarithmic data", xData, yData);

    return chart;
  }
}