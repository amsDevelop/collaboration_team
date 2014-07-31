package com.sinopec.application;

import java.util.ArrayList;

import com.esri.core.tasks.ags.find.FindResult;
import com.esri.core.tasks.ags.geocode.LocatorGeocodeResult;
import com.esri.core.tasks.ags.identify.IdentifyResult;
import com.sinopec.common.OilGasData;

import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;

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
     * 当前图层URL
     */
    public static String currentLayerUrl = genUrl;
	
	/**
	 * 主页面底部菜单分成屏幕宽度的几份
	 */
	public static int menuDivisionNumber = 6;
	public static LocatorGeocodeResult searchResult;
	public static FindResult findResult;
	public static IdentifyResult identifyResult;
	

	public static Context getContext() {
		return mContext;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		this.mContext = this;
	}
	
	/**
	 * 纪录油气专题的选中情况
	 */
	public static ArrayList<OilGasData> mOilGasData = new ArrayList<OilGasData>();
}
