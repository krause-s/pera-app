package de.uni_koeln.dh.pera.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import de.uni_koeln.dh.pera.gui.misc.LayoutHelper;

public class MainComposite extends Composite {

		// parent == shell
		private Composite parent = null;
	
	public MainComposite(Composite parent) {
		super(parent, SWT.NONE);
		this.parent = parent;
		
		setLayout(LayoutHelper.getLayout(0, 50));	// TODO calc marginWidth
	}

	public void setSize(int height) {
		int width = parent.getBounds().width;
		setLayoutData(new GridData(width, height));
	}
	
}
