package junithelperv2.targetdto;

public class AssertDummy {

	public AddressDto createAddressDto() {
		AddressDto dto = new AddressDto();
		dto.setAddressCode("0110000002");
		dto.setZipNo("888-0000");
		dto.setPrefectures("福岡県");
		dto.setCity("福岡市");
		dto.setBantigo("南福岡");
		return dto;
	}
}
