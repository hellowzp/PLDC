package demo.charts.area;

import com.xeiam.xchart.Chart;
import com.xeiam.xchart.ChartBuilder;
import com.xeiam.xchart.StyleManager;
import com.xeiam.xchart.StyleManager.ChartType;
import com.xeiam.xchart.StyleManager.LegendPosition;
import com.xeiam.xchart.SwingWrapper;
import demo.charts.ExampleChart;
import java.util.ArrayList;
import java.util.List;

public class AreaChart02
  implements ExampleChart
{
  public static void main(String[] args)
  {
    ExampleChart exampleChart = new AreaChart02();
    Chart chart = exampleChart.getChart();
    new SwingWrapper(chart).displayChart();
  }

  public Chart getChart()
  {
    Chart chart = new ChartBuilder().chartType(StyleManager.ChartType.Area).width(800).height(600).title(getClass().getSimpleName()).xAxisTitle("X").yAxisTitle("Y").build();

    List xData = new ArrayList();
    List yData = new ArrayList();
    for (int i = 0; i < 5; i++) {
      xData.add(Integer.valueOf(i));
      yData.add(Integer.valueOf(i * i));
    }
    xData.add(Integer.valueOf(5));
    yData.add(null);

    for (int i = 6; i < 10; i++) {
      xData.add(Integer.valueOf(i));
      yData.add(Integer.valueOf(i * i));
    }
    xData.add(Integer.valueOf(10));
    yData.add(null);
    xData.add(Integer.valueOf(11));
    yData.add(Integer.valueOf(100));
    xData.add(Integer.valueOf(12));
    yData.add(Integer.valueOf(90));

    chart.addSeries("a", xData, yData);

    chart.getStyleManager().setLegendPosition(StyleManager.LegendPosition.InsideNW);

    return chart;
  }
}