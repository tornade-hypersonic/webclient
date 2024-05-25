package junithelperv2;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.poi.ss.usermodel.Cell;

public class DtoInfo {


	public List<DtoFieldInfo> getDtoFieldInfo() {
		return dtoFieldInfo;
	}
	public void setDtoFieldInfo(List<DtoFieldInfo> dtoFieldInfo) {
		this.dtoFieldInfo = dtoFieldInfo;
	}
	public Map<String, Map<String, List<List<Cell>>>> getDtoDatas() {
		return dtoDatas;
	}
	public void setDtoDatas(Map<String, Map<String, List<List<Cell>>>> dtoDatas) {
		this.dtoDatas = dtoDatas;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}


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
	private Map<String, Map<String, List<List<Cell>>>> dtoDatas;

	/** クラス名 **/
	private String className;

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
