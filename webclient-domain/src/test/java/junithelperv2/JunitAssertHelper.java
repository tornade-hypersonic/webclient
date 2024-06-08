package junithelperv2;

import static org.junit.Assert.*;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import junithelperv2.excel.CellOperationException;
import junithelperv2.excel.ExcelLoader;
import junithelperv2.excel.ExcelUtils;
import junithelperv2.exceldata.DtoFieldInfo;
import junithelperv2.exceldata.ExcelData;
import junithelperv2.exceldata.SheetData;
import junithelperv2.targetdto.AddressDto;
import junithelperv2.targetdto.AssertDummy;

public class JunitAssertHelper {

	private static final Logger logger = LoggerFactory.getLogger(JunitAssertHelper.class);

	public static void main(String[] args) {
		AssertDummy dummy = new AssertDummy();
		AddressDto addressDto = dummy.createAddressDto();

		JunitAssertHelper asserter = new JunitAssertHelper("data/test/junithelperv2/ContractDto.xlsx");
		asserter.assertDto(addressDto, "addressInfo", "99-1", "2");

	}

	private static String ANOTHER_SHEET_REGEX = "\\[(.*)\\]\\[(.*)\\]\\[(.*)\\]";
	private static Pattern CELL_ANOTHER_SHEET = Pattern.compile(ANOTHER_SHEET_REGEX);

	/** 1Excelのシート **/
	ExcelData excelData;

	public JunitAssertHelper(String excelFileName) {
		excelData = ExcelLoader.loadExcelData(excelFileName);
	}

	public void assertDto(Object targetDto, String sheetName, String testNo, String tuban) {

		// エクセルシート生成
		SheetData sheetData = excelData.getSheetData(sheetName);

		List<DtoFieldInfo> dtoFieldInfos = sheetData.getDtoFieldInfo();
		List<List<Cell>> renbanList = sheetData.getDtoDatas().get(testNo).get(tuban);

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
		    int level = fieldInfo.getLevel();

		    String cellValue = ExcelUtils.getExcelValue(cell);

		    try {

			    // TODO 後で削除する
			    if ("serviceInfoListLevel".equals(fieldName)) {
			    	logger.debug("デバッグ用");
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
			    
//			    Field field = ClassUtils.loadFiled(targetDto).get(fieldName);
			    Field field = ClassFieldUtils.getField(targetDto.getClass(), fieldName);
			    if (Objects.isNull(field)) {
			    	throw new CellOperationException("フィールド名がDTOに存在しない可能性があります", cell, cellValue);
			    }

			    if (cellValue.matches(ANOTHER_SHEET_REGEX)) {
					// 別シート参照のDTOを設定する場合
	
			    	if (field.getType().isArray()) {
				    	// 配列の場合
			    		assertAnotherSheetArray(targetDto, fieldInfo, renbanList, itemIndex);
	
			    	} else if (List.class.isAssignableFrom(field.getType())) {
				    	// Listの場合
			    		assertAnotherSheetList(targetDto, fieldInfo, renbanList, itemIndex);
	
			    	} else if (Map.class.isAssignableFrom(field.getType())) {
				    	// Mapの場合
				    	assertAnotherSheetMap(targetDto, cell, fieldInfo);
	
			    	} else {
					    // DTOの場合
			    		assertAnotherSheetDto(targetDto, cell, fieldInfo);
	
			    	}

			    } else if (field.getType().isArray()) {
			    	// 配列の場合
			    	
			    	if ("[new]".equals(cellValue)) {
				    	// DTO配列の場合
			    		
		            	// 子階層のアサート 連番のデータもここで設定する
			    		int assertLineCount = appendRenbanItems(targetDto, fieldInfo, fields, field, renbanList, itemIndex);
				        // 子階層の行数をスキップ
				    	itemIndex = itemIndex + assertLineCount;
				    	
			    	} else {
			    		
				    	// 配列にプリミティブ相当の値を追加する場合
				    	assertArray(targetDto, cell, fieldInfo, field, renbanList, itemIndex);
			    	}

			    } else if (List.class.isAssignableFrom(field.getType())) {
			    	// リストの場合
			    	
			    	
			    	if ("[new]".equals(cellValue)) {
				    	// DTO配列の場合
			    		
		            	// 子階層のアサート 連番のデータもここで設定する
			    		int assertLineCount = appendRenbanItems(targetDto, fieldInfo, fields, field, renbanList, itemIndex);
				        // 子階層の行数をスキップ
				    	itemIndex = itemIndex + assertLineCount;
				    	
			    	} else {
			    		
				    	// Listにプリミティブ相当の値を追加する場合
				    	assertList(targetDto, cell, fieldInfo, field, renbanList, itemIndex);
			    	}

			    } else if ("[new]".equals(cellValue)) {
				    // インスタンスを生成する場合
			    	// ※リストと配列はその前のif文で処理される

		    		// DTOの場合
					Object instance = Utils.getFieldObject(targetDto, fieldName);

			        dtoStack.push(instance);

			    } else {
			    	// プリミティブ相当の値を設定する場合
			    	assertFieldPrimitive(targetDto, cell, fieldInfo, field);
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
    	SheetData sheetData = excelData.getSheetData(anotherSheetName);

//		Map<String, Map<String, List<List<Cell>>>> datas = sheetData.getDtoDatas();
//		List<List<Cell>> renbanList = datas.get(anotherTestNo).get(anotherTuban);
		List<List<Cell>> renbanList = sheetData.getDtoDatas().get(anotherTestNo).get(anotherTuban);

		// Mapシートは連番リストは2つのみ
		List<Cell> keyCells = renbanList.get(0);
		List<Cell> valCells = renbanList.get(1);

		for (int i = 0; i < keyCells.size(); i++) {
			String key = ExcelUtils.getExcelValue(keyCells.get(i));
			String expected = ExcelUtils.getExcelValue(valCells.get(i));
			String actual = (String) actualMap.get(key);
			logger.info(String.format("【アサート】期待値=[%s], 実際値=[%s]", expected, actual));
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
			Field field,
			List<List<Cell>> renbanList,
			int itemIndex) {

	    String fieldName = fieldInfo.getFieldName();

    	// DTOに設定されたListを取得する
	    List<?> actualList = (List<?>) Utils.getFieldObject(targetDto, fieldName);

	    for (int i = 0; i < actualList.size(); i++) {
			Object actual = actualList.get(i);
			Cell expectedCell = renbanList.get(i).get(itemIndex);

		    String listElementType = ClassFieldUtils.getClassNameByListElement(field);

		    // 検証予測値を取得
			Object expected = ExcelUtils.getExcelValueForDto(expectedCell, listElementType);

		    // アサート
			logger.info(String.format("【アサート】期待値=[%s], 実際値=[%s]", expected, actual));
			assertEquals(expected, actual);
		}
	}

	/** 配列の要素を検証する **/
	private void assertArray(
			Object targetDto,
			Cell cell,
			DtoFieldInfo fieldInfo,
			Field field,
			List<List<Cell>> renbanList,
			int itemIndex) {

	    String arrayElementType = ClassFieldUtils.getClassNameByArrayElement(field);
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
			logger.info(String.format("【アサート】期待値=[%s], 実際値=[%s]", expected, actual));
			assertEquals(expected, actual);
    	}
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
			DtoFieldInfo fieldInfo,
			Field field) {

	    String fieldClassName = field.getType().getName();
	    String fieldName = fieldInfo.getFieldName();

	    // セルの値からDTOに設定するオブジェクトを取得する
	    Object expected = ExcelUtils.getExcelValueForDto(cell, fieldClassName);
	    logger.debug(expected + "\t" + fieldName + "\t" + fieldClassName);

	    // アサート
	    Utils.assertField(dto, fieldName, expected);
	}

	private int appendRenbanItems(
			Object targetDto,
			DtoFieldInfo fieldInfo,
			List<DtoFieldInfo> fields,
			Field field,
			List<List<Cell>> renbanList,
			int itemIndex
			) {
		
		// リストまたは配列を取得
		Object fieldObject = Utils.getFieldObject(targetDto, field.getName());
		Object[] targetChildDtoArray = List.class.isAssignableFrom(field.getType()) ?
											List.class.cast(fieldObject).toArray() :
											(Object[]) fieldObject;
	    
		// 子要素の対象数
		int childLineCount = 0;

	    // 親の階層レベルを取得
	    // ここに到達するのは、親の階層
	    int parentLevel = fieldInfo.getLevel();

	    List<DtoFieldInfo> wFields = new ArrayList<>();
	    List<List<Cell>> wRenbanList = new ArrayList<>();
	    
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
		    childLineCount = wCells.size();

		    // 子要素のアサート
		    assertValue(targetChildDtoArray[renbanCnt], wFields, wRenbanList);

		    wRenbanList.clear();
	    }
	    
	    return childLineCount;
	}
	
	
}
