package testPackage;

import static org.junit.Assert.*;

import org.junit.Test;

import Agenda.*;

public class TimeTest {

	@Test
	public void testAddMinutes() {
		
		AgendaTime myTime= new AgendaTime(2,5);
		
		myTime.addMinutes(10);
		assertEquals("minutes must increase 10",15, myTime.getMinutes());
		myTime.addMinutes(10);
		assertEquals("minutes must increase 10",25, myTime.getMinutes());
		myTime.addMinutes(10);
		assertEquals("minutes must increase 10",35, myTime.getMinutes());
		myTime.addMinutes(10);
		assertEquals("minutes must increase 10",45, myTime.getMinutes());
		myTime.addMinutes(10);
		assertEquals("minutes must increase 10",55, myTime.getMinutes());
		myTime.addMinutes(10);
		assertEquals("minutes must increase 10",5, myTime.getMinutes());
		assertEquals("hours must increase 1",3, myTime.getHours());
		
		myTime.addMinutes(10);
		assertEquals("minutes must increase 10",15, myTime.getMinutes());
		
		myTime.addMinutes(180);
		assertEquals("minutes must increase 10", 15, myTime.getMinutes());
		assertEquals("minutes must increase 10", 6, myTime.getHours());
	
		
		
	}
	
	@Test 
	public void testMakeSchedule()
	{
		AgendaTime myBeginTime = new AgendaTime(8,30);
		AgendaTime myEndTime = new AgendaTime(17,30);
		
		Schedule mySchedule = new Schedule(myBeginTime, myEndTime);
		
		mySchedule.printParcels();
		
		
	}

}
