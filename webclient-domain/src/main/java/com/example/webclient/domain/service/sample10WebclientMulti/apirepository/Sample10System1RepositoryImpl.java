package com.example.webclient.domain.service.sample10WebclientMulti.apirepository;

import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.webclient.domain.service.sample10WebclientMulti.entity.system1.Sample10System1DetailRequestEntity;
import com.example.webclient.domain.service.sample10WebclientMulti.entity.system1.Sample10System1ListRequestEntity;

import jakarta.inject.Inject;

@Repository
public class Sample10System1RepositoryImpl implements Sample10System1Repository {

	private static final Logger logger = LoggerFactory.getLogger(Sample10System1RepositoryImpl.class);

	// ※Bean定義と同じ変数名にする
	@Inject
	private WebClient sample10WebClientSystem1;

//	@Override
//	public Sample10System1ListResponseEntity callApiSystem1List(Sample10System1ListRequestEntity requestEntity) {
//        String json = sample10WebClientSystem1.get()
//                .uri("/1")
//                .retrieve()
//                .bodyToMono(String.class)
//                .block();
//        Sample10System1ListResponseEntity responseEntity = new Sample10System1ListResponseEntity();
//        responseEntity.setResult(json);
//        return responseEntity;
//	}
//
//	@Override
//	public Sample10System1DetailResponseEntity callApiSystem1Detail(Sample10System1DetailRequestEntity requestEntity) {
//        String json = sample10WebClientSystem1.get()
//                .uri("/2")
//                .retrieve()
//                .bodyToMono(String.class)
//                .block();
//        Sample10System1DetailResponseEntity responseEntity = new Sample10System1DetailResponseEntity();
//        responseEntity.setResult(json);
//        return responseEntity;
//	}

	
	public CompletableFuture<ResponseEntity<String>> callAsyncApiSystem1List(Sample10System1ListRequestEntity requestEntity) {
		logger.debug("callAsyncApiSystem1List-1 " + Thread.currentThread().getName());
        return sample10WebClientSystem1.get()
                .uri("/1")
                .retrieve()
                .toEntity(String.class)
                .toFuture();
	}

	public CompletableFuture<ResponseEntity<String>> callAsyncApiSystem1Detail(Sample10System1DetailRequestEntity requestEntity) {
        return sample10WebClientSystem1.get()
                .uri("/2")
                .retrieve()
                .toEntity(String.class)
                .toFuture();
	}


}
