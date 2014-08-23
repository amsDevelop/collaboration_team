package com.sinopec.task;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.esri.android.map.Callout;
import com.esri.android.map.GraphicsLayer;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.tasks.ags.identify.IdentifyParameters;
import com.esri.core.tasks.ags.identify.IdentifyResult;
import com.esri.core.tasks.ags.identify.IdentifyTask;
import com.sinopec.activity.R;
import com.sinopec.application.SinoApplication;
import com.sinopec.common.CommonData;

/**
 * 根据点 查询的task： SearchIdentifyTask
 */
public class SearchIdentifyTask extends
		AsyncTask<IdentifyParameters, Void, IdentifyResult[]> {
			String tag = "map";
	IdentifyTask mIdentifyTask;
	Point mAnchor;
	private Context mContext;
	private ProgressDialog mProgressDialog;
	private String mServicesUrl;
	private Button mTitle;
	private OnFinishListener finishListener;
	private String OperateType = CommonData.TypeOperateLongPress;
	private GraphicsLayer mDrawLayer4HighLight;
	private Callout mCallout;
	public SearchIdentifyTask(Context context, Point anchorPoint, String url,
		Button title, String operateType, GraphicsLayer drawLayer, Callout callout) {
		this.mContext = context;
		this.mAnchor = anchorPoint;
		this.mServicesUrl = url;
		this.mTitle = title;
		this.OperateType = operateType;
		this.mCallout = callout;
		this.mDrawLayer4HighLight = drawLayer;
		mProgressDialog = new ProgressDialog(mContext);
		mProgressDialog.setTitle(context.getString(R.string.search_loading));
		mProgressDialog.setCancelable(false);
	}
	
	public SearchIdentifyTask(Context context, String url, String operateType) {
		this.mContext = context;
		this.mServicesUrl = url;
		this.OperateType = operateType;
		mProgressDialog = new ProgressDialog(mContext);
		mProgressDialog.setTitle(context.getString(R.string.search_loading));
		mProgressDialog.setCancelable(false);
	}

	public interface OnFinishListener {

		void onFinish(ArrayList<IdentifyResult> resultList);

	}

	public void setFinishListener(OnFinishListener finishListener) {
		this.finishListener = finishListener;
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
				Log.e(tag, "doInBackground......................" + e.toString());
				mProgressDialog.dismiss();
				e.printStackTrace();
			}

		}
		return mResult;
	}

	@Override
	protected void onPostExecute(IdentifyResult[] results) {
		mProgressDialog.dismiss();
		if (results == null) {
			Toast.makeText(mContext, mContext.getString(R.string.search_no_result), Toast.LENGTH_SHORT).show();
			return;
		}
		

		if(results.length > 0){
			if(CommonData.TypeOperateLongPress.equals(OperateType)){
				Log.d("searchtask", "SearchIdentifyTask  onPostExecute......长按查询结果个数................" + results.length);
				IdentifyResult result = filterLongPressResults(results);
				if(result == null){
					Toast.makeText(mContext, mContext.getString(R.string.search_no_result), Toast.LENGTH_SHORT).show();
				}else{
					if (!mCallout.isShowing()){
						mCallout.show();
					}
					
					SinoApplication.identifyResult = result;
					String name = SinoApplication.getIdentifyResultName(result, result.getLayerName());
					mTitle.setText(name);
					
					//绘制高亮区域
					if(mDrawLayer4HighLight != null){
						Geometry geometry = result.getGeometry();
						SimpleFillSymbol resultSymbol = new SimpleFillSymbol(Color.YELLOW);
						Graphic resultLocation = new Graphic(geometry, resultSymbol);
						mDrawLayer4HighLight.addGraphic(resultLocation);
					}
				}
			}else if(CommonData.TypeOperateFrameChoos.equals(OperateType)){
				if(finishListener != null){
					ArrayList<IdentifyResult> resultList = new ArrayList<IdentifyResult>();
					for (int index = 0; index < results.length; index++) {
						resultList.add(results[index]);
					}
					finishListener.onFinish(resultList);
				}
			}else if(CommonData.TypeOperateMulti.equals(OperateType)){
				if(finishListener != null){
					ArrayList<IdentifyResult> resultList = new ArrayList<IdentifyResult>();
					for (int index = 0; index < results.length; index++) {
						resultList.add(results[index]);
					}
					finishListener.onFinish(resultList);
				}
			}
		}else{
			Toast.makeText(mContext, mContext.getString(R.string.search_no_result), Toast.LENGTH_SHORT).show();
//			if(mCallout != null){
//				if (mCallout.isShowing()) {
//					mCallout.hide();
//				}
//			}
			return;
		}
		


		// String message = "查询结果: \n";
		for (int index = 0; index < results.length; index++) {
			IdentifyResult result = results[index];
			Log.v("mandy",
					"-结果 len:　" + results.length + "  " + result.getValue()
							+ "  Name: " + result.getLayerName() + " id: "
							+ result.getLayerId()+"  getDisplayFieldName: "+result.getDisplayFieldName());
		}
	}

	@Override
	protected void onPreExecute() {
		mIdentifyTask = new IdentifyTask(mServicesUrl);
		Log.d("searchtask", "onPostExecute......SearchIdentifyTask........当前图层的url: " + mServicesUrl);
//				"http://202.204.193.201:6080/arcgis/rest/services/marine_oil/MapServer");
//				"http://202.204.193.201:6080/arcgis/rest/services/basin/MapServer");
		mProgressDialog.show();
	}
	
	private IdentifyResult filterLongPressResults(IdentifyResult[] results) {
		IdentifyResult result = null;
		for (int i = 0; i < results.length; i++) {
			IdentifyResult temp = results[i];
			if(temp != null){
				if(temp.getLayerName().equals(SinoApplication.mLayerName)){
					result = temp;
					break;
				}
			}
		}
		return result;
	}

}