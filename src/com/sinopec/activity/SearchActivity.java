package com.sinopec.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.sinopec.view.MenuButtonNoIcon;
import com.sinopec.view.MenuChildButton;

public class SearchActivity extends Activity implements OnClickListener {
	private ListView mListView;
	private Context mContext;
	private MenuButtonNoIcon mConfirm;
	private ViewGroup mViewGroup;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.mContext = this;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_search);
		initView();
		initData();
	}
	
	private ImageButton mBtnBack;
	private void initView() {
		mConfirm = (MenuButtonNoIcon) findViewById(R.id.btn_search_confirm);
		mConfirm.setOnClickListener(this);
		mListView = (ListView) findViewById(R.id.search_listview);
		mViewGroup = (ViewGroup) findViewById(R.id.search_listview_layout);
		mBtnBack = (ImageButton) findViewById(R.id.btn_login_back);
		mBtnBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		
	}
	
	private ArrayList<HashMap<String, Object>> mList = new ArrayList<HashMap<String, Object>>();
	private MyAdapter mAdapter;
	private void initData() {
		mAdapter = new MyAdapter(mContext, mList);
		mListView.setAdapter(mAdapter);
		
	}
	
	class MyAdapter extends BaseAdapter {
		private ArrayList<HashMap<String, Object>> mList;
		private Context mContext;
		public MyAdapter(Context context, ArrayList<HashMap<String, Object>> list ) {
			this.mContext = context;
			this.mList = list;
		}
		
		@Override
		public int getCount() {
			return mList.size();
		}

		@Override
		public HashMap<String, Object> getItem(int index) {
			return mList.get(index);
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = new ViewHolder();
			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(R.layout.search_listview_item, null);

				holder.mName = (TextView) convertView.findViewById(R.id.menu_item_name);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			HashMap<String, Object> map = mList.get(position);
			String name = (String) map.get("name");
			holder.mName.setText(name);
			return convertView;
		}
		
		private class ViewHolder {
			public TextView mName;
//			ImageView icon;
		}
		
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btn_search_confirm:
			for (int i = 0; i < 12; i++) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("name", "名字"+ i);
				mList.add(map);
			}
			mAdapter.notifyDataSetChanged();
			mViewGroup.setVisibility(View.VISIBLE);
			break;
		case R.id.id_btn_operator_2:
			break;
		}
	}
}
