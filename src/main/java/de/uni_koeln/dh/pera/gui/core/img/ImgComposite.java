package de.uni_koeln.dh.pera.gui.core.img;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uni_koeln.dh.pera.gui.core.BaseComposite;
import de.uni_koeln.dh.pera.gui.misc.LayoutHelper;
import de.uni_koeln.dh.pera.util.Calc;

public class ImgComposite extends BaseComposite {

		private Logger logger = LoggerFactory.getLogger(getClass());
		private Map map = null;
		
		private SelectionListener placesSelection = new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				map.setLayerVisibility(
						getSelection(e), 
						map.getPlacesLayer());
			}
			public void widgetDefaultSelected(SelectionEvent e) {}
		};
		
		private SelectionListener routeSelection = new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {				
				map.setLayerVisibility(
						getSelection(e), 
						map.getRouteLayer());
			}
			public void widgetDefaultSelected(SelectionEvent e) {}
		};
		
		private SelectionListener territorySelection = new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {	
				map.setLayerVisibilities(
						getSelection(e), 
						map.getTerritoryLayers(), 
						map.getLegendShell(getTooltip(e)));
			}
			public void widgetDefaultSelected(SelectionEvent e) {}
		};
		
	public ImgComposite(Composite parent) {
		super(parent);
	}

	public void init(int height) {
		logger.info("Initialize image composite...".toUpperCase());	
		super.init(height);
		
		// sample data: http://al-teef.info/wp-content/uploads/2017/12/texture-seamless-seamless-city-textures-door-texture-65-best-wood-texture-images-on-pinterest-wood-texture-and-wood.jpg
		setBackgroundImage(new Image(display, "src/main/resources/img/wood.jpg"));		// TODO image reference (deployment)
		setInitialized(true);
	}
	
	public void addMapComponents() {
		logger.info("Add map components...");
		
		int height = getHeight();
		int mapHeight = (int) Calc.getValByPct(height, Map.H_HIMGCOMP_PCT);
		int headHeight = (height - mapHeight) / 2;

		setHeader(headHeight);
		setMap(mapHeight);		
		setControls();
	}
	
	private void setHeader(int headHeight) {
		logger.info("Initialize header...");
		
		Label header = new Label(this, SWT.CENTER);	
		header.setLayoutData(LayoutHelper.getCenteredData(parentWidth));
		header.setForeground(getDefaultFgColor());
//		header.setBackground(display.getSystemColor(SWT.COLOR_CYAN));
		header.setFont(getHeaderFont(headHeight));
		// TODO TEXT ADVENTURE: default heading
		header.setText(/*"Einführung"*/"Kapitel VII – Konstantinopel: Ein Besuch beim Kaiser");
	}

	private void setMap(int mapHeight) {
		map = new Map(this);
		map.init(mapHeight);
	}
	
	private void setControls() {
		logger.info("Initialize controls...");
		
		Composite ctrlComp = new Composite(this, SWT.NONE);
		ctrlComp.setLayout(getCtrlLayout());
		ctrlComp.setLayoutData(LayoutHelper.getCenteredData());
//		ctrlComp.setBackground(display.getSystemColor(SWT.COLOR_MAGENTA));

		Button placesButton = new Button(ctrlComp, SWT.TOGGLE);
		placesButton.setText("\u25bc"); 	// ▼
		placesButton.setToolTipText("Orte");
		placesButton.addSelectionListener(placesSelection);
		
		Button routeButton = new Button(ctrlComp, SWT.TOGGLE);
		routeButton.setText("\u2693︎");	// ⚓
		routeButton.setSelection(true);		// TODO connect with Map.layer
		routeButton.setToolTipText("Reiseroute");
		routeButton.addSelectionListener(routeSelection);
		
		Button territoryButton = new Button(ctrlComp, SWT.TOGGLE);
		territoryButton.setText("\u2655"); 	// ♕
		territoryButton.setToolTipText("Hoheitsgebiete");
		territoryButton.addSelectionListener(territorySelection);
	}
	
	///////////////////////////
	
	private Font getHeaderFont(int headHeight) {
		Font font = getFont("Palatino Linotype", (headHeight / 3), SWT.BOLD);
		return font;
	}
	
	private Layout getCtrlLayout() {
		RowLayout layout = LayoutHelper.getRowLayout(parentWidth / 8);
		return layout;
	}
	
	protected int getWidth() {
		return parentWidth;
	}
	
	public int getInnerWidth() {
		return map.getWidth();
	}
	
	private boolean getSelection(SelectionEvent e) {
		return ((Button)e.widget).getSelection();
	}
	
	private String getTooltip(SelectionEvent e) {
		return ((Button)e.widget).getToolTipText();
	}
	
}
