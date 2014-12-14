package myAgents;

public class Constraint {
	Proposal proposal;
	Identifier id;
	
	public Constraint(Proposal proposal, Identifier id){
		this.proposal = proposal;
		this.id = id;
	}
	
	public Proposal getProposal() {
		return proposal;
	}
	public void setP(Proposal proposal) {
		this.proposal = proposal;
	}
	public Identifier getId() {
		return id;
	}
	public void setId(Identifier id) {
		this.id = id;
	}
}
