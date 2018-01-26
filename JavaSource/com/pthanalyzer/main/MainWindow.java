/**
 * 
 */
package com.pthanalyzer.main;

import static com.pthanalyzer.utils.Constants.DISPLAY_INSUFFICIENT;
import static com.pthanalyzer.utils.Constants.RESOURCES_NOT_AVAILABLE;
import static com.pthanalyzer.utils.Constants.filterExtensions;
import static com.pthanalyzer.utils.Constants.filterNames;

import java.io.File;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import com.pthanalyzer.db.PDBFile;
import com.pthanalyzer.exception.GeneralPTHException;
import com.pthanalyzer.tabs.GeneralPTHTab;
import com.pthanalyzer.tabs.HandsPTHTab;
import com.pthanalyzer.tabs.PTHTab;
import com.pthanalyzer.tabs.PlayersPTHTab;
import com.pthanalyzer.utils.Constants;
import com.pthanalyzer.utils.ResourceUtils;

/**
 * @author albgonza
 *
 */
public class MainWindow {

	protected static final Logger logger = Logger.getLogger(MainWindow.class);
	
	Display display = null;
	MainWindow mainWindow = null; 
	Shell shell = null; 

	Image appIcon = null;
	
	PDBFile pdbFile = null;
	
	Composite baseComposite = null;
	Composite topComposite = null; 
	Composite bottomComposite = null; // maybe mainComposite would be a better name :), and reserve bottom for a statusBar (we'll see)  
	
	public Shell open(Display display) throws GeneralPTHException { 
		this.display = display; 
		this.mainWindow = this;
		
		int res = -1; 
		if ((res = initApp()) != 0) {
			throw new GeneralPTHException(res); 
		}
		
		initResources(); 
		this.shell = new Shell(display);
		
		createShellContents(); 
		
		shell.open();
		return shell;
	}
	
	
	/**
	 * 
	 * 
	 * 
	 * @return 0 iff success,   
	 */
	private int initApp() { 
		
		int res = 0; 
		// Check display
		if ((res = checkDisplay()) != 0) return DISPLAY_INSUFFICIENT; 
		// Check if resources are available (config file)
		if (!ResourceUtils.resourcesAvailable()) return RESOURCES_NOT_AVAILABLE; 

		// We should check here any other condition that leads the application to crash or make it unable to be displayed correctly  
		
		
		return res; 
	}
	
	

	/**
	 * Checks for minimum resolution size (800x600) 
	 * @return
	 */
	private int checkDisplay() { 
		if (display != null) { 
			Rectangle size = display.getBounds(); 
			if (size.width - size.x < 800 || size.height - size.y < 600) { 
				MessageBox box = new MessageBox(new Shell(display), SWT.ICON_ERROR);
				box.setText("Error on display");
				box.setMessage("Resolution must be at least 800x600");
				box.open();
				
				return -1;  
			}
		}
		return 0; 
	}
	
	/**
	 * Initializes resources 
	 */
	void initResources() {
		appIcon = ResourceUtils.loadImage(display, "/generic");
	}	
	
	/**
	 * Creates contents for main Shell (if any other) 
	 */
	public void createShellContents() { 
		shell.setText(ResourceUtils.getResourceString("window.title"));
		shell.setImage(appIcon);

		shell.setLayout(new FillLayout());
		
		Menu bar = new Menu(shell, SWT.BAR);
		shell.setMenuBar(bar);
		
		createFileMenu(bar);
		createHelpMenu(bar);
	}

	/** 
	 * Creates File Menu
	 * @param parent
	 */
	private void createFileMenu(Menu parent) { 
		int accel = -1; 
		
		Menu menu = new Menu(parent); 
		MenuItem header = new MenuItem(parent, SWT.CASCADE);
		header.setText(ResourceUtils.getResourceString(ResourceUtils.menuFile));
		header.setMenu(menu);
		
		MenuItem openItem = new MenuItem(menu, SWT.NONE); 
		openItem.setText(ResourceUtils.getResourceString(ResourceUtils.openFile));
		if ((accel = ResourceUtils.getAccelerator(openItem.getText())) != -1) { 
			openItem.setAccelerator(accel); 
		}
		openItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				openFile(); 
			}
			
		});
		
		new MenuItem(menu, SWT.SEPARATOR); 
		
		accel = -1; 
		MenuItem exitItem = new MenuItem(menu, SWT.NONE);
		exitItem.setText(ResourceUtils.getResourceString(ResourceUtils.quitApp));
		if ((accel = ResourceUtils.getAccelerator(exitItem.getText())) != -1) {
			exitItem.setAccelerator(accel);
		}
		exitItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}
		});
	}
	
	/**
	 * Creates Help Menu 
	 * @param parent
	 */
	private void createHelpMenu(Menu parent) {
		Menu menu = new Menu(parent);
		MenuItem header = new MenuItem(parent, SWT.CASCADE); 
		header.setText(ResourceUtils.getResourceString(ResourceUtils.menuHelp));
		header.setMenu(menu);
		
		MenuItem itemAbout = new MenuItem(menu, SWT.NONE);
		itemAbout.setText(ResourceUtils.getResourceString(ResourceUtils.aboutApp));
		itemAbout.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				MessageBox box = new MessageBox(shell, SWT.ICON_INFORMATION | SWT.OK);
				box.setText(ResourceUtils.getResourceString(ResourceUtils.aboutAppText)); 
				box.setMessage(ResourceUtils.getResourceString(
						ResourceUtils.aboutAppMessage, new Object[] {System.getProperty("os.name")}
				));
				box.open(); 
			}
		});
	}

	/**
	 * Opens and loads file 
	 */
	private void openFile() { 
		// TODO: Try to locate pokerth location.  
		// @See Example at TestPTHUtils (v.0.1.2)  
		
		FileDialog fd = new FileDialog(shell, SWT.OPEN);
		
		fd.setFilterExtensions(filterExtensions);
		fd.setFilterNames(filterNames);
		String name = fd.open();
		
		if (name == null) return; 
		File file = new File(name);
		
		if (!file.exists()) {
			logger.warn("Unable to locate file: \"" + name + "\"");
			MessageBox box = new MessageBox(shell, SWT.ICON_ERROR);
			box.setMessage("Error: File " + name + " not found");
			box.open();
			return; 
		}

		if (pdbFile != null) {  // clean TODO: All objects should be disposed
//			System.out.println("Cleaning... ");
			disposeObjects();
			pdbFile = null;
		}
		
		Cursor cursor = new Cursor(this.display, SWT.CURSOR_WAIT); 
		shell.setCursor(cursor);
		
		try {
			pdbFile = new PDBFile(name).loadPDB();
		} 
		catch (GeneralPTHException e) {
			// TODO 
			// Dos possibles errors: En qualsevol cas, cal mostrar error i resetejar-ho tot
			// No es pot carregar el fitxer
			// No és fitxer sqllite
		}

		shell.setCursor(null);
		
		if (pdbFile != null) createUpperLayout();
	}

	/* ================ VERSION 1: Create layouts all in one ======================= */
	
	/**
	 * This function should create all layouts necessary 
	 * 
	 */
	private void createLayouts() { 
		baseComposite = new Composite(shell, SWT.NONE);
		GridLayout baseLayout = new GridLayout(1, false);
		baseLayout.marginWidth = 5;
		baseLayout.marginHeight = 5;
		baseLayout.verticalSpacing = 15;
		baseLayout.horizontalSpacing = 0;
		baseComposite.setLayout(baseLayout);

		this.topComposite = new UpperComposite(baseComposite, SWT.NONE, this.pdbFile); 
		this.bottomComposite = new Composite(baseComposite, SWT.NONE);
		this.bottomComposite.setLayout(new GridLayout());
		this.bottomComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		TabFolder tabFolder = new TabFolder(bottomComposite, SWT.TOP);
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		PTHTab[] tabs = new PTHTab[] { 
				new GeneralPTHTab(),
				new HandsPTHTab(),
				new PlayersPTHTab()
		}; 
		
		for (PTHTab tab : tabs) { 
			TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
			tabItem.setText(tab.getName());
			tabItem.setControl(tab.createControl(tabFolder));
		}
		
		/*
		tabFolder = new TabFolder(parentComposite, SWT.TOP);
		PTHTab[] tabs = new PTHTab[] { 
				new GeneralPTHTab(this), 
				new HandsPTHTab(this), 
				new PlayersPTHTab(this)				
		};
//		tabFolder.setLayoutData(new GridData (SWT.FILL, SWT.FILL, true, true));
		for (PTHTab tab : tabs) { 
			TabItem item = new TabItem(tabFolder, 0); 
			item.setText(tab.getName());
//			item.setControl(tab.createTabFolderPage(tabFolder));
			item.setControl(tab.createControl(tabFolder));
		}

		 */
		
		tabFolder.layout(true, true);
		bottomComposite.layout(true, true);
		topComposite.layout(true, true);
		baseComposite.layout(true, true);
		shell.layout(true, true);
	}
	
	/* ================ VERSION 2: Create layouts separated ======================= */
	/* This version creates bottom layout, when a game is selected */ 
	
	private void createUpperLayout() { 
		baseComposite = new Composite(shell, SWT.NONE);
		GridLayout baseLayout = new GridLayout(1, false);
		baseLayout.marginWidth = 5;
		baseLayout.marginHeight = 5;
		baseLayout.verticalSpacing = 15;
		baseLayout.horizontalSpacing = 0;
		baseComposite.setLayout(baseLayout);

		this.topComposite = new UpperComposite(baseComposite, SWT.NONE, this.pdbFile); 

		topComposite.layout(true, true);
		baseComposite.layout(true, true);
		shell.layout();
	}
	
	
	/**
	 * TODO:  
	 * Dispose composite elements in case of being reloaded
	 */
	private void disposeObjects() {
//		if (upperComposite != null && !upperComposite.isDisposed()) upperComposite.dispose();
//		if (lowerComposite != null && !lowerComposite.isDisposed()) lowerComposite.dispose();
	}

	
	/**
	 * Entry point
	 * @param args No args defined, yet!
	 */
	public static void main(String[] args) {
		logger.info(" Main -- starting... ");
		
		Display display = null; 
		try  { 
			display = new Display();
			MainWindow mainWindow = new MainWindow();
			Shell shell = mainWindow.open(display); 
			while (!shell.isDisposed ()) {
				if (!display.readAndDispatch ()) 
					display.sleep ();
			}
			display.dispose ();
		}
		catch(GeneralPTHException e) { 
			if (e.getErrorCode() != null) { 
				ResourceUtils.showError("FATAL", Constants.getMessage(e.getErrorCode()), new Shell(display));
			}
			if (display != null) display.dispose();
			System.exit(e.getErrorCode());
		}
		
		catch (Throwable e) { 
			logger.error("An unexpected error ocurred", e);
			if (display != null) display.dispose(); 
			System.exit(-1);
		}

		logger.info(" Main -- exiting... "); 
	}

	
	
}
