package com.example.webclient.app.sample7MockitoRule;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;

import org.junit.Test;
import org.mockito.Mockito;

import com.example.webclient.domain.service.sample7.Sample7Service;

/**
 * DI機能を無視した試験。
 * テストケース毎にインスタンス生成とモック化処理を書く必要がある。
 */
public class Sample7ControllerTest1NoGood {

	Sample7Controller target;

	@Test
	public void test() throws Exception {
		// インスタンス生成
		target = new Sample7Controller();
		
		// モック化
		Sample7Service sample7Service = Mockito.mock(Sample7Service.class);
		doReturn("999").when(sample7Service).sample7();
		set(target, sample7Service);
		
		// 試験実施
		String actual = target.sample7();
		
		// アサート
		assertEquals("999", actual);
		
	}
	
	
	private void set(Object target, Object value) throws Exception {
		Field field = Sample7Controller.class.getField("sample7Service");
		field.setAccessible(true);
		field.set(target, value);
	}
}

