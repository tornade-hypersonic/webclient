package com.example.webclient.app.sample4beanValidator;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Sample4BeanValidatorController {

//	@Inject
//	Sample4BeanValidator sample4BeanValidator;
//	
//	@InitBinder("sample4Data1")
//	public void initBinderSample4BeanValidator(WebDataBinder binder) {
//		binder.addValidators(sample4BeanValidator);
//	}
	
	@GetMapping("/sample4/beanvalidator1")
	public void sample4Beanvalidator1(@Validated Sample4BeanValidatorRequest sample4Data1) {
		System.out.println("Sample4Controller#sample4Beanvalidator1()");
		
	}
	
}
