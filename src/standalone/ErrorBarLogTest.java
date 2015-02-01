package standalone;

import com.xeiam.xchart.Chart;
import com.xeiam.xchart.Series;
import com.xeiam.xchart.SeriesColor;
import com.xeiam.xchart.SeriesLineStyle;
import com.xeiam.xchart.SeriesMarker;
import com.xeiam.xchart.StyleManager;
import com.xeiam.xchart.SwingWrapper;
import java.awt.Color;

public class ErrorBarLogTest
{
  public static void main(String[] args)
    throws Exception
  {
    double[] xData = { 0.0D, 1.0D, 2.0D, 3.0D, 4.0D, 5.0D, 6.0D };

    double[] yData1 = { 100.0D, 100.0D, 100.0D, 60.0D, 10.0D, 10.0D, 10.0D };

    double[] yData2 = { 150.0D, 120.0D, 110.0D, 112.0D, 19.0D, 12.0D, 11.0D };

    double[] yData3 = { 50.0D, 80.0D, 90.0D, 8.0D, 1.0D, 8.0D, 9.0D };

    double[] errdata = { 50.0D, 20.0D, 10.0D, 52.0D, 9.0D, 2.0D, 1.0D };

    Chart mychart = new Chart(1200, 800);

    mychart.getStyleManager().setYAxisLogarithmic(true);

    mychart.getStyleManager().setYAxisMin(0.08D);

    mychart.getStyleManager().setYAxisMax(1000.0D);

    mychart.getStyleManager().setErrorBarsColor(Color.black);

    Series series1 = mychart.addSeries("Error bar test data", xData, yData1, errdata);

    Series series2 = mychart.addSeries("Y+error", xData, yData2);

    Series series3 = mychart.addSeries("Y-error", xData, yData3);

    series1.setLineStyle(SeriesLineStyle.SOLID);

    series1.setMarker(SeriesMarker.DIAMOND);

    series1.setMarkerColor(Color.MAGENTA);

    series2.setLineStyle(SeriesLineStyle.DASH_DASH);

    series2.setMarker(SeriesMarker.NONE);

    series2.setLineColor(SeriesColor.RED);

    series3.setLineStyle(SeriesLineStyle.DASH_DASH);

    series3.setMarker(SeriesMarker.NONE);

    series3.setLineColor(SeriesColor.RED);

    new SwingWrapper(mychart).displayChart();
  }
}