package com.hewei.oschina.pipeline;

import com.hewei.oschina.constants.OsChinaConstants;
import com.hewei.oschina.pojos.OsChinaRootCity;
import org.apache.commons.collections.CollectionUtils;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author hewei
 * 
 * @date 2015/9/9  16:08
 *
 * @version 5.0
 *
 * @desc 
 *
 */
public class OsChinaPipeline implements Pipeline {

    @Override
    public void process(ResultItems resultItems, Task task) {
        Map<String, Object> map = resultItems.getAll();
        System.out.println("1-----");
        List<OsChinaRootCity> list = (List<OsChinaRootCity>) map.get(OsChinaConstants.CITY);
        if (CollectionUtils.isNotEmpty(list)) {
            for (OsChinaRootCity city : list) {
                System.out.println(city.getName() + "--" + city.getUrl());
            }
        }
        System.out.println("1-----end");
    }
}
