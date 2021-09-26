package com.easytuop.number.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static com.fasterxml.jackson.databind.SerializationFeature.FAIL_ON_EMPTY_BEANS;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;

/**
 * @author beck.yang
 * @date 2021/3/2 11:42 下午
 * @description json处理工具类
 */
@Slf4j
@Component
public class JsonUtils {

	private final ObjectMapper mapper = new ObjectMapper();

	public JsonUtils() {
		this.mapper.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
		this.mapper.configure(WRITE_DATES_AS_TIMESTAMPS, false);
		this.mapper.configure(FAIL_ON_EMPTY_BEANS, false);
		this.mapper.setSerializationInclusion(ALWAYS);
	}

	/**
	 * 对象转化字符串
	 *
	 * @param object 对象
	 * @return String
	 */
	public String toJsonString(Object object) {
		try {
			if (object == null) {
				return null;
			}
			if (object instanceof String) {
				return (String) object;
			}
			return mapper.writeValueAsString(object);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Json字符串转为Map对象
	 *
	 * @param json Json字符串
	 * @return Map对象
	 */
	public Map<String, Object> parseObject(String json) {
		try {
			return mapper.readValue(json, new TypeReference<Map<String, Object>>() {
			});
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
