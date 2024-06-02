package com.example.webclient.domain.service.sample10WebclientMulti;

import java.util.concurrent.CompletableFuture;

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
public class Sample10ExternalSystem {

	@Inject
    private Sample10System1RepositoryImpl sample10System1Repository;
	@Inject
    private Sample10System2RepositoryImpl sample10System2Repository;
	
	@Inject
	private Sample10ApiResponseConverter responseConverter;
	
	public Sample10Entity callApi(Sample10Entity requestEntity) {
		CompletableFuture completableFuture = callApiAsync(requestEntity);
		Sample10Entity responseEntity = responseConverter.convert(completableFuture, requestEntity);
		return responseEntity;
	}
	
	
	public Sample10Entity[] callMultiApi(Sample10Entity... requestEntities) {
		
		// 非同期通信を実行
		CompletableFuture[] completableFutures = new CompletableFuture[requestEntities.length];
		for (int i = 0; i < requestEntities.length; i++) {
			completableFutures[i] = callApiAsync(requestEntities[i]);
		}
		
		// 通信結果を受け取り
		Sample10Entity[] responseEntities = new Sample10Entity[10];
		for (int i = 0; i < completableFutures.length; i++) {
			responseEntities[i] = responseConverter.convert(completableFutures[i], requestEntities[i]);
		}
		
		return responseEntities;
	}
	
	private CompletableFuture callApiAsync(Sample10Entity requestEntity) {
		
		if (requestEntity instanceof Sample10System1ListRequestEntity) {
			return sample10System1Repository.callAsyncApiSystem1List((Sample10System1ListRequestEntity) requestEntity);
		} else if (requestEntity instanceof Sample10System1DetailRequestEntity) {
			return sample10System1Repository.callAsyncApiSystem1Detail((Sample10System1DetailRequestEntity) requestEntity);
		} else if (requestEntity instanceof Sample10System2ListRequestEntity) {
			return sample10System2Repository.callAsyncApiSystem2List((Sample10System2ListRequestEntity) requestEntity);
		} else if (requestEntity instanceof Sample10System2DetailRequestEntity) {
			return sample10System2Repository.callAsyncApiSystem2Detail((Sample10System2DetailRequestEntity) requestEntity);
		}
		throw new RuntimeException("エンティティがありえない");
		
	}
	
}
