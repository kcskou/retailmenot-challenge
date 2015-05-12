package clickouts.test;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import clickouts.util.MetricsGetter;

/**
 * JUnit tests for the MetricsGetter class.
 */
public class MetricsGetterTest {
	
	private Map<String, Integer> clickRates;
	private MetricsGetter mg;

	@Before
	public void setUp() throws Exception {
		clickRates = new HashMap<>();
		clickRates.put("00:12", 3);
		clickRates.put("00:25", 12);
		clickRates.put("00:46", 7);
		clickRates.put("01:43", 8);
		clickRates.put("21:44", 1);
		mg = new MetricsGetter(clickRates);
	}

	@Test
	public void testComputeTotalClicks() {		
		assertEquals(3 + 12 + 7 + 8 + 1, mg.getTotalClicks());
	}
	
	@Test
	public void testComputeMinMax() {		
		assertEquals(1, mg.getMin());
		assertEquals(12, mg.getMax());
		assertEquals("21:44", mg.getTimeAtMin());
		assertEquals("00:25", mg.getTimeAtMax());		
	}

	@Test
	public void testComputeMean() {
		assertEquals((3 + 12 + 7 + 8 + 1) / 5.0, mg.getMean(), 0.001);
	}
	
	@Test
	public void testComputeMedianOddNumberOfValues() {
		assertEquals(7, mg.getMedian(), 0.001);
	}

	@Test
	public void testComputeMedianEvenNumberOfValues() {
		clickRates.put("15:32", 16);
		mg = new MetricsGetter(clickRates);
		assertEquals((7 + 8) / 2.0, mg.getMedian(), 0.001);
	}
	
	@Test
	public void testComputerSd() {
		double mean = 6.2;
		double sumOfSquares = Math.pow(3 - mean, 2) +
				              Math.pow(12 - mean, 2) +
				              Math.pow(7 - mean, 2) +
				              Math.pow(8 - mean, 2) +
				              Math.pow(1 - mean, 2);
		double variance = sumOfSquares / (5 - 1);
		double sd = Math.sqrt(variance);
		assertEquals(sd, mg.getSd(), 0.001);
	}
}
