package com.example.webclient.domain.service.sample2;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.webclient.domain.model.Member;
import com.example.webclient.domain.repository.Sample2Repository;

import jakarta.inject.Inject;

@Service
public class Sample2ServiceImpl implements Sample2Service {

	@Inject
	Sample2Repository sample2Repository;
	
	@Transactional(readOnly = true)
	public void find() {
		String id = "309915486";
		Member member = sample2Repository.findOneMember(id);
		System.out.println(member.getStatus().getValue());
		System.out.println(member.getKengen().getValue());
	}
	
}
