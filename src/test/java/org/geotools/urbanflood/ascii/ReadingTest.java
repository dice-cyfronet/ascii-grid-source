package org.geotools.urbanflood.ascii;

import java.net.URL;

import org.geotools.coverage.grid.GridCoverage2D;
import org.junit.Before;
import org.junit.Test;
import org.opengis.geometry.Envelope;

import pl.cyfronet.urbanflood.ogc.geoserver.data.ASCIIGridDataReader;


import static org.junit.Assert.*;
import static org.junit.Assume.*;

public class ReadingTest {
	
	private URL inputData ;
	
	@Before
	public void before() {
		inputData = this.getClass().getResource("/topography.txt");
		assumeNotNull(inputData);
	}
	
	@Test
	public void readTopo() throws Exception {
		ASCIIGridDataReader reader = new ASCIIGridDataReader(inputData.toString());
		assertEquals(1,reader.getGridCoverageCount());
		GridCoverage2D gc = reader.read(null);
		assertNotNull(gc);
		
		Envelope ev = gc.getEnvelope();
		assertNotNull(ev);
	}
	
	@Test
	public void readFromUrl() throws Exception {
		ASCIIGridDataReader reader = new ASCIIGridDataReader(inputData);
		assertNotNull(reader.read(null));
	}
	
	@Test
	public void originalEnvelopeNotNull() throws Exception {
		ASCIIGridDataReader reader = new ASCIIGridDataReader(inputData);
		assertNotNull(reader.getOriginalEnvelope());
		
	}
	
	@Test
	public void srsNotNull() throws Exception {
		ASCIIGridDataReader reader = new ASCIIGridDataReader(inputData);
		assertNotNull(reader.getCrs());
		
	}
}
