package de.uni_koeln.dh.pera.gui.core.img;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Listener;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.grid.GridCoverage2D;
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
		File rasterFile = new File("src/test/resources/pera/test.tif");
		
//									File shpFile = new File("src/test/resources/50m_cultural-edit/ne_50m_admin_0_sovereignty.shp");    		
//									File shpFile = new File("src/main/resources/gis/shapes/political/AegyptischesMamelukenSultanat.shp");
//									File shpFile = new File("src/main/resources/gis/shapes/Reiseroute.shp");
									
		try {
			AbstractGridFormat format = GridFormatFinder.findFormat(rasterFile);
			Hints hints = new Hints();
			
			if (format instanceof GeoTiffFormat) 
				hints = new Hints(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.TRUE);
			
			/*GridCoverage2DReader*/ reader = format.getReader(rasterFile, hints);

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

									politLayers = setShapeLayers("political/");
									mContent.addLayers(politLayers);
			
//									Layer shpLayer = new FeatureLayer(shapefileSource, shpStyle);
									
									routeLayer = setShapeLayer("Reiseroute", true);		// parent.getRouteSelection()	
									mContent.addLayer(routeLayer);
									chapterLayer = setShapeLayer("Kapitel_neu", false);		// TODO selection?
									mContent.addLayer(chapterLayer);	
									cityLayer = setShapeLayer("Standorte_neu", false);	// parent.getCitySelection()
									mContent.addLayer(cityLayer);
																		
									
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
	
		private Layer chapterLayer,
			routeLayer,
			cityLayer;	
		
		private List<Layer> politLayers;
	
	protected Layer getChapterLayer() {
		return chapterLayer;
	}	
	
	protected Layer getRouteLayer() {
		return routeLayer;
	}
	
	protected Layer getCityLayer() {
		return cityLayer;
	}
	
	protected List<Layer> getPolitLayers() {
		return politLayers;
	}
	
	private List<Layer> setShapeLayers(String folderName) {
		List<Layer> layers = new ArrayList<Layer>();
		
		layers.add(setShapeLayer(folderName.concat("AegyptischesMamelukenSultanat"), false));
		layers.add(setShapeLayer(folderName.concat("Byzantinische Gebiete"), false));
		layers.add(setShapeLayer(folderName.concat("Genuesische Gebiete"), false));
		layers.add(setShapeLayer(folderName.concat("Herzogtum Naxos"), false));
		layers.add(setShapeLayer(folderName.concat("Johanniterorden"), false));
		layers.add(setShapeLayer(folderName.concat("Kamariden Emirat"), false));
		layers.add(setShapeLayer(folderName.concat("Königreich Zypern"), false));
		layers.add(setShapeLayer(folderName.concat("OsmanischesReich"), false));
		layers.add(setShapeLayer(folderName.concat("Venezianische Gebiete"), false));
		
		return layers;
	}
	
		private Color[] colors = new Color[] { Color.WHITE, Color.BLUE, Color.CYAN, Color.MAGENTA, Color.ORANGE,
				Color.PINK, Color.YELLOW, Color.BLACK, Color.GRAY};
		private int idx = 0;
	
	private Layer setShapeLayer(String fileName, boolean visible) {
		File shpFile = new File("src/main/resources/gis/shapes", fileName + ".shp");
		
		try {
			FileDataStore dataStore = FileDataStoreFinder.getDataStore(shpFile);
			SimpleFeatureSource shpFileSrc = dataStore.getFeatureSource();
			
			Style shpStyle = null;
				
			if (fileName.equals("Kapitel_neu"))
				shpStyle = SLD.createPointStyle("Star", Color.BLACK, Color.YELLOW, 1.0f, 30.0f/*, null, null*/);
			else 
				if (fileName.equals("Reiseroute"))
					shpStyle = SLD.createLineStyle(Color.RED, 3.0f);
			else 
				if (fileName.equals("Standorte_neu")) {
					Font font = sf.getDefaultFont();
					font.setSize(ff.literal(20));
					// TODO font color
					shpStyle = SLD.createPointStyle("Circle", Color.BLACK, Color.GREEN, 1.0f, 10.0f, "Standort", font);
			} else {
				shpStyle = SLD.createPolygonStyle(Color.BLACK, colors[idx], 1.0f);
				idx++;
			}	
			
			Layer layer = new FeatureLayer(shpFileSrc, shpStyle);
			if (!visible) layer.setVisible(false);
			
			return layer;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
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
	
	/////////////////////////////////////////////
	
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

		private GridCoverage2DReader reader;

	/**
	 * This method examines the names of the sample dimensions in the provided
	 * coverage looking for "red...", "green..." and "blue..." (case insensitive
	 * match). If these names are not found it uses bands 1, 2, and 3 for the red,
	 * green and blue channels. It then sets up a raster symbolizer and returns this
	 * wrapped in a Style.
	 *
	 * @return a new Style object containing a raster symbolizer set up for RGB
	 *         image
	 */
	private Style createRGBStyle() {
		GridCoverage2D cov = null;
		try {
			cov = reader.read(null);
		} catch (IOException giveUp) {
			throw new RuntimeException(giveUp);
		}
		// We need at least three bands to create an RGB style
		int numBands = cov.getNumSampleDimensions();
		if (numBands < 3) {
			return null;
		}
		// Get the names of the bands
		String[] sampleDimensionNames = new String[numBands];
		for (int i = 0; i < numBands; i++) {
			GridSampleDimension dim = cov.getSampleDimension(i);
			sampleDimensionNames[i] = dim.getDescription().toString();
		}
		final int RED = 0, GREEN = 1, BLUE = 2;
		int[] channelNum = { -1, -1, -1 };
		// We examine the band names looking for "red...", "green...", "blue...".
		// Note that the channel numbers we record are indexed from 1, not 0.
		for (int i = 0; i < numBands; i++) {
			String name = sampleDimensionNames[i].toLowerCase();
			if (name != null) {
				if (name.matches("red.*")) {
					channelNum[RED] = i + 1;
				} else if (name.matches("green.*")) {
					channelNum[GREEN] = i + 1;
				} else if (name.matches("blue.*")) {
					channelNum[BLUE] = i + 1;
				}
			}
		}
		// If we didn't find named bands "red...", "green...", "blue..."
		// we fall back to using the first three bands in order
		if (channelNum[RED] < 0 || channelNum[GREEN] < 0 || channelNum[BLUE] < 0) {
			channelNum[RED] = 1;
			channelNum[GREEN] = 2;
			channelNum[BLUE] = 3;
		}
		// Now we create a RasterSymbolizer using the selected channels
		SelectedChannelType[] sct = new SelectedChannelType[cov.getNumSampleDimensions()];
		ContrastEnhancement ce = sf.contrastEnhancement(ff.literal(1.0), ContrastMethod.NORMALIZE);
		for (int i = 0; i < 3; i++) {
			sct[i] = sf.createSelectedChannelType(String.valueOf(channelNum[i]), ce);
		}
		RasterSymbolizer sym = sf.getDefaultRasterSymbolizer();
		ChannelSelection sel = sf.channelSelection(sct[RED], sct[GREEN], sct[BLUE]);
		sym.setChannelSelection(sel);

		return SLD.wrapSymbolizers(sym);
	}

}
