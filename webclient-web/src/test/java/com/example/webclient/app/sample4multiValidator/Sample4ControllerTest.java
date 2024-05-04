package com.example.webclient.app.sample4multiValidator;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import jakarta.inject.Inject;

/**
 * MockMvcを用いてControllerのテストを行う。
 * ・Validatorが動作する。
 * ・HttpステータスとJsonの検証が可能である。
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
//		"classpath:META-INF/spring/applicationContext.xml",
		"classpath:META-INF/spring/test-context.xml",
		"classpath:META-INF/spring/spring-mvc-test.xml"
})
public class Sample4ControllerTest {
	
	@Inject
	Sample4Controller target;
	
	MockMvc mockMvc;
	
	@Before
	public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(target).alwaysDo(log()).build();	
	}

	@Test
	public void test() throws Exception {
		
		// setup and run the test
		mockMvc.perform(
			get("/sample4/data1")
	            .param("item1", "aaa")
	            .param("item2", "bbb")
	        )
            // assert
            .andExpect(status().is(200))
            .andExpect(MockMvcResultMatchers.jsonPath("$.item1Response").value("aaa response!"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.item2Response").value("bbb response!"))
            ;
    }
}
