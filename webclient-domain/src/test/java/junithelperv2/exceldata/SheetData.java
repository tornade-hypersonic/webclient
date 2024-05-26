package junithelperv2.exceldata;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class SheetData {

	/**
	 * String[0]：DTOの変数の物理名
	 * String[1]：DTOの変数のクラス名
	 * String[2]：DTOの設定値で別シートを参照するか
	 */
	private List<DtoFieldInfo> dtoFieldInfo;

	/**
	 * Map {
	 *   key  ：試験No,
	 *   value：Map {
	 *     key  ：通番,
	 *     value：データリスト [cells, cells, ...]
	 *   }
	 * }
	 */
//	private Map<String, Map<String, List<List<Cell>>>> dtoDatas;
	private DtoDataTestNo dataTestNo;

	/** クラス名 **/
	private String className;

	/** シート名 **/
	private String sheetName;
	

	public List<DtoFieldInfo> getDtoFieldInfo() {
		return dtoFieldInfo;
	}
	public void setDtoFieldInfo(List<DtoFieldInfo> dtoFieldInfo) {
		this.dtoFieldInfo = dtoFieldInfo;
	}
	//
//	public Map<String, Map<String, List<List<Cell>>>> getDtoDatas() {
//		return dtoDatas;
//	}
//	public void setDtoDatas(Map<String, Map<String, List<List<Cell>>>> dtoDatas) {
//		this.dtoDatas = dtoDatas;
//	}
	public DtoDataTestNo getDtoDatas() {
		return dataTestNo;
	}
	public void setDtoDatas(DtoDataTestNo dataTestNo) {
		this.dataTestNo = dataTestNo;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getSheetName() {
		return sheetName;
	}
	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}


	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
