/**
 * 
 */
package com.pthanalyzer.exception;

/**
 * @author albgonza
 *
 */
public class GeneralPTHException extends Exception {

	private static final long serialVersionUID = -710128245902962081L;
	String text = ""; 
	Integer errorCode = null;
	
	
	public GeneralPTHException(String text) { 
		super(text);
		this.text = text; 
	}
	
	public GeneralPTHException(Integer errorCode) { 
		super(""); 
		this.errorCode = errorCode; 
	}

	public Integer getErrorCode() { 
		return errorCode; 
	}
	
}
