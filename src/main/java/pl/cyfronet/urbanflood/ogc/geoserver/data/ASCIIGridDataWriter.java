package pl.cyfronet.urbanflood.ogc.geoserver.data;

import java.io.IOException;

import org.geotools.coverage.grid.io.AbstractGridCoverageWriter;
import org.opengis.coverage.grid.Format;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.coverage.grid.GridCoverageWriter;
import org.opengis.parameter.GeneralParameterValue;

public class ASCIIGridDataWriter extends AbstractGridCoverageWriter implements
		GridCoverageWriter {
	

	public Format getFormat() {
		return new ASCIIGridDataFormat();
	}

	public void write(GridCoverage arg0, GeneralParameterValue[] arg1)
			throws IllegalArgumentException, IOException {
	}

}
