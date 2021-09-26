package com.easytuop.number.manager;

import com.easytuop.number.builder.HeaderBuilder;
import com.easytuop.number.builder.ParamBuilder;
import com.easytuop.number.builder.ProxyBuilder;
import com.easytuop.number.builder.UserBuilder;
import com.easytuop.number.client.HttpsClient;
import com.easytuop.number.conf.FileConf;
import com.easytuop.number.conf.SendConf;
import com.easytuop.number.ip.IpPool;
import com.easytuop.number.pojo.Ip;
import com.easytuop.number.send.Sender;
import com.easytuop.number.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.Proxy;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * @author beck.yang
 * @date 2021/8/7 5:45 下午
 * @description 上号管理类
 */
@Component
public class NumberManager implements ApplicationRunner {
	private static final String DATE_FORMAT = "yyyy-MM-dd_hh-mm-ss";

	private final UserBuilder userBuilder;
	private final ProxyBuilder proxyBuilder;
	private final IpPool ipPool;
	private final HttpsClient httpsClient;
	private final HeaderBuilder headerBuilder;
	private final ParamBuilder paramBuilder;
	private final JsonUtils jsonUtils;
	private final FileConf fileConf;
	private final SendConf sendConf;
	private final ThreadPoolManager threadPoolManager;
	private boolean stop = false;
	private static Integer totalData = 0;
	private static Integer failedData = 0;
	private static Integer successData = 0;

	@Autowired
	public NumberManager(UserBuilder userBuilder, ProxyBuilder proxyBuilder,
	                     IpPool ipPool, HttpsClient httpsClient,
	                     HeaderBuilder headerBuilder, ParamBuilder paramBuilder,
	                     JsonUtils jsonUtils, FileConf fileConf,
	                     SendConf sendConf, ThreadPoolManager threadPoolManager) {
		this.threadPoolManager = threadPoolManager;
		this.sendConf = sendConf;
		this.fileConf = fileConf;
		this.jsonUtils = jsonUtils;
		this.paramBuilder = paramBuilder;
		this.headerBuilder = headerBuilder;
		this.httpsClient = httpsClient;
		this.ipPool = ipPool;
		this.proxyBuilder = proxyBuilder;
		this.userBuilder = userBuilder;
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		// 上号
		sendUser();
//		sendUserInfo();
	}

	public void sendUserInfo() {
		try {
			String path = fileConf.getOutPath();
			String info = path + "success.txt";
			// 读取User
			FileInputStream fis = new FileInputStream(info);
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			// 初始化线程池
			String line = null;
			long start = System.currentTimeMillis();
			while ((line = br.readLine()) != null) {
				if (line == null || line.length() == 0) {
					continue;
				}
				String s = line.replaceAll(" ", "");
				String[] split = s.split("\t");
				if (split == null || split.length < 7) {
					continue;
				}
				System.out.println(s);

			}
			br.close();
			fis.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendUser() {
		try {
			long s = System.currentTimeMillis();
			int batchNum = 1;
			String userFile = fileConf.getUserFile();
			while (!stop && batchNum <= sendConf.getMaxSendBatch()) {
				userFile = batchSend(userFile, batchNum);
				batchNum++;
				Thread.sleep(5000);
			}
			long e = System.currentTimeMillis();
			Double sp = (Double.valueOf(successData) / Double.valueOf(totalData)) * 100;
			long takeMills = e - s;
			long avgTakeMills = takeMills / totalData;

			System.out.println("============================================");
			System.out.println("全部提交完成，提交次数：" + --batchNum);
			System.out.println("全部提交数量：" + totalData);
			System.out.println("全部提交数量成功数量：" + successData);
			System.out.println("全部提交数量失败数量：" + failedData);
			System.out.println("全部提交成功率：" + sp);
			System.out.println("全部提交总耗时：" + takeMills);
			System.out.println("全部提交平均耗时：" + avgTakeMills);
			System.out.println("============================================");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String batchSend(String useFile, int batchNum) {
		try {
			if (useFile == null) {
				stop = true;
				return null;
			}
			// 读取User
			FileInputStream fis = new FileInputStream(useFile);
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
			String fileName = "failed-" + sdf.format(new Date()) + ".txt";
			String successName = "success-" + sdf.format(new Date()) + ".txt";
			String submitName = "result-" + sdf.format(new Date()) + ".txt";
			String outFilePath = fileConf.getOutPath() + fileName;
			String outOpenIdFilePath = fileConf.getOutPath() + successName;
			String submitNameFilePath = fileConf.getOutPath() + submitName;
			File outFile = new File(outFilePath);
			File openFile = new File(outOpenIdFilePath);
			File submitFile = new File(submitNameFilePath);
			NumberView numberView = new NumberView();
			if (!outFile.exists()) {
				outFile.createNewFile();
			}
			if (!openFile.exists()) {
				openFile.createNewFile();
			}
			if (!submitFile.exists()) {
				submitFile.createNewFile();
			}
			BufferedWriter out = new BufferedWriter(new FileWriter(outFile));
			BufferedWriter open = new BufferedWriter(new FileWriter(openFile));
			BufferedWriter submit = new BufferedWriter(new FileWriter(submitFile));
			// 初始化线程池
			String line = null;
			int lineNum = 1;
			long start = System.currentTimeMillis();
			ExecutorService exc = threadPoolManager.initSenderPool("SendPool-" + batchNum, sendConf.getCoreThread(), sendConf.getMaxThread(), sendConf.getKeepAlive());
			while ((line = br.readLine()) != null) {
				Sender sender = new Sender(userBuilder, proxyBuilder, ipPool, httpsClient,
						headerBuilder, paramBuilder, jsonUtils, out, line, sendConf.getRetry(),
						sendConf.getFormId(), numberView, open, submit);
				exc.execute(sender);
				lineNum++;
			}
			br.close();
			fis.close();
			// 判断线程池完成
			exc.shutdown();
			while (true) {
				if (exc.isTerminated()) {
					out.flush();
					out.close();
					open.flush();
					open.close();
					break;
				}
				Thread.sleep(1000);
			}

			long end = System.currentTimeMillis();
			long use = end - start;
			long avg = use / numberView.getTotal().get();

			successData += numberView.getSuccessTotal().get();
			if (batchNum == 1) {
				totalData = numberView.getTotal().get();
			}
			failedData = numberView.getFailedTotal().get();

			Double successP = (Double.valueOf(numberView.getSuccessTotal().get()) / Double.valueOf(numberView.getTotal().get())) * 100;

			System.out.println("------------------------第" + batchNum + "次提交结果--------------------");
			System.out.println("提交总数量:" + numberView.getTotal().get());
			System.out.println("总成功数量:" + numberView.getSuccessTotal().get());
			System.out.println("总失败数量:" + numberView.getFailedTotal().get());
			System.out.println("重试数量:" + numberView.getRetryTotal().get());
			System.out.println("总耗时:" + use);
			System.out.println("平均耗时:" + avg);
			System.out.println("成功率:" + successP + "%");
			System.out.println("-----------------------------------------------------------------------");
			int i = numberView.getFailedTotal().get();
			if (i == 0) {
				stop = true;
			}
			return outFilePath;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
