package junithelperv2;

import org.junit.BeforeClass;
import org.junit.Test;

import junithelperv2.targetdto.AddressDto;
import junithelperv2.targetdto.AssertDummy;

public class JunitAssertHelperTest {

	private static JunitAssertHelper asserter;

	@BeforeClass
	public static void setUpClass() {
		asserter = new JunitAssertHelper("data/test/junithelperv2/ContractDto.xlsx");
	}

	@Test
	public void assertAddressDto() {

		AssertDummy dummy = new AssertDummy();
		AddressDto addressDto = dummy.createAddressDto();

		asserter.assertDto(addressDto, "addressInfo", "99-1", "2");
	}

	@Test
	public void assertContractDto() {

		JunitDtoHelperMapToDto dtoHelper = new JunitDtoHelperMapToDto();
		DtoAll actualDtos = dtoHelper.createDtoFromExcel("data/test/junithelperv2/ContractDto.xlsx");

		Object actual = actualDtos.getDto("Sheet1", "1-1", "1");

		JunitAssertHelper asserter = new JunitAssertHelper("data/test/junithelperv2/ContractDto.xlsx");
		asserter.assertDto(actual, "Sheet1", "1-1", "1");
		
		actual = actualDtos.getDto("Sheet1", "1-1", "2");
		asserter.assertDto(actual, "Sheet1", "1-1", "2");
		
		actual = actualDtos.getDto("Sheet1", "1-2", "1");
		asserter.assertDto(actual, "Sheet1", "1-2", "1");
		
	}
	
	@Test
	public void assertItem() {
		
		JunitDtoHelperMapToDto dtoHelper = new JunitDtoHelperMapToDto();
		DtoAll actualDtos = dtoHelper.createDtoFromExcel("data/test/junithelperv2/Item.xlsx");
		
		JunitAssertHelper asserter = new JunitAssertHelper("data/test/junithelperv2/Item.xlsx");
		
		Object actual = actualDtos.getDto("Sheet1", "1-1", "1");
		asserter.assertDto(actual, "Sheet1", "1-1", "1");
	}

}
