package com.hewei.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;

/**
 * 
 * @author hewei
 * 
 * @date 2015/9/14  11:16
 *
 * @version 5.0
 *
 * @desc 
 *
 */
public class IpSelector2 extends Thread {

    private static final Logger logger = LoggerFactory.getLogger(IpSelector2.class);

    @Override
    public void run() {
        while (true) {
            String ip = IpProxyTools.randomIp();



            if (IpProxyTools.ERROR_SET.contains(ip)) {
                continue;
            }

            if (IpProxyTools.OVER_SET.contains(ip)) {
                continue;
            }

            try {
                if (InetAddress.getByName(ip).isReachable(3000)) {
                    IpProxyTools.SUCCESS_QUEUE.offer(ip);
                    logger.info("isReachable:" + ip);
                } else {
                    IpProxyTools.ERROR_SET.add(ip);
                }
            } catch (IOException e) {
                IpProxyTools.ERROR_SET.add(ip);
            }
        }
    }

}
