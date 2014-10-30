package myAgents;

import jade.core.*;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
/*import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.DFService;
import jade.domain.FIPAException;
*/
public class MyBehaviour extends SimpleBehaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Agent myAgent =null;
	 private int n = 0;
	public MyBehaviour(Agent myAgent)
	{ 
		this.myAgent =  myAgent;
		
	}
   
	
	
	@Override
	 public void action() {
         ACLMessage msg = myAgent.blockingReceive();
         if(msg.getPerformative() == ACLMessage.INFORM) {
            System.out.println(++n + " " + myAgent.getLocalName() + ": recebi " + msg.getContent());
            // cria resposta
            ACLMessage reply = msg.createReply();
            // preenche conteúdo da mensagem
            if(msg.getContent().equals("ping"))
               reply.setContent("pong");
            else reply.setContent("ping");
            // envia mensagem
            myAgent.send(reply);
         }
         
         
         
      }
	
	@Override
	public boolean done() {
             return n==10;
          }
	
}
