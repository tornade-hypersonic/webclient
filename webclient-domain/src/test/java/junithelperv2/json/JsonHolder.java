package junithelperv2.json;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * JSON文字列を保持する
 * 
 */
public class JsonHolder {

	private Map<String, String> jsonMap = new HashMap<>();
	
	private static final String KEY_DELIMITER = "\\v";
	
	public void put(String sheetName, String testNo, String tuban, String json) {
		String key = StringUtils.joinWith(KEY_DELIMITER, sheetName, testNo, tuban);
		jsonMap.put(key, json);
	}
	
	public String get(String sheetName, String testNo, String tuban) {
		String key = StringUtils.joinWith("\\v", sheetName, testNo, tuban);
		return jsonMap.get(key);
	}
	
}
