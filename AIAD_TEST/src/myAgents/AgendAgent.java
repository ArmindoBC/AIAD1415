package myAgents;

import java.util.Vector;

import jade.core.Agent;
import jade.core.behaviours.*;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

import org.apache.poi.hssf.record.cf.FontFormatting;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.ConditionalFormattingRule;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.SheetConditionalFormatting;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import Agenda.*;
import ExcelManager.*;



public class AgendAgent extends Agent {

/**
* 
*/
	private static final long serialVersionUID = 1L;

	private Behaviour myBehaviour;
	//ExcelReader myReader =  new ExcelReader("formulaDemo");
	ExcelWriter myWriter = new ExcelWriter("formulaDemo");
	String agentExcelFile; 
	EventAgenda myAgenda; 
	AgentGUI myAgentGUI;

	protected void setup() {
				
		
			
			myWriter.printInExcel();	
		
		      // regista agente no DF
		 
		      
		      myAgentGUI = new AgentGUI(this);
		      System.out.println(getName()); 
		      myAgentGUI.renderGUI();
		    
		      // cria behaviour
		     
		      dfaRegistation();
		      myBehaviour = new AgentBehaviour(this);
		      addBehaviour(myBehaviour);


		   }



	private void dfaRegistation() {
		DFAgentDescription dfd = new DFAgentDescription();
		  dfd.setName(getAID());
		  ServiceDescription sd = new ServiceDescription();
		  sd.setName(getName());
		  sd.setType("Agente");
		  dfd.addServices(sd);
		  try {
		     DFService.register(this, dfd);
		  } catch(FIPAException e) {
		     e.printStackTrace();
		  }
		  
	}



		   // método takeDown
		   protected void takeDown() {
		      // retira registo no DF
		    myAgentGUI.setVisible(false);
		    
			   try {
		         DFService.deregister(this);  
		      } catch(FIPAException e) {
		         e.printStackTrace();
		      }
		   		}
	
	
	public Behaviour getBehaviour(){
		
		return this.myBehaviour;
	}
	
	public void sendMsg(EventAgent event)
	{ // toma a iniciativa se for agente "pong"
	     
	          // pesquisa DF por agentes "ping"
	          DFAgentDescription template = new DFAgentDescription();
	          ServiceDescription sd1 = new ServiceDescription();
	          sd1.setType("Agente");
	          template.addServices(sd1);
	          try {
	             DFAgentDescription[] result = DFService.search(this, template);
	             // envia mensagem "pong" inicial a todos os agentes "ping"
	             ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
	             for(int i=0; i<result.length; ++i)
	             {
	            	int j= 0;
	            	
	             	for(; j<event.getAttendants().size(); j++){
	            	if(result[i].getName().getLocalName().equals(event.getAttendants().get(j)))
	                  msg.addReceiver(result[i].getName());
	            	 }
	            	
	             }
	             msg.setContent(event.getEventName());
	             
	             send(msg);
	          }	             
	          catch(FIPAException e) {
	        	  
	        	  
	        	  e.printStackTrace();
	          
	          
	          }
	       
		
	}
	
	public  DFAgentDescription[]  getNetworkAgents()
	{
		DFAgentDescription[] result= null;
		// toma a iniciativa se for agente 
	      DFAgentDescription template = new DFAgentDescription();
        ServiceDescription sd1 = new ServiceDescription();
        sd1.setType("Agente");
        template.addServices(sd1);
        
        try {
           result = DFService.search(this, template);
           // envia mensagem "pong" inicial a todos os agentes "ping"
           
        } catch(FIPAException e) { e.printStackTrace(); }
		return  result;
	}
	
	
	
}
