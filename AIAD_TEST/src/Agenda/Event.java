package Agenda;
import java.util.*;
import org.joda.time.*;

public class Event {
	private
		DateTime windowStartingHour;
		DateTime windowEndingHour;
		Duration duration;
		Map<String, Boolean> attendants;
		String eventName;
		String host;
		
		//list of attendants - pair name/obligation 
		
	public Event(DateTime sH, DateTime eH,Duration duration, Map<String, Boolean> atdnts, String eventName, String host){
		this.windowStartingHour =sH;
		this.windowEndingHour = eH;
		this.attendants = atdnts;
		this.host =  host;
		this.eventName =  eventName;
	
		
		this.duration = duration;
	}

	public DateTime getWindowStartingHour() {
		return windowStartingHour;
	}

	public void setWindowStartingHour(DateTime windowStartingHour) {
		this.windowStartingHour = windowStartingHour;
	}

	public DateTime getWindowEndingHour() {
		return windowEndingHour;
	}

	public void setWindowEndingHour(DateTime windowEndingHour) {
		this.windowEndingHour = windowEndingHour;
	}

	public Duration getDuration() {
		return duration;
	}

	public void setDuration(Duration duration) {
		this.duration = duration;
	}

	public Map<String, Boolean> getAttendants() {
		return attendants;
	}

	public void setAttendants(Map<String, Boolean> attendants) {
		this.attendants = attendants;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	
}





