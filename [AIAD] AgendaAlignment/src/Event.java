import java.util.Vector;

import org.joda.time.*;

public class Event {
	private
		String name;
		DateTime windowStartingHour;
		DateTime windowEndingHour;
		DateTime startHour;
		DateTime endHour;
		Duration duration;
		int priority; // 0 - optional , 1 - mandatory
		Vector<Attendant> attendants;
		
	public Event(String name, DateTime wsh, DateTime weh, Duration dur, Vector<Attendant> atdnts, int priority){
		this.name = name;
		this.windowStartingHour = wsh;
		this.windowEndingHour = weh;
		this.duration = dur;
		this.attendants = atdnts;
		this.priority = priority;
		this.startHour = wsh;
		this.endHour = this.startHour.plus(dur);
	}
	
	public Event(String name, DateTime sh, DateTime eh, String proposer, int priority){
		this.name = name;
		this.windowStartingHour = sh;
		this.windowEndingHour = eh;
		this.startHour = sh;
		this.endHour = eh;
		this.duration = new Duration(sh, eh);
		this.priority = priority;
		this.attendants = new Vector<Attendant>();
		Attendant sender = new Attendant(proposer);
		attendants.addElement(sender);
	}
	
	public void reset(){
		startHour = windowStartingHour;
		endHour = startHour.plus(duration);
	}
	
	public void pushHourForward(){
		startHour = startHour.plus(duration);
		endHour = startHour.plus(duration);
	}
	
	public boolean hasAttendant(String name){
		for(int i = 0; i < attendants.size(); i++){
			if(attendants.elementAt(i).getName().equals(name))
				return true;;
		}
		return false;
	}
	
	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

		
	public DateTime getStartHour() {
		return startHour;
	}

	public void setStartHour(DateTime startHour) {
		this.startHour = startHour;
		endHour = startHour.plus(duration);
	}

	public DateTime getEndHour() {
		return endHour;
	}

	public void setEndHour(DateTime endingHour) {
		this.endHour = endingHour;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
		
		toReturn= "|Event name:"+this.name
				+ "|WSH:"+ this.windowStartingHour
				+ "|WEH:"+ this.windowEndingHour
				+ "|SH:"+this.startHour
				+ "|EH:"+this.endHour
				+ "|Dur:"+this.duration
				+ "|Prio:"+this.priority
				+ "|Atds:"+attendantsToString();
		
		return toReturn;
	}
	
	public String attendantsToString(){
		String ret = "[";
		
		for(int i = 0; i < attendants.size(); i++){
			ret += attendants.elementAt(i).toString();
		}
		ret += "]";
		return ret;
	}
	
	public Vector<Attendant> getAttendants() {
		return attendants;
	}

	public void setAttendants(Vector<Attendant> attendants) {
		this.attendants = attendants;
	}

	public int getPriorityOfAttendant(String name) {
		for(int i = 0; i < attendants.size(); i++){
			if(attendants.elementAt(i).getName().equals(name))
				return attendants.elementAt(i).getPriority();
		}
		return -1;
	}
}
