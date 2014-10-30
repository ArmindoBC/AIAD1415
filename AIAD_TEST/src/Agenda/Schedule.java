package Agenda;

import java.util.Vector;

public class Schedule
{
	
	Vector<HourParcel> myHoursParcels = new Vector<HourParcel>(); 
	
	AgendaTime beginOfDay;
	AgendaTime endOfDay;
	
	public Schedule(AgendaTime beginOfDay, AgendaTime endOfDay)
	{
		this.beginOfDay =  beginOfDay;
		this.endOfDay =  endOfDay;

		makeSchedule();
		
	}
	
	
	private void makeSchedule()
	{
		//first parcel in the time
		HourParcel initialParcel =  new HourParcel(beginOfDay, beginOfDay.addMinutesToAnother(15));
	
		myHoursParcels.add(initialParcel);
		System.out.println( endOfDay.getHours());
		System.out.println( endOfDay.getMinutes());
		int i= 0; 
		while(myHoursParcels.get(i).end.getHours() != endOfDay.getHours() 
						|| myHoursParcels.get(i).end.getMinutes() != endOfDay.getMinutes())
		{
			HourParcel myTempParcel = myHoursParcels.get(i); 
			HourParcel myNewParcel = new HourParcel(myTempParcel.end, myTempParcel.end.addMinutesToAnother(15));
			myHoursParcels.add(myNewParcel);
			
			i++;
		}
		
		
	}
	
	public void printParcels()
	{
		
		for(int i = 0; i <myHoursParcels.size(); i++ )
		{	
			System.out.println("Parcela nº" + i +" :");
			System.out.println(myHoursParcels.get(i).begin.toString());
			System.out.println(myHoursParcels.get(i).end.toString()); 
			System.out.println(myHoursParcels.get(i).isOccupied());
			System.out.println();
		}
		
	}
}