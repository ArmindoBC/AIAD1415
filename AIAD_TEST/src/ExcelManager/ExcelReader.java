package ExcelManager;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;
import java.util.Vector;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelReader {
		
		Vector<Vector<String>> myExcelCells = new Vector<Vector<String>>();
		



		private static String nameOfFile;
		public ExcelReader(String nameOfFile) 
		{
			ExcelReader.nameOfFile =  nameOfFile;
		}
	
	
	
	//codigo usado para leitura do excel
		public void readSheetWithFormula(String nameOfFile)
		{	
			
			try
			{
				FileInputStream file = new FileInputStream(new File(nameOfFile+".xlsx"));

				//Create Workbook instance holding reference to .xlsx file
				XSSFWorkbook workbook = new XSSFWorkbook(file);

				FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
				
				//Get first/desired sheet from the workbook
				XSSFSheet sheet = workbook.getSheetAt(0);

				//Iterate through each rows one by one
				Iterator<Row> rowIterator = sheet.iterator();
				int row0  = 0; 
				while (rowIterator.hasNext()) 
				{	
					
					Row row = rowIterator.next();
					//For each row, iterate through all the columns
					Iterator<Cell> cellIterator = row.cellIterator();
					
	 
					Vector<String> myTemp = new Vector<String>();
					while (cellIterator.hasNext()) 
					{	
						
						Cell cell = cellIterator.next();
						//If it is formula cell, it will be evaluated otherwise no change will happen
						
						switch (evaluator.evaluateInCell(cell).getCellType()) 
						{
					
						case Cell.CELL_TYPE_NUMERIC:
								System.out.print(cell.getNumericCellValue() + "\t\t");
								break;
							case Cell.CELL_TYPE_STRING:
							{	
								if(row0!=0)
								{	
									myTemp.add(cell.getStringCellValue());
									if(! cell.getStringCellValue().equals(" "))
									{
										
										
									}
									
								}							
								
								
								
							}break;
							case Cell.CELL_TYPE_FORMULA:
								//Not again
								break;
						}
						
					}
					
					if(row0!=0)
						this.myExcelCells.add(myTemp);
					row0++;
				}
				file.close();
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}

		}

		public Vector<Vector<String>> getMyExcelCells() {
			return myExcelCells;
		}


		
}
