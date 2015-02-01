package standalone;

import com.xeiam.xchart.Chart;
import com.xeiam.xchart.QuickChart;
import com.xeiam.xchart.VectorGraphicsEncoder;
import com.xeiam.xchart.VectorGraphicsEncoder.VectorGraphicsFormat;
import java.io.IOException;

public class VectorGraphicsTest
{
  public static void main(String[] args)
    throws IOException
  {
    double[] xData = { 0.0D, 1.0D, 2.0D };
    double[] yData = { 2.0D, 1.0D, 0.0D };

    Chart chart = QuickChart.getChart("Sample Chart", "X", "Y", "y(x)", xData, yData);

    VectorGraphicsEncoder.saveVectorGraphic(chart, "", VectorGraphicsEncoder.VectorGraphicsFormat.PDF);
  }
}