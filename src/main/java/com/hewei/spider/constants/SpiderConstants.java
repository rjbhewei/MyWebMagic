package com.hewei.spider.constants;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 
 * @author hewei
 * 
 * @date 2015/6/15  2:31
 *
 * @version 5.0
 *
 * @desc 
 *
 */
public class SpiderConstants {
	public static JedisPool pool = new JedisPool(new JedisPoolConfig(), "172.18.2.37", 7008);
}
