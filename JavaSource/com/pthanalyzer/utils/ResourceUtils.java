package com.pthanalyzer.utils;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

public class ResourceUtils {

	private static final ResourceBundle resources = ResourceBundle.getBundle("config"); 
	
	public static String menuFile = "menu.file"; 
	public static String openFile = "menu.file.open";  
	public static String quitApp = "menu.file.quit";
	public static String menuHelp = "menu.help";
	public static String aboutApp = "menu.help.about"; 
	public static String aboutAppText = "menu.help.about.text"; 
	public static String aboutAppMessage = "menu.help.about.message"; 

	public static boolean resourcesAvailable() { 
		return resources != null; 
	}
	
	public static String getResourceString(String key, Object[] args) {
		String text = getResourceString(key); 
		if (args == null || args.length == 0) return text;
		else {
			try { 
				return MessageFormat.format(text, args);
			}
			catch (IllegalArgumentException e) { 
				e.printStackTrace();
				System.err.println("Error");
				return text; 
			}
		}
	}
	
	public static String getResourceString(String key) {
		try {
			return resources.getString(key);
		} catch (MissingResourceException e) {
			return key;
		} catch (NullPointerException e) {
			return "!" + key + "!"; 
		}
	}
	
	public static int getAccelerator(String option) { 
		int accel = -1; 
		
		if (option.indexOf('\t') > 0) { 
			if (option.substring(option.indexOf('\t')).indexOf('+') > 0) { 
				String[] split = option.substring(option.indexOf('\t') + 1).split("\\+");
				if (split[0] != null && split[0].trim().length() > 0) {
					if (split[0].equalsIgnoreCase("Ctrl")) { 
						accel = SWT.MOD1; 
					}
					else if (split[0].equalsIgnoreCase("Alt")) { 
						accel = SWT.MOD3; 
					}
					else if (split[0].equalsIgnoreCase("Shift")) {
						accel = SWT.MOD2; 
					}
					else { 
						System.err.println("Undefined key " + split[0]);
						return -1; 
					}
				}
				if (split[1] != null && split[1].trim().length() == 1 && accel != -1) { 
					if (Character.isLetter(split[1].trim().charAt(0))) { 
						accel += split[1].trim().charAt(0); 
					}
				}
			}
		}
		
		return accel;  
	}
	
	public static Image loadImage(Display display, String fileName) {
		Image image = null; 
		try {
			InputStream sourceStream = ClassLoader.class.getResourceAsStream(fileName + ".ico");  //$NON-NLS-1$ //$NON-NLS-2$
			ImageData source = new ImageData(sourceStream);
			ImageData mask = source.getTransparencyMask();
			image = new Image(display, source, mask);
			sourceStream.close();
		} catch (IOException e) {
			showError(ResourceUtils.getResourceString("Error"), e.getMessage(), display.getShells()[0]); //$NON-NLS-1$
		}
		return image;
	}

	
	// FIXME: Probably this method should be placed elsewhere
	public static void showError (String title, String message, Shell shell) {
		MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR | SWT.CLOSE);
		messageBox.setText(title);
		messageBox.setMessage(message);
		messageBox.open();
	}

	
}
