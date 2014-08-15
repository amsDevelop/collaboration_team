package com.sinopec.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Point;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.esri.core.tasks.ags.find.FindParameters;
import com.esri.core.tasks.ags.find.FindResult;
import com.esri.core.tasks.ags.find.FindTask;
import com.esri.core.tasks.ags.geocode.Locator;
import com.sinopec.activity.R;
import com.sinopec.adapter.SearchAdapter;
import com.sinopec.common.InterfaceDataCallBack;

public class SearchFindTask extends AsyncTask<String, Void, List<FindResult>> {
	private FindTask mFindTask;
	private Context mContext;
	private ProgressDialog mProgressDialog;
	private Locator locator;
	private InterfaceDataCallBack mCallBack;
	private ListView mListView;
	private ArrayList<HashMap<String, Object>> mList = new ArrayList<HashMap<String, Object>>();
	private SearchAdapter mAdapter;
	private ViewGroup mSearchViewGroup;
	private String mServicesUrl;
	public SearchFindTask(InterfaceDataCallBack callBack, Context context, ListView listView,ArrayList<HashMap<String, Object>> list, ViewGroup viewGroup, SearchAdapter adapter, String servicesUrl){
		this.mContext = context;
		this.mCallBack = callBack;
		this.mAdapter = adapter;
		this.mList = list;
		this.mSearchViewGroup = viewGroup;
		mProgressDialog = new ProgressDialog(mContext);
		mProgressDialog.setTitle(context.getString(R.string.search_loading));
		this.mServicesUrl = servicesUrl;
	}
	
	public SearchFindTask(Context context, ListView listView,ArrayList<HashMap<String, Object>> list, ViewGroup viewGroup, SearchAdapter adapter, String servicesUrl){
		this.mContext = context;
		this.mAdapter = adapter;
		this.mList = list;
		this.mSearchViewGroup = viewGroup;
		mProgressDialog = new ProgressDialog(mContext);
		mProgressDialog.setTitle(context.getString(R.string.search_loading));
		this.mServicesUrl = servicesUrl;
	}
	
	@Override
	protected List<FindResult> doInBackground(String... params) {
		List<FindResult> mResult = null;
		if (params != null && params.length > 0) {
			int[] layerIDs = new int[] { 0,1,2,3,4,5,6,7 };
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
			Toast.makeText(mContext, mContext.getString(R.string.search_no_result), Toast.LENGTH_SHORT).show();
			return;
		}

		if(results.size() == 1){
			//只有一个结果就直接跳转地图
			mAdapter.notifyDataSetChanged();
			mSearchViewGroup.setVisibility(View.VISIBLE);
			mCallBack.setData(results.get(0));
			
		}else if(results.size() == 0){
			Toast.makeText(mContext, mContext.getString(R.string.search_no_result), Toast.LENGTH_SHORT).show();
		}else{
			updateData(results);
		}
	}

	@Override
	protected void onPreExecute() {
		mFindTask = new FindTask(mServicesUrl);
		Log.d("search", "onPostExecute..........模糊擦鞋.....url: " + mServicesUrl);
//		mFindTask = new FindTask(
//				"http://10.225.14.201:6080/arcgis/rest/services/marine_oil/MapServer");
//				"http://202.204.193.201:6080/arcgis/rest/services/marine_geo/MapServer");
		mProgressDialog.show();
	}
	
	private void updateData(List<FindResult> results) {
		for (int i = 0; i < results.size(); i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("FindResult", results.get(i));
			mList.add(map);
		}
		mAdapter.notifyDataSetChanged();
		mSearchViewGroup.setVisibility(View.VISIBLE);
	}
}
