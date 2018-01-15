package de.uni_koeln.dh.pera.gui;

import org.eclipse.swt.widgets.Composite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uni_koeln.dh.pera.gui.core.TxtComposite;
import de.uni_koeln.dh.pera.gui.core.img.ImgComposite;
import de.uni_koeln.dh.pera.util.Calc;

public class ClientWrapper {
	
	/*
	 * // check percentage
	 * imgHeight = 			calcPct(compImg_Height, 67);		// admin; 2/3
	 * compImg_innerHeight =	(compImg_Height-imgHeight) / 2;	// final; 1/6
	 */
	
		private Logger logger = LoggerFactory.getLogger(getClass());
	
		private static final int H_HEIGHT_PCT = 60;
	
		// parent == shell
		private Composite parent = null;
	
	protected ClientWrapper(Composite parent) {
		this.parent = parent;
	}
	
	protected void init() {
		logger.info("Initialize client area...");
		
		// parent.getClientArea() == inner area
		final int height = parent.getClientArea().height,
				imgCompHeight = (int) Calc.getValByPct(height, H_HEIGHT_PCT),
				txtCompHeight = height - imgCompHeight;			// 40%

		ImgComposite imgComp = new ImgComposite(parent);
		imgComp.init(imgCompHeight);	
		if (imgComp.isInitialized()) imgComp.setXXXXX();

		TxtComposite txtComp = new TxtComposite(parent);
		txtComp.init(txtCompHeight);
		if (txtComp.isInitialized()) txtComp.setTextBox();
	}
	
}
