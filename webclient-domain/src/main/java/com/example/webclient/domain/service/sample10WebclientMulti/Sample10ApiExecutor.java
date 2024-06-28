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
public class Sample10ApiExecutor {

	@Inject
    private Sample10System1RepositoryImpl sample10System1Repository;
	@Inject
    private Sample10System2RepositoryImpl sample10System2Repository;
	
	@SuppressWarnings("rawtypes") // 警告の消し方がわからなかったので強制的に出ないようにした
	public CompletableFuture callApiAsync(Sample10Entity requestEntity) {
		
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
