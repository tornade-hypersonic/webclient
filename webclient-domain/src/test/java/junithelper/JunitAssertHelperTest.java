package junithelper;

import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

public class JunitAssertHelperTest {

	private static JunitAssertHelper asserter;

	@BeforeClass
	public static void setUpClass() {
		asserter = new JunitAssertHelper("data/test/ContractDto.xlsx");
	}

	@Test
	public void assertAddressDto() {

		AssertDummy dummy = new AssertDummy();
		AddressDto addressDto = dummy.createAddressDto();

		asserter.assertDto(addressDto, "addressInfo", "99-1", "2");
	}

	@Test
	public void assertContractDto() {

		JunitDtoHelper dtoHelper = new JunitDtoHelper();
		Map<String, Map<String, Map<String, Object>>> actualDtos =
				dtoHelper.createDtoFromExcel("data/test/ContractDto.xlsx");

		Object actual = actualDtos.get("Sheet1").get("1-1").get("1");

		JunitAssertHelper asserter = new JunitAssertHelper("data/test/ContractDto.xlsx");
		asserter.assertDto(actual, "Sheet1", "1-1", "1");
	}

}
