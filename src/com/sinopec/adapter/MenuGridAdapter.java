package com.sinopec.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.sinopec.activity.R;
import com.sinopec.view.MenuChildButton;

public class MenuGridAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<HashMap<String, Object>> mList;

	public MenuGridAdapter(Context context, ArrayList<HashMap<String, Object>> list) {
		mContext = context;
		this.mList = list;
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public HashMap<String, Object> getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public void setVisibleButtonTag(ArrayList<HashMap<String, Object>> list) {
		this.mList = list;
		notifyDataSetChanged();
	}

	@SuppressLint("ResourceAsColor")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = new ViewHolder();
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.menu_child_item, null);

			holder.mMenuChildButton = (MenuChildButton) convertView.findViewById(R.id.btn_menuchild);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		HashMap<String, Object> map = mList.get(position);
		String tag = (String)map.get("tag");
		holder.mMenuChildButton.setTitle((String)map.get("name"));
		holder.mMenuChildButton.setIcon((Integer)map.get("icon"));
		holder.mMenuChildButton.setSplitNumber((Integer)map.get("split"));
		
		HashMap<String, Boolean> showMap = (HashMap<String, Boolean>) map.get("clicktag");
		if(showMap.get(tag)){
			holder.mMenuChildButton.setEnabled(true);
		}else{
			holder.mMenuChildButton.setEnabled(false);
		}
		
		
		if (position == selectItem) {
			convertView.setBackgroundColor(R.color.lightyellow);
		} else {
			convertView.setBackgroundColor(R.color.white);
		}
		return convertView;
	}

	private int selectItem = -1;

	public void setSelectItem(int selectItem) {
		this.selectItem = selectItem;
	}

	
	private class ViewHolder {
//		TextView text;
//		ImageView icon;
		public MenuChildButton mMenuChildButton;
	}
}
