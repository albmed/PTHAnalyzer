package com.pthanalyzer.tabs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

public class TabLayout {

	private Composite parent; 
	
	public TabLayout(Composite parent) {
		this.parent = parent; 
		
		TabFolder tabFolder = new TabFolder(this.parent, SWT.TOP);
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

		tabFolder.layout(true, true);
		this.parent.layout(true, true);
	}
}
