package com.example.webclient.domain.service.sample10WebclientMulti.apirepository;

import java.util.concurrent.CompletableFuture;

import org.springframework.http.ResponseEntity;

import com.example.webclient.domain.service.sample10WebclientMulti.entity.system1.Sample10System1DetailRequestEntity;
import com.example.webclient.domain.service.sample10WebclientMulti.entity.system1.Sample10System1DetailResponseEntity;
import com.example.webclient.domain.service.sample10WebclientMulti.entity.system1.Sample10System1ListRequestEntity;
import com.example.webclient.domain.service.sample10WebclientMulti.entity.system1.Sample10System1ListResponseEntity;

public interface Sample10System1Repository {

    public Sample10System1ListResponseEntity callApiSystem1List(Sample10System1ListRequestEntity requestEntity);

    public Sample10System1DetailResponseEntity callApiSystem1Detail(Sample10System1DetailRequestEntity requestEntity);

    public CompletableFuture<ResponseEntity<String>> callAsyncApiSystem1List(Sample10System1ListRequestEntity requestEntity);

    public CompletableFuture<ResponseEntity<String>> callAsyncApiSystem1Detail(Sample10System1DetailRequestEntity requestEntity);

}
