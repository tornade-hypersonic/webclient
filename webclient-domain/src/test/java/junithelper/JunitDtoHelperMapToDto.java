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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.Cell;

import com.fasterxml.jackson.databind.ObjectMapper;

import junithelper.Enums.PropertPattern;

public class JunitDtoHelperMapToDto {

	private static String ANOTHER_SHEET_REGEX = "\\[(.*)\\]\\[(.*)\\]\\[(.*)\\]";
	private static Pattern CELL_ANOTHER_SHEET = Pattern.compile(ANOTHER_SHEET_REGEX);
	private static Pattern CELL_LIST_TYPE = Pattern.compile(".*<(.*)>");
	
	private JsonBuilder json = new JsonBuilder();
	private JsonHolder jsonHolder = new JsonHolder();
	
	private final ObjectMapper objectMapper = ObjectMapperFactory.getInstance();

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
//				    dtosTubanMap.put(tubanEntry.getKey(), dto);

				    // Field情報をロード
				    Map<String,Field> classFiledMap = ClassUtils.loadFiled(dtoInfo.getClassName());

			    	// Mapを定義したシートの場合、Map固有の設定処理を行う
				    if (dto instanceof Map) {
				    	setValueMap(dto, tubanEntry.getValue(), fields);
				    	continue;
				    }

				    // Excelの値をDTOに設定する
				    setValue(dtoAll, sheetMap, fields, classFiledMap, tubanEntry.getValue(), 0);
			    	
				    // JSONからDTOへ変換
				    String jsonString = json.toJson();
			    	System.out.println("★JSON★" + jsonString);
					Object dtoResult = objectMapper.readValue(jsonString, dto.getClass());
				    dtosTubanMap.put(tubanEntry.getKey(), dtoResult);
				    jsonHolder.put(sheetName, testNoEntry.getKey(), tubanEntry.getKey(), jsonString);
			    	json.clear();
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
			Map<String,Field> classFiledMap,
			List<List<Cell>> renbanList,
			int currentLevel
			) {
		
	    json.appendOpen(currentLevel);
	    
		List<Cell> cells = renbanList.get(0);

		int preLevel = 1;	// 前回の階層レベル
	    boolean openAssociative = false;
	    
		// 設定対象DTO格納スタック
        Deque<Map<String, Field>> classFiledMapStack = new ArrayDeque<>();
        classFiledMapStack.push(classFiledMap);

        for (int itemIndex = 0; itemIndex < cells.size(); itemIndex++) {

		    Cell cell = cells.get(itemIndex);
		    String cellValue = ExcelUtils.getExcelValue(cell);
			
		    // Java変数情報
			DtoFieldInfo fieldInfo = fields.get(itemIndex);
		    int level = fieldInfo.getLevel();
	    	openAssociative = false;
		    
		    // 階層レベルが下がった場合、スタックからDTOを一つ削除する
		    if (preLevel > level) {
		    	classFiledMapStack.pop();
		    	json.appendClose(level);
		    }

		    // 設定対象のField
		    Map<String, Field> currentClassFiledMap = classFiledMapStack.peek();
			Field field = currentClassFiledMap.get(fieldInfo.getFieldName());
			if (field == null) {
				System.out.println(json.toJson());
				throw new RuntimeException(String.format("field==null, i=%s, name=%s", itemIndex, fieldInfo.getFieldName()));
			}

		    // TODO 後で削除する
		    if ("serviceInfoListLevel".equals(fieldInfo.getFieldName())) {
		    	System.out.println("デバッグ用");
		    }

		    if (cellValue.matches(ANOTHER_SHEET_REGEX)) {
				// TODO 保留
		    	
		    	if (List.class.isAssignableFrom(field.getType())) {
		    		
			    } else if (field.getType().isArray()) {
			    	
		    	} else {
		    		appendAnotherDto(dtoAll, sheetMap, cell, fieldInfo);
		    	}
		    	
		    } else if (field.getType().isArray()) {
				// TODO 配列の場合
		    	
		    	// DTO配列の場合
		    	if ("[new]".equals(cellValue)) {
	            	// 親階層のJSON編集
			    	json.appendAssociativeArray(field, level);
			    	openAssociative = true;
	            	// 子階層のJSON編集
			        int assertLineCount = appendRenbanItems(
			        		dtoAll, sheetMap, fieldInfo, fields, field, renbanList, itemIndex, PropertPattern.DTO_ARRAY);
			        // 子階層の行数をスキップ
			    	itemIndex = itemIndex + assertLineCount;
		    	}
		    	
				
            } else if (List.class.isAssignableFrom(field.getType())) {
				// リストの場合
            	
		    	// DTOリストの場合
		    	if ("[new]".equals(cellValue)) {
	            	// 親階層のJSON編集
			    	json.appendAssociativeArray(field, level);
	            	// 子階層のJSON編集
			        int assertLineCount = appendRenbanItems(
			        		dtoAll, sheetMap, fieldInfo, fields, field, renbanList, itemIndex, PropertPattern.LIST);
			        // 子階層の行数をスキップ
			    	itemIndex = itemIndex + assertLineCount;
			    	
		    	} else {
		    		// TODO プリミティブ型はまだ
		    		
		    	}
				
            } else if (Map.class.isAssignableFrom(field.getType())) {
				// TODO 保留
				
            } else if ("[new]".equals(cellValue)) {
				// DTOの場合
            	
            	// 親階層のJSON編集
		    	json.appendAssociativeArray(field, level);
            	// 子階層のJSON編集
		        int assertLineCount = appendRenbanItems(
		        		dtoAll, sheetMap, fieldInfo, fields, field, renbanList, itemIndex, PropertPattern.DTO);
		        // 子階層の行数をスキップ
		    	itemIndex = itemIndex + assertLineCount;
		        
    		    	
			} else {
				System.out.println("通常\t" + field.getName() + "\t" + field.getType());
				json.appendValue(field, cell, level);
			}
			
		    // 階層レベルを前回分として保持
		    preLevel = level;

		}

		json.appendClose(preLevel - 1);
	}

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
	private int appendRenbanItems(
			Map<String, Map<String, Map<String, Object>>> dtoAll,
			Map<String, DtoExcelSheet> sheetMap,
			DtoFieldInfo fieldInfo,
			List<DtoFieldInfo> fields,
			Field field,
			List<List<Cell>> renbanList,
			int itemIndex,
			PropertPattern pattern
			) {
		
	    Map<String,Field> classFiledMap = ClassUtils.loadFiledByField(field, pattern);
	    
		// JSON追加の対象数
		int appendLineCount = 0;

	    // 親の階層レベルを取得
	    // ここに到達するのは、親の階層
	    int parentLevel = fieldInfo.getLevel();

	    List<DtoFieldInfo> wFields = new ArrayList<>();
	    List<List<Cell>> wRenbanList = new ArrayList<>();
	    
	    if (pattern.isArray()) {
	    	json.appendOpenArray(parentLevel);
	    }
	    
	    // 連番でループ
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
		    	if (parentLevel == dtoFieldInfo.getLevel()) {
		    		// 親階層と同じレベルの場合、子階層終了とみなす
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

		    // 値の設定処理
		    setValue(dtoAll, sheetMap, wFields, classFiledMap, wRenbanList, parentLevel);

		    wRenbanList.clear();
	    }
	    if (pattern.isArray()) {
	    	json.appendCloseArray(parentLevel);
	    }
	    
	    return appendLineCount;
	}

	
	/**
	 * 別シートのJSONを取得する。
	 * createDtoFromSheet()を再帰的に呼び出すため、未作成のシートであれば、ここで生成される
	 **/
	private void appendAnotherDto(
			Map<String, Map<String, Map<String, Object>>> dtoAll,
			Map<String, DtoExcelSheet> sheetMap,
			Cell cell,
			DtoFieldInfo fieldInfo) {

    	// セルの値を取得
	    String cellValue = ExcelUtils.getExcelValue(cell);

	    // 別シート参照情報の解析
	    Matcher matcher = CELL_ANOTHER_SHEET.matcher(cellValue);
	    if (!matcher.find()) {
	    	throw new CellOperationException("別シート参照の形式誤り", cell, cellValue);
	    }

    	// 解析結果から、シート名・試験No・通番の情報を取得する
    	String anotherSheetName = matcher.group(1);
    	String anotherTestNo = matcher.group(2);
    	String anotherTuban = matcher.group(3);
    	if (!dtoAll.containsKey(anotherSheetName)) {
	    	// 指定したシートのDTOがまだ生成されてない場合、生成する
	    	createDtoFromSheet(dtoAll, sheetMap, anotherSheetName);
    	}
    	
    	// createDtoFromSheet() でJSONがJsonHolderに格納されるので、
    	// そのJSONを取得し、JsonBuiderに追加する
    	String jsonString = jsonHolder.get(anotherSheetName, anotherTestNo, anotherTuban);
    	json.appendAnotherSheet(jsonString, fieldInfo.getLevel());
    	
	}
	
	
}
