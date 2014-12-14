package myAgents;

import jade.core.AID;

public class PersonOnline {
	AID aid;
	boolean processed;
	boolean sentMsg;
	
	public PersonOnline(AID aid){
		this.aid = aid;
		this.processed = false;
		this.sentMsg = false;
	}
	
	public AID getAid() {
		return aid;
	}
	public void setAid(AID aid) {
		this.aid = aid;
	}
	public boolean isProcessed() {
		return processed;
	}
	public void setProcessed(boolean processed) {
		this.processed = processed;
	}
	
	public boolean wasSentMsg() {
		return sentMsg;
	}

	public void setSentMsg(boolean sentMsg) {
		this.sentMsg = sentMsg;
	}

	public String toString(){
		if(processed && sentMsg)
			return "[AID:"+aid+"|Processed:true|WasSentMsg:true]";
		else if(processed && !sentMsg)
			return "[AID:"+aid+"|Processed:true|WasSentMsg:false]";
		else if(!processed && sentMsg)
			return "[AID:"+aid+"|Processed:false|WasSentMsg:true]";
		else
			return "[AID:"+aid+"|Processed:false|WasSentMsg:false]";
	}
}
