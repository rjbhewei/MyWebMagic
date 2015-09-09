package com.hewei.oschina.pipeline;

import com.hewei.oschina.constants.OsChinaConstants;
import com.hewei.oschina.pojos.OsChinaActivity;
import com.hewei.oschina.pojos.OsChinaActivityDetails;
import com.hewei.oschina.pojos.OsChinaRootCity;
import com.hewei.oschina.utils.OsChinaEsUtils;
import org.apache.commons.collections.CollectionUtils;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.List;
import java.util.Map;
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

		System.out.println(total.incrementAndGet());

		Map<String, Object> map = resultItems.getAll();

		List<OsChinaRootCity> cityList = (List<OsChinaRootCity>) map.get(OsChinaConstants.CITY);

		if (CollectionUtils.isNotEmpty(cityList)) {
			for (OsChinaRootCity city : cityList) {
				OsChinaEsUtils.add(OsChinaConstants.CITY, city);
			}
			return;
		}

		List<OsChinaActivity> activityList = (List<OsChinaActivity>) map.get(OsChinaConstants.ACTIVITY);

		if (CollectionUtils.isNotEmpty(activityList)) {
			for (OsChinaActivity activity : activityList) {
				OsChinaEsUtils.add(OsChinaConstants.ACTIVITY, activity);
			}
			return;
		}

		OsChinaActivityDetails activityDetails = (OsChinaActivityDetails) map.get(OsChinaConstants.ACTIVITY_DETAILS);

		if (activityDetails != null) {
			OsChinaEsUtils.add(OsChinaConstants.ACTIVITY_DETAILS, activityDetails);
		}

	}
}
