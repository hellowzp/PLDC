package demo.charts.bar;

import com.xeiam.xchart.Chart;
import com.xeiam.xchart.ChartBuilder;
import com.xeiam.xchart.Histogram;
import com.xeiam.xchart.StyleManager;
import com.xeiam.xchart.StyleManager.ChartType;
import com.xeiam.xchart.StyleManager.LegendPosition;
import com.xeiam.xchart.SwingWrapper;
import demo.charts.ExampleChart;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BarChart08
  implements ExampleChart
{
  public static void main(String[] args)
  {
    ExampleChart exampleChart = new BarChart08();
    Chart chart = exampleChart.getChart();
    new SwingWrapper(chart).displayChart();
  }

  public Chart getChart()
  {
    Chart chart = new ChartBuilder().chartType(StyleManager.ChartType.Bar).width(800).height(600).title("Histogram").xAxisTitle("Mean").yAxisTitle("Count").build();

    Histogram histogram1 = new Histogram(getGaussianData(10000), 10, -10.0D, 10.0D);
    chart.addSeries("histogram", histogram1.getxAxisData(), histogram1.getyAxisData(), getFakeErrorData(histogram1.getxAxisData().size()));

    chart.getStyleManager().setLegendPosition(StyleManager.LegendPosition.InsideNW);
    chart.getStyleManager().setBarWidthPercentage(0.96D);

    return chart;
  }

  private List<Double> getGaussianData(int count)
  {
    List data = new ArrayList(count);
    Random r = new Random();
    for (int i = 0; i < count; i++) {
      data.add(Double.valueOf(r.nextGaussian() * 5.0D));
    }

    return data;
  }

  private List<Double> getFakeErrorData(int count)
  {
    List data = new ArrayList(count);
    Random r = new Random();
    for (int i = 0; i < count; i++) {
      data.add(Double.valueOf(r.nextDouble() * 200.0D));
    }
    return data;
  }
}