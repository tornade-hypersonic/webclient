package junithelperv2;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class DtoAll {

	private Map<String, Map<String, Map<String, Object>>> dtoAllMap;
    
    public DtoAll() {
		dtoAllMap = new LinkedHashMap<>();
    }
    
    public void putDto(String sheetName, String testNo, String tuban, Object dto) {
    	
    	Map<String, Map<String, Object>> testNoMap = dtoAllMap.get(sheetName);
    	if (testNoMap == null) {
    		testNoMap = new LinkedHashMap<>();
    		dtoAllMap.put(sheetName, testNoMap);
    	}
    	
    	Map<String, Object> tubanMap = testNoMap.get(testNo);
    	if (tubanMap == null) {
    		tubanMap = new LinkedHashMap<>();
    		testNoMap.put(testNo, tubanMap);
    	}
    	
    	tubanMap.put(tuban, dto);
    }

    public Object getDto(String sheetName, String testNo, String tuban) {
    	return dtoAllMap.get(sheetName).get(testNo).get(tuban);
    }

    public boolean containsSheet(String sheetName) {
    	return dtoAllMap.containsKey(sheetName);
    }

	public static void copyAnotherSheet(String sheetName, DtoAll dest, DtoAll src) {
		Map<String,Map<String,Object>> map = src.getSheet(sheetName);
		dest.putSheetSimple(sheetName, map);
	}
	
    private void putSheetSimple(String sheetName, Map<String,Map<String,Object>> testNoMap) {
	    dtoAllMap.put(sheetName, testNoMap);
    }

	private Map<String, Map<String, Object>> getSheet(String sheetName) {
		return dtoAllMap.get(sheetName);
	}

    @Override
    public String toString() {
    	return Objects.toString(dtoAllMap);
    }

}
