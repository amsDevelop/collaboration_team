package com.sinopec.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import android.R.attr;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.esri.core.geometry.Geometry;
import com.esri.core.map.Graphic;
import com.esri.core.tasks.ags.find.FindParameters;
import com.esri.core.tasks.ags.find.FindResult;
import com.esri.core.tasks.ags.find.FindTask;
import com.esri.core.tasks.ags.identify.IdentifyParameters;
import com.esri.core.tasks.ags.identify.IdentifyResult;
import com.esri.core.tasks.ags.identify.IdentifyTask;
import com.esri.core.tasks.ags.query.QueryTask;
import com.sinopec.view.MenuButtonNoIcon;

public class SearchActivity extends Activity implements OnClickListener {
	private ListView mListView;
	private Context mContext;
	private MenuButtonNoIcon mConfirm;
	private ViewGroup mViewGroup;
	private EditText mEditText;

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
		mEditText = (EditText) findViewById(R.id.edittext_search);
		mProgressDialog = new ProgressDialog(mContext);
		mProgressDialog.setTitle(getString(R.string.search_loading));
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				HashMap<String, Object> map = (HashMap<String, Object>) arg0.getAdapter().getItem(position);
				FindResult result = (FindResult) map.get("FindResult");
			    Map<String, Object> attrMap = result.getAttributes();
			    Set<Entry<String ,Object>> ents= attrMap.entrySet();  
			    String message = "";
			    String x = (String) attrMap.get("CENX");
			    String y = (String) attrMap.get("CENY");
			    Log.d("map", " X："+x+"  Y：  "+y);
//			    for(Entry<String ,Object> ent:ents){  
////			       message += ent.getKey()+" : "+ent.getValue()+"\n";  
////			       Log.d("map", " 名字："+result.getValue()+"  详情：  "+message);
//			    }
				
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

		public MyAdapter(Context context,
				ArrayList<HashMap<String, Object>> list) {
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
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.search_listview_item, null);

				holder.mName = (TextView) convertView
						.findViewById(R.id.menu_item_name);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			HashMap<String, Object> map = mList.get(position);
			FindResult result = (FindResult) map.get("FindResult");
			Map<String, Object> attributes = result.getAttributes();
			String name = result.getValue();
			holder.mName.setText(name);
			return convertView;
		}

		private class ViewHolder {
			public TextView mName;
			// ImageView icon;
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_search_confirm:
			String key = mEditText.getText().toString();
			MyIdentifyTask mTask = new MyIdentifyTask();
		     mTask.execute(key);
			break;
		case R.id.id_btn_operator_2:
			break;
		}
	}

	/*
	 * Dismiss dialog when geocode task completes
	 */
	public class MyRunnable implements Runnable {
		public void run() {
			// dialog.dismiss();
		}
	}

	private Handler handler = new Handler();

	private class MyIdentifyTask extends
			AsyncTask<String, Void, List<FindResult>> {
		FindTask mFindTask;
		Point mAnchor;
		private String mName;

		@Override
		protected List<FindResult> doInBackground(String... params) {
			List<FindResult> mResult = null;
			if (params != null && params.length > 0) {
				int[] layerIDs = new int[]{7};
				FindParameters mParams = new FindParameters(params[0], layerIDs);
				
				try {
					mResult = mFindTask.execute(mParams);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
			return mResult;
		}

		@Override
		protected void onPostExecute(List<FindResult> results) {
			mProgressDialog.dismiss();
			if (results == null) {
				return;
			}
			
			updateData(results);
				
				//TODO:
//				gLayer.removeAll();
//				Geometry geom = results[index].getGeometry();
//				Graphic pGraphic = new Graphic(geom, selectSym);
//				gLayer.addGraphic(pGraphic);
				//TODO:
		}

		@Override
		protected void onPreExecute() {
			mFindTask = new FindTask(
					"http://10.225.14.201:6080/arcgis/rest/services/marine_oil/MapServer");
			mProgressDialog.show();
		}
	}
	
	private ProgressDialog mProgressDialog;
	private void updateData(List<FindResult> results) {
		for (int i = 0; i < results.size(); i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("FindResult", results.get(i));
			mList.add(map);
		}
		mAdapter.notifyDataSetChanged();
		mViewGroup.setVisibility(View.VISIBLE);
	}

}
