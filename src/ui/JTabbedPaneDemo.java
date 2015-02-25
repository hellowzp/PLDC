package ui;

import javax.swing.JTabbedPane;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.*;
import java.awt.event.*;
import java.util.Timer;
import java.util.TimerTask;

public class JTabbedPaneDemo extends JPanel {
    
	private static final long serialVersionUID = 1L;
	    
    static{
    	
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
    	
    }

	public JTabbedPaneDemo() {
        ImageIcon icon = new ImageIcon("java-swing-tutorial.JPG");
        final JTabbedPane jtbExample = new JTabbedPane();
        jtbExample.setBorder(new BevelBorder(BevelBorder.RAISED)); 
        
        JPanel jplInnerPanel1 = createInnerPanel("Tab 1 Contains Tooltip and Icon");
        jtbExample.addTab("One", icon, jplInnerPanel1, "Tab 1");
        jtbExample.setSelectedIndex(0);

        JPanel jplInnerPanel2 = createInnerPanel("Tab 2 Contains Icon only");
        jtbExample.addTab("Two", icon, jplInnerPanel2);

        JPanel jplInnerPanel3  = createInnerPanel("Tab 3 Contains Tooltip and Icon");
        jtbExample.addTab("Three", icon, jplInnerPanel3, "Tab 3");

        JPanel jplInnerPanel4 = createInnerPanel("Tab 4 Contains Text only");
        jtbExample.addTab("Four", jplInnerPanel4);

        //Add the tabbed pane to this panel.
        setLayout(new GridLayout(1, 1)); 
        add(jtbExample);
        
        final ChangeListener changeListener = new ChangeListener() {
            public void stateChanged(ChangeEvent changeEvent) {
              JTabbedPane sourceTabbedPane = (JTabbedPane) changeEvent.getSource();
              int index = sourceTabbedPane.getSelectedIndex();
              System.out.println("Tab changed to: " + sourceTabbedPane.getTitleAt(index));
            }
        };
        jtbExample.addChangeListener(changeListener);
        
        final Timer timer = new Timer(true);
    	final TimerTask task = new TimerTask() {
    		int index = 0;
    		
    		@Override
    		public void run() {
    			index++;
    			if(index>3) index=0;
    			System.out.println(index);
    			jtbExample.setSelectedIndex(index);
    			ChangeEvent e = new ChangeEvent(jtbExample);
    			changeListener.stateChanged(e);
    		}		
    	};
    	timer.schedule(task, 10, 2000);
    }

    protected JPanel createInnerPanel(String text) {
        JPanel jplPanel = new JPanel();
        JLabel jlbDisplay = new JLabel(text);
        jlbDisplay.setHorizontalAlignment(JLabel.CENTER);
        jplPanel.setLayout(new GridLayout(1, 1));
        jplPanel.add(jlbDisplay);
        return jplPanel;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("TabbedPane Source Demo");
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
            	System.exit(0);}
        });

        frame.getContentPane().add( new JTabbedPaneDemo(), BorderLayout.CENTER);
        frame.setSize(400, 125);
        //frame.setBounds(100, 100, 800, 600);
        //frame.pack();
        frame.setVisible(true);
    }
}
