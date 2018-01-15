package de.uni_koeln.dh.pera.gui.misc;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LayoutHelper {
		
		private static Logger logger = LoggerFactory.getLogger(LayoutHelper.class);
	
	public static GridLayout getNormalizedLayout() {
		return getLayout(true);
	}
	
	public static GridLayout getLayout(boolean reset) {
		GridLayout layout = new GridLayout();	// == new GridLayout(1, true);
		
		if (reset) {
			layout.marginHeight = 0;
			layout.marginWidth = 0;
			layout.verticalSpacing = 0;
		}
		
		logger.info("GridLayout (marginHeight, marginWidth, verticalSpacing): "
				+ layout.marginHeight + ", " + layout.marginWidth + ", " 
				+ layout.verticalSpacing);
		return layout;
	}

	public static GridData getGridData(int width, int height) {
		return getGridData(width, height, false);
	}
	
	public static GridData getGridData(int width, int height, boolean centered) {
		GridData data = new GridData(width, height);
		
		if (centered) {
			data.verticalAlignment = SWT.CENTER;
			data.horizontalAlignment = SWT.CENTER;
			data.grabExcessVerticalSpace = true;
			data.grabExcessHorizontalSpace = true;
		}
		
		logger.info("GridData (width / height; verticalCentered): " + width + " / " 
				+ height + "; " + centered);
		return data;
	}
	
}
