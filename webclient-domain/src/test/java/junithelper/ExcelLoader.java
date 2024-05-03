package junithelper;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class ExcelLoader {

	/** Excel情報読込み **/
	static Map<String, DtoExcelSheet> loadExcelSheet(String path) {

		Workbook excel;
		try {
			excel = WorkbookFactory.create(new File(path));
		} catch (Exception e) {
			throw new RuntimeException("読込み失敗 path=" + path, e);
		}
		System.out.println(excel);

		int numberOfSheets = excel.getNumberOfSheets();
		System.out.println(numberOfSheets);

		Map<String, DtoExcelSheet> sheetMap = new HashMap<>();
		for (int i = 0; i < numberOfSheets; i++) {
			Sheet sheet = excel.getSheetAt(i);
			String sheetName = sheet.getSheetName();
			DtoExcelSheet dtoExcelSheet = new DtoExcelSheet(sheet);
			if (dtoExcelSheet.isDtoSheet()) {
				sheetMap.put(sheetName, dtoExcelSheet);
			}
		}

		return sheetMap;
	}

}
