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
 * JSON連想配列相当をMapで保持、JSON配列相当をリストで保持する
 * toJson()で登録した内容をJSON文字列に変換する
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
	 * 連想配列開始
	 */
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
	 * 配列開始
	 */
	public JsonBuilder appendOpenArray(Field field) {
		Map<String, Object> map = stack.peek();
		map.put(field.getName(), new ArrayList<>());
		return this;
	}
	
	/**
	 * 値を追加する
	 */
	public JsonBuilder appendValue(Field field, Cell cell) {
		return appendValue(field.getName(), cell);
	}
	
	/**
	 * 値を追加する
	 */
	public JsonBuilder appendValue(String name, Cell cell) {
		
		Map<String,Object> map = stack.peek();
		String value = ExcelUtils.getExcelValue(cell);
		map.put(name, value);
		return this;
	}
	
	/**
	 * 連想配列をクローズする
	 */
	public JsonBuilder appendClose(int level) {
		// スタックから取り除く
		stack.pop();
		return this;
	}

	/**
	 * 別シートで生成されたJSONを登録
	 */
	public JsonBuilder appendAnotherSheetMap(String name, String jsonString) {

		Map<String,Object> map = stack.peek();
		map.put(name, new JsonAnotherSheet(jsonString));
		return this;
	}
	
	/**
	 * 登録された情報をJSON文字列へ変換
	 */
	public String toJson() {
		StringBuilder json = new StringBuilder();
		toJsonMap(this.jsonMap, json, 0);
		return json.toString();
	}
	
	/**
	 * MapをJSONへ変換
	 */
	@SuppressWarnings("unchecked")
	private void toJsonMap(Map<String, Object> map, StringBuilder json, int indent) {
		
		int _indent = indent;

		// 最初以外は改行を追加
		if (json.length() != 0) {
			json.append(kaigyo());
		}
		// 連想配列開始を追加
		json.append(headspace(_indent))
			.append("{");

		++_indent;
		
		// 連想配列の値を追加
		for(Map.Entry<String, Object> entry : map.entrySet()){
			String key = entry.getKey();
			Object value = entry.getValue();
			
			// 連想配列のキーを編集
			json.append(kaigyo())
				.append(headspace(_indent))
				.append("\"").append(key).append("\"").append(": ");
			
			// 連想配列の値を編集
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

		// カンマと半角空白を削除
		json.deleteCharAt(json.length() - 2);
		
		// 連想配列のクローズ
		json.append(kaigyo())
			.append(headspace(_indent))
			.append("}");
	}
	
	/**
	 * ListをJSONへ変換
	 */
	@SuppressWarnings("unchecked")
	private void toJsonList(List<Object> list, StringBuilder json, int indent) {
		
		int _indent = indent;
		
		// 改行＋配列開始
		json.append(kaigyo())
			.append(headspace(_indent))
			.append("[");

		++_indent;
		
		// リストの要素を追加
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

		// 末尾のカンマと改行を削除
		json.deleteCharAt(json.length() - 2);
		
		// 改行して閉じる
		json.append(kaigyo())
			.append(headspace(_indent))
			.append("]");
	}
	
	/**
	 * 改行を返却
	 */
	private String kaigyo() {
		return "\n";
	}
	
	/**
	 * 階層に応じたインデントを返却
	 */
	private String headspace(int level) {
		StringBuilder space = new StringBuilder();
		for (int i = 0; i <= level; i++) {
			space.append("  ");
		}
		return space.toString();
	}
	
	/**
	 * 別シートで作成したJSON文字列を保持するクラス
	 * このクラスのみで使用する
	 */
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
