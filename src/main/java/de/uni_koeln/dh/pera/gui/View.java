package de.uni_koeln.dh.pera.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;

import de.uni_koeln.dh.pera.gui.misc.LayoutHelper;
import de.uni_koeln.dh.pera.util.Calc;

// main (ui) thread
public class View {
			
		// TODO if fitting set to final
		private static int H_HMONITOR_PCT = 80, 			// "height: 80% of the height of the monitor"
				W_HEIGHT_PCT = /*75*/80;					// "width: 80% of the height (of the app)"
	
		private Display display = null;
		private Shell shell = null;
		
		private Rectangle monitorBounds = null;
		
		private String title = null;
		private boolean initialized = false;
	
	public View(String title) {
		this.display = new Display();
		this.title = title;		
		
		Monitor monitor = display.getPrimaryMonitor();
		this.monitorBounds = monitor.getBounds();
	}
	
	public void init() {		
		shell = new Shell(display, getStyle());
		shell.setSize(getSizeByHeight(H_HMONITOR_PCT));		
		shell.setLocation(getCenter());		
		shell.setLayout(LayoutHelper.getDefaultLayout());
//		shell.setBackground(display.getSystemColor(SWT.COLOR_RED));	
		shell.setText(title);
		
		initialized = true;
	}
	
	public void loadComponents() {
		ClientArea cArea = new ClientArea(shell);
		cArea.init();
	}
	
	public void show() {
		shell.open();
		
		// TODO if needed add processing thread here
		
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) 
				display.sleep();
		}
	}
	
	public void dispose() {
		if (!display.isDisposed())
			display.dispose();
	}
	
	// style
	private int getStyle() {
		// default: SHELL_TRIM = CLOSE | TITLE | MIN | MAX | RESIZE
		// TODO deactivate MAX
		return SWT.CLOSE | SWT.TITLE | SWT.MIN;
	}
	
	// size
	private Point getSizeByHeight(int percentage) {
		final int monitorHeight = monitorBounds.height;
		
		// TODO height: maybe user choice via GUI
		float height = Calc.getValByPct(monitorHeight, percentage),		
				width = 	Calc.getValByPct(height, W_HEIGHT_PCT);
			
		return new Point((int)width, (int)height);
	}
	
	// location
	private Point getCenter() {
		// shell.getBounds() == outer area (!= client area)
		Rectangle bounds = shell.getBounds();	 
		
		int x = (monitorBounds.width - bounds.width) / 2, 
				y = (monitorBounds.height - bounds.height) / 2;
		
		return new Point(x, y);
	}	

	public boolean isInitialized() {
		if (!initialized)
			System.err.println("No view initialized.");
		
		return initialized;
	}

}
