package com.example.webclient.app.sample4beanValidator;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import jakarta.inject.Inject;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:META-INF/spring/spring-mvc-test.xml",
		"classpath:META-INF/spring/test-context.xml"
})
public class Sample4BeanValidatorControllerTest {

	@Inject
	Sample4BeanValidatorController target;
	 
	MockMvc mockMvc;
	
	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.standaloneSetup(target).alwaysDo(log()).build();
	}
	
	@Test
	public void testaaa() throws Exception {
		System.out.println("Sample4BeanValidatorController");
		
		this.mockMvc
			.perform(
				get("/sample4/beanvalidator1")
				.param("name", "name-val1")
				.param("ename", "aaa@gmail.com")
				.param("age", "90")
			)
			.andExpect(status().isOk())
		;
	}
}
