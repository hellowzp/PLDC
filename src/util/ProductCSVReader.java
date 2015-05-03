package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import model.Product;
import model.Worker;

public class ProductCSVReader {
	
	private String uri;
//	private List<Product> products = new ArrayList<>();
//	private List<Worker> workers = new ArrayList<>();
	
	private Set<Product> products = new HashSet<>(5);
	private Set<Product.Stage> stages = new HashSet<>();
	private Set<Worker> workers = new HashSet<>();
	
	public ProductCSVReader(String uri) {		
		this.uri = uri;
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(uri));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		String line = null;
		try{
			while ((line = br.readLine()) != null && !line.contains("*")); 
		} catch (IOException e) {
			e.printStackTrace();
		} 

		//read products list and init all stages
		try{
			while((line = br.readLine()) != null && !line.contains("#")) {
				String[] values = line.split(",");
				Product pro = new Product(getSeriesName(),values[0]);
				for(int i=1; i<values.length; i++) {
//					System.out.println(values[i]);
					Product.Stage stage = new Product.Stage(pro, values[i]);
					stages.add(stage);
				}
				products.add(pro);
			}
		}catch (IOException e) {
			e.printStackTrace();
		} 
		
		System.out.println("Products: " + products.size() + " Stages: " + stages.size());
		
		try{
			while ((line = br.readLine()) != null && !line.contains("*")); 
		} catch (IOException e) {
			e.printStackTrace();
		} 

		//read BOM
		try {
			br.readLine(); //read header line
			//take care of the last dummy row: end of file
			String lastStageCode = null;
			while ((line = br.readLine()) != null && !line.contains("#")) {
				String[] values = line.split(",");
				String stageCode = values[0];
				String itemCode = values[2].trim();
				float quantity = Float.valueOf(values[3]);
				
				String queryCode = stageCode.equals("") ? lastStageCode : stageCode;
				Iterator<Product.Stage> stageItr = stages.iterator();
				Product.Stage stage = null;
				while(stageItr.hasNext()) {
					 stage = stageItr.next();
					if(stage.getCode().equals(queryCode)) {
						stage.addBOM(itemCode, quantity);
						break;
					} 
				}
				
				if(!stageCode.equals("")) { 
					int authLevel = Integer.valueOf(values[1]);
					long stdTime = (long) (Float.valueOf(values[4]) * 1000); //mS
					stage.setAuthLevel(authLevel);
					stage.setStdTime(stdTime);
					lastStageCode = stageCode;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		try{
			while ((line = br.readLine()) != null && !line.contains("*")); 
		} catch (IOException e) {
			e.printStackTrace();
		} 

		//read authorization
		try {
			br.readLine(); //read header line
			//take care of the last dummy row: end of file
			while ((line = br.readLine()) != null && !line.contains("#")) {
				String[] values = line.split(",");
//				System.out.println(line + " " + values.length);
//				for(int i=0; i<values.length; i++) System.out.println(i+values[i]+i);
//				System.out.flush();
				Worker w = new Worker(Integer.valueOf(values[0]), values[1]);
				for(int i=2; i<26; i++) {
					if(values[i].equals("Y"))
						w.addAuth(i-1);
				}
				if(!workers.add(w)){
					System.err.println("Duplicate workers exist in the setting file " + uri);
					try {
						throw new NullPointerException();
					} catch (NullPointerException e) {
						e.printStackTrace();
					}
					System.exit(0);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public String getSeriesName() {
		String fName = new File(uri).getName();
		return fName.substring(0, fName.length()-4);
	}
	
	public List<String> getSupportedStages(String proName) {
		Product product = null;
		for(Product pro : products) {
			if(pro.getName().equals(proName)) {
				product = pro;
				break;
			}
		}
		
		if(product==null) {
			System.err.println("Unknown error");
			try {
				throw new NullPointerException();
			} catch (NullPointerException e) {
				e.printStackTrace();
			}
			System.exit(0);
		}
		
		List<String> stages = new ArrayList<String>();
		for(Product.Stage s : product.getStages()) {
			stages.add(s.getCode());
		}
		
		return stages;
	}
	
	private Product.Stage getStage(String stageCode) {
		Iterator<Product.Stage> stageItr = stages.iterator();
		Product.Stage stage = null;
		while(stageItr.hasNext()) {
			 stage = stageItr.next();
			if(stage.getCode().equals(stageCode)) {
				return stage;
			} 
		}
		
		return null;
	}
	
	public List<String> getQualifiedWorkers(String stageCode) {
		Product.Stage stage = getStage(stageCode);
		List<String> workNames = new ArrayList<String>();
		int auth = stage.getAuthLevel();
		for(Worker w : workers) {
			if(w.getAuthLevels()!=null && w.getAuthLevels().contains(auth))
				workNames.add(w.getName());
		}
		return workNames;
	}
	
	public List<String> getQualifiedWorkers(Product.Stage stage) {
		List<String> workNames = new ArrayList<String>();
		int auth = stage.getAuthLevel();
		for(Worker w : workers) {
			if(w.getAuthLevels()!=null && w.getAuthLevels().contains(auth))
				workNames.add(w.getName());
		}
		return workNames;
	}

	public List<String> getProductList() {
		List<String> list = new ArrayList<String>(products.size());
		for(Product pro : products) {
			list.add(pro.getName());
		}
		return list;
	}
	
	public Set<Product.Stage> getStages() {
		return stages;
	}
	
	public Set<Worker> getWorkers() {
		return workers;
	}
	
}
