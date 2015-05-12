package clickouts.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * An object that computes descriptive statistics about
 * click out rates per minute.
 */
public class MetricsGetter {
	
	private Map<String, Integer> clickRates;
	private int totalClicks;
	private int totalMinutes;
	List<Integer> values;
	private int min;
	private int max;
	private String timeAtMin;
	private String timeAtMax;
	private double mean;
	private double median;
	private double sd;
	
	/**
	 * Constructor
	 * @param clickRates A map that represents the count of
	 *        click outs for each minute.
	 */
	public MetricsGetter(Map<String, Integer> clickRates) {
		this.clickRates = clickRates;
		totalMinutes = clickRates.size();
		values = new ArrayList<>(clickRates.values());
		computeTotalClicks();
		computeMinMax();
		computeMean();
		computeMedian();
		computeSD();
	}
	
	/**
	 * Compute total count of click outs.
	 */
	private void computeTotalClicks() {
		int total = 0;
		for(int value : values) {
			total += value;
		}
		totalClicks = total;
	}

	/**
	 * Compute min and max at the same time with a single loop
	 */
	private void computeMinMax() {
		Iterator<Entry<String, Integer>> it = clickRates.entrySet().iterator();
		// initialize candidates with the first element
		Entry<String, Integer> minCandidate = it.next();
		Entry<String, Integer> maxCandidate = minCandidate;
		while (it.hasNext()) {
			Entry<String, Integer> next = it.next();
			if (next.getValue() > maxCandidate.getValue()) {
				maxCandidate = next;
			} else if (next.getValue() < minCandidate.getValue()) {
				minCandidate = next;
			}
		}
		min = minCandidate.getValue();
		max = maxCandidate.getValue();
		timeAtMin = minCandidate.getKey();
		timeAtMax = maxCandidate.getKey();
	}
	
	/**
	 * Compute mean of click out per minute.
	 */
	private void computeMean() {
		mean = totalClicks / (double) totalMinutes;		
	}

	/**
	 * Compute median of click out per minute.
	 */
	private void computeMedian() {
		Collections.sort(values);
	    int middle = totalMinutes / 2;
	    if (totalMinutes % 2 == 1) {
	        median = (double) values.get(middle);
	    } else {
	        median = (values.get(middle - 1) + values.get(middle)) / 2.0;
	    }
	}
	
	/**
	 * Compute sample standard deviation of click out per minute.
	 */
	private void computeSD() {
		double sum = 0;
		double variance;
	    for (Integer x : values) {
	    	sum += (x - mean) * (x - mean);
	    }
	    variance = sum / (totalMinutes - 1);
	    sd = Math.sqrt(variance);
	}
	
	/**
	 * Print descriptive statistics to console.
	 */
	public void report() {
		System.out.println("Found " + totalClicks +
				" clicks out spanning " + totalMinutes + " minutes.");
		System.out.println("Descriptive statistics of clicks out per minute:");
		System.out.println("Min: " + min + " at " + timeAtMin);
		System.out.println("Max: " + max + " at " + timeAtMax);
		System.out.println("Mean: " + mean);
		System.out.println("Median: " + median);
		System.out.println("Standard deviation: " + sd);	
	}
	
	/** GETTERS **/

	public Map<String, Integer> getClickRates() {
		return clickRates;
	}

	public int getTotalClicks() {
		return totalClicks;
	}

	public int getTotalMinutes() {
		return totalMinutes;
	}

	public List<Integer> getValues() {
		return values;
	}

	public int getMin() {
		return min;
	}

	public int getMax() {
		return max;
	}

	public String getTimeAtMin() {
		return timeAtMin;
	}

	public String getTimeAtMax() {
		return timeAtMax;
	}

	public double getMean() {
		return mean;
	}

	public double getMedian() {
		return median;
	}

	public double getSd() {
		return sd;
	}

}


