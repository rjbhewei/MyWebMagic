package com.hewei.csdn.pipeline;

import com.hewei.common.utils.EsUtils;
import com.hewei.csdn.pojos.CsdnActivity;
import com.hewei.csdn.pojos.CsdnActivityDetails;
import org.apache.commons.collections.CollectionUtils;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import static com.hewei.csdn.constants.CsdnConstants.*;
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
public class CsdnPipeline implements Pipeline {

    private AtomicLong total=new AtomicLong();

    @Override
    public void process(ResultItems resultItems, Task task) {

        Map<String, Object> map = resultItems.getAll();

        List<CsdnActivity> activityList = (List<CsdnActivity>) map.get(ACTIVITY);

        if (CollectionUtils.isNotEmpty(activityList)) {
            for (CsdnActivity activity : activityList) {
                EsUtils.add(ES_INDEX_NAME, ACTIVITY, activity);
            }
            System.out.println(total.addAndGet(activityList.size()));
            return;
        }

        CsdnActivityDetails activityDetails = (CsdnActivityDetails) map.get(ACTIVITY_DETAILS);
        if (activityDetails != null) {
            EsUtils.add(ES_INDEX_NAME, ACTIVITY_DETAILS, activityDetails);
            System.out.println(total.incrementAndGet());
            return;
        }


        System.out.println("-----");
    }
}
