package clickouts.util;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A wrapper class that helps me write to CSV files.
 */
public class CsvFileWriter {
	
	private FileWriter fw;
	private char delimiter;
	// A list of comma separated values corresponding to a line 
	// in the CSV file.
	private List<String> buffer;

	/**
	 * Constructor
	 * @param fw A FileWriter object for writing data to file.
	 * @param delimiter The delimiter to be inserted between values.
	 */
	public CsvFileWriter(FileWriter fw, char delimiter) {
		this.fw = fw;
		this.delimiter = delimiter;
		buffer = new ArrayList<>();
	}
	
	/**
	 * Adds a value to the buffer.
	 * @param input A value to be written to the buffer. 
	 */
	public void write(String input) {
		buffer.add(input);
	}
	
	/**
	 * Ends the line and write it to file.
	 * @throws IOException
	 */
	public void end() throws IOException {
		StringBuilder sb = new StringBuilder();
		int size = buffer.size();
		// add the delimiter after each value except the last one
		for (int i = 0; i < (size - 1); i++) {
			sb.append(buffer.get(i)).append(delimiter);
		}
		// the last value is not followed by a delimiter 
		sb.append(buffer.get(size - 1)).append("\n");
		fw.write(sb.toString());
		// clear the buffer for the next line
		buffer.clear();
	}
	
	/**
	 * Close the FileWriter.
	 * @throws IOException
	 */
	public void close() throws IOException {
		fw.close();
	}
}
