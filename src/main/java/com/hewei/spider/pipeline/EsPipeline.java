package com.hewei.spider.pipeline;

import com.hewei.spider.es.ESUtils;
import com.hewei.spider.jdbc.DataSourceUtils;
import com.hewei.spider.jdbc.Star;
import com.hewei.spider.pojos.StorageData;
import com.hewei.spider.utils.HtmlUtils;
import org.elasticsearch.client.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.Map;
import java.util.concurrent.Executors;

import static com.hewei.spider.constants.Messages.*;
import static com.hewei.spider.constants.Messages.httpHost;
import static com.hewei.spider.constants.Messages.originalHtml;

/**
 * 
 * @author hewei
 * 
 * @date 2015/6/15  16:28
 *
 * @version 5.0
 *
 * @desc 
 *
 */
public class EsPipeline implements Pipeline {

    private static final Logger logger = LoggerFactory.getLogger(StoragePipeline.class);

    @Override
    public void process(final ResultItems resultItems, final Task task) {

        Executors.newFixedThreadPool(10).execute(new Runnable() {

            @Override
            public void run() {
                StorageData data = new StorageData();

                Map<String, Object> map = resultItems.getAll();
                data.setErrorPage(map.get(errorPage) != null);
                if (data.isErrorPage()) {
                    logger.info("error page");
                    return;
                }

                if (map.get(filter) != null) {
                    logger.info("skip page");
                    return;
                }

                data.setUrl(resultItems.getRequest().getUrl());
                data.setName(map.get(name).toString());
                data.setDesc(map.get(desc).toString());
                data.setExperience(map.get(experience).toString());
                data.setOriginalHtml(map.get(originalHtml).toString());
                data.setSearchText(HtmlUtils.getPlainText(data.getOriginalHtml()));

                logger.info(map.get(httpHost) + "-->" + data.getName());

                Star star = new Star(data.getName(), data.getUrl());

                DataSourceUtils.insertData(star);

                data.setCreateTime(star.getCreateTime());

                data.setId(star.getId());

                try (Client client = ESUtils.getClient()) {
                    ESUtils.add(client, data);
                }
            }
        });

    }

}
