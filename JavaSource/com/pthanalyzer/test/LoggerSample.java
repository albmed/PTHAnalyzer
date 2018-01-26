package com.pthanalyzer.test;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


/**
 * Aquesta classe simula com re-configurar Log4j.
 * Es podria fer servir per activar/desactivar els logs 
 * 
 * Mitjançant les preferències, indicar si es volen logs, i on s'han d'emmagatzemar. 
 * Aleshores, caldria modificar o crear el fitxer log4j.xml amb les opcions escollides.
 * 
 * Per a fer-ho servir, caldria crear una classe pont amb un mètode estàtic de l'estil 
 * 
 * public static void log(Class<T> class, String type,  String value) { 
 *    logger = Logger.getLogger(class); 
 *    logger.type(value); // Ens entenem :) 
 * }
 * 
 * per a cridar-ho:  
 * log(this, info, "my log"); 
 * 
 * NOTA: En cas d'activar-ho, caldria emmagatzemar el fitxer a un lloc que fos al classpath
 *  Es podria posar al mateix lloc on sigui el fitxer de properties.  
 * 
 * @author albgonza
 *
 */
public class LoggerSample {

	protected static final Logger logger = Logger.getLogger(LoggerSample.class); 
	
	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, TransformerFactoryConfigurationError, TransformerException {
		logger.info("EoM");
		
		LogManager.resetConfiguration();
		
		URL url = ClassLoader.getSystemResource("log4jTemp.xml"); 
		
		DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document doc = dBuilder.parse(new File(url.getFile())); 

		NodeList nl = doc.getElementsByTagName("param");
		
		if (nl != null && nl.getLength() > 0) {
			for (int i = 0; i <  nl.getLength(); i++) {
				Element nd = (Element) nl.item(i); 
				if (nd.getAttribute("name") != null && nd.getAttribute("name").equals("file")) { 
					System.out.println("File: " + nd.getAttribute("value"));
					
					nd.setAttribute("value", "d:/logs/logsPTH-LA/pthLANew.log");
					
				}
			}
		}

		logger.info("eo2");

		Transformer tr = TransformerFactory.newInstance().newTransformer(); 
		tr.setOutputProperty(OutputKeys.INDENT, "yes");
		tr.transform(new DOMSource(doc), new StreamResult(url.getFile()));

		DOMConfigurator.configure(url.getFile());
		
		logger.info("new");
	}

}
