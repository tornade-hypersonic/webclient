package junithelperv2.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class ExcelSheetWrapper {

	private Sheet sheet;

	ExcelSheetWrapper(Sheet sheet) {
		this.sheet = sheet;
	}
	

	/** Excelに合わせたセルを取得 **/
	Cell getCell(int row, int col) {
		return sheet.getRow(row - 1).getCell(col - 1);
	}

	/** poiに合わせたセルを取得 **/
	Cell getCellPoi(int row, int col) {
		return sheet.getRow(row).getCell(col);
	}

	/** DTOクラス名を取得 **/
	String getDtoClassName() {
		return getCell(2, 3).getStringCellValue();
	}

	/** 試験No行取得 **/
	Row getTestNoRow() {
		return sheet.getRow(ExcelConst.POS_TEST_NO_ROW_JAVA);
	}

	/** 通番行取得 **/
	Row getTubanRow() {
		return sheet.getRow(ExcelConst.POS_TUBAN_ROW_JAVA);
	}

	/** 最終行取得 **/
	int getLastRowNum() {
		return sheet.getLastRowNum();
	}

	/** カラム定義情報を取得するインデックスを返す **/
	int getColumnInfosIndex(int rowCnt) {
		return rowCnt - ExcelConst.POS_DATA_START_ROW_JAVA;
	}

	/** まだ行が存在するか **/
	boolean hasRow(int rowCnt) {
		if (sheet.getRow(rowCnt) == null) {
			return false;
		}
		return true;
	}

	/** まだ列が存在するか **/
	boolean hasCol(int colCnt) {
		if (getTestNoRow().getCell(colCnt) == null) {
			return false;
		}
		return true;
	}


	public String getSheetName() {
		return sheet.getSheetName();
	}


	public Row getRow(int i) {
		return sheet.getRow(i);
	}

//	/** カラム情報取得 **/
//	public List<DtoFieldInfo> getColumnList() {
//
//	    List<DtoFieldInfo> fields = new ArrayList<>();
//	    for (int row = POS_DATA_START_ROW_JAVA; hasRow(row); row++) {
//
//	    	// Noの項目がない場合、読込み終了する
//		    Cell noCell = getCell(row + 1, POS_FIELD_NO_COL);
//		    if (noCell == null) {
//		    	break;
//		    }
//
//		    String fieldName = getCell(row + 1, POS_FIELD_NAME_COL).getStringCellValue();
//		    String fieldClassName = getCell(row + 1, POS_FIELD_CLASS_NAME_COL).getStringCellValue();
//		    int level = (int) ExcelUtils.getExcelValueForDto(
//		    						getCell(row + 1, POS_FIELD_LEVEL_COL), "int");
//
//		    DtoFieldInfo dtoFieldInfo = new DtoFieldInfo();
//		    dtoFieldInfo.setFieldName(fieldName);
//		    dtoFieldInfo.setFieldClassName(fieldClassName);
//		    dtoFieldInfo.setLevel(level);
//		    fields.add(dtoFieldInfo);
//	    }
//	    fields.stream().forEach(s -> System.out.println(s));
//	    return fields;
//	}

}
