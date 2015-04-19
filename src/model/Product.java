package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Product {
	
	private String series;
	private String name;
	private String barcode;
	private List<Stage> stages = new ArrayList<>();
	
	public Product(String name) {
		this(null,name);
	}
	
	public Product(String ser, String name) {
		this.series = ser;
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
			System.out.println("Stage.getStage " + s.workStation);
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
	
	@Override
	public boolean equals(Object object) {
		if(this == object) 
            return true;
        if(object instanceof Product) 
			return ((Product)object).name.equalsIgnoreCase(name) && 
				   ((Product)object).series.equalsIgnoreCase(series);
		return 	false;
	}
	
	@Override
	public int hashCode() {
		return series.hashCode() + name.hashCode();
	}
	
	public String getSeries() {
		return series;
	}

	public void setSeries(String series) {
		this.series = series;
	}

	//may consider using the builder pattern and reflection to invoke method dynamically
	//http://www.rgagnon.com/javadetails/java-0031.html
	public static class Stage{
		
		private Product product;
		private String code;
		private int authLevel;
		private int workStation;
		private int workerID;
		private long stdTime;  //in mS
		private long minTime;
		private long maxTime;
		
		private Map<String, Float> BOM = new HashMap<>();
//		EnumMap<RawMaterial, Integer> BOM = new EnumMap<RawMaterial, Integer>(RawMaterial.class);;
		
//		public Stage(String code) {
//			this.code = code;
//		}
		
		public Stage(Product pro, String code){
			this.product = pro;
			this.code = code;
			this.product.addStage(this);
		}
		
		@Override
		public boolean equals(Object obj) {
			if(this==obj)
				return true;
			if(obj instanceof Product.Stage) {
				return this.product.equals( ((Product.Stage)obj).product)
					&& this.code.equals( ((Product.Stage)obj).code );
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return product.hashCode() + code.hashCode();
		}
		
		public Product getProduct() {
			return this.product;
		}

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
