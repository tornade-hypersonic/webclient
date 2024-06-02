package com.example.webclient.app.sample10WebclientMulti;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.webclient.domain.service.sample10WebclientMulti.Sample10Service;

import jakarta.inject.Inject;

@RestController
public class Sample10Controller {

	@Inject
	private Sample10Service sample10Service;
	
	@GetMapping("sample10")
	public String aaa() {
		return this.sample10Service.getDataFromExternalSystems();
	}
}
