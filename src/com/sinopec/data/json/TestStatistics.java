package com.sinopec.data.json;

import android.content.Context;
import com.lenovo.nova.util.debug.slog;
import com.lenovo.nova.util.parse.Bean;
import com.lenovo.nova.util.parse.JsonToBeanParser;
import com.lenovo.nova.util.parse.JsonToBeanParser.OnJSONFillBeanHelper;
import com.sinopec.data.json.statistics.StatisticsValue;
import org.json.JSONArray;

/**
 * 五、统计
 * @author liuzhaodong
 *
 */
public class TestStatistics extends TestQueryGuDing {

	public TestStatistics(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	/**
		
	 */
	public void testStatistics() {
		//http://<host>:<port>/
		String dzdybm = "200700000002";
		String url = Constant.baseURL +
				"peprisapi/statistical.html?dzdybm=" + dzdybm
				;
		slog.p("url " + url);
		JSONArray jsonArray = getJsonArray(url);
		
		final StatisticsValue instance = new StatisticsValue();
		JsonToBeanParser.getInstance().fillBeanWithJson(instance.mChilds, jsonArray, new OnJSONFillBeanHelper() {
			@Override
			public Bean getNewBean() {
				return instance.newChildInstance();
			}
	
		});
		System.out.println(instance.mChilds);
	}
}
