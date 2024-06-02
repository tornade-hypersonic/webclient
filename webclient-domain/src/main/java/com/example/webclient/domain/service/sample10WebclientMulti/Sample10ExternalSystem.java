package com.example.webclient.domain.service.sample10WebclientMulti;

import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

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
	
	private static final Logger logger = LoggerFactory.getLogger(Sample10ExternalSystem.class);

	@Inject
    private Sample10System1RepositoryImpl sample10System1Repository;
	@Inject
    private Sample10System2RepositoryImpl sample10System2Repository;
	
	@Inject
	private Sample10ApiResponseConverter responseConverter;
	
	public Sample10Entity callApi(Sample10Entity requestEntity) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		
		@SuppressWarnings("rawtypes") // 警告の消し方がわからなかったので強制的に出ないようにした
		CompletableFuture completableFuture = callApiAsync(requestEntity);
		Sample10Entity responseEntity = responseConverter.convert(completableFuture, requestEntity);
		
		stopWatch.stop();
		logger.info("通信時間：[{}]ミリ秒", stopWatch.getTotalTimeMillis());

		return responseEntity;
	}
	
	public Sample10Entity[] callMultiApi(Sample10Entity... requestEntities) {

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		
		// 非同期通信を実行
		@SuppressWarnings("rawtypes") // 警告の消し方がわからなかったので強制的に出ないようにした
		CompletableFuture[] completableFutures = new CompletableFuture[requestEntities.length];
		for (int i = 0; i < requestEntities.length; i++) {
			completableFutures[i] = callApiAsync(requestEntities[i]);
		}
		
		// 通信結果を受け取り　通信障害のハンドリングもconverterの中で実施
		Sample10Entity[] responseEntities = new Sample10Entity[10];
		for (int i = 0; i < completableFutures.length; i++) {
			responseEntities[i] = responseConverter.convert(completableFutures[i], requestEntities[i]);
		}
		
		stopWatch.stop();
		logger.info("通信時間：[{}]ミリ秒", stopWatch.getTotalTimeMillis());

		return responseEntities;
	}
	
	@SuppressWarnings("rawtypes") // 警告の消し方がわからなかったので強制的に出ないようにした
	private CompletableFuture callApiAsync(Sample10Entity requestEntity) {
		
		// リクエストEntityに合わせてAPIリポジトリを振り分ける
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
