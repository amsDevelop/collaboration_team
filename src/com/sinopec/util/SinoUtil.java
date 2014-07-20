package com.sinopec.util;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.sinopec.activity.R;
import com.sinopec.adapter.MenuAdapter;

public class SinoUtil {
	public static void showWindow(Context context, PopupWindow popupWindow, View view, 
			ListView listview, BaseAdapter baseadapter, OnItemClickListener listener, ArrayList<HashMap<String, Object>> list) {
		// if (popupWindow == null) {
		LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		view = layoutInflater.inflate(R.layout.view_menu_popwindow, null);
		// 创建一个PopuWidow对象
		popupWindow = new PopupWindow(view, 400, 300);
		// }

		listview = (ListView) view.findViewById(R.id.menu_listview);

		baseadapter = new MenuAdapter(context, list);
		listview.setAdapter(baseadapter);
		listview.setOnItemClickListener(listener);
		
		// 已经定义好布局，怕破坏掉样式，只需要设置一个空的Drawable即可
		 popupWindow.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.popup_bg));

		// 使其聚集
		popupWindow.setFocusable(true);
		// 设置允许在外点击消失
		popupWindow.setOutsideTouchable(true);

	}
}
