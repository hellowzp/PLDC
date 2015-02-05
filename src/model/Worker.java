package model;

import java.util.LinkedList;
import java.util.List;

import com.sun.org.apache.bcel.internal.generic.RETURN;

public class Worker {
	private int ID;
	private String name;
	
	public static final int NOT_STARTED = -1;
	public static final int BREAK = 0;
	public static final int WORKING = 1;
	private int workState;
	
	private long workTime;	
	private int products;
	private int breakTimes;
	
	private List<TimeLineEvent> timeLine = new LinkedList<>();
	
	public Worker(int iD, String name) {
		ID = iD;
		this.name = name;
		
		workState = NOT_STARTED;
	}
	
	public int getProducts() {
		return products;
	}
	
	public int getWorkState() {
		return workState;
	}
	
	public String getWorkStateString() {
		if(workState == NOT_STARTED) return "Off work";
		else if(workState == BREAK) return "Break";
		else return "Working";
	}
	
	public boolean isWorking() {
		return workState != NOT_STARTED;
	}
	
	public int getBreakTimes() {
		return breakTimes;
	}
	
	public float getEfficiency() {
		if(products==0) return 0;
		return workTime/products;  //time in mS to complete one item
	}
	
	public float getProductivity() {
		if(timeLine.isEmpty()) return 0.0f;
		long totalTime = getLatestTime() - getStartTime();
		return workTime * 1.00f / totalTime;
	}
	
	public float getPerformance() {
		return getEfficiency() * getProductivity();
	}
	
	public int getID() {
		return ID;
	}

	public String getName() {
		return name;
	}
	
	public int getLastWorkStation(){
//		System.out.println("Worker ID: " + ID + " timeLine size: " + timeLine.size());
		if(timeLine.isEmpty()) 
			return 0; 
		return timeLine.get(timeLine.size()-1).workStation;
	}

	public long getStartTime() {
		if(timeLine.isEmpty()) 
			return System.currentTimeMillis();
		else
			return ((LinkedList<TimeLineEvent>)timeLine).getFirst().ts;
	}
	
	public long getLatestTime() {
		if(timeLine.isEmpty()) 
			return 0;
		else
			return ((LinkedList<TimeLineEvent>)timeLine).getLast().ts;
	}
	
	public long getWorkTime() {
		return workTime;
	}
	
	public List<TimeLineEvent> getTimeLine() {
		return timeLine;
	}
	
	public void printTimeLine() {
		for(int i=0; i<timeLine.size(); i++) {
			TimeLineEvent e = timeLine.get(i);
			System.out.println(e.ts + " " + e.workStation + " " + e.barcode );
		}
	}
	
	public void addTimeLineEvent(long ts, int workStation, String barcode) {
		long lastTS = getLatestTime();
		lastTS = lastTS>0 ? lastTS : ts;
		
		if( Character.isWhitespace(barcode.charAt(0)) ) { 
			if(workState==WORKING) {  //take a break
				workState = BREAK;
				breakTimes++;
				workTime += ts - lastTS;
			}else{         //begin or restart to work
				workState = WORKING;				
			}			
		}else if( Character.isDigit(barcode.charAt(0)) ){	//scan a new item
			products++;
			workTime += ts - lastTS;
		}else{   //scan materials
			workTime += ts - lastTS;
		}
		
		timeLine.add(new TimeLineEvent(ts, workStation, barcode));
	}
	
	//refer to HashMap.Node
	static class TimeLineEvent {  //without access modifier: package access
		long ts;   
		int workStation;
		String barcode;
		
		TimeLineEvent(long time, int workStation,String barcode) {
			this.ts = time;
			this.workStation = workStation;
			this.barcode = barcode;
		}
	}
	
	public static void main(String[] args){
		Worker w = new Worker(1,"wang");

		for(int i=0; i<20; i++) {
			w.addTimeLineEvent(System.currentTimeMillis(), i, ""+i);
		}
		System.out.println(w.timeLine.size() + " " + w.workTime);
	}
	
	public enum WorkState {
		NOT_STARTED(-1), WORKING(1), BREAK(0);
		
		private int value;
		
		WorkState(int value) {
			this.value = value;
		}
		
		public int getValue() {
			return value;
		}
	}

}
