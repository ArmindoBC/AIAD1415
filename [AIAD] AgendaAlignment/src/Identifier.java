import jade.core.AID;

public class Identifier {
	private 
	String name;
	AID agentName;
	
	public Identifier(String name){
		this.name = name;
		this.agentName = null;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public AID getAgentName() {
		return agentName;
	}
	public void setAgentName(AID agentName) {
		this.agentName = agentName;
	}
}
