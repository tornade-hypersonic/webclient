package com.example.webclient.domain.service.sample9WebclientMulti;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.webclient.domain.service.sample9WebclientMulti.apirepositoory.Sample9System1RepositoryImpl;
import com.example.webclient.domain.service.sample9WebclientMulti.apirepositoory.Sample9System2RepositoryImpl;
import com.example.webclient.domain.service.sample9WebclientMulti.entity.system1.Sample9System1ListResponseEntity;
import com.example.webclient.domain.service.sample9WebclientMulti.entity.system2.Sample9System2ListResponseEntity;
import com.example.webclient.domain.service.sample9WebclientMulti.exception.Sapmle9ApiException;

import jakarta.inject.Inject;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

@Service
public class Sample9ServiceImpl implements Sample9Service {

	private static final Logger logger = LoggerFactory.getLogger(Sample9ServiceImpl.class);
	
	@Inject
    private Sample9System1RepositoryImpl sample9System1Repository;
	@Inject
    private Sample9System2RepositoryImpl sample9System2Repository;

//    public Sample9ServiceImpl(
//    		Sample9System1RepositoryImpl sample9System1Repository, 
//    		Sample9System2RepositoryImpl sample9System2Repository) {
//        this.sample9System1Repository = sample9System1Repository;
//        this.sample9System2Repository = sample9System2Repository;
//    }

    public String getDataFromSystem1() {
    	Sample9System1ListResponseEntity responseEntity = sample9System1Repository.callApiSystem1List(null);
    	return responseEntity.getResult();
    }

    public String getDataFromSystem2() {
    	Sample9System2ListResponseEntity responseEntity = sample9System2Repository.callApiSystem2List(null);
    	return responseEntity.getResult();
    }	
    
    public String getDataFromExternalSystems() {
    	
    	logger.debug("getDataFromExternalSystems-1 " + Thread.currentThread().getName());
    	
    	Mono<Sample9System1ListResponseEntity> sys1Res = sample9System1Repository.callAsyncApiSystem1List(null);
    	Mono<Sample9System2ListResponseEntity> sys2Res = sample9System2Repository.callAsyncApiSystem2List(null);
    	
   		logger.debug("getDataFromExternalSystems-2 " + Thread.currentThread().getName());

   		Tuple2<Sample9System1ListResponseEntity,Sample9System2ListResponseEntity> tubple = null;
   		try {
   			// TODO 通信エラーが発生したとき、block()でも例外が発生する
   			// → Entityにマッピングして返却する方法が厳しい
   	    	tubple = Mono.zip(sys1Res, sys2Res).block();
    	} catch (Sapmle9ApiException e) {
    		logger.error("通信エラー１");
    		return "connect error";
    	}
   		
    	Sample9System1ListResponseEntity sample9System1ListResponseEntity = tubple.getT1();
    	Sample9System2ListResponseEntity sample9System2ListResponseEntity = tubple.getT2();
    	
    	
    	logger.debug("getDataFromExternalSystems-3 " + Thread.currentThread().getName());

    	return sample9System1ListResponseEntity.getResult() + "\n" + sample9System2ListResponseEntity.getResult();
    };
    
    
    
}
