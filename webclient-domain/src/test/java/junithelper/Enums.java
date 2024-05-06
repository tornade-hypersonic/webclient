package junithelper;

public class Enums {

	public enum PropertPattern {
		DTO, LIST, DTO_ARRAY;
		
		public boolean isDto() {
			return this == DTO;
		}

		public boolean isArray() {
			return this == DTO_ARRAY || this == LIST;
		}
	}
}
