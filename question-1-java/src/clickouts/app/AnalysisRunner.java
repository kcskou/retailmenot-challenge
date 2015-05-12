package clickouts.app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import clickouts.model.RequestLog;
import clickouts.model.RequestLogEntry;
import clickouts.util.MetricsGetter;
import clickouts.util.LogParser;
import clickouts.util.CsvFileWriter;

public class AnalysisRunner {
	
	private final static String WEBLOGFILEPATH = "input/rmn_weblog_sample.log";
	private final static String OUTPUTPATH = "output/click_out_per_minute_stats.csv";
	private static RequestLog log = new RequestLog();
	private static Map<String, Integer> clickOutRates = new HashMap<>();
	
	/**
	 * Run the analysis application
	 */
	public static void main(String[] args) {
		
		// parse the web traffic log
		parseWebLog();
		
		// filter for GET requests with URI that matches /out/{couponId}
		computeClickOutRates();
		
		// compute all metrics using a MetricsGetter object
		MetricsGetter mg = new MetricsGetter(clickOutRates);
		
		// print results to console
		mg.report();
		
		// save to CSV file
		saveAsCsvFile(mg);
		
	}
	
	/**
	 * Parse the HTTP access log sample from retailmenot.com and
	 * add entries to the log field of this class.
	 */
	private static void parseWebLog() {
		try (BufferedReader br = new BufferedReader(new FileReader(WEBLOGFILEPATH))) {
			LogParser parser = new LogParser(log);
			parser.parse(br);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Calculate click out per minutes and save results to
	 * the clickOutRates field of this class.
	 * We can hash on string representation of hour:minute because
	 * this is only one hour worth of data, otherwise we have to hash
	 * on the date as well
	 */
	private static void computeClickOutRates() {
		
		System.out.println("Filtering and grouping entries by minute...");
		
		for (RequestLogEntry e : log) {
			if (e.getMethod().equals("GET") && 
					(e.getRequestURI().matches("/out/\\w+"))) {
				addToMap(e);
			}
		}
	}
	
	/**
	 * Calculate number of click outs per minute by hashing on 
	 * the string representation of time (HH:MM). We can hash on
	 * HH:MM only because this is one hour worth of data, 
	 * otherwise we may hash on the date as well.
	 * @param e A log entry to be mapped to the corresponding HH:MM.
	 */
	private static void addToMap(RequestLogEntry e) {
		String timeStamp = e.getTimeStamp();
		// check if timestamp is already keyed
		if (clickOutRates.containsKey(timeStamp)) {
			// increment click out count
			clickOutRates.put(timeStamp, clickOutRates.get(timeStamp) + 1);			
		} else {
			clickOutRates.put(timeStamp, 1);
		}
	}
	
	/**
	 * Save metrics about click outs per minute to a CSV file. 
	 * @param mg A MetricGetter object that computes the descriptive statistics
	 *           of click outs per minute.
	 */
	private static void saveAsCsvFile(MetricsGetter mg) {
		
		boolean fileExists = new File(OUTPUTPATH).exists();
		
		try {
			CsvFileWriter csvOutput = 
					new CsvFileWriter(new FileWriter(OUTPUTPATH, true), ',');
			// write the header if this is a new file
			if (!fileExists) {
				csvOutput.write("minimum_click_outs_per_minute");
				csvOutput.write("time_at_minimum");
				csvOutput.write("maximum_click_outs_per_minute");
				csvOutput.write("time_at_maximum");
				csvOutput.write("mean_click_outs_per_minute");
				csvOutput.write("median_click_outs_per_minute");
				csvOutput.write("standard_deviation_click_outs_per_minute");
				csvOutput.end();
			}
			// write the metrics
			csvOutput.write(Integer.toString(mg.getMin()));
			csvOutput.write(mg.getTimeAtMin());
			csvOutput.write(Integer.toString(mg.getMax()));
			csvOutput.write(mg.getTimeAtMax());
			csvOutput.write(Double.toString(mg.getMean()));
			csvOutput.write(Double.toString(mg.getMedian()));
			csvOutput.write(Double.toString(mg.getSd()));
			csvOutput.end();
			
			// close the writer
			csvOutput.close();
			
			System.out.println("Successfully saved results to \"" + OUTPUTPATH + "\".");
		} catch (IOException e) {
			e.printStackTrace();
		}			
		
	}
}
