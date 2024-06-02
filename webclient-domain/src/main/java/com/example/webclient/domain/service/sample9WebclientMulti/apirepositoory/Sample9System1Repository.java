package com.example.webclient.domain.service.sample9WebclientMulti.apirepositoory;

import java.util.concurrent.CompletableFuture;

import com.example.webclient.domain.service.sample9WebclientMulti.entity.system1.Sample9System1DetailRequestEntity;
import com.example.webclient.domain.service.sample9WebclientMulti.entity.system1.Sample9System1DetailResponseEntity;
import com.example.webclient.domain.service.sample9WebclientMulti.entity.system1.Sample9System1ListRequestEntity;
import com.example.webclient.domain.service.sample9WebclientMulti.entity.system1.Sample9System1ListResponseEntity;

import reactor.core.publisher.Mono;

public interface Sample9System1Repository {

    public Sample9System1ListResponseEntity callApiSystem1List(Sample9System1ListRequestEntity requestEntity);

    public Sample9System1DetailResponseEntity callApiSystem1Detail(Sample9System1DetailRequestEntity requestEntity);

    public Mono<Sample9System1ListResponseEntity> callAsyncApiSystem1List(Sample9System1ListRequestEntity requestEntity);

    public CompletableFuture<String> callAsyncApiSystem1Detail(Sample9System1DetailRequestEntity requestEntity);

}
