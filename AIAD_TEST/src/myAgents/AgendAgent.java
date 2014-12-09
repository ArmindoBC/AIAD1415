package myAgents;

import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.joda.time.DateTime;
import org.joda.time.Duration;

import jade.core.Agent;
import jade.core.behaviours.*;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import java.util.HashMap;

import java.util.Iterator;

import Agenda.*;
import ExcelManager.*;



public class AgendAgent extends Agent {

/**
* 
*/
	private static final long serialVersionUID = 1L;

	private Behaviour myBehaviour;
	
	
	String agentExcelFile; 
	EventAgenda myAgenda; 
	AgentGUI myAgentGUI;
	

	protected void setup() {
				
		
			
				      // regista agente no DF
		 
		      
		      myAgentGUI = new AgentGUI(this);
		      //System.out.println(getName()); 
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
	
	public void sendMsg(Event event)
	{      
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
	
	
	public Vector<Event> setEventVector(Vector<Vector<String>> excelRows)
	{
		Vector<Event> eventVector =  new Vector<Event>();
		for(int i = 0;  i< excelRows.size(); i++)
		{
			if(excelRows.get(i).size() == 6)
			{
			//String parcelaTemp =  excelRows.get(i).get(0);
			String nameTemp = excelRows.get(i).get(1);
			String estadoTemp = excelRows.get(i).get(2);
			String duracaoTemp = excelRows.get(i).get(3);
			String participantesTemp = excelRows.get(i).get(4);
			String assistentesTemp = excelRows.get(i).get(5);
			
			int j = i +1;
			String hourFinal = "";
			String hourBegin= "";
			while(j<excelRows.size() &&
					 excelRows.get(j).size()==6 &&
					((nameTemp.equalsIgnoreCase(excelRows.get(j).get(1))) && 
					(estadoTemp.equalsIgnoreCase(excelRows.get(j).get(2))) &&
					(duracaoTemp.equalsIgnoreCase(excelRows.get(j).get(3))) &&
					(participantesTemp.equalsIgnoreCase(excelRows.get(j).get(4))) &&
					(assistentesTemp.equalsIgnoreCase(excelRows.get(j).get(5)))
					))
		{
			
		System.out.println("i  " + i);
			System.out.println("cona " + j );	

		hourBegin =  excelRows.get(i).get(0);
		hourFinal =  excelRows.get(j).get(0);
		nameTemp = excelRows.get(j).get(1);
	    estadoTemp = excelRows.get(j).get(2);
		duracaoTemp = excelRows.get(j).get(3);
		participantesTemp = excelRows.get(j).get(4);
		assistentesTemp = excelRows.get(j).get(5);
		
		j++;
		
		}
			//criar o evento
		

		int beginHour =  Character.getNumericValue(hourBegin.charAt(0));
		int beginHour1 =  Character.getNumericValue(hourBegin.charAt(1));
		int beginMin =  Character.getNumericValue(hourBegin.charAt(3));
		int beginMin1 =  Character.getNumericValue(hourBegin.charAt(4));
		
		DateTime begin =  new DateTime(1993, 10, 8, (beginHour*10 + beginHour1), (beginMin*10 + beginMin1));
		
		int endHour =  Character.getNumericValue(hourFinal.charAt(6));
		int endHour1 =  Character.getNumericValue(hourFinal.charAt(7));
		int endMin =  Character.getNumericValue(hourFinal.charAt(9));
		int endMin1 =  Character.getNumericValue(hourFinal.charAt(10));
		
		DateTime end =  new DateTime(1993, 10, 8, (endHour*10 + endHour1), (endMin*10 + endMin1));
		
		int duration =  Character.getNumericValue(duracaoTemp.charAt(0));
		int duration1 =  Character.getNumericValue(duracaoTemp.charAt(1));
		int duration2 =  Character.getNumericValue(duracaoTemp.charAt(3));
		int duration3 =  Character.getNumericValue(duracaoTemp.charAt(4));
		
		DateTime startTime =  new DateTime(1993, 10, 8, 0, 0);
		DateTime endTime =  new DateTime(1993, 10, 8, (duration*10 + duration1), (duration2*10 + duration3));
		Duration myDuration =  new Duration(startTime, endTime);
		//adicionar o evento ao vetor de eventos
		
		
		Duration compDuration =  new Duration(begin, end);
		System.out.println(compDuration.toString() + "         " + myDuration.toString());
		
		
		if(compDuration.isShorterThan(myDuration))
			throw new IllegalArgumentException("O intervalo é menor que a duracao.");
		
		Map<String , Boolean> atdnts = new HashMap<String, Boolean>();

		String[] atendants = participantesTemp.split(",");
		for(int h = 0; h<atendants.length; h++)
		{
			atdnts.put(atendants[h], true);
		}
		String[] viewers =  assistentesTemp.split(",");   
		for(int g = 0; g<viewers.length; g++)
		{
			atdnts.put(viewers[g], false);
		}
		Event eventTemp  = new Event(begin, end, myDuration,atdnts, nameTemp, this.getLocalName());
		eventVector.add(eventTemp);	
		i=j-1;
		}
			
		}
		
		for(int i =0; i<eventVector.size();i++)
		{
			System.out.println(eventVector.get(i).getAttendants().toString());
			System.out.println(eventVector.get(i).getDuration().toString());
			System.out.println(eventVector.get(i).getHost());
			System.out.println(eventVector.get(i).getWindowStartingHour().toString());
			System.out.println(eventVector.get(i).getWindowEndingHour().toString());
			System.out.println(eventVector.get(i).getEventName());
		}
		return eventVector;
	}
	
	/*
	public String eventToString(Event evt)
	{
	
		Map<String, Boolean> attendants = evt.getAttendants();
		String name = evt.getEventName();
		String host = evt.getHost();
		DateTime startTime = evt.getWindowStartingHour();
		DateTime endTime =  evt.getWindowEndingHour();
		Duration duration = evt.getDuration();
		
		String event = "{[" + name + "][" + host + "],";
		
		String myAttendants = "[";
		String priority = "[";
		
		Integer stHour = startTime.getHourOfDay();
		Integer stMin =  startTime.getMinuteOfHour();
		Integer edHour = endTime.getHourOfDay();
		Integer edMin = endTime.getMinuteOfHour();
		Integer du =(int) duration.getMillis();
		
		String stTime =  stHour.toString();
		String stTimeMin =  stMin.toString();
		String edTime =  edHour.toString();
		String edTimeMin  =  edMin.toString(); 
		String dur =  du.toString(); 
		
		String time = "[" + stTime+ ":" + stTimeMin + ","+ edTime+":"+ edTimeMin + ","+ dur +"]";
		
		Iterator<Map.Entry<String, Boolean>> iterator = attendants.entrySet().iterator();
		while(iterator.hasNext()){
		   Map.Entry<String, Boolean> entry = iterator.next();
		   //System.out.printf("Key : %s and Value: %s %n", entry.getKey(), entry.getValue());
		   if(iterator.hasNext())
		   { 
			   myAttendants +=  entry.getKey() + ",";
		   	   priority += entry.getValue() + ",";
		   }	
		   else
		   {  
			   myAttendants += entry.getKey() + "]";
			   priority += entry.getValue().toString() + "]";
		   }
		   iterator.remove(); 
		   }	
		
		
		event += time+ myAttendants + priority + "}";
		System.out.println(event);
		return event;
	}*/
	
		
}
