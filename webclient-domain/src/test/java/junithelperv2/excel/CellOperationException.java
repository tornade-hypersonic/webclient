package junithelperv2.excel;

import org.apache.poi.ss.usermodel.Cell;

public class CellOperationException extends RuntimeException {

	public CellOperationException(String msg, Cell cell, String cellValue, Throwable e) {
		super(getExceptionMessage(msg, cell, cellValue), e);
	}

	public CellOperationException(String msg, Cell cell, String cellValue) {
		super(getExceptionMessage(msg, cell, cellValue));
	}

	/** 例外時のメッセージ **/
	private static String getExceptionMessage(String msg, Cell cell, String cellValue) {

		String val = cellValue;
		if (val == null) {
			val = cell.toString();
		}
		return String.format("%s（%s列, %s行）cellvalue=[%s]",
				msg,
				cell.getAddress().getColumn() + 1,
				cell.getAddress().getRow() + 1,
				val);
	}
}
