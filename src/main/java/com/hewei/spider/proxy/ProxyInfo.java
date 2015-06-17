//package com.hewei.spider.proxy;
//
//import java.util.Objects;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
///**
// * 使用详情
// *
// * @author zhangnan1
// *
// */
//public class ProxyInfo {
//
//	private int connectTime;
//	private int readTime;
//	private int codeStatus;
//	private String throwableMessage;
//
//	public ProxyInfo() {
//
//	}
//
//	public ProxyInfo(int codeStatus, int contentTime, int readTime) {
//		this.codeStatus = codeStatus;
//		this.connectTime = contentTime;
//		this.readTime = readTime;
//	}
//
//	public static ProxyInfo parse(String str) {
//		Objects.requireNonNull(str, "msg不允许为空");
//		str = str.trim();
//		try {
//			if (!str.startsWith("{") && !str.endsWith("}")) {
//				str = "{" + str + "}";
//			}
//			JSONObject obj = new JSONObject(str);
//			ProxyInfo info = new ProxyInfo();
//			info.setConnectTime(obj.optInt("ct", 0));
//			info.setReadTime(obj.optInt("rt", 0));
//			info.setCodeStatus(obj.optInt("code", -1));
//			info.setThrowableMessage(obj.optString("msg", null));
//			return info;
//		} catch (JSONException e) {
//			throw new IllegalArgumentException(e);
//		}
//
//	}
//
//	public int getReadTime() {
//		return readTime;
//	}
//
//	public void setReadTime(int readTime) {
//		this.readTime = readTime;
//	}
//
//	public int getConnectTime() {
//		return connectTime;
//	}
//
//	public void setConnectTime(int connectTime) {
//		this.connectTime = connectTime;
//	}
//
//	public int getCodeStatus() {
//		return codeStatus;
//	}
//
//	public void setCodeStatus(int codeStatus) {
//		this.codeStatus = codeStatus;
//	}
//
//	public String getThrowableMessage() {
//		return throwableMessage;
//	}
//
//	public void setThrowableMessage(String throwableMessage) {
//		this.throwableMessage = throwableMessage;
//	}
//}
