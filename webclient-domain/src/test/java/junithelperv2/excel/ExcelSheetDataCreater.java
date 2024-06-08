package junithelperv2.excel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import junithelperv2.Utils;
import junithelperv2.exceldata.DtoDataTestNo;
import junithelperv2.exceldata.DtoDataTuban;
import junithelperv2.exceldata.DtoFieldInfo;
import junithelperv2.exceldata.SheetData;

public class ExcelSheetDataCreater {

	private static final Logger logger = LoggerFactory.getLogger(ExcelSheetDataCreater.class);
	
	public static void main(String[] args) throws Exception {

		// Excelファイルへアクセス
		String path = "data/test/junithelperv2/ContractDto.xlsx";
		Workbook excel = WorkbookFactory.create(new File(path));
		logger.debug(Objects.toString(excel));

		int numberOfSheets = excel.getNumberOfSheets();
		logger.debug(Objects.toString(numberOfSheets));

	    // シート名を取得
	    ExcelSheetDataCreater creater = new ExcelSheetDataCreater(excel.getSheet("Sheet1"));
	    SheetData sheetData = creater.createSheetData();
	    logger.debug(Objects.toString(sheetData));
	}

	private ExcelSheetWrapper sheet;

	/** コンストラクタ **/
	public ExcelSheetDataCreater(Sheet _sheet) {
		this.sheet = new ExcelSheetWrapper(_sheet);
	}

	/** メイン処理 **/
	SheetData createSheetData() {

		SheetData sheetData = new SheetData();
		sheetData.setSheetName(sheet.getSheetName());

		try {
			// 変数名情報取得・設定
			List<DtoFieldInfo> fields = getColumnList();
			sheetData.setDtoFieldInfo(fields);

		    // DTOクラス名取得
		    String dtoClassName = sheet.getDtoClassName();
		    sheetData.setClassName(dtoClassName);
		    if (StringUtils.isEmpty(dtoClassName)) {
				// クラス名が存在しない場合、当該シートは無効とする
		    	logger.warn("クラス名が定義されてないシート「" + sheet.getSheetName() + "」" );
		    	return null;
		    }
		} catch (Exception e) {
			// 例外発生時、当該シートは無効とする
//			logger.error(e.getMessage() + "「" + sheet.getSheetName() + "」");
			logger.error("Excelシートが無効 シート名=[{}]", sheet.getSheetName());
			logger.error("", e);
			return null;
		}

		if (Utils.isMap(sheetData.getClassName())) {
			loadMapData(sheetData);
			return sheetData;
		}

	    // 全データ格納用
	    DtoDataTestNo dtoDataTestNo = new DtoDataTestNo();
	    DtoDataTuban dtoDataTuban = new DtoDataTuban();

	    int colCnt = ExcelConst.POS_DATA_START_COL_JAVA;
	    int startRow = ExcelConst.POS_DATA_START_ROW_JAVA;
	    String preTestNo = "";
	    String preTuban = "";

	    List<List<Cell>> tubanList = new ArrayList<List<Cell>>();

	    // 列でループ
	    while (true) {

		    List<Cell> cells = new ArrayList<Cell>();

		    // 試験No取得
		    String testNo = sheet.getTestNoRow().getCell(colCnt).getStringCellValue();
		    String tuban = sheet.getTubanRow().getCell(colCnt).getStringCellValue();

		    // 試験Noが異なる場合、通番Mapを初期化
		    if (!testNo.equals(preTestNo)) {
		    	preTestNo = testNo;
		    	dtoDataTuban = new DtoDataTuban();
		    	dtoDataTestNo.put(testNo, dtoDataTuban);
		    	preTuban = "";
		    }
		    // 通番が異なる場合、連番リストを初期化
		    if (!tuban.equals(preTuban)) {
		    	preTuban = tuban;
		    	tubanList = new ArrayList<List<Cell>>();
		    }
		    tubanList.add(cells);
		    dtoDataTuban.put(tuban, tubanList);

		    // 行でループする
		    int rowCnt = startRow;
		    while (true) {
			    // セルの値を取得する
			    Cell cell = sheet.getCellPoi(rowCnt, colCnt);
			    cells.add(cell);
			    logger.debug(cell.toString());
		    	if (sheet.getRow(rowCnt + 1) == null) {
		    		logger.debug("行ループ終了");
			    	break;
		    	}
		    	rowCnt++;
		    }
		    // 次の列のセルがなかったら終了する
		    if (sheet.getTestNoRow().getCell(colCnt + 1) == null) {
		    	logger.debug("列ループ終了");
		    	break;
		    }

	    	colCnt++;
	    }
	    sheetData.setDtoDatas(dtoDataTestNo);
	    logger.debug("★ dtoDatas=" + dtoDataTestNo);

	    return sheetData;

	}

	private void loadMapData(SheetData sheetData) {

	    // 全データ格納用マップ
	    DtoDataTestNo dtoDataTestNo = new DtoDataTestNo();
	    DtoDataTuban dtoDataTuban = new DtoDataTuban();

	    int colCnt = ExcelConst.POS_DATA_START_COL_JAVA;
	    int startRow = ExcelConst.POS_DATA_START_ROW_JAVA;
	    String preTestNo = "";
	    String preTuban = "";

	    List<List<Cell>> tubanList = new ArrayList<List<Cell>>();

	    // 列でループ
	    while (true) {

		    List<Cell> keyCells = new ArrayList<Cell>();
		    List<Cell> valCells = new ArrayList<Cell>();

		    // 試験No取得
		    String testNo = sheet.getTestNoRow().getCell(colCnt).getStringCellValue();
		    String tuban = sheet.getTubanRow().getCell(colCnt).getStringCellValue();

		    // 試験Noが異なる場合、通番Mapを初期化
		    if (!testNo.equals(preTestNo)) {
		    	preTestNo = testNo;
//		    	dtoData = new HashMap<>();
//		    	dtoDatas.put(testNo, dtoData);
		    	dtoDataTuban = new DtoDataTuban();
		    	dtoDataTestNo.put(testNo, dtoDataTuban);
		    	preTuban = "";
		    }
		    // 通番が異なる場合、連番リストを初期化
		    if (!tuban.equals(preTuban)) {
		    	preTuban = tuban;
		    	tubanList = new ArrayList<List<Cell>>();
		    }
		    tubanList.add(keyCells);
		    tubanList.add(valCells);
		    dtoDataTuban.put(tuban, tubanList);

		    // 行でループする
		    int rowCnt = startRow;
		    while (true) {
			    // セルの値を取得する
			    Cell keyCell = sheet.getCellPoi(rowCnt, colCnt);
			    Cell valCell = sheet.getCellPoi(rowCnt, colCnt + 1);
			    keyCells.add(keyCell);
			    valCells.add(valCell);
		    	if (sheet.getRow(rowCnt + 1) == null ||
		    			StringUtils.isEmpty(sheet.getRow(rowCnt + 1).getCell(colCnt).toString())) {
		    		logger.debug("行ループ終了");
			    	break;
		    	}
		    	rowCnt++;
		    }
		    // 次の列のセルがなかったら終了する
		    if (sheet.getTestNoRow().getCell(colCnt + 2) == null) {
		    	logger.debug("列ループ終了");
		    	break;
		    }

	    	colCnt = colCnt + 2;
	    }
	    sheetData.setDtoDatas(dtoDataTestNo);
	    logger.debug("★ Map dtoDatas=" + dtoDataTestNo);

	}

	/** カラム情報取得 **/
	public List<DtoFieldInfo> getColumnList() {

	    List<DtoFieldInfo> fields = new ArrayList<>();
	    for (int row = ExcelConst.POS_DATA_START_ROW_JAVA; sheet.hasRow(row); row++) {

	    	// Noの項目がない場合、読込み終了する
		    Cell noCell = sheet.getCell(row + 1, ExcelConst.POS_FIELD_NO_COL);
		    if (noCell == null) {
		    	break;
		    }

		    String fieldName = sheet.getCell(row + 1, ExcelConst.POS_FIELD_NAME_COL).getStringCellValue();
		    String fieldClassName = sheet.getCell(row + 1, ExcelConst.POS_FIELD_CLASS_NAME_COL).getStringCellValue();
		    int level = (int) ExcelUtils.getExcelValueForDto(
		    		sheet.getCell(row + 1, ExcelConst.POS_FIELD_LEVEL_COL), "int");
//		    String levelStr = (String) ExcelUtils.getExcelValue(
//		    		sheet.getCell(row + 1, ExcelConst.POS_FIELD_LEVEL_COL)).replaceAll("\\..*", "");
//		    Integer level = Integer.valueOf(levelStr);

		    DtoFieldInfo dtoFieldInfo = new DtoFieldInfo();
		    dtoFieldInfo.setFieldName(fieldName);
		    dtoFieldInfo.setFieldClassName(fieldClassName);
		    dtoFieldInfo.setLevel(level);
		    fields.add(dtoFieldInfo);
	    }
	    fields.stream().forEach(s -> logger.debug(Objects.toString(s)));
	    return fields;
	}

}
