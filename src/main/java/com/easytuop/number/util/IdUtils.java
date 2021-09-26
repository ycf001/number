package com.easytuop.number.util;

import com.easytuop.number.send.Sender;
import sun.security.provider.certpath.BuildStep;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Random;

/**
 * @author beck.yang
 * @date 2021/8/30 9:11 下午
 * @description
 */
public class IdUtils {

	public static void main(String[] args) {
		buildSignUps(22);
		buildOpenIds(22);
	}

	public static void buildSignUps(int n) {
		try {
			File outFile = new File("/Users/yangweihua/Documents/code/number2/src/main/resources/sinups.txt");
			BufferedWriter out = new BufferedWriter(new FileWriter(outFile));
			if (!outFile.exists()) {
				outFile.createNewFile();
			}
			Random random = new Random();
			for (int i = 0; i < n; i++) {
				int r = random.nextInt(1000000);
				Long l = 1626585219144L + r;
				out.write(l.toString() + "\n");
				out.flush();
			}
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void buildOpenIds(int n) {
		try {
			File outFile = new File("/Users/yangweihua/Documents/code/number2/src/main/resources/openId.txt");
			BufferedWriter out = new BufferedWriter(new FileWriter(outFile));
			if (!outFile.exists()) {
				outFile.createNewFile();
			}
			for (int i = 0; i < n; i++) {
				String openId = Sender.buildOpenId();
				out.write(openId + "\n");
				out.flush();
			}
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
