package junithelper;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;


public class DtoExcelCreater {

	public static void main(String[] args) {
	
		try {
			new DtoExcelCreater().execute(ContractDto.class);
		} catch (Throwable e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}
	
	private void execute(Class<?> clazz) throws Throwable {
		
		if (!clazz.getName().startsWith("junithelper")) {
			return;
		}
		
		System.out.println("★★★クラス名\t" + clazz.getName());
		
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			if (field.getType().isArray()) {
				System.out.println("配列\t" + field.getName() + "\t" + 
								field.getType().getSimpleName() + "\t" + 
								field.getType().getComponentType().getSimpleName());
//				execute(Class.forName(field.getType().getComponentType().getTypeName()));
				
            } else if (List.class.isAssignableFrom(field.getType())) {
            	ParameterizedType listType = (ParameterizedType) field.getGenericType();
            	Type[] listTypes = listType.getActualTypeArguments();
				System.out.println("List\t" + field.getName() + "\t" + 
								field.getType().getSimpleName() + "\t" + 
								"[" + listTypes[0].getTypeName() + "]");
				execute(Class.forName(listTypes[0].getTypeName()));
				
            } else if (Map.class.isAssignableFrom(field.getType())) {
            	ParameterizedType mapType = (ParameterizedType) field.getGenericType();
            	Type[] mapTypes = mapType.getActualTypeArguments();
				System.out.println("Map\t" + field.getName() + "\t" + 
            	field.getType().getSimpleName() + "\t" + 
						"<" + mapTypes[0].getTypeName() + " , " + 
							mapTypes[1].getTypeName() + ">");
				execute(Class.forName(mapTypes[1].getTypeName()));
				
			} else {
				System.out.println("通常\t" + field.getName() + "\t" + field.getType());
			}
		}
		
	}
	
    public static void aaa(String[] args) {
        ContractDto contractDto = new ContractDto();

        Field[] fields = contractDto.getClass().getDeclaredFields();
        for (Field field : fields) {
            System.out.println("Field: " + field.getName());
            System.out.println("Type: " + field.getType().getSimpleName());
            if (List.class.isAssignableFrom(field.getType())) {
                // If the field is a List
                ParameterizedType listType = (ParameterizedType) field.getGenericType();
                Type elementType = listType.getActualTypeArguments()[0];
                System.out.println("List Element Type: " + elementType.getTypeName());
            } else if (Map.class.isAssignableFrom(field.getType())) {
                // If the field is a Map
                ParameterizedType mapType = (ParameterizedType) field.getGenericType();
                Type[] mapTypes = mapType.getActualTypeArguments();
                System.out.println("Map Key Type: " + mapTypes[0].getTypeName());
                System.out.println("Map Value Type: " + mapTypes[1].getTypeName());
            } else if (field.getType().isArray()) {
                // If the field is an array
                System.out.println("Array Component Type: " + field.getType().getComponentType().getSimpleName());
            }
            System.out.println();
        }
    }
	

}
