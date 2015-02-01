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

public class BarChart06
  implements ExampleChart
{
  public static void main(String[] args)
  {
    ExampleChart exampleChart = new BarChart06();
    Chart chart = exampleChart.getChart();
    new SwingWrapper(chart).displayChart();
  }

  public Chart getChart()
  {
    Chart chart = new ChartBuilder().chartType(StyleManager.ChartType.Bar).width(800).height(600).title("Score Histogram").xAxisTitle("Mean").yAxisTitle("Count").build();

    Histogram histogram1 = new Histogram(getGaussianData(10000), 30, -30.0D, 30.0D);
    Histogram histogram2 = new Histogram(getGaussianData(5000), 30, -30.0D, 30.0D);
    chart.addSeries("histogram 1", histogram1.getxAxisData(), histogram1.getyAxisData());
    chart.addSeries("histogram 2", histogram2.getxAxisData(), histogram2.getyAxisData());

    chart.getStyleManager().setLegendPosition(StyleManager.LegendPosition.InsideNW);
    chart.getStyleManager().setBarWidthPercentage(0.96D);
    chart.getStyleManager().setBarsOverlapped(true);

    return chart;
  }

  private List<Double> getGaussianData(int count)
  {
    List data = new ArrayList(count);
    Random r = new Random();
    for (int i = 0; i < count; i++) {
      data.add(Double.valueOf(r.nextGaussian() * 10.0D));
    }

    return data;
  }
}