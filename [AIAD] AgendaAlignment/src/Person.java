import org.joda.time.DateTime;

import jade.core.behaviours.*;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.DFService;
import jade.domain.FIPAException;

import java.util.Vector;

public class Person extends Agent {
	private
	String name;
	Agenda agenda;
	//Vector<Identifier> peopleIHaveEventsWith;
	Vector<DFAgentDescription> peopleOnline;


	class FindPeopleBehaviour extends SimpleBehaviour {
		private 
		int nPeopleFound = 0;

		// construtor do behaviour
		public FindPeopleBehaviour(Agent a) {
			super(a);

			Vector<String> ce = new Vector<String>(); ce.add("Pedro"); ce.add("Miguel"); ce.add("Jorge");
			Event coolEvent = new Event("Cool Event", new DateTime(2014, 1, 1, 12, 20, 0, 0), new DateTime(2014, 1, 1, 14, 10, 0, 0), ce);
			Event coolEvent2 = new Event("Cool Event 2", new DateTime(2014, 1, 1, 15, 20, 0, 0), new DateTime(2014, 1, 1, 18, 10, 0, 0), ce);

			Person.this.agenda = new Agenda();
			Person.this.agenda.addEvent(coolEvent);
			Person.this.agenda.addEvent(coolEvent2);
		}

		public void action() {

			ACLMessage msg = blockingReceive();

			System.out.println("{"+Person.this.name+"}received: " + msg.getContent());

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
				}
			}
		}

		@Override
		public boolean done() {
			//if(nPeopleFound == Person.this.agenda.getPeopleIHaveEventsWith().size())
			if(nPeopleFound == 2){
				System.out.println("{"+Person.this.name+"}Found two companions.");
				//I now have everyone, will start event processing!
				return true;
			}
				
			else
				return false;
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

		FindPeopleBehaviour fpb = new FindPeopleBehaviour(this);
		addBehaviour(fpb);

		/*
		ArrangeAgendaBehaviour aab = new ArrangeAgendaBehaviour(this);
		addBehaviour(aab);
		 */

		//Ticker here???
		findPeopleOnline();

		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		for(int i = 0; i < peopleOnline.size(); i++)
		{
			System.out.println("{"+Person.this.name+"}Added receiver:"+peopleOnline.elementAt(i).getName());

			msg.addReceiver(peopleOnline.elementAt(i).getName());
		}

		System.out.println("{"+Person.this.name+"}sent GETNAME's");

		msg.setContent("[GETNAME]"+"{"+Person.this.name+"}");

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
				if(!result[i].getName().equals(this.getName())){
					System.out.println("{" + Person.this.name + "} -> AID["+ this.getAID()+"]");
					System.out.println("found agent with AID ["+result[i].getName()+"]");	

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
