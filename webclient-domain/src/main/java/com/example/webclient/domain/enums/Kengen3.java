package com.example.webclient.domain.enums;

public enum Kengen3 implements EnumBase<Kengen3> {
	KENGEN01("01"),
	ALL("99");

	private final String value;
	Kengen3(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
}
