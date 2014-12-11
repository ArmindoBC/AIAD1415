import java.util.Vector;

import org.joda.time.*;

public class Event {
	private
		String name;
		DateTime windowStartingHour;
		DateTime windowEndingHour;
		Duration duration;
		Vector<String> attendants;
		//list of attendants - pair name/obligation 
		
	public Event(String name, DateTime wsh, DateTime weh, Vector<String> atdnts){
		this.name = name;
		this.windowStartingHour = wsh;
		this.windowEndingHour = weh;
		this.attendants = atdnts;
		
		Interval interval = new Interval(wsh, weh);
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
	
	public String toString(){
		String toReturn = new String();
		
		toReturn = "Event name: " + this.name  + " // Can start at: " + this.windowStartingHour + " // Can end at: "  + this.windowEndingHour + "//";
		
		return toReturn;
	}
	
	public Vector<String> getAttendants() {
		return attendants;
	}

	public void setAttendants(Vector<String> attendants) {
		this.attendants = attendants;
	}
}
