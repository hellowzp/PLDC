package model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import util.SqlServerUtil;

public class Repository {
	
	private final List<Worker> workers = loadWorkers();
	private final List<Product> products = new ArrayList<>(5);
	
	//real-time data
	private static final DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
	private final List<Date> xDate = new LinkedList<>();
	private final List<Float> yEfficiency = new LinkedList<>();
	private final List<Float> yProductivity = new LinkedList<>();
	private final List<Float> yPerformance = new LinkedList<>();
	
	private static final Repository repos = new Repository();
	
	private Repository() {
		
	}
	
	private Repository(List<Product> products) throws Exception {
		if(products==null)
			throw new Exception("The products can not be null");
		this.products.addAll(products);
	}
	
	public static Repository getInstance() {
		return repos;
	}
	
	public void setProducts(String path) {
		if (!path.endsWith(".csv"))
			return;
		
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(path));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		try {
			String line = br.readLine();
			final String[] columns = line.split(",");
//			List<Product> 
//			for(int i=3; i<columns.length; i++) {
//				
//			}
			//take care of the last dummy row: end of file
			while ((line = br.readLine()) != null && line.length()>5) {
				String[] values = line.split(",");
				String proName = values[0];

				Product pro = getProduct(proName);
				if (pro == null) {
					pro = new Product(proName);
					products.add(pro);
				}

				int workStation = Integer.valueOf(values[1]);
				long stdTime = (long) (Float.valueOf(values[2]) * 60000);
				Product.Stage stage = new Product.Stage(workStation, stdTime);

				for (int i = 3; i < values.length; i++) {
					String material = columns[i];
					int amount = Integer.valueOf(values[i]);
					stage.addBOM(material, amount);
				}
				
				pro.addStage(stage);

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
	
	public void assignProducts(String path) {
		
	}
	
	public void initProducts(String path) {
		if (!path.endsWith(".csv"))
			return;

		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(path));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		try {
			String line = br.readLine();
			final String[] columns = line.split(",");
			//take care of the last dummy row: end of file
			while ((line = br.readLine()) != null && line.length()>5) {
				String[] values = line.split(",");
//				System.out.println(line + " " + values.length);
//				for(int i=0; i<values.length; i++) System.out.println(i+values[i]+i);
//				System.out.flush();
				String proName = values[0];

				Product pro = getProduct(proName);
				if (pro == null) {
					pro = new Product(proName);
					products.add(pro);
				}

				int workStation = Integer.valueOf(values[1]);
				long stdTime = (long) (Float.valueOf(values[2]) * 60000);
				Product.Stage stage = new Product.Stage(workStation, stdTime);

				for (int i = 3; i < values.length; i++) {
					String material = columns[i];
					int amount = Integer.valueOf(values[i]);
					stage.addBOM(material, amount);
				}
				
				pro.addStage(stage);

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public void updateProducts(String path) {
		products.clear();
		initProducts(path);
	}
	
	public void addProducts(List<Product> products) throws Exception { 
		if(products==null)
			throw new Exception("The products can not be null");
		this.products.addAll(products);
	}
	
	public List<Product> getProducts() {
		return products;
	}
	
	public Product getProduct(String name){
		for(Product p : products) {
			if(p.getName().equals(name))
				return p;
		}
		return null;
	}
	
	public Product.Stage getStage(int workStation) {
		for(Product p : products) {
			Product.Stage s = p.getStage(workStation);
			if(s!=null) return s;
		}
		return null;
	}

	public List<Worker> loadWorkers() {
		System.out.println("loading workers");

		List<Worker> workers = new ArrayList<Worker>(10);
		ResultSet res = SqlServerUtil.getWorkers();
		try {
			while(res.next()){
				workers.add(new Worker(res.getInt(1), res.getString(2)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return workers;
	}
	
	//when calling this method outside this class, the class itself need to be 
	//first initialized, which will initialize all its static members in order
	//so loadWorkers() will be called beforehand
	public List<Worker> getWorkers() {
//		System.out.println("returning workers");
		return workers;
	}
	
	public Worker getWorker(int id){
		for(Worker w : workers){
			if(w.getID()==id) 
				return w;
		}
		return null;
	}
	
	public String getWorkerName(int id) {
		return getWorkers().get(id)==null ? null : getWorkers().get(id).getName();
	}
	
	public Worker getLastWorkerAtStation(int workStation){
		for(Worker w : getWorkers()){
			if(w.getLastWorkStation()==workStation)
				return w;
		}
		return null;  //exception should be thrown here
	}
		
	public int getNumberOfWorkingWorkers() {
		int n = 0;
		for(Worker w : getWorkers()) {
			if(w.isWorking()) n++;
		}
		return n;
	}

	public void readEvent(Timestamp ts, int workStation, int workId, String barcode) {
		Worker worker = workId>0 ? workers.get(workId-1) : getLastWorkerAtStation(workStation);
		
		if(worker==null) { //work station 17 status = 1 and count =1 (count = 3?  at 324)
			
		}else{
			worker.addTimeLineEvent(ts.getTime(), workStation, barcode);
		}
	    
		Date time = null;
		try {
			time = sdf.parse(ts.toString());
		} catch (ParseException e) {
			e.printStackTrace();
		}
	    
		//add real-time data
		float avgEfficiency = 0.0f;
		float avgProductivity = 0.0f;
		float avgPerformance = 0.0f;
		int j = 0;
		for(int i=0; i<workers.size(); i++) {
			Worker w = workers.get(i);
			if(w.isWorking()) {
				avgEfficiency += w.getEfficiency();
				avgProductivity += w.getProductivity();
				avgPerformance += w.getPerformance();
				j++;
			}
		}		
		
		xDate.add(time);
		yEfficiency.add(avgEfficiency*1.0f/j);
		yProductivity.add(avgProductivity);
		yPerformance.add(avgPerformance);
		
//		if(avgEfficiency>0.0f) { //filter
//			xDate.add(time);
//			yEfficiency.add(avgEfficiency*1.0f/j);
//		}
		
	}

	public List<Date> getXDate() {
		return xDate;
	}

	public List<Float> getYEfficiency() {
		return yEfficiency;
	}

	public List<Float> getyProductivity() {
		return yProductivity;
	}

	public List<Float> getyPerformance() {
		return yPerformance;
	}
}
