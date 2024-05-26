package junithelperv2.exceldata;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class DtoDataTestNo {

	/**
	 * Map {
	 *   key  ：試験No,
	 *   value：Map {
	 *     key  ：通番,
	 *     value：データリスト [cells, cells, ...]
	 *   }
	 * }
	 */
	private Map<String, DtoDataTuban> dtoDataTestNoMap = new LinkedHashMap<>();
	
	public void put(String testNo, DtoDataTuban dtoDataTuban) {
		dtoDataTestNoMap.put(testNo, dtoDataTuban);
	}
	
	public DtoDataTuban get(String testNo) {
		return dtoDataTestNoMap.get(testNo);
	}
	
	public Set<Entry<String,DtoDataTuban>> entrySet() {
		return dtoDataTestNoMap.entrySet();
	}
}
