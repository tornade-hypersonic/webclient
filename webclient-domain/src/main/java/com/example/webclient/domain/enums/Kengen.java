package com.example.webclient.domain.enums;

public enum Kengen implements EnumBase<Kengen> {
	KENGEN01("01"),
	ALL("99");

//	private final String code;
//	Kengen(String code) {
//		this.code = code;
//	}
//	
//	@Override
//	public String getCode() {
//		return code;
//	}
//	
//	public static Kengen fromValue(String code) {
//		for (Kengen kengen: values()) {
//			if (kengen.getCode().equals(code)) {
//				return kengen;
//			}
//		}
//		throw new IllegalArgumentException("Invalid Status value: " + code);
//	}
	
	private final String value;
	Kengen(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
	
	public static Kengen fromValue(String value) {
		for (Kengen kengen: values()) {
			if (kengen.getValue().equals(value)) {
				return kengen;
			}
		}
		throw new IllegalArgumentException("Invalid Status value: " + value);
	}
	
}
