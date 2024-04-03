package com.example.webclient.app.sample4;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class Sample4Data2Validator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return Sample4Data2.class.isAssignableFrom(clazz);
	}
	
	@Override
	public void validate(Object target, Errors errors) {
		
		Sample4Data2 Sample4Data2 = Sample4Data2.class.cast(target);
		System.out.println("Sample4Data2Validator#validate");
		System.out.println(Sample4Data2.getItem91());
		System.out.println(Sample4Data2.getItem92());
		
	}

}
