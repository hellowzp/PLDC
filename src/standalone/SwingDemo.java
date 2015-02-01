package standalone;

import com.xeiam.xchart.XChartPanel;
import demo.charts.area.AreaChart01;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class SwingDemo
{
  private static void createAndShowGUI()
  {
    JFrame frame = new JFrame("XChart Swing Demo");
    frame.setDefaultCloseOperation(3);

    JPanel chartPanel = new XChartPanel(new AreaChart01().getChart());
    frame.add(chartPanel);

    frame.pack();
    frame.setVisible(true);
  }

  public static void main(String[] args)
  {
    SwingUtilities.invokeLater(new Runnable()
    {
      public void run()
      {
        SwingDemo.createAndShowGUI();
      }
    });
  }
}