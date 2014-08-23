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
import com.sinopec.data.json.basin.BasinBaseValueRoot;
import com.sinopec.data.json.standardquery.BasinBelonToRoot;
import com.sinopec.data.json.standardquery.DistributeCengGai;
import com.sinopec.data.json.standardquery.DistributeChuJi;
import com.sinopec.data.json.standardquery.DistributeJingRockYuanYan;
import com.sinopec.data.json.standardquery.DistributeRate;
import com.sinopec.data.json.standardquery.DistributeRateResource;

/**
 * ??????
 * @author liuzhaodong
 *
 */
public class TestQueryGuDing extends AndroidTestCase {
	String defaultID = "201102001130";

	protected JSONArray getJsonArray(String url) {
		String str = getJsonStr(url);
		JSONArray array = null;
		try {
			array = new JSONArray(str);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return array;
	}

	protected JSONObject getJson(String URL) {
		String jsonStr = null;
		jsonStr = getJsonStr(URL);
		JSONObject jsonObj = null;
		try {
			jsonObj = new JSONObject(jsonStr);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonObj;
	}

	protected String getJsonStr(String URL) {
		NetworkManager manager = new NetworkManager(mContext);
		HttpResponse response = manager.getResponse(manager.getConnType(), false, URL, null, null);
		String jsonStr = null;
		try {
			jsonStr = EntityUtils.toString(response.getEntity());
			slog.p("jsonStr " + jsonStr);

		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jsonStr;
	}

//	protected BasinDistribution getBasinDisributionList() {
//		String URl = "http://10.225.14.204:18880/query1.ashx";
//		NetworkManager manager = new NetworkManager(mContext);
//		HttpResponse response = manager.getResponse(manager.getConnType(), false, URl, null, null);
//		try {
//			String jsonStr = EntityUtils.toString(response.getEntity());
//			slog.p("jsonStr " + jsonStr);
//
//			JSONObject jsonObj = new JSONObject(jsonStr);
//			BasinDistributionRoot bean = new BasinDistributionRoot();
//			JsonToBeanParser.getInstance().fillBeanWithJson(bean, jsonObj);
//
//			return bean;
//		} catch (ParseException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		return null;
//	}

	/**
	 * ???????
	 */
	public void testBasinValue() {
		String id = "201102001130";
		String URL = Constant.basinURL + id;
		slog.p("URL is " + URL);
		JSONObject jsonObj = null;
		jsonObj = getJson(URL);
		try {
			BasinBaseValueRoot root = new BasinBaseValueRoot();
	
			JsonToBeanParser.getInstance().fillBeanWithJson(root, jsonObj);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * 1. ??????????????????????
	 */
	public void testDistributeForOil() {
		// ??????????»Ç??????????????
		String chenjitixi = "72057594037927935";
		String url = Constant.distributeOilGas + chenjitixi;
		slog.p("url + " + url);
		String jsonStr = getJsonStr(url);
		BasinBelonToRoot root = new BasinBelonToRoot();
		try {
			JSONArray jsonArray = new JSONArray(jsonStr);
			for (int i = 0; i < jsonArray.length(); i++) {
				try {
					JsonToBeanParser.getInstance().fillBeanWithJson(root.newBasinBelongTo(), jsonArray.getJSONObject(0));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
	
		} catch (JSONException e) {
			e.printStackTrace();
		}
		// JsonToBeanParser.getInstance().fillBeanWithJson(root, jsonObj);
	
		System.out.println(root.mBasinBelongTo);
	}

	/**
	 * 2.????????????????????????????????????????????
	 */
	public void testDistributeRate() {
		// type=72057594037927935&haixiang=72057594037927935&tansuanyanyan=72057594037927935
		String type = "72057594037927935";
		String haixiang = "72057594037927935";
		String tansuanyanyan = "72057594037927935";
		String url = Constant.distributeRate + "type=" + type + "&haixiang=" + haixiang + "&tansuanyanyan=" + tansuanyanyan;
		slog.p("url " + url);
		JSONArray jsonArray = getJsonArray(url);
		final DistributeRate rate = new DistributeRate();
		JsonToBeanParser.getInstance().fillBeanWithJson(rate.mChilds, jsonArray, new OnJSONFillBeanHelper() {
			@Override
			public Bean getNewBean() {
				return rate.newChildInstance();
			}
	
		});
		System.out.println(rate.mChilds);
	}

	/**
	 * 3.?????????????????????????????????????????????????
	 */
	public void testDistributeRateRockResouce() {
		String type = "72057594037927935";
		String tansuanyanyan = "72057594037927935";
		String url = Constant.baseURL + "peprisapi/fixquery3.html?type=" + type + 
				"&tansuanyanyan=" + tansuanyanyan;
		slog.p("url " + url);
		JSONArray jsonArray = getJsonArray(url);
		
		final DistributeRateResource instance = new DistributeRateResource();
		JsonToBeanParser.getInstance().fillBeanWithJson(instance.mChilds, jsonArray, new OnJSONFillBeanHelper() {
			@Override
			public Bean getNewBean() {
				return instance.newChildInstance();
			}
	
		});
		System.out.println(instance.mChilds);
	}
	
	
	
	/**
	 * 4.???????????????????????
	 */
	public void testDistributeRockJingYuan() {
		String cengXi = "72057594037927935"; 
		String url = Constant.baseURL + "peprisapi/fixquery4.html?cengxi=" + cengXi;
		slog.p("url " + url);
		JSONArray jsonArray = getJsonArray(url);
		
		final DistributeJingRockYuanYan instance = new DistributeJingRockYuanYan();
		JsonToBeanParser.getInstance().fillBeanWithJson(instance.mChilds, jsonArray, new OnJSONFillBeanHelper() {
			@Override
			public Bean getNewBean() {
				return instance.newChildInstance();
			}
	
		});
		System.out.println(instance.mChilds);
	}


	/**
	 * 5.?????????????????????????
	 */
	public void testDistributeChuJi() {
		String chujikongjian = "72057594037927935";
		String chenjixiang = "72057594037927935";
		String tansuanyanyan = "72057594037927935";
		
		String url = Constant.baseURL + 
				"peprisapi/fixquery5.html?chujikongjian=" + chujikongjian 
				+"&chenjixiang="+ chenjixiang +"&tansuanyanyan="+tansuanyanyan
				;
		slog.p("url " + url);
		JSONArray jsonArray = getJsonArray(url);
		
		final DistributeChuJi instance = new DistributeChuJi();
		JsonToBeanParser.getInstance().fillBeanWithJson(instance.mChilds, jsonArray, new OnJSONFillBeanHelper() {
			@Override
			public Bean getNewBean() {
				return instance.newChildInstance();
			}
	
		});
		System.out.println(instance.mChilds);
	}
	
	
	/**
	 * 5.?????????????????????????
	 */
	public void testDistributeCengGai() {
		String gaiceng = "72057594037927935";
		
		String url = Constant.baseURL + 
				"peprisapi/fixquery6.html?gaiceng=" + gaiceng
				;
		slog.p("url " + url);
		JSONArray jsonArray = getJsonArray(url);
		
		final DistributeCengGai instance = new DistributeCengGai();
		JsonToBeanParser.getInstance().fillBeanWithJson(instance.mChilds, jsonArray, new OnJSONFillBeanHelper() {
			@Override
			public Bean getNewBean() {
				return instance.newChildInstance();
			}
	
		});
		System.out.println(instance.mChilds);
	}
	
}
