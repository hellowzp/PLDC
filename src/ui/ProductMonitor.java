package ui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import model.Repository;

import java.awt.Dimension;
import java.awt.FlowLayout;

public class ProductMonitor extends JPanel  implements RealTimeDisplay{
	
	private static final long serialVersionUID = 1L;
	private List<StagePanel> stagePanels = new ArrayList<>(40);
	
	public ProductMonitor() {
		setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		setPreferredSize(new Dimension(1350,660));
		for(int i=1; i<=40; i++) {
			StagePanel p = new StagePanel(i, null); 
			stagePanels.add(p);
			add(p);
		}		
	}

	@Override
	public void updateDisplay() {
		for(StagePanel s : stagePanels) {
			s.updateDisplay();
		}
	}

	public void updateStages() {
		for(int i=1; i<=40; i++) {
			stagePanels.get(i-1).setStage(Repository.getInstance().getStage(i));
		}
		updateDisplay();
	}
}
