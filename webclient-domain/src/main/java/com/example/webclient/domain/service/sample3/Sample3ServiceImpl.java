package com.example.webclient.domain.service.sample3;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.webclient.domain.model.Member3;
import com.example.webclient.domain.repository.Sample3Repository;

import jakarta.inject.Inject;

/**
 * Enumの汎用的なTypeHandlerを使用する
 * Enumは EnumBaseを継承する
 * 
 * EnumValueTypeHandlerではテーブルのCHAR/VARCHARを使用する
 * テーブルのnumberを使用するとエラーとなる
 * rs.getString(カラム名) としているため。
 */
@Service
public class Sample3ServiceImpl implements Sample3Service {

	@Inject
	Sample3Repository sample3Repository;
	
	@Transactional(readOnly = true)
	public void find() {
		String id = "309915486";
		Member3 member3 = sample3Repository.findOneMember(id);
		System.out.println(member3.getStatus().getValue());
		System.out.println(member3.getKengen().getValue());
	}
	
}
