package junithelper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class TryReadJson {

	@Test
	public void aaa() throws Exception {
//		String readString = Files.readString(Paths.get("C:\\Users\\takayoshi\\Desktop\\無題1.json"));
		String readString = Files.readString(Paths.get("src/test/java/junithelper/z_contractdto.json"));
		System.out.println(readString);
		
		ObjectMapper mapper = new ObjectMapper();
		JavaTimeModule jtm = new JavaTimeModule();
		mapper.registerModule(jtm);	
		ContractDto dto = mapper.readValue(readString, ContractDto.class);
		System.out.println(dto);
		
		
		JunitDtoHelper helper = new JunitDtoHelper();
		Map<String, Map<String, Map<String, Object>>> dtos =
				helper.createDtoFromExcel("data/test/ContractDto.xlsx");
		Map<String, Map<String, Object>> testNoMap = dtos.get("Sheet1");
		Map<String, Object> tubanMap = testNoMap.get("1-1");
		ContractDto contractDto1 = ContractDto.class.cast(tubanMap.get("1"));
		
		System.out.println("■" + dto);
		System.out.println("■" + contractDto1);
//		assertThat(dto, samePropertyValuesAs(contractDto1));
		
//        ContractDto deserializedObj1 = deserialize(serialize(dto));
//        ContractDto deserializedObj2 = deserialize(serialize(contractDto1));
//        Assert.assertEquals(deserializedObj1, deserializedObj2);
		
//		Assert.assertTrue(new ReflectionEquals(dto).append(contractDto1).build());
		
	}
	
    private byte[] serialize(ContractDto obj) throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(obj);
            return bos.toByteArray();
        }
    }

    private ContractDto deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
                ObjectInputStream ois = new ObjectInputStream(bis)) {
            return (ContractDto) ois.readObject();
        }
    }	
}
