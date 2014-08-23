package com.sinopec.data.json;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.test.AndroidTestCase;

import com.lenovo.nova.util.slog;
import com.lenovo.nova.util.network.NetworkManager;
import com.lenovo.nova.util.parse.Bean;
import com.lenovo.nova.util.parse.JsonToBeanParser;
import com.lenovo.nova.util.parse.JsonToBeanParser.OnJSONFillBeanHelper;
import com.sinopec.data.json.basin.BasinDistributionID;
import com.sinopec.data.json.standardquery.BasinBelonToRoot;
import com.sinopec.data.json.standardquery.DistributeCengGai;
import com.sinopec.data.json.standardquery.DistributeChuJi;
import com.sinopec.data.json.standardquery.DistributeJingRockYuanYan;
import com.sinopec.data.json.standardquery.DistributeRate;
import com.sinopec.data.json.standardquery.DistributeRateResource;

/**
 * 四、固定统计
 * 1、全球含油气盆地碳酸盐岩层系油气储量及资源量分布
 * @author liuzhaodong
 *
 */
public class TestGuDingStatistics extends TestQueryGuDing {

	/**
	 * 1、全球含油气盆地碳酸盐岩层系油气储量及资源量分布
	 *  lrr 储量及资源量级别   
		lrr=1 表示资源总量    
		lrr=2 表示探明储量     
		lrr=3 表示待发现资源量

		yqlx 油气类型 如124为石油天然气
		tsyy 碳酸盐岩编码：1095216660480
		hx  海相编码：35183298347008

	 */
	public void testStatistics() {
		String lrr = "1";
		String yqlx = "124";
		String tsyy = "1095216660480";
		String hx = "35183298347008";
		String url = Constant.baseURL +
				"peprisapi/oilGasDFR.html?lrr="+ lrr +
				"&yqlx=" + yqlx +
				"&tsyy=" + tsyy +
				"&hx=" + hx
				;
		slog.p("url " + url);
		JSONArray jsonArray = getJsonArray(url);
		
		final BasinDistributionID instance = new BasinDistributionID();
		JsonToBeanParser.getInstance().fillBeanWithJson(instance.mChilds, jsonArray, new OnJSONFillBeanHelper() {
			@Override
			public Bean getNewBean() {
				return instance.newChildInstance();
			}
	
		});
		System.out.println(instance.mChilds);
	}
}
