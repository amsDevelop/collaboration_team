package com.sinopec.data.json;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.test.AndroidTestCase;

import com.lenovo.nova.util.slog;
import com.lenovo.nova.util.network.NetworkManager;
import com.lenovo.nova.util.parse.Bean;
import com.lenovo.nova.util.parse.JsonToBeanParser;
import com.lenovo.nova.util.parse.JsonToBeanParser.OnJSONFillBeanHelper;
import com.lenovo.nova.util.parse.PreferencesUtil;
import com.sinopec.data.json.basin.BasinDistributionID;
import com.sinopec.data.json.basin.BasinDistributionID.DistributeChild;
import com.sinopec.data.json.standardquery.BasinBelonToRoot;
import com.sinopec.data.json.standardquery.DistributeCengGai;
import com.sinopec.data.json.standardquery.DistributeChuJi;
import com.sinopec.data.json.standardquery.DistributeJingRockYuanYan;
import com.sinopec.data.json.standardquery.DistributeRate;
import com.sinopec.data.json.standardquery.DistributeRateResource;
import static com.sinopec.activity.ConditionQuery.DEBUG_URL;
/**
 * ．定制查询
 * @author liuzhaodong
 *
 */
public class TestCustomQuery extends TestQueryGuDing {

	public TestCustomQuery(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	/**
	 * 1.盆地
	 * @return 
	 */
	public ArrayList testCustomBasin(String key) {
//		String key = "cjtx=2";
		String url = Constant.baseURL +
				"peprisapi/basinSearch.html?"+key
				;
		slog.p("url " + url);
		JSONArray jsonArray = getJsonArray(url);
		if(jsonArray != null){
			final BasinDistributionID instance = new BasinDistributionID();
			JsonToBeanParser.getInstance().fillBeanWithJson(instance.mChilds, jsonArray, new OnJSONFillBeanHelper() {
				@Override
				public Bean getNewBean() {
					return instance.newChildInstance();
				}
			});
			System.out.println(instance.mChilds);
			return instance.mChilds;
		}
		return null;
	}
	/**
	 * 2)	油气田：
	 * @return 
	 */
	public ArrayList<DistributeChild> testCustomOilTian(String key) {
//		String key = "mblx=1";
		String url = Constant.baseURL +
				"peprisapi/oilGasSearch.html?mblx=1&"+key ;
		slog.p("url " + url);
		JSONArray jsonArray = getJsonArray(url);
		if(jsonArray != null){
			final BasinDistributionID instance = new BasinDistributionID();
			JsonToBeanParser.getInstance().fillBeanWithJson(instance.mChilds, jsonArray, new OnJSONFillBeanHelper() {
				@Override
				public Bean getNewBean() {
					return instance.newChildInstance();
				}
				
			});
			System.out.println(instance.mChilds);
			return instance.mChilds;
		}
		return null;
	}
	/**
	 * 3)	油气藏：
	 * @return 
	 */
	public ArrayList<DistributeChild> testCustomOilCang(String key) {
//		String key = "";
		String url = Constant.baseURL +
				"peprisapi/oilGasSearch.html?mblx=2&"+key ;
		slog.p("url " + url);
		JSONArray jsonArray = getJsonArray(url);
		if(jsonArray != null){
			final BasinDistributionID instance = new BasinDistributionID();
			JsonToBeanParser.getInstance().fillBeanWithJson(instance.mChilds, jsonArray, new OnJSONFillBeanHelper() {
				@Override
				public Bean getNewBean() {
					return instance.newChildInstance();
				}
				
			});
			System.out.println(instance.mChilds);
			return instance.mChilds;
		}
		return null;
	}
	
}
