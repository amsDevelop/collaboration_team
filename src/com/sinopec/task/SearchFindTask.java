package com.sinopec.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
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
import com.sinopec.application.SinoApplication;
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
	private boolean mCancelKeySearch = false;
	public SearchFindTask(InterfaceDataCallBack callBack, Context context, ListView listView,ArrayList<HashMap<String, Object>> list, ViewGroup viewGroup, SearchAdapter adapter, String servicesUrl){
		this.mContext = context;
		this.mCallBack = callBack;
		this.mAdapter = adapter;
		this.mList = list;
		this.mSearchViewGroup = viewGroup;
		mProgressDialog = new ProgressDialog(mContext);
		mProgressDialog.setTitle(context.getString(R.string.search_loading));
		mProgressDialog.setCancelable(true);
		mProgressDialog.setOnCancelListener(new OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface arg0) {
				Toast.makeText(mContext, mContext.getString(R.string.user_cancel_search), Toast.LENGTH_SHORT).show();
				Log.d("searchtask", "用户取消查询..........SearchFindTask....");
				mCancelKeySearch = true;
			}
		});
//		mProgressDialog.setCancelable(false);
		this.mServicesUrl = servicesUrl;
	}
	
	@Override
	protected List<FindResult> doInBackground(String... params) {
		List<FindResult> mResult = null;
		if (params != null && params.length > 0) {
			int[] layerIDs = new int[] { 0,1,2,3,4,5,6,7,8,9,10 };
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
		if(mCancelKeySearch){
			Log.d("searchtask", "onPostExecute.........返回数据  但是被取消.....");
			mCancelKeySearch = false;
			return;
		}
		
		mProgressDialog.dismiss();
		if (results == null) {
			mCallBack.setData(null);
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
			mCallBack.setData(null);
		}else{
			mCallBack.setData(null);
			updateData(results);
		}
	}

	@Override
	protected void onPreExecute() {
		mFindTask = new FindTask(mServicesUrl);
		Log.d("searchtask", "onPostExecute..........SearchFindTask.....url: " + mServicesUrl);
		mCancelKeySearch = false;
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
