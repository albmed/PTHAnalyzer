package com.pthanalyzer.logger;

import org.apache.log4j.Category;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;



/**
 * TODO: (future issue -- configurable logger) 
 * 
 * Aquesta classe hauria de servir com a Log-Manager
 * En primer lloc, només s'hauria de carregar en cas que els logs estiguin activats (mitjaçant una propietat de configuració) 
 * 
 * En tal cas s'haria de configurar en funció dels paràmetres que sol·liciti l'usuari. 
 * (Veure: {@link com.pthanalyzer.test.LoggerSample}) 
 * 
 * @author albgonza
 *
 */
public class LoggerUtils {

	static Logger logger = null; 
	
	static { 
		// Bloc estàtic que hauria d'inicialitzar tot allò que tingui a veure amb els logs 
	}
	public static void log(Class<?> clazz, Level level, Object message) { 
		logger = Logger.getLogger(clazz); 
		logger.log(Category.class.getName(), level, message, null);
	}
	
}
