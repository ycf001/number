package com.easytuop.number.conf;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author beck.yang
 * @date 2021/8/9 11:17 下午
 * @description
 */
@Data
@Component
@ConfigurationProperties(prefix = "send")
public class SendConf {
	private Integer retry;
	private Integer formId;
	private Integer coreThread;
	private Integer maxThread;
	private Long keepAlive;
	private Integer maxSendBatch;
}
