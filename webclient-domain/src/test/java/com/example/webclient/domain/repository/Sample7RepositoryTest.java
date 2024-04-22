package com.example.webclient.domain.repository;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.example.webclient.domain.model.Member3;

import jakarta.inject.Inject;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:META-INF/spring/test-webclient-infra.xml",
		"classpath:test-context.xml"
		})
public class Sample7RepositoryTest {

	@Inject
	Sample7Repository target;
	
	@Test
	public void test() {
		String id = "309915486";
		Member3 member = target.findOneMember(id);
		assertNotNull(member);;
	}
}
