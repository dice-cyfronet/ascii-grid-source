package pl.cyfronet.urbanflood.ogc.geoserver.data;

import java.awt.RenderingHints.Key;
import java.util.Collections;
import java.util.Map;

import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridFormatFactorySpi;

public class ASCIIGridFormatFactorySpi implements GridFormatFactorySpi {
	
	public ASCIIGridFormatFactorySpi() {
		
	}

	public boolean isAvailable() {
		return true;
	}

	public Map<Key, ?> getImplementationHints() {
		return Collections.emptyMap();
	}

	public AbstractGridFormat createFormat() {
		return new ASCIIGridDataFormat();
	}
	
	

}
