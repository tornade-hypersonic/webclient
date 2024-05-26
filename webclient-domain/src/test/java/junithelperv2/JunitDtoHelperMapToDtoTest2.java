package junithelperv2;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.Test;

import junithelperv2.target.AddressDto;
import junithelperv2.target.ContractDto;
import junithelperv2.target.ServiceInfo;


public class JunitDtoHelperMapToDtoTest2 {

	@Test
	public void testAll() throws Exception {

		JunitDtoHelperMapToDto2 helper = new JunitDtoHelperMapToDto2();
		DtoAll dtos =
				helper.createDtoFromExcel("data/test/junithelperv2/ContractDto.xlsx");

		ContractDto contractDto1 = ContractDto.class.cast(dtos.getDto("Sheet1", "1-1", "1"));
		testContractDto1_1(contractDto1);

		ContractDto contractDto2 = ContractDto.class.cast(dtos.getDto("Sheet1", "1-1", "2"));
		testContractDto1_2(contractDto2);

		ContractDto contractDto1_2_1 = ContractDto.class.cast(dtos.getDto("Sheet1", "1-2", "1"));
		testContractDto1_2_1(contractDto1_2_1);

		
		
//		ObjectMapper objectMapper = new ObjectMapper();
//		JavaTimeModule jtm = new JavaTimeModule();
//		objectMapper.registerModule(jtm);	
//		String json1 = objectMapper.writeValueAsString(contractDto1);
//		System.out.println("■" + json1);
		
	}


	private void testContractDto1_1(ContractDto contractDto) {

		assertEquals("092-261-0001", contractDto.getTelNo());
		assertEquals("2", contractDto.getBunkatsuNo());
		assertEquals("料金　太郎", contractDto.getContractName());
		assertEquals("2021/12/23 11:22:33.789", convertDateTimeToString(contractDto.getUpdateDate()));
		assertEquals(Integer.parseInt("3"), contractDto.getUpdateCnt().intValue());
		assertEquals(11, contractDto.getTestInt());
		assertEquals(21, contractDto.getTestLong());
		assertEquals(31.1, contractDto.getTestFloat(), 0.001);

		AddressDto addressDto = AddressDto.class.cast(contractDto.getAddressList().get(0));
		assertEquals("0110000002", addressDto.getAddressCode());
		assertEquals("888-0000", addressDto.getZipNo());
		assertEquals("福岡県", addressDto.getPrefectures());
		assertEquals("福岡市", addressDto.getCity());
		assertEquals("南福岡", addressDto.getBantigo());

		addressDto = AddressDto.class.cast(contractDto.getAddressList().get(1));
		assertEquals("0110000001", addressDto.getAddressCode());
		assertEquals("888-9999", addressDto.getZipNo());
		assertEquals("北海道", addressDto.getPrefectures());
		assertEquals("札幌市", addressDto.getCity());
		assertEquals("横浜町", addressDto.getBantigo());


		assertEquals(" ", contractDto.getStatusPulldown().get(" "));
		assertEquals("作業中", contractDto.getStatusPulldown().get("001"));
		assertEquals("完了", contractDto.getStatusPulldown().get("009"));
		assertFalse(contractDto.getStatusPulldown().containsKey("000"));
		assertFalse(contractDto.getStatusPulldown().containsKey("002"));
		assertFalse(contractDto.getStatusPulldown().containsKey("003"));


		assertEquals("リストb", contractDto.getStringList().get(0));
		assertEquals("リストa", contractDto.getStringList().get(1));
		assertEquals(2, contractDto.getStringList().size());

		assertEquals(2, contractDto.getIntegerList().size());
		assertEquals(Integer.parseInt("51"), contractDto.getIntegerList().get(0).intValue());
		assertEquals(Integer.parseInt("52"), contractDto.getIntegerList().get(1).intValue());
		assertEquals(2, contractDto.getFloatList().size());
		assertEquals(Long.parseLong("61"), contractDto.getLongList().get(0).longValue());
		assertEquals(Long.parseLong("62"), contractDto.getLongList().get(1).longValue());

		String[] strings = contractDto.getArrayString();
		assertEquals(2, strings.length);
		assertEquals("配列b", strings[0]);
		assertEquals("配列a", strings[1]);

		AddressDto[] addressDtos = contractDto.getArrayAddress();
		assertEquals(2,  addressDtos.length);
		assertEquals("0110000002", addressDtos[0].getAddressCode());
		assertEquals("888-0000", addressDtos[0].getZipNo());
		assertEquals("福岡県", addressDtos[0].getPrefectures());
		assertEquals("福岡市", addressDtos[0].getCity());
		assertEquals("南福岡", addressDtos[0].getBantigo());

		assertEquals("0110000001", addressDtos[1].getAddressCode());
		assertEquals("888-9999", addressDtos[1].getZipNo());
		assertEquals("北海道", addressDtos[1].getPrefectures());
		assertEquals("札幌市", addressDtos[1].getCity());
		assertEquals("横浜町", addressDtos[1].getBantigo());

		Integer[] arrayInteger = contractDto.getArrayInteger();
		assertEquals(2, arrayInteger.length);
		assertEquals(Integer.parseInt("51"), arrayInteger[0].intValue());
		assertEquals(Integer.parseInt("52"), arrayInteger[1].intValue());

		int[] arrayInt = contractDto.getArrayInt();
		assertEquals(2, arrayInt.length);
		assertEquals(Integer.parseInt("51"), arrayInt[0]);
		assertEquals(Integer.parseInt("52"), arrayInt[1]);

		Long[] arrayLongClass = contractDto.getArrayLongClass();
		assertEquals(2, arrayLongClass.length);
		assertEquals(Long.parseLong("61"), arrayLongClass[0].longValue());
		assertEquals(Long.parseLong("62"), arrayLongClass[1].longValue());

		long[] arrayLong = contractDto.getArrayLong();
		assertEquals(2, arrayInt.length);
		assertEquals(Long.parseLong("61"), arrayLong[0]);
		assertEquals(Long.parseLong("62"), arrayLong[1]);

		Float[] arrayFloatClass = contractDto.getArrayFloatClass();
		assertEquals(2, arrayFloatClass.length);
		assertEquals(Float.parseFloat("71.1"), arrayFloatClass[0].floatValue(), 0.0001);
		assertEquals(Float.parseFloat("72.2"), arrayFloatClass[1].floatValue(), 0.0001);

		float[] arrayFloat = contractDto.getArrayFloat();
		assertEquals(2, arrayInt.length);
		assertEquals(Float.parseFloat("71.1"), arrayFloat[0], 0.0001);
		assertEquals(Float.parseFloat("72.2"), arrayFloat[1], 0.0001);

		ServiceInfo serviceInfo = contractDto.getServiceInfo();
		assertEquals("101", serviceInfo.getId());
		assertEquals("サービス101", serviceInfo.getName());

		assertEquals("1", contractDto.getHaishi());

		ServiceInfo serviceInfo2 = contractDto.getServiceInfo2();
		assertEquals("131", serviceInfo2.getId());
		assertEquals("サービス131", serviceInfo2.getName());

		AddressDto addressDtoUnit = contractDto.getAddress();
		assertEquals("0110000002", addressDtoUnit.getAddressCode());
		assertEquals("888-0000", addressDtoUnit.getZipNo());
		assertEquals("福岡県", addressDtoUnit.getPrefectures());
		assertEquals("福岡市", addressDtoUnit.getCity());
		assertEquals("南福岡", addressDtoUnit.getBantigo());

		ServiceInfo[] serviceInfoArrayLevel = contractDto.getServiceInfoArrayLevel();
		assertEquals(2, serviceInfoArrayLevel.length);
		assertEquals("301", serviceInfoArrayLevel[0].getId());
		assertEquals("サービス301", serviceInfoArrayLevel[0].getName());
		assertEquals("302", serviceInfoArrayLevel[1].getId());
		assertEquals("サービス302", serviceInfoArrayLevel[1].getName());

		List<ServiceInfo> serviceInfoListLevel = contractDto.getServiceInfoListLevel();
		assertEquals(2, serviceInfoListLevel.size());
		assertEquals("401", serviceInfoListLevel.get(0).getId());
		assertEquals("サービス401", serviceInfoListLevel.get(0).getName());
		assertEquals("402", serviceInfoListLevel.get(1).getId());
		assertEquals("サービス402", serviceInfoListLevel.get(1).getName());

	}


	private void testContractDto1_2(ContractDto contractDto) {

		assertEquals("092-261-0002", contractDto.getTelNo());
		assertEquals("0", contractDto.getBunkatsuNo());
		assertEquals("料金　太郎", contractDto.getContractName());
		assertEquals("2021/12/24 11:22:33.789", convertDateTimeToString(contractDto.getUpdateDate()));
		assertEquals(Integer.parseInt("4"), contractDto.getUpdateCnt().intValue());
		assertEquals(12, contractDto.getTestInt());
		assertEquals(22, contractDto.getTestLong());
		assertEquals(32.2, contractDto.getTestFloat(), 0.001);

		AddressDto addressDto = AddressDto.class.cast(contractDto.getAddressList().get(0));
		assertEquals("0110001001", addressDto.getAddressCode());
		assertEquals("888-1999", addressDto.getZipNo());
		assertEquals("鹿児島県", addressDto.getPrefectures());
		assertEquals("鹿児島市", addressDto.getCity());
		assertEquals("神崎", addressDto.getBantigo());

		assertEquals("未着手", contractDto.getStatusPulldown().get("000"));
		assertEquals("レビュー中", contractDto.getStatusPulldown().get("002"));
		assertEquals(2, contractDto.getStatusPulldown().size());

		assertEquals("リストzz", contractDto.getStringList().get(0));
		assertEquals(1, contractDto.getStringList().size());
		assertEquals(Integer.parseInt("53"), contractDto.getIntegerList().get(0).intValue());
		assertEquals(1, contractDto.getIntegerList().size());
		assertEquals(Long.parseLong("63"), contractDto.getLongList().get(0).longValue());
		assertEquals(1, contractDto.getLongList().size());
		assertEquals(Float.parseFloat("73.3"), contractDto.getFloatList().get(0).floatValue(), 0.0001);
		assertEquals(1, contractDto.getFloatList().size());

		String[] strings = contractDto.getArrayString();
		assertEquals(1, strings.length);
		assertEquals("配列zz", strings[0]);

		AddressDto[] addressDtos = contractDto.getArrayAddress();
		assertEquals(1,  addressDtos.length);
		assertEquals("0110001001", addressDtos[0].getAddressCode());
		assertEquals("888-1999", addressDtos[0].getZipNo());
		assertEquals("鹿児島県", addressDtos[0].getPrefectures());
		assertEquals("鹿児島市", addressDtos[0].getCity());
		assertEquals("神崎", addressDtos[0].getBantigo());

		Integer[] arrayInteger = contractDto.getArrayInteger();
		assertEquals(1, arrayInteger.length);
		assertEquals(Integer.parseInt("53"), arrayInteger[0].intValue());

		int[] arrayInt = contractDto.getArrayInt();
		assertEquals(1, arrayInt.length);
		assertEquals(Integer.parseInt("53"), arrayInt[0]);

		Long[] arrayLongClass = contractDto.getArrayLongClass();
		assertEquals(1, arrayLongClass.length);
		assertEquals(Long.parseLong("63"), arrayLongClass[0].longValue());

		long[] arrayLong = contractDto.getArrayLong();
		assertEquals(1, arrayInt.length);
		assertEquals(Long.parseLong("63"), arrayLong[0]);

		Float[] arrayFloatClass = contractDto.getArrayFloatClass();
		assertEquals(1, arrayFloatClass.length);
		assertEquals(Float.parseFloat("73.3"), arrayFloatClass[0].floatValue(), 0.0001);

		float[] arrayFloat = contractDto.getArrayFloat();
		assertEquals(1, arrayInt.length);
		assertEquals(Float.parseFloat("73.3"), arrayFloat[0], 0.0001);

		ServiceInfo serviceInfo = contractDto.getServiceInfo();
		assertEquals("102", serviceInfo.getId());
		assertEquals("サービス102", serviceInfo.getName());

		assertEquals("0", contractDto.getHaishi());

		ServiceInfo serviceInfo2 = contractDto.getServiceInfo2();
		assertEquals("132", serviceInfo2.getId());
		assertEquals("サービス132", serviceInfo2.getName());

		AddressDto addressDtoUnit = contractDto.getAddress();
		assertEquals("0110001001", addressDtoUnit.getAddressCode());
		assertEquals("888-1999", addressDtoUnit.getZipNo());
		assertEquals("鹿児島県", addressDtoUnit.getPrefectures());
		assertEquals("鹿児島市", addressDtoUnit.getCity());
		assertEquals("神崎", addressDtoUnit.getBantigo());

		ServiceInfo[] serviceInfoArrayLevel = contractDto.getServiceInfoArrayLevel();
		assertEquals(1, serviceInfoArrayLevel.length);
		assertEquals("311", serviceInfoArrayLevel[0].getId());
		assertEquals("サービス311", serviceInfoArrayLevel[0].getName());

		List<ServiceInfo> serviceInfoListLevel = contractDto.getServiceInfoListLevel();
		assertEquals(1, serviceInfoListLevel.size());
		assertEquals("411", serviceInfoListLevel.get(0).getId());
		assertEquals("サービス411", serviceInfoListLevel.get(0).getName());

	}

	private void testContractDto1_2_1(ContractDto contractDto) {

		assertEquals("092-261-0012", contractDto.getTelNo());
		assertEquals("0", contractDto.getBunkatsuNo());
		assertEquals("料金　太郎", contractDto.getContractName());
		assertEquals("2021/12/23 11:22:33.789", convertDateTimeToString(contractDto.getUpdateDate()));
		assertEquals(Integer.parseInt("5"), contractDto.getUpdateCnt().intValue());
		assertEquals(51, contractDto.getTestInt());
		assertEquals(61, contractDto.getTestLong());
		assertEquals(71.3, contractDto.getTestFloat(), 0.001);

		AddressDto addressDto = AddressDto.class.cast(contractDto.getAddressList().get(0));
		assertEquals("0110001004", addressDto.getAddressCode());
		assertEquals("888-1994", addressDto.getZipNo());
		assertEquals("和歌山県", addressDto.getPrefectures());
		assertEquals("和歌山市", addressDto.getCity());
		assertEquals("ＡＡＡ", addressDto.getBantigo());

		assertEquals("確認待ち", contractDto.getStatusPulldown().get("003"));
		assertEquals(1, contractDto.getStatusPulldown().size());

		assertEquals("リストxx", contractDto.getStringList().get(0));
		assertEquals(1, contractDto.getStringList().size());
		assertEquals(Integer.parseInt("54"), contractDto.getIntegerList().get(0).intValue());
		assertEquals(1, contractDto.getIntegerList().size());
		assertEquals(Long.parseLong("64"), contractDto.getLongList().get(0).longValue());
		assertEquals(1, contractDto.getLongList().size());
		assertEquals(Float.parseFloat("74.4"), contractDto.getFloatList().get(0).floatValue(), 0.0001);
		assertEquals(1, contractDto.getFloatList().size());

		String[] strings = contractDto.getArrayString();
		assertEquals(1, strings.length);
		assertEquals("配列xx", strings[0]);

		AddressDto[] addressDtos = contractDto.getArrayAddress();
		assertEquals(1,  addressDtos.length);
		assertEquals("0110001004", addressDtos[0].getAddressCode());
		assertEquals("888-1994", addressDtos[0].getZipNo());
		assertEquals("和歌山県", addressDtos[0].getPrefectures());
		assertEquals("和歌山市", addressDtos[0].getCity());
		assertEquals("ＡＡＡ", addressDtos[0].getBantigo());

		Integer[] arrayInteger = contractDto.getArrayInteger();
		assertEquals(1, arrayInteger.length);
		assertEquals(Integer.parseInt("54"), arrayInteger[0].intValue());

		int[] arrayInt = contractDto.getArrayInt();
		assertEquals(1, arrayInt.length);
		assertEquals(Integer.parseInt("54"), arrayInt[0]);

		Long[] arrayLongClass = contractDto.getArrayLongClass();
		assertEquals(1, arrayLongClass.length);
		assertEquals(Long.parseLong("64"), arrayLongClass[0].longValue());

		long[] arrayLong = contractDto.getArrayLong();
		assertEquals(1, arrayInt.length);
		assertEquals(Long.parseLong("64"), arrayLong[0]);

		Float[] arrayFloatClass = contractDto.getArrayFloatClass();
		assertEquals(1, arrayFloatClass.length);
		assertEquals(Float.parseFloat("74.4"), arrayFloatClass[0].floatValue(), 0.0001);

		float[] arrayFloat = contractDto.getArrayFloat();
		assertEquals(1, arrayInt.length);
		assertEquals(Float.parseFloat("74.4"), arrayFloat[0], 0.0001);

		ServiceInfo serviceInfo = contractDto.getServiceInfo();
		assertEquals("221", serviceInfo.getId());
		assertEquals("サービス221", serviceInfo.getName());

		assertEquals("0", contractDto.getHaishi());

		ServiceInfo serviceInfo2 = contractDto.getServiceInfo2();
		assertEquals("141", serviceInfo2.getId());
		assertEquals("サービス141", serviceInfo2.getName());

		AddressDto addressDtoUnit = contractDto.getAddress();
		assertEquals("0110001004", addressDtoUnit.getAddressCode());
		assertEquals("888-1994", addressDtoUnit.getZipNo());
		assertEquals("和歌山県", addressDtoUnit.getPrefectures());
		assertEquals("和歌山市", addressDtoUnit.getCity());
		assertEquals("ＡＡＡ", addressDtoUnit.getBantigo());

		ServiceInfo[] serviceInfoArrayLevel = contractDto.getServiceInfoArrayLevel();
		assertEquals(1, serviceInfoArrayLevel.length);
		assertEquals("321", serviceInfoArrayLevel[0].getId());
		assertEquals("サービス321", serviceInfoArrayLevel[0].getName());

		List<ServiceInfo> serviceInfoListLevel = contractDto.getServiceInfoListLevel();
		assertEquals(1, serviceInfoListLevel.size());
		assertEquals("421", serviceInfoListLevel.get(0).getId());
		assertEquals("サービス421", serviceInfoListLevel.get(0).getName());

	}

	public String convertDateTimeToString(LocalDateTime dateTime) {
		return Utils.convertDateTimeToString(dateTime);
//    	String string = dateTime.toString("yyyy/MM/dd HH:mm:ss.SSS");
//    	System.out.println(string);
//    	return string;
	}
}
