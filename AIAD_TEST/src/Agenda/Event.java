package Agenda;
import java.util.*;
import org.joda.time.*;

public class Event {
	private
		DateTime windowStartingHour;
		DateTime windowEndingHour;
		Duration duration;
		Map<String, String> attendants;
		//list of attendants - pair name/obligation 
		
	public Event(DateTime sH, DateTime eH, Map<String, String> atdnts){
		this.windowStartingHour =sH;
		this.windowEndingHour = eH;
		this.attendants = atdnts;
		
		Interval interval = new Interval(sH, eH);
		this.duration = interval.toDuration();
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

	public Map<String, String> getAttendants() {
		return attendants;
	}

	public void setAttendants(Map<String, String> attendants) {
		this.attendants = attendants;
	}

}





