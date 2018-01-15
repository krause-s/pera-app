package de.uni_koeln.dh.pera.gui.core.img;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uni_koeln.dh.pera.gui.core.BaseComposite;
import de.uni_koeln.dh.pera.gui.misc.LayoutHelper;

public class ImgComposite extends BaseComposite {

		private Logger logger = LoggerFactory.getLogger(getClass());
		
		// TODO if fitting set to final
		private static int H_HIMGCOMP_PCT = 5;
	
	public ImgComposite(Composite parent) {
		super(parent);
	}
	
	public void init(int height) {
		logger.info("Initialize image composite...");	
		super.init(height);

		setBackground(display.getSystemColor(SWT.COLOR_DARK_RED));		// TODO background
		setInitialized(true);
	}
	
	// TODO components
	public void setXXXXX() {
		logger.info("Set XXXXX...");
		// title
		Composite comp = new Composite(this, SWT.NONE);
		comp.setLayout(new GridLayout());
		comp.setLayoutData(LayoutHelper.getGridData(
				getInnerWidth(), 75, 
				true));
		comp.setBackground(display.getSystemColor(SWT.COLOR_GREEN));
		
		// map; see core.img.MapPane
		Composite comp2 = new Composite(this, SWT.NONE);
		comp2.setLayout(new GridLayout());
		comp2.setLayoutData(LayoutHelper.getGridData(
				getInnerWidth(), 350, 
				true));
		comp2.setBackground(display.getSystemColor(SWT.COLOR_YELLOW));
		
		// controls
		Composite comp3 = new Composite(this, SWT.NONE);
		comp3.setLayout(new GridLayout());
		comp3.setLayoutData(LayoutHelper.getGridData(
				getInnerWidth(), 75, 
				true));
		comp3.setBackground(display.getSystemColor(SWT.COLOR_MAGENTA));
	}
		
}
