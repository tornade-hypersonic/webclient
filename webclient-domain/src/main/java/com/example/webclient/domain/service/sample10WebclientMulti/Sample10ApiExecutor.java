package com.example.webclient.domain.service.sample10WebclientMulti;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import org.springframework.stereotype.Component;

import com.example.webclient.domain.service.sample10WebclientMulti.apirepository.Sample10System1RepositoryImpl;
import com.example.webclient.domain.service.sample10WebclientMulti.apirepository.Sample10System2RepositoryImpl;
import com.example.webclient.domain.service.sample10WebclientMulti.entity.Sample10Entity;
import com.example.webclient.domain.service.sample10WebclientMulti.entity.system1.Sample10System1DetailRequestEntity;
import com.example.webclient.domain.service.sample10WebclientMulti.entity.system1.Sample10System1ListRequestEntity;
import com.example.webclient.domain.service.sample10WebclientMulti.entity.system2.Sample10System2DetailRequestEntity;
import com.example.webclient.domain.service.sample10WebclientMulti.entity.system2.Sample10System2ListRequestEntity;

import jakarta.inject.Inject;

@Component
public class Sample10ApiExecutor {

	@Inject
    private Sample10System1RepositoryImpl sample10System1Repository;
	@Inject
    private Sample10System2RepositoryImpl sample10System2Repository;
	
	
	private final Map<Class<?>, Function<Sample10Entity, CompletableFuture<?>>> apiCallers = new HashMap<>();
	
	public Sample10ApiExecutor() {

        apiCallers.put(Sample10System1ListRequestEntity.class, 
            entity -> sample10System1Repository.callAsyncApiSystem1List((Sample10System1ListRequestEntity) entity));
        apiCallers.put(Sample10System1DetailRequestEntity.class, 
            entity -> sample10System1Repository.callAsyncApiSystem1Detail((Sample10System1DetailRequestEntity) entity));
        apiCallers.put(Sample10System2ListRequestEntity.class, 
            entity -> sample10System2Repository.callAsyncApiSystem2List((Sample10System2ListRequestEntity) entity));
        apiCallers.put(Sample10System2DetailRequestEntity.class, 
            entity -> sample10System2Repository.callAsyncApiSystem2Detail((Sample10System2DetailRequestEntity) entity));
	}
	
    public CompletableFuture<?> callApiAsync(Sample10Entity requestEntity) {
        Function<Sample10Entity, CompletableFuture<?>> apiCaller = apiCallers.get(requestEntity.getClass());

        if (apiCaller != null) {
            return apiCaller.apply(requestEntity);
        } else {
            throw new RuntimeException("エンティティがありえない");
        }
    }	
}
