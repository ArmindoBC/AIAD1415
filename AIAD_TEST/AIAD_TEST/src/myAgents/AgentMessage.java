package myAgents;

import Agenda.*;
public class AgentMessage {
	
	String agentName;
	int type; //1 pedido de marca��o; 2 resposta
	AgendaTime beginTime;
	AgendaTime duration; 
	
	public AgentMessage(String agentName, int type, AgendaTime beginTime, AgendaTime duration)
	{
		
		this.agentName = agentName;
		this.type =  type; 
		this.beginTime =  beginTime;
		this.duration =  duration;
	
	}
}
