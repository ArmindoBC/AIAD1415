import org.joda.time.*;

import jade.core.behaviours.*;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
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
	//Vector<Identifier> peopleIHaveEventsWith;
	Vector<DFAgentDescription> peopleOnline;

	class ArrangeAgendaBehaviour extends CyclicBehaviour{
		int nPeopleFound = 0;
		int currentEvent = 0;
		Vector<Proposal> proposals = null;
		Vector<Constraint> constraints = null;

		public ArrangeAgendaBehaviour(Agent a) {
			super(a);
			proposals = new Vector<Proposal>();
			constraints = new Vector<Constraint>();
		}

		@Override
		public void action() {
			ACLMessage msg = blockingReceive();

			//System.out.println("{"+Person.this.name+"}received: " + msg.getContent());

			if(msg.getPerformative() == ACLMessage.INFORM) {

				if(msg.getContent().contains("[GETNAME]")){

					ACLMessage reply = msg.createReply();
					System.out.println("{"+Person.this.name+"}replied:[NAME]{"+Person.this.name+"}");
					reply.setContent("[NAME]{"+Person.this.name+"}");	
					send(reply);
				}
				else if (msg.getContent().contains("[NAME]")){
					//extracts a string inbetween brackets!
					String name = msg.getContent().replaceAll(".*\\{|\\}.*", "");

					System.out.println("{"+Person.this.name+"}processed name:"+name);

					if(Person.this.agenda.haveEventWith(name)){
						Person.this.agenda.addAIDofPersonInEvents(name, msg.getSender());
						nPeopleFound++;

						System.out.println("{"+Person.this.name+"}found a name on his event list.");
					}

					if(nPeopleFound == Person.this.agenda.getPeopleIHaveEventsWith().size()){
						System.out.println("{"+Person.this.name+"}found every name on his event list, starting agenda arrangement.");

						sendProposal();
					}
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

				System.out.println("{"+Person.this.name+"}"+p.toString());

				// -1 - error / 0 - has event with same person (can) / 1 - assigns event proposed / 2 - has event marked 
				int check = Person.this.agenda.checkProposal(p);

				ACLMessage reply = msg.createReply();

				switch(check){
				case -1:
					System.out.println("{"+Person.this.name+"}unexpected error");
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
				String name = msg.getContent().replaceAll(".*\\{|\\}.*", "");

				Proposal p = getProposal(convId);

				if(p != null){
					p.addToAnswered(name);

					Event e = Person.this.agenda.getEvent(name);
					
					if(namesMatch(e.getAttendants(), p.getAnswered())){
						System.out.println("{"+Person.this.name+"}everyone replied positively to event:"+e.getName());
						this.currentEvent++;
						
						if(this.currentEvent == Person.this.agenda.getEvents().size()){
							System.out.println("{"+Person.this.name+"}sucessfully arranged all his/hers events!");
							//takeDown//doDelete ??? - ver caso dos bookbuyers 
						}
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
								//takeDown//doDelete ??? - ver caso dos bookbuyers 	
					}
					else{
						Person.this.agenda.resetHour(currentEvent);
						currentEvent--;
						sendProposal();
					}
				}
			}
		}

		public void sendProposal() {
			String convId = getLocalName() + hashCode() + System.currentTimeMillis()%10000 + "_";

			if(!implementConstraints(currentEvent)){
				System.out.println("{"+Person.this.name+"}has constraints that make "+Person.this.agenda.getEvents().elementAt(currentEvent).getName()+" impossible to arrange!");
			}
			else {
				System.out.println("{"+Person.this.name+"}added constraints to "+Person.this.agenda.getEvents().elementAt(currentEvent).getName());
			}
			
			
			Event event = Person.this.agenda.getEvent(currentEvent);
			
			Vector<String> attendants = event.getAttendants();

			Proposal p = new Proposal(convId, event.getName(), event.getStartHour(), event.getEndHour(), Person.this.name);
			proposals.add(p);

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

			for(int i = 0; i < attendants.size(); i++){
				if(!attendants.elementAt(i).equals(Person.this.name)) {
					eventProposal.addReceiver(Person.this.agenda.getAIDOfPerson(attendants.elementAt(i)));
				}
			}

			eventProposal.setContent(serializedProposal);
			send(eventProposal);
		}	
		
		private boolean implementConstraints(int eventNumber) {
			Vector<Constraint> constraintsOfThisEvent = new Vector<Constraint>();
			Event e = Person.this.agenda.getEvent(eventNumber);
			
			for(int i = 0; i < constraints.size(); i++){
				if(constraints.elementAt(i).getProposal().getEventName().equals(e.getName()) && e.hasAttendant(Person.this.getName())){
					constraintsOfThisEvent.add(constraints.elementAt(i));
				}
			}
			
			if(constraintsOfThisEvent.size() == 0)
				return true;
			
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

		private boolean namesMatch(Vector<String> attendants, Vector<String> answered) {
			
			if(attendants.size() != answered.size())
				return false;
			
			for(int i = 0; i < attendants.size(); i++){
				boolean currentAttHasMatch = false;
				String currentAtt = attendants.elementAt(i);
				
				for(int j = 0; j < answered.size(); j++){	
					if(currentAtt.equals(answered.elementAt(j)))
						currentAttHasMatch = true;
				}
				
				if(!currentAttHasMatch){
					System.out.println("{"+Person.this.name+"}inside namesMatch, vectors of same size but found irregularity");
					return false;
				}
			}
			return true;
			
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

		Vector<String> ce = new Vector<String>(); ce.add("Pedro"); ce.add("Miguel"); ce.add("Jorge");
		Event coolEvent = new Event("Cool Event", new DateTime(2014, 1, 1, 12, 20, 0, 0), new DateTime(2014, 1, 1, 14, 10, 0, 0), new Duration(3600000),  ce);
		Event coolEvent2 = new Event("Cool Event 2", new DateTime(2014, 1, 1, 15, 20, 0, 0), new DateTime(2014, 1, 1, 18, 10, 0, 0), new Duration(3600000), ce);

		Person.this.agenda = new Agenda(Person.this.name);
		Person.this.agenda.addEvent(coolEvent);
		Person.this.agenda.addEvent(coolEvent2);

		System.out.println("{"+Person.this.name+"}has events with "+Person.this.agenda.getPeopleIHaveEventsWith().toString());

		ArrangeAgendaBehaviour aab = new ArrangeAgendaBehaviour(this);
		addBehaviour(aab);

		findPeopleOnline();

		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);

		//
		//Test if agent can find every person he needs to communicate to before start.
		//TICKER AQUIII

		for(int i = 0; i < peopleOnline.size(); i++)
		{
			System.out.println("{"+Person.this.name+"}Added receiver:"+peopleOnline.elementAt(i).getName());

			msg.addReceiver(peopleOnline.elementAt(i).getName());
		}

		System.out.println("{"+Person.this.name+"}sent GETNAME's");

		msg.setContent("{"+Person.this.name+"}[GETNAME]");

		send(msg);

	}


	public void findPeopleOnline() {
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd1 = new ServiceDescription();
		sd1.setType("Person");
		template.addServices(sd1);

		Person.this.peopleOnline = new Vector<DFAgentDescription>();

		try {
			DFAgentDescription[] result = DFService.search(this, template);

			for(int i=0; i<result.length; ++i){
				if(!result[i].getName().equals(this.getName()) && !peopleOnline.contains(result[i])){
					System.out.println("{" + Person.this.name + "} -> AID["+ this.getAID()+"]found agent with AID ["+result[i].getName()+"]");	

					peopleOnline.addElement(result[i]);
				}
			}
		} catch(FIPAException e) { e.printStackTrace(); }
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
