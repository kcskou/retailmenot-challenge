package clickouts.model;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RequestLog implements Iterable<RequestLogEntry>{
	
	private List<RequestLogEntry> entries;
	
	public RequestLog() {
		entries = new ArrayList<>();
	}
	
	public void add(RequestLogEntry entry) {
		entries.add(entry);
	}

	public List<RequestLogEntry> getEntries() {
		return entries;
	}
	
	public int getSize() {
		return entries.size();
	}

	@Override
	public Iterator<RequestLogEntry> iterator() {
		return entries.iterator();
	}

}
