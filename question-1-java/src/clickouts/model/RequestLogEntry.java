package clickouts.model;
/**
 * Represent a single entry in the HTTP request log from retailmenot.com
 */
public class RequestLogEntry {
	
	private int hour;
	private int minute;
	private String method;
	private String requestURI;
	
	/**
	 * Constructor
	 * @param hour Hour in HH format of the time when the HTTP request was received.
	 * @param minute Minute in MM format of the time when the HTTP request was received.
	 * @param method HTTP request method.
	 * @param requestURI URI of the resource to which the request is applied.
	 */
	public RequestLogEntry(int hour, int minute, String method, String requestURI) {
		this.hour = hour;
		this.minute = minute;
		this.method = method;
		this.requestURI = requestURI;
	}
	
	/**
	 * Get the time in HH:MM format.
	 * @return A string that represents a time in HH:MM format.
	 */
	public String getTimeStamp() {
		StringBuilder sb = new StringBuilder();
		sb.append(hour).append(":").append(minute);
		return sb.toString();
	}

	/** GETTERS **/

	public int getHour() {
		return hour;
	}

	public int getMinute() {
		return minute;
	}

	public String getMethod() {
		return method;
	}

	public String getRequestURI() {
		return requestURI;
	}
	
}
