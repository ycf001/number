package com.easytuop.number.send;

import com.easytuop.number.builder.HeaderBuilder;
import com.easytuop.number.builder.ParamBuilder;
import com.easytuop.number.builder.ProxyBuilder;
import com.easytuop.number.builder.UserBuilder;
import com.easytuop.number.client.HttpsClient;
import com.easytuop.number.ip.IpPool;
import com.easytuop.number.manager.NumberView;
import com.easytuop.number.pojo.Ip;
import com.easytuop.number.util.JsonUtils;

import java.io.BufferedWriter;
import java.net.Proxy;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author beck.yang
 * @date 2021/8/9 10:34 下午
 * @description
 */
public class Sender implements Runnable {
	private static final String VISITOR_URL = "https://i.qz.fkw.com/appAjax/wxAppConnectionVisitor.jsp";
	private static final String USER_URL = "https://i.qz.fkw.com/wxAppConnectionV3.jsp?cmd=addUserInfo";
	private static final String LOG_DATA_URL = "https://i.qz.fkw.com/wxAppConnectionV3.jsp";
	private static final String LATESTINFOSTATUS_URL = "https://i.qz.fkw.com/appAjax/mypage.jsp";
	private static final String SUBMIT_FORM_URL = "https://i.qz.fkw.com/appAjax/wxAppConnectionForm.jsp";
	private static final int LOG_RETRY = 3;


	private final UserBuilder userBuilder;
	private final ProxyBuilder proxyBuilder;
	private final IpPool ipPool;
	private final HttpsClient httpsClient;
	private final HeaderBuilder headerBuilder;
	private final ParamBuilder paramBuilder;
	private final JsonUtils jsonUtils;

	private String line;
	private int retry;
	private Ip ip;
	private Integer formId;
	private BufferedWriter out;
	private NumberView numberView;
	private BufferedWriter open;
	private BufferedWriter submitFile;


	public Sender(UserBuilder userBuilder, ProxyBuilder proxyBuilder,
	              IpPool ipPool, HttpsClient httpsClient,
	              HeaderBuilder headerBuilder, ParamBuilder paramBuilder,
	              JsonUtils jsonUtils, BufferedWriter out,
	              String line, Integer retry, Integer formId,
	              NumberView numberView, BufferedWriter openFile, BufferedWriter submitFile) {
		this.numberView = numberView;
		this.out = out;
		this.formId = formId;
		this.retry = retry;
		this.line = line;
		this.jsonUtils = jsonUtils;
		this.paramBuilder = paramBuilder;
		this.headerBuilder = headerBuilder;
		this.httpsClient = httpsClient;
		this.ipPool = ipPool;
		this.proxyBuilder = proxyBuilder;
		this.userBuilder = userBuilder;
		this.open = openFile;
		this.submitFile = submitFile;
	}


	@Override
	public void run() {
		try {
			// 逐条处理
			numberView.getTotal().incrementAndGet();
			int rts = 0;
			boolean success = false;


			success = send(line, rts);


			if (rts > 0) {
				rts = 0;
				numberView.getRetryTotal().incrementAndGet();
			}
			if (success) {
				numberView.getSuccessTotal().incrementAndGet();
				String idLine = line + "\n";
				open.write(idLine);
				open.flush();
			} else {
				out.write(line + "\n");
				out.flush();
				numberView.getFailedTotal().incrementAndGet();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	@Override
//	public void run() {
//		try {
//			// 逐条处理
//			numberView.getTotal().incrementAndGet();
//			int rts = 0;
//			boolean success = false;
//
//			success = query(line);
//
//			if (rts > 0) {
//				rts = 0;
//				numberView.getRetryTotal().incrementAndGet();
//			}
//			if (success) {
//				numberView.getSuccessTotal().incrementAndGet();
//			} else {
//				numberView.getFailedTotal().incrementAndGet();
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	private boolean send(String line, int rts) {
		try {
			String user = userBuilder.build(line);

			String s = line.replaceAll(" ", "");
			String[] split = s.split("\t");
			if (split == null || split.length < 8) {
				System.out.println("user.txt is error!");
				return false;
			}
			String openId = split[0];

			// 获取IP
//			this.ip = null;
//			int ipN = 0;
//			while (ip == null && ipN < retry) {
//				ip = ipPool.generateSingleIp();
//				ipN++;
//			}

//			// 生成代理
//			Proxy proxy = proxyBuilder.build(ip.getIp(), ip.getPort());
			Proxy proxy = null;
			// 请求1： logData
//			int dogId = 7000085;
//			int srcId = 1;
//			boolean rsp = sendLogData(proxy, dogId, srcId);

			// 请求2： logData
//			int dogId1 = 7000293;
//			int srcId1 = 0;
//			boolean rsp1 = sendLogData(proxy, dogId1, srcId1);

			// 请求3： logData
//			int dogId2 = 7000082;
//			int srcId2 = 1074;
//			boolean rsp2 = sendLogData(proxy, dogId2, srcId2);

			// 请求4： cmd=getWXAppInfo

			// 请求5： logData
			int dogId4 = 7000090;
			int srcId4 = 4;
			int t4 = 0;
			boolean rsp4 = false;
			while (!rsp4 && t4 < LOG_RETRY) {
				rsp4 = sendLogData(proxy, dogId4, srcId4);
				t4 ++;
			}
//			if (!rsp4){
//				return false;
//			}

			// 请求6： cmd=getUserInfoByCode 获取userInfo与openId

			// 请求7： cmd=getLatestInfoStatus  {"success":true,"haveData":false,"unreadCount":0}
			int t5 = 0;
			boolean rsp6 = false;
			while (!rsp6 && t5 < retry) {
				rsp6 = getLatestInfoStatus(proxy, openId);
				t5++;
			}
			if (!rsp6) {
				return false;
			}

			// 请求8： cmd=getBindCardInfo  {"success":true,"hasBindCard":false,"consultOpen":false,"cardInfo":{},"cardConfig":{}}

			// 请求9： cmd=getWXAppColModuleInfo  {"success":true,"colInfo":{"name":"我的","tdk":{"t":"","k":"","d":""},"colStyle":{"moduleStyle":"","pageBgStyle":""},"avatarUrl":"//qz.faisys.com/image/wxImage/head.png","setting":{"bp":0,"bml":1,"btype":0,"bmtgs":[],"es":{}}}}

			// 请求10： cmd=addVisitor 获取visitorId
			int t9 = 0;
			String visitorId = null;
			while (visitorId == null && t9 < retry){
				visitorId = addVisitor(openId, proxy);
				t9 ++;
			}
			if (visitorId == null) {
				return false;
			}

			// 请求11： cmd=getLatestInfoStatus  {"success":true,"haveData":false,"unreadCount":0}
//			int t10 = 0;
//			boolean rsp10 = false;
//			while (!rsp10 && t10 < retry){
//				rsp10 = getLatestInfoStatus(proxy, openId);
//				t10++;
//			}
//			if (!rsp10) {
//				return false;
//			}

			// 请求12： logData
//			int dogId11 = 7000171;
//			int srcId11 = 0;
//			int t11 = 0;
//			boolean rsp11 = false;
//			while (!rsp11 && t11 < LOG_RETRY) {
//				rsp11 = sendLogData(proxy, dogId11, srcId11);
//				t11 ++;
//			}

			// 请求13：
//			int dogId12 = 7000172;
//			int srcId12 = 0;
//			boolean rsp12 = sendLogData(proxy, dogId12, srcId12);

			// 请求14： cmd=getCardVisitSimpleInfo {"success":false,"data":{}}

			// 请求15： cmd=getWXAppColModuleInfo

			// 请求16： cmd=addUpdatings
			boolean rsp15 = false;
			int t15 = 0;
			while (!rsp15 && t15 < retry) {
				rsp15 = update(proxy, visitorId, "2");
				t15++;
			}
			if (!rsp15) {
				return false;
			}

			// 请求17：logData
//			int dogId16 = 7000090;
//			int srcId16 = 3;
//			boolean rsp16 = sendLogData(proxy, dogId16, srcId16);

			// 请求18：cmd=getSessionKeyByCode

			// 请求18：cmd=getWXAppColModuleInfo

			// 请求18：cmd=addUpdatings
			boolean rsp17 = false;
			int t17 = 0;
			while (!rsp17 && t17 < retry) {
				rsp17 = update(proxy, visitorId, "123");
				t17++;
			}
			if (!rsp17) {
				return false;
			}

			// 请求19：logData
			int dogId18 = 7000090;
			int srcId18 = 3;
			int t18 = 0;
			boolean rsp18 = false;
			while (!rsp18 && t18 < LOG_RETRY) {
				rsp18 = sendLogData(proxy, dogId18, srcId18);
				t18 ++;
			}

			// 请求20：cmd=getSessionKeyByCode

			// 请求21：cmd=getWXAppColModuleInfo

			// 请求22：cmd=addUpdatings
			boolean rsp21 = false;
			int t21 = 0;
			while (!rsp21 && t21 < retry) {
				rsp21 = update(proxy, visitorId, "121");
				t21++;
			}
			if (!rsp21) {
				return false;
			}

			// 请求23：logData
//			int dogId22 = 7000090;
//			int srcId22 = 3;
//			boolean rsp22 = sendLogData(proxy, dogId22, srcId22);

			// 请求24：cmd=getProvinceInfo

			// 请求25：cmd=cityGetChildren

			// 请求26：cmd=getSessionKeyByCode

			// 请求27：cmd=cityGetChildren

			// 请求28：logData
//			int dogId27 = 7000110;
//			int srcId27 = 2;
//			int t27 = 0;
//			boolean rsp27 = false;
//			while (!rsp27 && t27 < LOG_RETRY) {
//				rsp27 = sendLogData(proxy, dogId27, srcId27);
//				t27 ++;
//			}

			// 请求29：cmd=addUserInfo
			boolean rsp28 = false;
			int t28 = 0;
			while (!rsp28 && t28 < retry) {
				rsp28 = addUserInfo(split, proxy);
				t28++;
			}
			if (!rsp28) {
				return false;
			}

			// 请求31：logData
//			int dogId30 = 7000110;
//			int srcId30 = 0;
//			boolean rsp30 = sendLogData(proxy, dogId30, srcId30);

			// 请求32：cmd=addUpdatings
			boolean rsp31 = false;
			int t31 = 0;
			while (!rsp31 && t31 < retry) {
				rsp31 = updateWithType(proxy, visitorId, "18", "3939669");
				t31++;
			}
			if (!rsp31) {
				return false;
			}

			// 请求33：cmd=addUpdatings
			boolean rsp32 = false;
			int t32 = 0;
			while (!rsp32 && t32 < retry) {
				rsp32 = updateWithType(proxy, visitorId, "17", "3939669");
				t32++;
			}
			if (!rsp32) {
				return false;
			}

			// 请求34：cmd=getWXAppColModuleInfo

			// 请求35：cmd=addUpdatings
			boolean rsp34 = false;
			int t34 = 0;
			while (!rsp34 && t34 < retry) {
				rsp34 = update(proxy, visitorId, "121");
				t34++;
			}
			if (!rsp34) {
				return false;
			}

			// 请求36：logData
//			int dogId35 = 7000090;
//			int srcId35 = 3;
//			boolean rsp35 = sendLogData(proxy, dogId35, srcId35);

			// 请求37：cmd=getSessionKeyByCode

			// 请求38： cmd=getLatestInfoStatus  {"success":true,"haveData":false,"unreadCount":0}
/*			boolean rsp37 = false;
			int t37 = 0;
			while (!rsp37 && t37 < retry) {
				rsp37 = getLatestInfoStatus(proxy, openId);
				t37++;
			}
			if (!rsp37) {
				return false;
			}*/

			// 请求39：logData
//			int dogId38 = 7000090;
//			int srcId38 = 4;
//			boolean rsp38 = sendLogData(proxy, dogId38, srcId38);

			// 请求30：cmd=addWXAppFormSubmit
			boolean rsp29 = false;
			int t29 = 0;
			while (!rsp29 && t29 < retry) {
				rsp29 = submit(user, formId, openId, proxy);
				t29++;
			}
			if (t29 > 0) {
				rts++;
			}
			// 请求40: cmd=getFormSubmitList 查询提交结果
//			boolean result = false;
//			int t39 = 0;
//			while (!result && t39 < retry) {
//				result = getFormSubmitList(submitFile, openId, proxy);
//				t39++;
//			}

			return rsp29;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}


	private boolean query(String line) {
		try {
			String s = line.replaceAll(" ", "");
			String[] split = s.split("\t");
			if (split == null || split.length < 8) {
				System.out.println("user.txt is error!");
				return false;
			}
			String openId = split[0];

			// 请求40: cmd=getFormSubmitList 查询提交结果
			boolean result = false;
			int t39 = 0;
			while (!result && t39 < 30) {
				result = getFormSubmitList(submitFile, openId, null);
				t39++;
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	private boolean getFormSubmitList(BufferedWriter submitFile, String openId, Proxy proxy) {
		try {
			Map<String, String> header = headerBuilder.buildQuery();
			String param = paramBuilder.buildGetFormSubmit(openId);
			String s = httpsClient.sendHttpsGet(SUBMIT_FORM_URL, header, param);
			System.out.println("getFormSubmitList result: " + s);
			if (s != null && s.length() != 0) {
				Map<String, Object> map = jsonUtils.parseObject(s);
				if ((boolean) map.get("success")) {
					List<Map<String, Object>> formSubmitList = (List<Map<String, Object>>) map.get("formSubmitList");
					if (formSubmitList !=null && formSubmitList.size() ==0){
						submitFile.write(openId + "\n");
						submitFile.flush();
					}
					if (formSubmitList != null && formSubmitList.size() > 0) {
						String subResult = openId;
						for (Map<String, Object> stringObjectMap : formSubmitList) {
							String price = stringObjectMap.get("price").toString();
							String formOpenPay = stringObjectMap.get("formOpenPay").toString();
							String title = stringObjectMap.get("title").toString();
							String createTime = stringObjectMap.get("createTime").toString();
							String payTxt = stringObjectMap.get("payTxt").toString();
							List<Map<String, Object>> formData = (List<Map<String, Object>>) stringObjectMap.get("formData");
							String val = formData.get(4).get("val").toString().replaceAll("\n", "");
							subResult = subResult + "\t" + title + "\t" + createTime + "\t" +
									price + "\t" + formOpenPay + "\t" + payTxt + "\t"
									+ formData.get(0).get("val") + "\t"
									+ formData.get(1).get("val") + "\t"
									+ formData.get(2).get("val") + "\t"
									+ formData.get(3).get("val") + "\t"
									+ val + "\t";
						}
						submitFile.write(subResult+"\n");
						submitFile.flush();
					}
					return true;
				} else {
					return false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	private boolean submit(String user, Integer formId, String openId, Proxy proxy) {

		// 生成请求参数
		String param = paramBuilder.build(user, formId, openId);
		// 生成请求头
		Map<String, String> header = headerBuilder.build(param);
		// 获取client
		String result = httpsClient.sendHttpsPost(header, param, proxy);
		System.out.println(result);
		// 解析result
		if (result != null && result.length() != 0) {
			Map<String, Object> map = jsonUtils.parseObject(result);
			return (boolean) map.get("success");
		}
		return false;
	}

	private boolean getLatestInfoStatus(Proxy proxy, String openId) {
		Map<String, String> header = headerBuilder.buildVisitor();
		String param = paramBuilder.buildLatestInfoStatus(openId);
		String s = httpsClient.sendHttpsGet(LATESTINFOSTATUS_URL, header, param, proxy);
		System.out.println("getLatestInfoStatus result: "+ s);
		if (s != null && s.length() != 0) {
			Map<String, Object> map = jsonUtils.parseObject(s);
			return (boolean) map.get("success");
		}
		return false;
	}

	private boolean sendLogData(Proxy proxy, int dogId, int srcId) {
		Map<String, String> header = headerBuilder.buildVisitor();
		String param = paramBuilder.buildLogData(dogId, srcId);
		String s = httpsClient.sendHttpsGet(LOG_DATA_URL, header, param, proxy);
		if (s != null && s.length() != 0) {
			Map<String, Object> map = jsonUtils.parseObject(s);
			return (boolean) map.get("success");
		}
		return false;
	}


	public static String buildOpenId() {
		int maxNum = 64;
		int count = 0;
		char[] str = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K',
				'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
				'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-',
				'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
				'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '_'};

		StringBuffer buffer = new StringBuffer();
		Random r = new Random();
		while (count < 28) {
			int i = Math.abs(r.nextInt(maxNum));
			if (i >= 0 && i < str.length) {
				buffer.append(str[i]);
				count++;
			}
		}
		return buffer.toString();
	}


	private boolean addUserInfo(String[] split, Proxy proxy) {
		try {
			// 生成请求参数
			String param = paramBuilder.buildUserInfo(split);
			// 生成请求头
			Map<String, String> header = headerBuilder.build(param);

			String result = httpsClient.sendPost(USER_URL, header, param, proxy);
			System.out.println(result);
			if (result == null || result.length() == 0) {
				return false;
			}
			Map<String, Object> map = jsonUtils.parseObject(result);
			return (boolean) map.get("success");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}


	private String addVisitor(String openId, Proxy proxy) {
		try {
			// 生成请求头
			Map<String, String> header = headerBuilder.buildVisitor();
			// 生成请求参数
			String param = paramBuilder.buildVisitor(openId);
			String result = httpsClient.sendHttpsGet(VISITOR_URL, header, param, proxy);
			System.out.println(result);
			if (result == null || result.length() == 0) {
				return null;
			}
			Map<String, Object> map = jsonUtils.parseObject(result);
			if ((boolean) map.get("success")) {
				return map.get("visitorId").toString();
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}

	private boolean update(Proxy proxy, String visitorId, String relationId) {
		try {
			// 生成请求头
			Map<String, String> header = headerBuilder.buildVisitor();
			// 生成请求参数
			String param = paramBuilder.buildUpdating(visitorId, relationId);
			String result = httpsClient.sendHttpsGet(VISITOR_URL, header, param, proxy);
			System.out.println("addUpdatings result:" + result);
			if (result == null || result.length() == 0) {
				return false;
			}
			Map<String, Object> map = jsonUtils.parseObject(result);
			return (boolean) map.get("success");
		}catch (Exception e){
			e.printStackTrace();
		}
		return false;
	}

	private boolean updateWithType(Proxy proxy, String visitorId, String type, String relationId) {
		try {
			// 生成请求头
			Map<String, String> header = headerBuilder.buildVisitor();
			// 生成请求参数
			String param = paramBuilder.buildUpdatingWithType(visitorId, type, relationId);
			String result = httpsClient.sendHttpsGet(VISITOR_URL, header, param, proxy);
			System.out.println("addUpdatings result:" + result);
			if (result == null || result.length() == 0) {
				return false;
			}
			Map<String, Object> map = jsonUtils.parseObject(result);
			return (boolean) map.get("success");
		}catch (Exception e){
			e.printStackTrace();
		}
		return false;
	}

//	public static void main(String[] args) {
//		Long l = 1630308772402L;
//		Long l2 = 1630308772402L - 180000;
//		Date date = new Date(l);
//		Date date2 = new Date(l2);
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss:SSS'Z'");
//		System.out.println(sdf.format(date));
//		System.out.println(sdf.format(date2));
//	}
}
