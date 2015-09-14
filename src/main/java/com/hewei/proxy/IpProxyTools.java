package com.hewei.proxy;

import com.google.common.collect.Sets;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClientConfig;
import com.ning.http.client.ProxyServer;

import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 
 * @author hewei
 * 
 * @date 2015/9/14  11:26
 *
 * @version 5.0
 *
 * @desc 
 *
 */
public class IpProxyTools {

    public static class HEWEI {

        public static final AsyncHttpClientConfig.Builder builder = new AsyncHttpClientConfig.Builder();

        static {
            builder.setRequestTimeout(IpProxyConstants.timeout);
            builder.setConnectTimeout(IpProxyConstants.timeout);
            builder.setProxyServerSelector(new IpProxyServerSelector());
        }

        public static final AsyncHttpClient asyncHttpClient = new AsyncHttpClient(builder.build());
    }

    public static AsyncHttpClient client() {
        return HEWEI.asyncHttpClient;
    }

    public static Set<String> OVER_SET = Sets.newConcurrentHashSet();

    public static Queue<String> SUCCESS_QUEUE = new ConcurrentLinkedQueue<>();

    public static Set<String> ERROR_SET = Sets.newConcurrentHashSet();

    public static ThreadLocal<ProxyServer> proxyServers = new ThreadLocal<>();

    private static final int IP_BLOCK_MAX = 255;

    private static Random IP_RANDOM = new Random();

    public static String randomIp() {
        int block1 = IP_RANDOM.nextInt(IP_BLOCK_MAX) + 1;
        int block2 = IP_RANDOM.nextInt(IP_BLOCK_MAX) + 1;
        int block3 = IP_RANDOM.nextInt(IP_BLOCK_MAX) + 1;
        int block4 = IP_RANDOM.nextInt(IP_BLOCK_MAX) + 1;
        return block1 + "." + block2 + "." + block3 + "." + block4;
    }

}
