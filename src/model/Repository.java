package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import util.SqlServerUtil;

public class Repository {
	
	private static final List<Worker> workers = loadWorkers();
	
	public static String getWorkerName(int id) {
		return workers.get(id)==null ? null : workers.get(id).getName();
	}

	public static List<Worker> loadWorkers() {
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
	
	public static Worker getLastWorkerAtStation(int workStation){
		for(Worker w : workers){
			if(w.getLastWorkStation()==workStation)
				return w;
		}
		return null;
	}
}
