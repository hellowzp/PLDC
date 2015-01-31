package model;

import java.util.List;

import view.EfficiencyPlot;

public class Worker {
	private int startTime = -1;
	private int endTime = -1;
	private int workTime = -1;
	
	private int products;
	private float efficiency;
	
	private int ID;
	private String name;
	
	private List<WorkerData> timeLine;
	
	public Worker(int iD, String name) {
		ID = iD;
		this.name = name;
	}
	
	public float getEfficiency() {
		return efficiency;
	}
	
	public void updateEfficiency() {
		
	}
	
	public int getID() {
		return ID;
	}

	public String getName() {
		return name;
	}

	public int getStartTime() {
		return startTime;
	}
	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}
	public int getEndTime() {
		return endTime;
	}
	public void setEndTime(int endTime) {
		this.endTime = endTime;
	}
	public int getWorkTime() {
		return workTime;
	}
	public void setWorkTime(int workTime) {
		this.workTime = workTime;
	}
	
	public List<WorkerData> getTimeLine() {
		return timeLine;
	}
	
	public void addTimeLine(int time, int workStation) {
		timeLine.add(new WorkerData(time, workStation));
	}
	
	private class WorkerData {
		private int time;
		private int workStation;
		
		private WorkerData(int time, int workStation) {
			this.time = time;
			this.workStation = workStation;
		}
	}
}
