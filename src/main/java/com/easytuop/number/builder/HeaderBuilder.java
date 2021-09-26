package com.easytuop.number.builder;

import com.sun.tools.internal.xjc.Language;
import org.springframework.stereotype.Component;
import sun.management.Agent;
import sun.plugin2.gluegen.runtime.CPU;

import javax.crypto.Mac;
import java.util.HashMap;
import java.util.Map;

/**
 * @author beck.yang
 * @date 2021/8/9 2:11 下午
 * @description
 */
@Component
public class HeaderBuilder {

	public Map<String, String> build(String param) {
		Map<String, String> header = new HashMap<>();
		header.put("accept", "*/*");
		header.put("Connection", "Keep-Alive");
		header.put("Content-Length", String.valueOf(param.length()));
		header.put("content-type", "application/x-www-form-urlencoded");
		header.put("Referer", "https://servicewechat.com/wx508d8fbbaea7e235/63/page-frame.html");
		header.put("Accept-Encoding", "gzip, deflate, br");
		header.put("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 12_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/16A366 MicroMessenger/6.7.3(0x16070321) NetType/WIFI Language/zh_CN");
		header.put("Accept-Language", "zh-cn");
		return header;
	}

	public Map<String, String> buildVisitor() {
		Map<String, String> header = new HashMap<>();
		header.put("accept", "*/*");
		header.put("Connection", "Keep-Alive");
		header.put("content-type", "application/x-www-form-urlencoded");
		header.put("Referer", "https://servicewechat.com/wx508d8fbbaea7e235/63/page-frame.html");
		header.put("Accept-Encoding", "gzip, deflate, br");
		header.put("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 12_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/16A366 MicroMessenger/6.7.3(0x16070321) NetType/WIFI Language/zh_CN");
		header.put("Accept-Language", "zh-cn");
		return header;
	}

	public Map<String, String> buildQuery() {
		Map<String, String> header = new HashMap<>();
		header.put("accept", "*/*");
		header.put("Connection", "Keep-Alive");
		header.put("content-type", "application/x-www-form-urlencoded");
		header.put("Referer", "https://servicewechat.com/wx508d8fbbaea7e235/63/page-frame.html");
		header.put("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 12_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/16A366 MicroMessenger/6.7.3(0x16070321) NetType/WIFI Language/zh_CN");
		header.put("Accept-Language", "zh-cn");
		return header;
	}
}
