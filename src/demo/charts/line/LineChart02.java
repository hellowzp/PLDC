package demo.charts.line;

import com.xeiam.xchart.Chart;
import com.xeiam.xchart.Series;
import com.xeiam.xchart.SeriesColor;
import com.xeiam.xchart.SeriesLineStyle;
import com.xeiam.xchart.SeriesMarker;
import com.xeiam.xchart.StyleManager;
import com.xeiam.xchart.SwingWrapper;
import demo.charts.ExampleChart;
import java.util.ArrayList;
import java.util.List;

public class LineChart02
  implements ExampleChart
{
  public static void main(String[] args)
  {
    ExampleChart exampleChart = new LineChart02();
    Chart chart = exampleChart.getChart();
    new SwingWrapper(chart).displayChart();
  }

  public Chart getChart()
  {
    int size = 30;
    List xData = new ArrayList();
    List yData = new ArrayList();
    for (int i = 0; i <= size; i++) {
      double radians = 3.141592653589793D / (size / 2) * i;
      xData.add(Integer.valueOf(i - size / 2));
      yData.add(Double.valueOf(-1.0E-006D * Math.sin(radians)));
    }

    Chart chart = new Chart(800, 600);

    chart.getStyleManager().setChartTitleVisible(false);
    chart.getStyleManager().setLegendVisible(false);

    Series series1 = chart.addSeries("y=sin(x)", xData, yData);
    series1.setLineColor(SeriesColor.PURPLE);
    series1.setLineStyle(SeriesLineStyle.DASH_DASH);
    series1.setMarkerColor(SeriesColor.GREEN);
    series1.setMarker(SeriesMarker.SQUARE);

    return chart;
  }
}