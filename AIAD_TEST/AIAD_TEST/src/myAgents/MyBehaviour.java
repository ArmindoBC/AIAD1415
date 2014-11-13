package myAgents;

import javax.swing.JFrame;

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
	private AgentGui myGui = null;
	private int n = 0;
	private String myTempText =null;

	private boolean processComplete = false;
	public MyBehaviour(Agent myAgent,  AgentGui myGui)
	{ 
		this.myAgent =  myAgent;
		this.myGui =  myGui; 
		
	}
   
	
	
	@Override
	 public void action() {
         //ACLMessage msg = myAgent.blockingReceive();
			ACLMessage msg = myAgent.blockingReceive();
         if(msg.getPerformative() == ACLMessage.REQUEST) {
            //System.out.println(" " + myAgent.getLocalName() + ": recebi " + msg.getContent());
            myTempText = msg.getContent();
        	myGui.textarea.append("Recebi " + msg.getContent() + "\n");
            // cria resposta
            ACLMessage reply = msg.createReply();
            // preenche conteúdo da mensagem
            if(msg.getContent().equals("Marcacao de Evento"))
               reply.setContent("Vou ver se posso!");
            else
            	reply.setContent("Vou ver");
            	processComplete = true;
            // envia mensagem
            myAgent.send(reply);
         }
         else if(msg.getPerformative() == ACLMessage.INFORM && !msg.getContent().isEmpty())
         {
        	 ACLMessage reply  =msg.createReply(); 
        	 reply.setContent("oi");
        	 myAgent.send(reply);
        	
        	 
         }
         
      }
	
	@Override
	public boolean done() {
             return processComplete;
          }
	
}
