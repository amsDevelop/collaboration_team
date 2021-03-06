package com.sinopec.data.json;

import android.content.Context;
import com.lenovo.nova.util.network.NetworkHelper;
import com.lenovo.nova.util.parse.Bean;
import com.lenovo.nova.util.parse.JsonToBeanParser;
import com.lenovo.nova.util.parse.JsonToBeanParser.OnJSONFillBeanHelper;
import com.lenovo.nova.util.parse.PreferencesUtil;
import com.lenovo.nova.util.debug.slog;
import com.sinopec.data.json.basin.BasinBaseAttributeRoot;
import com.sinopec.data.json.basin.BasinBaseValueRoot;
import com.sinopec.data.json.standardquery.*;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

import static com.sinopec.activity.ConditionQuery.DEBUG_URL;

/**
 * 固定查询
 * @author liuzhaodong
 *
 */
public class TestQueryGuDing {
	String defaultID = "201102001130";

	public Context mContext;
	public TestQueryGuDing(Context context) {
		// TODO Auto-generated constructor stub
		mContext = context;
	}
	protected JSONArray getJsonArray(String url) {
		PreferencesUtil util = new PreferencesUtil(mContext);
		if(util.getString(DEBUG_URL) != null && util.getString(DEBUG_URL).equals("debug")){
			util.save(DEBUG_URL, url);
			return null;
		}
		String str = getJsonStr(url);
		JSONArray array = null;
		try {
			array = new JSONArray(str);
		} catch (Exception e) {
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

		NetworkHelper.NetworkManager manager = NetworkHelper.newNetworkManager();
        HttpResponse response = null;
        try {
            response = manager.connect(NetworkHelper.Method.GET,URL,null,null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String jsonStr = null;
		try {
			if(response != null){
				jsonStr = EntityUtils.toString(response.getEntity());
			}
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
	 * 测试盆地
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
	 * 测试油气田
	 */
	public void testBasinAttValue() {
		
		
		String id = "201102001130";
		String URL = Constant.basinURL + id;
		slog.p("URL is " + URL);
		URL = "http://10.225.14.204:8080/peprisapi/oilGasFieldAttribute.html?dzdybm=201102001063";
		JSONObject jsonObj = null;
		jsonObj = getJson(URL);
		try {
			BasinBaseAttributeRoot root = new BasinBaseAttributeRoot();
	
			JsonToBeanParser.getInstance().fillBeanWithJson(root, jsonObj);
			
			System.out.println("#################" + root.list.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}



	/**
	 * 1. 全球海相碳酸盐岩含油气盆地分布
	 */
	public void testDistributeForOil() {
		// 沉积体系（如：海相碳酸盐岩编码）
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
	 * 2.全球含油气盆地碳酸盐岩层系油气储量占盆地油气总储量比例分布
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
	 * 3.全球含油气盆地碳酸盐岩层系油气资源量占盆地油气总资源量比例分布
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
	 * 4.分层系全球海相碳酸盐岩烃源岩分布
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
	 * 5.分类型全球海相碳酸盐岩储集层分布
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
	 * 5.分类型全球海相碳酸盐岩储集层分布
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
