package demo.charts.theme;

import com.xeiam.xchart.Chart;
import com.xeiam.xchart.ChartBuilder;
import com.xeiam.xchart.Series;
import com.xeiam.xchart.SeriesLineStyle;
import com.xeiam.xchart.StyleManager;
import com.xeiam.xchart.SwingWrapper;
import demo.charts.ExampleChart;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ThemeChart03
  implements ExampleChart
{
  public static void main(String[] args)
  {
    ExampleChart exampleChart = new ThemeChart03();
    Chart chart = exampleChart.getChart();
    new SwingWrapper(chart).displayChart();
  }

  public Chart getChart()
  {
    Chart chart = new ChartBuilder().width(800).height(600).theme(StyleManager.ChartTheme.Matlab).title("Matlab Theme").xAxisTitle("X").yAxisTitle("Y").build();
    chart.getStyleManager().setPlotGridLinesVisible(false);
    chart.getStyleManager().setXAxisTickMarkSpacingHint(100);

    List xData = new ArrayList();
    List y1Data = new ArrayList();
    List y2Data = new ArrayList();

    DateFormat sdf = new SimpleDateFormat("yyyy-MM");
    try
    {
      Date date = sdf.parse("2012-08");
      xData.add(date);
      y1Data.add(Double.valueOf(120.0D));
      y2Data.add(Double.valueOf(15.0D));

      date = sdf.parse("2012-11");
      xData.add(date);
      y1Data.add(Double.valueOf(165.0D));
      y2Data.add(Double.valueOf(15.0D));

      date = sdf.parse("2013-01");
      xData.add(date);
      y1Data.add(Double.valueOf(210.0D));
      y2Data.add(Double.valueOf(20.0D));

      date = sdf.parse("2013-02");
      xData.add(date);
      y1Data.add(Double.valueOf(400.0D));
      y2Data.add(Double.valueOf(30.0D));

      date = sdf.parse("2013-03");
      xData.add(date);
      y1Data.add(Double.valueOf(800.0D));
      y2Data.add(Double.valueOf(100.0D));

      date = sdf.parse("2013-04");
      xData.add(date);
      y1Data.add(Double.valueOf(2000.0D));
      y2Data.add(Double.valueOf(120.0D));

      date = sdf.parse("2013-05");
      xData.add(date);
      y1Data.add(Double.valueOf(3000.0D));
      y2Data.add(Double.valueOf(150.0D));
    }
    catch (ParseException e) {
      e.printStackTrace();
    }

    Series series1 = chart.addSeries("downloads", xData, y1Data);
    series1.setLineStyle(SeriesLineStyle.DOT_DOT);
    chart.addSeries("price", xData, y2Data);

    return chart;
  }
}