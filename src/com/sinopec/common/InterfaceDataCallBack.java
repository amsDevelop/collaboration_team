package com.sinopec.common;

import java.util.ArrayList;

import com.esri.core.map.FeatureSet;
import com.esri.core.tasks.ags.identify.IdentifyResult;

public interface InterfaceDataCallBack {
	/**
	 * 范围查询返回结果方法(queryTask)
	 * @param list
	 */
	public void setData4Query(Object data);
	/**
	 * 长按查询返回结果方法(identifyTask)
	 * @param list
	 */
	public void setData4LongPressed(Object data);
	public void setData(Object data);
	/**
	 * 范围查询返回结果方法(queryTask)
	 * @param list
	 */
	public void setSearchData4Query(FeatureSet results);
	/**
	 * 范围查询返回结果方法
	 * @param list
	 */
	public void setSearchData(ArrayList<IdentifyResult> list);
	
	/**
	 * 范围查询（包括框选，多边形选择）返回结果方法，跳转主页面
	 * @param data
	 */
	public void setData4Frame(Object data);
	
}
