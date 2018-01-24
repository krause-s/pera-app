package de.uni_koeln.dh.pera.gui.core.img;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uni_koeln.dh.pera.gui.core.BaseComposite;
import de.uni_koeln.dh.pera.gui.misc.LayoutHelper;
import de.uni_koeln.dh.pera.util.Calc;

public class ImgComposite extends BaseComposite {

		private Logger logger = LoggerFactory.getLogger(getClass());
		
		// TODO if fitting set to final
		private static int W_WIMGCOMP_PCT = 80;
//		private static int H_HIMGCOMP_PCT = 5;
		
		private int innerWidth = 0;
	
	public ImgComposite(Composite parent) {
		super(parent);
	}
	
	public void init(int height) {
		logger.info("Initialize image composite...");	
		super.init(height);
		setInnerWidth();
		
		setBackground(display.getSystemColor(SWT.COLOR_DARK_RED));		// TODO background
		setInitialized(true);
	}
	
	// TODO components
	public void setXXXXX() {
		int mapHeight = (int) Calc.getValByPct(getHeight(), 75);
		
		
		
		
		
		logger.info("Set XXXXX...");
		// title
		Label comp = new Label(this, SWT.CENTER);
//		comp.setLayout(new GridLayout());
		comp.setLayoutData(LayoutHelper.getGridData(
				getInnerWidth(), 20, 
				true));
		comp.setText("Einführung");
		comp.setBackground(display.getSystemColor(SWT.COLOR_GREEN));
		
		
		
		
		
		
		
		
		// map; see core.img.MapPane
		Composite comp2 = new Composite(this, SWT.NONE);
		comp2.setLayout(new GridLayout());
		comp2.setLayoutData(LayoutHelper.getGridData(
				getInnerWidth(), mapHeight, 
				true));
		comp2.setBackground(display.getSystemColor(SWT.COLOR_YELLOW));
		
			try {
				MapPane.setMap(comp2);
			} catch (Exception e) {
				e.printStackTrace();
			}
		
			
			
			
			
		// controls
		Composite comp3 = new Composite(this, SWT.NONE);
		comp3.setLayout(new RowLayout());
		comp3.setLayoutData(LayoutHelper.getGridData(
				getInnerWidth(), 20, 
				true));
		comp3.setBackground(display.getSystemColor(SWT.COLOR_MAGENTA));
		
		Button but1 = new Button(comp3, SWT.PUSH);
		but1.setText("Städte");
		Button but2 = new Button(comp3, SWT.PUSH);
		but2.setText("Routen");
		Button but3 = new Button(comp3, SWT.PUSH);
		but3.setText("+");
		Button but4 = new Button(comp3, SWT.PUSH);
		but4.setText("-");
	}

	
	
	
	
	private void setInnerWidth() {
		innerWidth = (int) Calc.getValByPct(parentWidth, W_WIMGCOMP_PCT);
	}
	
	public int getInnerWidth() {
		return innerWidth;
	}
	
}
