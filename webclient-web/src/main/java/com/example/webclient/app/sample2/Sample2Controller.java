package com.example.webclient.app.sample2;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.webclient.domain.service.sample2.Sample2Service;

import jakarta.inject.Inject;

@RestController
public class Sample2Controller {

	@Inject
	Sample2Service sample2Service;
	
	@GetMapping("/sample2")
	public void sample2() {
		sample2Service.find();
	}
}
