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
		Vector<Proposal> proposals;

		public ArrangeAgendaBehaviour(Agent a) {
			super(a);
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
				
				//processar mensagens recebidas
				//ver se numero total de pessoas do evento aceitou 
			}
			else if(msg.getPerformative() == ACLMessage.REJECT_PROPOSAL){
				
				System.out.println("{"+Person.this.name+"}received rejected proposal:"+msg.getContent());
				
				//guardar constraint (ONDE E QUE AS VOU CHECKAR DEPOIS ??? - check sempre que fizer backtrack ??? )
				//aumentar window do evento currente senao diminuir 1 do current event, se diminuir para baixo de 0 explode o programa.
			}
		}

		public void sendProposal() {
			String convId = getLocalName() + hashCode() + System.currentTimeMillis()%10000 + "_";
			
			Event event = Person.this.agenda.getEvent(this.currentEvent);
			
			Vector<String> attendants = event.getAttendants();
			
			DateTime eventEnd = event.getWindowStartingHour().plus(event.duration);
			
			Proposal p = new Proposal(convId, event.getName(),event.getWindowStartingHour(), eventEnd, Person.this.name);
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

		/*
		Vector<String> ce = new Vector<String>(); ce.add("Pedro"); ce.add("Miguel"); ce.add("Jorge");
		Event coolEvent = new Event("Cool Event", new DateTime(2014, 1, 1, 12, 20, 0, 0), new DateTime(2014, 1, 1, 14, 10, 0, 0), new Duration(3600000),  ce);
		Event coolEvent2 = new Event("Cool Event 2", new DateTime(2014, 1, 1, 15, 20, 0, 0), new DateTime(2014, 1, 1, 18, 10, 0, 0), new Duration(3600000), ce);

		Person.this.agenda = new Agenda(Person.this.name);
		Person.this.agenda.addEvent(coolEvent);
		Person.this.agenda.addEvent(coolEvent2);
		*/

		//COLOCAR PROCESSAMENTO DO EXECEL AQUI

		System.out.println("{"+Person.this.name+"}has events with "+Person.this.agenda.getPeopleIHaveEventsWith().toString());

		ArrangeAgendaBehaviour aab = new ArrangeAgendaBehaviour(this);
		addBehaviour(aab);

		findPeopleOnline();

		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);

		//
		//Test if agent can find every person he needs to communicate to before start.
		//

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
