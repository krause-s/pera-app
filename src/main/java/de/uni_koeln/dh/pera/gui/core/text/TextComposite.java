package de.uni_koeln.dh.pera.gui.core.text;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Composite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uni_koeln.dh.pera.gui.core.BaseComposite;
import de.uni_koeln.dh.pera.gui.core.img.ImgComposite;

public class TextComposite extends BaseComposite {
	
		private Logger logger = LoggerFactory.getLogger(getClass());
		
		private ImgComposite neighbour = null;
	
	public TextComposite(Composite parent, ImgComposite neighbour) {
		super(parent);
		this.neighbour = neighbour;
	}
	
	public void init(int height) {
		logger.info("Initialize text composite...");
		super.init(height);		
				
		setColors();
		setInitialized(true);
	}
	
	// TODO base class for in- and output (see 'margin' etc)
	public void addTexts() {
		logger.info("Add text boxes...");
		
		TextOutput output = new TextOutput(this);
		output.init();

		TextInput input = new TextInput(this);
		input.init();
	}

	private void setColors() {
		setBackground(display.getSystemColor(SWT.COLOR_BLACK));
		setForeground(display.getSystemColor(SWT.COLOR_WHITE));
	}
	
	protected int getTextMargin() {
		return (parentWidth - neighbour.getInnerWidth()) / 4;
	}
	
	protected int getWidth() {
		return parentWidth;
	}
	
	protected Font getFont(int style) {	
		// TODO check calculation
		int size = neighbour.getInnerWidth() / 40;

		// TODO Courier: universal font? 
		FontData data = new FontData("Courier", size, style);
		return new Font(display, data);
	}
	
}
