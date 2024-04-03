package com.example.webclient.app.sample4;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class Sample4Data1Validator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return Sample4Data1.class.isAssignableFrom(clazz);
	}
	
	@Override
	public void validate(Object target, Errors errors) {
		
		Sample4Data1 sample4Data1 = Sample4Data1.class.cast(target);
		System.out.println("Sample4Data1Validator#validate");
		System.out.println(sample4Data1.getItem1());
		System.out.println(sample4Data1.getItem2());
		
	}

}
