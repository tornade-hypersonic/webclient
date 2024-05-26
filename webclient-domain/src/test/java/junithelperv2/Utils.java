package junithelperv2;

import static org.junit.Assert.*;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;

import junithelperv2.excel.FieldType;

public class Utils {

	private static final Logger logger = LoggerFactory.getLogger(Utils.class);
	
	/** 配列を生成する **/
	public static Object newArrayInstance(String fieldClassName) {

		Object val = null;

	    String arrayElementType = fieldClassName.replaceAll("\\[\\]", "");

    	FieldType fieldType = FieldType.getPrimitiveInstance(arrayElementType);
    	if (fieldType == null) {
			try {
				return Array.newInstance(Class.forName(arrayElementType), 0);
			} catch (NegativeArraySizeException | ClassNotFoundException e) {
				throw new RuntimeException(e);
			}
    	}

    	switch (fieldType) {
    	case PRIMITIVE_INT:
			val = new int[0];
			break;
    	case PRIMITIVE_LONG:
			val = new long[0];
			break;
    	case PRIMITIVE_FLOAT:
			val = new float[0];
			break;
    	default:
			break;
    	}

	    return val;
	}

	/** 配列に要素を追加する **/
	public static void addArray(
			Object dto, String fieldClassName, String fieldName, Object addValue, Object array) {

		if (!array.getClass().isArray()) {
			throw new RuntimeException("配列でないオブジェクト");
		}

	    String arrayElementType = fieldClassName.replaceAll("\\[\\]", "");

    	int length = Array.getLength(array);

    	FieldType fieldType = FieldType.getPrimitiveInstance(arrayElementType);

    	if (fieldType == null) {
    		// その他のタイプ
	    	Object newArray = Array.newInstance(addValue.getClass(), length + 1);

	    	for (int i=0; i<length; i++) {
	    	    Object elem = Array.get(array, i);
	    	    Array.set(newArray, i, elem);
	    	}
		    Array.set(newArray, length, addValue);
    		setField(dto, fieldName, newArray);
    		return;
    	}

    	switch (fieldType) {
    	case PRIMITIVE_INT:
			int[] copyArrayInt = Arrays.copyOf((int[]) array, length + 1);
			Array.setInt(copyArrayInt, length, (int) addValue);
    		setField(dto, fieldName, copyArrayInt);
			break;

    	case PRIMITIVE_LONG:
			long[] copyArrayLong = Arrays.copyOf((long[]) array, length + 1);
			Array.setLong(copyArrayLong, length, (long) addValue);
    		setField(dto, fieldName, copyArrayLong);
			break;

    	case PRIMITIVE_FLOAT:
			float[] copyArrayFloat = Arrays.copyOf((float[]) array, length + 1);
			Array.setFloat(copyArrayFloat, length, (float) addValue);
    		setField(dto, fieldName, copyArrayFloat);
			break;

    	default:
			break;
    	}

	}

	/** DTOの指定したフィールド名に値を設定する **/
	public static void setField(Object dto, String fieldName, Object val) {

    	Field field = ReflectionUtils.findField(dto.getClass(), fieldName);
    	field.setAccessible(true);
    	ReflectionUtils.setField(field, dto, val);
	}

	/** Mapインターフェースを継承しているか **/
	public static boolean isMap(String className) {
		try {
			Object instance = newInstance(className);
			if (instance instanceof Map) {
				return true;
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	/** Listインターフェースを継承しているか **/
	public static boolean isList(String className) {
		return className.matches("java\\.util\\..*List.*");
	}

	/** インスタンス生成する（検査例外を省略するためのメソッド） **/
	public static Object newInstance(String className) {
		try {
			return Class.forName(className).getDeclaredConstructor().newInstance();
		} catch (Exception e) {
			throw new RuntimeException("インスタンス生成失敗", e);
		}
	}

	/** DTOのフィールドの値を取得する **/
	public static Object getFieldObject(Object dto, String fieldName) {

    	Method method = ReflectionUtils.findMethod(
    			dto.getClass(),
    			"get" + StringUtils.left(fieldName, 1).toUpperCase() + fieldName.substring(1));

    	if (method == null) {
    		method = ReflectionUtils.findMethod(
        			dto.getClass(),
        			"is" + StringUtils.left(fieldName, 1).toUpperCase() + fieldName.substring(1));
    	}
    	if (method == null) {
    		throw new RuntimeException(
    				String.format("DTOにないフィールド名が指定されました クラス名=[%s], フィールド名=[%s]", dto.getClass().getTypeName(), fieldName));
    	}

    	method.setAccessible(true);
    	return ReflectionUtils.invokeMethod(method, dto);
	}


	// =============================
	//    アサート関連
	// =============================

	/** DTOのフィールドの値をアサートする **/
	public static void assertField(Object dto, String fieldName, Object expected) {

		Object actual = Utils.getFieldObject(dto, fieldName);
		logger.debug(String.format("【アサート】期待値=[%s], 実際値=[%s]", expected, actual));
		assertEquals(expected, actual);
	}
	
	public static void assertFieldFromDto(Object dto, String fieldName, Object expected) {

		Object actual = Utils.getFieldObject(dto, fieldName);
		logger.debug(String.format("【アサート】期待値=[%s], 実際値=[%s]", expected, actual));
		assertEquals(expected, actual);
	}
	
	
	public static String convertDateTimeToString(LocalDateTime localDateTime) {
		DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("uuuu/MM/dd HH:mm:ss.SSS")
                .withLocale(Locale.JAPANESE)
                .withResolverStyle(ResolverStyle.STRICT);
		String result = formatter1.format(localDateTime);
		return result;
	}

}
