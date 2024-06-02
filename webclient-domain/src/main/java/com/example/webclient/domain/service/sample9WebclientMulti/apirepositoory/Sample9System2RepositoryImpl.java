package com.example.webclient.domain.service.sample9WebclientMulti.apirepositoory;

import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.webclient.domain.service.sample9WebclientMulti.entity.system2.Sample9System2DetailRequestEntity;
import com.example.webclient.domain.service.sample9WebclientMulti.entity.system2.Sample9System2DetailResponseEntity;
import com.example.webclient.domain.service.sample9WebclientMulti.entity.system2.Sample9System2ListRequestEntity;
import com.example.webclient.domain.service.sample9WebclientMulti.entity.system2.Sample9System2ListResponseEntity;

import jakarta.inject.Inject;
import reactor.core.publisher.Mono;

@Repository
public class Sample9System2RepositoryImpl implements Sample9System2Repository {

	private static final Logger logger = LoggerFactory.getLogger(Sample9System2RepositoryImpl.class);

	@Inject
	private WebClient webClientSystem2;

	@Override
	public Sample9System2ListResponseEntity callApiSystem2List(Sample9System2ListRequestEntity requestEntity) {
        String json = webClientSystem2.get()
                .uri("/8")
                .retrieve()
                .bodyToMono(String.class)
                .block();
        Sample9System2ListResponseEntity responseEntity = new Sample9System2ListResponseEntity();
        responseEntity.setResult(json);
        return responseEntity;
	}

	@Override
	public Sample9System2DetailResponseEntity callApiSystem2Detail(Sample9System2DetailRequestEntity requestEntity) {
        String json = webClientSystem2.get()
                .uri("/9")
                .retrieve()
                .bodyToMono(String.class)
                .block();
        Sample9System2DetailResponseEntity responseEntity = new Sample9System2DetailResponseEntity();
        responseEntity.setResult(json);
        return responseEntity;
	}

	@Override
	public Mono<Sample9System2ListResponseEntity> callAsyncApiSystem2List(Sample9System2ListRequestEntity requestEntity) {
		logger.debug("callAsyncApiSystem2List-1 " + Thread.currentThread().getName());
        return webClientSystem2.get()
                .uri("/8")
                .retrieve()
                .bodyToMono(String.class)
                .map(string -> {
                	logger.debug("callAsyncApiSystem2List-2 " + Thread.currentThread().getName());
                	Sample9System2ListResponseEntity responseEntity = new Sample9System2ListResponseEntity();
                	responseEntity.setResult(string);
                	return responseEntity;
                }).doOnError(t -> {
                	logger.error("callAsyncApiSystem1List doOnError", t);
//                	throw new Sapmle9ApiException("通信エラー", t);
                });
	}

	@Override
	public CompletableFuture<String> callAsyncApiSystem2Detail(Sample9System2DetailRequestEntity requestEntity) {
        return webClientSystem2.get()
                .uri("/9")
                .retrieve()
                .bodyToMono(String.class)
                .toFuture();
	}

}
