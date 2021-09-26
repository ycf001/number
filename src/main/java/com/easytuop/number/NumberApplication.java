package com.easytuop.number;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author beck.yang
 * @description 程序启动主类
 */
@SpringBootApplication(scanBasePackages = {"com.easytuop.number"})
public class NumberApplication {
	/**
	 * 程序入口
	 *
	 * @param args 启动参数
	 */
	public static void main(String[] args) {
		SpringApplication.run(NumberApplication.class, args);
	}
}
