package standalone;

import com.xeiam.xchart.Chart;
import com.xeiam.xchart.ChartBuilder;
import com.xeiam.xchart.Series;
import com.xeiam.xchart.SeriesMarker;
import com.xeiam.xchart.StyleManager;
import com.xeiam.xchart.SwingWrapper;
import java.util.ArrayList;
import java.util.List;

public class Example2
{
  public static void main(String[] args)
  {
    int numCharts = 4;

    List charts = new ArrayList();

    for (int i = 0; i < numCharts; i++) {
      Chart chart = new ChartBuilder().xAxisTitle("X").yAxisTitle("Y").width(600).height(400).build();
      chart.getStyleManager().setYAxisMin(-10.0D);
      chart.getStyleManager().setYAxisMax(10.0D);
      Series series = chart.addSeries("" + i, null, getRandomWalk(200));
      series.setMarker(SeriesMarker.NONE);
      charts.add(chart);
    }
    new SwingWrapper(charts).displayChartMatrix();
  }

  private static double[] getRandomWalk(int numPoints)
  {
    double[] y = new double[numPoints];
    y[0] = 0.0D;
    for (int i = 1; i < y.length; i++) {
      y[i] = (y[(i - 1)] + Math.random() - 0.5D);
    }
    return y;
  }
}