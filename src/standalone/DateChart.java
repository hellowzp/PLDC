package standalone;

import com.xeiam.xchart.Chart;
import com.xeiam.xchart.ChartBuilder;
import com.xeiam.xchart.StyleManager;
import com.xeiam.xchart.SwingWrapper;
import demo.charts.ExampleChart;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class DateChart
  implements ExampleChart
{
  public static void main(String[] args)
  {
    ExampleChart exampleChart = new DateChart();
    Chart chart = exampleChart.getChart();
    new SwingWrapper(chart).displayChart();
  }

  public Chart getChart()
  {
    Chart chart = new ChartBuilder().width(800).height(600).title("Day Scale").build();
    chart.getStyleManager().setLegendVisible(false);

    List xData = new ArrayList();
    List yData = new ArrayList();

    Random random = new Random();

    DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Date date = null;
    for (int i = 1; i <= 5; i++) {
      try {
        date = sdf.parse("2014-02-" + i);
      } catch (ParseException e) {
        e.printStackTrace();
      }
      System.out.println(date);
      xData.add(date);
      System.out.println(date.getTime());
      yData.add(Double.valueOf(Math.random() * i));
    }

    chart.addSeries("blah", xData, yData);

    return chart;
  }
}