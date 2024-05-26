package junithelperv2.exceldata;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;

public class DtoDataTuban {

	/**
	 * Map {
	 *   key  ：試験No,
	 *   value：Map {
	 *     key  ：通番,
	 *     value：データリスト [cells, cells, ...]
	 *   }
	 * }
	 */
	private Map<String, List<List<Cell>>> dtoDataTubanMap  = new LinkedHashMap<>();
	
	public void put(String tuban, List<List<Cell>> tubanList) {
		dtoDataTubanMap.put(tuban, tubanList);
	}

	public List<List<Cell>> get(String tuban) {
		return dtoDataTubanMap.get(tuban);
	}
	
	public Set<Entry<String, List<List<Cell>>>> entrySet() {
		return dtoDataTubanMap.entrySet();
	}
}
