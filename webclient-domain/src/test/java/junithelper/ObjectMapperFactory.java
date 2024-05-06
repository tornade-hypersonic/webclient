package junithelper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;

public class ObjectMapperFactory {
	
	public static ObjectMapper getInstance() {
		ObjectMapper objectMapper = new ObjectMapper();
	    JavaTimeModule javaTimeModule = new JavaTimeModule();
	    javaTimeModule.addDeserializer(LocalDateTime.class, new CustomLocalDateTimeDeserializer());
	    objectMapper.registerModule(javaTimeModule);
	    return objectMapper;
	}

    private static class CustomLocalDateTimeDeserializer extends LocalDateTimeDeserializer {
        public CustomLocalDateTimeDeserializer() {
            super(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSS"));
        }
    }
}
