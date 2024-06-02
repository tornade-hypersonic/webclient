package com.example.webclient.domain.service.sample10WebclientMulti;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.example.webclient.domain.service.sample10WebclientMulti.entity.Sample10Entity;
import com.example.webclient.domain.service.sample10WebclientMulti.entity.system1.Sample10System1DetailRequestEntity;
import com.example.webclient.domain.service.sample10WebclientMulti.entity.system1.Sample10System1DetailResponseEntity;
import com.example.webclient.domain.service.sample10WebclientMulti.entity.system1.Sample10System1ListRequestEntity;
import com.example.webclient.domain.service.sample10WebclientMulti.entity.system1.Sample10System1ListResponseEntity;
import com.example.webclient.domain.service.sample10WebclientMulti.entity.system2.Sample10System2DetailRequestEntity;
import com.example.webclient.domain.service.sample10WebclientMulti.entity.system2.Sample10System2DetailResponseEntity;
import com.example.webclient.domain.service.sample10WebclientMulti.entity.system2.Sample10System2ListRequestEntity;
import com.example.webclient.domain.service.sample10WebclientMulti.entity.system2.Sample10System2ListResponseEntity;
import com.example.webclient.domain.service.sample10WebclientMulti.mapper.Sample10System1DetailResponseMapper;
import com.example.webclient.domain.service.sample10WebclientMulti.mapper.Sample10System1ListResponseMapper;
import com.example.webclient.domain.service.sample10WebclientMulti.mapper.Sample10System2DetailResponseMapper;
import com.example.webclient.domain.service.sample10WebclientMulti.mapper.Sample10System2ListResponseMapper;

import jakarta.inject.Inject;

@Component
public class Sample10ApiResponseConverter {

	private static final Logger logger = LoggerFactory.getLogger(Sample10ApiResponseConverter.class);
	
	@Inject
	private Sample10System1ListResponseMapper system1ListResponseMapper;
	@Inject
	private Sample10System1DetailResponseMapper system1DetailResponseMapper;
	@Inject
	private Sample10System2ListResponseMapper system2ListResponseMapper;
	@Inject
	private Sample10System2DetailResponseMapper system2DetailResponseMapper;
	
	public Sample10Entity convert(@SuppressWarnings("rawtypes") CompletableFuture completableFuture, Sample10Entity request) {

		Object response = null;
		try {
			// ※警告の消し方がわからない。。。
			response = ((ResponseEntity) completableFuture.get()).getBody();
		} catch (InterruptedException | ExecutionException e) {
			// 通信障害とみなす
			logger.error("通信障害", e);
			return convertConnectError(request);
		}
		
		// Jacsonによる自動マッピングの場合、そのまま返却
		if (response instanceof Sample10Entity) {
			return (Sample10Entity) response;
		}
		
		// 上記以外は電文を文字列を前提とする
		// Stringにキャストできないことは考えない
		String telegram = (String) response;
		
		// 上記以外はStringなので、リクエストに応じてマッピング処理を行う
		if (request instanceof Sample10System1ListRequestEntity) {
			return system1ListResponseMapper.map(telegram);
		} else if (request instanceof Sample10System1DetailRequestEntity) {
			return system1DetailResponseMapper.map(telegram);
		} else if (request instanceof Sample10System2ListRequestEntity) {
			return system2ListResponseMapper.map(telegram);
		} else if (request instanceof Sample10System2DetailRequestEntity) {
			return system2DetailResponseMapper.map(telegram);
		}
		
		// 想定外のEntityなので、システムエラーとする
		throw new RuntimeException("convert() 設計上はありえない, ここに到達したらバグ");
		
	}
	
	
	private Sample10Entity convertConnectError(Sample10Entity request) {
		
		// Mapperに委譲するのがいいかもしれないが、とりあえず同じ処理をしておく
		if (request instanceof Sample10System1ListRequestEntity) {
			Sample10System1ListResponseEntity responseEntity = new Sample10System1ListResponseEntity();
			responseEntity.setResult("NG");
			return responseEntity;
		} else if (request instanceof Sample10System1DetailRequestEntity) {
			Sample10System1DetailResponseEntity responseEntity = new Sample10System1DetailResponseEntity();
			responseEntity.setResult("NG");
			return responseEntity;
		} else if (request instanceof Sample10System2ListRequestEntity) {
			Sample10System2ListResponseEntity responseEntity = new Sample10System2ListResponseEntity();
			responseEntity.setResult("NG");
			return responseEntity;
		} else if (request instanceof Sample10System2DetailRequestEntity) {
			Sample10System2DetailResponseEntity responseEntity = new Sample10System2DetailResponseEntity();
			responseEntity.setResult("NG");
			return responseEntity;
		}
		// 想定外のEntityなので、システムエラーとする
		throw new RuntimeException("convertConnectError() 設計上はありえない, ここに到達したらバグ");
	}
	
}
