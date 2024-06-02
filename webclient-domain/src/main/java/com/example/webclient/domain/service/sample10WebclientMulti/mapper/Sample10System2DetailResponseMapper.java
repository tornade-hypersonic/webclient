package com.example.webclient.domain.service.sample10WebclientMulti.mapper;

import org.springframework.stereotype.Component;

import com.example.webclient.domain.service.sample10WebclientMulti.entity.system2.Sample10System2DetailResponseEntity;

@Component
public class Sample10System2DetailResponseMapper {

	// 本来は、XMLの各項目をEntityにマッピングする
	public Sample10System2DetailResponseEntity map(String xml) {
		Sample10System2DetailResponseEntity responseEntity = new Sample10System2DetailResponseEntity();
		responseEntity.setResult(xml);;
		return responseEntity;
	}
}
