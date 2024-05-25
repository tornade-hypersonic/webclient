package junithelperv2.excel;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import junithelperv2.DtoFieldInfo;
import junithelperv2.DtoInfo;
import junithelperv2.Utils;

public class DtoExcelSheet {

	public static void main(String[] args) throws Exception {

		// Excelファイルへアクセス
		String path = "data/test/junithelperv2/ContractDto.xlsx";
		Workbook excel = WorkbookFactory.create(new File(path));
		System.out.println(excel);

		int numberOfSheets = excel.getNumberOfSheets();
		System.out.println(numberOfSheets);

	    // シート名を取得
	    DtoExcelSheet dtoExcelSheet = new DtoExcelSheet(excel.getSheet("プルダウンmap"));
	    dtoExcelSheet.createDtoInfo();
	}

	private Sheet sheet;
	private DtoInfo dtoInfo;

	/** Noの列 **/
	public static final int POS_FIELD_NO_COL = 1;

	/** カラム物理名の列 **/
	public static final int POS_FIELD_NAME_COL = 3;

	/** 型の列 **/
	public static final int POS_FIELD_CLASS_NAME_COL = 4;

	/** 階層の列 **/
	public static final int POS_FIELD_LEVEL_COL = 5;

	/** 試験No行 **/
	public static final int POS_TEST_NO_ROW = 3;
	public static final int POS_TEST_NO_ROW_JAVA = POS_TEST_NO_ROW - 1;

	/** 通番行 **/
	public static final int POS_TUBAN_ROW = 4;
	public static final int POS_TUBAN_ROW_JAVA = POS_TUBAN_ROW - 1;

	/** 連番行 **/
	public static final int POS_RENBAN_ROW = 5;
	public static final int POS_RENBAN_ROW_JAVA = POS_RENBAN_ROW - 1;

	/** データの開始行 **/
	public static final int POS_DATA_START_ROW = 6;
	public static final int POS_DATA_START_ROW_JAVA = POS_DATA_START_ROW - 1;

	/** データの開始列 **/
	public static final int POS_DATA_START_COL = 7;
	public static final int POS_DATA_START_COL_JAVA = POS_DATA_START_COL - 1;

	/** コンストラクタ **/
	public DtoExcelSheet(Sheet sheet) {
		this.sheet = sheet;
		this.dtoInfo = createDtoInfo();
	}

	/** DTO情報生成 **/
	private DtoInfo createDtoInfo() {

		DtoInfo dtoInfo = new DtoInfo();

		try {
			// 変数名情報取得・設定
			List<DtoFieldInfo> fields = getColumnList();
			dtoInfo.setDtoFieldInfo(fields);

		    // DTOクラス名取得
		    String dtoClassName = getDtoClassName();
		    dtoInfo.setClassName(dtoClassName);
		    if (StringUtils.isEmpty(dtoClassName)) {
				// クラス名が存在しない場合、当該シートは無効とする
		    	System.out.println("クラス名が定義されてないシート「" + sheet.getSheetName() + "」" );
		    	return null;
		    }
		} catch (Exception e) {
			// 例外発生時、当該シートは無効とする
			System.out.println(e.getMessage() + "「" + sheet.getSheetName() + "」");
			return null;
		}

		if (Utils.isMap(dtoInfo.getClassName())) {
			loadMapData(dtoInfo);
			return dtoInfo;
		}

	    // 全データ格納用マップ
	    Map<String, Map<String, List<List<Cell>>>> dtoDatas = new LinkedHashMap<>();
	    Map<String, List<List<Cell>>> dtoData = new LinkedHashMap<>();

	    int colCnt = POS_DATA_START_COL_JAVA;
	    int startRow = POS_DATA_START_ROW_JAVA;
	    String preTestNo = "";
	    String preTuban = "";

	    List<List<Cell>> tubanList = new ArrayList<List<Cell>>();

	    // 列でループ
	    while (true) {

		    List<Cell> cells = new ArrayList<Cell>();

		    // 試験No取得
		    String testNo = getTestNoRow().getCell(colCnt).getStringCellValue();
		    String tuban = getTubanRow().getCell(colCnt).getStringCellValue();

		    // 試験Noが異なる場合、通番Mapを初期化
		    if (!testNo.equals(preTestNo)) {
		    	preTestNo = testNo;
		    	dtoData = new HashMap<>();
		    	dtoDatas.put(testNo, dtoData);
		    	preTuban = "";
		    }
		    // 通番が異なる場合、連番リストを初期化
		    if (!tuban.equals(preTuban)) {
		    	preTuban = tuban;
		    	tubanList = new ArrayList<List<Cell>>();
		    }
		    tubanList.add(cells);
		    dtoData.put(tuban, tubanList);

		    // 行でループする
		    int rowCnt = startRow;
		    while (true) {
			    // セルの値を取得する
			    Cell cell = getCellPoi(rowCnt, colCnt);
			    cells.add(cell);
			    System.out.println(cell.toString());
		    	if (sheet.getRow(rowCnt + 1) == null) {
			    	System.out.println("行ループ終了");
			    	break;
		    	}
		    	rowCnt++;
		    }
		    // 次の列のセルがなかったら終了する
		    if (getTestNoRow().getCell(colCnt + 1) == null) {
		    	System.out.println("列ループ終了");
		    	break;
		    }

	    	colCnt++;
	    }
	    dtoInfo.setDtoDatas(dtoDatas);
	    System.out.println("★ dtoDatas=" + dtoDatas);

	    return dtoInfo;

	}

	private void loadMapData(DtoInfo dtoInfo) {

	    // 全データ格納用マップ
	    Map<String, Map<String, List<List<Cell>>>> dtoDatas = new LinkedHashMap<>();
	    Map<String, List<List<Cell>>> dtoData = new LinkedHashMap<>();

	    int colCnt = POS_DATA_START_COL_JAVA;
	    int startRow = POS_DATA_START_ROW_JAVA;
	    String preTestNo = "";
	    String preTuban = "";

	    List<List<Cell>> tubanList = new ArrayList<List<Cell>>();

	    // 列でループ
	    while (true) {

		    List<Cell> keyCells = new ArrayList<Cell>();
		    List<Cell> valCells = new ArrayList<Cell>();

		    // 試験No取得
		    String testNo = getTestNoRow().getCell(colCnt).getStringCellValue();
		    String tuban = getTubanRow().getCell(colCnt).getStringCellValue();

		    // 試験Noが異なる場合、通番Mapを初期化
		    if (!testNo.equals(preTestNo)) {
		    	preTestNo = testNo;
		    	dtoData = new HashMap<>();
		    	dtoDatas.put(testNo, dtoData);
		    	preTuban = "";
		    }
		    // 通番が異なる場合、連番リストを初期化
		    if (!tuban.equals(preTuban)) {
		    	preTuban = tuban;
		    	tubanList = new ArrayList<List<Cell>>();
		    }
		    tubanList.add(keyCells);
		    tubanList.add(valCells);
		    dtoData.put(tuban, tubanList);

		    // 行でループする
		    int rowCnt = startRow;
		    while (true) {
			    // セルの値を取得する
			    Cell keyCell = getCellPoi(rowCnt, colCnt);
			    Cell valCell = getCellPoi(rowCnt, colCnt + 1);
			    keyCells.add(keyCell);
			    valCells.add(valCell);
		    	if (sheet.getRow(rowCnt + 1) == null ||
		    			StringUtils.isEmpty(sheet.getRow(rowCnt + 1).getCell(colCnt).toString())) {
			    	System.out.println("行ループ終了");
			    	break;
		    	}
		    	rowCnt++;
		    }
		    // 次の列のセルがなかったら終了する
		    if (getTestNoRow().getCell(colCnt + 2) == null) {
		    	System.out.println("列ループ終了");
		    	break;
		    }

	    	colCnt = colCnt + 2;
	    }
	    dtoInfo.setDtoDatas(dtoDatas);
	    System.out.println("★ Map dtoDatas=" + dtoDatas);

	}


	/** Dtoシートが無効か？ Dtoが作成できなかったら無効 **/
	public boolean isDtoSheet() {
		return dtoInfo != null;
	}

	/** Dto情報取得 **/
	public DtoInfo getDtoInfo() {
		return dtoInfo;
	}

	/** Excelに合わせたセルを取得 **/
	public Cell getCell(int row, int col) {
		return sheet.getRow(row - 1).getCell(col - 1);
	}

	/** poiに合わせたセルを取得 **/
	public Cell getCellPoi(int row, int col) {
		return sheet.getRow(row).getCell(col);
	}

	/** DTOクラス名を取得 **/
	public String getDtoClassName() {
		return getCell(2, 3).getStringCellValue();
	}

	/** 試験No行取得 **/
	public Row getTestNoRow() {
		return sheet.getRow(POS_TEST_NO_ROW_JAVA);
	}

	/** 通番行取得 **/
	public Row getTubanRow() {
		return sheet.getRow(POS_TUBAN_ROW_JAVA);
	}

	/** 最終行取得 **/
	public int getLastRowNum() {
		return sheet.getLastRowNum();
	}

	/** カラム定義情報を取得するインデックスを返す **/
	public int getColumnInfosIndex(int rowCnt) {
		return rowCnt - POS_DATA_START_ROW_JAVA;
	}

	/** まだ行が存在するか **/
	public boolean hasRow(int rowCnt) {
		if (sheet.getRow(rowCnt) == null) {
			return false;
		}
		return true;
	}

	/** まだ列が存在するか **/
	public boolean hasCol(int colCnt) {
		if (getTestNoRow().getCell(colCnt) == null) {
			return false;
		}
		return true;
	}

	/** カラム情報取得 **/
	public List<DtoFieldInfo> getColumnList() {

	    List<DtoFieldInfo> fields = new ArrayList<>();
	    for (int row = POS_DATA_START_ROW_JAVA; hasRow(row); row++) {

	    	// Noの項目がない場合、読込み終了する
		    Cell noCell = getCell(row + 1, POS_FIELD_NO_COL);
		    if (noCell == null) {
		    	break;
		    }

		    String fieldName = getCell(row + 1, POS_FIELD_NAME_COL).getStringCellValue();
		    String fieldClassName = getCell(row + 1, POS_FIELD_CLASS_NAME_COL).getStringCellValue();
		    int level = (int) ExcelUtils.getExcelValueForDto(
		    						getCell(row + 1, POS_FIELD_LEVEL_COL), "int");

		    DtoFieldInfo dtoFieldInfo = new DtoFieldInfo();
		    dtoFieldInfo.setFieldName(fieldName);
		    dtoFieldInfo.setFieldClassName(fieldClassName);
		    dtoFieldInfo.setLevel(level);
		    fields.add(dtoFieldInfo);
	    }
	    fields.stream().forEach(s -> System.out.println(s));
	    return fields;
	}

}
