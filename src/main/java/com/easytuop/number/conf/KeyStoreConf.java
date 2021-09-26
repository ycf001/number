package com.easytuop.number.conf;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author beck.yang
 * @date 2021/8/8 7:43 下午
 * @description keyStore配置文件
 */
@Data
@Component
@ConfigurationProperties(prefix = "key-store")
public class KeyStoreConf {
	private String url;
	private String filePath;
	private String password;
}
