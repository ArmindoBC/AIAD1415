package myAgents;

import java.awt.BorderLayout;
import java.util.Vector;

import jade.core.Agent;
import jade.core.behaviours.*;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

import javax.swing.*;;

public class MyHelloWorldAgent extends Agent {

/**
* 
*/
	AgentGui myGui = null;
	JFrame myFrame = null;
	
	private static final long serialVersionUID = 1L;

	private Behaviour myBehaviour;
	
	Vector<AgentMessage> receviedMessages = new Vector<AgentMessage>(); 
	
	protected void setup() {
		   
		   	makeGUI();
		   	myBehaviour = new MyBehaviour(this, myGui);
		      String tipo = "";  
		      // regista agente no DF
		      
		      
		      DFAgentDescription dfd = new DFAgentDescription();
		      dfd.setName(getAID());
		      ServiceDescription sd = new ServiceDescription();
		      sd.setName(getName());
		      sd.setType("Agente Ocupado");
		      dfd.addServices(sd);
		      try {
		         DFService.register(this, dfd);
		      } catch(FIPAException e) {
		         e.printStackTrace();
		      }

		      // cria behaviour
		     
		      addBehaviour(myBehaviour);
			  
		      // toma a iniciativa se for agente "pong"
		    

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
	
	
	public Behaviour getBehaviour(){
		
		return this.myBehaviour;
	}
	
	private void makeGUI() {
		JFrame myFrame =  new JFrame();
		   	 myGui  =  new AgentGui(this, myFrame);
		   	 
	        myFrame.setTitle("AIAD" + "@ChatRoom");
	        myFrame.getContentPane().setLayout(new BorderLayout()); 
	        myFrame.getContentPane().add("Center", myGui); 
	        myFrame.getContentPane().add("South", new JLabel("Type \'quit\' to exit or just close the window")); 
	        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        myFrame.pack(); 
	        myFrame.setSize(400, 200); 
	        myFrame.show();
	}
}
