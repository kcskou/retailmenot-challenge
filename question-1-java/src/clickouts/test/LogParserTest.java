package clickouts.test;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.StringReader;
import java.text.ParseException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import clickouts.model.RequestLog;
import clickouts.model.RequestLogEntry;
import clickouts.util.LogParser;

/**
 * JUnit tests for the LogParser class.
 */
public class LogParserTest {
	
	// path to the data file

	private RequestLog log;
	private LogParser parser;
	private String testEntries;
	
	@Before
	public void setUp() throws Exception {
		
		log = new RequestLog();
		parser = new LogParser(log);		
		String testEntry1 = "66.229.50.207 - - [21/Sep/2011:23:59:59 +0000] \"GET /ajax/instant.php?q=A%26 HTTP/1.1\" 200 28 \"http://www.retailmenot.com/view/bmw-online.com\" \"Mozilla/5.0 (iPad; U; CPU OS 3_2_1 like Mac OS X; en-us) AppleWebKit/531.21.10 (KHTML, like Gecko) Version/4.0.4 Mobile/7B405 Safari/531.21.10\"";
		String testEntry2 = "97.29.167.176 - - [22/Sep/2011:00:02:20 +0000] \"POST /ajax/savings.php HTTP/1.1\" 200 69 \"http://www.retailmenot.com/view/landsend.com\" \"Mozilla/5.0 (Windows NT 5.1; rv:6.0.2) Gecko/20100101 Firefox/6.0.2\"";
		String testEntry3 = "108.6.184.41 - - [01/Nov/2011:10:02:20 +0000] \"GET /out/2845912 HTTP/1.1\" 302 20 \"http://www.retailmenot.com/view/partycity.com\" \"Mozilla/5.0 (Macintosh; Intel Mac OS X 10.7; rv:2.0.1) Gecko/20100101 Firefox/4.0.1\"";
		StringBuilder sb = new StringBuilder();
		sb.append(testEntry1).append("\n");
		sb.append(testEntry2).append("\n");
		sb.append(testEntry3);
		testEntries = sb.toString();
	}

	@Test
	public void testParser() throws ParseException {
		
		BufferedReader input;
		List<RequestLogEntry> entries;
		
		// parse the test entries
		input = new BufferedReader(new StringReader(testEntries));
		parser.parse(input);
		
		// check if the three entries were added
		entries = log.getEntries();
		assertTrue(entries.size() == 3);
		
		// check if first entry is correctly parsed
		RequestLogEntry firstEntry = entries.get(0);
		assertEquals(23, firstEntry.getHour());
		assertEquals(59, firstEntry.getMinute());		
		assertEquals("GET", firstEntry.getMethod());
		assertEquals("/ajax/instant.php?q=A%26", firstEntry.getRequestURI());
		
		// check if second entry is correctly parsed
		RequestLogEntry secondEntry = entries.get(1);
		assertEquals(0, secondEntry.getHour());
		assertEquals(2, secondEntry.getMinute());		
		assertEquals("POST", secondEntry.getMethod());
		assertEquals("/ajax/savings.php", secondEntry.getRequestURI());
		
		// check if third entry is correctly parsed
		RequestLogEntry thirdEntry = entries.get(2);
		assertEquals(10, thirdEntry.getHour());
		assertEquals(2, thirdEntry.getMinute());		
		assertEquals("GET", thirdEntry.getMethod());
		assertEquals("/out/2845912", thirdEntry.getRequestURI());
		
	}
	
}
