package com.example.webclient.domain.service.sample3;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.webclient.domain.model.Member;
import com.example.webclient.domain.repository.Sample3Repository;

import jakarta.inject.Inject;

@Service
public class Sample3ServiceImpl implements Sample3Service {

	@Inject
	Sample3Repository sample3Repository;
	
	@Transactional(readOnly = true)
	public void find() {
		String id = "309915486";
		Member member = sample3Repository.findOneMember(id);
		System.out.println(member.getStatus());
		System.out.println(member.getKengen());
	}
	
}
