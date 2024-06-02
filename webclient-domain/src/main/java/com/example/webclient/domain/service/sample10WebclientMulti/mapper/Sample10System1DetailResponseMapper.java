package com.example.webclient.domain.service.sample10WebclientMulti.mapper;

import org.springframework.stereotype.Component;

import com.example.webclient.domain.service.sample10WebclientMulti.entity.system1.Sample10System1DetailResponseEntity;

@Component
public class Sample10System1DetailResponseMapper {

	public Sample10System1DetailResponseEntity map(String xml) {
		Sample10System1DetailResponseEntity responseEntity = new Sample10System1DetailResponseEntity();
		responseEntity.setResult(xml);;
		return responseEntity;
	}
}
