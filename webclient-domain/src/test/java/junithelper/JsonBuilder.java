package junithelper;

import java.lang.reflect.Field;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;

/**
 * JSON文字列を生成する
 * 項目追加時にカンマを追加し、無駄なカンマと判定した場合、削除する
 * 改行は項目を追加するときに、先頭に追加する
 * 
 */
public class JsonBuilder {

	private Map<String, Object> jsonMap = new LinkedHashMap<>();
    private Deque<Map<String, Object>> stack = new ArrayDeque<>();
	
	public JsonBuilder() {
		clear();
	}

	public void clear() {
		jsonMap = new LinkedHashMap<>();
	    stack = new ArrayDeque<>();
	    stack.push(jsonMap);
	}

	/**
	 * 値を追加する
	 * 　・改行を追加
	 * 　・インデントを追加
	 * 　・値を追加
	 * 　・カンマを追加する
	 * 　　例）
	 * 　　　"city": "福岡市",
	 * 　　　　↓↓↓
	 * 　　　"city": "福岡市",
	 * 　　　"bantigo": "南福岡",
	 * 
	 * @param field
	 * @param cell
	 * @param level
	 * @return
	 */
	public JsonBuilder appendValue(Field field, Cell cell, int level) {
		
		Map<String,Object> map = stack.peek();
		String value = ExcelUtils.getExcelValue(cell);
		map.put(field.getName(), value);
		return this;
	}
	
	/**
	 * 連想配列をクローズする
	 * 　・最後の1文字を削除
	 * 　・改行を追加
	 * 　・インデントを追加
	 * 　・"},"を追加
	 *  　 例）
	 * 　　　"city": "福岡市",
	 * 　　　"bantigo": "南福岡",
	 * 　　　　↓↓↓
	 * 　　　"city": "福岡市",
	 * 　　　"bantigo": "南福岡"
	 * 　　　},
	 * 
	 * @param jsonMap
	 * @param level
	 * @return
	 */
	public JsonBuilder appendClose(int level) {
		// スタックから取り除く
		stack.pop();
		return this;
	}
	
	public JsonBuilder appendCloseArray(int level) {
//		jsonMap.deleteCharAt(jsonMap.length() - 1);
//		jsonMap.append("],");
		return this;
	}
	
	/**
	 * 連想配列をオープンする
	 * 　・改行を追加
	 * 　・インデントを追加
	 * 　・"{"を追加
	 * 　　例）
	 * 　　　"bantigo": "南福岡"
	 * 　　　},
	 * 　　　　　↓↓↓
	 * 　　　"bantigo": "南福岡"
	 * 　　　},
	 * 　　　{
	 * 
	 * @param level
	 * @return
	 */
//	public JsonBuilder appendOpen(Field field) {
//		Map<String, Object> map = stack.peek();
//		map.put(field.getName(), new LinkedHashMap<>());
//		return this;
//	}
	
	public JsonBuilder appendOpenArray(Field field) {
		Map<String, Object> map = stack.peek();
		map.put(field.getName(), new ArrayList<>());
		return this;
	}
	
	public JsonBuilder appendAssociativeArray(Field field, int level) {
		Map<String, Object> newMap = new LinkedHashMap<>();
		
		Map<String, Object> map = stack.peek();
		if (map == null) {
			System.out.println("debug appendAssociativeArray");
		}
		map.put(field.getName(), newMap);
		stack.push(newMap);
		return this;
	}
	
	/**
	 * 別シートで作成されたJSONは、連想配列形式として完結している状態である
	 * 末尾にカンマが存在しないため、カンマを付与する
	 */
//	public JsonBuilder appendAnotherSheet(String jsonString, int level) {
//
//		Map<String,Object> map = stack.peek();
//		map.put("", map);
//		return this;
//	}
	
	public JsonBuilder appendAnotherSheetMap(String name, String jsonString) {

		Map<String,Object> map = stack.peek();
		map.put(name, new JsonAnotherSheet(jsonString));
		return this;
	}
	
	public String toJson() {
		StringBuilder json = new StringBuilder();
		toJsonMap(this.jsonMap, json, 0);
		return json.toString();
	}
	
	@SuppressWarnings("unchecked")
	private void toJsonMap(Map<String, Object> map, StringBuilder json, int indent) {
		
		int _indent = indent;

		if (json.length() != 0) {
			json.append(kaigyo());
		}
		json.append(headspace(_indent))
			.append("{");

		++_indent;
		
		for(Map.Entry<String, Object> entry : map.entrySet()){
			String key = entry.getKey();
			Object value = entry.getValue();
			
			json.append(kaigyo())
				.append(headspace(_indent))
				.append("\"").append(key).append("\"").append(": ");
			
			if (value instanceof Map) {
				toJsonMap((Map<String, Object>) value, json, _indent);
				
			} else if (value instanceof List) {
				toJsonList((List<Object>) value, json, _indent);
				
			} else if (value instanceof JsonAnotherSheet) {
				json.append(JsonAnotherSheet.class.cast(value).getJsonString());
				
			} else {
				json.append("\"").append(value).append("\"");
				
			}
			json.append(", ");
		}		
		
		--_indent;
		
		json.deleteCharAt(json.length() - 2);
		json.append(kaigyo())
			.append(headspace(_indent))
			.append("}");
	}
	
	@SuppressWarnings("unchecked")
	private void toJsonList(List<Object> list, StringBuilder json, int indent) {
		
		int _indent = indent;
		
		json.append(kaigyo())
			.append(headspace(_indent))
			.append("[");

		++_indent;
		
		for (Object value : list) {
			
			if (value instanceof Map) {
				toJsonMap((Map<String, Object>) value, json, _indent);
				
			} else if (value instanceof List) {
				toJsonList((List<Object>) value, json, _indent);
				
			} else if (value instanceof JsonAnotherSheet) {
				json.append(JsonAnotherSheet.class.cast(value).getJsonString());
				
			} else {
				json.append("\"").append(value).append("\"");
			}
			
			json.append(",").append(kaigyo());
		}
		
		--_indent;

		json.deleteCharAt(json.length() - 2);
		json.append(kaigyo())
			.append(headspace(_indent))
			.append("]");
	}
	

	private String kaigyo() {
		return "\n";
	}
	
	private String headspace(int level) {
		StringBuilder space = new StringBuilder();
		for (int i = 0; i <= level; i++) {
			space.append("  ");
		}
		return space.toString();
	}
	
	private static class JsonAnotherSheet {
		private String jsonString;
		private JsonAnotherSheet(String jsonString) {
			this.jsonString = jsonString;
		}
		private String getJsonString() {
			return this.jsonString;
		}
	}
}
