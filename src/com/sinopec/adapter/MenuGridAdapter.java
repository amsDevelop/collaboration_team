package com.sinopec.adapter;

import java.util.ArrayList;
import java.util.HashMap;

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
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

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
		holder.mMenuChildButton.setTitle((String)map.get("name"));
		holder.mMenuChildButton.setIcon((Integer)map.get("icon"));
		holder.mMenuChildButton.setSplitNumber((Integer)map.get("split"));
		
		return convertView;
	}

	private class ViewHolder {
//		TextView text;
//		ImageView icon;
		public MenuChildButton mMenuChildButton;
	}
}