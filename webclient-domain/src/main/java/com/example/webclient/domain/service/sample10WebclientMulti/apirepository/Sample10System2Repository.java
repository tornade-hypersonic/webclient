package com.example.webclient.domain.service.sample10WebclientMulti.apirepository;

import java.util.concurrent.CompletableFuture;

import org.springframework.http.ResponseEntity;

import com.example.webclient.domain.service.sample10WebclientMulti.entity.system2.Sample10System2DetailRequestEntity;
import com.example.webclient.domain.service.sample10WebclientMulti.entity.system2.Sample10System2DetailResponseEntity;
import com.example.webclient.domain.service.sample10WebclientMulti.entity.system2.Sample10System2ListRequestEntity;
import com.example.webclient.domain.service.sample10WebclientMulti.entity.system2.Sample10System2ListResponseEntity;

public interface Sample10System2Repository {

    public Sample10System2ListResponseEntity callApiSystem2List(Sample10System2ListRequestEntity requestEntity);

    public Sample10System2DetailResponseEntity callApiSystem2Detail(Sample10System2DetailRequestEntity requestEntity);

    public CompletableFuture<ResponseEntity<String>> callAsyncApiSystem2List(Sample10System2ListRequestEntity requestEntity);

    public CompletableFuture<ResponseEntity<String>> callAsyncApiSystem2Detail(Sample10System2DetailRequestEntity requestEntity);

}
