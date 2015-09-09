package com.hewei.oschina.constants;

import com.hewei.oschina.inits.OsChinaInit;

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
public class OsChinaConstants {

    public static final String ES_INDEX_NAME = "oschina_index";

    public static final String ORIGINAL_HTML = "originalhtml";

    public static final String CITY = "city";

    public static final String ACTIVITY = "activity";

    public static final String ACTIVITY_DETAILS = "activityDetails";

    /**************************************************************************/

    public static final String REDIS_IP = OsChinaInit.getString("REDIS_IP");//REDIS

    public static final int REDIS_PORT = OsChinaInit.getInt("REDIS_PORT");

    public static final int REDIS_MAX_TOTAL = OsChinaInit.getInt("REDIS_MAX_TOTAL");

    public static final int REDIS_MAX_IDLE = OsChinaInit.getInt("REDIS_MAX_IDLE");

    public static final int REDIS_MAX_WAIT_MILLIS = OsChinaInit.getInt("REDIS_MAX_WAIT_MILLIS");

    public static final int REDIS_TIMEOUT = OsChinaInit.getInt("REDIS_TIMEOUT");

    public static final int ES_NUMBER_OF_SHARDS = OsChinaInit.getInt("ES_NUMBER_OF_SHARDS");//ES

    public static final int ES_NUMBER_OF_REPLICAS = OsChinaInit.getInt("ES_NUMBER_OF_REPLICAS");

    public static final String ES_CLUSTER_NAME = OsChinaInit.getString("ES_CLUSTER_NAME");

    public static final int ES_PORT = OsChinaInit.getInt("ES_PORT");

    public static final String ES_IP = OsChinaInit.getString("ES_IP");

    public static final String ORIGINAL_URL = OsChinaInit.getString("ORIGINAL_URL");


}
