package com.hewei.proxy;

import com.ning.http.client.ProxyServer;
import org.apache.commons.lang.StringUtils;

import java.util.concurrent.Semaphore;

/**
 * 
 * @author hewei
 * 
 * @date 2015/9/14  11:21
 *
 * @version 5.0
 *
 * @desc 
 *
 */
public class IpProxyAsyncHttp extends Thread {

    static final Semaphore semaphore =new Semaphore(500);

    @Override
    public void run() {
        while (true) {
            String ip = IpProxyTools.SUCCESS_QUEUE.poll();
            if (StringUtils.isEmpty(ip)) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }
            //						for (int port : selectPorts) {
            //							try {
            //								semaphore.acquire();
            //							} catch (InterruptedException e) {
            //								e.printStackTrace();
            //							}
            //							proxyServers.set(new ProxyServer(ip, port));
            //							client.prepareGet(URL).setBodyEncoding("UTF-8").execute(new B(ip, port,semaphore));
            //						}
            for (int port = 1; port < 65535; port++) {
                try {
                    semaphore.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                IpProxyTools.proxyServers.set(new ProxyServer(ip, port));
                IpProxyTools.client().prepareGet(IpProxyConstants.URL).setBodyEncoding(IpProxyConstants.UTF_8).execute(new IpProxyAsyncHandler(ip, port, semaphore));
            }

            IpProxyTools.OVER_SET.add(ip);

        }
    }
}
