package junithelperv2.excel;

import java.io.File;
import java.util.Objects;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import junithelperv2.exceldata.ExcelData;
import junithelperv2.exceldata.SheetData;

public class ExcelLoader {

	private static final Logger logger = LoggerFactory.getLogger(ExcelLoader.class);
	
	/** Excel情報読込み **/
	public static ExcelData loadExcelData(String path) {

		// Excel読込み
		Workbook excel;
		try {
			excel = WorkbookFactory.create(new File(path));
		} catch (Exception e) {
			throw new RuntimeException("読込み失敗 path=" + path, e);
		}
		logger.debug(Objects.toString(excel));

		// シート毎にデータを作成し、ExcelDataに登録
		ExcelData excelData = new ExcelData();
		for (int i = 0; i < excel.getNumberOfSheets(); i++) {
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
