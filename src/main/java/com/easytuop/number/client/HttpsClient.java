package com.easytuop.number.client;

import com.easytuop.number.cert.MyX509TrustManager;
import com.easytuop.number.conf.KeyStoreConf;
import com.easytuop.number.util.NullHostNameVerifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * @author beck.yang
 * @date 2021/8/8 7:43 下午
 * @description HttpsClient
 */
@Component
public class HttpsClient {
	private final KeyStoreConf keyStoreConf;

	@Autowired
	public HttpsClient(KeyStoreConf keyStoreConf) {
		this.keyStoreConf = keyStoreConf;
	}

	public String sendHttpsPost(Map<String, String> header, String param, Proxy proxy) {
		HttpsURLConnection con = null;
		try {
			//设置可通过ip地址访问https请求
			HttpsURLConnection.setDefaultHostnameVerifier(new NullHostNameVerifier());
			// 创建SSLContext对象，并使用我们指定的信任管理器初始化
			TrustManager[] tm = {new MyX509TrustManager(keyStoreConf.getFilePath(), keyStoreConf.getPassword())};
			SSLContext sslContext = SSLContext.getInstance("TLS");
			sslContext.init(null, tm, new java.security.SecureRandom());
			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();
			URL url = new URL(keyStoreConf.getUrl());
			con = (HttpsURLConnection) url.openConnection();
			con.setSSLSocketFactory(ssf);
			con.setConnectTimeout(5000);
			con.setRequestMethod("POST");
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setUseCaches(false);
			if (header != null && header.size() > 0) {
				for (Map.Entry<String, String> entry : header.entrySet()) {
					String key = entry.getKey();
					String value = entry.getValue();
					con.setRequestProperty(key, value);
				}
			}
			PrintWriter out = new PrintWriter(new OutputStreamWriter(con.getOutputStream(), "UTF-8"));
			out.print(param);
			out.flush();
			out.close();
			//读取请求返回值
			InputStreamReader in = new InputStreamReader(con.getInputStream(), "UTF-8");
			BufferedReader bfreader = new BufferedReader(in);
			String result = "";
			String line = "";
			while ((line = bfreader.readLine()) != null) {
				result += line;
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	public String sendPost(String urlStr, Map<String, String> header, String param, Proxy proxy) {
		HttpsURLConnection con = null;
		try {
			//设置可通过ip地址访问https请求
			HttpsURLConnection.setDefaultHostnameVerifier(new NullHostNameVerifier());
			// 创建SSLContext对象，并使用我们指定的信任管理器初始化
			TrustManager[] tm = {new MyX509TrustManager(keyStoreConf.getFilePath(), keyStoreConf.getPassword())};
			SSLContext sslContext = SSLContext.getInstance("TLS");
			sslContext.init(null, tm, new java.security.SecureRandom());
			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();
			URL url = new URL(urlStr);
			con = (HttpsURLConnection) url.openConnection();
			con.setSSLSocketFactory(ssf);
			con.setConnectTimeout(5000);
			con.setRequestMethod("POST");
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setUseCaches(false);
			if (header != null && header.size() > 0) {
				for (Map.Entry<String, String> entry : header.entrySet()) {
					String key = entry.getKey();
					String value = entry.getValue();
					con.setRequestProperty(key, value);
				}
			}
			PrintWriter out = new PrintWriter(new OutputStreamWriter(con.getOutputStream(), "UTF-8"));
			out.print(param);
			out.flush();
			out.close();
			//读取请求返回值
			InputStreamReader in = new InputStreamReader(con.getInputStream(), "UTF-8");
			BufferedReader bfreader = new BufferedReader(in);
			String result = "";
			String line = "";
			while ((line = bfreader.readLine()) != null) {
				result += line;
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	public String sendHttpsGet(String urlStr, Map<String, String> header, String param, Proxy proxy) {
		HttpsURLConnection con = null;
		try {
			//设置可通过ip地址访问https请求
			HttpsURLConnection.setDefaultHostnameVerifier(new NullHostNameVerifier());
			// 创建SSLContext对象，并使用我们指定的信任管理器初始化
			TrustManager[] tm = {new MyX509TrustManager(keyStoreConf.getFilePath(), keyStoreConf.getPassword())};
			SSLContext sslContext = SSLContext.getInstance("TLS");
			sslContext.init(null, tm, new java.security.SecureRandom());
			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();
			URL url = new URL(urlStr+"?"+param);
			con = (HttpsURLConnection) url.openConnection();
			con.setSSLSocketFactory(ssf);
			con.setConnectTimeout(5000);
			con.setRequestMethod("GET");
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setUseCaches(false);
			if (header != null && header.size() > 0) {
				for (Map.Entry<String, String> entry : header.entrySet()) {
					String key = entry.getKey();
					String value = entry.getValue();
					con.setRequestProperty(key, value);
				}
			}
			PrintWriter out = new PrintWriter(new OutputStreamWriter(con.getOutputStream(), "UTF-8"));
			out.print(param);
			out.flush();
			out.close();
			//读取请求返回值
			InputStreamReader in = new InputStreamReader(con.getInputStream(), "UTF-8");
			BufferedReader bfreader = new BufferedReader(in);
			String result = "";
			String line = "";
			while ((line = bfreader.readLine()) != null) {
				result += line;
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String sendHttpsGet(String urlStr, Map<String, String> header, String param) {
		HttpsURLConnection con = null;
		try {
			//设置可通过ip地址访问https请求
			HttpsURLConnection.setDefaultHostnameVerifier(new NullHostNameVerifier());
			// 创建SSLContext对象，并使用我们指定的信任管理器初始化
			TrustManager[] tm = {new MyX509TrustManager(keyStoreConf.getFilePath(), keyStoreConf.getPassword())};
			SSLContext sslContext = SSLContext.getInstance("TLS");
			sslContext.init(null, tm, new java.security.SecureRandom());
			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();
			URL url = new URL(urlStr+"?"+param);
			con = (HttpsURLConnection) url.openConnection();
			con.setSSLSocketFactory(ssf);
			con.setConnectTimeout(5000);
			con.setRequestMethod("GET");
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setUseCaches(false);
			if (header != null && header.size() > 0) {
				for (Map.Entry<String, String> entry : header.entrySet()) {
					String key = entry.getKey();
					String value = entry.getValue();
					con.setRequestProperty(key, value);
				}
			}
			PrintWriter out = new PrintWriter(new OutputStreamWriter(con.getOutputStream(), "UTF-8"));
			out.print(param);
			out.flush();
			out.close();
			//读取请求返回值
			InputStreamReader in = new InputStreamReader(con.getInputStream(), "UTF-8");
			BufferedReader bfreader = new BufferedReader(in);
			String result = "";
			String line = "";
			while ((line = bfreader.readLine()) != null) {
				result += line;
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * map转url参数
	 */
	public static String map2Url(Map<String, String> paramToMap) {
		if (null == paramToMap || paramToMap.isEmpty()) {
			return null;
		}
		StringBuffer url = new StringBuffer();
		boolean isfist = true;
		for (Map.Entry<String, String> entry : paramToMap.entrySet()) {
			if (isfist) {
				isfist = false;
			} else {
				url.append("&");
			}
			url.append(entry.getKey()).append("=");
			String value = entry.getValue();
			if (!StringUtils.isEmpty(value)) {
				try {
					url.append(URLEncoder.encode(value, "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		}
		return url.toString();
	}

	public String setGet(String url, Map<String, String> param) {
		BufferedReader in = null;
		String result = "";
		try {
			String urlNameString = url + "?" + map2Url(param);
			URL realUrl = new URL(urlNameString);
			// 打开和URL之间的连接
			URLConnection connection = realUrl.openConnection();
			// 设置通用的请求属性
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 建立实际的连接
			connection.connect();
			// 获取所有响应头字段
			Map<String, List<String>> map = connection.getHeaderFields();
			// 遍历所有的响应头字段
			for (String key : map.keySet()) {
				System.out.println(key + "--->" + map.get(key));
			}
			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line = "";
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("Connect error, It would retry again!");
//			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return result;
	}
}