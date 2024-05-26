package junithelperv2.exceldata;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ExcelData {

	private Map<String, SheetData> excelDataMap = new HashMap<>();
	
	public void put(String sheetName, SheetData sheetData) {
		excelDataMap.put(sheetName, sheetData);
	}
	
	public Set<String> getSheetKeySet() {
		return excelDataMap.keySet();
	}

	public SheetData getSheetData(String sheetName) {
		return excelDataMap.get(sheetName);
	}
}
