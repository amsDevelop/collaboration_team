package com.sinopec.common;

import java.util.ArrayList;

import com.esri.core.tasks.ags.identify.IdentifyResult;

public interface InterfaceDataCallBack {
	public void setData(Object data);
	//范围查询返回结果方法
	public void setSearchData(ArrayList<IdentifyResult> list);
	
	//范围查询返回结果方法，跳转主页面
	public void setData4Frame(Object data);
}
