package ExcelManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelWriter {
	
	Agenda myAgenda;
	String nameOfFile;
	
	public ExcelWriter(String nameOfFile){
		
		this.nameOfFile =  nameOfFile;
	}
	
	public void printInExcel(){
		
	AgendaTime begin =  new AgendaTime(8,0);
	AgendaTime end =  new AgendaTime(20,0);
	myAgenda =  new Agenda(begin, end);
	myAgenda.addDay();
	myAgenda.addDay();
	
	//codigo usado para escrever no excel
	XSSFWorkbook workbook = new XSSFWorkbook();
    XSSFSheet sheet = workbook.createSheet("Agent Agenda");
 
    Row header = sheet.createRow(0);
	header.createCell(0).setCellValue("Hour");
	header.createCell(1).setCellValue("Activitie");	
    	
    
	for(int i = 0; i < myAgenda.getSchedule().getHoursParcels().size() ; i++)
    	{   
		Row dataRow = sheet.createRow(i+1);
		dataRow.createCell(0).setCellValue(myAgenda.getSchedule().getHoursParcels().get(i).getBegin().toString()+ "-" 
					+ myAgenda.getSchedule().getHoursParcels().get(i).getEnd().toString());
    		
		dataRow.createCell(1).setCellValue(" ");
		dataRow.createCell(2).setCellValue(" ");;
		dataRow.createCell(3).setCellValue(" ");
			
    	}
    
	
	
    try {
        FileOutputStream out =  new FileOutputStream(new File(this.nameOfFile+ ".xlsx"));
        workbook.write(out);
        out.close();
        System.out.println("Excel was created successfully!");
        

         
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }

	}
}
