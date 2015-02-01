package demo.charts.line;

import com.xeiam.xchart.Chart;
import com.xeiam.xchart.ChartBuilder;
import com.xeiam.xchart.StyleManager;
import com.xeiam.xchart.StyleManager.LegendPosition;
import com.xeiam.xchart.SwingWrapper;
import demo.charts.ExampleChart;
import java.util.ArrayList;
import java.util.List;

public class LineChart01
  implements ExampleChart
{
  public static void main(String[] args)
  {
    ExampleChart exampleChart = new LineChart01();
    Chart chart = exampleChart.getChart();
    new SwingWrapper(chart).displayChart();
  }

  public Chart getChart()
  {
    List xData = new ArrayList();
    List yData = new ArrayList();
    for (int i = -3; i <= 3; i++) {
      xData.add(Integer.valueOf(i));
      yData.add(Double.valueOf(Math.pow(10.0D, i)));
    }

    Chart chart = new ChartBuilder().width(800).height(600).build();

    chart.getStyleManager().setChartTitleVisible(false);
    chart.getStyleManager().setLegendPosition(StyleManager.LegendPosition.InsideNW);
    chart.getStyleManager().setYAxisLogarithmic(true);

    chart.addSeries("10^x", xData, yData);

    return chart;
  }
}