package com.example.webclient.app.sample6StaticTwoArgumentJunit;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;

/**
 * staticメソッドの引数が2つ存在するケース
 * Answerで引数に応じた振る舞いを定義できるか、その中で引数をアサートする手法
 * ArgumentCaptorを使用したかったが、verify()が使用できなさそうだったので、あきらめた。。。→test()
 * 
 * staticでもverify()が使用できた。キャプチャも使用できた
 * →test1()
 */
public class Sample6ControllerTest {

	@Test
	public void test() {
		
		String expectedItem1 = "val-item11";
		String expectedItem2 = "val-item22";
		
		MockedStatic<Sample6Static> mockStaticSample6Static = mockStatic(Sample6Static.class);
		mockStaticSample6Static
			.when(() -> Sample6Static.aaa(any(Sample6StaticParam.class), anyBoolean()))
			.thenAnswer(invocation -> {
                // 引数を取得して検証
                Sample6StaticParam param = invocation.getArgument(0);
                boolean flag = invocation.getArgument(1);
                assertEquals(expectedItem1, param.getItem1());
                assertEquals(expectedItem2, param.getItem2());
                assertEquals(false, flag);
                return null; // 任意の戻り値
             });
		
		Sample6Controller target = new Sample6Controller();
		target.sample6();
		
		mockStaticSample6Static.close();
		
	}


	@Test
	public void test1() {
		
		String expectedItem1 = "val-item11";
		String expectedItem2 = "val-item22";
		
		// モック化
		MockedStatic<Sample6Static> mockStatic = mockStatic(Sample6Static.class);
		mockStatic.when(() -> Sample6Static.aaa(any(), anyBoolean())).thenReturn("mock-result");
		
		// 実行
		Sample6Controller target = new Sample6Controller();
		String sample6 = target.sample6();
		System.out.println(sample6);
		
		// キャプチャ
		ArgumentCaptor<Sample6StaticParam> paramCaptor = ArgumentCaptor.forClass(Sample6StaticParam.class);
		ArgumentCaptor<Boolean> boolcaptor = ArgumentCaptor.forClass(Boolean.class);
		mockStatic.verify(() -> Sample6Static.aaa(paramCaptor.capture(), boolcaptor.capture()));
		
		// アサート
		assertEquals(paramCaptor.getValue().getItem1(), expectedItem1);
		assertEquals(paramCaptor.getValue().getItem2(), expectedItem2);
		assertEquals(boolcaptor.getValue(), false);

		mockStatic.close();
	}
	
}
