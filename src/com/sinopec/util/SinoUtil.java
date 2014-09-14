package com.sinopec.util;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.JsonReader;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.esri.core.map.FeatureSet;
import com.esri.core.map.Graphic;
import com.esri.core.tasks.ags.identify.IdentifyResult;
import com.sinopec.activity.R;
import com.sinopec.adapter.MenuAdapter;
import com.sinopec.application.SinoApplication;
import com.sinopec.common.CommonData;
import com.sinopec.data.json.Constant;
import com.sinopec.query.AsyncHttpQuery;

public class SinoUtil {
	public static void showWindow(Context context, PopupWindow popupWindow, View view, 
			ListView listview, BaseAdapter baseadapter, OnItemClickListener listener, ArrayList<HashMap<String, Object>> list) {
	

		listview = (ListView) view.findViewById(R.id.menu_listview);

		baseadapter = new MenuAdapter(context, list);
		listview.setAdapter(baseadapter);
		listview.setOnItemClickListener(listener);
		
		// 已经定义好布局，怕破坏掉样式，只需要设置一个空的Drawable即可
		 popupWindow.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.popup_center_bg));

		// 使其聚集
		popupWindow.setFocusable(true);
		// 设置允许在外点击消失
		popupWindow.setOutsideTouchable(true);
		 // 显示在屏幕的最中央  
		popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0); 
	}
	
	
	public static void showWindow4Compared(Context context, PopupWindow popupWindow, ViewGroup view, ArrayList<IdentifyResult> list) {
		HorizontalScrollView scrollView = new HorizontalScrollView(context);
		scrollView.setHorizontalScrollBarEnabled(false);
		
		LinearLayout baseLayout = new LinearLayout(context);
		baseLayout.setOrientation(LinearLayout.HORIZONTAL);
		baseLayout.setGravity(Gravity.CENTER);
		//生成第一列（名字+字段名）
		
		//（名字+字段名）
		ArrayList<String> keyList = new ArrayList<String>();
		keyList.add("地理要素名字");
		//取行名称,取某一个就行
		Set<Entry<String, Object>> ents = list.get(0).getAttributes().entrySet();
		for (Entry<String, Object> ent : ents) {
			keyList.add(ent.getKey());
		}	
		
		//第一列，列明
		LinearLayout layoutFirst = new LinearLayout(context);
		layoutFirst.setOrientation(LinearLayout.VERTICAL);
		for (int i = 0; i < keyList.size(); i++) {
			TextView tv = new TextView(context);
			tv.setText(keyList.get(i));
			layoutFirst.addView(tv);
		}
		
		baseLayout.addView(layoutFirst);
		//第二列开始 是数据
		//取名字
		int number = list.size();
		if(number >= SinoApplication.mComparedNumber){
			number = SinoApplication.mComparedNumber;
		}
		for (int i = 0; i < number; i++) {
			LinearLayout layoutContent = new LinearLayout(context);
			layoutContent.setOrientation(LinearLayout.VERTICAL);
			String name = SinoApplication.getIdentifyResultName(list.get(i), list.get(i).getLayerName());
			
			TextView tvname = new TextView(context);
			tvname.setText(name);
			layoutContent.addView(tvname);
			String value = "";
			
			Set<Entry<String, Object>> tmpEnts = list.get(i).getAttributes().entrySet();
			for (Entry<String, Object> ent : tmpEnts) {
				value = (String) ent.getValue();

				TextView tvvalue = new TextView(context);
				tvvalue.setText(value);
				layoutContent.addView(tvvalue);
			}	
			
			//加到列里面 
			baseLayout.addView(layoutContent);
		}
		
		
		
		
		
		scrollView.addView(baseLayout);
		view.addView(scrollView);
		
		
		//每一行，第一列是属性名称，后面是数据
//		for (Entry<String, Object> ent : ents) {
////				sb = new StringBuilder();
//			sb.append(ent.getKey());
//			sb.append(":  ");
//			sb.append(ent.getValue() + "\n");
//			textView.setText(text);
////				rightList.add(sb.toString());
//		}			
		
		
		
		// 已经定义好布局，怕破坏掉样式，只需要设置一个空的Drawable即可
		popupWindow.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.popup_center_bg));
		
		// 使其聚集
		popupWindow.setFocusable(true);
		// 设置允许在外点击消失
		popupWindow.setOutsideTouchable(true);
		// 显示在屏幕的最中央  
		popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0); 
	}
	
	public static void showWindow4Compared4Table(Context context, PopupWindow popupWindow, ViewGroup view, 
			ArrayList<IdentifyResult> list) {
		asyncHttpQuery = new AsyncHttpQuery(handler, context);
//		ScrollView scrollView = new ScrollView(context);
		ScrollView scrollView = (ScrollView) view.findViewById(R.id.scrollview_content);
		scrollView.setVerticalScrollBarEnabled(false);
		scrollView.setBackgroundColor(Color.WHITE);
		
		SimpleTableView stv1 = new SimpleTableView(context);
		//生成第一列（名字+字段名）
		
		//（名字+字段名）
		ArrayList<String> keyList = new ArrayList<String>();
		keyList.add("地理要素名字");
		//取行名称,取某一个就行
		Set<Entry<String, Object>> ents = list.get(0).getAttributes().entrySet();
		String id = (String) list.get(0).getAttributes().get("OBJ_ID");
		String type = "盆地";
		getJson(id, type);
		for (Entry<String, Object> ent : ents) {
			keyList.add(ent.getKey());
		}	
		
		//第一列字段名字
		for (int i = 0; i < keyList.size(); i++) {
			stv1.AddRow(new String[] {keyList.get(i)});
		}
		
		//第二列开始 是数据
		//取名字
		int number = list.size();
		if(number >= SinoApplication.mComparedNumber){
			number = SinoApplication.mComparedNumber;
		}
		
		//字段值
		ArrayList<ArrayList<String>> allValueList = new ArrayList<ArrayList<String>>();
		for (int i = 0; i < number; i++) {
			ArrayList<String> valueList = new ArrayList<String>();
			//地理要素名字
			String name = SinoApplication.getIdentifyResultName(list.get(i), list.get(i).getLayerName());
			valueList.add(name);
			String value = "";
			
			Set<Entry<String, Object>> tmpEnts = list.get(i).getAttributes().entrySet();
			for (Entry<String, Object> ent : tmpEnts) {
				value = (String) ent.getValue();
				valueList.add(value);
			}	
			
			//第二列以后是值
			stv1.AddRow(new String[] {valueList.get(i)});
			allValueList.add(valueList);
		}
		
//		for (int i = 0; i < keyList.size(); i++) {
//			stv1.AddRow(new String[] {keyList.get(i)});
//		}
		
		for (int j = 0; j < allValueList.size(); j++) {
			ArrayList<String> tmpList = allValueList.get(j);
			for (int i = 0; i < keyList.size(); i++) {
				stv1.AddRow(new String[] {tmpList.get(i)});
			}
		}
		
		LayoutParams lp = new RelativeLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		lp.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
		stv1.setLayoutParams(lp);
		scrollView.addView(stv1);
//		view.addView(scrollView);
		
		// 已经定义好布局，怕破坏掉样式，只需要设置一个空的Drawable即可
		popupWindow.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.popup_center_bg));
		
		// 使其聚集
		popupWindow.setFocusable(true);
		// 设置允许在外点击消失
		popupWindow.setOutsideTouchable(true);
		// 显示在屏幕的最中央  
		popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0); 
	}
	
	public static void showWindow4Compared4FeatureSet(Context context, PopupWindow popupWindow, ViewGroup view, FeatureSet featureSet) {
		Graphic[] graphics = featureSet.getGraphics();
		HorizontalScrollView scrollView = new HorizontalScrollView(context);
		scrollView.setHorizontalScrollBarEnabled(false);
		
		LinearLayout baseLayout = new LinearLayout(context);
		baseLayout.setOrientation(LinearLayout.HORIZONTAL);
		baseLayout.setGravity(Gravity.CENTER);
		//生成第一列（名字+字段名）
		
		//（名字+字段名）
		ArrayList<String> keyList = new ArrayList<String>();
		keyList.add("地理要素名字");
		//取行名称,取某一个就行
		Set<Entry<String, Object>> ents = graphics[0].getAttributes().entrySet();
		for (Entry<String, Object> ent : ents) {
			keyList.add(ent.getKey());
		}	
		
		//第一列，列明
		LinearLayout layoutFirst = new LinearLayout(context);
		layoutFirst.setOrientation(LinearLayout.VERTICAL);
		for (int i = 0; i < keyList.size(); i++) {
			TextView tv = new TextView(context);
			tv.setText(keyList.get(i));
			layoutFirst.addView(tv);
		}
		
		baseLayout.addView(layoutFirst);
		//第二列开始 是数据
		//取名字
		for (int i = 0; i < graphics.length; i++) {
			LinearLayout layoutContent = new LinearLayout(context);
			layoutContent.setOrientation(LinearLayout.VERTICAL);
			String name = (String) graphics[i].getAttributes().get("OBJ_NAME_C");
			
			TextView tvname = new TextView(context);
			tvname.setText(name);
			layoutContent.addView(tvname);
			String value = "";
			
			Set<Entry<String, Object>> tmpEnts = graphics[i].getAttributes().entrySet();
			for (Entry<String, Object> ent : tmpEnts) {
				value = getValue(ent.getValue());
				
				TextView tvvalue = new TextView(context);
				tvvalue.setText(value);
				layoutContent.addView(tvvalue);
			}	
			
			//加到列里面 
			baseLayout.addView(layoutContent);
		}
		
		
		
		
		
		scrollView.addView(baseLayout);
		view.addView(scrollView);
		
		
		//每一行，第一列是属性名称，后面是数据
//		for (Entry<String, Object> ent : ents) {
////				sb = new StringBuilder();
//			sb.append(ent.getKey());
//			sb.append(":  ");
//			sb.append(ent.getValue() + "\n");
//			textView.setText(text);
////				rightList.add(sb.toString());
//		}			
		
		
		
		// 已经定义好布局，怕破坏掉样式，只需要设置一个空的Drawable即可
		popupWindow.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.popup_center_bg));
		
		// 使其聚集
		popupWindow.setFocusable(true);
		// 设置允许在外点击消失
		popupWindow.setOutsideTouchable(true);
		// 显示在屏幕的最中央  
		popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0); 
	}
	
	private static String getValue(Object data) {
		String value = "";
		try {
			double temp = (Double) data;
			java.text.DecimalFormat df = new java.text.DecimalFormat("#");
			value = df.format(temp);
		} catch (Exception e) {
			value = (String) data;
		}
		return value;
	}
	
	private static AsyncHttpQuery asyncHttpQuery;
	private static final int Basin = 1;
	private static final int OilGas = 2;
	private static Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case Basin:
				dealJson4Basin((String) msg.obj);
				break;
			case OilGas:
//				dealJson4OilGas((String) msg.obj);
				break;
			}
		}
	};
	
	private static List<HashMap<String, Object>> mDataList = new ArrayList<HashMap<String, Object>>();

	private static void dealJson4Basin(String result) {
		mDataList.clear();
		Log.d("json", "-------result: " + result);
		JsonParse jsonParse = new JsonParse();
		try {
			mDataList = jsonParse.parseItemsJson(new JsonReader(
					new StringReader(result)));

		} catch (Exception e) {
			Log.e("json", "-dealJson---属性解析 error: " + e.toString());
		}
//		showItemTable(mInitData);
	}
	
	private void showItemTable(String type) {
//		if (CommonData.TypeProperty.equals(dataName)) {
			HashMap<String, Object> result = getItemData(type);
			if (result != null) {
//				addBasePropertyTable(result.entrySet());
			} else {
//				dealNoResult();
			}
//		}
	}
	
	private HashMap<String, Object> getItemData(String key) {
		HashMap<String, Object> result = null;
		try {
			for (HashMap<String, Object> hashMap : mDataList) {

				for (Entry<String, Object> hashMaps : hashMap.entrySet()) {
					// Log.d("json",
					// "0000 getItemData parent key: "+hashMaps.getKey()+" value: "
					// + hashMaps.getValue());
					Log.d("json",
							"0000 getItemData parent key: " + hashMaps.getKey()
									+ " 传进的：" + key);
					if (hashMaps.getValue() instanceof HashMap) {
						if (hashMaps.getKey().equals(key)) {
							result = (HashMap<String, Object>) hashMaps
									.getValue();
							Log.d("json",
									"11111 getItemData parent key: "
											+ hashMaps.getKey() + " value: "
											+ hashMaps.getValue());
							break;
						}
					}
				}
			}
		} catch (Exception e) {
			Log.e("json", "----属性解析 error: " + e.toString());
		}

		return result;
	}
	
	private static void getJson(String id, String type) {
		// String chenjitixi = "72057594037927935";
		String url = "";
		if (CommonData.TopicBasin.equals(type)) {
			url = Constant.urlAttributeBasin + id;
			asyncHttpQuery.execute(Basin, url);
		} else if (CommonData.TopicOilField.equals(type)
				|| CommonData.TopicGasField.equals(type)) {
			url = Constant.urlAttributeOilGas + id;
			// url = Constant.urlAttributeOilGas + "201102001063";
			asyncHttpQuery.execute(OilGas, url);
		}
	}
}
