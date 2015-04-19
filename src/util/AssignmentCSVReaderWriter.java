package util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.table.TableModel;

public class AssignmentCSVReaderWriter {
	
	public static void writeTableToCSV(JTable table, String path) {
		if(!path.endsWith(".csv")) path += ".csv";
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(path));
			TableModel model = table.getModel();
			int row = model.getRowCount();
			int col = model.getColumnCount();
			
			//write header
			for(int i=0; i<col-1; i++) {
				bw.write(model.getColumnName(i)+",");				
			}
			bw.write(model.getColumnName(col-1));
			bw.newLine();
			
			//write rows
			for(int i=0; i<row; i++) {
				for(int j=0; j<col-1; j++) {
					bw.write( String.valueOf(model.getValueAt(i, j)));
					bw.write(",");
				}
				bw.write( String.valueOf(model.getValueAt(i, col-1)) );
				bw.newLine();
				bw.flush();  //must be called before closing the stream
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static Vector<Vector<String>> generateTableFromCSV(JTable table, String path) {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(path));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		String line = null;
		Vector<Vector<String>> data = new Vector<>();
		try{
			line = br.readLine();
			while( (line = br.readLine()) != null && line.length()>10 ) {
				data.add(new Vector<String>( Arrays.asList(line.split(",")) ));
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
		
		return data;
	}	

}
