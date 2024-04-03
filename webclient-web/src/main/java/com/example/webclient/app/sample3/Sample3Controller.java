package com.example.webclient.app.sample3;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.webclient.domain.service.sample3.Sample3Service;

import jakarta.inject.Inject;

@RestController
public class Sample3Controller {

	@Inject
	Sample3Service sample3Service;
	
	@GetMapping("/sample3")
	public void sample3() {
		sample3Service.find();
	}
}
