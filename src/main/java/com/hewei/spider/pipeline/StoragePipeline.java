package com.hewei.spider.pipeline;

import com.hewei.spider.jdbc.DataSourceUtils;
import com.hewei.spider.jdbc.Star;
import com.hewei.spider.pojos.StorageData;
import com.hewei.spider.utils.HtmlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.Map;

import static com.hewei.spider.constants.Messages.*;
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
public class StoragePipeline implements Pipeline {
	private static final Logger logger = LoggerFactory.getLogger(StoragePipeline.class);

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
		data.setSearchText(HtmlUtils.getPlainText(data.getOriginalHtml()));
		logger.info(data.getName());
        DataSourceUtils.insertData(new Star(data.getName()));
    }
}
