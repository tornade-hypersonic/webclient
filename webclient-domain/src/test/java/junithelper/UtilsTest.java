package junithelper;

import java.time.LocalDateTime;

import org.junit.Test;

public class UtilsTest {

//	@Test
//	public void testMapString() {
//		myAssert("java.util.HashMap<java.lang.String, java.lang.String>", true);
//	}
//	@Test
//	public void testMapObject() {
//		myAssert("java.util.HashMap<java.lang.String, Object>", true);
//	}
//	@Test
//	public void testString() {
//		myAssert("java.lang.String", false);
//	}
//	@Test
//	public void testInt() {
//		myAssert("int", false);
//	}
//	@Test
//	public void testInteger() {
//		myAssert("java.lang.Integer", false);
//	}
//	@Test
//	public void testLong() {
//		myAssert("long", false);
//	}
//	@Test
//	public void testLongObj() {
//		myAssert("java.lang.Long", false);
//	}
//	@Test
//	public void testFloat() {
//		myAssert("float", false);
//	}
//	@Test
//	public void testFloatObj() {
//		myAssert("java.lang.Float", false);
//	}
//	@Test
//	public void testDateTime() {
//		myAssert("org.joda.time.DateTime", false);
//	}
//	@Test
//	public void testListString() {
//		myAssert("java.util.ArrayList<java.lang.String>", false);
//	}
//	@Test
//	public void testListInteger() {
//		myAssert("java.util.ArrayList<java.lang.Integer>", false);
//	}
//	@Test
//	public void testListFloat() {
//		myAssert("java.util.ArrayList<java.lang.Float>", false);
//	}
//	@Test
//	public void testListLong() {
//		myAssert("java.util.ArrayList<java.lang.Long>", false);
//	}
//	@Test
//	public void testListDto() {
//		myAssert("java.util.ArrayList<xx.xxx.XxxDto>", true);
//	}
//
//	@Test
//	public void testArrayString() {
//		myAssert("java.lang.String[]", false);
//	}
//	@Test
//	public void testArrayDto() {
//		myAssert("xx.xxx.XxxDto[]", true);
//	}
//	@Test
//	public void testArrayInt() {
//		myAssert("int[]", false);
//	}
//	@Test
//	public void testArrayLong() {
//		myAssert("long[]", false);
//	}
//	@Test
//	public void testArrayFloat() {
//		myAssert("float[]", false);
//	}
//
//	private void myAssert(String fieldClassName, boolean expected) {
//		boolean actual = Utils.isAnotherSheetReference(fieldClassName);
//		if (expected) {
//			assertTrue(actual);
//			return;
//		}
//		assertFalse(actual);
//	}
	
	@Test
	public void testConvertDateTimeToString() {
		
		Utils.convertDateTimeToString(LocalDateTime.now());
	}
}
