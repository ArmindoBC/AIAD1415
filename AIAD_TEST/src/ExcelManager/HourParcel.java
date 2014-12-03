package ExcelManager;


public class HourParcel {
	
	boolean occupied;
	AgendaTime begin;
	AgendaTime end;
	
	public HourParcel(AgendaTime begin, AgendaTime end){
		
		this.begin =  begin;
		this.end =  end;
		occupied =  false;
	}
	
	public AgendaTime getBegin()
	{
		return this.begin;
	}
	
	public AgendaTime getEnd()
	{
		return this.end;
	}
	
	public boolean setOccupied()
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
	
	public	boolean setFree()
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
	
	public int isOccupied()
	{
		if(this.occupied==true)
		return 1;
		else
			return 0; 
	}


}


