package com.example.webclient.domain.service.sample10WebclientMulti.apirepository;

import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.webclient.domain.service.sample10WebclientMulti.entity.system2.Sample10System2DetailRequestEntity;
import com.example.webclient.domain.service.sample10WebclientMulti.entity.system2.Sample10System2DetailResponseEntity;
import com.example.webclient.domain.service.sample10WebclientMulti.entity.system2.Sample10System2ListRequestEntity;
import com.example.webclient.domain.service.sample10WebclientMulti.entity.system2.Sample10System2ListResponseEntity;

import jakarta.inject.Inject;

@Repository
public class Sample10System2RepositoryImpl implements Sample10System2Repository {

	private static final Logger logger = LoggerFactory.getLogger(Sample10System2RepositoryImpl.class);

	// ※Bean定義と同じ変数名にする
	@Inject
	private WebClient sample10WebClientSystem2;

	@Override
	public Sample10System2ListResponseEntity callApiSystem2List(Sample10System2ListRequestEntity requestEntity) {
        String json = sample10WebClientSystem2.get()
                .uri("/8")
                .retrieve()
                .bodyToMono(String.class)
                .block();
        Sample10System2ListResponseEntity responseEntity = new Sample10System2ListResponseEntity();
        responseEntity.setResult(json);
        return responseEntity;
	}

	@Override
	public Sample10System2DetailResponseEntity callApiSystem2Detail(Sample10System2DetailRequestEntity requestEntity) {
        String json = sample10WebClientSystem2.get()
                .uri("/9")
                .retrieve()
                .bodyToMono(String.class)
                .block();
        Sample10System2DetailResponseEntity responseEntity = new Sample10System2DetailResponseEntity();
        responseEntity.setResult(json);
        return responseEntity;
	}

	@Override
	public CompletableFuture<ResponseEntity<String>> callAsyncApiSystem2List(Sample10System2ListRequestEntity requestEntity) {
		logger.debug("callAsyncApiSystem2List-1 " + Thread.currentThread().getName());
        return sample10WebClientSystem2.get()
                .uri("/8")
                .retrieve()
                .toEntity(String.class)
                .toFuture();
	}

	@Override
	public CompletableFuture<ResponseEntity<String>> callAsyncApiSystem2Detail(Sample10System2DetailRequestEntity requestEntity) {
        return sample10WebClientSystem2.get()
                .uri("/9")
                .retrieve()
                .toEntity(String.class)
                .toFuture();
	}

}
