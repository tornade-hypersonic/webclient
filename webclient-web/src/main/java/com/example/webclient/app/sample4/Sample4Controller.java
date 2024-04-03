package com.example.webclient.app.sample4;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RestController;

import jakarta.inject.Inject;

/**
 * 以下のURLでSample4Data1Validatorが実行される
 * 　http://localhost:8080/webclient-web/sample4/data1?item1=aaa&item2=bbb
 * 
 * 以下のURLでSample4Data2Validatorが実行される
 * 　http://localhost:8080/webclient-web/sample4/data2?item91=aaa&item92=bbb
 * 
 */
@RestController
public class Sample4Controller {

	@Inject
	Sample4Data1Validator sample4Data1Validator;
	@Inject
	Sample4Data2Validator sample4Data2Validator;
	
	@InitBinder("sample4Data1")
	public void initBinderSample4Data1Validator(WebDataBinder binder) {
		binder.addValidators(sample4Data1Validator);
	}
	@InitBinder("sample4Data2")
	public void initBinderSample4Data2Validator(WebDataBinder binder) {
		binder.addValidators(sample4Data2Validator);
	}
	
	@GetMapping("/sample4/data1")
	public void sample4Data1(@Validated Sample4Data1 sample4Data1) {
		System.out.println("Sample4Controller#sample4Data1()");
		System.out.println("sample4Data1 item1=" + sample4Data1.getItem1());
	}
	
	@GetMapping("/sample4/data2")
	public void sample4Data2(@Validated Sample4Data2 sample4Data2) {
		System.out.println("Sample4Controller#sample4Data2()");
		System.out.println("sample4Data2 item91=" + sample4Data2.getItem91());
	}
	
}
