//package com.hewei.spider.proxy;
//
//import java.io.StringReader;
//import java.util.Iterator;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.concurrent.atomic.AtomicInteger;
//
//import org.apache.commons.io.IOUtils;
//import org.apache.http.HttpHost;
//import org.apache.http.client.methods.CloseableHttpResponse;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.util.EntityUtils;
//import org.jsoup.Jsoup;
//
//import com.google.common.collect.Lists;
//import com.pyc.paper.utils.SleepUtil;
//
//public abstract class ProxySource implements Iterable<HttpHost>,
//		Iterator<HttpHost> {
//	protected List<String> list = Lists.newArrayList();
//	protected AtomicInteger index = new AtomicInteger(0);
//	protected AtomicInteger pageNum = new AtomicInteger(0);
//
//	@Override
//	public Iterator<HttpHost> iterator() {
//		return this;
//	}
//
//	@Override
//	public boolean hasNext() {
//		if (index.get() >= list.size()) {
//			try {
//				refreshList(list, index, pageNum);
//			} catch (Exception e) {
//				throw new IllegalArgumentException(e);
//			}
//		}
//		return index.get() < list.size();
//	}
//
//	@Override
//	public HttpHost next() {
//		return HttpHost.create(list.get(index.getAndIncrement()));
//	}
//
//	protected abstract void refreshList(List<String> list, AtomicInteger index,
//			AtomicInteger pageNum) throws Exception;
//	/**
//	 * 总出口
//	 * @return
//	 */
//	public static Iterable<HttpHost> allProxySource() {
//		LinkedList<Iterator<HttpHost>> queue = new LinkedList<>();
//		// queue.offer(kuaidailiProxySource(Integer.MAX_VALUE));
//		queue.offer(apiProxySource());
//		return () -> {
//			return new Iterator<HttpHost>() {
//				@Override
//				public boolean hasNext() {
//					for (;;) {
//						Iterator<HttpHost> itr = queue.poll();
//						if (null == itr) {
//							return false;
//						}
//						if (itr.hasNext()) {
//							queue.offer(itr);
//							return true;
//						}
//					}
//				}
//
//				@Override
//				public HttpHost next() {
//					return queue.peekLast().next();
//				}
//
//			};
//		};
//
//		// return Iterables.concat(kuaidailiProxySource(Integer.MAX_VALUE),
//		// apiProxySource());
//	}
//
//	static String apiURL = "";
//
//	public static void setApiURL(String apiUrl) {
//		apiURL = apiUrl;
//	}
//
//	public static ProxySource apiProxySource() {
//		return new ProxySource() {
//			private CloseableHttpClient client = HttpClients.createDefault();
//
//			@Override
//			protected void refreshList(List<String> list, AtomicInteger index,
//					AtomicInteger pageNum) throws Exception {
//				index.set(0);
//				list.clear();
//				for (;;) {
//					String apiUrl = "http://www.kuaidaili.com/api/getproxy/?orderid=943312563822216&num=300&area_ex=%E4%B8%AD%E5%9B%BD&browser=1&protocol=1&method=1&an_an=1&an_ha=1&quality=0&sort=0&format=text&sep=1";
//					HttpGet request = new HttpGet(apiUrl);
//					CloseableHttpResponse response = null;
//					try {
//						response = client.execute(request);
//						IOUtils.readLines(
//								new StringReader(EntityUtils.toString(response
//										.getEntity()))).forEach(
//								line -> list.add("http://" + line));
//
//						if (list.isEmpty()) {
//							SleepUtil.sleep(3000);
//							continue;
//						}
//					} finally {
//						if (null != response)
//							response.close();
//						else
//							request.abort();
//					}
//					break;
//				}
//			}
//		};
//
//	}
//
//	/**
//	 * 快代理
//	 *
//	 * @return
//	 */
//	public static ProxySource kuaidailiProxySource(int num) {
//		return new ProxySource() {
//			private CloseableHttpClient client = HttpClients.createDefault();
//
//			@Override
//			protected void refreshList(List<String> list, AtomicInteger index,
//					AtomicInteger pageNum) throws Exception {
//				index.set(0);
//				list.clear();
//				String url = "http://www.kuaidaili.com/free/outha/"
//						+ pageNum.incrementAndGet() + "/";// 国外高匿
//				CloseableHttpResponse response = client
//						.execute(new HttpGet(url));
//				Jsoup.parse(EntityUtils.toString(response.getEntity()))
//						.select("div#container>div>div#list>table.table.table-bordered.table-striped>tbody>tr")
//						.forEach(e -> {
//							String ip = e.select("td:nth-child(1)").text();
//							String port = e.select("td:nth-child(2)").text();
//							String s = "http://" + ip + ":" + port;
//							list.add(s);
//						});
//				if (list.isEmpty()) {
//					client.close();
//				}
//			}
//		};
//	}
//}
