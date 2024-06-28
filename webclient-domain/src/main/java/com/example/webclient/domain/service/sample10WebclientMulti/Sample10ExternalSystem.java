package com.example.webclient.domain.service.sample10WebclientMulti;

import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Component;

import com.example.webclient.domain.service.sample10WebclientMulti.entity.Sample10Entity;

import jakarta.inject.Inject;

@Component
public class Sample10ExternalSystem {
	
	@Inject
	private Sample10ApiExecutor apiExecutor;
	
	@Inject
	private Sample10ApiResponseConverter responseConverter;
	
	public Sample10Entity callApi(Sample10Entity requestEntity) {
		
		@SuppressWarnings("rawtypes") // 警告の消し方がわからなかったので強制的に出ないようにした
		CompletableFuture completableFuture = apiExecutor.callApiAsync(requestEntity);
		Sample10Entity responseEntity = responseConverter.convert(completableFuture, requestEntity);
		
		return responseEntity;
	}
	
	public Sample10Entity[] callMultiApi(Sample10Entity... requestEntities) {

		// 非同期通信を実行
		@SuppressWarnings("rawtypes") // 警告の消し方がわからなかったので強制的に出ないようにした
		CompletableFuture[] completableFutures = new CompletableFuture[requestEntities.length];
		for (int i = 0; i < requestEntities.length; i++) {
			completableFutures[i] = apiExecutor.callApiAsync(requestEntities[i]);
		}
		
		// 通信結果を受け取り　通信障害のハンドリングもconverterの中で実施
		Sample10Entity[] responseEntities = new Sample10Entity[10];
		for (int i = 0; i < completableFutures.length; i++) {
			responseEntities[i] = responseConverter.convert(completableFutures[i], requestEntities[i]);
		}
		
		return responseEntities;
	}
	
}
