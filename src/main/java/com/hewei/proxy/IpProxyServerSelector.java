package com.hewei.proxy;

import com.ning.http.client.ProxyServer;
import com.ning.http.client.ProxyServerSelector;
import com.ning.http.client.uri.Uri;

/**
 * 
 * @author hewei
 * 
 * @date 2015/9/14  11:14
 *
 * @version 5.0
 *
 * @desc 
 *
 */
public class IpProxyServerSelector implements ProxyServerSelector {

    @Override
    public ProxyServer select(Uri uri) {
        return IpProxyTools.proxyServers.get();
    }
}
