package Agenda;

public class HourParcel {
	
	boolean occupied;
	AgendaTime begin;
	AgendaTime end;
	
	public HourParcel(AgendaTime begin, AgendaTime end){
		
		this.begin =  begin;
		this.end =  end;
		occupied =  false;
	}
	
	boolean setOccupied()
	{
		if(occupied==false)
		{
			this.occupied = true;
			return true;
		}
		else
		{ 
			System.out.println("Already is Occupied");
			return false;
		}
	}
	
	boolean setFree()
	{
		
		if(this.occupied == true)
		{	
			this.occupied = false;
			return true;
		}
		else
		{
			System.out.println("Already free this parcel");
			return false;
		}
		
		
		
	}
	
	boolean isOccupied()
	{
		return  this.occupied;
		
	}


}


