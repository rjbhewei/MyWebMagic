//package com.hewei.spider.proxy;
//
//import org.apache.http.HttpHost;
//
//class TplProxy {
//	public enum Status {
//		/**
//		 * 可用
//		 */
//		OK,
//		/**
//		 * 错误
//		 */
//		ERR,
//		/**
//		 * 待验证
//		 */
//		TRY
//	}
//
//	public TplProxy(String s) {
//		this(HttpHost.create(s));
//	}
//
//	public TplProxy(HttpHost httpHost) {
//		this.httpHost = httpHost;
//	}
//
//	@Override
//	public String toString() {
//		return httpHost.toString();
//	}
//
//	public HttpHost getHttpHost() {
//		return httpHost;
//	}
//
//	@Override
//	public int hashCode() {
//		return httpHost.hashCode();
//	}
//
//	@Override
//	public boolean equals(Object obj) {
//		if (obj instanceof TplProxy) {
//			return httpHost.equals(((TplProxy) obj).httpHost);
//		}
//		return false;
//	}
//
//	public Status getStatus() {
//		return status;
//	}
//
//	public void setStatus(Status status) {
//		this.status = status;
//	}
//
//	public long getCreateTime() {
//		return createTime;
//	}
//
//	public long getLastTime() {
//		return lastTime;
//	}
//
//	public void setLastTime(long lastTime) {
//		this.lastTime = lastTime;
//	}
//
//	public int getScore() {
//		return score;
//	}
//
//	public void setScore(int score) {
//		this.score = score;
//	}
//
//	private Status status = Status.OK;
//	private final HttpHost httpHost;
//	private final long createTime = System.currentTimeMillis();
//	private long lastTime = createTime;
//	int score;
//
//}
