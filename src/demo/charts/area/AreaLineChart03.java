package demo.charts.area;

import com.xeiam.xchart.Chart;
import com.xeiam.xchart.Series;
import com.xeiam.xchart.Series.SeriesType;
import com.xeiam.xchart.SeriesMarker;
import com.xeiam.xchart.StyleManager;
import com.xeiam.xchart.StyleManager.ChartType;
import com.xeiam.xchart.StyleManager.LegendPosition;
import com.xeiam.xchart.StyleManager.TextAlignment;
import com.xeiam.xchart.SwingWrapper;
import demo.charts.ExampleChart;

public class AreaLineChart03
  implements ExampleChart
{
  public static void main(String[] args)
  {
    ExampleChart exampleChart = new AreaLineChart03();
    Chart chart = exampleChart.getChart();
    new SwingWrapper(chart).displayChart();
  }

  public Chart getChart()
  {
    Chart chart = new Chart(800, 600);

    chart.setChartTitle(getClass().getSimpleName());
    chart.setXAxisTitle("Age");
    chart.setYAxisTitle("Amount");
    chart.getStyleManager().setLegendPosition(StyleManager.LegendPosition.InsideNW);
    chart.getStyleManager().setChartType(StyleManager.ChartType.Line);

    double[] xAges = { 60.0D, 61.0D, 62.0D, 63.0D, 64.0D, 65.0D, 66.0D, 67.0D, 68.0D, 69.0D, 70.0D, 71.0D, 72.0D, 73.0D, 74.0D, 75.0D, 76.0D, 77.0D, 78.0D, 79.0D, 80.0D, 81.0D, 82.0D, 83.0D, 84.0D, 85.0D, 86.0D, 87.0D, 88.0D, 89.0D, 90.0D, 91.0D, 92.0D, 93.0D, 94.0D, 95.0D, 96.0D, 97.0D, 98.0D, 99.0D, 100.0D };

    double[] yLiability = { 672234.0D, 691729.0D, 711789.0D, 732431.0D, 753671.0D, 775528.0D, 798018.0D, 821160.0D, 844974.0D, 869478.0D, 907735.0D, 887139.0D, 865486.0D, 843023.0D, 819621.0D, 795398.0D, 770426.0D, 744749.0D, 719011.0D, 693176.0D, 667342.0D, 641609.0D, 616078.0D, 590846.0D, 565385.0D, 540002.0D, 514620.0D, 489380.0D, 465149.0D, 441817.0D, 419513.0D, 398465.0D, 377991.0D, 358784.0D, 340920.0D, 323724.0D, 308114.0D, 293097.0D, 279356.0D, 267008.0D, 254873.0D };

    double[] yPercentile75th = { 800000.0D, 878736.0D, 945583.0D, 1004209.0D, 1083964.0D, 1156332.0D, 1248041.0D, 1340801.0D, 1440138.0D, 1550005.0D, 1647728.0D, 1705046.0D, 1705032.0D, 1710672.0D, 1700847.0D, 1683418.0D, 1686522.0D, 1674901.0D, 1680456.0D, 1679164.0D, 1668514.0D, 1672860.0D, 1673988.0D, 1646597.0D, 1641842.0D, 1653758.0D, 1636317.0D, 1620725.0D, 1589985.0D, 1586451.0D, 1559507.0D, 1544234.0D, 1529700.0D, 1507496.0D, 1474907.0D, 1422169.0D, 1415079.0D, 1346929.0D, 1311689.0D, 1256114.0D, 1221034.0D };

    double[] yPercentile50th = { 800000.0D, 835286.0D, 873456.0D, 927048.0D, 969305.0D, 1030749.0D, 1101102.0D, 1171396.0D, 1246486.0D, 1329076.0D, 1424666.0D, 1424173.0D, 1421853.0D, 1397093.0D, 1381882.0D, 1364562.0D, 1360050.0D, 1336885.0D, 1340431.0D, 1312217.0D, 1288274.0D, 1271615.0D, 1262682.0D, 1237287.0D, 1211335.0D, 1191953.0D, 1159689.0D, 1117412.0D, 1078875.0D, 1021020.0D, 974933.0D, 910189.0D, 869154.0D, 798476.0D, 744934.0D, 674501.0D, 609237.0D, 524516.0D, 442234.0D, 343960.0D, 257025.0D };

    double[] yPercentile25th = { 800000.0D, 791439.0D, 809744.0D, 837020.0D, 871166.0D, 914836.0D, 958257.0D, 1002955.0D, 1054094.0D, 1118934.0D, 1194071.0D, 1185041.0D, 1175401.0D, 1156578.0D, 1132121.0D, 1094879.0D, 1066202.0D, 1054411.0D, 1028619.0D, 987730.0D, 944977.0D, 914929.0D, 880687.0D, 809330.0D, 783318.0D, 739751.0D, 696201.0D, 638242.0D, 565197.0D, 496959.0D, 421280.0D, 358113.0D, 276518.0D, 195571.0D, 109514.0D, 13876.0D, 29.0D, 0.0D, 0.0D, 0.0D, 0.0D };

    Series seriesLiability = chart.addSeries("Liability", xAges, yLiability);
    seriesLiability.setMarker(SeriesMarker.NONE);
    seriesLiability.setSeriesType(Series.SeriesType.Area);

    Series seriesPercentile75th = chart.addSeries("75th Percentile", xAges, yPercentile75th);
    seriesPercentile75th.setMarker(SeriesMarker.NONE);

    Series seriesPercentile50th = chart.addSeries("50th Percentile", xAges, yPercentile50th);
    seriesPercentile50th.setMarker(SeriesMarker.NONE);

    Series seriesPercentile25th = chart.addSeries("25th Percentile", xAges, yPercentile25th);
    seriesPercentile25th.setMarker(SeriesMarker.NONE);

    chart.getStyleManager().setYAxisLabelAlignment(StyleManager.TextAlignment.Right);
    chart.getStyleManager().setYAxisDecimalPattern("$ #,###.##");

    chart.getStyleManager().setPlotPadding(0);
    chart.getStyleManager().setAxisTickSpacePercentage(0.95D);

    return chart;
  }
}