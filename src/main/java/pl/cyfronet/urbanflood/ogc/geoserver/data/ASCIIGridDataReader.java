package pl.cyfronet.urbanflood.ogc.geoserver.data;

import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.regex.Pattern;

import org.geotools.coverage.CoverageFactoryFinder;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.data.DataSourceException;
import org.geotools.data.DataUtilities;
import org.geotools.factory.Hints;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.metadata.iso.extent.GeographicBoundingBoxImpl;
import org.opengis.coverage.grid.Format;
import org.opengis.metadata.extent.GeographicBoundingBox;
import org.opengis.parameter.GeneralParameterValue;


public class ASCIIGridDataReader extends AbstractGridCoverage2DReader {
	
	private static final java.util.logging.Logger log = org.geotools.util.logging.Logging.getLogger(ASCIIGridDataFormat.class);
	
	private static final Pattern WS = Pattern.compile("\\s+"); 
	
	private InputStream inputStream;
	
	private int nCols;
	
	private int nRows;
	
	private long xllCorner;
	
	private long yllCorner;
	
	private int cellSize;
	
	private float noData;
	
	private void readMetadata() throws DataSourceException {
		try {
			final double ratio = 10000.0;
			InputStream input = getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			//read first 6 lines
			String line = reader.readLine();
			nCols = Integer.parseInt(WS.split(line)[1]);
			line = reader.readLine();
			nRows = Integer.parseInt(WS.split(line)[1]);
			line=reader.readLine();
			xllCorner = Long.parseLong(WS.split(line)[1]);
			line = reader.readLine();
			yllCorner = Long.parseLong(WS.split(line)[1]);
			line = reader.readLine();
			cellSize = Integer.parseInt(WS.split(line)[1]); //cell size, typically in meters
			line = reader.readLine();
			noData = Float.parseFloat(WS.split(line)[1]);
			
			reader.close();
			
			Rectangle dim = new Rectangle(0,0,nCols,nRows);
			
			originalGridRange = new GridEnvelope2D(dim);

			crs = AbstractGridFormat.getDefaultCRS();
			
			//the parameters here are minx, maxx miny, maxy
			GeographicBoundingBox gbb = new GeographicBoundingBoxImpl(xllCorner, xllCorner + cellSize * nCols, yllCorner, yllCorner + nRows * cellSize);
			originalEnvelope = new GeneralEnvelope(gbb);
			originalEnvelope.setCoordinateReferenceSystem(crs);
		}
		catch (IOException ex) {
			throw new DataSourceException(ex);
		}
		
	}
	
	protected InputStream getInputStream() throws DataSourceException {
		try {
			if (source instanceof File) {
				inputStream = new FileInputStream((File)source);
			}
			else if (source instanceof InputStream) {
				inputStream = (InputStream) source;
			}
			return inputStream;
		}
		catch (IOException ex) {
			throw new DataSourceException(ex);
		}
	}

	public ASCIIGridDataReader(Object input, Hints uHints)
			throws DataSourceException {
		
		if (this.hints == null)
			this.hints = new Hints();
		if (uHints != null) {
			this.hints.add(uHints);
		}
		this.coverageFactory = CoverageFactoryFinder.getGridCoverageFactory(this.hints);
		
		if (input instanceof String) {
			try {
				input = new URL((String)input);
			} catch (MalformedURLException e) {
				throw new DataSourceException("invalid source path " + input, e);
			}
		}
		
		if (input instanceof URL) {
			final URL sourceUrl = (URL) input;
			source = DataUtilities.urlToFile(sourceUrl);
		}
		else {
			source = input;
		}
		
		if (source instanceof File) {
			coverageName = ((File)source).getName();
		}
		log.log(Level.INFO, "reading from " + source);
		
		readMetadata();
	}

	public ASCIIGridDataReader(Object input) throws DataSourceException {
		this(input, null);
	}

	public Format getFormat() {
		return new ASCIIGridDataFormat();
	}

	@Override
	public GridCoverage2D read(GeneralParameterValue[] parameters)
			throws IllegalArgumentException, IOException {
		
		InputStream inStream = getInputStream();
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));
		
		log.fine("the matrix to be read is " + nRows +"x" + nCols);
		//now we can read the data itself
		
		float [][] data = new float[nRows][nCols];
		int i = 0,j;
		int metadataLineCount = 6;
		//skip first 6 lines - they are the metadata
		while (metadataLineCount > 0) {
			reader.readLine();
			metadataLineCount --;
		}
		String line = reader.readLine();
		float val;
		while (i < nRows && line != null) {
			String [] colValues = WS.split(line);
			if (colValues.length != nCols) {
				throw new IOException("number of columns in line " + (i + 6) + " is " + colValues.length +". Should be " + nCols);
			}
			for (j = 0; j < colValues.length; j ++) {
				val = Float.parseFloat(colValues[j]);
				if (val == noData) {
					val = Float.NaN;
				}
				data[i][j] = val;
			}
			i++;
			line = reader.readLine();
		}
		return coverageFactory.create(coverageName, data, originalEnvelope);
	}

	@Override
	public int getGridCoverageCount() {
		return 1;
	}

	@Override
	public void dispose() {
		log.fine("closing");
	}

	
	
}

