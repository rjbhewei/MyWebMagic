package com.hewei.common.constants;

import com.hewei.common.inits.CommonInit;

/**
 * 
 * @author hewei
 * 
 * @date 2015/9/9  15:57
 *
 * @version 5.0
 *
 * @desc 
 *
 */
public class CommonConstants {

    public static final String REDIS_IP = CommonInit.getString("REDIS_IP");//REDIS

    public static final int REDIS_PORT = CommonInit.getInt("REDIS_PORT");

    public static final int REDIS_MAX_TOTAL = CommonInit.getInt("REDIS_MAX_TOTAL");

    public static final int REDIS_MAX_IDLE = CommonInit.getInt("REDIS_MAX_IDLE");

    public static final int REDIS_MAX_WAIT_MILLIS = CommonInit.getInt("REDIS_MAX_WAIT_MILLIS");

    public static final int REDIS_TIMEOUT = CommonInit.getInt("REDIS_TIMEOUT");

    public static final int ES_NUMBER_OF_SHARDS = CommonInit.getInt("ES_NUMBER_OF_SHARDS");//ES

    public static final int ES_NUMBER_OF_REPLICAS = CommonInit.getInt("ES_NUMBER_OF_REPLICAS");

    public static final String ES_CLUSTER_NAME = CommonInit.getString("ES_CLUSTER_NAME");

    public static final int ES_PORT = CommonInit.getInt("ES_PORT");

    public static final String ES_IP = CommonInit.getString("ES_IP");

}
