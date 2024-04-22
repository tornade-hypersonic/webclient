package com.example.webclient.app.sample7MockitoRule;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.webclient.domain.service.sample7.Sample7Service;

import jakarta.inject.Inject;

@RestController
public class Sample7Controller {

	@Inject
	public Sample7Service sample7Service;
	
	@GetMapping("sample7")
	public String sample7() {
		return sample7Service.sample7();
	}
	


}
