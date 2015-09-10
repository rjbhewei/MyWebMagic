package com.hewei.oschina.processor;

import com.google.common.collect.Lists;
import com.hewei.oschina.constants.OsChinaConstants;
import com.hewei.oschina.pojos.OsChinaActivity;
import com.hewei.oschina.pojos.OsChinaActivityDetails;
import com.hewei.oschina.pojos.OsChinaRootCity;
import com.hewei.oschina.site.OsChinaSite;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

import java.util.*;

import static com.hewei.oschina.constants.OsChinaConstants.*;

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
public class OsChinaProcessor implements PageProcessor {

    private Set<String> citySet = new HashSet<>();

    private Set<String> activitySet = new HashSet<>();

    @Override
    public void process(Page page) {

        String url = page.getUrl().get();

        if (OsChinaConstants.ORIGINAL_URL.equals(url)) {
            cityDeal(page);
            return;
        }

        if (citySet.contains(url)) {
            eventDeal(page);
            return;
        }

        if (activitySet.contains(url)) {
            activityDetails(page);
            return;
        }

        System.out.println("no deal url:" + url);

    }

    private void activityDetails(Page page) {
        Html pageHtml = page.getHtml();
        String headerMessage = pageHtml.xpath("//dl[@class='review_detail fleft']").toString();
        Html html = Html.create(headerMessage);
        String title = html.xpath("//dt[@class='review_title mb15']/span/text()").get();
        String time = html.xpath("//dd[1]/text()").get();
        String location = html.xpath("//dd[2]/text()").get();
        String expense = html.xpath("//dd[3]/text()").get();
        String type = html.xpath("//dd[4]/text()").get();
        String initiator = html.xpath("//dd[6]/a/text()").get();
        String initiatorUrl = html.xpath("//dd[6]").links().get();
        String desc = pageHtml.xpath("//div[@class='review_description mtb10 markdown-content']").toString();
        page.putField(ACTIVITY_DETAILS, new OsChinaActivityDetails(title, time, location, expense, type, initiator,initiatorUrl, desc));
    }

	private void eventDeal(Page page) {

		List<String> list = page.getHtml().xpath("//li[@class='event_reviewed_item mtb20 clearfix']").all();

		List<OsChinaActivity> activityList = new ArrayList<>(list.size());

		Set<String> set = new HashSet<>(list.size());

		for (String message : list) {

			Html html = Html.create(message);

			String url = html.xpath("//div[1]").links().get();

			set.add(url);

			String picture = html.xpath("//div[1]//img/@src").get();

			String title = html.xpath("//div[2]/[@class='event_reviewed_title pb10']/a/text()").get();

			String desc = html.xpath("//div[2]/text()").get();

			String time = html.xpath("//div[2]/[@class='event_reviewed_time pb5']/text()").get();

			String location = html.xpath("//div[2]/[@class='event_reviewd_position pb5']/text()").get();

			activityList.add(new OsChinaActivity(url, picture, title, desc, time, location));
		}
		page.putField(ACTIVITY, activityList);

		activitySet.addAll(set);

		List<String> pager = page.getHtml().xpath("//ul[@class='pager']/li[@class='page']").links().all();

		citySet.addAll(pager);

		page.putField(CITY, pager);

		set.addAll(pager);

		newDeal(page, set);
	}

    private void cityDeal(Page page) {

        List<String> list = page.getHtml().xpath("//div[@class='all_groups clearfix']/div[1]//a").all();

        List<OsChinaRootCity> cityList = new ArrayList<>(list.size());

        Set<String> set = new HashSet<>();

        for (String message : list) {

            Html html = Html.create(message);

            List<String> tmp = html.links().all();

            if (CollectionUtils.isEmpty(tmp)) {
                continue;
            }

            String url = tmp.get(0);

            String name = html.xpath("//a/text()").toString();

            if (StringUtils.isEmpty(name) || StringUtils.isEmpty(url)) {
                System.out.println("error name:" + name + "|url:" + url);
                continue;
            }

            String newUrl = url.endsWith("/") ? url + "event" : url + "/event";

            set.add(newUrl);

            cityList.add(new OsChinaRootCity(name, url));
        }

        page.putField(CITY, cityList);

        citySet.addAll(set);

        newDeal(page, set);
    }

    private void newDeal(Page page, Set<String> set) {
        page.addTargetRequests(Lists.newArrayList(set));
    }

    @Override
    public Site getSite() {
        return OsChinaSite.getSite();
    }
}
