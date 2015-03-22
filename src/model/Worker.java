package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;

import util.SqlServerUtil;

public class Worker implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private int ID;
	private String name;
	
	public static final int NOT_STARTED = -1;
	public static final int BREAK = 0;
	public static final int WORKING = 1;
	private int workState;
	
	private long workTime;
	private long lastCompletionTime;
	private int breakTimes;
	
	//completed number of each stage 
	private int completedStages;
	private List<Product.Stage> workingStages = new ArrayList<Product.Stage>(1);
	private Map<String, Integer> scannedItems = new HashMap<>(); //raw material item code

	private List<TimeLineEvent> timeLine = new LinkedList<>();
	private List<Float> AvgEfcList = new LinkedList<>();
		
	private List<Integer> auth = new ArrayList<>();
	
	public Worker(int iD, String name) {
		ID = iD;
		this.name = name;
		
		workState = NOT_STARTED;
	}
	
	public void addAuth(int authLevel) {
		this.auth.add(authLevel);
	}
	
	public int getID() {
		return ID;
	}

	public String getName() {
		return name;
	}
	
	public int getCompletedStages() {
		return completedStages;
	}
	
	public int getWorkState() {
		return workState;
	}
	
	public boolean isOnWork() {
		return workState == WORKING;
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
	
	private long getStandardTime() {		
		long standardTime = 0;
		for(Product.Stage s: workingStages) {
			standardTime += s.getStandardTime();
		}	
		return standardTime;
	}
	
	public long getLastCompletionTime() {  // in mS
		int size = timeLine.size();
		if(size<2) return -1;
		
		long ls = 0;  //last time line
		String lastBarcode = null;
		long fs = 0;  //last item
		long bs = 0;  //break time
		boolean stage = false; 
		boolean stacked = false;
		// Generate an iterator. Start just after the last element.
		ListIterator<TimeLineEvent> it = timeLine.listIterator(timeLine.size());

		// Iterate in reverse.
		while(it.hasPrevious()) {
		  TimeLineEvent te = it.previous();
		  if(te.ts<lastCompletionTime) break;
		  
		  if(Character.isDigit( te.barcode.charAt(0)) ) {
			  stage = true;
			  fs = te.ts;			  
		  }
		  if(!stage) continue;
		  
		  ls = te.ts;
		  lastBarcode = te.barcode;
		  
		  if( Character.isWhitespace( te.barcode.charAt(0)) ) {
			  stacked = !stacked;
			  if( Character.isWhitespace( lastBarcode.charAt(0)) && stacked){
				  bs += ls - te.ts;
			  }
		  }
		  
		}
		
		long ret = fs - lastCompletionTime - bs;
		return ret>0 ? ret : 0;
	}
	
	public float getLastEfficiency(){
		long lastCmpt = getLastCompletionTime();
		return lastCmpt<0 ? 0.0f : lastCmpt * 1.0f / getStandardTime();
	}
	
	public float getAvgEfficiency() {
		if(workTime == 0) return 0.0f;
		
		long standardTime = getStandardTime() * completedStages;

		if(!scannedItems.isEmpty()) {
			for(Integer value : scannedItems.values()) 
				standardTime += value * 5000;    //each scanning may take 5S
		}
		
		return standardTime * 1.0f / workTime;
	}
	
	public float getProductivity() {
		if(workTime==0) return 0.0f;
		long totalTime = getLastTime() - getStartTime();
		return workTime * 1.0f / totalTime;
	}
	
	public float getPerformance() {
		return getAvgEfficiency() * getProductivity();
	}
	
	public List<Float> getEfficiencyList() {
		return AvgEfcList;
	}
	
	public List<Date> getTimelineList() {
		List<Date> date = new LinkedList<Date>();
		for(TimeLineEvent t : timeLine)
			date.add(new Date(t.ts));
		return date;
	}

	private long getStartTime() {
		if(timeLine.isEmpty()) 
			return System.currentTimeMillis();
		else
			return ((LinkedList<TimeLineEvent>)timeLine).getFirst().ts;
	}
	
	private long getLastTime() {
		if(timeLine.isEmpty()) 
			return System.currentTimeMillis();
		else
			return ((LinkedList<TimeLineEvent>)timeLine).getLast().ts;
	}
	
	public long getWorkTime() {
		return workTime;
	}
	
	//called after assignment in Product.assignWorker()
	protected void addStage(Product.Stage s) {
		workingStages.add(s);
	}
	
	public List<TimeLineEvent> getTimeLineEvents() {
		return timeLine;
	}
	
	public Map<String,Integer> getScannedMaterials() {
		return scannedItems;
	}
	
	//mtr: BOM material in setting file
	public int getScannedMaterialAmount(String mtr) {
		if(scannedItems.isEmpty()) return 0;
		for(Entry<String, Integer> entry : scannedItems.entrySet()) {
			String key = entry.getKey();
			if(key.indexOf(mtr)>0) {
				int qIndex = key.lastIndexOf("Q");
//				int iIndex = key.lastIndexOf("I");
				int qty = Integer.valueOf(key.substring(qIndex+1, key.length()));
				return completedStages * qty;
			}
		}
		return -1;
	}
	
	public void addTimeLineEvent(long ts, int workStation, int workerId, String barcode) {
		//if no timeline available, set lastTime to ts so the calculated work-time is 0 
//		long lastTS = getLastTimeline();
//		lastTS = lastTS>0 ? lastTS : ts;
		long lastTS = 0;
		if(workState == NOT_STARTED) {
			lastTS = ts;
			lastCompletionTime = ts;
		} else {
			lastTS = ((LinkedList<TimeLineEvent>) timeLine).getLast().ts;
		}
		
		System.out.println("worker read data: " + workStation + " " + this.ID + " " + barcode + " " + workState);

//		System.out.println(barcode);
		if(barcode==null) barcode = new String("  ");
		else barcode = barcode.trim();
		
		timeLine.add(new TimeLineEvent(ts, workStation, barcode));
		
		if( Character.isWhitespace(barcode.charAt(0))) { //empty bar-code
			if(workState==WORKING) {  //take a break
				workState = BREAK;
				breakTimes++;
				workTime += ts - lastTS;
			}else{         //begin or restart to work
				workState = WORKING;				
			}	
			
			SqlServerUtil.sendMessage(workStation, "Worker ID= " + workerId);
			
		}else if( Character.isDigit(barcode.charAt(0)) ) {  //scan a new item
			workState = WORKING;
			
			completedStages++;
			workTime += ts - lastTS;
			lastCompletionTime = ts;
			
			AvgEfcList.add(getAvgEfficiency());
			
			sendFeedbackMessage(workStation);
			
		}else if(barcode.startsWith("A")){   //scan materials
			workState = WORKING;
			Integer value = scannedItems.get(barcode);
			if(value==null)
				scannedItems.put(barcode, 1);
			else 
				scannedItems.put(barcode, value+1);
			
			sendFeedbackMessage(workStation);
			
		}else if(barcode.startsWith("P")) {  //final product
			sendFeedbackMessage(workStation);
		}
		
	}
	
	private void sendFeedbackMessage(int workStation) {
		long t = getLastCompletionTime();
		if(t>0) t = t/1000; 
		else t = 0;
		int f = (int) (getAvgEfficiency() * 100);	
		SqlServerUtil.sendMessage(workStation, "E"+f+"%"+"Q"+completedStages+"T"+t+"sec");		
	}
	
	public void printTimeLine() {
		for(int i=0; i<timeLine.size(); i++) {
			TimeLineEvent e = timeLine.get(i);
			System.out.println(e.ts + " " + e.workStation + " " + e.barcode );
		}
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

	public List<Integer> getAuthLevels() {
		return auth;
	}
}
