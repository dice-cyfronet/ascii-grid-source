package pl.cyfronet.urbanflood.ogc.geoserver.data;

import java.util.Locale;

import javax.imageio.ImageWriteParam;

import org.geotools.coverage.grid.io.imageio.GeoToolsWriteParams;

public class ASCIIGridWriteParams extends GeoToolsWriteParams {

	public ASCIIGridWriteParams() {
		super(new ImageWriteParam(Locale.getDefault()));
	}

}
