package com.hewei.proxy;

import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 
 * @author hewei
 * 
 * @date 2015/9/14  11:08
 *
 * @version 5.0
 *
 * @desc 
 *
 */
public class IpProxyAsyncHandler extends AsyncCompletionHandler {

    private static final Logger logger = LoggerFactory.getLogger(IpProxyAsyncHandler.class);

    private static AtomicLong count = new AtomicLong();

    private String ip;

    private int port;

    private Semaphore semaphore;

    public IpProxyAsyncHandler(String ip, int port, Semaphore semaphore) {
        this.ip = ip;
        this.port = port;
        this.semaphore = semaphore;
    }

    @Override
    public Object onCompleted(Response response) throws Exception {
        logger.info("getStatusCode:{}---ip:{}---port:{}", response.getStatusCode(), ip, port);
        semaphore.release();
        return null;
    }

    @Override
    public void onThrowable(Throwable t) {
        long num = count.incrementAndGet();
        if (num % 1000 == 0) {
            System.out.println(num);
        }
        semaphore.release();
    }
}
