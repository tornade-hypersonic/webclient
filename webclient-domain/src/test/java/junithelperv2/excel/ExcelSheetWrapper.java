package junithelperv2.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

/**
 * JunitHelper用にExcelSheetをラップしたクラス
 */
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

}
