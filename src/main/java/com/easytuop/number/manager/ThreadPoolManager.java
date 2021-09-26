package com.easytuop.number.manager;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池管理工具类
 *
 * @author Caesar.Hou
 */
@Component
public class ThreadPoolManager {
	/**
	 * 初始化Sender线程池
	 */
	public ExecutorService initSenderPool(String threadName, int coreSize, int maxSize, Long keepAlive) {
		ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat(threadName + "-%d").build();
		return new ThreadPoolExecutor(coreSize, maxSize, keepAlive, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(), namedThreadFactory);
	}
}