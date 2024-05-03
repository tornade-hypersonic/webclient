package junithelper;

public enum FieldType {

	// 数値とDTO以外のクラス
	STRING("java.lang.String"),
//	DATE_TIME("org.joda.time.DateTime"),
	DATE_TIME("java.time.LocalDateTime"),
	DATE("java.util.Date"),

	// 数値クラス
	INTEGER("java.lang.Integer"),
	LONG("java.lang.Long"),
	FLOAT("java.lang.Float"),

	// 数値のプリミティブ型
	PRIMITIVE_INT("int"),
	PRIMITIVE_LONG("long"),
	PRIMITIVE_FLOAT("float");

	private String fieldType;
	private FieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	public String getType() {
		return fieldType;
	}

	/** 全フィールドのいずれかに該当する値を返却 **/
	public static FieldType getInstance(String fieldClassName) {
		FieldType[] fieldTypes = FieldType.values();
		return getEnumElement(fieldTypes, fieldClassName, "getInstance()");
	}

	/** プリミティブ型以外のクラスの場合、それに該当する値を返却 **/
	public static FieldType getInstanceExceptPrimitive(String fieldClassName) {
		FieldType[] fieldTypes = {STRING, DATE_TIME, DATE, INTEGER, LONG, FLOAT};
		return getEnumElement(fieldTypes, fieldClassName, "getNumberInstance()");
	}

	/** 数値クラスに該当する場合、それに該当する値を返却 **/
	public static FieldType getNumberInstance(String fieldClassName) {
		FieldType[] fieldTypes = {INTEGER, LONG, FLOAT};
		return getEnumElement(fieldTypes, fieldClassName, "getNumberInstance()");
	}

	/** プリミティブ型に該当する場合、それに該当する値を返却 **/
	public static FieldType getPrimitiveInstance(String fieldClassName) {
		FieldType[] fieldTypes = {PRIMITIVE_INT, PRIMITIVE_LONG, PRIMITIVE_FLOAT};
		return getEnumElement(fieldTypes, fieldClassName, "getPrimitiveInstance()");
	}

	/** 数値クラス・プリミティブ型に該当する場合、それに該当する値を返却 **/
	public static FieldType getNumberPrimitiveInstance(String fieldClassName) {
		FieldType[] fieldTypes = {
					INTEGER, LONG, FLOAT,
					PRIMITIVE_INT, PRIMITIVE_LONG, PRIMITIVE_FLOAT
				};
		return getEnumElement(fieldTypes, fieldClassName, "getNumberPrimitiveInstance()");
	}

	private static FieldType getEnumElement(FieldType[] fieldTypes, String fieldClassName, String methodName) {
		for (FieldType type : fieldTypes) {
			if (type.getType().equals(fieldClassName)) {
				return type;
			}
		}
//		throw new IllegalArgumentException("該当するenumがありません。 [" + fieldClassName + "]");
		System.out.println(methodName + " 該当するenumがありません。 [" + fieldClassName + "]");
		return null;
	}


	/** JunitHelperのサポート対象か **/
	public static boolean isSupportSingleType(String fieldClassName) {
		FieldType instance = getInstance(fieldClassName);
		return instance != null;
	}

	/** JunitHelperのサポート対象か（プリミティブ型以外） **/
	public static boolean isSupportSingleTypeExceptPrimitive(String fieldClassName) {
		FieldType instance = getInstanceExceptPrimitive(fieldClassName);
		return instance != null;
	}


}
