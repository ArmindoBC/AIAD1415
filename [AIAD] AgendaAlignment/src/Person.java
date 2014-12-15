import org.joda.time.*;

import jade.core.AID;
import jade.core.behaviours.*;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.DFService;
import jade.domain.FIPAException;

import java.io.*;
import java.util.Vector;

public class Person extends Agent {
	private
	String name;
	Agenda agenda;
	Vector<PersonOnline> peopleOnline;
	int eventsOwned;

	class ArrangeAgendaBehaviour extends CyclicBehaviour{
		boolean foundEveryone = false;
		int currentEvent = 0;
		Vector<Proposal> proposals = null;
		Vector<Constraint> constraints = null;

		public ArrangeAgendaBehaviour(Agent a) {
			super(a);
			proposals = new Vector<Proposal>();
			constraints = new Vector<Constraint>();

			Vector<Event>events = Person.this.agenda.getEvents();

			for(int i = 0; i < events.size(); i++){
				if(events.elementAt(i).hasNoAttendants())
					currentEvent++;
			}


		}

		@Override
		public void action() {
			ACLMessage msg = blockingReceive();

			//System.out.println("{"+Person.this.name+"}received: " + msg.getContent());

			if(msg.getPerformative() == ACLMessage.FAILURE){
				System.out.println("{"+Person.this.name+"}received a failure message, this person is terminating");
				Person.this.doDelete();
			}
			else if(msg.getPerformative() == ACLMessage.CONFIRM){				
				String convId = msg.getContent().replaceAll(".*\\(|\\).*", "");
				String pname = msg.getContent().replaceAll(".*\\{|\\}.*", "");

				System.out.println("{"+Person.this.name+"}received confirmation for proposal with convId:"+convId+" from "+pname);

				Proposal p = getProposal(convId);

				Person.this.agenda.insertProposal(p);
			}
			else if(msg.getPerformative() == ACLMessage.INFORM) {

				if(msg.getContent().contains("[GETNAME]")){

					ACLMessage reply = msg.createReply();
					System.out.println("{"+Person.this.name+"}replied:[NAME]{"+Person.this.name+"} to "+msg.getContent());
					reply.setContent("[NAME]{"+Person.this.name+"}");	
					send(reply);
				}
				else if (msg.getContent().contains("[NAME]")){
					//extracts a string inbetween brackets!
					String name = msg.getContent().replaceAll(".*\\{|\\}.*", "");

					System.out.println("{"+Person.this.name+"}processed name:"+name);

					if(Person.this.agenda.haveEventWith(name)){
						Person.this.agenda.addAIDofPersonInEvents(name, msg.getSender());
						setSenderAsProcessed(msg.getSender());
						System.out.println("{"+Person.this.name+"}set "+name+" as processed");
					}
				}

				if(match(Person.this.agenda.getPeopleIHaveEventsWith(), peopleOnline) && !foundEveryone){
					foundEveryone = true;
					System.out.println("{"+Person.this.name+"}found every name on his event list, starting agenda arrangement.");
					sendProposal();
				}
				else if(!foundEveryone){
					System.out.println("{"+Person.this.name+"} still needs to find more people.");

					findPeopleOnline();
				}
			}
			else if(msg.getPerformative() == ACLMessage.PROPOSE){

				Proposal p = null;

				try {
					byte b[] = msg.getByteSequenceContent(); 
					ByteArrayInputStream bi = new ByteArrayInputStream(b);
					ObjectInputStream si = new ObjectInputStream(bi);
					p = (Proposal) si.readObject();
				} catch (Exception e) {
					System.out.println(e);
				}

				proposals.add(p);

				System.out.println("{"+Person.this.name+"}"+p.toString());

				// -1 - error / 0 - has event with same person (can) / 1 - assigns event proposed / 2 - has event marked 
				int check = Person.this.agenda.checkProposal(p);

				ACLMessage reply = msg.createReply();

				switch(check){
				case -1:
					System.out.println("{"+Person.this.name+"}unexpected error");
					sendTerminateMessage();
					Person.this.doDelete();
					break;
				case 0:
					reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
					reply.setContent("{"+Person.this.name+"}[ACCEPT]("+p.conversationId+")");
					send(reply);
					break;
				case 1:
					reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
					reply.setContent("{"+Person.this.name+"}[ACCEPT]("+p.conversationId+")");
					send(reply);
					break;
				case 2:
					reply.setPerformative(ACLMessage.REJECT_PROPOSAL);
					reply.setContent("{"+Person.this.name+"}[REFUSE]("+p.conversationId+")");
					send(reply);
					break;
				}
			}
			else if(msg.getPerformative() == ACLMessage.ACCEPT_PROPOSAL){

				System.out.println("{"+Person.this.name+"}received accepted proposal:"+msg.getContent());

				String convId = msg.getContent().replaceAll(".*\\(|\\).*", "");
				String pname = msg.getContent().replaceAll(".*\\{|\\}.*", "");

				Proposal p = getProposal(convId);
				String eventName = getNameOfEventWithConvId(convId);

				if(eventName.equals(null)){
					System.out.println("{"+Person.this.name+"}couldn't find event to match proposal!");
					sendTerminateMessage();
					Person.this.doDelete();
				}

				System.out.println("{"+Person.this.name+"}searching for event with name:"+eventName);
				Event e = Person.this.agenda.getEvent(eventName);

				if(p != null && e != null){
					p.addToAnswered(pname);

					System.out.println("{"+Person.this.name+"}is analysing this proposal:"+p.toString());
					System.out.println("{"+Person.this.name+"}against this event:"+e.toString());

					if(namesMatch(e.getAttendants(), p.getAnswered())){
						System.out.println("{"+Person.this.name+"}everyone replied positively to event:"+e.getName()+"|currEvent is:"+currentEvent+" (adding one)");
						currentEvent++;
						sendConfirmation(p);
						if(!testIfAllProposed())
							sendProposal();
					}
				}
				else{
					System.out.println("{"+Person.this.name+"}something went wrong, could not find proposal to match id:"+convId);
				}
			}
			else if(msg.getPerformative() == ACLMessage.REJECT_PROPOSAL){

				System.out.println("{"+Person.this.name+"}received rejected proposal:"+msg.getContent());

				String convId = msg.getContent().replaceAll(".*\\(|\\).*", "");
				String name = msg.getContent().replaceAll(".*\\{|\\}.*", "");

				Identifier id = new Identifier(name, msg.getSender());
				Proposal p = getProposal(convId);
				Constraint c = new Constraint(p, id);

				constraints.addElement(c);

				if(Person.this.agenda.pushHourForward(currentEvent)){
					sendProposal();
				}
				else{
					if(currentEvent - 1 < 0){
						System.out.println("{"+Person.this.name+"}couldn't arrange his agenda!");
						sendTerminateMessage();
						Person.this.doDelete();
					}
					else{
						Person.this.agenda.resetHour(currentEvent);
						currentEvent--;
						sendProposal();
					}
				}
			}


			testIfAllProposed();
		}

		public boolean testIfAllProposed() {
			System.out.println("{"+Person.this.name+"}is on currentEvent:"+currentEvent+" and owns "+Person.this.eventsOwned+" events.");
			
			if(this.currentEvent >= Person.this.eventsOwned){
				System.out.println("{"+Person.this.name+"}proposed all his events.");
				Vector<Event> myEvents = Person.this.agenda.getEvents();
				for(int i = 0; i < myEvents.size(); i++){
					System.out.println("{"+Person.this.name+"}[EVENT"+i+"]"+myEvents.elementAt(i).toString());
				}
				return true;
			}
			else
				return false;
		}

		public void sendConfirmation(Proposal p) {

			System.out.println("{"+Person.this.name+"}sent confirmation for proposal of:"+p.getEventName());

			Vector<Identifier> recepients = Person.this.agenda.getIdsOf(p.getAnswered()); 

			ACLMessage eventConfirmation = new ACLMessage(ACLMessage.CONFIRM);
			eventConfirmation.setConversationId(p.getConversationId());
			eventConfirmation.setContent("{"+Person.this.name+"}[CONFIRM]("+p.conversationId+")");

			for(int i = 0; i < recepients.size(); i++){
				eventConfirmation.addReceiver(recepients.elementAt(i).getAgentName());
			}

			send(eventConfirmation);

		}

		public boolean match(Vector<Identifier> peopleIHaveEventsWith, Vector<PersonOnline> peopleOnline) {

			System.out.println("{"+Person.this.name+"}has "+peopleIHaveEventsWith.size()+" he has events with and "+peopleOnline.size()+" people online");

			if(peopleIHaveEventsWith.size() > peopleOnline.size()){
				System.out.println("{"+Person.this.name+"}has less people online than supposed.");
				return false;
			}

			for(int i = 0; i < peopleIHaveEventsWith.size(); i++){

				boolean currentIsProcessed = false;
				boolean currentIsOnMyEvents = false;
				Identifier currentId = peopleIHaveEventsWith.elementAt(i);

				System.out.println("{"+Person.this.name+"}testing "+currentId.toString());

				for(int j = 0; j < peopleOnline.size(); j++){

					PersonOnline currentPo = peopleOnline.elementAt(j);

					System.out.println("|vs|"+currentPo.toString());

					AID aname = currentId.getAgentName();
					AID aid = currentPo.getAid();

					if(aname == null)
						return false;

					if(currentId.getAgentName().equals(aid)){

						currentIsOnMyEvents = true;

						if(peopleOnline.elementAt(j).isProcessed()){
							System.out.println("{"+Person.this.name+"}found "+currentId.toString()+" online and processed!");
							currentIsProcessed = true;
						}
					}
				}

				if(!currentIsProcessed && currentIsOnMyEvents)
					return false;
			}

			return true;
		}

		public void setSenderAsProcessed(AID sender) {
			for(int i = 0; i < peopleOnline.size(); i++){
				if(peopleOnline.elementAt(i).getAid().equals(sender)){
					System.out.println("{"+Person.this.name+"} set sender with AID:"+sender+" as processed.");
					peopleOnline.elementAt(i).setProcessed(true);
				}
			}
		}	

		public String getNameOfEventWithConvId(String convId) {
			for(int i = 0; i < proposals.size(); i++){
				if(proposals.elementAt(i).getConversationId().equals(convId))
					return proposals.elementAt(i).getEventName();
			}
			return null;
		}

		public void sendTerminateMessage() {
			ACLMessage msg = new ACLMessage(ACLMessage.FAILURE);
			for(int i = 0; i < Person.this.peopleOnline.size(); i++){
				System.out.println("{"+Person.this.name+"}Added receiver:"+peopleOnline.elementAt(i).getAid());
				msg.addReceiver(peopleOnline.elementAt(i).getAid());
			}

			System.out.println("{"+Person.this.name+"}sent FAILURE's");
			msg.setContent("{"+Person.this.name+"}[FAILURE]");
			send(msg);

		}

		public void sendProposal() {

			if(testIfAllProposed())
				return;
			
			String convId = getLocalName() + hashCode() + System.currentTimeMillis()%10000 + "_";

			if(!implementConstraints(currentEvent)){
				System.out.println("{"+Person.this.name+"}has constraints that make "+Person.this.agenda.getEvents().elementAt(currentEvent).getName()+" impossible to arrange!");
			}

			Event event = Person.this.agenda.getEvent(currentEvent);
			Vector<Attendant> attendants = event.getAttendants();

			for(int i = 0; i < attendants.size(); i++){
				if(!attendants.elementAt(i).equals(Person.this.name)) {

					int priority = event.getPriorityOfAttendant(attendants.elementAt(i).getName());

					if(priority == -1){
						System.out.println("{"+Person.this.name+"}attendant "+attendants.elementAt(i).getName()+" has no priority defined!");
					}

					Proposal p = new Proposal(convId, event.getName(), event.getStartHour(), event.getEndHour(), Person.this.name, priority);
					proposals.add(p);

					System.out.println("{"+Person.this.name+"}[SENT PROPOSAL]"+p.toString());

					String serializedProposal = null;

					// serialize the object
					try {
						ByteArrayOutputStream bo = new ByteArrayOutputStream();
						ObjectOutputStream so = new ObjectOutputStream(bo);
						so.writeObject(p);
						so.flush();
						serializedProposal = bo.toString();
					} catch (Exception e) {
						System.out.println(e);
					}


					ACLMessage eventProposal = new ACLMessage(ACLMessage.PROPOSE);
					eventProposal.setConversationId(convId);

					eventProposal.addReceiver(Person.this.agenda.getAIDOfPerson(attendants.elementAt(i).getName()));

					eventProposal.setContent(serializedProposal);

					send(eventProposal);
				}
			}			
		}	

		public boolean implementConstraints(int eventNumber) {
			Vector<Constraint> constraintsOfThisEvent = new Vector<Constraint>();

			System.out.println("{"+Person.this.name+"}trying to constrain event number:"+eventNumber);

			Event e = Person.this.agenda.getEvent(eventNumber);

			if(e == null)
				return true;

			for(int i = 0; i < constraints.size(); i++){
				if(constraints.elementAt(i).getProposal().getEventName().equals(e.getName()) && e.hasAttendant(Person.this.getName())){
					constraintsOfThisEvent.add(constraints.elementAt(i));
				}
			}

			if(constraintsOfThisEvent.size() == 0){
				System.out.println("{"+Person.this.name+"}no constraints to implement on event:"+e.getName());
				return true;
			}
			else {
				System.out.println("{"+Person.this.name+"}added constraints to "+Person.this.agenda.getEvents().elementAt(currentEvent).getName());
			}

			DateTime firstAvailableHour = null;

			for(int j = 0; j < constraintsOfThisEvent.size(); j++){
				if(j == 0)
					firstAvailableHour = constraintsOfThisEvent.elementAt(j).getProposal().getStartHour();

				if(constraintsOfThisEvent.elementAt(j).getProposal().getStartHour().isAfter(firstAvailableHour)){
					firstAvailableHour = constraintsOfThisEvent.elementAt(j).getProposal().getStartHour();
				}
			}

			if(firstAvailableHour.isAfter(e.getWindowEndingHour())){
				return false;
			}
			else {
				e.setStartHour(firstAvailableHour);
			}

			return true;
		}

		public boolean namesMatch(Vector<Attendant> attendants, Vector<String> answered) {

			int matchesToFind = answered.size();

			for(int i = 0; i < attendants.size(); i++){
				String currentAtt = attendants.elementAt(i).getName();

				for(int j = 0; j < answered.size(); j++){	
					if(currentAtt.equals(answered.elementAt(j)) && !currentAtt.equals(Person.this.name))
						matchesToFind--;
				}
			}

			if(matchesToFind == 0){
				System.out.println("{"+Person.this.name+"}sucessfully matched his currentEvent to people who answered.");
				return true;
			}
			else{
				System.out.println("{"+Person.this.name+"}didnt match his currentEvent to people who answered.");
				return false;
			}
		}

		public Proposal getProposal(String convId) {
			for(int i = 0; i < proposals.size(); i++){
				if(proposals.elementAt(i).getConversationId().equals(convId))
					return proposals.elementAt(i);
			}
			return null;
		}
	}

	// método setup -------------------------------------------------------------------------------
	protected void setup() {
		Object[] args = getArguments();
		if(args != null && args.length > 0) {
			this.name = (String) args[0];
			System.out.println("{"+Person.this.name+"}Arguments: " + args.toString());
		} else {
			System.out.println("{"+Person.this.name+"}I don't have arguments.");
		}

		// regista agente no DF
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setName(getName());
		//System.out.println("I am " + this.name + " and this is my getName(): " + getName());
		sd.setType("Person");
		dfd.addServices(sd);

		try {
			DFService.register(this, dfd);
		} catch(FIPAException e) {
			e.printStackTrace();
		}

		Person.this.agenda = new Agenda(Person.this.name);

		if(Person.this.name.equals("Pedro")){
			Vector<Attendant> ce = new Vector<Attendant>(); ce.add(new Attendant("Miguel", 1));
			Event ss = new Event("Study Session", new DateTime(2014, 11, 10, 8, 0, 0, 0), new DateTime(2014, 11, 10, 12, 0, 0, 0), new Duration(7200000),  ce, 1);
			Person.this.agenda.addEvent(ss);
		}
		else if(Person.this.name.equals("Miguel")){
			Vector<Attendant> ce = new Vector<Attendant>();
			Vector<Attendant> ecatt = new Vector<Attendant>(); ecatt.add(new Attendant("Jorge", 1));
			Event ma = new Event("Medical Appointment", new DateTime(2014, 11, 10, 8, 30, 0, 0), new DateTime(2014, 11, 10, 10, 30, 0, 0), new Duration(3600000),  ce, 1);
			Event ec = new Event("English Class", new DateTime(2014, 11, 10, 16, 0, 0, 0), new DateTime(2014, 11, 10, 17, 0, 0, 0), new Duration(3600000),  ecatt, 1);
			Person.this.agenda.addEvent(ma);
			Person.this.agenda.addEvent(ec);
		}
		else if(Person.this.name.equals("Jorge")){
			Vector<Attendant> ecatt = new Vector<Attendant>(); ecatt.add(new Attendant("Miguel", 1));
			Vector<Attendant> datt = new Vector<Attendant>(); datt.add(new Attendant("Pedro", 1)); datt.add(new Attendant("Miguel", 1));
			Event ec = new Event("English Class", new DateTime(2014, 11, 10, 16, 0, 0, 0), new DateTime(2014, 11, 10, 17, 0, 0, 0), new Duration(3600000),  ecatt, 1);
			Event d = new Event("Dinner", new DateTime(2014, 11, 10, 18, 30, 0, 0), new DateTime(2014, 11, 10, 23, 30, 0, 0), new Duration(7200000),  datt, 1);
			Person.this.agenda.addEvent(ec);
			Person.this.agenda.addEvent(d);
		}
		
		Person.this.eventsOwned = Person.this.agenda.getEvents().size();
		
		System.out.println("{"+Person.this.name+"}has events with "+Person.this.agenda.getPeopleIHaveEventsWith().toString());

		ArrangeAgendaBehaviour aab = new ArrangeAgendaBehaviour(this);
		addBehaviour(aab);

		Person.this.peopleOnline = new Vector<PersonOnline>();

		findPeopleOnline();
	}

	public void findPeopleOnline() {
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd1 = new ServiceDescription();
		sd1.setType("Person");
		template.addServices(sd1);

		try {
			DFAgentDescription[] result = DFService.search(this, template);

			for(int i=0; i<result.length; ++i){

				boolean alreadyOnline = false;

				for(int j=0; j<peopleOnline.size();j++){
					if(peopleOnline.elementAt(j).getAid().equals(result[i].getName())){
						System.out.println("{" + Person.this.name + "}found some1 already online ["+result[i].getName()+"]");
						alreadyOnline = true;
						break;
					}
				}

				if(!result[i].getName().equals(this.getName()) && !alreadyOnline){

					System.out.println("{" + Person.this.name + "}found agent with AID ["+result[i].getName()+"] and added him to his peopleOnline.");	

					PersonOnline po = new PersonOnline(result[i].getName());
					peopleOnline.addElement(po);
				}
			}
		} catch(FIPAException e) { e.printStackTrace(); }

		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);

		boolean addedAtLeastOneReceiver = false;

		for(int i = 0; i < peopleOnline.size(); i++)
		{

			if(!peopleOnline.elementAt(i).wasSentMsg()){
				System.out.println("{"+Person.this.name+"}Found some1 new. Added receiver:"+peopleOnline.elementAt(i).getAid());
				peopleOnline.elementAt(i).setSentMsg(true);
				msg.addReceiver(peopleOnline.elementAt(i).getAid());
				addedAtLeastOneReceiver = true;
			}
		}

		if(addedAtLeastOneReceiver){
			System.out.println("{"+Person.this.name+"}sent GETNAME's");
			msg.setContent("{"+Person.this.name+"}[GETNAME]");
			send(msg);
		}
	}

	protected void takeDown() {
		System.out.println("Agent "+getLocalName()+": terminating");
		// retira registo no DF
		try {
			DFService.deregister(this);  
		} catch(FIPAException e) {
			e.printStackTrace();
		}
		this.doDelete();
	}
} 
