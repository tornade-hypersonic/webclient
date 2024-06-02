package com.example.webclient.domain.service.sample10WebclientMulti.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class Sample10WebClientConfig {

    @Bean
    public WebClient sample10WebClientSystem1() {
        return WebClient.builder()
                .baseUrl("https://jsonplaceholder.typicode.com/todos")
                .build();
    }

    @Bean
    public WebClient sample10WebClientSystem2() {
        return WebClient.builder()
                .baseUrl("https://jsonplaceholder.typicode.com/todoszzzz")
                .build();
    }	
}
