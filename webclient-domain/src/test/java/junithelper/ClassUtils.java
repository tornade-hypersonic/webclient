package junithelper;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ClassUtils {

	private static Map<Class<?>, Map<String, Field>> classInfoMap = new HashMap<>();

	/**
	 * ClassのFieldをロードする
	 * @param classname
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
	
	/**
	 * ClassのFieldをロードする
	 * @param classname
	 */
	public static Map<String, Field> loadFiledByDtoField(Field field) {
		return ClassUtils.loadFiled(
				field.getType().toString().split(" ", -1)[1]);
	}
	
	public static Map<String, Field> loadFiledByDtoArrayField(Field field) {
		return ClassUtils.loadFiled(
				field.getType().getComponentType().getName());
	}
}
