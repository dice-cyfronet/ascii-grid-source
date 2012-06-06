package pl.cyfronet.urbanflood.ogc.geoserver.data;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.logging.Level;

import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.imageio.GeoToolsWriteParams;
import org.geotools.data.DataSourceException;
import org.geotools.factory.Hints;
import org.geotools.parameter.DefaultParameterDescriptorGroup;
import org.geotools.parameter.ParameterGroup;
import org.opengis.coverage.grid.Format;
import org.opengis.coverage.grid.GridCoverageWriter;
import org.opengis.parameter.GeneralParameterDescriptor;

public class ASCIIGridDataFormat extends AbstractGridFormat implements Format {
	
	private java.util.logging.Logger log = org.geotools.util.logging.Logging.getLogger(ASCIIGridDataFormat.class);

	
	
	
	public ASCIIGridDataFormat() {
		mInfo = new HashMap<String, String>();
		mInfo.put("name", "ASCII topography");
		mInfo.put("vendor", "Cyfronet");
		mInfo.put("version", "0.1");
		mInfo.put("description", "Topography reader for UrbanFlood project");
		log.log(Level.INFO, "Ascii data format initialized");
		
		// reading parameters
		readParameters = new ParameterGroup(
		new DefaultParameterDescriptorGroup(
		mInfo,
		new GeneralParameterDescriptor[] { READ_GRIDGEOMETRY2D }));
		
		// writing parameters
		writeParameters = null;
	}
	
	@Override
	public AbstractGridCoverage2DReader getReader(Object source) {
		try {
			return new ASCIIGridDataReader(source);
		}
		catch (DataSourceException ex) {
			log.log(Level.WARNING, ex.getLocalizedMessage(), ex);
			return null;
		}
	}

	@Override
	public AbstractGridCoverage2DReader getReader(Object source, Hints hints) {
		return getReader(source);
	}

	@Override
	public GridCoverageWriter getWriter(Object destination) {
		return new ASCIIGridDataWriter();
	}

	@Override
	public boolean accepts(Object source, Hints hints) {
		if (source == null) {
			return false;
		}
		if (source instanceof File || source instanceof URL) {
			return true;
		}
		return false;
	}

	@Override
	public GeoToolsWriteParams getDefaultImageIOWriteParameters() {
		return new ASCIIGridWriteParams();
	}

	@Override
	public GridCoverageWriter getWriter(Object destination, Hints hints) {
		throw new UnsupportedOperationException("this is a read-only plugin");
	}
}
