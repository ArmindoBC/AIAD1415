import jade.core.AID;

import java.util.Vector;
//import org.joda.time.*;

public class Agenda {
	private 
	Vector<Event> events = null;
	Vector<Identifier> peopleIHaveEventsWith = null;

	public Agenda(){
		events = new Vector<Event>();
		peopleIHaveEventsWith = new Vector<Identifier>();
	}

	public Vector<Identifier> getPeopleIHaveEventsWith() {
		return peopleIHaveEventsWith;
	}

	public void setPeopleIHaveEventsWith(Vector<Identifier> peopleIHaveEventsWith) {
		this.peopleIHaveEventsWith = peopleIHaveEventsWith;
	}

	public Vector<Event> getEvents() {
		return events;
	}
	
	public void setEvents(Vector<Event> events) {
		this.events = events;
	}
	
	public void addAIDofPersonInEvents(String name, AID aid){
		for(int j = 0; j < peopleIHaveEventsWith.size(); j++)
		{
			Identifier id = peopleIHaveEventsWith.elementAt(j);
			
			if(id.getName().equals(name))
			{
				id.setAgentName(aid);
				
				System.out.println("Associated AID:["+aid+"] with name ["+name+"]");
				
				return;
			}
		}
		System.out.println("Failed to associate AID:["+aid+"] with name ["+name+"]");
	}
	
	public boolean haveEventWith(String name){
		for(int j = 0; j < peopleIHaveEventsWith.size(); j++)
		{
			if(peopleIHaveEventsWith.elementAt(j).getName().equals(name))
				return true;
		}
		return false;
	}
	
	public boolean haveEventWith(AID aid){
		for(int j = 0; j < peopleIHaveEventsWith.size(); j++)
		{
			if(peopleIHaveEventsWith.elementAt(j).getAgentName().equals(aid))
				return true;
		}
		return false;
	}

	public void addEvent(Event e) {
		events.add(e);

		if(peopleIHaveEventsWith.size() > 0){
			for(int i = 0; i < e.attendants.size(); i++)
			{
				boolean found = false;
				
				for(int j = 0; j < peopleIHaveEventsWith.size(); j++)
				{
					if(peopleIHaveEventsWith.elementAt(j).getName().equals(e.attendants.elementAt(i))){
						found = true;
						break;
					}
				}
				
				if(!found){
					Identifier id = new Identifier(e.attendants.elementAt(i));
					peopleIHaveEventsWith.add(id);
				}
			}
		}
		else{
			for(int i = 0; i < e.attendants.size(); i++) {
				Identifier id = new Identifier(e.attendants.elementAt(i));
				peopleIHaveEventsWith.add(id);
			}
		}
	}


	public String eventsToString() {
		String toReturn = new String();

		for(int i = 0; i < events.size(); i++) {
			toReturn += events.elementAt(i) + "\n";
		}

		return toReturn;
	}
}
