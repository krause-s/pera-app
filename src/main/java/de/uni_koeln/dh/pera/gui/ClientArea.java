package de.uni_koeln.dh.pera.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import de.uni_koeln.dh.pera.util.Calc;

public class ClientArea {

	/*
	 * innerWidth = 			calcPct(width, 80);				// admin; 4/5
	 * // TODO percentage
	 * imgHeight = 			calcPct(compImg_Height, 67);		// admin; 2/3
	 * compImg_innerHeight =	(compImg_Height-imgHeight) / 2;	// final; 1/6
	 * // TODO percentage
	 * txtHeight = 			calcPct(compTxt_Height, 88);		// admin
	 */
	
		private static final int H_HEIGHT_PCT = 60;		// "height: 60% of the height (of the app)"
	
		// parent == shell
		private Composite parent = null;
		private Display display = null;
	
	protected ClientArea(Composite parent) {
		this.parent = parent;
		this.display = parent.getDisplay();
	}
	
	// TODO seperating into ImgComposite & TxtComposite classes
	protected void init() {
		// parent.getClientArea() == inner area
		final int height = parent.getClientArea().height,
				imgCompHeight = (int)Calc.getValByPct(height, H_HEIGHT_PCT),
				txtCompHeight = height - imgCompHeight;		// 40%

		MainComposite imgComp = new MainComposite(parent);
		imgComp.setSize(imgCompHeight);
		imgComp.setBackground(display.getSystemColor(SWT.COLOR_BLACK));	// TODO tmp
		
//						Composite comp = new Composite(imgComp, SWT.NONE);
//						comp.setLayout(new GridLayout());
//						comp.setLayoutData(new GridData(100, 75));
//						comp.setBackground(display.getSystemColor(SWT.COLOR_GREEN));
	
		MainComposite txtComp = new MainComposite(parent);
		txtComp.setSize(txtCompHeight);
		txtComp.setBackground(display.getSystemColor(SWT.COLOR_YELLOW));	// TODO tmp
		
//						Composite comp2 = new Composite(txtComp, SWT.NONE);
//						comp2.setLayout(new GridLayout());
//						comp2.setLayoutData(new GridData(300, 150));
//						comp2.setBackground(display.getSystemColor(SWT.COLOR_BLUE));
	}
	
}
