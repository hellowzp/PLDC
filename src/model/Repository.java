package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import model.Product.Stage;
import util.Assert;
import util.SqlServerUtil;
import util.TextWriter;

public class Repository {
	
	private Set<Worker> workers;
	private Set<Product.Stage> stages;
	private List<Product> products;
	
	private DateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
	public static final String HOME_DIRECTORY = System.getProperty("user.dir");
	public static final String ASSIGN_DIR = HOME_DIRECTORY + "/res/assignments";
	public static final String SERIES_DIR = HOME_DIRECTORY + "/series";
	
	public static final String ICON_PATH = HOME_DIRECTORY + "/res/icons";
		
	//real-time data
	private final List<Date> xDate = new LinkedList<>();
	private final List<Float> yEfficiency = new LinkedList<>();
	private final List<Float> yProductivity = new LinkedList<>();
	private final List<Float> yPerformance = new LinkedList<>();
		
	private static final Repository repos = new Repository();
	public static final TextWriter LOGGER = new TextWriter();

	private Repository() {
		
	}
	
	public static Repository getInstance() {
		return repos;
	}
	
	//dummy method, just to load the class
	public static void init() {
		
	}
	
//	public void initProducts(List<Product> products) {
//		clearData();
//		this.products.addAll(products);
//	}
//	
//	public void initProducts(String path) {
//		clearData();
//	}
	
	public void initStages(Set<Product.Stage> stages) {
		Assert.assertNotNull(stages, "stages can not be null");
		System.out.println("Repos.initStages()");
		this.stages = stages;
	}
	
	public void initWorkers() {
		workers = loadWorkers();
		Assert.assertNotNull(workers, null);
		System.out.println("Repos.initWorkers()");
	}
	
	public void initWorkers(Set<Worker> workers) {
		Assert.assertNotNull(workers, null);
		System.out.println("Repos.initWorkers()");
		this.workers = workers;
	}
	
	public void init(String path) {
		
	}
	
	public void cleanStages() {
		Assert.assertNotNull(stages, null);
		Assert.assertNotNull(workers, null);
		
		Iterator<Product.Stage> stageItr = stages.iterator();
		Product.Stage stage = null;
		while(stageItr.hasNext()) {
			stage = stageItr.next();
			if(stage.workStation == -1) { //not assigned
				stageItr.remove();
			} 
		}
	}
	
//	private void clearData() {
//		products.clear();
//		xDate.clear();
//		yEfficiency.clear();
//		yProductivity.clear();
//		yPerformance.clear();
//	}
	
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
	
	public Set<Product.Stage> getStages() {
		return stages;
	}
	
	public Product.Stage getStage(int workStation) {
		Iterator<Product.Stage> stageItr = stages.iterator();
		Product.Stage stage = null;
		while(stageItr.hasNext()) {
			 stage = stageItr.next();
			if(stage.getWorkStation() == workStation ){
				return stage;
			} 
		}
		
		return null;
	}
	
	public Stage getStage(String stageCode) {
		for(Product.Stage s : stages) {
			if(s.getCode().equals(stageCode))
				return s;
		}
		return null;
	}

	private Set<Worker> loadWorkers() {
		System.out.println("Repos.loadWorkers()");

		Set<Worker> workers = new HashSet<Worker>(10);
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
	public Set<Worker> getWorkers() {
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
	
	public Worker getWorker(String name){
		for(Worker w : workers){
			if(w.getName().equals(name)) 
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
	
	public Worker getStationWorker(int ws) {
		Worker w = null;
		for(Product.Stage s : stages) {
			if(s.getWorkStation()==ws)
				return getWorker(s.getWorkerID());
		}
		
//		for(Product p : products) {
//			for(Product.Stage s : p.getStages()) {
//				if(s.getWorkStation()==ws)
//					return getWorker(s.getWorkerID());
//			}
//		}
		
		return w;
	}

	public void readEvent(Timestamp ts, int workStation, int workerId, String barcode) {
		//get worker based on the assignment
		Worker worker = getStationWorker(workStation);
		
		if(worker==null) { //work station 17 status = 1 and count =1 (count = 3?  at 324)
			System.err.println("Cannot find worker at station " + workStation + " with ID " + workerId);
		}else{
			worker.addTimeLineEvent(ts.getTime(), workStation, workerId, barcode);
//			if(workerId!=0 && worker.getID() != workerId)
//				SqlServerUtil.sendMessage(workStation, "Wrong worker ID");
		}
	    
		//add real-time data if a new stage is finished
		if(barcode!=null && Character.isDigit(barcode.charAt(0))) {
			Date time = null;
			try {
				time = DATE_FORMATTER.parse(ts.toString());
			} catch (ParseException e) {
				e.printStackTrace();
			}
		    
			float avgEfficiency = 0.0f;
			float avgProductivity = 0.0f;
			float avgPerformance = 0.0f;
			int j = 0;
//			for(int i=0; i<workers.size(); i++) {
//				Worker w = workers.get(i);
//				if(w.isWorking()) {
//					avgEfficiency += w.getAvgEfficiency();
//					avgProductivity += w.getProductivity();
//					avgPerformance += w.getPerformance();
//					j++;
//				}
//			}		
			
			for(Worker w : workers){
				if(w.isWorking()) {
					avgEfficiency += w.getLastEfficiency();
					avgProductivity += w.getProductivity();
					avgPerformance += w.getPerformance();
					j++;
				}
			}
			
			if(avgEfficiency>0) {
				xDate.add(time);
				yEfficiency.add(avgEfficiency*1.0f/j);
				yProductivity.add(avgProductivity*1.0f/j);
				yPerformance.add(avgPerformance*1.0f/j);
			}
			
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

	public void assignWorker(Stage stage, int id) {
		stage.workerID = id;
		getWorker(id).addStage(stage);
	}
	
}
