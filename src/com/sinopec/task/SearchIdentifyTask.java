package com.sinopec.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
	private ArrayList<IdentifyResult> resultList;
	private OnFinishListener finishListener;

	public SearchIdentifyTask(Context context, Point anchorPoint, String url,
			Button title) {
		this.mContext = context;
		this.mAnchor = anchorPoint;
		this.mServicesUrl = url;
		this.mTitle = title;
		mProgressDialog = new ProgressDialog(mContext);
		mProgressDialog.setTitle(context.getString(R.string.search_loading));
	}

	public interface OnFinishListener {

		void onFinish(ArrayList<IdentifyResult> resultList);

	}

	public void setFinishListener(OnFinishListener finishListener) {
		this.finishListener = finishListener;
	}

	public SearchIdentifyTask(Context context, String url) {
		this.mContext = context;
		this.mServicesUrl = url;
		mProgressDialog = new ProgressDialog(mContext);
		mProgressDialog.setTitle(context.getString(R.string.search_loading));
	}

	@Override
	protected IdentifyResult[] doInBackground(IdentifyParameters... params) {
		System.out.println("doInBackground......................");

		IdentifyResult[] mResult = null;
		if (params != null && params.length > 0) {
			IdentifyParameters mParams = params[0];
			try {
				mResult = mIdentifyTask.execute(mParams);
				// mResult = mIdentifyTask.execute();
			} catch (Exception e) {
				System.out.println("doInBackground......................"
						+ e.getMessage());
				e.printStackTrace();
			}

		}
		return mResult;
	}

	@Override
	protected void onPostExecute(IdentifyResult[] results) {
		System.out.println("onPostExecute......................" + results);

		if (results == null) {
			return;
		}

		resultList = new ArrayList<IdentifyResult>();
		for (int index = 0; index < results.length; index++) {

			// if(results[index].getAttributes().get(results[index].getDisplayFieldName())!=null)
			resultList.add(results[index]);
		}

		System.out.println("resultList.........size: " + results.length);
		finishListener.onFinish(resultList);

		// String message = "查询结果: \n";
		for (int index = 0; index < results.length; index++) {
			IdentifyResult result = results[index];
			Log.v("mandy",
					"-结果 len:　" + results.length + "  " + result.getValue()
							+ "  Name: " + result.getLayerName() + " id: "
							+ result.getLayerId());
			// message += "选中图层名称: " + results[index].getLayerName() + "\n";
			// message += "属性字段与值: " + "\n";
			// Map<String, Object> attrMap = results[index].getAttributes();
			// Set<Entry<String, Object>> ents = attrMap.entrySet();
			// for (Entry<String, Object> ent : ents) {
			// message += ent.getKey() + " : " + ent.getValue() + "\n";
			// }
			// Log.d("map", "-msg :　"+ message);
		}
		// mTitle.setText(results[0].getValue().toString());
	}

	@Override
	protected void onPreExecute() {
		mIdentifyTask = new IdentifyTask(
				"http://202.204.193.201:6080/arcgis/rest/services/marine_oil/MapServer");
	}

}