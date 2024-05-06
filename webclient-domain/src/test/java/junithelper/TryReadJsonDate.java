package junithelper;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;

public class TryReadJsonDate {

    public static void main(String[] args) {
        List<String> jsonList = new ArrayList<>();
//        jsonList.add("{\"dateTime\": \"2021/12/23 11:22:33\"}");
        jsonList.add("{\"dateTime\": \"2021/12/23 11:22:33.789\"}");

        ObjectMapper objectMapper = new ObjectMapper();
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addDeserializer(LocalDateTime.class, new CustomLocalDateTimeDeserializer());
        objectMapper.registerModule(javaTimeModule);

        for (String json : jsonList) {
            try {
                MyDTO myDTO = objectMapper.readValue(json, MyDTO.class);
                System.out.println("Parsed LocalDateTime: " + myDTO.getDateTime());
                
                String string = objectMapper.writeValueAsString(myDTO);
                System.out.println(string);
                
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static class MyDTO {
        private LocalDateTime dateTime;

        public LocalDateTime getDateTime() {
            return dateTime;
        }

        public void setDateTime(LocalDateTime dateTime) {
            this.dateTime = dateTime;
        }
    }
    
    private static class CustomLocalDateTimeDeserializer extends LocalDateTimeDeserializer {
        public CustomLocalDateTimeDeserializer() {
            super(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSS"));
        }
    }
}
