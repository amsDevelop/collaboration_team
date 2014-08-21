package com.sinopec.application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.esri.core.map.FeatureSet;
import com.esri.core.tasks.ags.find.FindResult;
import com.esri.core.tasks.ags.geocode.LocatorGeocodeResult;
import com.esri.core.tasks.ags.identify.IdentifyResult;
import com.sinopec.activity.R;
import com.sinopec.common.OilGasData;

public class SinoApplication extends Application {
	public static Context mContext;
	public static int screenWidth = 0;
	public static int screenHeight = 0;
	public static float density = 0;
	public static int densityDpi = 0;
	public static String LNsatellite = "Satellite";
	public static String LNgeographic = "geographic";
	public static String LNoilGas = "OilGas";
	/**
	 * 图层设置中选择图层的名称
	 */
	public static String layerName = LNoilGas;
	public static final String imageUrl = "http://202.204.193.201:6080/arcgis/rest/services/marine_image/MapServer";
    public static final String genUrl = "http://202.204.193.201:6080/arcgis/rest/services/marine_geo/MapServer";
    /**
     * 搜索和长按 所用的url
     */
    public static final String oilUrl = "http://202.204.193.201:6080/arcgis/rest/services/marine_oil/MapServer";
    /**
     * 当前图层URL(初始化为盆地)
     */
    public static String currentLayerUrl = genUrl;
    /**
     * 给多选使用的  当前图层URL(初始化为盆地)
     */
    public static String currentLayerUrl4Multi = genUrl;
    /**
     * 当前查询图层的名字
     */
    public static String mLayerName = "盆地";
	
	/**
	 * 主页面底部菜单分成屏幕宽度的几份
	 */
	public static int menuDivisionNumber = 6;
	public static LocatorGeocodeResult searchResult;
	public static FindResult findResult;
	public static IdentifyResult identifyResult;
	public static IdentifyResult identifyResult4Frame;

	public static Context getContext() {
		return mContext;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		SinoApplication.mContext = this;
	}
	
	/**
	 * 图层id和查询结果map中，包含中午名称的key 对应关系的map
	 */
	public static HashMap<String, String> mapLayerIDAndKey =  new HashMap<String, String>();

	/**
	 * 纪录油气专题的选中情况
	 */
	public static ArrayList<OilGasData> mOilGasData = new ArrayList<OilGasData>();
	/**
	 * 纪录框选查询结果
	 */
	public static ArrayList<IdentifyResult> mResultList4FrameSearch = new ArrayList<IdentifyResult>();
	/**
	 * 纪录框选查询结果（queryTask查询回来的）
	 */
	public static FeatureSet mFeatureSet4Query = null;
	/**
	 * 纪录多选查询结果
	 */
	public static ArrayList<IdentifyResult> mResultListMulti = new ArrayList<IdentifyResult>();
	public static HashMap<String, Integer> mResultMapMulti = new HashMap<String, Integer>();
	/**
	 * 对比用的集合
	 */
	public static ArrayList<IdentifyResult> mResultList4Compared = new ArrayList<IdentifyResult>();
	/**
	 * 对比用的集合
	 */
	public static FeatureSet mFeatureSet4Compared = null;
	/**
	 * 获得IdentifyResult中地理元素的名字
	 * @param result
	 * @return
	 */
	public static String getIdentifyResultName(IdentifyResult result, String type) {
		Map<String, Object> attributes = result.getAttributes();
		String name = (String) attributes.get(SinoApplication.mapLayerIDAndKey.get(type));
		//气田： {发现日期=0, 圈闭数量=0, 区域=, 可采储量=0.00000000000, OBJ_ID=201102001074, OBJ_NAME=, 状态=, 所在盆地=, OBJ_NAME_C=威远, 经度=, 操作者=, 剩余可采=0.00000000000, 国家=, Shape=Polygon, FID=10308, 纬度=, ESRI_OID=, 油气田类型=}
		if(TextUtils.isEmpty(name)){
			if("气田".equals(type) || "油田".equals(type)){
				name = (String) attributes.get("OBJ_NAME_C");
			}
//			name = result.getValue().toString();
		}
		return name;
	}
	
	public static String getIdentifyResultNameByType(IdentifyResult result, String type) {
		Map<String, Object> attributes = result.getAttributes();
		String name = (String) attributes.get(SinoApplication.mapLayerIDAndKey.get(type));
		if(TextUtils.isEmpty(name)){
			if("气田".equals(type) || "油田".equals(type)){
				name = (String) attributes.get("OBJ_NAME_C");
			}
//			name = result.getValue().toString();
		}
		return name;
	}
	
	public static String getFindResultNameByType(FindResult result, String type) {
		Map<String, Object> attributes = result.getAttributes();
		String name = (String) attributes.get(SinoApplication.mapLayerIDAndKey.get(type));
		
		if(TextUtils.isEmpty(name)){
			if("气田".equals(type) || "油田".equals(type)){
				name = (String) attributes.get("OBJ_NAME_C");
			}
//			name = result.getValue().toString();
		}
		return name;
	}
	
	/**
	 * 获得图层id和查询结果map中，包含中午名称的key 对应关系的map
	 */
	public static void getLayerIDAndKeyMap() {
		mapLayerIDAndKey.put("气田", "OBJ_NAME");
//		mapLayerIDAndKey.put("气田", "OBJ_NAME_C");
//		mapLayerIDAndKey.put("油田", "油气田名称");
		mapLayerIDAndKey.put("油田", "OBJ_NAME");
//		mapLayerIDAndKey.put("油田", "OBJ_NAME");
		mapLayerIDAndKey.put("井", "井位名称");
		mapLayerIDAndKey.put("盆地", "OBJ_NAME_C");
		mapLayerIDAndKey.put("气藏点", "名称");
		mapLayerIDAndKey.put("油藏点", "名称");
	}
	
}
