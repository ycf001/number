package com.easytuop.number.builder;

import com.easytuop.number.client.HttpsClient;
import com.easytuop.number.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author beck.yang
 * @date 2021/8/9 6:22 下午
 * @description
 */
@Component
public class ParamBuilder {
	@Autowired
	private JsonUtils jsonUtils;

	public String build(String user, Integer formId, String openId) {
		Map<String, String> body = new HashMap<>();
		body.put("aid", "25910146");
		body.put("wxappId", "101");
		body.put("wxappAid", "1844535");
		body.put("isOem", "false");
		body.put("from", "0");
		body.put("isModel", "undefined");
		body.put("wxappAppId", "wx508d8fbbaea7e235");
		body.put("cmd", "addWXAppFormSubmit");
		body.put("formId", formId.toString());
		body.put("colId", "121");
		body.put("sourceType", "0");
		body.put("tmpFileList", "[]");
		body.put("submitContentList", user);
		body.put("codeValidationList", "[]");
		body.put("openId", openId);
		return HttpsClient.map2Url(body);
	}

	public String buildLogData(int dogId, int srcId) {
		Map<String, String> body = new HashMap<>();
		body.put("aid", "25910146");
		body.put("wxappId", "101");
		body.put("wxappAid", "1844535");
		body.put("isOem", "false");
		body.put("from", "0");
		body.put("isModel", "undefined");
		body.put("wxappAppId", "wx508d8fbbaea7e235");
		body.put("cmd", "logDog");
		body.put("sourceOpenId", String.valueOf(dogId));
		body.put("srcId", String.valueOf(srcId));
		return HttpsClient.map2Url(body);
	}

	public String buildVisitor(String openId) {
		Map<String, String> body = new HashMap<>();
		body.put("aid", "25910146");
		body.put("wxappId", "101");
		body.put("wxappAid", "1844535");
		body.put("isOem", "false");
		body.put("from", "0");
		body.put("isModel", "undefined");
		body.put("wxappAppId", "wx508d8fbbaea7e235");
		body.put("cmd", "addVisitor");
		body.put("sourceOpenId", openId);
		body.put("openId", openId);
		return HttpsClient.map2Url(body);
	}

	public String buildGetFormSubmit(String openId) {
		Map<String, String> body = new HashMap<>();
		body.put("aid", "25910146");
		body.put("wxappId", "101");
		body.put("wxappAid", "1844535");
		body.put("isOem", "false");
		body.put("from", "0");
		body.put("isModel", "undefined");
		body.put("wxappAppId", "wx508d8fbbaea7e235");
		body.put("start", "0");
		body.put("cmd", "getFormSubmitList");
		body.put("openId", openId);
		return HttpsClient.map2Url(body);
	}
	public String buildLatestInfoStatus(String openId) {
		Map<String, String> body = new HashMap<>();
		body.put("aid", "25910146");
		body.put("wxappId", "101");
		body.put("wxappAid", "1844535");
		body.put("isOem", "false");
		body.put("from", "0");
		body.put("isModel", "undefined");
		body.put("wxappAppId", "wx508d8fbbaea7e235");
		body.put("cmd", "getLatestInfoStatus");
		body.put("openId", openId);
		return HttpsClient.map2Url(body);
	}


	public String buildUpdating(String vid, String relationId) {
		Map<String, String> body = new HashMap<>();
		body.put("aid", "25910146");
		body.put("wxappId", "101");
		body.put("wxappAid", "1844535");
		body.put("isOem", "false");
		body.put("from", "0");
		body.put("isModel", "undefined");
		body.put("wxappAppId", "wx508d8fbbaea7e235");
		body.put("cmd", "addUpdatings");
		body.put("visitorId", vid);
		body.put("pageUrl", "pages/cusCol/cusCol");
		body.put("relationId", relationId);
		return HttpsClient.map2Url(body);
	}


	public String buildUpdatingWithType(String vid, String type,  String relationId) {
		Map<String, String> body = new HashMap<>();
		body.put("aid", "25910146");
		body.put("wxappId", "101");
		body.put("wxappAid", "1844535");
		body.put("isOem", "false");
		body.put("from", "0");
		body.put("isModel", "undefined");
		body.put("wxappAppId", "wx508d8fbbaea7e235");
		body.put("cmd", "addUpdatings");
		body.put("visitorId", vid);
		body.put("pageUrl", "pages/cusCol/cusCol");
		body.put("type", type);
		body.put("relationId", relationId);
		return HttpsClient.map2Url(body);
	}

	public String buildUserInfo(String[] split) {
		String info = buildInfo(split);
		Map<String, String> body = new HashMap<>();
		body.put("aid", "25910146");
		body.put("wxappId", "101");
		body.put("wxappAid", "1844535");
		body.put("isOem", "false");
		body.put("from", "0");
		body.put("isModel", "undefined");
		body.put("wxappAppId", "wx508d8fbbaea7e235");
		body.put("cmd", "addUserInfo");
		body.put("userInfo", info);
		return HttpsClient.map2Url(body);
	}

	private String buildInfo(String[] split) {
		Map<String, Object> info = new HashMap<>();
		info.put("wxappAid",1844535);
		info.put("appId","wx508d8fbbaea7e235");
		info.put("openId",split[0]);
		info.put("nickName",split[2]+split[4]);
		info.put("province","Guizhou");
		info.put("city","Guiyang");
		info.put("country","China");
		info.put("avatarUrl","https://thirdwx.qlogo.cn/mmopen/vi_32/MHPb22FND3ht26Tjqfh5VDo1zA3x3NKQYUb9Cib4HjibkniaJKXX9AoqZL7Ya1vobOVvJ4uEC1LZMx8ia90AwTCgHA/132");
		info.put("language","zh_CN");
		info.put("signupTime", split[7]);
		info.put("loginTime", System.currentTimeMillis() - 160000);
		info.put("isPromoter",false);
		info.put("isCustomer",false);
		return jsonUtils.toJsonString(info);
	}
}
