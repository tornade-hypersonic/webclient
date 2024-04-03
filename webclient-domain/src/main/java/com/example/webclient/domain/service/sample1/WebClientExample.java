package com.example.webclient.domain.service.sample1;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class WebClientExample {
	
	private static Logger logger = LoggerFactory.getLogger("WebClientExample");

    public static void main(String[] args) throws Exception {
        
    }
    
    public void getJson() {
    	
        // 1. WebClientを作成
        WebClient webClient = WebClient.create();
        
        logger.debug("★★★　START　★★★");

        // 2. リクエストを送信し、レスポンスを受け取る
        String apiUrl = "https://jsonplaceholder.typicode.com/todos/1";
        String responseData = webClient.get()
                .uri(apiUrl)
                .retrieve()
                .bodyToMono(String.class)
                .block();  // 非同期処理をブロックして同期的に実行

        // 3. レスポンスデータを取得
        System.out.println("Response Data: " + responseData);
        logger.debug("★★★　END　★★★");
        

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> map;
		try {
			map = objectMapper.readValue(responseData, Map.class);
	        System.out.println(map.get("userId"));
	        System.out.println(map.get("id"));
	        System.out.println(map.get("title"));
	        System.out.println(map.get("completed"));
		} catch (JsonProcessingException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
    }
}
