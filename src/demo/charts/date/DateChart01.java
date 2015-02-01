package demo.charts.date;

import com.xeiam.xchart.Chart;
import com.xeiam.xchart.ChartBuilder;
import com.xeiam.xchart.Series;
import com.xeiam.xchart.SeriesMarker;
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

public class DateChart01
  implements ExampleChart
{
  public static void main(String[] args)
  {
    ExampleChart exampleChart = new DateChart01();
    Chart chart = exampleChart.getChart();
    new SwingWrapper(chart).displayChart();
  }

  public Chart getChart()
  {
    Chart chart = new ChartBuilder().width(800).height(600).title("Millisecond Scale").build();
    chart.getStyleManager().setLegendVisible(false);

    Random random = new Random();

    List xData = new ArrayList();
    List yData = new ArrayList();

    DateFormat sdf = new SimpleDateFormat("HH:mm:ss.S");
    Date date = null;
    for (int i = 1; i <= 14; i++)
    {
      try {
        date = sdf.parse("23:45:31." + (100 * i + random.nextInt(20)));
      } catch (ParseException e) {
        e.printStackTrace();
      }

      xData.add(date);
      yData.add(Double.valueOf(Math.random() * i));
    }

    Series series = chart.addSeries("blah", xData, yData);
    series.setMarker(SeriesMarker.NONE);

    return chart;
  }
}