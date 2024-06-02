package com.example.webclient.app.sample9WebclientMulti;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.webclient.domain.service.sample9WebclientMulti.Sample9Service;

import jakarta.inject.Inject;

@RestController
public class Sample9Controller {

	@Inject
	private Sample9Service sample9Service;
	
	@GetMapping("sample9")
	public String aaa() {
		return this.sample9Service.getDataFromExternalSystems();
	}
}
