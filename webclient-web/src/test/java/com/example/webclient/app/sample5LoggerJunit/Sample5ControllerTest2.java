package com.example.webclient.app.sample5LoggerJunit;

import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Sample5ControllerTestを改善して、System.currentTimeMillis()をモック化しようとしたが、
 * Systemクラスは特別な事情でモック化できなかった。
 * このクラスのテストを実行すると、その事情がわかる。
 */
@RunWith(MockitoJUnitRunner.class)
public class Sample5ControllerTest2 {

	Sample5Controller target;
	static MockedStatic<LoggerFactory> mockStaticLoggerFactory;
	static Logger mockLogger;
	
	static MockedStatic<System> mockStaticSystem;
	
	@BeforeClass
	public static void setupAll() {
		mockStaticLoggerFactory = mockStatic(LoggerFactory.class);
		mockLogger = Mockito.mock(Logger.class);
		mockStaticLoggerFactory
			.when(() -> LoggerFactory.getLogger(Sample5Controller.class))
			.thenReturn(mockLogger);
	}

	
	@Test
	public void test() {
		
        long startTime = 1000L;
        long endTime = 1200L;
        try (MockedStatic<System> mockedSystem = mockStatic(System.class)) {
            mockedSystem.when(System::currentTimeMillis)
                    .thenReturn(startTime)
                    .thenReturn(endTime);
            
    		target = new Sample5Controller();
    		target.sample5();
    		System.out.println(mockLogger.toString());
    		
    		ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
    		mockLogger.info(captor.capture());
    		verify(mockLogger, atLeast(1)).info(captor.capture());
    		List<String> allValues = captor.getAllValues();
    		allValues.forEach(value -> System.out.println(value));
        }
        
		
	}
}
