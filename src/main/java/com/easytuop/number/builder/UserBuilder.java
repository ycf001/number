package com.easytuop.number.builder;

import com.easytuop.number.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author beck.yang
 * @date 2021/8/9 2:50 下午
 * @description
 */
@Component
public class UserBuilder {
	private final JsonUtils jsonUtils;

	@Autowired
	public UserBuilder(JsonUtils jsonUtils) {
		this.jsonUtils = jsonUtils;
	}

	public String build(String line) {
		List<Map<String, Object>> result = new ArrayList<>();
		String s = line.replaceAll(" ", "");
		String[] split = s.split("\t");
		if (split.length < 7) {
			System.out.println("User info length error!");
			return null;
		}
		for (int i = 1; i < 5; i++) {
			Map<String, Object> map = new HashMap<>();
			map.put("val", split[i]);
			map.put("id", i-1);
			result.add(map);
		}
		String addr = split[5] + "\n" + split[6].replaceAll("/", "");
		Map<String, Object> map = new HashMap<>();
		map.put("id", 4);
		map.put("val", addr);
		result.add(map);
		return jsonUtils.toJsonString(result);
	}
}
