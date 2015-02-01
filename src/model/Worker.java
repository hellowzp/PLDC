package model;

import java.util.LinkedList;
import java.util.List;

public class Worker {
	private int ID;
	private String name;
	
	private boolean working;
	private long workTime;	
	private int products;
	private int breakTimes;
	
	private List<TimeLine> timeLine = new LinkedList<>();
	
	public Worker(int iD, String name) {
		ID = iD;
		this.name = name;
	}
	
	public int getProducts() {
		return products;
	}
	
	public boolean isWorking() {
		return working;
	}
	
	public void setWorking(boolean working) {
		this.working = working;
	}
	
	public float getEfficiency() {
		return workTime/products;  //time in mS to complete one item
	}
	
	public int getID() {
		return ID;
	}

	public String getName() {
		return name;
	}
	
	public int getLastWorkStation(){
		System.out.println("time  size: " + timeLine.size());
		return timeLine.get(timeLine.size()-1).workStation;
	}

	public long getStartTime() {
		if(timeLine.isEmpty()) 
			return System.currentTimeMillis();
		else
			return ((LinkedList<TimeLine>)timeLine).getFirst().timestamp;
	}
	
	public long getLatestTime() {
		if(timeLine.isEmpty()) 
			return System.currentTimeMillis();
		else
			return ((LinkedList<TimeLine>)timeLine).getLast().timestamp;
	}
	
	public long getWorkTime() {
		return workTime;
	}
	
	public List<TimeLine> getTimeLine() {
		return timeLine;
	}
	
	public void addTimeLine(long ts, int workStation, String barcode) {
		if(barcode==null) { 
			if(working) {  //take a break
				working = false;
				breakTimes++;
				workTime += ts - getLatestTime();
			}else{         //begin or restart to work
				working = true;				
			}
			
		}else{	//scan a new item
			if ( Character.isDigit(barcode.charAt(0)) )
				products++;
			workTime += ts - getLatestTime();
		}
		
		
		TimeLine tm = new TimeLine(ts, workStation, barcode);
		timeLine.add(tm);
		System.out.println(tm + " " + timeLine.size());
	}
	
	//refer to HashMap.Node
	static class TimeLine {  //without access modifier: package access
		long timestamp;   
		int workStation;
		String barcode;
		
		TimeLine(long time, int workStation,String barcode) {
			this.timestamp = time;
			this.workStation = workStation;
			this.barcode = barcode;
		}
	}
	
}
