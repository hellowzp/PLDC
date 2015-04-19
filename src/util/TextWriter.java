package util;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TextWriter {
	
	private BufferedWriter writer;
	private int flag = 0;
	
	public TextWriter() {
		
		Path dir = Paths.get("reports");
		if (!Files.exists(dir)) {
		    try {
		        Files.createDirectory(dir);
		    } catch (IOException e) {
		        System.err.println(e);
		        System.exit(0);
		    }
		}	
		
		DateFormat format = new SimpleDateFormat("yyyyMMdd");
		String filename = "reports/" + format.format(new Date()) + ".csv";  
		
		FileOutputStream fos = null;
		try {
		    fos = new FileOutputStream(filename, true);
		} catch(FileNotFoundException e) {
			while(Paths.get(filename).toFile().exists()) {
				filename = filename.substring(0, filename.length()-4)
						  .concat( "_" + (int)(Math.random() * 128) + ".csv");
			}			
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		    
//		while( !Files.isWritable(Paths.get(filename))) {
//			filename = filename.substring(0, filename.length()-4)
//					  .concat( "_" + (Math.random() * 8 % 8) + ".csv");
//		}
		
		try {
			writer = new BufferedWriter(new FileWriter(filename));
			writer.write("Stage name,Completion time,Worker name\n");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void write(String data) {
		try {
			writer.write(data);
			writer.newLine();
			flag = (flag++)%16;
			if(flag==0) 
				writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void close() {
		try {
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		new TextWriter();
	}
	
}
