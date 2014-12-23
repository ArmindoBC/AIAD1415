import jade.core.AID;
import java.util.Vector;
import org.joda.time.*;

public class Agenda {
	private 
	String owner;
	Vector<Event> events = null;
	Vector<Identifier> peopleIHaveEventsWith = null;

	public Agenda(String owner){
		this.owner = owner;
		events = new Vector<Event>();
		peopleIHaveEventsWith = new Vector<Identifier>();
	}

	public void resetHour(int eventPos){
		events.elementAt(eventPos).reset();
	}

	public boolean pushHourForward(int eventPos){
		Event e = events.elementAt(eventPos);
		DateTime eStart = e.getStartHour();
		DateTime tempStart = eStart.plus(1800000);
		Duration eDur = e.getDuration();
		DateTime eWindowEnd = e.getWindowEndingHour();

		if(tempStart.plus(eDur).isAfter(eWindowEnd))
			return false;
		else {
			e.pushHourForward();
			return true;
		}

	}

	// -1 - error / 0 - has event with same person (can) / 1 - has an available timespace for the proposal / 2 - has event marked 
	public int checkProposal(Proposal p){

		Event e = getEvent(p.getEventName(), p.getSender());


		if(e != null){
			if(p.getStartHour().isEqual(e.getStartHour()) && p.getEndHour().isEqual(e.getEndHour())){
				System.out.println("{"+owner+"}shares event "+p.getEventName()+" with "+p.getSender());
				return 0;
			}
			else {
				System.out.println("{"+owner+"}has event "+p.getEventName()+" with "+p.getSender()+"but with different hours?");
				return -1;
			}
		}
		else {
			System.out.println("{"+owner+"}found no event with proposal("+p.toString()+".");

			if(checkOverlappingEvents(p)){
				System.out.println("{"+owner+"}has overlapping event with "+p.getEventName());
				return 2;
			}
			else {
				//insertProposal(p);
				System.out.println("{"+owner+"}has available timespace event: "+p.getEventName());
				return 1;
			}			
		}

	}

	public void insertProposal(Proposal p){
		int i;

		if( getEvent(p.getEventName()) == null){

			Event e = new Event(p.getEventName(), p.getStartHour(), p.getEndHour(), p.getSender(), p.getPriority());
			events.add(e);
			System.out.println("{"+owner+"}inserted new event: "+p.getEventName());
		}
		else{
			System.out.println("{"+owner+"}had already that event: "+p.getEventName());
		}
	}


	public boolean checkOverlappingEvents(Proposal p)
	{
		for(int i = 0; i < events.size(); i++){

			Event tempEvent = events.elementAt(i);

			/*
			Let ConditionA Mean DateRange A Completely After DateRange B (True if StartA > EndB)
			Let ConditionB Mean DateRange A Completely Before DateRange B (True if EndA < StartB)
			Then Overlap exists if Neither A Nor B is true ( If one range is neither completely after the other, nor completely before the other, then they must overlap)
			Now deMorgan's law says that:
			Not (A Or B) <=>  Not A And Not B
			Which means (StartA <= EndB)  and  (EndA >= StartB)
			 */

			System.out.println("{"+owner+"}[EVENT "+tempEvent.getName()+"]sH:"+tempEvent.getStartHour()+"|eH:"+tempEvent.getEndHour());
			System.out.println("{"+owner+"}[PROPO "+p.getEventName()+"]sH:"+p.getStartHour()+"|eH:"+p.getEndHour());

			if(p.getPriority() > tempEvent.getPriority()){
				System.out.println("{"+owner+"}was proposed a event with a higher priority than he has at the same time");
				return false;
			}
			else if(p.getPriority() < tempEvent.getPriority()){
				System.out.println("{"+owner+"}was proposed a event with a lower priority than he has at the same time");
				return true;
			}

			Interval eventInterval = new Interval(tempEvent.getStartHour(), tempEvent.getEndHour());
			Interval proposalInterval = new Interval(p.getStartHour(), p.getEndHour());

			if(eventInterval.overlaps(proposalInterval)){
				System.out.println("{"+owner+"} was proposed an event he already has one.");
				return true;
			}

			/*
			if(tempEvent.getStartHour().isBefore(p.getEndHour()) && tempEvent.getEndHour().isAfter(p.getStartHour())){
				System.out.println("{"+owner+"}was proposed a event but he already has one at"+p.getStartHour().toString());
				return true;
			}
			 */

		}
		return false;
	}

	public Vector<Identifier> getPeopleIHaveEventsWith() {
		return peopleIHaveEventsWith;
	}

	public void setPeopleIHaveEventsWith(Vector<Identifier> peopleIHaveEventsWith) {
		this.peopleIHaveEventsWith = peopleIHaveEventsWith;
	}

	public Event getEvent(String name){
		for(int i = 0; i < events.size(); i++){
			if(events.elementAt(i).getName().equals(name))
				return events.elementAt(i);
		}
		return null;
	}

	public Event getEvent(String name, String attendant){
		for(int i = 0; i < events.size(); i++){
			if(events.elementAt(i).getName().equals(name) && events.elementAt(i).hasAttendant(attendant))
				return events.elementAt(i);
		}
		return null;
	}

	public Event getEvent(int i){
		if(i >= 0 && i < events.size())
			return events.elementAt(i);
		else
			return null;
	}

	public Vector<Event> getEvents() {
		return events;
	}

	public void setEvents(Vector<Event> events) {
		this.events = events;
	}

	public AID getAIDOfPerson(String name){
		for(int j = 0; j < peopleIHaveEventsWith.size(); j++)
		{
			Identifier id = peopleIHaveEventsWith.elementAt(j);

			if(id.getName().equals(name))
			{
				return id.getAgentName();
			}
		}

		return null;
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

		for(int i = 0; i < e.getAttendants().size(); i++)
		{
			boolean found = false;

			for(int j = 0; j < peopleIHaveEventsWith.size(); j++)
			{
				if(peopleIHaveEventsWith.elementAt(j).getName().equals(e.getAttendants().elementAt(i).getName())){
					System.out.println(owner+" found "+e.getAttendants().elementAt(i).getName()+" on his peopleEventsWith");
					found = true;
					break;
				}
			}

			if(!found && !e.getAttendants().elementAt(i).getName().equals(owner)){
				Identifier id = new Identifier(e.getAttendants().elementAt(i).getName());
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

	public Vector<Identifier> getIdsOf(Vector<String> names){
		Vector<Identifier> ret = new Vector<Identifier>();

		for(int i = 0; i < peopleIHaveEventsWith.size(); i++){
			for(int j = 0; j < names.size(); j++){
				if(peopleIHaveEventsWith.elementAt(i).getName().equals(names.elementAt(j)))
					ret.add(peopleIHaveEventsWith.elementAt(i));
			}
		}

		if(names.size() != ret.size())
			System.out.println(owner+" error getting Ids of names.");

		return ret;
	}
}
