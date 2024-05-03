package junithelper;

import static org.junit.Assert.*;

import java.lang.reflect.Array;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;

public class JunitAssertHelper {

	public static void main(String[] args) {
		AssertDummy dummy = new AssertDummy();
		AddressDto addressDto = dummy.createAddressDto();

		JunitAssertHelper asserter = new JunitAssertHelper("data/test/ContractDto.xlsx");
		asserter.assertDto(addressDto, "addressInfo", "99-1", "2");

	}

	private static String ANOTHER_SHEET_REGEX = "\\[(.*)\\]\\[(.*)\\]\\[(.*)\\]";
	private static Pattern CELL_ANOTHER_SHEET = Pattern.compile(ANOTHER_SHEET_REGEX);
	private static Pattern CELL_LIST_TYPE = Pattern.compile(".*<(.*)>");

	/** 1Excelのシート **/
	Map<String, DtoExcelSheet> sheetMap;

	public JunitAssertHelper(String excelFileName) {
		sheetMap = ExcelLoader.loadExcelSheet(excelFileName);
	}

	public void assertDto(Object targetDto, String sheetName, String testNo, String tuban) {

		// エクセルシート生成
		DtoExcelSheet sheet = sheetMap.get(sheetName);

		DtoInfo dtoInfo = sheet.getDtoInfo();
		List<DtoFieldInfo> dtoFieldInfos = dtoInfo.getDtoFieldInfo();
		Map<String, Map<String, List<List<Cell>>>> datas = dtoInfo.getDtoDatas();
		List<List<Cell>> renbanList = datas.get(testNo).get(tuban);

		assertValue(targetDto, dtoFieldInfos, renbanList);

	}

	private void assertValue(Object targetRootDto, List<DtoFieldInfo> fields, List<List<Cell>> renbanList) {

		List<Cell> cells = renbanList.get(0);

		int preLevel = 1;	// 前回の階層レベル

		// 設定対象DTO格納スタック
        Deque<Object> dtoStack = new ArrayDeque<>();
        dtoStack.push(targetRootDto);

        // Excelの縦方向にループ
		for (int itemIndex = 0; itemIndex < cells.size(); itemIndex++) {

			Cell cell = cells.get(itemIndex);

		    // Java変数情報
			DtoFieldInfo fieldInfo = fields.get(itemIndex);
		    String fieldName = fieldInfo.getFieldName();
		    String fieldClassName = fieldInfo.getFieldClassName();
		    int level = fieldInfo.getLevel();

		    String cellValue = ExcelUtils.getExcelValue(cell);

		    try {

			    // TODO 後で削除する
			    if ("arrayInteger".equals(fieldName)) {
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

					Object instance = null;

			    	if (fieldClassName.endsWith("[]")) {
				    	// 配列の場合
			    		instance = assertLevelArray(targetDto, fieldInfo);

			    	} else if (Utils.isList(fieldClassName)) {
				    	// Listの場合
			    		instance = assertLevelList(targetDto, fieldInfo);

			    	} else {
			    		// DTOの場合
						instance = Utils.getFieldObject(targetDto, fieldName);
			    	}

			        dtoStack.push(instance);

			    } else if (cellValue.matches(ANOTHER_SHEET_REGEX)) {
					// 別シート参照のDTOを設定する場合

			    	if (fieldClassName.endsWith("[]")) {
				    	// 配列の場合
			    		assertAnotherSheetArray(targetDto, fieldInfo, renbanList, itemIndex);

			    	} else if (Utils.isList(fieldClassName)) {
				    	// Listの場合
			    		assertAnotherSheetList(targetDto, fieldInfo, renbanList, itemIndex);

			    	} else if (Utils.isMap(fieldClassName)) {
				    	// Mapの場合
				    	assertAnotherSheetMap(targetDto, cell, fieldInfo);

			    	} else {
					    // DTOの場合
			    		assertAnotherSheetDto(targetDto, cell, fieldInfo);

			    	}

			    } else if (Utils.isList(fieldClassName)) {
			    	// Listにプリミティブ相当の値を追加する場合
			    	assertList(targetDto, cell, fieldInfo, renbanList, itemIndex);

			    } else if (fieldClassName.endsWith("[]")) {
			    	// 配列にプリミティブ相当の値を追加する場合
			    	assertArray(targetDto, cell, fieldInfo, renbanList, itemIndex);

			    } else if (targetDto.getClass().isArray()) {
			    	// DTOが配列の場合
			    	int assertLineCount = assertArrayDto(targetDto, cell, fieldInfo, fields, renbanList, itemIndex);
			    	// アサート対象数をスキップする for分で+1となるので-1する
			    	itemIndex = itemIndex + (assertLineCount - 1);

			    } else if (targetDto instanceof List) {
			    	// DTOがリストの場合
			    	int assertLineCount = assertListDto(targetDto, cell, fieldInfo, fields, renbanList, itemIndex);
			    	// アサート対象数をスキップする for分で+1となるので-1する
			    	itemIndex = itemIndex + (assertLineCount - 1);

			    } else {
			    	// プリミティブ相当の値を設定する場合
			    	assertFieldPrimitive(targetDto, cell, fieldInfo);
			    }

			    // 階層レベルを前回分として保持
			    preLevel = level;

		    } catch (Exception e) {
		    	throw new CellOperationException("例外発生", cell, cellValue, e);
		    }
		}
	}

	/** 配列の要素を、別シートで定義された内容で検証する **/
	private void assertAnotherSheetArray(
			Object targetDto,
			DtoFieldInfo fieldInfo,
			List<List<Cell>> renbanList,
			int itemIndex) {

	    String fieldName = fieldInfo.getFieldName();

    	// DTOに設定されたListを取得する
	    Object actualArray = Utils.getFieldObject(targetDto, fieldName);

    	int length = Array.getLength(actualArray);
    	for (int i=0; i<length; i++) {
    	    Object actual = Array.get(actualArray, i);
			Cell expectedCell = renbanList.get(i).get(itemIndex);
			assertAnotherDto(expectedCell, actual);
    	}

	}

	/** Listの要素を、別シートで定義された内容で検証する **/
	private void assertAnotherSheetList(
			Object targetDto,
			DtoFieldInfo fieldInfo,
			List<List<Cell>> renbanList,
			int itemIndex) {

	    String fieldName = fieldInfo.getFieldName();

    	// DTOに設定されたListを取得する
	    List<?> actualList = (List<?>) Utils.getFieldObject(targetDto, fieldName);

	    for (int i = 0; i < actualList.size(); i++) {
			Object actual = actualList.get(i);
			Cell expectedCell = renbanList.get(i).get(itemIndex);
			assertAnotherDto(expectedCell, actual);
		}
	}

	/** Mapの要素を、別シートで定義された内容で検証する **/
	private void assertAnotherSheetMap(
			Object targetDto,
			Cell cell,
			DtoFieldInfo fieldInfo) {

	    String fieldName = fieldInfo.getFieldName();

    	// DTOに設定されたMapを取得する
	    Map<?,?> actualMap = (Map<?,?>) Utils.getFieldObject(targetDto, fieldName);

	    // MapはrenbanListの先頭にのみ設定される
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

		// Mapのシートを取得
		DtoExcelSheet sheet = sheetMap.get(anotherSheetName);

		DtoInfo dtoInfo = sheet.getDtoInfo();
		Map<String, Map<String, List<List<Cell>>>> datas = dtoInfo.getDtoDatas();
		List<List<Cell>> renbanList = datas.get(anotherTestNo).get(anotherTuban);

		// Mapシートは連番リストは2つのみ
		List<Cell> keyCells = renbanList.get(0);
		List<Cell> valCells = renbanList.get(1);

		for (int i = 0; i < keyCells.size(); i++) {
			String key = ExcelUtils.getExcelValue(keyCells.get(i));
			String expected = ExcelUtils.getExcelValue(valCells.get(i));
			String actual = (String) actualMap.get(key);
			System.out.println(String.format("【アサート】期待値=[%s], 実際値=[%s]", expected, actual));
			assertEquals(expected, actual);
		}

	}

	/** DTOを別シートで定義された内容で検証する **/
	private void assertAnotherSheetDto(
			Object targetDto,
			Cell cell,
			DtoFieldInfo fieldInfo) {

	    String fieldName = fieldInfo.getFieldName();
	    Object actual = Utils.getFieldObject(targetDto, fieldName);

	    assertAnotherDto(cell, actual);

	}

	/** Listの要素を検証する **/
	private void assertList(
			Object targetDto,
			Cell cell,
			DtoFieldInfo fieldInfo,
			List<List<Cell>> renbanList,
			int itemIndex) {

    	// Listのセルの値を取得
	    String cellValue = ExcelUtils.getExcelValue(cell);

	    String fieldClassName = fieldInfo.getFieldClassName();
	    String fieldName = fieldInfo.getFieldName();

    	// DTOに設定されたListを取得する
	    List<?> actualList = (List<?>) Utils.getFieldObject(targetDto, fieldName);

	    for (int i = 0; i < actualList.size(); i++) {
			Object actual = actualList.get(i);
			Cell expectedCell = renbanList.get(i).get(itemIndex);

	    	// Listの総称型に定義されたクラスで値を取得
		    Matcher matcher = CELL_LIST_TYPE.matcher(fieldClassName);
		    if (!matcher.find()) {
		    	throw new CellOperationException("Listの型定義誤り", cell, cellValue);
		    }
		    String listElementType = matcher.group(1);

		    // 検証予測値を取得
			Object expected = ExcelUtils.getExcelValueForDto(expectedCell, listElementType);

		    // アサート
			System.out.println(String.format("【アサート】期待値=[%s], 実際値=[%s]", expected, actual));
			assertEquals(expected, actual);
		}
	}

	/** 配列の要素を検証する **/
	private void assertArray(
			Object targetDto,
			Cell cell,
			DtoFieldInfo fieldInfo,
			List<List<Cell>> renbanList,
			int itemIndex) {

	    String fieldClassName = fieldInfo.getFieldClassName();
	    String arrayElementType = fieldClassName.replaceAll("\\[\\]", "");
	    String fieldName = fieldInfo.getFieldName();

    	// DTOに設定された配列を取得する
		Object array = Utils.getFieldObject(targetDto, fieldName);
		if (!array.getClass().isArray()) {
			throw new RuntimeException("配列でないオブジェクト");
		}

    	int length = Array.getLength(array);
    	for (int i = 0; i < length; i++) {
    	    Object actual = Array.get(array, i);

		    // 検証予測値を取得
			Cell expectedCell = renbanList.get(i).get(itemIndex);
			Object expected = ExcelUtils.getExcelValueForDto(expectedCell, arrayElementType);

		    // アサート
			System.out.println(String.format("【アサート】期待値=[%s], 実際値=[%s]", expected, actual));
			assertEquals(expected, actual);
    	}
	}

	/** DTO配列の要素を検証する **/
	private int assertArrayDto(
			Object targetDtoArray,
			Cell cell,
			DtoFieldInfo fieldInfo,
			List<DtoFieldInfo> fields,
			List<List<Cell>> renbanList,
			int itemIndex) {

		// アサートの対象数
		int assertLineCount = 0;

	    // 親の階層レベルを取得
	    // ここに到達するのは、配列で階層レベルが変わった最初の項目
	    int parentLevel = fieldInfo.getLevel() - 1;

	    List<DtoFieldInfo> wFields = new ArrayList<>();
	    List<List<Cell>> wRenbanList = new ArrayList<>();

	    // targetDtoArrayのfieldsと、renbanListを抜き出す
	    // 階層が変わった部分のみwRenbanListに設定する
	    // ここで先に進めた分を戻り値で返却し、二重アサートを防ぐ
	    for (int renbanCnt = 0; renbanCnt < renbanList.size(); renbanCnt++) {
	    	List<Cell> cells = renbanList.get(renbanCnt);
			boolean first = true;
		    List<Cell> wCells = new ArrayList<>();
		    for (int i = itemIndex; i < fields.size(); i++) {
		    	DtoFieldInfo dtoFieldInfo = fields.get(i);
		    	if (parentLevel == dtoFieldInfo.getLevel()) {
		    		break;
		    	}
		    	if (first) {
			    	wFields.add(fields.get(i));
		    	}
		    	wCells.add(cells.get(i));
	    	}
		    first = false;
		    wRenbanList.add(wCells);
		    assertLineCount = wCells.size();

		    // アサート（再帰処理）
    	    Object actual = Array.get(targetDtoArray, renbanCnt);
    	    assertValue(actual, wFields, wRenbanList);

		    wRenbanList.remove(0);
		}
	    return assertLineCount;
	}

	/** DTOを要素とするListを検証する **/
	private int assertListDto(
			Object targetDtoList,
			Cell cell,
			DtoFieldInfo fieldInfo,
			List<DtoFieldInfo> fields,
			List<List<Cell>> renbanList,
			int itemIndex) {

		// アサートの対象数
		int assertLineCount = 0;

		List<?> target = (List<?>) targetDtoList;

	    // 親の階層レベルを取得
	    // ここに到達するのは、配列で階層レベルが変わった最初の項目
	    int parentLevel = fieldInfo.getLevel() - 1;

	    List<DtoFieldInfo> wFields = new ArrayList<>();
	    List<List<Cell>> wRenbanList = new ArrayList<>();

	    // targetDtoArrayのfieldsと、renbanListを抜き出す
	    // 階層が変わった部分のみwRenbanListに設定する
	    // ここで先に進めた分を戻り値で返却し、二重アサートを防ぐ
	    for (int renbanCnt = 0; renbanCnt < renbanList.size(); renbanCnt++) {
	    	List<Cell> cells = renbanList.get(renbanCnt);
			boolean first = true;
		    List<Cell> wCells = new ArrayList<>();
		    for (int i = itemIndex; i < fields.size(); i++) {
		    	DtoFieldInfo dtoFieldInfo = fields.get(i);
		    	if (parentLevel == dtoFieldInfo.getLevel()) {
		    		break;
		    	}
		    	if (first) {
			    	wFields.add(fields.get(i));
		    	}
		    	wCells.add(cells.get(i));
	    	}
		    first = false;
		    wRenbanList.add(wCells);
		    assertLineCount = wCells.size();

		    // アサート（再帰処理）
    	    Object actual = target.get(renbanCnt);
    	    assertValue(actual, wFields, wRenbanList);

		    wRenbanList.remove(0);
		}
		return assertLineCount;
	}

	/**
	 * DTOのフィールドが配列の場合
	 **/
	private Object assertLevelArray(
			Object targetDto,
			DtoFieldInfo fieldInfo) {

	    String fieldName = fieldInfo.getFieldName();

    	// DTOに設定された配列を取得する
		Object array = Utils.getFieldObject(targetDto, fieldName);
		if (!array.getClass().isArray()) {
			throw new RuntimeException("配列でないオブジェクト");
		}

	    return array;

	}

	/**
	 * DTOのフィールドがListの場合
	 **/
	private Object assertLevelList(
			Object targetDto,
			DtoFieldInfo fieldInfo) {

	    String fieldName = fieldInfo.getFieldName();

    	// DTOに設定された配列を取得する
		Object list = Utils.getFieldObject(targetDto, fieldName);
		if (!(list instanceof List)) {
			throw new RuntimeException("リストでないオブジェクト");
		}

	    return list;

	}









	/** 別シートで定義された内容でDTOを検証する **/
	private void assertAnotherDto(Cell cell, Object actual) {
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

    	assertDto(actual, anotherSheetName, anotherTestNo, anotherTuban);
	}


	/** プリミティブ相当の値をアサートする **/
	private void assertFieldPrimitive(
			Object dto,
			Cell cell,
			DtoFieldInfo fieldInfo) {

	    String fieldClassName = fieldInfo.getFieldClassName();
	    String fieldName = fieldInfo.getFieldName();

	    // セルの値からDTOに設定するオブジェクトを取得する
	    Object expected = ExcelUtils.getExcelValueForDto(cell, fieldClassName);
	    System.out.println(expected + "\t" + fieldName + "\t" + fieldClassName);

	    // アサート
	    Utils.assertField(dto, fieldName, expected);
	}

}
