package de.uni_koeln.dh.pera.gui.core;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uni_koeln.dh.pera.gui.misc.LayoutHelper;

public class BaseComposite extends Composite {

		private Logger logger = LoggerFactory.getLogger(getClass());
		
		protected Display display = null;
		
		// parent == shell
		protected int parentWidth = 0;
		private int	height = 0;
		
		private boolean initialized = false;
	
	protected BaseComposite(Composite parent) {
		super(parent, SWT.NONE);
		parentWidth = parent.getBounds().width;
		
		this.display = getDisplay();
	}

	protected void init(int height) {
		this.height = height;
		
		setLayout(LayoutHelper.getNormalizedLayout());		
		setLayoutData(LayoutHelper.getGridData(parentWidth, height));
	}
	
	public int getHeight() {
		return height;
	}
	
	protected void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}
	
	public boolean isInitialized() {
		if (!initialized)
			logger.error("Core composite not initialized.");
		
		return initialized;
	}
	
	protected Color getDefaultFgColor() {
		return display.getSystemColor(SWT.COLOR_WHITE);
	}
	
	public Color getDefaultBgColor() {
		return display.getSystemColor(SWT.COLOR_BLACK);
	}
	
	protected Font getFont(String name, int height, int style) {
		FontData data = new FontData(name, height, style);
		return new Font(display, data);
	}
	
}
