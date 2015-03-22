package model;

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
	private final List<Product> products = new ArrayList<>();
	
	//real-time data
	private static final DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
	private final List<Date> xDate = new LinkedList<>();
	private final List<Float> yEfficiency = new LinkedList<>();
	private final List<Float> yProductivity = new LinkedList<>();
	private final List<Float> yPerformance = new LinkedList<>();
	
	private static final Repository repos = new Repository();
	
	private Repository() {
		
	}
	
	public static Repository getInstance() {
		return repos;
	}
	
	//initialize products from csv file
//	public void initProducts(String path) {
//		if (!path.endsWith(".csv"))
//			return;
//
//		BufferedReader br = null;
//		try {
//			br = new BufferedReader(new FileReader(path));
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		}
//
//		try {
//			String line = br.readLine();
//			final String[] columns = line.split(",");
//			//take care of the last dummy row: end of file
//			while ((line = br.readLine()) != null && line.length()>5) {
//				String[] values = line.split(",");
////				System.out.println(line + " " + values.length);
////				for(int i=0; i<values.length; i++) System.out.println(i+values[i]+i);
////				System.out.flush();
//				String proName = values[0];
//
//				Product pro = getProduct(proName);
//				if (pro == null) {
//					pro = new Product(proName);
//					products.add(pro);
//				}
//
//				int workStation = Integer.valueOf(values[1]);
//				long stdTime = (long) (Float.valueOf(values[2]) * 60000);
//				Product.Stage stage = new Product.Stage(workStation, stdTime);
//
//				for (int i = 3; i < values.length; i++) {
//					String material = columns[i];
//					int amount = Integer.valueOf(values[i]);
//					stage.addBOM(material, amount);
//				}
//				
//				pro.addStage(stage);
//
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//	}
	
	public void initProducts(List<Product> products) {
		clearData();
		this.products.addAll(products);
	}
	
	public void initProducts(String path) {
		clearData();
	}
	
	private void clearData() {
		products.clear();
		xDate.clear();
		yEfficiency.clear();
		yProductivity.clear();
		yPerformance.clear();
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
			System.out.println("Repos getStage: " + s);
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
		return getWorker(id)==null ? null : getWorker(id).getName();
	}
	
	public int getWorkerID(String name) {
		for(Worker w : workers) {
			if(w.getName().equals(name))
				return w.getID();
		}
		return 0;
	}
		
	public int getNumberOfWorkingWorkers() {
		int n = 0;
		for(Worker w : workers) {
			if(w.isWorking()) n++;
		}
		return n;
	}
	
	public Worker getStageWorker(int ws) {
		Worker w = null;
		for(Product p : products) {
			for(Product.Stage s : p.getStages()) {
				if(s.getWorkStation()==ws)
					return getWorker(s.getWorkerID());
			}
		}
		return w;
	}

	public void readEvent(Timestamp ts, int workStation, int workerId, String barcode) {
		//get worker based on the assignment
		Worker worker = getStageWorker(workStation);
		
		if(worker==null) { //work station 17 status = 1 and count =1 (count = 3?  at 324)
			System.out.println("Cannot find worker at station " + workStation + " with ID " + workerId);
		}else{
			worker.addTimeLineEvent(ts.getTime(), workStation, workerId, barcode);
//			if(workerId!=0 && worker.getID() != workerId)
//				SqlServerUtil.sendMessage(workStation, "Wrong worker ID");
		}
	    
		//add real-time data if a new stage is finished
		if(barcode!=null && Character.isDigit(barcode.charAt(0))) {
			Date time = null;
			try {
				time = sdf.parse(ts.toString());
			} catch (ParseException e) {
				e.printStackTrace();
			}
		    
			float avgEfficiency = 0.0f;
			float avgProductivity = 0.0f;
			float avgPerformance = 0.0f;
			int j = 0;
			for(int i=0; i<workers.size(); i++) {
				Worker w = workers.get(i);
				if(w.isWorking()) {
					avgEfficiency += w.getAvgEfficiency();
					avgProductivity += w.getProductivity();
					avgPerformance += w.getPerformance();
					j++;
				}
			}		
			
			xDate.add(time);
			yEfficiency.add(avgEfficiency*1.0f/j);
			yProductivity.add(avgProductivity*1.0f/j);
			yPerformance.add(avgPerformance*1.0f/j);
		}	
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
