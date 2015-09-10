package com.hewei.oschina.pipeline;

import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.concurrent.atomic.AtomicLong;

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

	private AtomicLong total=new AtomicLong();

    @Override
    public void process(ResultItems resultItems, Task task) {

//        Map<String, Object> map = resultItems.getAll();
//
//        List<OsChinaRootCity> cityList = (List<OsChinaRootCity>) map.get(OsChinaConstants.CITY);
//
//        if (CollectionUtils.isNotEmpty(cityList)) {
//            for (OsChinaRootCity city : cityList) {
//                EsUtils.add(OsChinaConstants.ES_INDEX_NAME, OsChinaConstants.CITY, city);
//            }
//            System.out.println(total.addAndGet(cityList.size()));
//            return;
//        }
//
//        List<OsChinaActivity> activityList = (List<OsChinaActivity>) map.get(OsChinaConstants.ACTIVITY);
//
//        if (CollectionUtils.isNotEmpty(activityList)) {
//            for (OsChinaActivity activity : activityList) {
//                EsUtils.add(OsChinaConstants.ES_INDEX_NAME, OsChinaConstants.ACTIVITY, activity);
//            }
//            System.out.println(total.addAndGet(activityList.size()));
//            return;
//        }
//
//        OsChinaActivityDetails activityDetails = (OsChinaActivityDetails) map.get(OsChinaConstants.ACTIVITY_DETAILS);
//
//        if (activityDetails != null) {
//            EsUtils.add(OsChinaConstants.ES_INDEX_NAME, OsChinaConstants.ACTIVITY_DETAILS, activityDetails);
//            System.out.println(total.incrementAndGet());
//        }

    }
}
