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
	
	private List<TimeLineEvent> timeLine = new LinkedList<>();
	
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
		System.out.println("Worker ID: " + ID + " timeLine size: " + timeLine.size());
		if(timeLine.isEmpty()) 
			return 0; 
		return timeLine.get(timeLine.size()-1).workStation;
	}

	public long getStartTime() {
		if(timeLine.isEmpty()) 
			return System.currentTimeMillis();
		else
			return ((LinkedList<TimeLineEvent>)timeLine).getFirst().timestamp;
	}
	
	public long getLatestTime() {
		if(timeLine.isEmpty()) 
			return System.currentTimeMillis();
		else
			return ((LinkedList<TimeLineEvent>)timeLine).getLast().timestamp;
	}
	
	public long getWorkTime() {
		return workTime;
	}
	
	public List<TimeLineEvent> getTimeLine() {
		return timeLine;
	}
	
	public void addTimeLineEvent(long ts, int workStation, String barcode) {
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
		
		timeLine.add(new TimeLineEvent(ts, workStation, barcode));
	}
	
	//refer to HashMap.Node
	static class TimeLineEvent {  //without access modifier: package access
		long timestamp;   
		int workStation;
		String barcode;
		
		TimeLineEvent(long time, int workStation,String barcode) {
			this.timestamp = time;
			this.workStation = workStation;
			this.barcode = barcode;
		}
	}
	
	public static void main(String[] args){
		Worker w = new Worker(1,"wang");

		for(int i=0; i<20; i++) {
			w.addTimeLineEvent(System.currentTimeMillis(), i, ""+i);
		}
		System.out.println(w.timeLine.size() + " " + w.working + " " + w.workTime);
	}
	
}
