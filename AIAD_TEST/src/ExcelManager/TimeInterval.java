package ExcelManager;

import java.util.Vector;

public class TimeInterval
{
	
	Vector<HourParcel> myHoursParcels = new Vector<HourParcel>(); 
	
	AgendaTime beginOfDay;
	AgendaTime endOfDay;
	private int interval = 30;
	
	public TimeInterval(AgendaTime beginOfDay, AgendaTime endOfDay)
	{
		this.beginOfDay =  beginOfDay;
		this.endOfDay =  endOfDay;

		makeSchedule();
		
	}
	
	
	private void makeSchedule()
	{
		//first parcel in the time
		HourParcel initialParcel =  new HourParcel(beginOfDay, beginOfDay.addMinutesToAnother(this.interval));
	
		myHoursParcels.add(initialParcel);
		
		int i= 0; 
		while(myHoursParcels.get(i).end.getHours() != endOfDay.getHours() 
						|| myHoursParcels.get(i).end.getMinutes() != endOfDay.getMinutes())
		{
			HourParcel myTempParcel = myHoursParcels.get(i); 
			HourParcel myNewParcel = new HourParcel(myTempParcel.end, myTempParcel.end.addMinutesToAnother(this.interval));
			myHoursParcels.add(myNewParcel);
			
			i++;
		}
		
		
	}
	
	public int getInterval() {
		return interval;
	}


	public void setInterval(int interval) {
		this.interval = interval;
	}


	public Vector<HourParcel> getHoursParcels()
	{
		
		return this.myHoursParcels;
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