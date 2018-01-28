package de.uni_koeln.dh.pera.gui.core.img;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.Hints;
import org.geotools.gce.geotiff.GeoTiffFormat;
import org.geotools.map.FeatureLayer;
import org.geotools.map.GridReaderLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.renderer.lite.StreamingRenderer;
import org.geotools.styling.ChannelSelection;
import org.geotools.styling.ContrastEnhancement;
import org.geotools.styling.Font;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.SLD;
import org.geotools.styling.SelectedChannelType;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.geotools.swt.SwtMapPane;
import org.opengis.filter.FilterFactory2;
import org.opengis.style.ContrastMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uni_koeln.dh.pera.gui.misc.LayoutHelper;
import de.uni_koeln.dh.pera.util.Calc;

public class Map extends Composite {

		// TODO check PCT
		private final static int W_WIMGCOMP_PCT = /*80*/77;
			
		// TODO names
		private Color lineColor = Color.BLACK,
				fillColor = new Color(202,30,0);		// dark red
	
	// TODO
	private void configMapPane() {
		logger.info("Add map pane...");
		
  		/*
		 *  sample data (download): 
		 *  - http://udig.refractions.net/docs/data-v1_2.zip
		 *  - http://www.naturalearthdata.com/http//www.naturalearthdata.com/download/50m/cultural/50m_cultural.zip
		 */
	
		// JPEG image (+ meta [JPW/PRJ...], IMAGE API)
//		File rasterFile = new File("src/test/resources/data-v1_2/clouds.jpg");
//		File rasterFile = new File("src/test/resources/pera/jpeg/TextadventrueEmpty_geo.jpg");
	
		// JPEG2000 image (+ IMAGE API, IMAGEIO-EXT API)
//		File rasterFile = new File("src/test/resources/pera/jpeg2000/TextadventrueEmpty_geo.jp2");
		
		// GeoTIFF incl. meta
//		File rasterFile = new File("src/main/resources/gis/raster/TextadventrueEmpty_geo.tif");
		File rasterFile = new File("src/test/resources/pera/Textadventrue_neu.tif");
		
//									File shpFile = new File("src/test/resources/50m_cultural-edit/ne_50m_admin_0_sovereignty.shp");    		
//									File shpFile = new File("src/main/resources/gis/shapes/political/AegyptischesMamelukenSultanat.shp");
//									File shpFile = new File("src/main/resources/gis/shapes/Reiseroute.shp");
									
		try {
			AbstractGridFormat format = GridFormatFinder.findFormat(rasterFile);
			Hints hints = new Hints();
			
			if (format instanceof GeoTiffFormat) 
				hints = new Hints(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.TRUE);
			
			GridCoverage2DReader reader = format.getReader(rasterFile, hints);

			Style rasterStyle = createGreyscaleStyle(/*1*/);
//			Style rasterStyle = createRGBStyle();

//									FileDataStore dataStore = FileDataStoreFinder.getDataStore(shpFile);
//									SimpleFeatureSource shapefileSource = dataStore.getFeatureSource();
////									Style shpStyle = SLD.createPolygonStyle(java.awt.Color.RED, /*java.awt.Color.RED*/null, 1.0f);
//									Style shpStyle = SLD.createLineStyle(java.awt.Color.RED, 3.0f);

			final MapContent mContent = new MapContent();
			mContent.setTitle("ImageLab");

			Layer rasterLayer = new GridReaderLayer(reader, rasterStyle);
			mContent.addLayer(rasterLayer);

									territoryLayers = setShapeLayers("territories");
									mContent.addLayers(territoryLayers);
			
//									Layer shpLayer = new FeatureLayer(shapefileSource, shpStyle);
									
									routeLayer = setShapeLayer("Reiseroute", true);		// parent.getRouteSelection()	
									mContent.addLayer(routeLayer);
//									chapterLayer = setShapeLayer("Kapitel_neu", false);		// TODO selection?
//									mContent.addLayer(chapterLayer);	
									placesLayer = setShapeLayer("Standorte_neu", false);	// parent.getCitySelection()
									mContent.addLayer(placesLayer);
									
			//////////////////////////////			
								
			SwtMapPane mPane = new SwtMapPane(this, SWT.NO_BACKGROUND);
			mPane.setBackground(parent.getDefaultBgColor());		// TODO pane standard color?
			mPane.setRenderer(new StreamingRenderer());			
			mPane.setMapContent(mContent);
			mPane.setLayoutData(getMapLayoutData());

			// TODO bug: SwtMapPane/MapMouseListener and macOS because of THIS (Composite)
			int eventType = SWT.MouseDown;
			for (Listener listener : mPane.getListeners(eventType)) 
				mPane.removeListener(eventType, listener);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// TODO connect visibility with ImgComposite.button
	private Layer setShapeLayer(String subPath, boolean visible) {
		Layer layer = null;
		File file = new File("src/main/resources/gis/shapes", subPath + ".shp");		// TODO file reference (deployment)
		
		try {
			FileDataStore dataStore = FileDataStoreFinder.getDataStore(file);
			SimpleFeatureSource fileSrc = dataStore.getFeatureSource();
			
			Style style = null;
			
			if (!subPath.contains(File.separator)) {
				if (subPath.startsWith("Standorte")) {
					style = SLD.createPointStyle(
								"Circle", 
								lineColor, fillColor, 
								1.0f/*opacity*/, 
								7.0f/*size*/, 					// TODO dynamize!
								"Standort", setFont());
				} else
					if (subPath.startsWith("Reiseroute")) {
						style = SLD.createLineStyle(
									fillColor, 		
									1.5f/*size*/);				// TODO dynamize?
				}
			} else {			// territories/
				// TODO optimize workaround?
				String namePrefix = file.getName().substring(0,8);
				
				for (String name : territoriesMap.keySet()) {
					if (name.startsWith(namePrefix)) {
						Integer[] c = territoriesMap.get(name);
						style = SLD.createPolygonStyle(
									lineColor, new Color(c[0],c[1],c[2]), 
									0.5f/*opacity*/);
						
						break;
					}
				}
			}
			
			layer = new FeatureLayer(fileSrc, style);
			if (!visible) layer.setVisible(false);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return layer;
	}
	
	// TODO name, color
	private Font setFont() {
		Font font = sf.getDefaultFont();
		font.setSize(ff.literal(15));		// TODO dynamize
		font.setStyle(ff.literal(Font.Style.ITALIC));
		font.setWeight(ff.literal(Font.Weight.BOLD));

		return font;
	}
	
	protected void init(int height) {
		this.height = height;
		
		configComposite();
		
		// TODO kapseln
		setTerritories();
		configMapPane();
		setLegend();
	}
	
		

	//////////////////////////////////////////////////////////////////////////////////////////	


	
		private Logger logger = LoggerFactory.getLogger(getClass());
	
		protected final static int H_HIMGCOMP_PCT = 75;
	
		private ImgComposite parent = null;
		private Shell legendShell;		
		
		private GridLayout layout = null;
		
		private int width = 0,
				height = 0;
		
		private java.util.Map<String, Integer[]> territoriesMap;
		
		private Layer placesLayer,
			routeLayer;	
		private List<Layer> territoryLayers;
	
	protected Map(ImgComposite parent) {
		super(parent, SWT.NONE);
		this.parent = parent;
	}
	
	private void configComposite() {
		logger.info("Initialize map composite...");
		
		setLayout(getCompositeLayout());
		setCompositeLayoutData();			
		setBackground(parent.getDefaultBgColor());
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
	
	private void setTerritories() {
		territoriesMap = new TreeMap<String, Integer[]>();	// name, rgb
		
		territoriesMap.put("Aegyptisches Mameluken Sultanat", new Integer[] { 180,133,21 });	// brown
		territoriesMap.put("Byzantinische Gebiete", new Integer[] { 227,25,77 });				// red
		territoriesMap.put("Genuesische Gebiete", new Integer[] { 195,54,176 });				// pink/magenta
		territoriesMap.put("Herzogtum Naxos", new Integer[] { 14,38,178 });					// dark blue
		territoriesMap.put("Johanniterorden", new Integer[] { 91,245,71 });					// bright green
		territoriesMap.put("Kamariden Emirat", new Integer[] { 31,120,180 });					// bright blue
		territoriesMap.put("Königreich Zypern", new Integer[] { 235,131,23 });				// orange
		territoriesMap.put("Osmanisches Reich", new Integer[] { 238,234,73 });				// yellow
		territoriesMap.put("Venezianische Gebiete", new Integer[] { 36,112,31 });				// dark green
	}	
	
	// TODO optimize layout?
	private void setLegend() {
		Display display = Display.getCurrent();
		
		legendShell = new Shell(display, SWT.TITLE | SWT.MIN);
		legendShell.setLayout(LayoutHelper.getVerticalFillLayout());
		legendShell.setBackground(parent.getDefaultBgColor());
		
		for (String name : territoriesMap.keySet()) {
			Integer[] c = territoriesMap.get(name);
			
			Label label = new Label(legendShell, SWT.NONE);
			label.setText(name);
			label.setBackground(new org.eclipse.swt.graphics.Color(display, c[0], c[1], c[2]));
		}
		
		legendShell.setVisible(false);
		legendShell.pack();
	}
	
	private List<Layer> setShapeLayers(String dirName) {
		List<Layer> layers = new ArrayList<Layer>();
		
		String subPath = dirName.concat(File.separator);
		boolean visible = false;
		
		layers.add(setShapeLayer(
				subPath.concat("AegyptischesMamelukenSultanat"), 
				visible));
		layers.add(setShapeLayer(
				subPath.concat("Byzantinische Gebiete"),
				visible));
		layers.add(setShapeLayer(
				subPath.concat("Herzogtum Naxos"),
				visible));
		layers.add(setShapeLayer(
				subPath.concat("Johanniterorden"),
				visible));
		layers.add(setShapeLayer(
				subPath.concat("Kamariden Emirat"),
				visible));
		layers.add(setShapeLayer(
				subPath.concat("Königreich Zypern"),
				visible));
		layers.add(setShapeLayer(
				subPath.concat("OsmanischesReich"),
				visible));
		layers.add(setShapeLayer(
				subPath.concat("Venezianische Gebiete"),
				visible));
		layers.add(setShapeLayer(
				subPath.concat("Genuesische Gebiete"),
				visible));
		
		return layers;
	}
	
	protected Shell getLegendShell(String tooltip) {
		if (legendShell.getText().equals("")) 
			legendShell.setText(tooltip);
		
		return legendShell;
	}
	
	protected void setLayerVisibility(boolean selected, Layer layer) {
		layer.setVisible(selected);
	}	
		
	protected void setLayerVisibilities(boolean selected, List<Layer> layers, Composite comp) {
		for (Layer layer : layers) 
			setLayerVisibility(selected, layer);
		
		comp.setVisible(selected);
	}
	
	protected Layer getPlacesLayer() {
		return placesLayer;
	}
	
	protected Layer getRouteLayer() {
		return routeLayer;
	}	
	
	protected List<Layer> getTerritoryLayers() {
		return territoryLayers;
	}
	
	/////////////////////////////////////////////
		
//		private GridCoverage2DReader reader;
		private StyleFactory sf = CommonFactoryFinder.getStyleFactory();
		private FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();

	/**
	 * Create a Style to display the specified band of the GeoTIFF image as a
	 * greyscale layer.
	 * <p>
	 * This method is a helper for createGreyScale() and is also called directly by
	 * the displayLayers() method when the application first starts.
	 *
	 * @param band
	 *            the image band to use for the greyscale display
	 *
	 * @return a new Style instance to render the image in greyscale
	 */
	private Style createGreyscaleStyle(/* int band */) {
		ContrastEnhancement ce = sf.contrastEnhancement(ff.literal(1.0), ContrastMethod.NORMALIZE);
		SelectedChannelType sct = sf.createSelectedChannelType(String.valueOf(/* band */1), ce);
	
		RasterSymbolizer sym = sf.getDefaultRasterSymbolizer();
		ChannelSelection sel = sf.channelSelection(sct);
		sym.setChannelSelection(sel);
	
		return SLD.wrapSymbolizers(sym);
	}

//	/**
//	 * This method examines the names of the sample dimensions in the provided
//	 * coverage looking for "red...", "green..." and "blue..." (case insensitive
//	 * match). If these names are not found it uses bands 1, 2, and 3 for the red,
//	 * green and blue channels. It then sets up a raster symbolizer and returns this
//	 * wrapped in a Style.
//	 *
//	 * @return a new Style object containing a raster symbolizer set up for RGB
//	 *         image
//	 */
//	private Style createRGBStyle() {
//		GridCoverage2D cov = null;
//		try {
//			cov = reader.read(null);
//		} catch (IOException giveUp) {
//			throw new RuntimeException(giveUp);
//		}
//		// We need at least three bands to create an RGB style
//		int numBands = cov.getNumSampleDimensions();
//		if (numBands < 3) {
//			return null;
//		}
//		// Get the names of the bands
//		String[] sampleDimensionNames = new String[numBands];
//		for (int i = 0; i < numBands; i++) {
//			GridSampleDimension dim = cov.getSampleDimension(i);
//			sampleDimensionNames[i] = dim.getDescription().toString();
//		}
//		final int RED = 0, GREEN = 1, BLUE = 2;
//		int[] channelNum = { -1, -1, -1 };
//		// We examine the band names looking for "red...", "green...", "blue...".
//		// Note that the channel numbers we record are indexed from 1, not 0.
//		for (int i = 0; i < numBands; i++) {
//			String name = sampleDimensionNames[i].toLowerCase();
//			if (name != null) {
//				if (name.matches("red.*")) {
//					channelNum[RED] = i + 1;
//				} else if (name.matches("green.*")) {
//					channelNum[GREEN] = i + 1;
//				} else if (name.matches("blue.*")) {
//					channelNum[BLUE] = i + 1;
//				}
//			}
//		}
//		// If we didn't find named bands "red...", "green...", "blue..."
//		// we fall back to using the first three bands in order
//		if (channelNum[RED] < 0 || channelNum[GREEN] < 0 || channelNum[BLUE] < 0) {
//			channelNum[RED] = 1;
//			channelNum[GREEN] = 2;
//			channelNum[BLUE] = 3;
//		}
//		// Now we create a RasterSymbolizer using the selected channels
//		SelectedChannelType[] sct = new SelectedChannelType[cov.getNumSampleDimensions()];
//		ContrastEnhancement ce = sf.contrastEnhancement(ff.literal(1.0), ContrastMethod.NORMALIZE);
//		for (int i = 0; i < 3; i++) {
//			sct[i] = sf.createSelectedChannelType(String.valueOf(channelNum[i]), ce);
//		}
//		RasterSymbolizer sym = sf.getDefaultRasterSymbolizer();
//		ChannelSelection sel = sf.channelSelection(sct[RED], sct[GREEN], sct[BLUE]);
//		sym.setChannelSelection(sel);
//
//		return SLD.wrapSymbolizers(sym);
//	}
	
}
