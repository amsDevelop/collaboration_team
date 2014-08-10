package com.sinopec.util;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.sinopec.activity.R;
import com.sinopec.adapter.MenuAdapter;

/**
 * 主页面主菜单数据
 */
public class ChildrenMenuDataUtil {
	public static void setToolChildrenMenuData(ArrayList<HashMap<String, Object>> list, Boolean[] clickTag, int splitNumber) {
		String[] name4count = new String[] { "测距", "测面积", "查询" };
		Integer[] icon4count = { R.drawable.icon_count_distance,
				R.drawable.icon_count_area,R.drawable.icon_count_area };
		String[] tag = new String[] { "toolDistance", "toolArea",
				"toolSelect" };
		list.clear();
		for (int i = 0; i < name4count.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("name", name4count[i]);
			map.put("icon", icon4count[i]);
			map.put("tag", tag[i]);
			
			HashMap<String, Boolean> showMap = new HashMap<String, Boolean>();
			showMap.put(tag[i], clickTag[i]);
			map.put("clicktag", showMap);
			map.put("split", splitNumber);
			list.add(map);
		}

	}
	
	public static void setSearchChildrenMenuData(ArrayList<HashMap<String, Object>> list, Boolean[] clickTag, int splitNumber) {
		String[] name4count = new String[] { "全球海相碳酸盐岩含油气盆地分布", "全球含油气盆地碳酸盐岩层系油气储量占盆地油气总储量比例分布", 
				"全球含油气盆地碳酸盐岩层系油气资源量占盆地油气总资源量比例分布", "分层系全球海相碳酸盐岩烃源岩分布",
				"分类型全球海相碳酸盐岩储集层分布", "分类型盖层分布", "定制查询"};
		Integer[] icon4count = { R.drawable.icon_count_distance, R.drawable.icon_count_area,
				R.drawable.icon_count_area,R.drawable.icon_count_area,R.drawable.icon_count_area,
				R.drawable.icon_count_area,R.drawable.icon_count_area,};
		String[] tag = new String[] { "toolDistance", "toolArea", "toolSelect",
				"toolDistance", "toolArea", "toolSelect" ,"toolSelect"};
		list.clear();
		for (int i = 0; i < name4count.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("name", name4count[i]);
			map.put("icon", icon4count[i]);
			map.put("tag", tag[i]);
			
			HashMap<String, Boolean> showMap = new HashMap<String, Boolean>();
			showMap.put(tag[i], clickTag[i]);
			map.put("clicktag", showMap);
			map.put("split", splitNumber);
			list.add(map);
		}
		
	}
	
	public static void setCountChildrenMenuData(ArrayList<HashMap<String, Object>> list, Boolean[] clickTag, int splitNumber) {
		String[] name4count = new String[] { "全球含油气盆地碳酸盐岩层系油气储量及资源量分布", "分层系全球已发现海相碳酸盐岩层系含油气盆地及其储量分布" };
		Integer[] icon4count = { R.drawable.icon_rang_oilgas, R.drawable.icon_rang_oilgas,};
//		String[] tag = new String[] { "全球含油气盆地碳酸盐岩层系油气储量及资源量分布", "分层系全球已发现海相碳酸盐岩层系含油气盆地及其储量分布" };
		String[] tag = new String[] { "CountChildrenMenuOne", "CountChildrenMenuTwo" };
//		String[] name4count = new String[] { "数.面积.储", "数密.面密", "储量丰度",
//				"储量分布", "油气田数", "油气田面积" };
//		Integer[] icon4count = { R.drawable.icon_rang_oilgas,
//				R.drawable.icon_rang_oilgas, R.drawable.icon_range_volume,
//				R.drawable.icon_distribute, R.drawable.icon_diffrent_object_nubmer, R.drawable.icon_diffrent_object_nubmer, };
//		
//		String[] tag = new String[] { "数.面积.储", "数密.面密", "储量丰度",
//				"储量分布", "油气田数", "油气田面积" };
		list.clear();
		for (int i = 0; i < name4count.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("name", name4count[i]);
			map.put("icon", icon4count[i]);
			map.put("tag", tag[i]);
			
			HashMap<String, Boolean> showMap = new HashMap<String, Boolean>();
			showMap.put(tag[i], clickTag[i]);
			map.put("clicktag", showMap);
			map.put("split", splitNumber);
			list.add(map);
		}
		
	}
	
	public static void setCompareChildrenMenuData(ArrayList<HashMap<String, Object>> list, Boolean[] clickTag, int splitNumber) {
		String[] name4count = new String[] { "待定" };
		Integer[] icon4count = { R.drawable.icon_compare_0 };
		String[] tag = new String[] { "待定" };
		list.clear();
		for (int i = 0; i < name4count.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("name", name4count[i]);
			map.put("icon", icon4count[i]);
			map.put("tag", name4count[i]);
			
			HashMap<String, Boolean> showMap = new HashMap<String, Boolean>();
			showMap.put(tag[i], clickTag[i]);
			map.put("clicktag", showMap);
			map.put("split", splitNumber);
			list.add(map);
		}
	}
	
	public static void setMineChildrenMenuData(ArrayList<HashMap<String, Object>> list, Boolean[] clickTag, int splitNumber) {
		String[] name4count = new String[] { "登陆", "账户管理", "收藏", "下载",
		"退出" };
		Integer[] icon4count = { R.drawable.icon_login,
				R.drawable.icon_accout_mrg, R.drawable.icon_store,
				R.drawable.icon_download, R.drawable.icon_logout };
		String[] tag = new String[] { "mineLogin", "mineManager",
				"mineCollect", "mineDownload", "mineLogout" };
		list.clear();
		for (int i = 0; i < name4count.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("name", name4count[i]);
			map.put("icon", icon4count[i]);
			map.put("tag", tag[i]);
			
			HashMap<String, Boolean> showMap = new HashMap<String, Boolean>();
			showMap.put(tag[i], clickTag[i]);
			map.put("clicktag", showMap);
			map.put("split", splitNumber);
			list.add(map);
		}
	}
	
	//层系数据
	public static void setCountLevelTwoChildrenMenuData(ArrayList<HashMap<String, Object>> list, Boolean[] clickTag, int splitNumber) {
		String[] name4count = new String[] { "前寒武系", "寒武系", "至留系","泥盆系", "二叠系", "奥陶系",
				"侏罗系", "白垩系","石灰系", "古近系", "新近系"  };
		Integer[] icon4count = { R.drawable.icon_rang_oilgas,
				R.drawable.icon_rang_oilgas, R.drawable.icon_range_volume,
				R.drawable.icon_distribute, R.drawable.icon_diffrent_object_nubmer, R.drawable.icon_diffrent_object_nubmer, };
		
		String[] tag = new String[] { "前寒武系", "寒武系", "至留系","泥盆系", "二叠系", "奥陶系",
				"侏罗系", "白垩系","石灰系", "古近系", "新近系" };
		list.clear();
		for (int i = 0; i < name4count.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("name", name4count[i]);
			map.put("icon", icon4count[i]);
			map.put("tag", tag[i]);
			
			HashMap<String, Boolean> showMap = new HashMap<String, Boolean>();
			showMap.put(tag[i], clickTag[i]);
			map.put("clicktag", showMap);
			map.put("split", splitNumber);
			list.add(map);
		}
		
	}
	
	
	//全球含油气盆地碳酸盐岩层系油气储量及资源量分布
	public static void setCountLevelTwoChildrenMenuOneData(ArrayList<HashMap<String, Object>> list, Boolean[] clickTag, int splitNumber) {
		String[] name4count = new String[] { "储量及资源量级别", "资源总量", "探明储量","待发现资源量"  };
		Integer[] icon4count = { R.drawable.icon_rang_oilgas,
				R.drawable.icon_rang_oilgas, R.drawable.icon_range_volume,
				R.drawable.icon_distribute };
		
		String[] tag = new String[] { "储量及资源量级别", "资源总量", "探明储量","待发现资源量" };
		list.clear();
		for (int i = 0; i < name4count.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("name", name4count[i]);
			map.put("icon", icon4count[i]);
			map.put("tag", tag[i]);
			
			HashMap<String, Boolean> showMap = new HashMap<String, Boolean>();
			showMap.put(tag[i], clickTag[i]);
			map.put("clicktag", showMap);
			map.put("split", splitNumber);
			list.add(map);
		}
		
	}
}
