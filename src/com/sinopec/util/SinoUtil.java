package com.sinopec.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.Map.Entry;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import com.esri.core.map.FeatureSet;
import com.esri.core.map.Graphic;
import com.esri.core.tasks.ags.identify.IdentifyResult;
import com.sinopec.activity.R;
import com.sinopec.adapter.MenuAdapter;
import com.sinopec.application.SinoApplication;

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
		for (int i = 0; i < list.size(); i++) {
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
}
