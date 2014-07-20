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
	
	public static void setCountChildrenMenuData(ArrayList<HashMap<String, Object>> list, Boolean[] clickTag, int splitNumber) {
		String[] name4count = new String[] { "数.面积.储", "数密.面密", "储量丰度",
				"储量分布", "油气田数", "油气田面积" };
		// String[] name4count = new String[] {
		// "范围内油气田的个数、面积、储量(油、气、...)", "范围内油气田的个数密度、面积密度",
		// "范围内油气田的储量丰度(吨油当量/平方公里)", "石油、天然气及凝析油储量在各油气田的分布",
		// "不同沉积体系油气田个数", "不同沉积体系油气田面积" };
		Integer[] icon4count = { R.drawable.icon_rang_oilgas,
				R.drawable.icon_rang_oilgas, R.drawable.icon_range_volume,
				R.drawable.icon_distribute, R.drawable.icon_diffrent_object_nubmer, R.drawable.icon_diffrent_object_nubmer, };
		
		String[] tag = new String[] { "数.面积.储", "数密.面密", "储量丰度",
				"储量分布", "油气田数", "油气田面积" };
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
}
