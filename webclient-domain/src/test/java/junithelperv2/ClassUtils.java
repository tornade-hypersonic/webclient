package junithelperv2;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import junithelper.Enums.PropertPattern;

public class ClassUtils {

	private static Map<Class<?>, Map<String, Field>> classInfoMap = new HashMap<>();

	/**
	 * ClassのFieldをロードする
	 */
	public static Map<String, Field> loadFiled(String classname) {
		Class<?> clazz;
		try {
			clazz = Class.forName(classname);
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
		} catch (ClassNotFoundException e) {
			new RuntimeException(e);
			return null;
		}
	}
	public static Map<String, Field> loadFiled(Object object) {
		return loadFiled(object.getClass().getName());
	}
	
	/**
	 * ClassのFieldをロードする
	 * @param classname
	 */
	public static Map<String, Field> loadFiledByDtoField(Field field) {
		String classname = getFieldTypeNameByDtoField(field);
		return ClassUtils.loadFiled(classname);
	}
	
	public static Map<String, Field> loadFiledByDtoArrayField(Field field) {
		String classname = getFieldTypeNameByArrayElement(field);
		return ClassUtils.loadFiled(classname);
	}

	public static Map<String, Field> loadFiledByListField(Field field) {
    	String classname = getFieldTypeNameByListElement(field);
		return ClassUtils.loadFiled(classname);
	}
	
	public static Map<String, Field> loadFiledByField(Field field, PropertPattern pattern) {
	    Map<String,Field> classFiledMap = null;
	    
	    switch (pattern) {
		    case DTO:
	    		classFiledMap = ClassUtils.loadFiledByDtoField(field);
	    		break;
		    case DTO_ARRAY:
	    		classFiledMap = ClassUtils.loadFiledByDtoArrayField(field);
	    		break;
		    case LIST:
		    case DTO_LIST:
	    		classFiledMap = ClassUtils.loadFiledByListField(field);
	    		break;
	    }
	    return classFiledMap;
	}

	public static String getFieldTypeNameByDtoField(Field field) {
		return field.getType().toString().split(" ", -1)[1];
	}
	
	public static String getFieldTypeNameByArrayElement(Field field) {
		return field.getType().getComponentType().getName();
	}
	
	public static String getFieldTypeNameByListElement(Field field) {
    	ParameterizedType listType = (ParameterizedType) field.getGenericType();
    	Type[] listTypes = listType.getActualTypeArguments();
    	return listTypes[0].getTypeName();
	}
	
	
}
