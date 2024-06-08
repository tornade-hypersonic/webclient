package junithelperv2;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import junithelperv2.Enums.PropertPattern;

public class ClassUtils {

	private static Map<Class<?>, Map<String, Field>> classInfoMap = new HashMap<>();

	/**
	 * クラスに定義されたフィールドを返却する
	 * そのクラスに存在しない場合、継承元クラスから取得する
	 * 
	 * フィールドはクラスから取得する
	 * 一度取得した情報はメモリにキャッシュし、二度目からはキャッシュから取得する
	 */
	public static Field getField(Class<?> clazz, String fieldName) {
		
		if (clazz.equals(Object.class)) {
			// 再帰処理でObjectまで呼び出された場合、フィールドなしとする
			return null;
		}
		
		Map<String, Field> fieldMap = loadField(clazz);
		Field field = fieldMap.get(fieldName);
		if (Objects.isNull(field)) {
			field = getField(clazz.getSuperclass(), fieldName);
		}
		
		return field;
	}
	
	/**
	 * DTOのフィールドに定義されたクラスを取得する。
	 * 
	 * DTOの場合、DTOのクラス名を返却する
	 * 配列の場合、配列要素のクラス名を返却する
	 * Listの場合、List要素のクラス名を返却する
	 */
	public static Class<?> getClass(Field field, PropertPattern pattern) {
		String classname = null;
	    switch (pattern) {
		    case DTO:
				classname = getClassNameByDtoField(field);
	    		break;
		    case DTO_ARRAY:
				classname = getClassNameByArrayElement(field);
	    		break;
		    case LIST:
		    case DTO_LIST:
		    	classname = getClassNameByListElement(field);
	    		break;
	    }
	    try {
		    return Class.forName(classname);
	    } catch(ClassNotFoundException e) {
	    	throw new RuntimeException(e);
	    }
	}
	
	/**
	 * ClassのFieldをロードする
	 * ロードしたField情報はメモリ上にキャッシュする
	 * キャッシュに存在しなければ、Classから取得する
	 * キャッシュに存在する場合、キャッシュした情報を返却する
	 */
	private static Map<String, Field> loadField(Class<?> clazz) {

		if (classInfoMap.containsKey(clazz)) {
			return classInfoMap.get(clazz);
		}
		
		Field[] fields = clazz.getDeclaredFields();
		
		Map<String, Field> fieldMap = new HashMap<>();
		for (Field field : fields) {
			fieldMap.put(field.getName(), field);
		}
		
		classInfoMap.put(clazz, fieldMap);
		
		return fieldMap;
	}
	
	/**
	 * DTOのフィールドに定義されたDTOのクラス名を取得
	 */
	public static String getClassNameByDtoField(Field field) {
		return field.getType().toString().split(" ", -1)[1];
	}
	
	/**
	 * DTOのフィールドに定義された配列要素のクラス名を取得
	 */
	public static String getClassNameByArrayElement(Field field) {
		return field.getType().getComponentType().getName();
	}
	
	/**
	 * DTOのフィールドに定義されたList要素のクラス名を取得
	 */
	public static String getClassNameByListElement(Field field) {
    	ParameterizedType listType = (ParameterizedType) field.getGenericType();
    	Type[] listTypes = listType.getActualTypeArguments();
    	return listTypes[0].getTypeName();
	}

}
