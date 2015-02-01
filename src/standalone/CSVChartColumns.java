package standalone;

import com.xeiam.xchart.CSVExporter;
import com.xeiam.xchart.CSVImporter;
import com.xeiam.xchart.CSVImporter.DataOrientation;
import com.xeiam.xchart.Chart;
import com.xeiam.xchart.Series;
import com.xeiam.xchart.SwingWrapper;
import java.util.Map;

public class CSVChartColumns
{
  public static void main(String[] args)
    throws Exception
  {
    Chart chart = CSVImporter.getChartFromCSVDir("./CSV/CSVChartColumns/", CSVImporter.DataOrientation.Columns, 600, 600);

    CSVExporter.writeCSVColumns((Series)chart.getSeriesMap().get("series1"), "./CSV/CSVChartColumnsExport/");

    new SwingWrapper(chart).displayChart();
  }
}