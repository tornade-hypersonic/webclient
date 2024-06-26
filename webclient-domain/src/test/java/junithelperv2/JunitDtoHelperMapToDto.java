package junithelperv2;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.Cell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import junithelperv2.Enums.PropertPattern;
import junithelperv2.excel.CellOperationException;
import junithelperv2.excel.ExcelLoader;
import junithelperv2.excel.ExcelUtils;
import junithelperv2.exceldata.DtoDataTestNo;
import junithelperv2.exceldata.DtoDataTuban;
import junithelperv2.exceldata.DtoFieldInfo;
import junithelperv2.exceldata.ExcelData;
import junithelperv2.exceldata.SheetData;
import junithelperv2.json.JsonBuilder;
import junithelperv2.json.JsonBuilderHolder;
import junithelperv2.json.JsonHolder;
import junithelperv2.json.ObjectMapperFactory;

public class JunitDtoHelperMapToDto {

	private static final Logger logger = LoggerFactory.getLogger(JunitDtoHelperMapToDto.class);
	
	private static String ANOTHER_SHEET_REGEX = "\\[(.*)\\]\\[(.*)\\]\\[(.*)\\]";
	private static Pattern CELL_ANOTHER_SHEET = Pattern.compile(ANOTHER_SHEET_REGEX);
	
	private JsonBuilder json = new JsonBuilder();
	private JsonHolder jsonHolder = new JsonHolder();
	
	private final ObjectMapper objectMapper = ObjectMapperFactory.getInstance();

	public static void main(String[] args) {
		try {
			new JunitDtoHelperMapToDto().createDtoFromExcel("data/test/junithelperv2/ContractDto.xlsx");
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
	public DtoAll createDtoFromExcel(String fileName) {

		DtoAll dtoAll;
		try {
			// Excelを読込み、シート毎のDTO情報をメモリに展開する
			ExcelData excelData = ExcelLoader.loadExcelData(fileName);

			// シート毎のDTOを生成する
			dtoAll = createDtos(excelData);
			logger.debug(Objects.toString(dtoAll));

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return dtoAll;

	}


	/** 1Excelに定義されたDTOを作成する **/
	private DtoAll createDtos(ExcelData excelData) {

	    // 全DTO格納用
		DtoAll dtoAll = new DtoAll();

		// シート毎にDTOを作成する
		Set<String> sheetKeySet = excelData.getSheetKeySet();
		for (String sheetName : sheetKeySet) {
			createDtoFromSheet(dtoAll, excelData, sheetName);
		}

	    return dtoAll;
	}

	/** 1Sheetに定義されたDTOを作成する **/
	private void createDtoFromSheet(
			DtoAll dtoAll,
			ExcelData excelData,
			String sheetName
			) {
		

		if (dtoAll.containsSheet(sheetName)) {
			logger.warn("シート名[" + sheetName + "]はすでに生成済みのため、生成しない");
			return;
		}

		try {

			// シートのデータを取得
			SheetData sheetData = excelData.getSheetData(sheetName);

		    // 項目物理名を取得
		    List<DtoFieldInfo> fields = sheetData.getDtoFieldInfo();

		    // Excelから取得した情報
		    DtoDataTestNo dtoDataTestNo = sheetData.getDtoDatas();

	    	// 試験Noでループ
		    for (Iterator<Entry<String, DtoDataTuban>> testNoiterator = dtoDataTestNo.entrySet().iterator();
		    		testNoiterator.hasNext();) {

		    	Entry<String, DtoDataTuban> testNoEntry = testNoiterator.next();

			    DtoDataTuban dtoDataTuban = testNoEntry.getValue();

		    	// 通番でループ
			    for (Iterator<Entry<String, List<List<Cell>>>> tubanIterator = dtoDataTuban.entrySet().iterator();
			    		tubanIterator.hasNext();) {
			    	
			    	Entry<String, List<List<Cell>>> tubanEntry = tubanIterator.next();

				    // DTOを作成する
			    	Object dto = Utils.newInstance(sheetData.getClassName());

			    	// Mapを定義したシートの場合、Map固有の設定処理を行う
				    if (dto instanceof Map) {
				    	setValueMap(dto, tubanEntry.getValue(), fields);
				    } else {
					    setValue(dtoAll, excelData, fields, dto.getClass(), tubanEntry.getValue(), 0);
				    }

				    // JSONからDTOへ変換
				    String jsonString = json.toJson();
					Object dtoResult = objectMapper.readValue(jsonString, dto.getClass());
					
					// DTOを登録
				    dtoAll.putDto(sheetName, testNoEntry.getKey(), tubanEntry.getKey(), dtoResult);
				    
				    // DTO生成したJSONは別シート参照時に再利用するため、保持する
				    jsonHolder.put(sheetName, testNoEntry.getKey(), tubanEntry.getKey(), jsonString);
				    
				    // 後処理
			    	json.clear();
			    }
		    }
		} catch (Exception e) {
			throw new RuntimeException(String.format("sheetName=[%s]にて例外発生", sheetName), e);
		}
	}

	/** Mapを定義したシートの値をMapに設定する **/
	private void setValueMap(
			Object dto,
			List<List<Cell>> tubanValue,
			List<DtoFieldInfo> fields) {

		List<Cell> keyCells = tubanValue.get(0);
		List<Cell> valCells = tubanValue.get(1);

		for (int i = 0; i < keyCells.size(); i++) {
			Cell keyCell = keyCells.get(i);
			Cell valCell = valCells.get(i);

			json.addValue(
					ExcelUtils.getExcelValue(keyCell),
					valCell);
		}

	}

	/** Excelの1通番をDTOに設定する **/
	private void setValue(
			DtoAll dtoAll,
			ExcelData excelData,
			List<DtoFieldInfo> fields,
			Class<?> targetDtoClass,
			List<List<Cell>> renbanList,
			int currentLevel
			) {
		
		List<Cell> cells = renbanList.get(0);

        for (int itemIndex = 0; itemIndex < cells.size(); itemIndex++) {

		    Cell cell = cells.get(itemIndex);
		    String cellValue = ExcelUtils.getExcelValue(cell);
			
		    // Java変数情報
			DtoFieldInfo fieldInfo = fields.get(itemIndex);
		    
		    // 設定対象のField
			Field field = ClassFieldUtils.getField(targetDtoClass, fieldInfo.getFieldName());
			if (field == null) {
				logger.debug(json.toJson());
				throw new RuntimeException(String.format("field==null, i=%s, name=%s", itemIndex, fieldInfo.getFieldName()));
			}

		    // TODO 後で削除する
		    if ("serviceInfoArrayLevel".equals(fieldInfo.getFieldName())) {
		    	logger.debug("debug");
		    }

		    if (cellValue != null && cellValue.matches(ANOTHER_SHEET_REGEX)) {
		    	
		    	if (field.getType().isArray() ||
		    			List.class.isAssignableFrom(field.getType())) {
					// 配列、またはリストの場合
			    	appendAnotherDtoList(dtoAll, excelData, fieldInfo, renbanList, itemIndex);
		    		
			    } else if (Map.class.isAssignableFrom(field.getType())) {
					// Mapの場合
					appendAnotherDto(dtoAll, excelData, cell, fieldInfo, false);
			    	
		    	} else {
		    		// DTOの場合
		    		appendAnotherDto(dtoAll, excelData, cell, fieldInfo, false);
		    	}
		    	
		    } else if (field.getType().isArray()) {
				// 配列の場合
		    	
		    	// DTO配列の場合
		    	if (isDtoArrayOrList(fields, itemIndex)) {
		    		
	            	// 子階層の値を設定 連番のデータもここで設定する
			        int assertLineCount = appendRenbanItems(
			        		dtoAll, excelData, fieldInfo, fields, field, renbanList, itemIndex, PropertPattern.DTO_ARRAY);
			        // 子階層の行数をスキップ
			    	itemIndex = itemIndex + assertLineCount;
			    	
		    	} else {
		    		// プリミティブ型の配列
		    		appendPrimitiveArray(fieldInfo, renbanList, itemIndex);
		    		
		    	}
		    	
				
            } else if (List.class.isAssignableFrom(field.getType())) {
				// リストの場合
            	
		    	if (isDtoArrayOrList(fields, itemIndex)) {
			    	// DTOリストの場合

	            	// 子階層の値を設定 連番のデータもここで設定する
			        int assertLineCount = appendRenbanItems(
			        		dtoAll, excelData, fieldInfo, fields, field, renbanList, itemIndex, PropertPattern.DTO_LIST);
			        // 子階層の行数をスキップ
			    	itemIndex = itemIndex + assertLineCount;
			    	
		    	} else {
		    		// プリミティブ型のリスト
		    		appendPrimitiveArray(fieldInfo, renbanList, itemIndex);
		    		
		    	}
				
            } else if (Map.class.isAssignableFrom(field.getType())) {
				// Mapの場合、必ず別シート参照とするため、ここを通るときは設定値誤り
		    	throw new CellOperationException("Mapフィールドの形式が誤っています。設定値を確認してください。", cell, cellValue);
				
            } else if (isDtoArrayOrList(fields, itemIndex)) {
				// DTOの場合
            	
            	// 親階層のJSON編集
		    	json.startAssociativeArray(field);
            	// 子階層のJSON編集
		        int assertLineCount = appendRenbanItems(
		        		dtoAll, excelData, fieldInfo, fields, field, renbanList, itemIndex, PropertPattern.DTO);
		        // 子階層の行数をスキップ
		    	itemIndex = itemIndex + assertLineCount;
		        
    		    	
			} else {
				logger.debug("通常\t" + field.getName() + "\t" + field.getType());
				json.addValue(field, cell);
			}
			
		}
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
	 *     
	 *  注意）このメソッドは、縦に設定→横の連番の値を設定、であるため、プリミティブ型の配列は検討しない
	 */
	private int appendRenbanItems(
			DtoAll dtoAll,
			ExcelData excelData,
			DtoFieldInfo fieldInfo,
			List<DtoFieldInfo> fields,
			Field field,
			List<List<Cell>> renbanList,
			int itemIndex,
			PropertPattern pattern
			) {
		
	    Class<?> targetDtoClass = ClassFieldUtils.getClass(field, pattern);
	    
		// JSON追加の対象数
		int appendLineCount = 0;

	    // 親の階層レベルを取得
	    // ここに到達するのは、親の階層
	    int parentLevel = fieldInfo.getLevel();

	    List<DtoFieldInfo> wFields = new ArrayList<>();
	    List<List<Cell>> wRenbanList = new ArrayList<>();
	    
    	if (pattern.isDtoArray()) {
    		json.startArray(field);
    	}
	    
	    // 連番でループ
	    for (int renbanCnt = 0; renbanCnt < renbanList.size(); renbanCnt++) {
	    	
	    	List<Cell> cells = renbanList.get(renbanCnt);
	    	
		    if (pattern.isDtoArray()) {
		    	// DTO配列の場合、連想配列をリストに登録
		    	json.startAssociativeArrayAddList();
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
		    
		    // 設定値対象がすべて値なしの場合、データなしとみなす
		    if (isEmptyData(wCells)) {
		    	continue;
		    }
		    
		    wRenbanList.add(wCells);
		    appendLineCount = wCells.size();

		    // 値の設定処理
		    setValue(dtoAll, excelData, wFields, targetDtoClass, wRenbanList, parentLevel);

		    if (pattern.isDtoArray()) {
		    	// DTO配列の場合、連想配列を除去
		    	json.closeAssociativeArray();
		    }
		    
		    wRenbanList.clear();
	    }
	    
    	if (pattern.isDtoArray()) {
    		json.closeArray();
    	} else if (pattern.isDto()) {
    		json.closeAssociativeArray();
    	}
	    return appendLineCount;
	}

	
	/**
	 * 別シートのJSONを取得する。
	 * createDtoFromSheet()を再帰的に呼び出すため、未作成のシートであれば、ここで生成される
	 **/
	private void appendAnotherDto(
			DtoAll dtoAll,
			ExcelData excelData,
			Cell cell,
			DtoFieldInfo fieldInfo,
			boolean isList) {

    	// セルの値を取得
	    String cellValue = ExcelUtils.getExcelValue(cell);

	    // 別シート参照情報の解析
	    Matcher matcher = CELL_ANOTHER_SHEET.matcher(cellValue);
	    if (!matcher.find()) {
	    	// throw new CellOperationException("別シート参照の形式誤り", cell, cellValue);
	    	// 設定値がないものとして、処理を終了する
	    	return;
	    }

    	// 解析結果から、シート名・試験No・通番の情報を取得する
    	String anotherSheetName = matcher.group(1);
    	String anotherTestNo = matcher.group(2);
    	String anotherTuban = matcher.group(3);
    	if (!dtoAll.containsSheet(anotherSheetName)) {
	    	// 指定したシートのDTOがまだ生成されてない場合、生成する
    		backupJsonBuilder();
    		
	    	createDtoFromSheet(dtoAll, excelData, anotherSheetName);
	    	
	    	restoreJsonBuilder();
    	}
    	
    	// createDtoFromSheet() でJSONがJsonHolderに格納されるので、
    	// そのJSONを取得し、JsonBuiderに追加する
    	String jsonString = jsonHolder.get(anotherSheetName, anotherTestNo, anotherTuban);
    	
    	if (isList) {
    		json.addAnotherSheetArray(jsonString);
    	} else {
        	json.addAnotherSheetMap(fieldInfo.getFieldName(), jsonString);
    	}
    	
	}
	
	/**
	 * DTOリストの別シートのJSONを取得する。
	 **/
	private void appendAnotherDtoList(
			DtoAll dtoAll,
			ExcelData excelData,
			DtoFieldInfo fieldInfo,
			List<List<Cell>> renbanList,
			int itemIndex
			) {
		
		// DTO配列スタート
		json.startArray(fieldInfo.getFieldName());
		
	    // 連番でループ
	    for (int renbanCnt = 0; renbanCnt < renbanList.size(); renbanCnt++) {
	    	
	    	List<Cell> cells = renbanList.get(renbanCnt);
	    	
	    	Cell cell = cells.get(itemIndex);
	    	
		    // 値の設定処理
			appendAnotherDto(dtoAll, excelData, cell, fieldInfo, true);

	    }
	 		
		// DTO配列終了
		json.closeArray();
	}

	/**
	 * プリミティブ型の配列を設定する。
	 **/
	private void appendPrimitiveArray(
			DtoFieldInfo fieldInfo,
			List<List<Cell>> renbanList,
			int itemIndex
			) {
		
		// DTO配列スタート
		json.startArray(fieldInfo.getFieldName());
		
	    // 連番でループ
	    for (int renbanCnt = 0; renbanCnt < renbanList.size(); renbanCnt++) {
	    	
	    	List<Cell> cells = renbanList.get(renbanCnt);
	    	
	    	// セルの値を取得
	    	Cell cell = cells.get(itemIndex);
	    	String value = ExcelUtils.getExcelValue(cell);
	    	
		    // 値の設定処理
			json.addArray(value);

	    }
	 		
		// DTO配列終了
		json.closeArray();
	}

	/**
	 * 現在のExcel行がDTOや配列に該当するか、判定する
	 */
	private boolean isDtoArrayOrList(List<DtoFieldInfo> fields, int itemIndex) {
		
		// 最終行の場合、配列orListではない
		if (itemIndex == fields.size() - 1) {
			return false;
		}
		
		// 階層が変わる場合、配列orListと判定する
    	DtoFieldInfo nextFieldInfo = fields.get(itemIndex + 1);
    	DtoFieldInfo currentFieldInfo = fields.get(itemIndex);
    	if (currentFieldInfo.getLevel() < nextFieldInfo.getLevel()) {
    		return true;
    	}
    	
		return false;
	}
	
    // 設定値対象がすべて値なしの場合、データなしとみなす
	private boolean isEmptyData(List<Cell> wCells) {
		
	    boolean empty = true;
	    for (Cell wCell : wCells) {
			if (wCell != null && ExcelUtils.getExcelValue(wCell) != null) {
				empty = false;
				break;
			}
		}
	    return empty;
	}

	/**
	 * JsonBuilderオブジェクトを退避する。
	 * 別シート参照時にクリアする必要があるが、別シート参照が終了すると、クリア前から再開する必要があるため。
	 * 退避したオブジェクトは変更されることがないため、ディープコピーは行わない。
	 */
	private void backupJsonBuilder() {
		JsonBuilderHolder.push(this.json);
		this.json = new JsonBuilder();
	}

	/**
	 * 退避したJsonBuilderオブジェクトをリストアする。
	 */
	private void restoreJsonBuilder() {
		this.json = JsonBuilderHolder.peek();
		JsonBuilderHolder.pop();
	}

}
