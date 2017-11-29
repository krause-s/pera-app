package de.uni_koeln.dh.pera.gui.core;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uni_koeln.dh.pera.gui.MainComposite;
import de.uni_koeln.dh.pera.util.Calc;

public class ImgComposite extends MainComposite {

		private Logger logger = LoggerFactory.getLogger(getClass());
		
		// TODO if fitting set to final
		private static int H_HIMGCOMP_PCT = 5;
	
	public ImgComposite(Composite parent) {
		super(parent);
	}
	
	public void init(int height) {
		logger.info("Initialize image composite...");	
		super.init(
				height, 
				(int) Calc.getValByPct(height, H_HIMGCOMP_PCT));

		setBackground(display.getSystemColor(SWT.COLOR_DARK_RED));		// TODO background
		setInitialized(true);
	}
	
	// TODO components
	public void setXXXXX() {
		logger.info("Set XXXXX...");
		Composite comp = new Composite(this, SWT.NONE);
		comp.setLayout(new GridLayout());
		comp.setLayoutData(new GridData(100, 75));
		comp.setBackground(display.getSystemColor(SWT.COLOR_GREEN));
		
		Composite comp2 = new Composite(this, SWT.NONE);
		comp2.setLayout(new GridLayout());
		comp2.setLayoutData(new GridData(100, 75));
		comp2.setBackground(display.getSystemColor(SWT.COLOR_YELLOW));
		
		Composite comp3 = new Composite(this, SWT.NONE);
		comp3.setLayout(new GridLayout());
		comp3.setLayoutData(new GridData(100, 75));
		comp3.setBackground(display.getSystemColor(SWT.COLOR_MAGENTA));
	}
	
}
