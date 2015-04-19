package util;

import javax.swing.JFileChooser;

import model.Repository;

public class FileChooser {
	
	private static final String DEFAULT_DIRECTORY = Repository.HOME_DIRECTORY + "/res/assignments";		
	
	public static String chooseCSVFile() {
		JFileChooser fc = new JFileChooser(DEFAULT_DIRECTORY);
		fc.setAcceptAllFileFilterUsed(false);
		fc.addChoosableFileFilter(new CSVChooserFilter());
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.setMultiSelectionEnabled(false);
		int selection = fc.showOpenDialog(fc);
		if(selection==JFileChooser.APPROVE_OPTION) {
			return fc.getSelectedFile().getAbsolutePath();
		}
		return "";
	}
	
	public static String saveAsFile() {
		JFileChooser fc = new JFileChooser(DEFAULT_DIRECTORY);
		fc.setAcceptAllFileFilterUsed(false);
		fc.addChoosableFileFilter(new CSVChooserFilter());
//		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fc.setMultiSelectionEnabled(false);
		fc.setDialogTitle("Save as CSV file");
		int selection = fc.showSaveDialog(fc);
		if(selection==JFileChooser.APPROVE_OPTION) {			
			return fc.getSelectedFile().getAbsolutePath();
		}
		return null;
	}
	
	public static String chooseCSVFolder() {
		JFileChooser fc = new JFileChooser(DEFAULT_DIRECTORY);
		fc.setAcceptAllFileFilterUsed(false);
		fc.setDialogTitle("Choose a directory containing all the series setting files");
//		fc.addChoosableFileFilter(new CSVChooserFilter());
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fc.setMultiSelectionEnabled(false);
		int selection = fc.showOpenDialog(fc);
		if(selection==JFileChooser.APPROVE_OPTION) {
			return fc.getSelectedFile().getAbsolutePath();
		}
		return null;
	}
	
}
