package com.example.webclient.app.sample5LoggerJunit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * static final で定義されたロガーをモック化できる。
 * 詳細は、Sample5ControllerTest.javaを参照
 * ただし、Systemクラスはモック化できないため、時間の計測結果を検証することはできない。
 * モック化できない事象は、Sample5ControllerTest2.javaを実行すると理解できる。
 */
@RestController
public class Sample5Controller {

	private static final Logger logger = LoggerFactory.getLogger(Sample5Controller.class);
	
	@GetMapping("sample5")
	public String sample5() {
		long start = System.currentTimeMillis();
		logger.info("start ");
		sleep(3000);
		logger.info("end " + (System.currentTimeMillis() - start));
		return "finish";
	}
	
	private void sleep(long mili) {
		try {
			Thread.sleep(mili);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
