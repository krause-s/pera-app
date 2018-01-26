package de.uni_koeln.dh.pera.gui.core.img;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.renderer.lite.StreamingRenderer;
import org.geotools.styling.SLD;
import org.geotools.styling.Style;
import org.geotools.swt.SwtMapPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uni_koeln.dh.pera.gui.misc.LayoutHelper;
import de.uni_koeln.dh.pera.util.Calc;

public class Map extends Composite {
	
		private Logger logger = LoggerFactory.getLogger(getClass());
	
		// TODO if fitting set to final
		protected static int H_HIMGCOMP_PCT = 75;
		private static int W_WIMGCOMP_PCT = 80;
	
		private ImgComposite parent = null;
		private GridLayout layout = null;
		
		private int width = 0,
				height = 0;
	
	protected Map(ImgComposite parent) {
		super(parent, SWT.NONE);
		this.parent = parent;
	}
	
	protected void init(int height) {
		this.height = height;
		
		configComposite();
		configMapPane();
	}

	private void configComposite() {
		logger.info("Initialize map composite...");
		
		setLayout(getCompositeLayout());
		setCompositeLayoutData();			
		setBackground(parent.getDefaultBgColor());
	}
	
	// TODO GeoTools (GIS)
	private void configMapPane() {
		logger.info("Add map pane...");
		
		// sample data (download): http://www.naturalearthdata.com/http//www.naturalearthdata.com/download/50m/cultural/50m_cultural.zip
		File file = new File("src/test/resources/ne_50m_admin_0_sovereignty.shp");
		
		try {
			ShapefileDataStore store = new ShapefileDataStore(file.toURI().toURL());
			SimpleFeatureSource featureSource = store.getFeatureSource();
	
			Style style = SLD.createSimpleStyle(featureSource.getSchema());
			Layer layer = new FeatureLayer(featureSource, style);
			
			MapContent mContent = new MapContent();
			mContent.addLayer(layer);
			
			SwtMapPane mPane = new SwtMapPane(this, SWT.NO_BACKGROUND);
			mPane.setBackground(parent.getDefaultBgColor());		// TODO pane standard color?
			mPane.setRenderer(new StreamingRenderer());
			mPane.setMapContent(mContent);
			mPane.setLayoutData(getMapLayoutData());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private GridLayout getCompositeLayout() {
		layout = LayoutHelper.getGridLayout();
		return layout;
	}
	
	private void setCompositeLayoutData() {
		width = (int) Calc.getValByPct(parent.getWidth(), W_WIMGCOMP_PCT);

		setLayoutData(LayoutHelper.getGridData(
				width, height, 
				false, true));
	}
	
	private GridData getMapLayoutData() {
		int width = this.width - (2*layout.marginWidth),
				height = this.height - (2*layout.marginHeight);
		
		return LayoutHelper.getGridData(width, height);
	}
	
	protected int getWidth() {
		return width;
	}

}
