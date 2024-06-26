package junithelper;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class DtoFieldInfo {

	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getFieldClassName() {
		return fieldClassName;
	}
	public void setFieldClassName(String fieldClassName) {
		this.fieldClassName = fieldClassName;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}

	private String fieldName;
	private String fieldClassName;
	private int level;

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
