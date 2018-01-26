package com.pthanalyzer.language;

import java.text.MessageFormat;
import java.util.ResourceBundle;

public class Language {

	static ResourceBundle resourceBundle = ResourceBundle.getBundle("application"); 
	
	public static final String UPPER_GAME_SELECT = "upper.game.select";
	public static final String UPPER_GAME_SELECT_CHOOSE = "upper.game.select.choose";
	public static final String UPPER_GAME_SELECT_GAME = "upper.game.select.game";
	public static final String UPPER_GAME_STATE = "upper.game.state"; 
	public static final String UPPER_GAME_STATE_COMPLETE = "upper.game.state.complete"; 
	public static final String UPPER_GAME_STATE_INCOMPLETE = "upper.game.state.incomplete"; 
	public static final String UPPER_GAME_WINNER = "upper.game.winner"; 
	public static final String UPPER_GAME_HANDS = "upper.game.hands"; 

	
	public static final String WARNING_NOTFOUND = "warning.notfound"; 
	public static final String WARNING_NOVALUE = "warning.novalue";
	
	// FIXME: Prevent initialization 
	private Language() {} 
	
	public static String getMessage(String key) { 
		return resourceBundle.getString(key);
	}

	public static String getMessage(String key, Object[] args) { 
		String value = getMessage(key); 
		if (args == null || args.length == 0)  return value; 
		else { 
			try { 
				return MessageFormat.format(value, args);
			}
			catch (IllegalArgumentException e) { 
				e.printStackTrace();
				System.err.println("Error");
				return value; 
			}
		}
	}
	
}
