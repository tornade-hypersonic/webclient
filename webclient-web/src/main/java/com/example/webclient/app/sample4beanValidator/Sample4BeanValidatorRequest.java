package com.example.webclient.app.sample4beanValidator;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class Sample4BeanValidatorRequest {

	@NotNull
	@Pattern(regexp = "[a-z]")
	private String name;
	private String email;
	private Integer age;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
}
