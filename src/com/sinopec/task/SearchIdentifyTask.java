package com.sinopec.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;

import com.esri.core.geometry.Point;
import com.esri.core.tasks.ags.geocode.Locator;
import com.esri.core.tasks.ags.identify.IdentifyParameters;
import com.esri.core.tasks.ags.identify.IdentifyResult;
import com.esri.core.tasks.ags.identify.IdentifyTask;
import com.sinopec.activity.R;

/**
 * 根据点 查询的task： SearchIdentifyTask
 */
public class SearchIdentifyTask extends
		AsyncTask<IdentifyParameters, Void, IdentifyResult[]> {
	IdentifyTask mIdentifyTask;
	Point mAnchor;
	private Context mContext;
	private ProgressDialog mProgressDialog;
	private Locator locator;
	private ArrayList<HashMap<String, Object>> mList = new ArrayList<HashMap<String, Object>>();
	private ViewGroup mSearchViewGroup;
	private String mServicesUrl;
	private Button mTitle;
	public SearchIdentifyTask(Context context, Point anchorPoint, String url, Button title){
		this.mContext = context;
		this.mAnchor = anchorPoint;
		this.mServicesUrl = url;
		this.mTitle = title;
		mProgressDialog = new ProgressDialog(mContext);
		mProgressDialog.setTitle(context.getString(R.string.search_loading));
	}


	@Override
	protected IdentifyResult[] doInBackground(IdentifyParameters... params) {
		IdentifyResult[] mResult = null;
		if (params != null && params.length > 0) {
			IdentifyParameters mParams = params[0];
			try {
				mResult = mIdentifyTask.execute(mParams);
				// mResult = mIdentifyTask.execute();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return mResult;
	}

	@Override
	protected void onPostExecute(IdentifyResult[] results) {
		if (results == null) {
			return;
		}
		String message = "查询结果: \n";
		for (int index = 0; index < results.length; index++) {
			message += "选中图层名称: " + results[index].getLayerName() + "\n";
			message += "属性字段与值: " + "\n";
			Map<String, Object> attrMap = results[index].getAttributes();
			Set<Entry<String, Object>> ents = attrMap.entrySet();
			for (Entry<String, Object> ent : ents) {
				message += ent.getKey() + " : " + ent.getValue() + "\n";
			}
			Log.d("map", "-msg :　"+ message);
		}
//		mTitle.setText(results[0].getValue().toString());
//		Toast toast = Toast.makeText(mContext, message,
//				Toast.LENGTH_LONG);
//		toast.show();
	}

	@Override
	protected void onPreExecute() {
		mIdentifyTask = new IdentifyTask("http://202.204.193.201:6080/arcgis/rest/services/marine_oil/MapServer");
	}
}
