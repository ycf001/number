package com.easytuop.number.manager;

import lombok.Data;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author beck.yang
 * @date 2021/8/9 10:52 下午
 * @description
 */
@Data
public class NumberView {
	/**
	 * 总提交数量
	 */
	private AtomicInteger total = new AtomicInteger(0);
	/**
	 * 提交成功数量
	 */
	private AtomicInteger successTotal = new AtomicInteger(0);
	/**
	 * 失败数量
	 */
	private AtomicInteger failedTotal = new AtomicInteger(0);
	/**
	 * 重试数量
	 */
	private AtomicInteger retryTotal = new AtomicInteger(0);
}
