package com.hewei.csdn.site;

import us.codecraft.webmagic.Site;

/**
 * 
 * @author hewei
 * 
 * @date 2015/9/9  15:15
 *
 * @version 5.0
 *
 * @desc 
 *
 */
public class CsdnSite {
    private static final String USERAGENT="Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31";

    public static Site getSite() {
        Site site = Site.me();
//        site.setDomain("blog.sina.com.cn");
        site.setSleepTime(5000);
        site.setUserAgent(USERAGENT);
        return site;
    }
}
