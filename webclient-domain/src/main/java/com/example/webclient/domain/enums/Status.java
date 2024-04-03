package com.example.webclient.domain.enums;

public enum Status implements EnumBase<Status> {
	ZERO("00"),
	EIGHT("08"),
	NINE("09");

//	private final String code;
//	Status(String code) {
//		this.code = code;
//	}
//	
//	@Override
//	public String getCode() {
//		return code;
//	}
//	
//	public static Status fromValue(String code) {
//		for (Status status: values()) {
//			if (status.getCode().equals(code)) {
//				return status;
//			}
//		}
//		throw new IllegalArgumentException("Invalid Status value: " + code);
//	}

	
	
	private final String value;
	Status(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
	
	public static Status fromValue(String value) {
		for (Status status: values()) {
//			if (status.getValue() == value) {
			if (status.getValue().equals(value)) {
				return status;
			}
		}
		throw new IllegalArgumentException("Invalid Status value: " + value);
	}
	
}
