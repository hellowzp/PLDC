package demo.charts.line;

import com.xeiam.xchart.Chart;
import com.xeiam.xchart.ChartColor;
import com.xeiam.xchart.Series;
import com.xeiam.xchart.SeriesColor;
import com.xeiam.xchart.SeriesLineStyle;
import com.xeiam.xchart.SeriesMarker;
import com.xeiam.xchart.StyleManager;
import com.xeiam.xchart.SwingWrapper;

import demo.charts.ExampleChart;

import java.awt.Color;
import java.awt.Font;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;

public class LineChart03
  implements ExampleChart
{
  public static void main(String[] args)
  {
    ExampleChart exampleChart = new LineChart03();
    Chart chart = exampleChart.getChart();
    new SwingWrapper(chart).displayChart();
  }

  public Chart getChart()
  {
    Chart chart = new Chart(800, 600);

    Collection<Date> xData = new ArrayList<Date>();
    Collection<Double> yData = new ArrayList<Double>();

    DateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    Date date = null;
    for (int i = 1; i <= 10; i++)
    {
      try {
        date = sdf.parse(i + ".10.2008");
      } catch (ParseException e) {
        e.printStackTrace();
      }
      xData.add(date);
      yData.add(Double.valueOf(Math.random() * i));
    }

    chart.setChartTitle("LineChart03");
    chart.setXAxisTitle("X");
    chart.setYAxisTitle("Y");
    chart.getStyleManager().setPlotBackgroundColor(ChartColor.getAWTColor(ChartColor.GREY));
    chart.getStyleManager().setPlotGridLinesColor(new Color(255, 255, 255));
    chart.getStyleManager().setChartBackgroundColor(Color.WHITE);
    chart.getStyleManager().setLegendBackgroundColor(Color.PINK);
    chart.getStyleManager().setChartFontColor(Color.MAGENTA);
    chart.getStyleManager().setChartTitleBoxBackgroundColor(new Color(0, 222, 0));
    chart.getStyleManager().setChartTitleBoxVisible(true);
    chart.getStyleManager().setChartTitleBoxBorderColor(Color.BLACK);
    chart.getStyleManager().setPlotGridLinesVisible(false);

    chart.getStyleManager().setAxisTickPadding(20);

    chart.getStyleManager().setAxisTickMarkLength(15);

    chart.getStyleManager().setPlotPadding(20);

    chart.getStyleManager().setChartTitleFont(new Font("Monospaced", 1, 24));
    chart.getStyleManager().setLegendFont(new Font("Serif", 0, 18));
    chart.getStyleManager().setLegendPosition(StyleManager.LegendPosition.InsideSE);
    chart.getStyleManager().setLegendSeriesLineLength(12);
    chart.getStyleManager().setAxisTitleFont(new Font("SansSerif", 2, 18));
    chart.getStyleManager().setAxisTickLabelsFont(new Font("Serif", 0, 11));
    chart.getStyleManager().setDatePattern("dd-MMM");
    chart.getStyleManager().setDecimalPattern("#0.000");
    chart.getStyleManager().setLocale(Locale.GERMAN);

    Series series = chart.addSeries("Fake Data", xData, yData);
    series.setLineColor(SeriesColor.BLUE);
    series.setMarkerColor(Color.ORANGE);
    series.setMarker(SeriesMarker.CIRCLE);
    series.setLineStyle(SeriesLineStyle.SOLID);

    return chart;
  }
}