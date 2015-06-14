package com.hewei.spider.processor;

import com.hewei.spider.pipeline.EmptyPipeline;
import com.hewei.spider.pojos.IpClass;
import com.hewei.spider.utils.HtmlUtils;
import org.apache.commons.lang.StringUtils;
import org.mvel2.MVEL;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.RedisScheduler;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author hewei
 * @version 5.0
 * @date 2015/6/15  1:42
 * @desc
 */
public class SpiderProcessor implements PageProcessor {

	private static final Site site = Site.me().enableHttpProxyPool().setSleepTime(1000).setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31");

	private static final Pattern p1 = Pattern.compile("<td>([0-9]*.[0-9]*.[0-9]*.[0-9]*)</td>");

	private static final Pattern p2 = Pattern.compile("<td><script>document.write\\((.*)\\);</script></td>");

	private static JedisPool pool = new JedisPool(new JedisPoolConfig(), "172.18.2.35", 7009);

	private static final String PROXY_KEY = "proxy";

	@Override
	public void process(Page page) {

		String scriptForCalculate = page.getHtml().xpath("//script").regex(">(var.*)<").toString();

		Map<String, String> map = HtmlUtils.eval(scriptForCalculate);

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


	public static void start() {
		Spider.create(new SpiderProcessor()).addUrl("http://pachong.org/").setScheduler(new RedisScheduler(pool)).addPipeline(new EmptyPipeline()).run();
	}

	public static void scan() {
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

			List<String> list = new ArrayList<>(set);
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

}
