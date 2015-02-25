package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Worker {
	private int ID;
	private String name;
	
	public static final int NOT_STARTED = -1;
	public static final int BREAK = 0;
	public static final int WORKING = 1;
	private int workState;
	
	private long workTime;	
	private int breakTimes;
	
	//completed number of each stage 
	private Map<Product.Stage, Integer> completedProductStages;
	private List<String> scannedMaterials = new ArrayList<String>();
	private List<TimeLineEvent> timeLine = new LinkedList<>();
	
	public Worker(int iD, String name) {
		ID = iD;
		this.name = name;
		
		workState = NOT_STARTED;
		completedProductStages = new HashMap<Product.Stage, Integer>();
	}
	
	public int getID() {
		return ID;
	}

	public String getName() {
		return name;
	}
	
	public int getCompletedProducts() {
		int n = 0;
		for(Integer value : completedProductStages.values()) {
			n += value;
		}
		return n;
	}
	
	public int getWorkState() {
		return workState;
	}
	
	public String getWorkStateString() {
		if(workState == NOT_STARTED) return "Off Work";
		else if(workState == BREAK) return "Work Break";
		else return "On Work";
	}
	
	public boolean isWorking() {
		return workState != NOT_STARTED;
	}
	
	public int getBreakTimes() {
		return breakTimes;
	}
	
	public float getEfficiency() {
		long standardTime = 0;
		for(Entry<Product.Stage, Integer> entry : completedProductStages.entrySet()) {
			standardTime += entry.getValue() * entry.getKey().standardTime;
		}
		return standardTime>0 ? workTime*1.0f/standardTime : 0.0f;  
	}
	
	public float getProductivity() {
		if(timeLine.isEmpty()) return 0.0f;
		long totalTime = getLastTime() - getStartTime();
		return workTime * 1.00f / totalTime;
	}
	
	public float getPerformance() {
		return getEfficiency() * getProductivity();
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
	
	public long getLastTime() {
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
	
	public List<String> getScannedMaterials() {
		return scannedMaterials;
	}
	
	public void printTimeLine() {
		for(int i=0; i<timeLine.size(); i++) {
			TimeLineEvent e = timeLine.get(i);
			System.out.println(e.ts + " " + e.workStation + " " + e.barcode );
		}
	}
	
	public void addTimeLineEvent(long ts, int workStation, String barcode) {
		//if no timeline available, set lastTime to ts so the calculated work-time is 0 
		long lastTS = getLastTime();
		lastTS = lastTS>0 ? lastTS : ts;
		
		if( Character.isWhitespace(barcode.charAt(0)) ) { //empty bar-code
			if(workState==WORKING) {  //take a break
				workState = BREAK;
				breakTimes++;
				workTime += ts - lastTS;
			}else{         //begin or restart to work
				workState = WORKING;				
			}			
		}else{
			if( Character.isDigit(barcode.charAt(0)) ){ //scan a new item
				Product.Stage s = Repository.getInstance().getStage(workStation);
				if(completedProductStages.containsKey(s)){
					int value = completedProductStages.get(s) + 1;
					completedProductStages.replace(s, value);
				}else{
					completedProductStages.put(s, 1);
				}
				workTime += ts - lastTS;
			}else{   //scan materials
				scannedMaterials.add(barcode);
				workTime += ts - lastTS;
			}
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
