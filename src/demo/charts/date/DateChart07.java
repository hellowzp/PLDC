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

public class DateChart07
  implements ExampleChart
{
  public static void main(String[] args)
  {
    ExampleChart exampleChart = new DateChart07();
    Chart chart = exampleChart.getChart();
    new SwingWrapper(chart).displayChart();
  }

  public Chart getChart()
  {
    Chart chart = new ChartBuilder().width(800).height(600).title("Year Scale").build();
    chart.getStyleManager().setLegendVisible(false);

    List xData = new ArrayList();
    List yData = new ArrayList();

    Random random = new Random();

    DateFormat sdf = new SimpleDateFormat("yyyy-MM");
    Date date = null;
    for (int i = 1; i <= 14; i++) {
      try {
        date = sdf.parse("" + (2001 + i) + "-" + random.nextInt(12));
      } catch (ParseException e) {
        e.printStackTrace();
      }
      xData.add(date);
      yData.add(Double.valueOf(Math.random() * i));
    }

    chart.addSeries("blah", xData, yData);

    return chart;
  }
}