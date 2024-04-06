package com.example.webclient.domain.enums;

public enum Status3 implements EnumBase<Status3> {
	ZERO("00"),
	EIGHT("08"),
	NINE("09");

	private final String value;
	Status3(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
	
}
