package com.example.webclient.domain.service.sample10WebclientMulti;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.webclient.domain.service.sample10WebclientMulti.entity.Sample10Entity;
import com.example.webclient.domain.service.sample10WebclientMulti.entity.system1.Sample10System1DetailRequestEntity;
import com.example.webclient.domain.service.sample10WebclientMulti.entity.system1.Sample10System1DetailResponseEntity;
import com.example.webclient.domain.service.sample10WebclientMulti.entity.system1.Sample10System1ListRequestEntity;
import com.example.webclient.domain.service.sample10WebclientMulti.entity.system1.Sample10System1ListResponseEntity;
import com.example.webclient.domain.service.sample10WebclientMulti.entity.system2.Sample10System2DetailRequestEntity;
import com.example.webclient.domain.service.sample10WebclientMulti.entity.system2.Sample10System2DetailResponseEntity;
import com.example.webclient.domain.service.sample10WebclientMulti.entity.system2.Sample10System2ListRequestEntity;
import com.example.webclient.domain.service.sample10WebclientMulti.entity.system2.Sample10System2ListResponseEntity;

import jakarta.inject.Inject;

@Service
public class Sample10ServiceImpl implements Sample10Service {

	private static final Logger logger = LoggerFactory.getLogger(Sample10ServiceImpl.class);
	
	@Inject
    private Sample10ExternalSystem externalSystem;

    public String getDataFromExternalSystems() {
    	
    	// 単品の通信
    	Sample10System1ListResponseEntity callApi = (Sample10System1ListResponseEntity) externalSystem.callApi(new Sample10System1ListRequestEntity());
    	logger.debug(callApi.getResult());

    	// 複数同時の通信
    	Sample10System1ListRequestEntity system1ListRequestEntity = new Sample10System1ListRequestEntity();
    	Sample10System1DetailRequestEntity system1DetailRequestEntity = new Sample10System1DetailRequestEntity();
    	Sample10System2ListRequestEntity system2ListRequestEntity = new Sample10System2ListRequestEntity();
    	Sample10System2DetailRequestEntity system2DetailRequestEntity = new Sample10System2DetailRequestEntity();
    	
    	Sample10Entity[] responseEntities = externalSystem.callMultiApi(system1ListRequestEntity, system1DetailRequestEntity, system2ListRequestEntity, system2DetailRequestEntity);
    	
    	Sample10System1ListResponseEntity sys1ListRes = (Sample10System1ListResponseEntity) responseEntities[0];
    	Sample10System1DetailResponseEntity sys1DetailRes = (Sample10System1DetailResponseEntity) responseEntities[1];
    	Sample10System2ListResponseEntity sys2ListRes = (Sample10System2ListResponseEntity) responseEntities[2];
    	Sample10System2DetailResponseEntity sys2DetailRes = (Sample10System2DetailResponseEntity) responseEntities[3];

    	logger.debug("sys1ListRes=" + sys1ListRes.getResult());
    	logger.debug("sys1DetailRes=" + sys1DetailRes.getResult());
    	logger.debug("sys2ListRes=" + sys2ListRes.getResult());
    	logger.debug("sys2DetailRes=" + sys2DetailRes.getResult());
    	
    	String join = 
    			sys1ListRes.getResult() + "\n" +
    			sys1DetailRes.getResult() + "\n" +
    			sys2ListRes.getResult() + "\n" +
    			sys2DetailRes.getResult();
    	
    	return join;
    };
    
}
