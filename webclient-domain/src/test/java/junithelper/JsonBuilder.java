package junithelper;

import java.lang.reflect.Field;

import org.apache.poi.ss.usermodel.Cell;

/**
 * JSON文字列を生成する
 * 項目追加時にカンマを追加し、無駄なカンマと判定した場合、削除する
 * 改行は項目を追加するときに、先頭に追加する
 * 
 */
public class JsonBuilder {

	private StringBuilder json = new StringBuilder();
	
	public void clear() { 
		json.setLength(0);
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
		String value = ExcelUtils.getExcelValue(cell);
		json.append(kaigyo())
		    .append(headspace(level))
		    .append("\"")
		    .append(field.getName())
		    .append("\"")
			.append(" : ")
			.append("\"")
			.append(value)
			.append("\",")
			;
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
	 * @param json
	 * @param level
	 * @return
	 */
	public JsonBuilder appendClose(int level) {
		json.deleteCharAt(json.length() - 1);
		json.append(kaigyo())
	    	.append(headspace(level))
		    .append("},");
		return this;
	}
	
	public JsonBuilder appendCloseArray(int level) {
		json.deleteCharAt(json.length() - 1);
//		json.append(kaigyo())
//	    	.append(headspace(level))
//		    .append("],");
		json.append("],");
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
	public JsonBuilder appendOpen(int level) {
		if (json.length() != 0) {
			json.append(kaigyo());
		}
		json.append(headspace(level)).append("{");
		return this;
	}
	
	public JsonBuilder appendOpenArray(int level) {
//		if (json.length() != 0) {
//			json.append(kaigyo());
//		}
//		json.append(headspace(level)).append("[");
		json.append("[");
		return this;
	}
	
	public JsonBuilder appendAssociativeArray(Field field, int level) {
		json.append(kaigyo())
		    .append(headspace(level))
		    .append("\"")
		    .append(field.getName())
		    .append("\"")
			.append(" : ")
			;
		return this;
	}
	
	public String toJson() {
		if (json.charAt(json.length() - 1) == ',') {
			json.deleteCharAt(json.length() - 1);
		}
		return json.toString();
	}

	private String kaigyo() {
		return "\n";
	}
	
	private String headspace(int level) {
		StringBuilder space = new StringBuilder();
		for (int i = 0; i < level; i++) {
			space.append("  ");
		}
		return space.toString();
	}
	
}
