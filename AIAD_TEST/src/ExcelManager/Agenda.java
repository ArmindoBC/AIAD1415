package ExcelManager;

import java.util.Vector;


public class Agenda {

	
	Vector<Day> myDays =  new Vector<Day>();
	AgendaTime begin;
	AgendaTime end;
	TimeInterval daySchedule;
  	
	public Agenda(AgendaTime begin, AgendaTime end)
	{
		this.begin=  begin;
		this.end =  end;
		Day myFirstDay = new Day(this.begin, this.end); 
		myDays.addElement(myFirstDay);
		
		daySchedule = new TimeInterval(this.begin, this.end);
	}

	public void addDay(){
		
		Day anotherDay = new Day(this.begin, this.end); 
		myDays.add(anotherDay);
		
		
	}

	public void addDayDifferent(AgendaTime begin , AgendaTime end)
	{
		Day anotherDay = new Day(begin, end); 
		myDays.add(anotherDay);
	
	}
	
	public Vector<Day> getDays()
	{
		
		return myDays;
		
	}
	
	public TimeInterval getSchedule()
	{
		
		return this.daySchedule;
	}

}
