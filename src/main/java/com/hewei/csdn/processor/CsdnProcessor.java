package com.hewei.csdn.processor;

import com.google.common.collect.Lists;
import com.hewei.csdn.constants.CsdnConstants;
import com.hewei.csdn.site.CsdnSite;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

import java.util.HashSet;
import java.util.List;
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

    private Set<String> urlSet = new HashSet<>();

    @Override
    public void process(Page page) {
        String url = page.getUrl().get();
        if (CsdnConstants.ORIGINAL_URL.equals(url) || !urlSet.contains(url)) {
            activityDeal(page);
            return;
        }
        System.out.println("no deal url:" + url);
    }

    private void activityDeal(Page page) {
        List<String> pages = page.getHtml().xpath("//div[@class='csdn-pagination hide-set hide-go']//a/text()").all();
        Set<String> set = new HashSet<>(pages.size());
        for (String message : pages) {
            if (!isNumeric(message)) {
                continue;
            }
            String newUrl = CsdnConstants.ORIGINAL_URL + "?&page=" + message;
            System.out.println("newUrl:"+newUrl);
            set.add(newUrl);
        }

        newDeal(page, set);

        List<String> activityList = page.getHtml().xpath("//div[@class='item clearfix']").all();

        System.out.println("------");
        for (String message : activityList) {

            Html html = Html.create(message);
            Selectable wraper = html.xpath("//div[@class='time-wraper']");

            String dayTime = wraper.xpath("//div[@class='month']/text()").get();
            System.out.println("dayTime:"+dayTime);
            String addr = wraper.xpath("//div[@class='addr']/text()").get();
            System.out.println("addr:"+addr);

            Selectable dis = html.xpath("//div[@class='dis']/dl");

            String url  = dis.xpath("//dt").links().get();
            System.out.println("url:"+url);

            String title  = dis.xpath("//dt/a/text()").get();
            System.out.println("title:"+title);

            String expense  = dis.xpath("//dd[1]/text()").get();
            System.out.println("expense:"+expense);

            String specificTime  = dis.xpath("//dd[2]/text()").get();
            System.out.println("specificTime:"+specificTime);

            String location  = dis.xpath("//dd[3]/a/text()").get();
            System.out.println("location:"+location);
        }

        System.out.println("------end");

        urlSet.add(page.getUrl().get());

    }

    private void newDeal(Page page, Set<String> set) {
        page.addTargetRequests(Lists.newArrayList(set));
    }

    public static boolean isNumeric(String str) {
        for (int i = str.length(); --i >= 0; ) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Site getSite() {
        return CsdnSite.getSite();
    }
}
