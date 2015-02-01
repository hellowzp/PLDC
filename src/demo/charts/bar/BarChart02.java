package demo.charts.bar;

import com.xeiam.xchart.Chart;
import com.xeiam.xchart.ChartBuilder;
import com.xeiam.xchart.Series;
import com.xeiam.xchart.SeriesColor;
import com.xeiam.xchart.StyleManager;
import com.xeiam.xchart.SwingWrapper;
import demo.charts.ExampleChart;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class BarChart02
  implements ExampleChart
{
  public static void main(String[] args)
  {
    ExampleChart exampleChart = new BarChart02();
    Chart chart = exampleChart.getChart();
    new SwingWrapper(chart).displayChart();
  }

  public Chart getChart()
  {
    Chart chart = new ChartBuilder().theme(StyleManager.ChartTheme.Matlab).chartType(StyleManager.ChartType.Bar).width(800).height(600).title("Units Sold Per Year").xAxisTitle("Year").yAxisTitle("Units Sold").build();

    List xData = new ArrayList();
    Collection yData = new ArrayList();

    Random random = new Random();
    DateFormat sdf = new SimpleDateFormat("yyyy");
    Date date = null;
    for (int i = 1; i <= 8; i++) {
      try {
        date = sdf.parse("" + (2000 + i));
      } catch (ParseException e) {
        e.printStackTrace();
      }
      xData.add(date);
      yData.add(Double.valueOf(-1.0E-008D * (random.nextInt(i) + 1)));
    }
    Series series = chart.addSeries("Model 77", xData, yData);
    series.setLineColor(SeriesColor.RED);
    chart.getStyleManager().setPlotGridLinesVisible(false);
    chart.getStyleManager().setBarFilled(false);

    return chart;
  }
}