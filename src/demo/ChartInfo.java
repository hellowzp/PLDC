package demo;

import com.xeiam.xchart.Chart;

public final class ChartInfo
{
  private final String exampleChartName;
  private final Chart exampleChart;

  public ChartInfo(String exampleChartName, Chart exampleChart)
  {
    this.exampleChartName = exampleChartName;
    this.exampleChart = exampleChart;
  }

  public String getExampleChartName()
  {
    return this.exampleChartName;
  }

  public Chart getExampleChart()
  {
    return this.exampleChart;
  }

  public String toString()
  {
    return this.exampleChartName;
  }
}