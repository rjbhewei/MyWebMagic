package com.hewei.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * 
 * @author hewei
 * 
 * @date 2015/9/13  16:50
 *
 * @version 5.0
 *
 * @desc 
 *
 */
public class IpProxyGenerator {

    private static final Logger logger = LoggerFactory.getLogger(IpProxyGenerator.class);

    public static void main(String[] args) throws IOException {
        ipSelectorStart();
        ipProxyAsyncHttpStart();
    }

    public static void ipProxyAsyncHttpStart() {
        for (int i = 0; i < IpProxyConstants.IPPROXYASYNCHTTP_THREAD; i++) {
            new IpProxyAsyncHttp().start();
        }
        logger.info("IpProxyAsyncHttp start");
    }

    public static void ipSelectorStart() {
        for (int i = 0; i < IpProxyConstants.IPSELECTOR_THREAD; i++) {
            new IpSelector2().start();
        }
        logger.info("IpSelector start");
    }
}
