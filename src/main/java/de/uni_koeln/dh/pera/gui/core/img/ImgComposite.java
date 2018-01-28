package de.uni_koeln.dh.pera.gui.core.img;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.geotools.map.Layer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uni_koeln.dh.pera.gui.core.BaseComposite;
import de.uni_koeln.dh.pera.gui.misc.LayoutHelper;
import de.uni_koeln.dh.pera.util.Calc;

public class ImgComposite extends BaseComposite {

		private Logger logger = LoggerFactory.getLogger(getClass());
		
		private Map map = null;
		
		// TODO zoom?
		/*
		private SelectionListener zoomOutSelection = new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				System.out.println("zoomOut");
			}
			public void widgetDefaultSelected(SelectionEvent e) {}
		};
		
		private SelectionListener zoomInSelection = new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				System.out.println("zoomIn");
			}
			public void widgetDefaultSelected(SelectionEvent e) {}
		};
		*/
		
		// TODO city layer
		private SelectionListener citySelection = new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				boolean selected = ((Button)e.widget).getSelection();
				System.out.println("city: " + selected);
				
				map.getCityLayer().setVisible(selected);
			}
			public void widgetDefaultSelected(SelectionEvent e) {}
		};
		
		private SelectionListener routeSelection = new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {				
				boolean selected = ((Button)e.widget).getSelection();
//				System.out.println("route: " + selected);
				
				map.getRouteLayer().setVisible(selected);
			}
			public void widgetDefaultSelected(SelectionEvent e) {}
		};
		
		private Shell shell;
		
		private SelectionListener politSelection = new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {				
				boolean selected = ((Button)e.widget).getSelection();
//				System.out.println("polit.: " + selected);
				
				

				
				shell.setVisible(selected);
				
				
				for (Layer layer : map.getPolitLayers()) 
					layer.setVisible(selected);
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
		
		
		
		
		
		
		shell = new Shell(display, SWT.TITLE | SWT.MIN);
		shell.setText("Child Shell");
		shell.setSize(200, 200);
		shell.setVisible(false);
		
		
		
		
		
		
		int height = getHeight();
		int mapHeight = (int) Calc.getValByPct(height, Map.H_HIMGCOMP_PCT);
		int headHeight = (height - mapHeight) / 2;

		setHeader(headHeight);
		
		map = new Map(this);
		map.init(mapHeight);
		
		setControls();
	}
	
	private void setHeader(int headHeight) {
		logger.info("Initialize header...");
		
		Label header = new Label(this, SWT.CENTER);	
	
		// TODO layout
		header.setLayoutData(/*LayoutHelper.getCenteredData()*/LayoutHelper.getAlignment(LayoutHelper.getGridData(parentWidth), true, true));
		header.setForeground(getDefaultFgColor());
		header.setFont(getHeaderFont(headHeight));
		header.setText(/*"Einführung"*/"Kapitel VII – Konstantinopel: Ein Besuch beim Kaiser");	// TODO default text
	}

	private void setControls() {
		logger.info("Initialize controls...");
		
		Composite ctrlComp = new Composite(this, SWT.NONE);
		ctrlComp.setLayout(getCtrlLayout());
		ctrlComp.setLayoutData(LayoutHelper.getCenteredData());
//		ctrlComp.setBackground(display.getSystemColor(SWT.COLOR_MAGENTA));
				
		/*
		Button zoomOutButton = new Button(ctrlComp, SWT.PUSH);
		zoomOutButton.setText("-");
		zoomOutButton.setEnabled(false);
		zoomOutButton.addSelectionListener(zoomOutSelection);
		
		Button zoomInButton = new Button(ctrlComp, SWT.PUSH);
		zoomInButton.setText("+");
		zoomInButton.setEnabled(false);
		zoomInButton.addSelectionListener(zoomInSelection);
		*/
		
		// TODO unicode
		
		cityButton = new Button(ctrlComp, SWT.TOGGLE);
		cityButton.setText("\u25bc");
		cityButton.setToolTipText("Orte");
		cityButton.addSelectionListener(citySelection);
		
		routeButton = new Button(ctrlComp, SWT.TOGGLE);
		routeButton.setText("⚓︎");
		routeButton.setSelection(true);
		routeButton.setToolTipText("Reiseroute");
		routeButton.addSelectionListener(routeSelection);
		
		politButton = new Button(ctrlComp, SWT.TOGGLE);
		politButton.setText("♕");
		politButton.setToolTipText("Hoheitsgebiete");
		politButton.addSelectionListener(politSelection);
	}

		private Button cityButton, 
			routeButton,
			politButton;
		
//	protected boolean getCitySelection() {
//		return cityButton.getSelection();
//	}	
//	
//	protected boolean getRouteSelection() {
//		return routeButton.getSelection();
//	}
//	
//	protected boolean getPolitSelection() {
//		return politButton.getSelection();
//	}
	
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
	
}
