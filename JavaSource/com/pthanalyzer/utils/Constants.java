package com.pthanalyzer.utils;

public class Constants {

	public static final String[] filterExtensions = new String[] {"*.pdb;", "*.*"}; 
	public static final String[] filterNames = 
			new String[] {"Sqlite DB File" + " (*.pdb)", "All Files" + " (*.*)"}; 
	
	public static final Integer WINNER_SERIES = Integer.valueOf(1);
	public static final Integer LOSER_SERIES = Integer.valueOf(0);
	
	
	
	
	
	
	// ERROR CODES 
	public static final Integer DISPLAY_INSUFFICIENT = Integer.valueOf(1); 
	public static final Integer RESOURCES_NOT_AVAILABLE = Integer.valueOf(2); 
	
	public static final String DISPLAY_INSUFFICIENT_TXT = "Min display must be 800x600";  
	public static final String RESOURCES_NOT_AVAILABLE_TXT = "Unable to locate Resources: config.properties"; 
			
	public static String getMessage(Integer errorCode) { 
		switch (errorCode) { 
			case 1: return DISPLAY_INSUFFICIENT_TXT;  
			case 2: return RESOURCES_NOT_AVAILABLE_TXT;  
		
			default: return "UNHANDLED ERROR"; 
		}
	}
	
}
