package junithelper;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;

public class JunitDtoHelper {

	private static String ANOTHER_SHEET_REGEX = "\\[(.*)\\]\\[(.*)\\]\\[(.*)\\]";
	private static Pattern CELL_ANOTHER_SHEET = Pattern.compile(ANOTHER_SHEET_REGEX);
	private static Pattern CELL_LIST_TYPE = Pattern.compile(".*<(.*)>");

	public static void main(String[] args) {
		try {
			new JunitDtoHelper().createDtoFromExcel("data/test/ContractDto.xlsx");
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

			    	// Mapを定義したシートの場合、Map固有の設定処理を行う
				    if (dto instanceof Map) {
				    	setValueMap(dto, tubanEntry.getValue(), fields);
				    	continue;
				    }

			    	// 連番でループ
			    	for (List<Cell> cells : tubanEntry.getValue()) {

					    // Excelの値をDTOに設定する
					    setValue(dtoAll, sheetMap, fields, cells, dto);
					}
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
			List<Cell> cells,
			Object dto) {

		int preLevel = 1;	// 前回の階層レベル

		// 設定対象DTO格納スタック
        Deque<Object> dtoStack = new ArrayDeque<>();
        dtoStack.push(dto);

		for (int i = 0; i < cells.size(); i++) {

			Cell cell = cells.get(i);

		    // Java変数情報
			DtoFieldInfo fieldInfo = fields.get(i);
		    String fieldName = fieldInfo.getFieldName();
		    String fieldClassName = fieldInfo.getFieldClassName();
		    int level = fieldInfo.getLevel();

		    String cellValue = ExcelUtils.getExcelValue(cell);

		    try {

			    // TODO 後で削除する
			    if ("statusPulldown".equals(fieldName)) {
			    	System.out.println("デバッグ用");
			    }

			    // セルに値がない場合
			    if (StringUtils.isEmpty(cellValue)) {
				    preLevel = level;   // 階層レベルを前回分として保持
			    	continue;
			    }

			    // 階層レベルが下がった場合、スタックからDTOを一つ削除する
			    if (preLevel > level) {
			    	dtoStack.pop();
			    }

			    // 設定対象のDTO
			    Object targetDto = dtoStack.peek();

			    if ("[new]".equals(cellValue)) {
				    // インスタンスを生成する場合

					Object instance;

			    	if (fieldClassName.endsWith("[]")) {
				    	// 配列の場合
			    		instance = setFieldLevelArray(targetDto, fieldInfo);

			    	} else if (Utils.isList(fieldClassName)) {
				    	// Listの場合
			    		instance = setFieldLevelList(targetDto, cell, fieldInfo);

			    	} else {
			    		// DTOの場合
						instance = Utils.newInstance(fieldClassName);
					    Utils.setField(targetDto, fieldName, instance);
			    	}

			        dtoStack.push(instance);

			    } else if (cellValue.matches(ANOTHER_SHEET_REGEX)) {
					// 別シート参照のDTOを設定する場合

			    	if (fieldClassName.endsWith("[]")) {
				    	// 配列の場合
			    		setFieldAnotherSheetArray(targetDto, dtoAll, sheetMap, cell, fieldInfo);

			    	} else if (Utils.isList(fieldClassName)) {
				    	// Listの場合
			    		setFieldAnotherSheetList(targetDto, dtoAll, sheetMap, cell, fieldInfo);

			    	} else if (Utils.isMap(fieldClassName)) {
				    	// Mapの場合
				    	setFieldAnotherSheetMap(targetDto, dtoAll, sheetMap, cell, fieldInfo);

			    	} else {
					    // DTOの場合
				    	setFieldAnotherSheetDto(targetDto, dtoAll, sheetMap, cell, fieldInfo);
			    	}

			    } else if (Utils.isList(fieldClassName)) {
			    	// Listにプリミティブ相当の値を追加する場合
			    	setFieldList(targetDto, cell, fieldInfo);

			    } else if (fieldClassName.endsWith("[]")) {
			    	// 配列にプリミティブ相当の値を追加する場合
		    		setFieldArray(targetDto, cell, fieldInfo);

			    } else {
			    	// プリミティブ相当の値を設定する場合
			    	setFieldPrimitive(targetDto, cell, fieldInfo);
			    }

			    // 階層レベルを前回分として保持
			    preLevel = level;

		    } catch (Exception e) {
		    	throw new CellOperationException("例外発生", cell, cellValue, e);
		    }
		}
	}

	/**
	 * 階層として配列に設定するインスタンスを設定する
	 * DTOに配列が設定されていない場合、配列のインスタンスを生成し、DTOに設定する
	 **/
	private Object setFieldLevelArray(
			Object dto,
			DtoFieldInfo fieldInfo) {

	    String fieldClassName = fieldInfo.getFieldClassName();
	    String fieldName = fieldInfo.getFieldName();

    	// DTOに設定された配列を取得する
    	Object val = getFieldObject(dto, fieldName);

    	// DTOの配列フィールドがNULLの場合、配列を生成し、DTOに設定する
    	if (val == null) {
    		val = Utils.newArrayInstance(fieldClassName);
    		Utils.setField(dto, fieldName, val);
    	}

	    String fieldClass = fieldClassName.replaceAll("\\[\\]", "");
	    Object instance = Utils.newInstance(fieldClass);

    	// 配列に生成したインスタンスを生成する
    	Utils.addArray(dto, fieldClassName, fieldName, instance, val);

	    return instance;

	}

	/**
	 * 階層としてListに設定するインスタンスを設定する
	 * DTOにListが設定されていない場合、Listのインスタンスを生成し、DTOに設定する
	 **/
	@SuppressWarnings("unchecked")
	private Object setFieldLevelList(
			Object dto,
			Cell cell,
			DtoFieldInfo fieldInfo) {

	    String fieldClassName = fieldInfo.getFieldClassName();
	    String fieldName = fieldInfo.getFieldName();

    	// DTOに設定されたListを取得する
    	Object val = getFieldObject(dto, fieldName);

    	if (val == null) {
		    val = Utils.newInstance(fieldClassName.replaceAll("<.*>", ""));
    		Utils.setField(dto, fieldName, val);
    	}

    	// Listの総称型に定義されたクラスで値を取得
	    Matcher matcher = CELL_LIST_TYPE.matcher(fieldClassName);
	    if (!matcher.find()) {
	    	throw new CellOperationException("Listの型定義誤り", cell, ExcelUtils.getExcelValue(cell));
	    }
	    String listElementType = matcher.group(1);
	    Object instance = Utils.newInstance(listElementType);

	    // DTOをリストに設定する
    	List.class.cast(val).add(instance);

	    return instance;
	}

	/** 配列に別シートで生成されたDTOを設定する **/
	private void setFieldAnotherSheetArray(
			Object dto,
			Map<String, Map<String, Map<String, Object>>> dtoAll,
			Map<String, DtoExcelSheet> sheetMap,
			Cell cell,
			DtoFieldInfo fieldInfo) {

	    String fieldClassName = fieldInfo.getFieldClassName();
	    String fieldName = fieldInfo.getFieldName();

    	// DTOに設定された配列を取得する
    	Object val = getFieldObject(dto, fieldName);

    	// 初回はNullなので、インスタンスを生成し設定しておく
    	if (val == null) {
    		val = Utils.newArrayInstance(fieldClassName);
    		Utils.setField(dto, fieldName, val);
    	}

    	// 別シートで定義されたDTOを取得する
    	Object anotherDto = getAnotherDto(dto, dtoAll, sheetMap, cell, fieldInfo);

    	// 値を追加する新しい配列を生成し追加する
    	Utils.addArray(dto, fieldClassName, fieldName, anotherDto, val);
	}

	/** Listに別シートで生成されたDTOを設定する **/
	@SuppressWarnings("unchecked")
	private void setFieldAnotherSheetList(
			Object dto,
			Map<String, Map<String, Map<String, Object>>> dtoAll,
			Map<String, DtoExcelSheet> sheetMap,
			Cell cell,
			DtoFieldInfo fieldInfo) {

	    String fieldClassName = fieldInfo.getFieldClassName();
	    String fieldName = fieldInfo.getFieldName();

    	// DTOに設定されたListを取得する
    	Object val = getFieldObject(dto, fieldName);

    	// 初回はNullなので、インスタンスを生成し設定しておく
    	if (val == null) {
		    val = Utils.newInstance(fieldClassName.replaceAll("<.*>", ""));
    		Utils.setField(dto, fieldName, val);
    	}

    	// 別シートで定義されたDTOを取得する
    	Object anotherDto = getAnotherDto(dto, dtoAll, sheetMap, cell, fieldInfo);

	    // DTOをリストに設定する
    	List.class.cast(val).add(anotherDto);
	}

	/** Mapのフィールドに値を設定する **/
	/** Mapは必ず別シートを参照する **/
	@SuppressWarnings("unchecked")
	private void setFieldAnotherSheetMap(
			Object dto,
			Map<String, Map<String, Map<String, Object>>> dtoAll,
			Map<String, DtoExcelSheet> sheetMap,
			Cell cell,
			DtoFieldInfo fieldInfo) {

	    String fieldClassName = fieldInfo.getFieldClassName();
	    String fieldName = fieldInfo.getFieldName();

    	// DTOに設定されたMapを取得する
    	Object val = getFieldObject(dto, fieldName);

    	// 初回はNullなので、インスタンスを生成し設定しておく
    	if (val == null) {
		    val = Utils.newInstance(fieldClassName.replaceAll("<.*>", ""));
    		Utils.setField(dto, fieldName, val);
    	}

    	// 別シートで定義されたDTOを取得する
    	Object anotherDto = getAnotherDto(dto, dtoAll, sheetMap, cell, fieldInfo);

    	// DTOに設定したMapに、別シートで定義されたMapの要素を追加する
    	Map.class.cast(val).putAll(Map.class.cast(anotherDto));
	}

	/** 別シート参照のDTOを生成し、値を設定する **/
	private void setFieldAnotherSheetDto(
			Object dto,
			Map<String, Map<String, Map<String, Object>>> dtoAll,
			Map<String, DtoExcelSheet> sheetMap,
			Cell cell,
			DtoFieldInfo fieldInfo) {

    	// 別シートで定義されたDTOを取得する
		Object anotherDto = getAnotherDto(dto, dtoAll, sheetMap, cell, fieldInfo);

	    Utils.setField(dto, fieldInfo.getFieldName(), anotherDto);
	}

	/** 配列にプリミティブ相当の値を追加する **/
	private void setFieldArray(
			Object dto,
			Cell cell,
			DtoFieldInfo fieldInfo) {

	    String fieldClassName = fieldInfo.getFieldClassName();
	    String fieldName = fieldInfo.getFieldName();

    	// DTOに設定された配列を取得する
    	Object val = getFieldObject(dto, fieldName);

    	if (val == null) {
    		val = Utils.newArrayInstance(fieldClassName);
    		Utils.setField(dto, fieldName, val);
    	}

	    String arrayElementType = fieldClassName.replaceAll("\\[\\]", "");
    	Object value = ExcelUtils.getExcelValueForDto(cell, arrayElementType);
    	Utils.addArray(dto, fieldClassName, fieldName, value, val);
	}

	/** Listにプリミティブ相当の値を追加する **/
	@SuppressWarnings("unchecked")
	private void setFieldList(
			Object dto,
			Cell cell,
			DtoFieldInfo fieldInfo) {

    	// Listのセルの値を取得
	    String cellValue = ExcelUtils.getExcelValue(cell);

	    String fieldClassName = fieldInfo.getFieldClassName();
	    String fieldName = fieldInfo.getFieldName();

    	// DTOに設定されたListを取得する
    	Object val = getFieldObject(dto, fieldName);

    	if (val == null) {
		    val = Utils.newInstance(fieldClassName.replaceAll("<.*>", ""));
    		Utils.setField(dto, fieldName, val);
    	}

    	// Listの総称型に定義されたクラスで値を取得
	    Matcher matcher = CELL_LIST_TYPE.matcher(fieldClassName);
	    if (!matcher.find()) {
	    	throw new CellOperationException("Listの型定義誤り", cell, cellValue);
	    }
	    String listElementType = matcher.group(1);
    	Object value = ExcelUtils.getExcelValueForDto(cell, listElementType);

	    // DTOをリストに設定する
    	List.class.cast(val).add(value);
	}

	/** プリミティブ相当の値をDTOに設定する **/
	private void setFieldPrimitive(
			Object dto,
			Cell cell,
			DtoFieldInfo fieldInfo) {

	    String fieldClassName = fieldInfo.getFieldClassName();
	    String fieldName = fieldInfo.getFieldName();

	    // セルの値からDTOに設定するオブジェクトを取得する
	    Object val = ExcelUtils.getExcelValueForDto(cell, fieldClassName);
	    System.out.println(val + "\t" + fieldName + "\t" + fieldClassName);

	    // DTOに設定する
	    Utils.setField(dto, fieldName, val);
	}

	/** 別シートのDTOを取得する。未作成のシートであれば、この時に作成する **/
	private Object getAnotherDto(
			Object dto,
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
    	// 別シートで定義されたDTOを取得する
    	Object anotherDto = dtoAll.get(anotherSheetName).get(anotherTestNo).get(anotherTuban);
    	return anotherDto;
	}

	/** DTOのフィールドの値を取得する **/
	private Object getFieldObject(Object dto, String fieldName) {
		return Utils.getFieldObject(dto, fieldName);
	}

}
