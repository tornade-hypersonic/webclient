package com.example.webclient.domain.service.sample10WebclientMulti.mapper;

import org.springframework.stereotype.Component;

import com.example.webclient.domain.service.sample10WebclientMulti.entity.system2.Sample10System2ListResponseEntity;

@Component
public class Sample10System2ListResponseMapper {

	public Sample10System2ListResponseEntity map(String xml) {
		Sample10System2ListResponseEntity responseEntity = new Sample10System2ListResponseEntity();
		responseEntity.setResult(xml);;
		return responseEntity;
	}
}
