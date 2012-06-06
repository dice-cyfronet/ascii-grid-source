package org.geotools.urbanflood.ascii;

import org.junit.Test;
import org.opengis.coverage.grid.Format;

import pl.cyfronet.urbanflood.ogc.geoserver.data.ASCIIGridFormatFactorySpi;
import static org.junit.Assert.*;

public class FactoryTest {
	
	
	@Test
	public void produceFormat() throws Exception {
		ASCIIGridFormatFactorySpi factory = new ASCIIGridFormatFactorySpi();
		Format f = factory.createFormat();
		assertNotNull(f);
	}
	
	@Test
	public void getSomeMetadata() throws Exception {
		ASCIIGridFormatFactorySpi factory = new ASCIIGridFormatFactorySpi();
		assertTrue(factory.isAvailable());
	}
}

