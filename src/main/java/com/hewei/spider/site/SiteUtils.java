package com.hewei.spider.site;

import com.hewei.spider.constants.SpiderConstants;
import redis.clients.jedis.Jedis;
import us.codecraft.webmagic.Site;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.hewei.spider.constants.Messages.PROXY_KEY;

/**
 * 
 * @author hewei
 * 
 * @date 2015/6/15  12:32
 *
 * @version 5.0
 *
 * @desc 
 *
 */
public class SiteUtils {

    private static final String USERAGENT="Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31";

    public static Site getBaiduBaikeSite(boolean useProxy) {
        Site site = Site.me();
        site.setSleepTime(100);
        site.setCycleRetryTimes(1000);
        site.setTimeOut(500);
        site.setUserAgent(USERAGENT);
        if (useProxy) {
            loadProxy(site);
        }
        return site;
    }

    public static Site getSpiderSite(boolean useProxy){
        Site site = Site.me();
        site.setSleepTime(500);
        site.setUserAgent(USERAGENT);
        if(useProxy){
            loadProxy(site);
        }
        return site;
    }

    private static void loadProxy(Site site) {
        Jedis jedis = SpiderConstants.pool.getResource();

        Set<String> set = new HashSet<>();

        try {
            set = jedis.smembers(PROXY_KEY);
        } finally {
            SpiderConstants.pool.returnResource(jedis);
        }

        List<String> list = new ArrayList<>(set);

        if (!list.isEmpty()) {
            String[][] httpProxyList = new String[list.size()][2];
            for (int i = 0; i < list.size(); i++) {
                String[] tmp = list.get(i).split(":");
                httpProxyList[i] = new String[]{tmp[0], tmp[1]};
            }
            site.enableHttpProxyPool();
            site.getHttpProxyPool().addProxy(httpProxyList);
        }
    }
}
