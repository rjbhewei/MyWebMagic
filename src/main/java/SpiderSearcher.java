import org.apache.commons.lang.StringUtils;
import org.mvel2.MVEL;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import us.codecraft.webmagic.*;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.RedisScheduler;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author hewei
 * @version 5.0
 * @date 2015/6/11  15:26
 * @desc
 */
public class SpiderSearcher implements PageProcessor {

	private static final Pattern p1 = Pattern.compile("<td>([0-9]*.[0-9]*.[0-9]*.[0-9]*)</td>");

	private static final Pattern p2 = Pattern.compile("<td><script>document.write\\((.*)\\);</script></td>");

	private static final Site site = Site.me().enableHttpProxyPool().setSleepTime(1000).setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31");

	private static JedisPool pool = new JedisPool(new JedisPoolConfig(), "172.18.2.35", 7009);
	private static final String PROXY_KEY="proxy";
	@Override
	public void process(Page page) {

		String scriptForCalculate = page.getHtml().xpath("//script").regex(">(var.*)<").toString();

		Map<String, String> map = eval(scriptForCalculate);

		List<String> list = page.getHtml().xpath("//tr").all();
		out:
		for (String s : list) {
			if (StringUtils.isEmpty(s)) {
				continue;
			}
			IpClass ipClass = new IpClass();
			Matcher m = p1.matcher(s);
			while (m.find()) {
				String ip = m.group(1).trim();
				if (StringUtils.isEmpty(ip)) {
					continue out;
				}
				ipClass.setIp(ip);
			}

			m = p2.matcher(s);
			while (m.find()) {
				String port = m.group(1).trim();
				if (StringUtils.isEmpty(port)) {
					continue out;
				}
				for (String key : map.keySet()) {
					if (!port.contains(key)) {

						continue;
					}
					port = String.valueOf(MVEL.eval(port.replace(key, map.get(key))));
					break;
				}
				ipClass.setPort(Integer.parseInt(port));
				if (StringUtils.isEmpty(ipClass.getIp())) {
					continue out;
				}
				String key = ipClass.getIp() + ":" + ipClass.getPort();

				Jedis jedis = pool.getResource();
				try {
					jedis.sadd(PROXY_KEY, key);
				} finally {
					pool.returnResource(jedis);
				}

			}
		}

		page.addTargetRequests(page.getHtml().xpath("//div[@class='natWap clear']/table").links().all());

	}

	@Override
	public Site getSite() {
		return site;
	}

	public static void main(final String[] args) {
		//        new Thread(){
		//
		//            @Override
		//            public void run() {
		//                BaiduBaikeSpider.main(args);
		//            }
		//        }.start();

		new Thread(){

			@Override
			public void run() {
				Spider.create(new SpiderSearcher()).addUrl("http://pachong.org/").setScheduler(new RedisScheduler(pool)).addPipeline(new StoragePipeline()).run();
			}
		}.start();

		new Thread() {

			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					Jedis jedis = pool.getResource();
					Set<String> set = new HashSet<>();
					try {
						set = jedis.smembers(PROXY_KEY);
					} finally {
						pool.returnResource(jedis);
					}

					if (set.isEmpty()) {
						continue;
					}

					List<String> list =new ArrayList<>(set);
					if (!list.isEmpty()) {
						String[][] httpProxyList = new String[list.size()][2];
						for (int i = 0; i < list.size(); i++) {
							String[] tmp = list.get(i).split(":");
							httpProxyList[i] = new String[]{tmp[0], tmp[1]};
						}
						site.getHttpProxyPool().addProxy(httpProxyList);
					}

				}
			}
		}.start();


	}

	public static class StoragePipeline implements Pipeline {

		@Override
		public void process(ResultItems resultItems, Task task) {
			//            Map<String, Object> map = resultItems.getAll();
			//            System.out.println(ipMap.size());
		}
	}

	public static Map<String, String> eval(String str) {
		String[] xx = str.split(";");

		Map<String, String> map = new HashMap<>();
		for (String x : xx) {
			String[] tmp = x.replace("var", "").trim().split("=");
			map.put(tmp[0], tmp[1]);
		}
		Set<String> keys = map.keySet();
		while (true) {
			int index = 0;
			for (Map.Entry<String, String> entry : map.entrySet()) {
				int loop = 0;
				for (String key : keys) {
					if (!entry.getValue().contains(key)) {
						loop++;
						continue;
					}
					entry.setValue(entry.getValue().replace(key, "(" + map.get(key) + ")"));
				}
				if (loop == keys.size()) {
					index++;
				}
			}
			if (index == keys.size()) {
				break;
			}
		}
		for (Map.Entry<String, String> entry : map.entrySet()) {
			entry.setValue(String.valueOf(MVEL.eval(entry.getValue())));
		}
		return map;
	}


	public static class IpClass {

		private String ip;

		private int port;

		public IpClass() {
		}

		public String getIp() {
			return ip;
		}

		public void setIp(String ip) {
			this.ip = ip;
		}

		public int getPort() {
			return port;
		}

		public void setPort(int port) {
			this.port = port;
		}

	}

}
