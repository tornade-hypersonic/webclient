package com.example.webclient.domain.service.sample7;

import org.springframework.stereotype.Service;

import com.example.webclient.domain.model.Member3;
import com.example.webclient.domain.repository.Sample7Repository;

import jakarta.inject.Inject;

@Service
public class Sample7ServiceImpl implements Sample7Service {

	@Inject
	Sample7Repository sample7Repository;
	
	public String sample7() {
		
		Member3 member = sample7Repository.findOneMember("");
		return member.getName();
	}
}
