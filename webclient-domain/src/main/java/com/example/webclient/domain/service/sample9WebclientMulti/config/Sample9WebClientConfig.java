package com.example.webclient.domain.service.sample9WebclientMulti.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class Sample9WebClientConfig {

    @Bean
    public WebClient webClientSystem1() {
        return WebClient.builder()
                .baseUrl("https://jsonplaceholder.typicode.com/todosxxx")
                .build();
    }

    @Bean
    public WebClient webClientSystem2() {
        return WebClient.builder()
                .baseUrl("https://jsonplaceholder.typicode.com/todos")
                .build();
    }	
}
