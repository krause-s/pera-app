package de.uni_koeln.dh.pera.gui.core.img;

import java.io.File;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.renderer.lite.StreamingRenderer;
import org.geotools.styling.SLD;
import org.geotools.styling.Style;
import org.geotools.swt.SwtMapPane;

// TODO GeoTools
public class MapPane /*extends SwtMapPane*/ {
	
	public static void main(String[] args) throws Exception {
		Display display = new Display();
		
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		
		setMap(shell);
		shell.open();
		
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) 
				display.sleep();
		}
		
		shell.dispose();
	}
	
	private static void setMap(Shell shell) throws Exception {
		// download: http://www.naturalearthdata.com/http//www.naturalearthdata.com/download/50m/cultural/50m_cultural.zip
		File file = new File("src/test/resources/ne_50m_admin_0_sovereignty.shp");
		
		ShapefileDataStore store = new ShapefileDataStore(file.toURI().toURL());
		SimpleFeatureSource featureSource = store.getFeatureSource();

		Style style = SLD.createSimpleStyle(featureSource.getSchema());
		Layer layer = new FeatureLayer(featureSource, style);

		MapContent mContent = new MapContent();
		mContent.addLayer(layer);

		SwtMapPane mPane = new SwtMapPane(shell, org.eclipse.swt.SWT.BORDER | org.eclipse.swt.SWT.NO_BACKGROUND);
		mPane.setBackground(Display.getCurrent().getSystemColor(org.eclipse.swt.SWT.COLOR_WHITE));
		mPane.setRenderer(new StreamingRenderer());
		mPane.setMapContent(mContent);
	}

}
