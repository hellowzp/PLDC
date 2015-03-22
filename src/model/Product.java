package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Product implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String barcode;
	private List<Stage> stages = new ArrayList<>();
	
	public Product(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public String getBarcode() {
		return barcode;
	}
	
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	
	public List<Stage> getStages() {
		return stages;
	}
	
	public List<String> getStageCodes() {
		List<String> codes = new ArrayList<String>(stages.size()); 
		for(Stage s : stages) {
			codes.add(s.getCode());
		}
		return codes;
	}
	
	public Stage getStage(int workStation) {
		for(Stage s : stages) {
			System.out.println(s.workStation);
			if(s.workStation == workStation) 
				return s;
		}
		return null;
	}
	
	public Stage getStage(String code) {
		for(Stage s : stages) {
			if(s.code.equals(code)) 
				return s;
		}
		return null;
	}
	
	public void addStage(Stage stage) {
		stages.add(stage);
	}
	
	public long getStageTime(int ws) {
		Stage s = stages.get(ws);
		if(s==null) return Integer.MAX_VALUE;
		return s.stdTime;
	}
	
	public long getCompletionTime(){
		long t = 0;
		for(Stage s : stages) {
			t += s.stdTime;
		}
		return t;
	}
	
	//may consider using LinkedHashMap
//	public String getBarcode() {
//		if(stages.isEmpty())
//			return null;	
//		return stages.values().iterator().next().ID;
//	}
	
	@Override
	public boolean equals(Object object) {
		if(this == object) 
            return true;
        if(object instanceof Product) 
			return ((Product)object).name.equalsIgnoreCase(name);
		return 	false;
	}
	
	//may consider using the builder pattern and reflection to invoke method dynamically
	//http://www.rgagnon.com/javadetails/java-0031.html
	public static class Stage{
		private String code;
		private int authLevel;
		private int workStation;
		private int workerID;
		private long stdTime;  //in mS
		private long minTime;
		private long maxTime;
		
		private Map<String, Float> BOM = new HashMap<>();
//		EnumMap<RawMaterial, Integer> BOM = new EnumMap<RawMaterial, Integer>(RawMaterial.class);;
		
		public Stage(String code) {
			this.code = code;
		}
		
//		public Stage(int ws, long stdTime) {
//			workStation = ws;
//			this.stdTime = stdTime;
//		}
//		
//		public Stage(int ws, int id, long stdTime) {
//			workStation = ws;
//			workerID = id;
//			this.stdTime = stdTime;
//		}
		
//		public Stage(String code, long standardTime, int level) {
//			this.code = code;
//			this.stdTime = standardTime;
//			this.authLevel = level;
//		}
		
//		public Stage(String code, long standardTime, long minTime, long maxTime) {
//			this.code = code;
//			this.stdTime = standardTime;
//			this.minTime = minTime;
//			this.maxTime = maxTime;
//		}

		public String getCode() {
			return code;
		}

		public long getStandardTime() {
			return stdTime;
		}
		
		public void setStdTime(long time) {
			this.stdTime = time;
		}

		public long getMinTime() {
			return minTime;
		}

		public long getMaxTime() {
			return maxTime;
		}

		public void assignWorkStation(int workStation) {
			this.workStation = workStation;
		}
		
		public int getWorkStation() {
			return workStation;
		}
		
		public int getWorkerID() {
			return workerID;
		}

		public void assignWorker(int id) {
			this.workerID = id;
			Repository.getInstance().getWorker(id).addStage(this);
		}

		public void addBOM(String rw, float quantity) {
			BOM.put(rw, quantity);
		}

		public int getAuthLevel() {
			return authLevel;
		}
		
		public void setAuthLevel(int auth) {
			this.authLevel = auth;
		}

		public Map<String, Float> getBOM() {
			return BOM;
		}		
	}
}
