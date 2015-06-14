package com.hewei.spider.pipeline;

import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

/**
 * 
 * @author hewei
 * 
 * @date 2015/6/15  1:46
 *
 * @version 5.0
 *
 * @desc 
 *
 */
public class EmptyPipeline implements Pipeline {

	@Override
	public void process(ResultItems resultItems, Task task) {
		//            Map<String, Object> map = resultItems.getAll();
		//            System.out.println(ipMap.size());
	}
}