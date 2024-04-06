package com.example.webclient.app.sample5LoggerJunit;

import static org.mockito.Mockito.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Sample5ControllerTest {

	static MockedStatic<LoggerFactory> mockStaticLoggerFactory;
	static Logger mockLogger;
	
	@BeforeClass
	public static void setupAll() {
		System.out.println("setupAll");
		mockStaticLoggerFactory = mockStatic(LoggerFactory.class);
		mockLogger = Mockito.mock(Logger.class);
		
		mockStaticLoggerFactory
			.when(() -> LoggerFactory.getLogger(Sample5Controller.class))
			.thenReturn(mockLogger);
	}
	
	@AfterClass
	public static void teardown() {
		System.out.println("teardown");
		mockStaticLoggerFactory.close();
	}
	
	@Test
	public void test1() {
		Sample5Controller target = new Sample5Controller();
		target.sample5();
		ArgumentCaptor<String> loggerCaptor = ArgumentCaptor.forClass(String.class);
		verify(mockLogger, atLeast(1)).info(loggerCaptor.capture());
		String argument = loggerCaptor.getValue();
		System.out.println(argument);
	}

	@Test
	public void test2() {
		Sample5Controller target = new Sample5Controller();
		target.sample5();
		ArgumentCaptor<String> loggerCaptor = ArgumentCaptor.forClass(String.class);
		verify(mockLogger, atLeast(1)).info(loggerCaptor.capture());
		String argument = loggerCaptor.getValue();
		System.out.println(argument);
	}

}
