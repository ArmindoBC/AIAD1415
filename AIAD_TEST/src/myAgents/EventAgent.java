package myAgents;

import java.util.*;
import org.joda.time.*;

public class EventAgent {
	
	String eventName;
	Vector<String> attendants;
	DateTime beginTime; 
	DateTime endTime;
	Duration duration;

	public EventAgent(String eventName, Vector<String> attendants, DateTime beginTime, DateTime endTime, Duration duration)
	{
		this.attendants = attendants;
		this.beginTime = beginTime;
		this.endTime = endTime;
		this.duration = duration;
		this.eventName = eventName;
		
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public Vector<String> getAttendants() {
		return attendants;
	}

	public void setAttendants(Vector<String> attendants) {
		this.attendants = attendants;
	}

	public DateTime getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(DateTime beginTime) {
		this.beginTime = beginTime;
	}

	public DateTime getEndTime() {
		return endTime;
	}

	public void setEndTime(DateTime endTime) {
		this.endTime = endTime;
	}

	public Duration getDuration() {
		return duration;
	}

	public void setDuration(Duration duration) {
		this.duration = duration;
	}
	
	
	

	
}
