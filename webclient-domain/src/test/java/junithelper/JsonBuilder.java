package junithelper;

import java.io.Serializable;
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

	private Map<String, Object> jsonMap;
    private Deque<Map<String, Object>> mapStack;
    private Deque<List<Object>> listStack;
	
    // コンストラクタ
	public JsonBuilder() {
		clear();
	}

	// 初期化
	public void clear() {
		jsonMap = new LinkedHashMap<>();
	    mapStack = new ArrayDeque<>();
	    mapStack.push(jsonMap);
	    listStack = new ArrayDeque<>();
	}

	/**
	 * 連想配列を開始
	 */
	public JsonBuilder startAssociativeArray(Field field) {
		return startAssociativeArray(field.getName());
	}
	public JsonBuilder startAssociativeArray(String name) {
		
		Map<String, Object> newMap = new LinkedHashMap<>();
		
		Map<String, Object> map = mapStack.peek();
		if (map == null) {
			System.out.println("debug appendAssociativeArray");
			System.out.println(this.toJson());
		}
		map.put(name, newMap);
		mapStack.push(newMap);
		return this;
	}

	/**
	 * 連想配列を開始し、リストに追加
	 */
	public JsonBuilder startAssociativeArrayAddList() {
		
		// 連想配列の追加
		Map<String, Object> newMap = new LinkedHashMap<>();
		mapStack.push(newMap);
		
		// リストに追加
		List<Object> list = listStack.peek();
		list.add(newMap);
		
		return this;
	}
	
	/**
	 * 配列を開始
	 */
	public JsonBuilder startArray(Field field) {
		Map<String, Object> map = mapStack.peek();
		
	    List<Object> _currentList = new ArrayList<>();
		map.put(field.getName(), _currentList);

		return this;
	}
	
	/**
	 * DTO配列を開始
	 */
	public JsonBuilder startDtoArray(Field field) {
		return startDtoArray(field.getName());
	}
	
	public JsonBuilder startDtoArray(String name) {

		// DTO配列格納用のリスト
		List<Object> list = new ArrayList<>();

		// DTO配列格納用リストを親のMapに登録
		Map<String, Object> map = mapStack.peek();
		map.put(name, list);
		
		listStack.push(list);
		
		return this;
	}
	
	/**
	 * 値を追加する
	 */
	public JsonBuilder addValue(Field field, Cell cell) {
		return addValue(field.getName(), cell);
	}
	
	/**
	 * 値を追加する
	 */
	public JsonBuilder addValue(String name, Cell cell) {
		
		Map<String,Object> map = mapStack.peek();
		String value = ExcelUtils.getExcelValue(cell);
		map.put(name, value);
		return this;
	}

	/**
	 * 別シートで生成されたJSONを登録
	 */
	public JsonBuilder addAnotherSheetMap(String name, String jsonString) {
		Map<String,Object> map = mapStack.peek();
		map.put(name, new JsonAnotherSheet(jsonString));
		return this;
	}

	public JsonBuilder addAnotherSheetList(String jsonString) {
		// リストに追加
		List<Object> list = listStack.peek();
		list.add(new JsonAnotherSheet(jsonString));
				
		return this;
	}

	/**
	 * 連想配列を終了
	 */
	public JsonBuilder closeAssociativeArray() {
		// Mapスタックから取り除く
		mapStack.pop();
		return this;
	}

	/**
	 * 配列を終了
	 */
	public JsonBuilder closeDtoArray() {
		// Listスタックから取り除く
		listStack.pop();
		return this;
	}

	/**
	 * 登録された情報をJSON文字列へ変換
	 */
	public String toJson() {
		StringBuilder json = new StringBuilder();
		appendMap(this.jsonMap, json, 0);
		return json.toString();
	}
	
	/**
	 * MapをJSONへ変換
	 */
	@SuppressWarnings("unchecked")
	private void appendMap(Map<String, Object> map, StringBuilder json, int indent) {
		
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
				appendMap((Map<String, Object>) value, json, _indent);
				
			} else if (value instanceof List) {
				appendList((List<Object>) value, json, _indent);
				
			} else if (value instanceof JsonAnotherSheet) {
				appendAnotherSheet(json, value, _indent);
				
			} else {
				appendValue(json, value);
				
			}
			json.append(",");
		}		
		
		--_indent;

		// カンマと半角空白を削除
		json.deleteCharAt(json.length() - 1);
		
		// 連想配列のクローズ
		json.append(kaigyo())
			.append(headspace(_indent))
			.append("}");
	}
	
	/**
	 * ListをJSONへ変換
	 */
	@SuppressWarnings("unchecked")
	private void appendList(List<Object> list, StringBuilder json, int indent) {
		
		int _indent = indent;
		
		// 改行＋配列開始
		json.append(kaigyo())
			.append(headspace(_indent))
			.append("[");

		++_indent;
		
		// リストの要素を追加
		for (Object value : list) {
			
			if (value instanceof Map) {
				appendMap((Map<String, Object>) value, json, _indent);
				
			} else if (value instanceof List) {
				appendList((List<Object>) value, json, _indent);
				
			} else if (value instanceof JsonAnotherSheet) {
				json.append(kaigyo());
				appendAnotherSheet(json, value, _indent);
				
			} else {
				json.append(kaigyo());
				appendValue(json, value);
			}
			
			json.append(",");
		}
		
		--_indent;

		// 末尾のカンマと改行を削除
		json.deleteCharAt(json.length() - 1);
		
		// 改行して閉じる
		json.append(kaigyo())
			.append(headspace(_indent))
			.append("]");
	}
	
	/**
	 * 値を追加する
	 */
	private void appendValue(StringBuilder json, Object value) {
		json.append("\"").append(value).append("\"");
	}
	
	/**
	 * 別シートで作成されたJSONを追加する
	 */
	private void appendAnotherSheet(StringBuilder json, Object value, int indent) {
		String jsonString = JsonAnotherSheet.class.cast(value).getJsonString();
		String[] split = jsonString.split(kaigyo(), -1);
		json.append(kaigyo());
		for (String string : split) {
			json.append(headspace(indent - 1)).append(string).append(kaigyo());
		}
		// 末尾の改行を削除
		json.deleteCharAt(json.length() - 1);
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
	private static class JsonAnotherSheet implements Serializable {
		private String jsonString;
		private JsonAnotherSheet(String jsonString) {
			this.jsonString = jsonString;
		}
		private String getJsonString() {
			return this.jsonString;
		}
	}
}
