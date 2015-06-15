package com.hewei.spider.listener;

import com.hewei.spider.constants.SpiderConstants;
import redis.clients.jedis.Jedis;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.SpiderListener;

/**
 * 
 * @author hewei
 * 
 * @date 2015/6/15  12:18
 *
 * @version 5.0
 *
 * @desc 
 *
 */
public class SpiderSearcherSpiderListener implements SpiderListener {

    private static final String LISTENER_KEY = "proxy_listener";

    @Override
    public void onSuccess(Request request) {
        Jedis jedis = SpiderConstants.pool.getResource();
        try {
            jedis.rpush(LISTENER_KEY + "_success", request.getUrl());
        } finally {
            SpiderConstants.pool.returnResource(jedis);
        }
    }

    @Override
    public void onError(Request request) {
//        Jedis jedis = SpiderConstants.pool.getResource();
//        try {
//            jedis.rpush(LISTENER_KEY + "_error", request.getUrl());
//        } finally {
//            SpiderConstants.pool.returnResource(jedis);
//        }
    }

}
