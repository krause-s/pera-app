package de.uni_koeln.dh.pera.gui.misc;

import org.eclipse.swt.layout.GridLayout;

public class LayoutHelper {
		
	public static GridLayout getDefaultLayout() {
		return getLayout(0, 0);
	}
	
	public static GridLayout getLayout(int marginHeight, int marginWidth) {
		GridLayout layout = new GridLayout();	// == new GridLayout(1, true);
		layout.marginHeight = marginHeight;
		layout.marginWidth = marginWidth;
		layout.verticalSpacing = 0;
		
		return layout;
	}
	
}
