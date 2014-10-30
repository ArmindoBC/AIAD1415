package Agenda;

public class Day {

	
	int dayNo;
	String weekDay;
	String month;
	int year;
	AgendaTime beginOfDay;
	AgendaTime endOfDay;
	
	
	Schedule daySchedule = new Schedule(this.beginOfDay, this.endOfDay);
	
	public Day(int dayNo,  String month, int year, AgendaTime beginOfDay, AgendaTime endOfDay){
		
		this.dayNo =  dayNo;
		this.month =  month;
		this.year =  year;
		this.beginOfDay = beginOfDay;
		this.endOfDay =  endOfDay;
	
	}
}
