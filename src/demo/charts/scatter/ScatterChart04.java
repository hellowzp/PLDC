package demo.charts.scatter;

import com.xeiam.xchart.Chart;
import com.xeiam.xchart.ChartBuilder;
import com.xeiam.xchart.Series;
import com.xeiam.xchart.SeriesMarker;
import com.xeiam.xchart.StyleManager;
import com.xeiam.xchart.StyleManager.ChartType;
import com.xeiam.xchart.SwingWrapper;
import demo.charts.ExampleChart;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class ScatterChart04
  implements ExampleChart
{
  public static void main(String[] args)
  {
    ExampleChart exampleChart = new ScatterChart04();
    Chart chart = exampleChart.getChart();
    new SwingWrapper(chart).displayChart();
  }

  public Chart getChart()
  {
    int size = 10;
    List xData = new ArrayList();
    List yData = new ArrayList();
    List errorBars = new ArrayList();
    for (int i = 0; i <= size; i++) {
      xData.add(Double.valueOf(i / 100000000.0D));
      yData.add(Double.valueOf(10.0D * Math.exp(-i)));
      errorBars.add(Double.valueOf(Math.random() + 0.3D));
    }

    Chart chart = new ChartBuilder().width(800).height(600).title("ScatterChart04").xAxisTitle("X").yAxisTitle("Y").chartType(StyleManager.ChartType.Scatter).build();

    chart.getStyleManager().setChartTitleVisible(false);
    chart.getStyleManager().setLegendVisible(false);
    chart.getStyleManager().setAxisTitlesVisible(false);

    Series series = chart.addSeries("10^(-x)", xData, yData, errorBars);
    series.setMarkerColor(Color.RED);
    series.setMarker(SeriesMarker.SQUARE);

    return chart;
  }
}