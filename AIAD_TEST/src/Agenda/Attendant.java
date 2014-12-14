package Agenda;

public class Attendant {
	String name;
	// 0 - optional / 1 - mandatory
	int priority;
	
	public Attendant(String name, int priority){
		this.name = name;
		this.priority = priority;
	}

	public Attendant(String name){
		this.name = name;
		this.priority = 1;
	}
	
	public String toString(){
		return "["+name+"|"+priority+"]";
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}
}
