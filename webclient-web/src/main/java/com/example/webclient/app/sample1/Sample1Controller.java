package com.example.webclient.app.sample1;

import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.webclient.domain.service.sample1.WebClientExample;

import jakarta.inject.Inject;

@RestController
public class Sample1Controller {

	@Inject
	WebClientExample example;
	
	@Value("${test.key}")
	private String xxx;

	@GetMapping("/sample1")
	public Sample1ResponseResource sample1() {
		Sample1ResponseResource resource = new Sample1ResponseResource();
		resource.setItem1("item1Value");
		resource.setItem2("item2Value");
		System.out.println("property-test xxx=" + xxx);
		
//		ResourceBundle rb = ResourceBundle.getBundle("xxx");
		ResourceBundle rb = ResourceBundle.getBundle("ValidationMessages");
		String string = rb.getString("msg1");
		System.out.println("ResourceBundle xxx=" + string);

		ResourceBundle rb999 = ResourceBundle.getBundle("ValidationMessages999");
		String string999 = rb999.getString("msg999");
		System.out.println("ResourceBundle xxx=" + string999);
		return resource;
	}
}
