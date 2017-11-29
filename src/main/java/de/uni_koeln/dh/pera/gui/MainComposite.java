package de.uni_koeln.dh.pera.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uni_koeln.dh.pera.gui.misc.LayoutHelper;
import de.uni_koeln.dh.pera.util.Calc;

public class MainComposite extends Composite {

		private Logger logger = LoggerFactory.getLogger(getClass());
	
		// TODO if fitting set to final
		private static int W_WIDTH_PCT = 80;	
	
		protected Display display = null;
		
		// parent == shell
		private int parentWidth = 0,
				height = 0,
				innerWidth = 0;
		private boolean initialized = false;
	
	protected MainComposite(Composite parent) {
		super(parent, SWT.NONE);
		parentWidth = parent.getBounds().width;
		
		this.display = getDisplay();
	}

	protected void init(int height, int verticalSpacing) {
		this.height = height;
		
		setLayout(LayoutHelper.getLayout(verticalSpacing));		
		setLayoutData(LayoutHelper.getGridData(parentWidth, height));
		setInnerWidth();
	}
	
	protected int getHeight() {
		return height;
	}
	
	private void setInnerWidth() {
		innerWidth = (int) Calc.getValByPct(parentWidth, W_WIDTH_PCT);
	}
	
	protected int getInnerWidth() {
		return innerWidth;
	}
	
	protected void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}
	
	protected boolean isInitialized() {
		if (!initialized)
			logger.error("No text composite initialized.");
		
		return initialized;
	}
	
}
