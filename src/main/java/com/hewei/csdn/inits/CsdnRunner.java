package com.hewei.csdn.inits;

import com.hewei.common.utils.JedisUtils;
import com.hewei.csdn.constants.CsdnConstants;
import com.hewei.csdn.pipeline.CsdnPipeline;
import com.hewei.csdn.processor.CsdnProcessor;
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
public class CsdnRunner {

    public static void start() {
//        EsUtils.createIndex(CsdnConstants.ES_INDEX_NAME);
        Spider spider = Spider.create(new CsdnProcessor());
        spider.addUrl(originalUrl());
        spider.setScheduler(new JedisScheduler(JedisUtils.newPool()));
        spider.setExitWhenComplete(false);
        spider.thread(1);
        spider.addPipeline(new CsdnPipeline());
        spider.run();
    }

    private static String[] originalUrl() {
        return new String[]{CsdnConstants.ORIGINAL_URL};
    }
}
