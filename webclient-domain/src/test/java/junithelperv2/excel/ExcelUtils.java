package junithelperv2.excel;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.Date;
import java.util.Locale;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
//import org.joda.time.DateTime;
//import org.joda.time.format.DateTimeFormat;
//import org.joda.time.format.DateTimeFormatter;

public class ExcelUtils {

	public static void main(String[] args) {
//		DateTimeFormatter DEF_FMT = DateTimeFormat.forPattern("G yyyy-MM-dd(E) aHH:mm:ss.SSS ZZ.");
//		DateTime pd3 = DateTime.parse("2006/01/02 15:04:05.789", DateTimeFormat.forPattern("yyyy/MM/dd HH:mm:ss.SSS"));
//		System.out.println(DEF_FMT.print(pd3));

		// 文字列 → LocalDate
		DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("uuuu/MM/dd")
                .withLocale(Locale.JAPANESE)
                .withResolverStyle(ResolverStyle.STRICT);
		LocalDate localDate = LocalDate.parse("2015/12/25", formatter1);
		System.out.println(localDate.toString());

		// 文字列 → LocalDateTime
		formatter1 = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss.SSS")
                .withLocale(Locale.JAPANESE)
                .withResolverStyle(ResolverStyle.STRICT);
		LocalDateTime localDateTime = LocalDateTime.parse("2006-01-02 15:04:05.789", formatter1);
		System.out.println(localDateTime.toString());
		
		// java.util.Date → LocalDateTime → 文字列
		Date date = new Date();
	    localDateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
		System.out.println(formatter1.format(localDateTime));
	}

	/**
	 * セルの値をDTOに設定する値として取得する
	 */
	public static Object getExcelValueForDto(Cell cell, String fieldClassName) {

		CellType cellType = cell.getCellType();

		switch (cell.getCellType()) {
			case BLANK:
				return null;

			case BOOLEAN:
				return cell.getBooleanCellValue();

			case ERROR:
				throw new CellOperationException("セルがエラーになってます", cell, null);

			case FORMULA:
				throw new CellOperationException("FORMULAはサポート対象外", cell, null);

			case STRING:
				String value = cell.getStringCellValue();

				FieldType fieldType = FieldType.getInstance(fieldClassName);
				switch (fieldType) {
				case STRING:
			    	return value;

				case INTEGER:
				case PRIMITIVE_INT:
			    	return Integer.parseInt(value);

				case LONG:
				case PRIMITIVE_LONG:
			    	return Long.parseLong(value);

				case FLOAT:
				case PRIMITIVE_FLOAT:
			    	return Float.parseFloat(value);

				case DATE_TIME:
			    	return LocalDateTime.parse(value, DateTimeFormatter.ofPattern("uuuu/MM/dd HH:mm:ss.SSS"));
			    default:
					throw new CellOperationException("JunitHelperのサポート対象外", cell, value);
				}

			case NUMERIC:
				if (DateUtil.isCellDateFormatted(cell)) {
				    Date date = cell.getDateCellValue();
				    Instant instant = date.toInstant();
				    return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
				}
				String valueDouble = Double.toString(cell.getNumericCellValue());

				FieldType type = FieldType.getNumberInstance(fieldClassName);

				switch (type) {
				case INTEGER:
			    	return Integer.parseInt(valueDouble);

				case LONG:
			    	return Long.parseLong(valueDouble);

				case FLOAT:
			    	return Float.parseFloat(valueDouble);

				default:
					throw new CellOperationException("JunitHelperのサポート対象外", cell, valueDouble);
				}

			default:
				break;
		}

		throw new CellOperationException("JunitHelperのサポート対象外 cell-type=[" + cellType + "]", cell, null);
	}

	/**
	 * セルの値をStringとして取得する
	 */
	public static String getExcelValue(Cell cell) {

		switch (cell.getCellType()) {
		case BLANK:
			return null;

		case BOOLEAN:
			return String.valueOf(cell.getBooleanCellValue());

		case ERROR:
			throw new CellOperationException("セルがエラーになってます", cell, null);

		case FORMULA:
			throw new CellOperationException("FORMULAはサポート対象外", cell, null);

		case STRING:
			return cell.getStringCellValue();

		case NUMERIC:
			if (DateUtil.isCellDateFormatted(cell)) {
			    Date date = cell.getDateCellValue();
			    DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss.SSS")
		                .withLocale(Locale.JAPANESE)
		                .withResolverStyle(ResolverStyle.STRICT);
			    LocalDateTime localDateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
				System.out.println(formatter1.format(localDateTime));
			     
			}
			return String.valueOf(cell.getNumericCellValue());

		default:
			break;
		}

		throw new CellOperationException("JunitHelperのサポート対象外", cell, null);
	}


}
