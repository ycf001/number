package com.easytuop.number.ip;

import com.easytuop.number.client.HttpsClient;
import com.easytuop.number.conf.IpConf;
import com.easytuop.number.pojo.Ip;
import com.easytuop.number.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author beck.yang
 * @date 2021/8/9 4:46 下午
 * @description 代理IP生成工具
 */
@Component
public class IpPool {
	private static final String DATE_FORMAT = "yyyy-MM-dd hh:mm:ss";
	private final IpConf ipConf;
	private final HttpsClient httpsClient;
	private final JsonUtils jsonUtils;

	@Autowired
	public IpPool(IpConf ipConf, HttpsClient httpsClient, JsonUtils jsonUtils) {
		this.ipConf = ipConf;
		this.httpsClient = httpsClient;
		this.jsonUtils = jsonUtils;
	}

	public Ip generateSingleIp() {
		Set<Ip> ips = generateIpList(1);
		if (ips == null) {
			return null;
		}
		return ips.stream().findFirst().get();
	}

	public Set<Ip> generateIpList(Integer num) {
		Set<Ip> result = new HashSet<>();
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
			Map<String, String> param = new HashMap<>();
			param.put("method", "proxyServer.ipinfolist");
			param.put("packid", "0");
			param.put("fa", "0");
			param.put("fetch_key", "");
			param.put("time", "102");
			param.put("quantity", num.toString());
			param.put("province", "");
			param.put("city", "");
			param.put("anonymous", "1");
			param.put("ms", "1");
			param.put("service", "0");
			param.put("protocol", "1");
			param.put("distinct", "true");
			param.put("format", "json");
			param.put("separator", "3");
			param.put("separator_txt", "");
			String in = httpsClient.setGet(ipConf.getUrl(), param);
			if (in == null || in.length() == 0) {
				return null;
			}
			Map<String, Object> map = jsonUtils.parseObject(in);
			String code = map.get("code").toString();
			if (!code.equals("200")) {
				return null;
			}
			Map<String, Object> data = (Map<String, Object>) map.get("data");
			Map<String, Object> list = (Map<String, Object>) data.get("list");
			List<Map<String, Object>> ipInfoList = (List<Map<String, Object>>) list.get("ProxyIpInfoList");
			if (ipInfoList != null && ipInfoList.size() > 0) {
				for (Map<String, Object> ipInfo : ipInfoList) {
					Ip ip = new Ip();
					ip.setIp((String) ipInfo.get("IP"));
					ip.setPort((Integer) ipInfo.get("Port"));
					ip.setAddr((String) ipInfo.get("IpAddress"));
					ip.setIsp((String) ipInfo.get("ISP"));
					ip.setExpire(sdf.parse((String) ipInfo.get("IpExpireTimeRemainSeconds")).getTime());
					result.add(ip);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}
