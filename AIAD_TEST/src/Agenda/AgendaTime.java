package Agenda;

public class AgendaTime {
	
	
	
	private int hours; 
	private int minutes;
	
	
	public AgendaTime(int hours, int minutes)
	{
		this.hours = hours;
		this.minutes = minutes;
		
	}
	
	
	@Override
	public String toString() 
	{
		String myTime =  null;
		if(this.hours < 10 )
		myTime = "0" +  this.hours;
		else
		myTime = "" + this.hours; 
		
		if(this.minutes <10 )
		myTime += ":0" + this.minutes;
		else
		myTime += ":" + this.minutes;
			
		return myTime;
	}
	
	
	public AgendaTime addMinutesToAnother(int min)
	{	
		//falta rever o caso das 24 horas por dia mas para os testes funciona
		AgendaTime myNewTime =  new AgendaTime(this.hours, this.minutes); 
	
		if(this.minutes +  min  >= 60 && min < 60) 
		{ 
			int temp  = this.minutes + min;
			myNewTime.minutes = temp - 60; 
			myNewTime.hours++;
			
		}
		else if(min >= 60)
		{ 
				int temp  = min/60;
				myNewTime.hours += temp; 
				
				int temp1  =  min%60;
				
				if(temp1 +  this.minutes >= 60)
		     		{
						
						System.out.println(temp);
						System.out.println(temp1);
						int aux = this.minutes + temp1; 
						System.out.println(aux);
						myNewTime.minutes =  aux - 60; 
						myNewTime.hours++;
						
		     		}
				else
					myNewTime.minutes += temp1;
		}
		else 
		{
			myNewTime.minutes += min;
			
		}
		return myNewTime;
		
	}
	
	public void addMinutes(int min)
	{	
		//falta rever o caso das 24 horas por dia mas para os testes funciona
	
		if(this.minutes +  min  >= 60 && min < 60) 
		{ 
			int temp  = this.minutes + min;
			this.minutes = temp - 60; 
			this.hours++;
			
		}
		else if(min >= 60)
		{ 
				int temp  = min/60;
				this.hours += temp; 
				
				int temp1  =  min%60;
				
				if(temp1 +  this.minutes >= 60)
		     		{
						
						System.out.println(temp);
						System.out.println(temp1);
						int aux = this.minutes + temp1; 
						System.out.println(aux);
						this.minutes =  aux - 60; 
						this.hours++;
						
		     		}
				else
					this.minutes += temp1;
		}
		else 
		{
			this.minutes += min;
			
		}
		
		
	}
	
	public int getHours() {
		return hours;
	}


	public void setHours(int hours) {
		this.hours = hours;
	}


	public int getMinutes() {
		return minutes;
	}


	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}


}
