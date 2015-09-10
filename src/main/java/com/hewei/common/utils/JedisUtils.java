package com.hewei.common.utils;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import static com.hewei.common.constants.CommonConstants.*;
/**
 * 
 * @author hewei
 * 
 * @date 2015/9/9  15:56
 *
 * @version 5.0
 *
 * @desc 
 *
 */
public class JedisUtils {

    public static JedisPool newPool() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(REDIS_MAX_TOTAL);
        config.setMaxIdle(REDIS_MAX_IDLE);
        config.setMaxWaitMillis(REDIS_MAX_WAIT_MILLIS);
        config.setTestOnBorrow(true);
        return new JedisPool(config, REDIS_IP, REDIS_PORT, REDIS_TIMEOUT);
    }
}
