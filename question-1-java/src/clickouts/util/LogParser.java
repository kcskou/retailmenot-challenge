package clickouts.util;

import java.io.BufferedReader;

import clickouts.model.RequestLog;
import clickouts.model.RequestLogEntry;

/**
 * A Parser for getting information relevant to the calculation of 
 * click out rates from the Http access log file provided by
 * RetailMeNot, Inc.
 */
public class LogParser {
	
	// An object to which parsed data will be saved
	private RequestLog log;
	
	/**
	 * Constructor
	 * @param log The object to which entries will be added.
	 */
	public LogParser(RequestLog log) {
		this.log = log;
	}
	
	/**
	 * Parse each line of the sample log
	 * @param br A BufferedReader that is ready to read the log.
	 */
	public void parse(BufferedReader br) {
		
		System.out.println("Parsing web log...");
		
		try {
			for (String line = br.readLine(); line != null; line = br.readLine()) {
				parseEntry(line);
			}
			System.out.println("Retrieved " + log.getSize() + " entries.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Parse a single log entry of the sample log
	 * @param input A single line from the sample log. 
	 */
	private void parseEntry(String input) {
		
		// focus on the relevant parts
		String[] parts = input.split("\"", 3);
		String dateTimePart = parts[0];
		String methodURIPart = parts[1];
		
		// parse hour and minute
		String[] dateTimeSubparts = dateTimePart.split(":");
		int hour = Integer.parseInt(dateTimeSubparts[1]);
		int minute = Integer.parseInt(dateTimeSubparts[2]);
		
		// parse method and URI
		String[] methodURISubparts = methodURIPart.split("\\s");
		String method = methodURISubparts[0];
		String uri = methodURISubparts[1];
		
		// add entry to log
		log.add(new RequestLogEntry(hour, minute, method, uri));
	}

}
