package junithelper;

import java.lang.reflect.Field;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.Cell;

public class JunitDtoHelperMapToDto {

	private static String ANOTHER_SHEET_REGEX = "\\[(.*)\\]\\[(.*)\\]\\[(.*)\\]";
	private static Pattern CELL_ANOTHER_SHEET = Pattern.compile(ANOTHER_SHEET_REGEX);
	private static Pattern CELL_LIST_TYPE = Pattern.compile(".*<(.*)>");

	public static void main(String[] args) {
		try {
			new JunitDtoHelperMapToDto().createDtoFromExcel("data/test/ContractDto.xlsx");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * Excelを読込み、DTOを返却する。
	 * 返却するDTOは下記の通り。
	 *
	 * dtos {
	 *   key   : Ecelのシート名,
	 *   value : map {
	 *     key   : 試験No,
	 *     value : map {
	 *       key   : 通番,
	 *       value : DTO
	 *     }
	 *   }
	 * }
	 *
	 * @param fileName Excel格納パス
	 * @return dtos
	 */
	public Map<String, Map<String, Map<String, Object>>> createDtoFromExcel(String fileName) {

		Map<String, Map<String, Map<String, Object>>> dtos = null;
		try {
			// Excelを読込み、シート毎のDTO情報をメモリに展開する
			Map<String, DtoExcelSheet> sheetMap = ExcelLoader.loadExcelSheet(fileName);

			// シート毎のDTOを生成する
			dtos = createDtos(sheetMap);
			System.out.println(dtos);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return dtos;

	}


	/** 1Excelに定義されたDTOを作成する **/
	private Map<String, Map<String, Map<String, Object>>> createDtos(Map<String, DtoExcelSheet> sheetMap) {

	    // 全データ格納用マップ（key：シート名、value：Map）
		Map<String, Map<String, Map<String, Object>>> dtoAll = new LinkedHashMap<>();

		Set<Entry<String,DtoExcelSheet>> entrySet = sheetMap.entrySet();
		for (Entry<String, DtoExcelSheet> entry : entrySet) {
			String sheetName = entry.getKey();
			createDtoFromSheet(dtoAll, sheetMap, sheetName);
		}

	    System.out.println("★★ createDtoFromSheet dtoAll=" + dtoAll);
	    return dtoAll;
	}

	/** 1Sheetに定義されたDTOを作成する **/
	private void createDtoFromSheet(
			Map<String, Map<String, Map<String, Object>>> dtoAll,
			Map<String, DtoExcelSheet> sheetMap,
			String sheetName) {

		if (dtoAll.containsKey(sheetName)) {
			System.out.println("シート名[" + sheetName + "]はすでに生成済みのため、生成しない");
			return;
		}

		try {

			DtoExcelSheet dtoExcelSheet = sheetMap.get(sheetName);
			DtoInfo dtoInfo = dtoExcelSheet.getDtoInfo();

		    // 項目物理名を取得
		    List<DtoFieldInfo> fields = dtoInfo.getDtoFieldInfo();

		    // シート単位のDTO格納マップ（key：試験No、value：DTO）
		    Map<String, Map<String, Object>> dtosTestNoMap = new LinkedHashMap<>();
		    dtoAll.put(sheetName, dtosTestNoMap);

		    // Excelから取得した情報
		    Map<String, Map<String, List<List<Cell>>>> dtoDatas = dtoInfo.getDtoDatas();

	    	// 試験Noでループ
		    for (Iterator<Entry<String, Map<String, List<List<Cell>>>>> testNoiterator = dtoDatas.entrySet().iterator();
		    		testNoiterator.hasNext();) {

		    	Entry<String, Map<String, List<List<Cell>>>> testNoEntry = testNoiterator.next();
		    	Map<String, Object> dtosTubanMap = new LinkedHashMap<>();
			    dtosTestNoMap.put(testNoEntry.getKey(), dtosTubanMap);

			    Map<String, List<List<Cell>>> tubanMap = testNoEntry.getValue();

		    	// 通番でループ
			    for (Iterator<Entry<String, List<List<Cell>>>> tubanIterator = tubanMap.entrySet().iterator();
			    		tubanIterator.hasNext();) {

			    	Entry<String, List<List<Cell>>> tubanEntry = tubanIterator.next();

				    // DTOを作成する
			    	Object dto = Utils.newInstance(dtoInfo.getClassName());
				    dtosTubanMap.put(tubanEntry.getKey(), dto);

				    // Field情報をロード
				    Map<String,Field> classFiledMap = ClassUtils.loadFiled(dtoInfo.getClassName());
				    StringBuilder json = new StringBuilder();

			    	// Mapを定義したシートの場合、Map固有の設定処理を行う
				    if (dto instanceof Map) {
				    	setValueMap(dto, tubanEntry.getValue(), fields);
				    	continue;
				    }

//			    	// 連番でループ
//			    	for (List<Cell> cells : tubanEntry.getValue()) {
//
//					    // Excelの値をDTOに設定する
//					    setValue(dtoAll, sheetMap, fields, cells, json, classFiledMap);
//					}
				    // Excelの値をDTOに設定する
				    setValue(dtoAll, sheetMap, fields, json, classFiledMap, tubanEntry.getValue());
			    	
			    	System.out.println("★JSON★" + json.toString());
			    }
		    }
		} catch (Exception e) {
			throw new RuntimeException(String.format("sheetName=[%s]にて例外発生", sheetName), e);
		}
	}

	/** Mapを定義したシートの値をMapに設定する **/
	@SuppressWarnings("unchecked")
	private void setValueMap(
			Object dto,
			List<List<Cell>> tubanValue,
			List<DtoFieldInfo> fields) {

		List<Cell> keyCells = tubanValue.get(0);
		List<Cell> valCells = tubanValue.get(1);

		DtoFieldInfo keyField = fields.get(0);
		DtoFieldInfo valField = fields.get(1);

		for (int i = 0; i < keyCells.size(); i++) {
			Cell keyCell = keyCells.get(i);
			Cell valCell = valCells.get(i);

		    Object key = ExcelUtils.getExcelValueForDto(keyCell, keyField.getFieldClassName());
		    Object val = ExcelUtils.getExcelValueForDto(valCell, valField.getFieldClassName());

		    Map.class.cast(dto).put(key, val);
		}

	}

	/** Excelの1通番をDTOに設定する **/
	private void setValue(
			Map<String, Map<String, Map<String, Object>>> dtoAll,
			Map<String, DtoExcelSheet> sheetMap,
			List<DtoFieldInfo> fields,
//			List<Cell> cells,
			StringBuilder json,
			Map<String,Field> classFiledMap,
			List<List<Cell>> renbanList
			) {
		
	    json.append("{").append("\n");
	    
		List<Cell> cells = renbanList.get(0);

		int preLevel = 1;	// 前回の階層レベル
	    
		// 設定対象DTO格納スタック
        Deque<Map<String, Field>> classFiledMapStack = new ArrayDeque<>();
        classFiledMapStack.push(classFiledMap);

        for (int itemIndex = 0; itemIndex < cells.size(); itemIndex++) {
			

		    Cell cell = cells.get(itemIndex);
		    String cellValue = ExcelUtils.getExcelValue(cell);
			
		    // Java変数情報
			DtoFieldInfo fieldInfo = fields.get(itemIndex);
		    int level = fieldInfo.getLevel();
		    
		    // 階層レベルが下がった場合、スタックからDTOを一つ削除する
		    if (preLevel > level) {
		    	classFiledMapStack.pop();
		    	json.append(headspace(level)).append("}\n");
		    }

		    // 設定対象のField
		    Map<String, Field> currentClassFiledMap = classFiledMapStack.peek();
			Field field = currentClassFiledMap.get(fieldInfo.getFieldName());
			if (field == null) {
				throw new RuntimeException(String.format("field==null, i=%s, name=%s", itemIndex, fieldInfo.getFieldName()));
			}

		    // TODO 後で削除する
		    if ("id".equals(fieldInfo.getFieldName())) {
		    	System.out.println("デバッグ用");
		    }

		    if (cellValue.matches(ANOTHER_SHEET_REGEX)) {
				// TODO 保留
		    	
		    } else if (field.getType().isArray()) {
				// TODO 保留
		    	appendAssociativeArray(field, json, level);
				
            } else if (List.class.isAssignableFrom(field.getType())) {
				// TODO 保留
				
            } else if (Map.class.isAssignableFrom(field.getType())) {
				// TODO 保留
				
            } else if ("[new]".equals(cellValue)) {
				// DTOの場合
            	appendAssociativeArray(field, json, level);
            	
			    Map<String,Field> classFiledMapChild = ClassUtils.loadFiledByField(field);
//		        classFiledMapStack.push(classFiledMapChild);
		        
		        int assertLineCount = appendRenbanItems(
		        		dtoAll, sheetMap, fieldInfo, fields, json, classFiledMapChild, renbanList, itemIndex);
		        
		    	itemIndex = itemIndex + assertLineCount;
		        
    		    	
			} else {
				System.out.println("通常\t" + field.getName() + "\t" + field.getType());
				appendValue(field, cell, json, level);
			}
			
		    // 階層レベルを前回分として保持
		    preLevel = level;

		}

		json.append("}").append("\n");
	}

	private void appendAssociativeArray(Field field, StringBuilder json, int level) {
		
		json.append(headspace(level)).append("\"").append(field.getName()).append("\"")
			.append(": ")
			.append("\n");
	}

	private void appendArray(Field field, StringBuilder json, int level) {
		
		json.append(headspace(level)).append("\"").append(field.getName()).append("\"")
			.append(": ")
			.append("\n");
	}

	private void appendValue(Field field, Cell cell, StringBuilder json, int level) {
		
		String value = ExcelUtils.getExcelValue(cell);
		
		json.append(headspace(level)).append("\"").append(field.getName()).append("\"")
			.append(": ")
			.append("\"").append(value).append("\",")
			.append("\n");
	}
	
	private int appendRenbanItems(
			Map<String, Map<String, Map<String, Object>>> dtoAll,
			Map<String, DtoExcelSheet> sheetMap,
			DtoFieldInfo fieldInfo,
			List<DtoFieldInfo> fields,
			StringBuilder json,
			Map<String,Field> classFiledMap,
			List<List<Cell>> renbanList,
			int itemIndex) {
		
		/**
		 * DTO配列として連番で定義された値を追加する。
		 * 例）
		 *   serviceInfoArrayLevel | junithelper.ServiceInfo[] | 1 |             | 
		 *   id                    | java.lang.String          | 2 | 301         | 302
		 *   name                  | java.lang.String          | 2 | サービス301 | サービス302
		 * このidとnameの連番で定義された値をJSONに追加する
		 * 手順としては、
		 *  1. cellsから id,name の値のみを抜き出した wCells を生成する
		 *  2. renbanListを一つのみ設定した wRenbanList を生成する
		 *  3. 再帰処理を利用して setValue()を呼び出す
		 *     再帰処理内では、id,name のみがJSONに追加される
		 *  4. 1～3をrenbanListの分だけ実行する
		 *     これにより、301,サービス301、302,サービス302 が追加される
		 *  注意）
		 *     id,name はこのメソッドで追加したため、id,nameはスキップする必要がある
		 *     スキップする行数を返却し、呼び出し元でスキップする
		 */
		
		// JSON追加の対象数
		int appendLineCount = 0;

	    // 親の階層レベルを取得
	    // ここに到達するのは、親の階層
	    int parentLevel = fieldInfo.getLevel();
	    int childLevel = parentLevel + 1;

	    List<DtoFieldInfo> wFields = new ArrayList<>();
	    List<List<Cell>> wRenbanList = new ArrayList<>();
	    
	    for (int renbanCnt = 0; renbanCnt < renbanList.size(); renbanCnt++) {
	    	List<Cell> cells = renbanList.get(renbanCnt);
	    	
	    	// 親階層に [new] が設定されてない場合、その連番はスキップする
	    	Cell parentCell = cells.get(itemIndex);
	    	String value = ExcelUtils.getExcelValue(parentCell);
	    	if ("[new]".equals(value) == false) {
	    		continue;
	    	}
	    	
			boolean first = true;
		    List<Cell> wCells = new ArrayList<>();
		    // 親階層はスキップするので、itemIndexの次からスタートする
		    for (int i = itemIndex + 1; i < fields.size(); i++) {
		    	DtoFieldInfo dtoFieldInfo = fields.get(i);
		    	if (childLevel != dtoFieldInfo.getLevel()) {
		    		break;
		    	}
		    	if (first) {
			    	wFields.add(fields.get(i));
		    	}
		    	wCells.add(cells.get(i));
		    }
		    first = false;
		    wRenbanList.add(wCells);
		    appendLineCount = wCells.size();
	    	
		    setValue(dtoAll, sheetMap, wFields, json, classFiledMap, wRenbanList);
		    
		    
		    wRenbanList.clear();
	    }
	    return appendLineCount;
	}

	
	private String headspace(int level) {
		StringBuilder space = new StringBuilder();
		for (int i = 0; i < level; i++) {
			space.append("  ");
		}
		return space.toString();
	}
}
