package myAgents;



import jade.core.*;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
/*import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.DFService;
import jade.domain.FIPAException;
*/
public class AgentBehaviour extends SimpleBehaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Agent myAgent =null;
	private int n = 0;
	private String myTempText =null;

	
	public AgentBehaviour(Agent myAgent)
	{ 
		this.myAgent =  myAgent;
		
		
	}
	

	
	
	@Override
	 public void action() {
         
		
		
		
		//ACLMessage msg = myAgent.blockingReceive();
			ACLMessage msg = myAgent.blockingReceive();
			System.out.println(msg.getContent());
			if(msg.getPerformative() == ACLMessage.INFORM) 
			{
            
            myTempText = msg.getContent();	
        	
     
			}
         
			
			
			;
			
      }
	
	@Override
	public boolean done() {
             return false;
          }
	
}
