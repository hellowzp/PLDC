package demo;

import com.xeiam.xchart.XChartPanel;
import demo.charts.area.AreaChart01;
import demo.charts.area.AreaChart02;
import demo.charts.area.AreaLineChart03;
import demo.charts.bar.BarChart01;
import demo.charts.bar.BarChart02;
import demo.charts.bar.BarChart03;
import demo.charts.bar.BarChart04;
import demo.charts.bar.BarChart05;
import demo.charts.bar.BarChart06;
import demo.charts.bar.BarChart07;
import demo.charts.bar.BarChart08;
import demo.charts.date.DateChart01;
import demo.charts.date.DateChart02;
import demo.charts.date.DateChart03;
import demo.charts.date.DateChart04;
import demo.charts.date.DateChart05;
import demo.charts.date.DateChart06;
import demo.charts.date.DateChart07;
import demo.charts.line.LineChart01;
import demo.charts.line.LineChart02;
import demo.charts.line.LineChart03;
import demo.charts.line.LineChart04;
import demo.charts.line.LineChart05;
import demo.charts.line.LineChart06;
import demo.charts.realtime.RealtimeChart01;
import demo.charts.realtime.RealtimeChart02;
import demo.charts.scatter.ScatterChart01;
import demo.charts.scatter.ScatterChart02;
import demo.charts.scatter.ScatterChart03;
import demo.charts.scatter.ScatterChart04;
import demo.charts.theme.ThemeChart01;
import demo.charts.theme.ThemeChart02;
import demo.charts.theme.ThemeChart03;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

public class XChartDemo extends JPanel
  implements TreeSelectionListener
{
  private JSplitPane splitPane;
  private JTree tree;
  private XChartPanel chartPanel;
  final RealtimeChart01 realtimeChart01 = new RealtimeChart01();
  final RealtimeChart02 realtimeChart02 = new RealtimeChart02();
  Timer timer = new Timer();

  public XChartDemo()
  {
    super(new GridLayout(1, 0));

    DefaultMutableTreeNode top = new DefaultMutableTreeNode("XChart Example Charts");
    createNodes(top);

    this.tree = new JTree(top);
    this.tree.getSelectionModel().setSelectionMode(1);

    this.tree.addTreeSelectionListener(this);

    JScrollPane treeView = new JScrollPane(this.tree);

    this.chartPanel = new XChartPanel(new AreaChart01().getChart());

    this.splitPane = new JSplitPane(0);
    this.splitPane.setTopComponent(treeView);
    this.splitPane.setBottomComponent(this.chartPanel);

    Dimension minimumSize = new Dimension(130, 160);
    treeView.setMinimumSize(minimumSize);
    this.splitPane.setPreferredSize(new Dimension(700, 700));

    add(this.splitPane);
  }

  public void valueChanged(TreeSelectionEvent e)
  {
    DefaultMutableTreeNode node = (DefaultMutableTreeNode)this.tree.getLastSelectedPathComponent();

    if (node == null) {
      return;
    }

    Object nodeInfo = node.getUserObject();

    if (node.isLeaf()) {
      ChartInfo chartInfo = (ChartInfo)nodeInfo;

      this.chartPanel = new XChartPanel(chartInfo.getExampleChart());
      this.splitPane.setBottomComponent(this.chartPanel);

      this.timer.cancel();
      if (chartInfo.getExampleChartName().startsWith("RealtimeChart01"))
      {
        TimerTask chartUpdaterTask = new TimerTask()
        {
          public void run()
          {
            XChartDemo.this.realtimeChart01.updateData();
            XChartDemo.this.chartPanel.updateSeries("series1", XChartDemo.this.realtimeChart01.getyData());
          }
        };
        this.timer = new Timer();
        this.timer.scheduleAtFixedRate(chartUpdaterTask, 0L, 500L);
      }
      else if (chartInfo.getExampleChartName().startsWith("RealtimeChart02"))
      {
        TimerTask chartUpdaterTask = new TimerTask()
        {
          public void run()
          {
            XChartDemo.this.realtimeChart02.updateData();
            XChartDemo.this.chartPanel.updateSeries("series1", XChartDemo.this.realtimeChart02.getxData(), XChartDemo.this.realtimeChart02.getyData());
          }
        };
        this.timer = new Timer();
        this.timer.scheduleAtFixedRate(chartUpdaterTask, 0L, 500L);
      }
    }
  }

  private void createNodes(DefaultMutableTreeNode top)
  {
    DefaultMutableTreeNode category = null;

    DefaultMutableTreeNode defaultMutableTreeNode = null;

    category = new DefaultMutableTreeNode("Area Charts");
    top.add(category);

    defaultMutableTreeNode = new DefaultMutableTreeNode(new ChartInfo("AreaChart01 - 3-Series", new AreaChart01().getChart()));
    category.add(defaultMutableTreeNode);

    defaultMutableTreeNode = new DefaultMutableTreeNode(new ChartInfo("AreaChart02 - Null Y-Axis Data Points", new AreaChart02().getChart()));
    category.add(defaultMutableTreeNode);

    defaultMutableTreeNode = new DefaultMutableTreeNode(new ChartInfo("AreaLineChart03 - Combination Are & Line Chart", new AreaLineChart03().getChart()));
    category.add(defaultMutableTreeNode);

    category = new DefaultMutableTreeNode("Line Charts");
    top.add(category);

    defaultMutableTreeNode = new DefaultMutableTreeNode(new ChartInfo("LineChart01 -  Logarithmic Y-Axis", new LineChart01().getChart()));
    category.add(defaultMutableTreeNode);

    defaultMutableTreeNode = new DefaultMutableTreeNode(new ChartInfo("LineChart02 - Customized Series Style", new LineChart02().getChart()));
    category.add(defaultMutableTreeNode);

    defaultMutableTreeNode = new DefaultMutableTreeNode(new ChartInfo("LineChart03 - Extensive Chart Customization", new LineChart03().getChart()));
    category.add(defaultMutableTreeNode);

    defaultMutableTreeNode = new DefaultMutableTreeNode(new ChartInfo("LineChart04 - Hundreds of Series on One Plot", new LineChart04().getChart()));
    category.add(defaultMutableTreeNode);

    defaultMutableTreeNode = new DefaultMutableTreeNode(new ChartInfo("LineChart05 - Scatter and Line", new LineChart05().getChart()));
    category.add(defaultMutableTreeNode);

    defaultMutableTreeNode = new DefaultMutableTreeNode(new ChartInfo("LineChart06 - Logarithmic Y-Axis with Error Bars", new LineChart06().getChart()));
    category.add(defaultMutableTreeNode);

    category = new DefaultMutableTreeNode("Scatter Charts");
    top.add(category);

    defaultMutableTreeNode = new DefaultMutableTreeNode(new ChartInfo("ScatterChart01 - Gaussian Blob", new ScatterChart01().getChart()));
    category.add(defaultMutableTreeNode);

    defaultMutableTreeNode = new DefaultMutableTreeNode(new ChartInfo("ScatterChart02 - Logarithmic Data", new ScatterChart02().getChart()));
    category.add(defaultMutableTreeNode);

    defaultMutableTreeNode = new DefaultMutableTreeNode(new ChartInfo("ScatterChart03 - Single point", new ScatterChart03().getChart()));
    category.add(defaultMutableTreeNode);

    defaultMutableTreeNode = new DefaultMutableTreeNode(new ChartInfo("ScatterChart04 - Error Bars", new ScatterChart04().getChart()));
    category.add(defaultMutableTreeNode);

    category = new DefaultMutableTreeNode("Bar Charts");
    top.add(category);

    defaultMutableTreeNode = new DefaultMutableTreeNode(new ChartInfo("BarChart01 - Basic Bar Chart", new BarChart01().getChart()));
    category.add(defaultMutableTreeNode);

    defaultMutableTreeNode = new DefaultMutableTreeNode(new ChartInfo("BarChart02 - Date Categories", new BarChart02().getChart()));
    category.add(defaultMutableTreeNode);

    defaultMutableTreeNode = new DefaultMutableTreeNode(new ChartInfo("BarChart03 - Positive and Negative", new BarChart03().getChart()));
    category.add(defaultMutableTreeNode);

    defaultMutableTreeNode = new DefaultMutableTreeNode(new ChartInfo("BarChart04 - Missing Point in Series", new BarChart04().getChart()));
    category.add(defaultMutableTreeNode);

    defaultMutableTreeNode = new DefaultMutableTreeNode(new ChartInfo("BarChart05 - GGPlot2 Theme", new BarChart05().getChart()));
    category.add(defaultMutableTreeNode);

    defaultMutableTreeNode = new DefaultMutableTreeNode(new ChartInfo("BarChart06 - Histogram Overlapped", new BarChart06().getChart()));
    category.add(defaultMutableTreeNode);

    defaultMutableTreeNode = new DefaultMutableTreeNode(new ChartInfo("BarChart07 - Histogram Not Overlapped", new BarChart07().getChart()));
    category.add(defaultMutableTreeNode);

    defaultMutableTreeNode = new DefaultMutableTreeNode(new ChartInfo("BarChart08 - Histogram with Error Bars", new BarChart08().getChart()));
    category.add(defaultMutableTreeNode);

    category = new DefaultMutableTreeNode("Chart Themes");
    top.add(category);

    defaultMutableTreeNode = new DefaultMutableTreeNode(new ChartInfo("ThemeChart01 - Default XChart Theme", new ThemeChart01().getChart()));
    category.add(defaultMutableTreeNode);

    defaultMutableTreeNode = new DefaultMutableTreeNode(new ChartInfo("ThemeChart02 - GGPlot2 Theme", new ThemeChart02().getChart()));
    category.add(defaultMutableTreeNode);

    defaultMutableTreeNode = new DefaultMutableTreeNode(new ChartInfo("ThemeChart03 - Matlab Theme", new ThemeChart03().getChart()));
    category.add(defaultMutableTreeNode);

    category = new DefaultMutableTreeNode("Date Charts");
    top.add(category);

    defaultMutableTreeNode = new DefaultMutableTreeNode(new ChartInfo("DateChart01 - Millisecond Scale", new DateChart01().getChart()));
    category.add(defaultMutableTreeNode);

    defaultMutableTreeNode = new DefaultMutableTreeNode(new ChartInfo("DateChart02 - Second Scale", new DateChart02().getChart()));
    category.add(defaultMutableTreeNode);

    defaultMutableTreeNode = new DefaultMutableTreeNode(new ChartInfo("DateChart03 - Minute Scale", new DateChart03().getChart()));
    category.add(defaultMutableTreeNode);

    defaultMutableTreeNode = new DefaultMutableTreeNode(new ChartInfo("DateChart04 - Hour Scale", new DateChart04().getChart()));
    category.add(defaultMutableTreeNode);

    defaultMutableTreeNode = new DefaultMutableTreeNode(new ChartInfo("DateChart05 - Day Scale", new DateChart05().getChart()));
    category.add(defaultMutableTreeNode);

    defaultMutableTreeNode = new DefaultMutableTreeNode(new ChartInfo("DateChart06 - Month Scale", new DateChart06().getChart()));
    category.add(defaultMutableTreeNode);

    defaultMutableTreeNode = new DefaultMutableTreeNode(new ChartInfo("DateChart07 - Year Scale", new DateChart07().getChart()));
    category.add(defaultMutableTreeNode);

    category = new DefaultMutableTreeNode("Real-time Charts");
    top.add(category);

    defaultMutableTreeNode = new DefaultMutableTreeNode(new ChartInfo("RealtimeChart01 - Fixed X-Axis Window", this.realtimeChart01.getChart()));
    category.add(defaultMutableTreeNode);

    defaultMutableTreeNode = new DefaultMutableTreeNode(new ChartInfo("RealtimeChart02 - Updating X-Axis Window", this.realtimeChart02.getChart()));
    category.add(defaultMutableTreeNode);
  }

  private static void createAndShowGUI()
  {
    JFrame frame = new JFrame("XChart Demo");
    frame.setDefaultCloseOperation(3);

    frame.add(new XChartDemo());

    frame.pack();
    frame.setVisible(true);
  }

  public static void main(String[] args)
  {
    SwingUtilities.invokeLater(new Runnable()
    {
      public void run()
      {
        XChartDemo.createAndShowGUI();
      }
    });
  }
}