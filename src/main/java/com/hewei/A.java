package com.hewei;

import com.google.common.base.Splitter;
import com.google.common.collect.Sets;
import com.ning.http.client.*;
import com.ning.http.client.uri.Uri;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.net.InetAddress;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 
 * @author hewei
 * 
 * @date 2015/9/13  16:50
 *
 * @version 5.0
 *
 * @desc 
 *
 */
public class A {

//	private static String ip = "124.206.100.186";

	private static final String URL = "http://www.baidu.com/img/baidu_jgylogo3.gif";

	private static ThreadLocal<ProxyServer> proxyServers = new ThreadLocal<>();

	public static void main(String[] args) throws IOException {

		ipSelectorStart();

		AsyncHttpClientConfig.Builder builder = new AsyncHttpClientConfig.Builder();
		builder.setRequestTimeout(5000);
		builder.setConnectTimeout(5000);
		builder.setProxyServerSelector(new ProxyServerSelector() {

			@Override
			public ProxyServer select(Uri uri) {
				return proxyServers.get();
			}
		});
		final Semaphore semaphore =new Semaphore(500);
		final AsyncHttpClient client = new AsyncHttpClient(builder.build());
		for (int i = 0; i < 10; i++) {
			new Thread() {

				@Override
				public void run() {
					while (true) {
						String ip = SUCCESS_QUEUE.poll();
						if (StringUtils.isEmpty(ip)) {
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							continue;
						}
//						for (int port : selectPorts) {
//							try {
//								semaphore.acquire();
//							} catch (InterruptedException e) {
//								e.printStackTrace();
//							}
//							proxyServers.set(new ProxyServer(ip, port));
//							client.prepareGet(URL).setBodyEncoding("UTF-8").execute(new B(ip, port,semaphore));
//						}
						for (int port = 1; port < 65535; port++) {
							try {
								semaphore.acquire();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							proxyServers.set(new ProxyServer(ip, port));
							client.prepareGet(URL).setBodyEncoding("UTF-8").execute(new B(ip, port, semaphore));
						}

						OVER_SET.add(ip);

					}
				}
			}.start();
		}

	}

	public static List<Integer> selectPorts = new ArrayList<>();//http://www.goubanjia.com/info/#dist_protocol

	static {
		String ports = "10,10000,10080,1010,10118,1029,10310,10368,1041,1057,10596,1071,1073,10732,1080,10801,1081,1088,10953,110,1111,11148,1122,1136,1139,11399,11413,11421,1146,1161,1186,1189,1206,12118,1229,1231,1234,12345,12446,1250,1281,1284,1293,12967,1309,1320,1334,1337,1340,13455,1355,1357,13816,1383,1412,1416,1424,1426,1441,1454,1466,1475,1507,1546,15547,15593,1569,1570,15866,1588,1592,1629,1632,1635,16365,1640,1644,16487,1654,1661,1683,1696,1713,1721,1743,1748,1755,1760,1776,1778,17817,17903,18000,18179,18186,1838,1839,1842,1849,1856,18593,1865,18888,18938,19131,1920,1939,1959,19695,1971,1974,1983,1997,19981,2019,2020,20355,2076,20801,21021,21320,2193,22,2214,2222,22819,23,23259,24215,24621,24821,25000,25463,26949,27408,27644,27977,29316,29405,29505,298,3030,3060,31089,31113,3127,3128,31281,3129,3130,3131,3149,31541,31743,3245,32658,32985,33290,3333,33352,33630,34491,34940,34945,35827,3583,3635,37097,37564,38817,38832,39054,39125,3933,39683,40887,40971,41787,42372,42654,4331,443,4444,44602,45092,4564,45881,45965,4624,47037,47141,48791,50001,5004,5007,50129,50272,5071,50929,51054,51561,51863,52329,52331,52595,52789,52803,53478,53486,5351,53753,54028,54321,54322,5441,55336,5555,591,59317,59385,59739,6006,60848,63000,63574,6397,64028,64081,64082,6543,6588,666,6666,6668,6675,7014,7280,7777,7808,7890,80,800,8000,8001,8008,8010,808,8080,8081,8083,8085,8086,8087,8088,8089,8090,81,8112,8118,8122,8123,8159,8180,8181,82,83,843,8585,86,8636,8800,8877,8888,9000,9050,9064,9090,9091,9191,93,9797,9876,9888,9999";
		List<String> tmp = Splitter.on(",").trimResults().splitToList(ports);
		for (String port : tmp) {
			selectPorts.add(Integer.parseInt(port));
		}
	}

	private static final int IP_BLOCK_MAX = 255;

	private static Random IP_RANDOM = new Random();

	public static String randomIp() {
		int block1 = 124;
		int block2 = 206;
		int block3 = 100;
//		int block1 = IP_RANDOM.nextInt(IP_BLOCK_MAX) + 1;
//		int block2 = IP_RANDOM.nextInt(IP_BLOCK_MAX) + 1;
//		int block3 = IP_RANDOM.nextInt(IP_BLOCK_MAX) + 1;
		int block4 = IP_RANDOM.nextInt(IP_BLOCK_MAX) + 1;
		return block1 + "." + block2 + "." + block3 + "." + block4;
	}


	private static Set<String> OVER_SET = Sets.newConcurrentHashSet();

	private static Queue<String> SUCCESS_QUEUE = new ConcurrentLinkedQueue<>();

	private static Set<String> ERROR_SET = Sets.newConcurrentHashSet();

	public static void ipSelectorStart(){
		for (int i = 0; i < 5; i++) {
			new Thread() {

				@Override
				public void run() {

					while (true) {
						String ip = randomIp();

						if (ERROR_SET.contains(ip)) {
							continue;
						}

						if (OVER_SET.contains(ip)) {
							continue;
						}

						try {
							if (InetAddress.getByName(ip).isReachable(3000)) {
								SUCCESS_QUEUE.offer(ip);
								System.out.println("isReachable:"+ip);
							} else {
								ERROR_SET.add(ip);
							}
						} catch (IOException e) {
							ERROR_SET.add(ip);
						}
					}
				}

			}.start();
		}
	}

}

class B extends AsyncCompletionHandler {

	private static AtomicLong count=new AtomicLong();

	private String ip;

	private int port;

	private Semaphore semaphore;

	public B(String ip, int port,Semaphore semaphore) {
		this.ip = ip;
		this.port = port;
		this.semaphore=semaphore;
	}


	@Override
	public Object onCompleted(Response response) throws Exception {
		System.out.println("getStatusCode:" + response.getStatusCode() + "---ip:" + ip + "---port:" + port);
		semaphore.release();
		return null;
	}

	@Override
	public void onThrowable(Throwable t) {
		long num = count.incrementAndGet();
		if (num % 1000 == 0) {
			System.out.println(num);
		}
		semaphore.release();
	}
}
