import java.io.Serializable;
import java.util.Vector;

import org.joda.time.*;

public class Proposal implements Serializable {

	private static final long serialVersionUID = 7859955352534641955L;
	String conversationId;
	String eventName;
	DateTime startHour;
	DateTime endHour;
	String sender;
	int priority; // 0 - optional , 1 - mandatory
	Vector<String> answered;

	public Proposal(String conversationId, String eventName, DateTime startHour, DateTime endHour, String sender, int priority){
		this.conversationId = conversationId;
		this.eventName = eventName;
		this.sender = sender;
		this.priority = priority;
		this.startHour = startHour;
		this.endHour = endHour;
		this.answered = new Vector<String>();
	}
	

	public String toString()
    {
        return "[PROPOSAL]"+this.conversationId+
        	"|eventName="+this.eventName+ 
            "|startHour="+this.startHour.toString()+
            "|endHour="+this.endHour.toString()+
            "|sender="+this.sender.toString()+
            "|priority="+this.priority+
            "|answered="+this.answered.toString()+
            "]";
    }    
	
	
	public Vector<String> getAnswered() {
		return answered;
	}

	public void setAnswered(Vector<String> answered) {
		this.answered = answered;
	}
	
	public void addToAnswered(String name){
		answered.add(name);
	}
	
	public String getConversationId() {
		return conversationId;
	}

	public void setConversationId(String conversationId) {
		this.conversationId = conversationId;
	}
	
	public String getEventName() {
		return eventName;
	}
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	public DateTime getStartHour() {
		return startHour;
	}
	public void setStartHour(DateTime startHour) {
		this.startHour = startHour;
	}
	public DateTime getEndHour() {
		return endHour;
	}
	public void setEndHour(DateTime endHour) {
		this.endHour = endHour;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	
}
