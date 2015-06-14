package com.hewei.spider.pipeline;

import com.hewei.spider.pojos.StorageData;
import com.hewei.spider.utils.HtmlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.io.FileOutputStream;
import java.util.Map;
import static com.hewei.spider.contants.Messages.*;
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
		//            logger.info(data.getDesc());
		//            logger.info(data.getExperience());

		try {
			FileOutputStream fileOutputStream = new FileOutputStream("d:\\111.txt", true);
			fileOutputStream.write((data.getName() + "\r\n").getBytes("UTF-8"));
			fileOutputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
