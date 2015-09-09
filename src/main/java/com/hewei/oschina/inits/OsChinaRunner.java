package com.hewei.oschina.inits;

import com.hewei.oschina.constants.OsChinaConstants;
import com.hewei.oschina.pipeline.OsChinaPipeline;
import com.hewei.oschina.processor.OsChinaProcessor;
import com.hewei.oschina.utils.JedisUtils;
import com.hewei.spider.scheduler.JedisScheduler;
import us.codecraft.webmagic.Spider;

/**
 * 
 * @author hewei
 * 
 * @date 2015/9/9  15:32
 *
 * @version 5.0
 *
 * @desc 
 *
 */
public class OsChinaRunner {

    public static void start() {
//        OsChinaEsUtils.createIndex();
        Spider spider = Spider.create(new OsChinaProcessor());
        spider.addUrl(originalUrl());
        spider.setScheduler(new JedisScheduler(JedisUtils.newPool()));
        spider.setExitWhenComplete(false);
        spider.thread(1);
        spider.addPipeline(new OsChinaPipeline());
        spider.run();
    }

    private static String[] originalUrl() {
        return new String[]{OsChinaConstants.ORIGINAL_URL};
    }
}
