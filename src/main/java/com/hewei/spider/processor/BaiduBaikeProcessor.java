package com.hewei.spider.processor;

import com.hewei.spider.constants.SpiderConstants;
import com.hewei.spider.es.ESUtils;
import com.hewei.spider.jdbc.DataSourceUtils;
import com.hewei.spider.pipeline.EsPipeline;
import com.hewei.spider.scheduler.JedisScheduler;
import com.hewei.spider.site.SiteUtils;
import com.hewei.spider.utils.HtmlUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpHost;
import org.assertj.core.util.Collections;
import org.elasticsearch.client.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;

import java.util.List;

import static com.hewei.spider.constants.Messages.*;

/**
 * @author hewei
 * @version 5.0
 * @date 2015/6/15  0:15
 * @desc
 */
public class BaiduBaikeProcessor extends BaseProcessor {

	private static final Logger logger = LoggerFactory.getLogger(BaiduBaikeProcessor.class);

    private boolean useProxy;

    public BaiduBaikeProcessor(boolean useProxy) {
        this.useProxy = useProxy;
    }

    @Override
    public void process(Page page) {
        newDeal(page);
        if (errorDeal(page)) {
            return;
        }

        if (useProxy) {
            proxyDeal(page);
        }

        if (filter(page)) {
            return;
        }
        originalHtmlDeal(page);
        nameDeal(page);
        descDeal(page);
        experienceDeal(page);
    }

    @Override
    public Site getSite() {
        return SiteUtils.getBaiduBaikeSite(useProxy);
    }

    private boolean filter(Page page) {
        List<String> list = page.getHtml().xpath("//div[@id=\"lemma-list\"]//li[@class=\"list-dot list-dot-paddingleft\"]").links().all();
        if (Collections.isNullOrEmpty(list)) {
            return false;
        }
        page.addTargetRequests(list);
        page.putField(filter, 1);
        return true;
    }

    private void proxyDeal(Page page) {
        HttpHost host = (HttpHost) page.getRequest().getExtra(Request.PROXY);
        page.putField(httpHost, host.getHostName() + ":" + host.getPort());
    }

	private void experienceDeal(Page page) {
		String e = page.getHtml().xpath("//div[@class='lemma-main-content']").replace("(\\[.*])", "").toString();
		//        List<String> eList = page.getHtml().xpath("//span[@class='headline-content']").replace("(\\[.*])", "").all();
		if (StringUtils.isEmpty(e)) {
			e = page.getHtml().xpath("//div[@class='main_tab main_tab-defaultTab curTab']").replace("(\\[.*])", "").toString();
		}

		if (StringUtils.isEmpty(e)) {
			e = "";
			logger.error(page.getUrl() + "---experience null");
		}

		page.putField(experience, HtmlUtils.getPlainText(e.replaceAll(middot, ".")));
	}

	private void newDeal(Page page) {
		page.addTargetRequests(page.getHtml().xpath("//div[@class=\"zhixin-group js-group\"]").links().all());
	}

	private boolean errorDeal(Page page) {
		String error = page.getHtml().xpath("//div[@class='errorBox']").toString();
		if (StringUtils.isEmpty(error)) {
			return false;
		}
		if (error.contains("您所访问的页面不存在")) {
			page.putField(errorPage, true);
			return true;
		}
		return false;
	}

	private void originalHtmlDeal(Page page) {
		page.putField(originalHtml, page.getHtml());
	}

	private void descDeal(Page page) {
		String d = page.getHtml().xpath("//div[@class='card-summary-content']").replace("(\\[.*])", "").toString();

		if (StringUtils.isEmpty(d)) {
			d = page.getHtml().xpath("//div[@class='lemma-summary']").replace("(\\[.*])", "").toString();
		}

		if (StringUtils.isEmpty(d)) {
			List<String> list = page.getHtml().xpath("//div[@id='lemmaContent-0']//div[@class='para']/allText()").all();
			StringBuilder builder = new StringBuilder();
			for (String s : list) {
				builder.append(s).append("\r\n");
			}
			d = builder.toString().replace("(\\[.*])", "");
		}

		if (StringUtils.isEmpty(d)) {
			d = "";
			logger.error(page.getUrl() + "---desc null");
		}

		page.putField(desc, HtmlUtils.getPlainText(d.replaceAll(middot, ".")));
	}

	private void nameDeal(Page page) {
		String n = page.getHtml().xpath("//span[@class='lemmaTitleH1']/allText()").toString();//马云.regex(">(.*)<[s//]pan>")

		if (StringUtils.isEmpty(n)) {
			n = page.getHtml().xpath("//h1[@class='lemmaTitle']").regex("<span>(.*?)</span>").toString();//刘德华
		}

		if (StringUtils.isEmpty(n)) {
			n = page.getHtml().xpath("//dd[@class='lemmaWgt-lemmaTitle-title']/h1/allText()").toString();//刘亦菲
		}

		if (StringUtils.isEmpty(n)) {
			n = page.getHtml().xpath("//div[@class='posterContent']/dl/dt").regex("<dt>(.*?)<[s//]").toString();//李沁
		}
		if (StringUtils.isEmpty(n)) {
			n = page.getHtml().xpath("//div[@class='lemmaTitleH1']/span/allText()").toString();//青春期
		}

		if (StringUtils.isEmpty(n)) {
			n = page.getHtml().xpath("//h1[@class='lemmaTitle']/allText()").toString();//李彦宏
		}

		if (StringUtils.isEmpty(n)) {
			n = "";
			logger.error(page.getUrl() + "---name null");
		}

		page.putField(name, n.replaceAll(middot, ".").trim());
	}

    public static void start() {
        DataSourceUtils.createTable();
        try (Client client = ESUtils.getClient()) {
            ESUtils.createIndex(client);
            ESUtils.createMapping(client);
        }
        Spider spider = Spider.create(new BaiduBaikeProcessor(true));
        spider.addUrl(originalUrl());
        spider.setScheduler(new JedisScheduler(SpiderConstants.pool));
        spider.addPipeline(new EsPipeline());
        spider.setExitWhenComplete(false);
        spider.thread(50);
        spider.run();
        addProxy(spider.getSite());
    }

    private static String[] originalUrl(){
        return new String[]{
                "http://baike.baidu.com/view/1758.htm",
                "http://baike.baidu.com/subview/2375/2375.htm",
                "http://baike.baidu.com/subview/18696/6054611.htm",
                "http://baike.baidu.com/view/1674619.htm",
                "http://baike.baidu.com/subview/3064/3064.htm",
                "http://baike.baidu.com/subview/2075/11117599.htm",
                "http://baike.baidu.com/subview/16360/5414449.htm",
        };
    }

	public static void otherStart() {
		//        Spider.create(new BaiduBaikeSpider()).addUrl("http://baike.baidu.com/view/1758.htm").addPipeline(new StoragePipeline()).run();//刘德华
		//        Spider.create(new BaiduBaikeSpider()).addUrl("http://baike.baidu.com/subview/2375/2375.htm").addPipeline(new StoragePipeline()).run();//李彦宏
		//        Spider.create(new BaiduBaikeSpider()).addUrl("http://baike.baidu.com/subview/18696/6054611.htm").addPipeline(new StoragePipeline()).run();//青春期
		//        Spider.create(new BaiduBaikeSpider()).addUrl("http://baike.baidu.com/view/1674619.htm").addPipeline(new StoragePipeline()).run();//李沁
		//        Spider.create(new BaiduBaikeSpider()).addUrl("http://baike.baidu.com/subview/3064/3064.htm").addPipeline(new StoragePipeline()).run();//刘亦菲
		//        Spider.create(new BaiduBaikeSpider()).addUrl("http://baike.baidu.com/subview/2075/11117599.htm").addPipeline(new StoragePipeline()).run();//比尔
		//        Spider.create(new BaiduBaikeSpider()).addUrl("http://baike.baidu.com/subview/16360/5414449.htm").addPipeline(new StoragePipeline()).run();//马云
		//        Spider.create(new BaiduBaikeSpider()).addUrl("http://baike.baidu.com/subview/8125411/8784937.htm").addPipeline(new StoragePipeline()).run();//不存在的页面
	}

}
