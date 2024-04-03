package com.example.webclient.domain.repository;

import org.springframework.stereotype.Repository;

import com.example.webclient.domain.model.Member;

@Repository
public interface Sample3Repository {

	Member findOneMember(String id);
}
