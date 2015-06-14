import org.apache.commons.lang.StringUtils;
import org.htmlparser.Parser;
import org.htmlparser.beans.StringBean;
import org.htmlparser.util.ParserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import us.codecraft.webmagic.*;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.RedisScheduler;

import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;

/**
 * @author hewei
 * @version 5.0
 * @date 2015/6/10  16:49
 * @desc
 */
public class BaiduBaikeSpider implements PageProcessor {

	private static final Logger logger = LoggerFactory.getLogger(BaiduBaikeSpider.class);

	public static Site site = Site.me().enableHttpProxyPool().setSleepTime(1000).setCycleRetryTimes(1000).setTimeOut(100).setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31");

	private static JedisPool pool = new JedisPool(new JedisPoolConfig(), "172.18.2.35", 7009);

	//    private static final String PROXY_KEY = "proxy";

	//    static {
	//
	//        Jedis jedis = pool.getResource();
	//
	//        Set<String> set = new HashSet<>();
	//
	//        try {
	//            set = jedis.smembers(PROXY_KEY);
	//        } finally {
	//            pool.returnResource(jedis);
	//        }
	//
	//        List<String> list =new ArrayList<>(set);
	//
	//        if (!list.isEmpty()) {
	//            String[][] httpProxyList = new String[list.size()][2];
	//            for (int i = 0; i < list.size(); i++) {
	//                String[] tmp = list.get(i).split(":");
	//                httpProxyList[i] = new String[]{tmp[0], tmp[1]};
	//            }
	//            site.getHttpProxyPool().addProxy(httpProxyList);
	//        }
	//
	//    }
	//    .setHttpProxy(new HttpHost("117.162.113.170", 8123))

	private static final String middot = "&middot;";

	@Override
	public void process(Page page) {
		newDeal(page);
		if (errorDeal(page)) {
			return;
		}
		originalHtmlDeal(page);
		nameDeal(page);
		descDeal(page);
		experienceDeal(page);
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

		page.putField(experience, getPlainText(e.replaceAll(middot, ".")));
	}

	private void newDeal(Page page){
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

		page.putField(desc, getPlainText(d.replaceAll(middot, ".")));
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

	@Override
	public Site getSite() {
		return site;
	}

	//    private static JedisPoolConfig config = new JedisPoolConfig();
	//
	//    static {
	//        config.setMaxTotal(1024);
	//        config.setMaxIdle(200);
	//        config.setMaxWaitMillis(1000);
	//        config.setTestOnBorrow(true);
	//    }



	public static void main(String[] args) {
		//        JedisPool pool = new JedisPool(config, "172.18.2.35",7009, 1000000);

		Spider.create(new BaiduBaikeSpider()).addUrl("http://baike.baidu.com/view/1758.htm").setScheduler(new RedisScheduler(pool)).addPipeline(new StoragePipeline()).setExitWhenComplete(false).thread(1).run();//刘德华
		//        Spider.create(new BaiduBaikeSpider()).addUrl("http://baike.baidu.com/subview/2375/2375.htm").addPipeline(new StoragePipeline()).run();//李彦宏
		//        Spider.create(new BaiduBaikeSpider()).addUrl("http://baike.baidu.com/subview/18696/6054611.htm").addPipeline(new StoragePipeline()).run();//青春期
		//        Spider.create(new BaiduBaikeSpider()).addUrl("http://baike.baidu.com/view/1674619.htm").addPipeline(new StoragePipeline()).run();//李沁
		//        Spider.create(new BaiduBaikeSpider()).addUrl("http://baike.baidu.com/subview/3064/3064.htm").addPipeline(new StoragePipeline()).run();//刘亦菲
		//        Spider.create(new BaiduBaikeSpider()).addUrl("http://baike.baidu.com/subview/2075/11117599.htm").addPipeline(new StoragePipeline()).run();//比尔
		//        Spider.create(new BaiduBaikeSpider()).addUrl("http://baike.baidu.com/subview/16360/5414449.htm").addPipeline(new StoragePipeline()).run();//马云
		//        Spider.create(new BaiduBaikeSpider()).addUrl("http://baike.baidu.com/subview/8125411/8784937.htm").addPipeline(new StoragePipeline()).run();//不存在的页面
	}

	public static class StoragePipeline implements Pipeline {

		@Override
		public void process(ResultItems resultItems, Task task) {
			StorageData data = new StorageData();
			data.setUrl(resultItems.getRequest().getUrl());
			Map<String, Object> map = resultItems.getAll();
			data.setErrorPage(map.get(errorPage) != null);
			if(data.isErrorPage()){
				logger.info("error page");
				return;
			}
			data.setName(map.get(name).toString());
			data.setDesc(map.get(desc).toString());
			data.setExperience(map.get(experience).toString());
			data.setOriginalHtml(map.get(originalHtml).toString());
			data.setSearchText(getPlainText(data.getOriginalHtml()));
			logger.info(data.getName());
			//            logger.info(data.getDesc());
			//            logger.info(data.getExperience());

			try {
				FileOutputStream fileOutputStream = new FileOutputStream("C:\\Users\\colin.he\\Desktop\\111.txt", true);
				fileOutputStream.write((data.getName() + "\r\n").getBytes("UTF-8"));
				fileOutputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	private static final String originalHtml = "originalhtml";

	private static final String name = "name";

	private static final String desc = "desc";

	private static final String experience = "experience";

	private static final String errorPage = "errorPage";

	public static String getPlainText(String str) {
		if (StringUtils.isEmpty(str)) {
			return str;
		}
		try {
			Parser parser = new Parser();
			parser.setInputHTML(str);
			//            TextExtractingVisitor visitor=new TextExtractingVisitor();
			//            parser.visitAllNodesWith(visitor);
			//            str=visitor.getExtractedText();
			StringBean bean = new StringBean();
			bean.setLinks(false);//设置不需要得到页面锁包含的链接信息
			bean.setReplaceNonBreakingSpaces(true);//设置将不间断空格由正规空格所替代
			bean.setCollapse(true);//设置将一序列空格由一个单一空格所代替
			parser.visitAllNodesWith(bean);
			str = bean.getStrings();

		} catch (ParserException e) {
			e.printStackTrace();
		}

		return str;
	}

	public static class StorageData {

		private String url;

		private String originalHtml;

		private String searchText;

		private String name;

		private String desc;

		private String experience;

		private boolean errorPage;

		public String getExperience() {
			return experience;
		}

		public void setExperience(String experience) {
			this.experience = experience;
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getOriginalHtml() {
			return originalHtml;
		}

		public void setOriginalHtml(String originalHtml) {
			this.originalHtml = originalHtml;
		}

		public String getSearchText() {
			return searchText;
		}

		public void setSearchText(String searchText) {
			this.searchText = searchText;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public boolean isErrorPage() {
			return errorPage;
		}

		public void setErrorPage(boolean errorPage) {
			this.errorPage = errorPage;
		}
	}
}
