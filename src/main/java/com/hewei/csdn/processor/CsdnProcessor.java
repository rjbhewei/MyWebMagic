package com.hewei.csdn.processor;

import com.hewei.csdn.site.CsdnSite;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.HashSet;
import java.util.Set;

/**
 * 
 * @author hewei
 * 
 * @date 2015/9/9  15:14
 *
 * @version 5.0
 *
 * @desc 
 *
 */
public class CsdnProcessor implements PageProcessor {

    @Override
    public void process(Page page) {

    }

    @Override
    public Site getSite() {
        return CsdnSite.getSite();
    }
}
