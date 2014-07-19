package com.sinopec.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sinopec.activity.R;

public class MenuAdapter extends BaseAdapter {
		private Context mContext;
		private ArrayList<HashMap<String, Object>> mList;

		public MenuAdapter(Context context, ArrayList<HashMap<String, Object>> list) {
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
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = new ViewHolder();
			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(R.layout.toolbar_childmenu_item, null);

				holder.mName = (TextView) convertView.findViewById(R.id.menu_item_name);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			HashMap<String, Object> map = mList.get(position);
			String name = (String)map.get("name");
			holder.mName.setText(name);
			return convertView;
		}
		
		private class ViewHolder {
			public TextView mName;
//			ImageView icon;
		}
		
}
