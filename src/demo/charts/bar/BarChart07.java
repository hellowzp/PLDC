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

public class BarChart07
  implements ExampleChart
{
  public static void main(String[] args)
  {
    ExampleChart exampleChart = new BarChart07();
    Chart chart = exampleChart.getChart();
    new SwingWrapper(chart).displayChart();
  }

  public Chart getChart()
  {
    Chart chart = new ChartBuilder().chartType(StyleManager.ChartType.Bar).width(800).height(600).title("Score Histogram").xAxisTitle("Mean").yAxisTitle("Count").build();

    Histogram histogram1 = new Histogram(getGaussianData(1000), 10, -30.0D, 30.0D);
    chart.addSeries("histogram 1", histogram1.getxAxisData(), histogram1.getyAxisData());
    Histogram histogram2 = new Histogram(getGaussianData(1000), 10, -30.0D, 30.0D);
    chart.addSeries("histogram 2", histogram2.getxAxisData(), histogram2.getyAxisData());

    chart.getStyleManager().setLegendPosition(StyleManager.LegendPosition.InsideNW);
    chart.getStyleManager().setBarWidthPercentage(0.96D);

    return chart;
  }

  private List<Integer> getGaussianData(int count)
  {
    List data = new ArrayList(count);
    Random r = new Random();
    for (int i = 0; i < count; i++) {
      data.add(Integer.valueOf((int)(r.nextGaussian() * 10.0D)));
    }

    return data;
  }
}