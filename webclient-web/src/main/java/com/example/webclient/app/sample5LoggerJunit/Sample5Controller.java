package com.example.webclient.app.sample5LoggerJunit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Sample5Controller {

	private static final Logger logger = LoggerFactory.getLogger(Sample5Controller.class);
	
	@GetMapping("sample5")
	public String sample5() {
		logger.error("logger error test message " + System.currentTimeMillis());
		return null;
	}
}
