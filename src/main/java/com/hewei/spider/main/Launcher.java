package com.hewei.spider.main;

import com.hewei.spider.pipeline.StoragePipeline;
import com.hewei.spider.processor.BaiduBaikeProcessor;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.scheduler.RedisScheduler;

/**
 * 
 * @author hewei
 * 
 * @date 2015/6/15  0:21
 *
 * @version 5.0
 *
 * @desc 
 *
 */
public class Launcher {
	private static JedisPool pool = new JedisPool(new JedisPoolConfig(), "172.18.2.35", 7009);
	public static void main(String[] args) {
		//        JedisPool pool = new JedisPool(config, "172.18.2.35",7009, 1000000);

		Spider.create(new BaiduBaikeProcessor()).addUrl("http://baike.baidu.com/view/1758.htm").setScheduler(new RedisScheduler(pool)).addPipeline(new StoragePipeline()).setExitWhenComplete(false).thread(1).run();//刘德华
		//        Spider.create(new BaiduBaikeSpider()).addUrl("http://baike.baidu.com/subview/2375/2375.htm").addPipeline(new StoragePipeline()).run();//李彦宏
		//        Spider.create(new BaiduBaikeSpider()).addUrl("http://baike.baidu.com/subview/18696/6054611.htm").addPipeline(new StoragePipeline()).run();//青春期
		//        Spider.create(new BaiduBaikeSpider()).addUrl("http://baike.baidu.com/view/1674619.htm").addPipeline(new StoragePipeline()).run();//李沁
		//        Spider.create(new BaiduBaikeSpider()).addUrl("http://baike.baidu.com/subview/3064/3064.htm").addPipeline(new StoragePipeline()).run();//刘亦菲
		//        Spider.create(new BaiduBaikeSpider()).addUrl("http://baike.baidu.com/subview/2075/11117599.htm").addPipeline(new StoragePipeline()).run();//比尔
		//        Spider.create(new BaiduBaikeSpider()).addUrl("http://baike.baidu.com/subview/16360/5414449.htm").addPipeline(new StoragePipeline()).run();//马云
		//        Spider.create(new BaiduBaikeSpider()).addUrl("http://baike.baidu.com/subview/8125411/8784937.htm").addPipeline(new StoragePipeline()).run();//不存在的页面
	}
}
