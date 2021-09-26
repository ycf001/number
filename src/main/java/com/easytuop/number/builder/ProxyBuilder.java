package com.easytuop.number.builder;

import com.easytuop.number.conf.IpConf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;

/**
 * @author beck.yang
 * @date 2021/8/9 2:05 下午
 * @description 代理创建
 */
@Component
public class ProxyBuilder {
	private final IpConf ipConf;

	@Autowired
	public ProxyBuilder(IpConf ipConf) {
		this.ipConf = ipConf;
	}

	public Proxy build(String proxyHost, int proxyPort) {
		// http代理: Proxy.Type.HTTP, socks代理: Proxy.Type.SOCKS
		Proxy.Type proxyType = Proxy.Type.HTTP;

		// 设置验证
		Authenticator.setDefault(new ProxyAuthenticator(ipConf.getUser(), ipConf.getPass()));

		// 创建代理服务器
		InetSocketAddress addr = new InetSocketAddress(proxyHost, proxyPort);
		return new Proxy(proxyType, addr);
	}

	static class ProxyAuthenticator extends Authenticator {
		private String authUser, authPwd;

		public ProxyAuthenticator(String authUser, String authPwd) {
			this.authUser = authUser;
			this.authPwd = authPwd;
		}

		public PasswordAuthentication getPasswordAuthentication() {
			return (new PasswordAuthentication(authUser, authPwd.toCharArray()));
		}
	}
}
