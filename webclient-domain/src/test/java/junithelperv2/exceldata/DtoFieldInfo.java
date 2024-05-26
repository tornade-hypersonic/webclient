package junithelperv2.exceldata;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class DtoFieldInfo {

	private String fieldName;
	private String fieldClassName;
	private int level;

	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	// 使用項目
	@Deprecated
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

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
