package com.example.webclient.domain.service.sample9WebclientMulti.apirepositoory;

import java.util.concurrent.CompletableFuture;

import com.example.webclient.domain.service.sample9WebclientMulti.entity.system2.Sample9System2DetailRequestEntity;
import com.example.webclient.domain.service.sample9WebclientMulti.entity.system2.Sample9System2DetailResponseEntity;
import com.example.webclient.domain.service.sample9WebclientMulti.entity.system2.Sample9System2ListRequestEntity;
import com.example.webclient.domain.service.sample9WebclientMulti.entity.system2.Sample9System2ListResponseEntity;

import reactor.core.publisher.Mono;

public interface Sample9System2Repository {

    public Sample9System2ListResponseEntity callApiSystem2List(Sample9System2ListRequestEntity requestEntity);

    public Sample9System2DetailResponseEntity callApiSystem2Detail(Sample9System2DetailRequestEntity requestEntity);

    public Mono<Sample9System2ListResponseEntity> callAsyncApiSystem2List(Sample9System2ListRequestEntity requestEntity);

    public CompletableFuture<String> callAsyncApiSystem2Detail(Sample9System2DetailRequestEntity requestEntity);

}
