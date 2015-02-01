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

public class RealtimeChart03
  implements ExampleChart
{
  private List<Integer> xData = new ArrayList();
  private List<Double> yData = new ArrayList();
  private List<Double> errorBars = new ArrayList();
  public static final String SERIES_NAME = "series1";

  public static void main(String[] args)
  {
    final RealtimeChart03 realtimeChart03 = new RealtimeChart03();
    final XChartPanel chartPanel = realtimeChart03.buildPanel();

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
        realtimeChart03.updateData();
        chartPanel.updateSeries("series1", realtimeChart03.xData, realtimeChart03.getyData(), realtimeChart03.errorBars);
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
    this.yData.add(Double.valueOf(0.0D));
    for (int i = 0; i < 50; i++) {
      double lastPoint = ((Double)this.yData.get(this.yData.size() - 1)).doubleValue();
      this.yData.add(getRandomWalk(lastPoint));
    }

    this.xData = new ArrayList();
    for (int i = 1; i < this.yData.size() + 1; i++) {
      this.xData.add(Integer.valueOf(i));
    }

    this.errorBars = new ArrayList();
    for (int i = 0; i < this.yData.size(); i++) {
      this.errorBars.add(Double.valueOf(20.0D * Math.random()));
    }

    Chart chart = new Chart(500, 400);
    chart.setChartTitle("Sample Real-time Chart");
    chart.setXAxisTitle("X");
    chart.setYAxisTitle("Y");
    chart.addSeries("series1", this.xData, this.yData, this.errorBars);

    return chart;
  }

  private Double getRandomWalk(double lastPoint)
  {
    return Double.valueOf(lastPoint + (Math.random() * 100.0D - 50.0D));
  }

  public void updateData()
  {
    double lastPoint = ((Double)this.yData.get(this.yData.size() - 1)).doubleValue();
    this.yData.add(getRandomWalk(lastPoint));
    this.yData.remove(0);

    this.errorBars.add(Double.valueOf(20.0D * Math.random()));
    this.errorBars.remove(0);
  }

  public List<Double> getyData()
  {
    return this.yData;
  }
}