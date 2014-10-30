package myAgents;

import jade.core.Agent;
import jade.core.behaviours.*;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;


public class myHelloWorldAgent extends Agent {

/**
* 
*/
	private static final long serialVersionUID = 1L;

	private Behaviour myBehaviour = new MyBehaviour(this);
	
	
	   protected void setup() {
		      String tipo = "";
		      // obtém argumentos
		      Object[] args = getArguments();
		      if(args != null && args.length > 0) {
		         tipo = (String) args[0];
		      } else {
		         System.out.println("Não especificou o tipo");
		      }
		      
		      // regista agente no DF
		      DFAgentDescription dfd = new DFAgentDescription();
		      dfd.setName(getAID());
		      ServiceDescription sd = new ServiceDescription();
		      sd.setName(getName());
		      sd.setType("Agente " + tipo);
		      dfd.addServices(sd);
		      try {
		         DFService.register(this, dfd);
		      } catch(FIPAException e) {
		         e.printStackTrace();
		      }

		      // cria behaviour
		     
		      addBehaviour(myBehaviour);
			  
		      // toma a iniciativa se for agente "pong"
		      if(tipo.equals("pong")) {
		         // pesquisa DF por agentes "ping"
		         DFAgentDescription template = new DFAgentDescription();
		         ServiceDescription sd1 = new ServiceDescription();
		         sd1.setType("Agente ping");
		         template.addServices(sd1);
		         try {
		            DFAgentDescription[] result = DFService.search(this, template);
		            // envia mensagem "pong" inicial a todos os agentes "ping"
		            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		            for(int i=0; i<result.length; ++i)
		               msg.addReceiver(result[i].getName());
		            msg.setContent("pong");
		            send(msg);
		         } catch(FIPAException e) { e.printStackTrace(); }
		      }

		   }   // fim do metodo setup

		   // método takeDown
		   protected void takeDown() {
		      // retira registo no DF
		      try {
		         DFService.deregister(this);  
		      } catch(FIPAException e) {
		         e.printStackTrace();
		      }
		   }
	
	
	
	
}
