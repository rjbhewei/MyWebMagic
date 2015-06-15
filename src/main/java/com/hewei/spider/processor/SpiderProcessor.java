package com.hewei.spider.processor;

import com.google.common.collect.Lists;
import com.hewei.spider.constants.Messages;
import com.hewei.spider.constants.SpiderConstants;
import com.hewei.spider.listener.SpiderSearcherSpiderListener;
import com.hewei.spider.pojos.IpClass;
import com.hewei.spider.scheduler.JedisScheduler;
import com.hewei.spider.site.SiteUtils;
import com.hewei.spider.utils.HtmlUtils;
import org.apache.commons.lang.StringUtils;
import org.mvel2.MVEL;
import redis.clients.jedis.Jedis;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.SpiderListener;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author hewei
 * @version 5.0
 * @date 2015/6/15  1:42
 * @desc
 */
public class SpiderProcessor extends BaseProcessor {

    private boolean useProxy;

    public SpiderProcessor(boolean useProxy) {
        this.useProxy = useProxy;
    }

	private static final Pattern ipPattern = Pattern.compile("<td>([0-9]*.[0-9]*.[0-9]*.[0-9]*)</td>");

	private static final Pattern portPattern = Pattern.compile("<td><script>document.write\\((.*)\\);</script></td>");

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

			Matcher m = ipPattern.matcher(s);

			while (m.find()) {
				ipClass.setIp(m.group(1).trim());
			}

			if (StringUtils.isEmpty(ipClass.getIp())) {
				continue;
			}


			m = portPattern.matcher(s);

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

                saveProxy(ipClass.getIp() + ":" + ipClass.getPort());
			}
		}

		page.addTargetRequests(page.getHtml().xpath("//div[@class='natWap clear']/table").links().all());

        page.setSkip(true);

	}

    private void saveProxy(String key) {
        Jedis jedis = SpiderConstants.pool.getResource();
        try {
            jedis.sadd(Messages.PROXY_KEY, key);
        } finally {
            SpiderConstants.pool.returnResource(jedis);
        }
    }

	@Override
	public Site getSite() {
		return SiteUtils.getSpiderSite(useProxy);
	}

    public static void start() {
        Spider spider = Spider.create(new SpiderProcessor(true));
        spider.addUrl("http://pachong.org/");
        spider.setScheduler(new JedisScheduler(SpiderConstants.pool));
        spider.setSpiderListeners(Lists.newArrayList(new SpiderListener[]{new SpiderSearcherSpiderListener()}));
        spider.run();
        addProxy(spider.getSite());
    }
}
