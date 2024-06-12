package junithelperv2.exceldata;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class SheetData {

	/** DTO定義情報 **/
	private List<DtoFieldInfo> dtoFieldInfo;

	/** 試験No単位に保持するデータ **/
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
