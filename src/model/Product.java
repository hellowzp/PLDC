package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Product implements Reportable{
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
	
	public Stage getStage(int workStation) {
		for(Stage s : stages) {
			if(s.workStation == workStation) 
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
		return s.standardTime;
	}
	
	public long getCompletionTime(){
		long t = 0;
		for(Stage s : stages) {
			t += s.standardTime;
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
	
	@Override
	public void generateReport() {
		
	}
	
	//may consider using the builder pattern and reflection to invoke method dynamically
	//http://www.rgagnon.com/javadetails/java-0031.html
	public static final class Stage{
		int workStation;
		int workID;
		long standardTime;
		
		final Map<String, Integer> BOM = new HashMap<>();
//		EnumMap<RawMaterial, Integer> BOM = new EnumMap<RawMaterial, Integer>(RawMaterial.class);;
		
		public Stage(int ws, long stdTime) {
			workStation = ws;
			standardTime = stdTime;
		}
		
		public Stage(int ws, int id, long stdTime) {
			workStation = ws;
			workID = id;
			standardTime = stdTime;
		}
		
		public void addBOM(String rw, int amount) {
			BOM.put(rw, amount);
		}		
	}
}
