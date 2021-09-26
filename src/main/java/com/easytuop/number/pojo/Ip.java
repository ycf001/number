package com.easytuop.number.pojo;

import lombok.Data;

import java.util.Objects;

/**
 * @author beck.yang
 * @date 2021/8/9 4:47 下午
 * @description
 */
@Data
public class Ip {
	private String ip;
	private Integer port;
	private String addr;
	private String isp;
	private Long expire;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Ip ip1 = (Ip) o;
		return Objects.equals(ip, ip1.ip) && Objects.equals(port, ip1.port);
	}

	@Override
	public int hashCode() {
		return Objects.hash(ip, port);
	}
}
