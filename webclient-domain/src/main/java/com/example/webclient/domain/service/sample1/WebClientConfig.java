package com.example.webclient.domain.service.sample1;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient defaultWebClient() {
        // (1)
        return WebClient.builder().build();
    }

}
