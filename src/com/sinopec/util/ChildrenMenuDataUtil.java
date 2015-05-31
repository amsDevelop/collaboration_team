package com.sinopec.util;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;

import com.sinopec.activity.R;

/**
 * 主页面主菜单数据
 */
public class ChildrenMenuDataUtil {
	private Context mContext;
	
	public ChildrenMenuDataUtil (Context mContext) {
		this.mContext = mContext;
	}
	
	public static void setToolChildrenMenuData(ArrayList<HashMap<String, Object>> list, Boolean[] clickTag, int splitNumber) {
		String[] name4count = new String[] { "测距", "测面积"};
		Integer[] icon4count = { R.drawable.icon_count_distance,
				R.drawable.icon_count_area};
		String[] tag = new String[] { "toolDistance", "toolArea"};
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
	
	public static void setToolChildrenMenuData(ArrayList<HashMap<String, Object>> list, Boolean[] clickTag, String[] name4count, int splitNumber) {
//		String[] name4count = new String[] { "测距", "测面积"};
		Integer[] icon4count = { R.drawable.icon_count_distance,
				R.drawable.icon_count_area};
		String[] tag = new String[] { "toolDistance", "toolArea"};
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
		String[] name4count = new String[] { "海相碳酸盐岩盆地", "碳酸盐岩储量比例", 
				"碳酸盐岩资源比例", "碳酸盐岩烃源分布",
				"碳酸盐岩储层分布", "分类型盖层分布", "定制查询"};
		Integer[] icon4count = { R.drawable.icon_count_distance, R.drawable.icon_count_area,
				R.drawable.icon_count_area,R.drawable.icon_count_area,R.drawable.icon_count_area,
				R.drawable.icon_count_area,R.drawable.icon_count_area,};
		String[] tag = new String[] {"海相碳酸盐岩盆地", "碳酸盐岩储量比例", 
				"碳酸盐岩资源比例", "碳酸盐岩烃源分布",
				"碳酸盐岩储层分布", "分类型盖层分布", "定制查询"};
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
		String[] name4count = new String[] { "碳酸盐岩储量及资源量分布",
                "分层系碳酸盐岩储量及资源量分布",
                "统计中国" };
		Integer[] icon4count = { R.drawable.icon_rang_oilgas,
                R.drawable.icon_rang_oilgas,
                R.drawable.icon_rang_oilgas
        };
//		String[] tag = new String[] { "全球含油气盆地碳酸盐岩层系油气储量及资源量分布", "分层系全球已发现海相碳酸盐岩层系含油气盆地及其储量分布" };
		String[] tag = new String[] { "CountChildrenMenuOne", "CountChildrenMenuTwo","CountChildrenMenuThree" };
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
//		String[] name4count = new String[] { "登陆", "账户管理", "收藏", "下载",
//		"退出" };
//		Integer[] icon4count = { R.drawable.icon_login,
//				R.drawable.icon_accout_mrg, R.drawable.icon_store,
//				R.drawable.icon_download, R.drawable.icon_logout };
//		String[] tag = new String[] { "mineLogin", "mineManager",
//				"mineCollect", "mineDownload", "mineLogout" };
		String[] name4count = new String[] { "登陆", "账户管理", "收藏", "退出" };
		Integer[] icon4count = { R.drawable.icon_login,
				R.drawable.icon_accout_mrg, R.drawable.icon_store, R.drawable.icon_logout };
		String[] tag = new String[] { "mineLogin", "mineManager",
				"mineCollect", "mineLogout" };

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
	
	public static void setMineNoLoginChildrenMenuData(ArrayList<HashMap<String, Object>> list, Boolean[] clickTag, int splitNumber) {
		String[] name4count = new String[] { "登陆", "退出" };
		Integer[] icon4count = { R.drawable.icon_login, R.drawable.icon_logout };
		String[] tag = new String[] { "mineLogin", "mineLogout" };

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
		String[] name4count = new String[] { "前寒武系", "寒武系", "奥陶系","至留系","泥盆系", "石炭系", "二叠系", "三叠系",
				"侏罗系", "白垩系", "古近系", "新近系"  };
		Integer[] icon4count = { 
				R.drawable.icon_rang_oilgas, R.drawable.icon_range_volume,R.drawable.icon_rang_oilgas,
				R.drawable.icon_distribute, R.drawable.icon_diffrent_object_nubmer, R.drawable.icon_diffrent_object_nubmer,
				R.drawable.icon_distribute, R.drawable.icon_diffrent_object_nubmer, R.drawable.icon_diffrent_object_nubmer,
				 R.drawable.icon_diffrent_object_nubmer, R.drawable.icon_diffrent_object_nubmer,R.drawable.icon_diffrent_object_nubmer};
		
		String[] tag = new String[] { "前寒武系s", "寒武系s", "奥陶系s","至留系s","泥盆系s","石炭系s", "二叠系s","三叠系s",
				"侏罗系s", "白垩系s", "古近系s", "新近系s" };
		
//		String []tag = new String[]{"PC_oil,PC_gas_mmb,PC_ngl,PC_pet","Cam_OIL,cam_gas_mm,Cam_NGL,cam_pet",
//				"O_oil,O_gas_mmbo,O_ngl,O_pet","S_oil,S_gas_mmbo,S_ngl,S_pet","D_oil,D_gas_mmbo,D_ngl,D_pet",
//				"C_oil,C_gas_mmbo,C_ngl,C_pet","P_oil,P_gas_mmbo,P_ngl,P_pet","T_oil,T_gas_mmbo,T_ngl,T_pet","J_oil,J_gas_mmbo,J_ngl,J_pet",
//				"K_oil,K_gas_mmbo,K_ngl,K_pet","Pg_oil,Pg_gas_mmb,Pg _ngl,Pg_pet","Ng_oil,Ng_gas_mmb,Ng _ngl,Ng_pet"};
		
		
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
	
	
	//"碳酸盐岩储量比例" 三级菜单
	public static void setSearchLevel23ChildrenMenuOneData(ArrayList<HashMap<String, Object>> list, Boolean[] clickTag, int splitNumber) {
		String[] name4count = new String[] { "石油", "天然气","石油天然气"  };
		Integer[] icon4count = { R.drawable.icon_rang_oilgas,
				R.drawable.icon_rang_oilgas, R.drawable.icon_range_volume};
		
		String[] tag = new String[] { "石油2", "天然气2","石油天然气2"  };
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
	
	//"碳酸盐岩资源比例"三级菜单
	public static void setSearchLevel33ChildrenMenuOneData(ArrayList<HashMap<String, Object>> list, Boolean[] clickTag, int splitNumber) {
		String[] name4count = new String[] { "石油", "天然气","石油天然气"  };
		Integer[] icon4count = { R.drawable.icon_rang_oilgas,
				R.drawable.icon_rang_oilgas, R.drawable.icon_range_volume};
		
		String[] tag = new String[] { "石油3", "天然气3","石油天然气3"  };
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
		String[] name4count = new String[] {"资源总量", "探明储量","待发现资源量"};
		Integer[] icon4count = {
				R.drawable.icon_rang_oilgas, R.drawable.icon_range_volume,
				R.drawable.icon_distribute };
		
		String[] tag = new String[] {"CR_KWNOIL,CR_KWNGAS,CR_KWNNGL",
				"CR_UND_OIL,CR_UND_GAS,CR_UND_NGL",
				"CR_OIL,CR_GAS,CR_NGL"};
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
	
	/**
	 * 碳酸盐岩烃源分布 子菜单(查询菜单第四项)
	 */
	public void setSearchChildren4MenuData(ArrayList<HashMap<String, Object>> list, Boolean[] clickTag, int splitNumber) {

		String[] name4count = mContext.getResources().getStringArray(R.array.qingyuanfenbu_name);
		
		Integer[] icon4count = {  R.drawable.icon_rang_oilgas, R.drawable.icon_rang_oilgas,R.drawable.icon_rang_oilgas, R.drawable.icon_rang_oilgas, R.drawable.icon_rang_oilgas,
				R.drawable.icon_rang_oilgas, R.drawable.icon_rang_oilgas, R.drawable.icon_rang_oilgas,
				R.drawable.icon_rang_oilgas, R.drawable.icon_rang_oilgas, R.drawable.icon_rang_oilgas,R.drawable.icon_rang_oilgas,};
		String[] tag = new String[] { 
				"'Pc','O,S','O,D,C','O+D','O','Cam+S'",
				"'Pc','O,S','O,D,C','O+D','O','Cam+S'",
				 "'S,D','S','O,S','Cam+S'", 
				"'S,D','O,D,C','O+D','D,C,P','D,C','D+C','D'" , 
				"'O,D,C','D,C,P','D,C','D+C','C,P','C+Tr','C+P','C-K','C,P','C'",
				"'P,K,E','P,K','P+Tr+K+Pg','P','D,C,P','C,P','C+P','C,P'",
				"'Tr+Ng','Tr+Jr','Tr','T,J,K3','P+Tr+K+Pg','C+Tr'" ,
				"'Tr+Jr','T,J,K3','Jr+Pg+Ng','Jr+K','Jr','J,K,E','J+K','J'",
				"'T,J,K3','P,K,E','P,K','P+Tr+K+Pg','K+Pg+Ng','K','Jr+K','J,K,E','J+K','C-K'",
				"'PgNg','Pg+Ng','Pg','P+Tr+K+Pg','K+Pg+Ng','Jr+Pg+Ng'" ,
				"'Tr+Ng',' PgNg','Pg+Ng','Ng','K+Pg+Ng','Jr+Pg+Ng'"};
		
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
	
	/**
	 * 碳酸盐岩烃源分布 子菜单“储存空间类型”(查询菜单第五项)
	 * 包括滩坝型、生物礁型、前斜坡/碎屑型、白垩岩/白垩质陆架型、白云岩化灰泥石灰岩型、裂缝/喀斯特型
	 */
	public static void setSearchChildren5MenuData(ArrayList<HashMap<String, Object>> list, Boolean[] clickTag, int splitNumber) {
		String[] name4count = new String[] { "滩坝型", "生物礁型" , "前斜坡/碎屑型",
				"深海白垩岩/白垩质陆架型", "白云岩化灰泥石灰岩型" , "裂缝/喀斯特型"};
//		"白垩岩/白垩质陆架型", "白云岩化灰泥石灰岩型" , "裂缝/喀斯特型"};
		Integer[] icon4count = { R.drawable.icon_rang_oilgas, R.drawable.icon_rang_oilgas, R.drawable.icon_rang_oilgas,
				R.drawable.icon_rang_oilgas, R.drawable.icon_rang_oilgas, R.drawable.icon_rang_oilgas,};
		String[] tag = new String[] { "滩坝型", "生物礁型" , "前斜坡/碎屑型",
//				"白垩岩/白垩质陆架型", "白云岩化灰泥石灰岩型" , "裂缝/喀斯特型"};
				"深海白垩岩/白垩质陆架型", "白云岩化灰泥石灰岩型" , "裂缝/喀斯特型"};
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
	
	/**
	 * 分类型盖层分布子菜单(查询菜单第六项)
	 */
	public void setSearchChildren6MenuData(ArrayList<HashMap<String, Object>> list, Boolean[] clickTag, int splitNumber) {
		String[] name4count = mContext.getResources().getStringArray(R.array.fenleigaiceng_name);
		Integer[] icon4count = { R.drawable.icon_rang_oilgas, R.drawable.icon_rang_oilgas, R.drawable.icon_rang_oilgas,
				R.drawable.icon_rang_oilgas, R.drawable.icon_rang_oilgas,};
		String[] tag = new String[] {
				"'salt and by mudstones','salt','anhydrite,salt beds,nonporous dolomites,and red','anhydrite or sh','anhydrite,salt','anhydrite'"
				,"'sh salt','salt and by mudstones','anhydrite, salt beds, nonporous dolomites, and red,anhydrite or sh,anhydrite,salt','Sh and anhydrite'"
				,"'sh salt','salt and by mudstones','anhydrite, salt beds, nonporous dolomites, and red,anhydrite or sh,anhydrite,salt','Sh and anhydrite'"
		};
		
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
