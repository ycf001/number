package com.easytuop.number.util;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

/**
 * @description Hostname校验工具类
 * @author beck.yang
 */
public class NullHostNameVerifier implements HostnameVerifier{

	@Override
	public boolean verify(String hostname, SSLSession session) {
		return true;
	}
}