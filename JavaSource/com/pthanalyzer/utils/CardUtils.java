package com.pthanalyzer.utils;

public class CardUtils {

	private static final String suitTXT = "dhsc";
	private static final String suitUTF = "\u2666\u2665\u2660\u2663"; // Unicode points of dhsc
	private static final String valueTXT = "23456789TJQKA"; 

	public static String getStringCards(Integer cards) { 
		return getStringCards(cards, false);
	}
	
	public static String getStringCards(Integer cards, boolean utf) {
		if (cards == null) return null; 
		int value = cards.intValue() % 13; 
		int suit = cards.intValue() / 13; 
		
		String text = ""; 
		if (utf) text = String.valueOf(suitUTF.charAt(value)) + String.valueOf(suitUTF.charAt(suit));
		else text = String.valueOf(valueTXT.charAt(value)) + String.valueOf(suitTXT.charAt(suit));
		
		return text; 
	}
	
	public static Integer getIntegerCards(String cards) { 
		if (cards == null || cards.length() != 2) return null; 

		int offset = valueTXT.indexOf(cards.charAt(0));
		int base = suitTXT.indexOf(cards.toLowerCase().charAt(1)); 
		
		return base*13 + offset;
	}

}
