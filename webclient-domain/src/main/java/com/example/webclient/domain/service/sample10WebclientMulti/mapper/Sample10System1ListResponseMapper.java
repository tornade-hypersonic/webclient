package com.example.webclient.domain.service.sample10WebclientMulti.mapper;

import org.springframework.stereotype.Component;

import com.example.webclient.domain.service.sample10WebclientMulti.entity.system1.Sample10System1ListResponseEntity;

@Component
public class Sample10System1ListResponseMapper {

	public Sample10System1ListResponseEntity map(String xml) {
		Sample10System1ListResponseEntity responseEntity = new Sample10System1ListResponseEntity();
		responseEntity.setResult(xml);;
		return responseEntity;
	}
}
