package com.pthanalyzer.tabs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;

public abstract class PTHTab {

	protected String name; 
	protected Composite layoutComposite; 
	
	public String getName() {return name;}
	
	public Composite createControl(TabFolder tabFolder) { 
		layoutComposite = new Composite (tabFolder, SWT.NONE);
		layoutComposite.setLayout(new FillLayout());
//		layoutComposite.getParent().setLayoutData(new GridData (SWT.FILL, SWT.FILL, true, true));
		
		return layoutComposite; 
	}
	
}
