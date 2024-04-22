package com.example.webclient.app.sample6StaticTwoArgumentJunit;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Sample6Controller {

	
	@GetMapping("sample6")
	public String sample6() {
		Sample6StaticParam param = new Sample6StaticParam();
		param.setItem1("val-item11");
		param.setItem2("val-item22");
		Sample6Static.aaa(param, false);
		return null;
	}
	


}
