package junithelperv2.excel;

import java.io.File;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import junithelperv2.exceldata.ExcelData;
import junithelperv2.exceldata.SheetData;

public class ExcelLoader {

	/** Excel情報読込み **/
	public static ExcelData loadExcelData(String path) {

		// Excel読込み
		Workbook excel;
		try {
			excel = WorkbookFactory.create(new File(path));
		} catch (Exception e) {
			throw new RuntimeException("読込み失敗 path=" + path, e);
		}
		System.out.println(excel);

		int numberOfSheets = excel.getNumberOfSheets();
		System.out.println(numberOfSheets);

		// シート毎にデータを作成し、ExcelDataに登録
		ExcelData excelData = new ExcelData();
		for (int i = 0; i < numberOfSheets; i++) {
			Sheet sheet = excel.getSheetAt(i);
			String sheetName = sheet.getSheetName();
			ExcelSheetDataCreater excelSheetDataCreater = new ExcelSheetDataCreater(sheet);
			SheetData sheetData = excelSheetDataCreater.createSheetData();
			
			// シートが有効なもののみ登録する
			if (sheetData != null) {
				excelData.put(sheetName, sheetData);
			}
		}

		return excelData;
	}

}
