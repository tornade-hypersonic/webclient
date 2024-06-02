package com.example.webclient.domain.service.sample9WebclientMulti.apirepositoory;

import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.webclient.domain.service.sample9WebclientMulti.entity.system1.Sample9System1DetailRequestEntity;
import com.example.webclient.domain.service.sample9WebclientMulti.entity.system1.Sample9System1DetailResponseEntity;
import com.example.webclient.domain.service.sample9WebclientMulti.entity.system1.Sample9System1ListRequestEntity;
import com.example.webclient.domain.service.sample9WebclientMulti.entity.system1.Sample9System1ListResponseEntity;

import jakarta.inject.Inject;
import reactor.core.publisher.Mono;

@Repository
public class Sample9System1RepositoryImpl implements Sample9System1Repository {

	private static final Logger logger = LoggerFactory.getLogger(Sample9System1RepositoryImpl.class);

	@Inject
	private WebClient webClientSystem1;

	@Override
	public Sample9System1ListResponseEntity callApiSystem1List(Sample9System1ListRequestEntity requestEntity) {
        String json = webClientSystem1.get()
                .uri("/1")
                .retrieve()
                .bodyToMono(String.class)
                .block();
        Sample9System1ListResponseEntity responseEntity = new Sample9System1ListResponseEntity();
        responseEntity.setResult(json);
        return responseEntity;
	}

	@Override
	public Sample9System1DetailResponseEntity callApiSystem1Detail(Sample9System1DetailRequestEntity requestEntity) {
        String json = webClientSystem1.get()
                .uri("/2")
                .retrieve()
                .bodyToMono(String.class)
                .block();
        Sample9System1DetailResponseEntity responseEntity = new Sample9System1DetailResponseEntity();
        responseEntity.setResult(json);
        return responseEntity;
	}

	
	public Mono<Sample9System1ListResponseEntity> callAsyncApiSystem1List(Sample9System1ListRequestEntity requestEntity) {
		logger.debug("callAsyncApiSystem1List-1 " + Thread.currentThread().getName());
        return webClientSystem1.get()
                .uri("/1")
                .retrieve()
                .bodyToMono(String.class)
                .map(string -> {
                	logger.debug("callAsyncApiSystem1List-2 " + Thread.currentThread().getName());
                	Sample9System1ListResponseEntity responseEntity = new Sample9System1ListResponseEntity();
                	responseEntity.setResult(string);
                	return responseEntity;
                }).doOnError(t -> {
                	logger.error("callAsyncApiSystem1List doOnError", t);
//                	throw new Sapmle9ApiException("通信エラー", t);
                });
	}

	public CompletableFuture<String> callAsyncApiSystem1Detail(Sample9System1DetailRequestEntity requestEntity) {
        CompletableFuture<String> future = webClientSystem1.get()
                .uri("/2")
                .retrieve()
                .bodyToMono(String.class)
                .toFuture();
        return future;
	}


}
