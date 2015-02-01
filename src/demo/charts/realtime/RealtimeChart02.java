package demo.charts.realtime;

import com.xeiam.xchart.Chart;
import com.xeiam.xchart.XChartPanel;

import demo.charts.ExampleChart;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class RealtimeChart02
  implements ExampleChart
{
  public static final String SERIES_NAME = "series1";
  private List<Integer> xData;
  private List<Double> yData;

  public static void main(String[] args)
  {
    final RealtimeChart02 realtimeChart02 = new RealtimeChart02();
    final XChartPanel chartPanel = realtimeChart02.buildPanel();

    SwingUtilities.invokeLater(new Runnable()
    {
      public void run()
      {
        JFrame frame = new JFrame("XChart");
        frame.setDefaultCloseOperation(3);
        frame.add(chartPanel);

        frame.pack();
        frame.setVisible(true);
      }
    });
    TimerTask chartUpdaterTask = new TimerTask()
    {
      public void run()
      {
        realtimeChart02.updateData();
        chartPanel.updateSeries("series1", realtimeChart02.getxData(), realtimeChart02.getyData());
      }
    };
    Timer timer = new Timer();
    timer.scheduleAtFixedRate(chartUpdaterTask, 0L, 500L);
  }

  public XChartPanel buildPanel()
  {
    return new XChartPanel(getChart());
  }

  public Chart getChart()
  {
    this.xData = getMonotonicallyIncreasingData(5);
    this.yData = getRandomData(5);

    Chart chart = new Chart(500, 400);
    chart.setChartTitle("Sample Real-time Chart");
    chart.setXAxisTitle("X");
    chart.setYAxisTitle("Y");
    chart.addSeries("series1", this.xData, this.yData);

    return chart;
  }

  private List<Double> getRandomData(int numPoints)
  {
    List<Double> data = new ArrayList<Double>();
    for (int i = 0; i < numPoints; i++) {
      data.add(Double.valueOf(Math.random() * 100.0D));
    }
    return data;
  }

  private List<Integer> getMonotonicallyIncreasingData(int numPoints)
  {
    List<Integer> data = new ArrayList<Integer>();
    for (int i = 0; i < numPoints; i++) {
      data.add(Integer.valueOf(i));
    }
    return data;
  }

  public void updateData()
  {
    List<Double> newData = getRandomData(1);

    this.yData.addAll(newData);

    while (this.yData.size() > 20) {
      this.yData.remove(0);
    }

    this.xData.add(Integer.valueOf(((Integer)this.xData.get(this.xData.size() - 1)).intValue() + 1));
    while (this.xData.size() > 20)
      this.xData.remove(0);
  }

  public List<Double> getyData()
  {
    return this.yData;
  }

  public List<Integer> getxData()
  {
    return this.xData;
  }
}