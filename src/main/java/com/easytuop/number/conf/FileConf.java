package com.easytuop.number.conf;

/**
 * @author beck.yang
 * @date 2021/8/9 10:39 下午
 * @description
 */

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
@ConfigurationProperties(prefix = "file")
public class FileConf {
	private String userFile;
	private String outPath;
}
