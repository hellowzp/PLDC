package demo.charts.date;

import com.xeiam.xchart.Chart;
import com.xeiam.xchart.ChartBuilder;
import com.xeiam.xchart.StyleManager;
import com.xeiam.xchart.SwingWrapper;
import demo.charts.ExampleChart;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class DateChart03
  implements ExampleChart
{
  public static void main(String[] args)
  {
    ExampleChart exampleChart = new DateChart03();
    Chart chart = exampleChart.getChart();
    new SwingWrapper(chart).displayChart();
  }

  public Chart getChart()
  {
    Chart chart = new ChartBuilder().width(800).height(600).title("Minute Scale").build();
    chart.getStyleManager().setLegendVisible(false);

    List xData = new ArrayList();
    List yData = new ArrayList();

    Random random = new Random();

    DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss.SSS");
    Date date = null;
    for (int i = 1; i <= 14; i++) {
      try {
        date = sdf.parse("2013-07-22-08:" + (5 * i + random.nextInt(2)) + ":" + random.nextInt(2) + "." + random.nextInt(1000));
      } catch (ParseException e) {
        e.printStackTrace();
      }

      xData.add(date);
      yData.add(Double.valueOf(Math.random() * i * 1000000000.0D));
    }

    chart.addSeries("blah", xData, yData);

    return chart;
  }
}