package com.easytuop.number.conf;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author beck.yang
 * @date 2021/8/9 4:57 下午
 * @description
 */
@Data
@Component
@ConfigurationProperties(prefix = "ip")
public class IpConf {
	private String url;
	private String user;
	private String pass;
}
