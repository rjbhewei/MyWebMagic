package com.hewei.spider.processor;

import com.hewei.spider.constants.Messages;
import com.hewei.spider.constants.SpiderConstants;
import redis.clients.jedis.Jedis;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @author hewei
 * 
 * @date 2015/6/15  12:44
 *
 * @version 5.0
 *
 * @desc 
 *
 */
public abstract class BaseProcessor implements PageProcessor {
    public static void addProxy(final Site site) {
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(new Runnable() {

            @Override
            public void run() {

                Jedis jedis = SpiderConstants.pool.getResource();

                Set<String> set = new HashSet<>();

                try {
                    set = jedis.smembers(Messages.PROXY_KEY);
                } finally {
                    SpiderConstants.pool.returnResource(jedis);
                }

                if (set.isEmpty()) {
                    return;
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
        }, 10, 10, TimeUnit.SECONDS);
    }
}
