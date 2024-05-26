package junithelperv2.excel;

import java.io.File;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import junithelperv2.exceldata.ExcelData;
import junithelperv2.exceldata.SheetData;

public class ExcelLoader2 {

	/** Excel情報読込み **/
	public static ExcelData loadExcelData(String path) {

		Workbook excel;
		try {
			excel = WorkbookFactory.create(new File(path));
		} catch (Exception e) {
			throw new RuntimeException("読込み失敗 path=" + path, e);
		}
		System.out.println(excel);

		int numberOfSheets = excel.getNumberOfSheets();
		System.out.println(numberOfSheets);
		

//		Map<String, DtoExcelSheet> sheetMap = new HashMap<>();
		ExcelData excelData = new ExcelData();
		for (int i = 0; i < numberOfSheets; i++) {
			Sheet sheet = excel.getSheetAt(i);
			String sheetName = sheet.getSheetName();
//			DtoExcelSheet dtoExcelSheet = new DtoExcelSheet(sheet);
			ExcelSheetDataCreater excelSheetDataCreater = new ExcelSheetDataCreater(sheet);
			SheetData sheetData = excelSheetDataCreater.createSheetData();
			
//			if (sheetData.isDtoSheet()) {
//				sheetMap.put(sheetName, dtoExcelSheet);
//			}
			if (sheetData != null) {
				excelData.put(sheetName, sheetData);
			}
		}

		return excelData;
	}

}
