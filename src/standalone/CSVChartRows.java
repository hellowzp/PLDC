package standalone;

import com.xeiam.xchart.CSVExporter;
import com.xeiam.xchart.CSVImporter;
import com.xeiam.xchart.CSVImporter.DataOrientation;
import com.xeiam.xchart.Chart;
import com.xeiam.xchart.Series;
import com.xeiam.xchart.SwingWrapper;
import java.util.Map;

public class CSVChartRows
{
  public static void main(String[] args)
    throws Exception
  {
    Chart chart = CSVImporter.getChartFromCSVDir("./CSV/CSVChartRows/", CSVImporter.DataOrientation.Rows, 600, 400);

    CSVExporter.writeCSVRows((Series)chart.getSeriesMap().get("series1"), "./CSV/CSVChartRowsExport/");

    new SwingWrapper(chart).displayChart();
  }
}