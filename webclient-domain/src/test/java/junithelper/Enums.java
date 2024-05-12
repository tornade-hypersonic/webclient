package junithelper;

public class Enums {

	public enum PropertPattern {
		DTO, LIST, DTO_ARRAY, DTO_LIST;
		
		public boolean isDto() {
			return this == DTO;
		}

		public boolean isArray() {
			return this == DTO_ARRAY || this == LIST || this == DTO_LIST;
		}
		
		public boolean isDtoArray() {
			return this == DTO_ARRAY || this == DTO_LIST;
		}
	}
}
