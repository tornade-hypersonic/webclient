package com.example.webclient.domain.repository;

import org.springframework.stereotype.Repository;

import com.example.webclient.domain.model.Member3;

@Repository
public interface Sample7Repository {

	Member3 findOneMember(String id);
}
