package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import model.Product;
import model.Repository;
import model.Worker;

public class ProductCSVReader {
	
	private String uri;
//	private List<Product> products = new ArrayList<>();
//	private List<Worker> workers = new ArrayList<>();
	
	private List<Product> products = new ArrayList<>(5);
	private List<Product.Stage> stages = new ArrayList<>();
	private Set<Worker> workers = Repository.getInstance().getWorkers();
	
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

		//read products list
		try{
			while((line = br.readLine()) != null && !line.contains("#")) {
				String[] values = line.split(",");
				Product pro = new Product(getSeriesName(),values[0]);
				for(int i=1; i<values.length; i++) {
					Product.Stage stage = new Product.Stage(pro, values[i]);
					stages.add(stage);
				}
				products.add(pro);
			}
		}catch (IOException e) {
			e.printStackTrace();
		} 
		
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
				String itemCode = values[2];
				float quantity = Float.valueOf(values[3]);
				
				if(stageCode.equals("")) { 
					for(Product pro : products) {
						Product.Stage stage = pro.getStage(lastStageCode);
						if(stage!=null) {
							stage.addBOM(itemCode, quantity);
						}
					}
				}else{
					int authLevel = Integer.valueOf(values[1]);
					long stdTime = (long) (Float.valueOf(values[4]) * 1000); //mS
					for(Product pro : products) {
						Product.Stage stage = pro.getStage(stageCode);
						if(stage!=null) {
							stage.setAuthLevel(authLevel);
							stage.setStdTime(stdTime);
							stage.addBOM(itemCode, quantity);
						}
					}
					
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
				workers.add(w);
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
		
		List<String> stages = new ArrayList<String>();
		for(Product.Stage s : product.getStages()) {
			stages.add(s.getCode());
		}
		
		return stages;
	}
	
	private Product.Stage getStage(String stageCode) {
		for(Product pro : products) {
			for(Product.Stage stage : pro.getStages()) {
				if(stage.getCode().equals(stageCode))
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
	
	public List<Product> getProducts() {
		return products;
	}
	
}
