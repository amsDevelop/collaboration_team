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
 * �����Ʋ�ѯ
 * @author liuzhaodong
 *
 */
public class TestCustomQuery extends TestQueryGuDing {

	/**
	 * 1.���
	 */
	public void testCustomBasin() {
		String key = "cjtx=2";
		String url = Constant.baseURL +
				"peprisapi/basinSearch.html?"+key
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
	/**
	 * 2)	�����
	 */
	public void testCustomOilTian() {
		String key = "mblx=1";
		String url = Constant.baseURL +
				"peprisapi/oilGasSearch.html?"+key ;
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
	/**
	 * 3)	����أ�
	 */
	public void testCustomOilCang() {
		String key = "mblx=2";
		String url = Constant.baseURL +
				"peprisapi/oilGasSearch.html?"+key ;
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
