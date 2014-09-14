package com.sinopec.task;

import java.util.Timer;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.esri.android.map.GraphicsLayer;
import com.esri.core.geometry.Point;
import com.esri.core.map.FeatureSet;
import com.esri.core.map.Graphic;
import com.esri.core.tasks.ags.query.Query;
import com.esri.core.tasks.ags.query.QueryTask;
import com.sinopec.activity.R;
import com.sinopec.application.SinoApplication;
import com.sinopec.common.CommonData;

public class SearchQueryTask extends AsyncTask<Query , Void, FeatureSet> {

	QueryTask m_qTask;
	private String tag = "SearchQueryTask";		
	private Context mContext;
	private ProgressDialog mProgressDialog;
	private String mServicesUrl;
	private Button mTitle;
	private OnQueryFinishListener finishListener;
	private ImageView mImageView;
	private Animation mAnimation;
	private String OperateType = CommonData.TypeOperateLongPress;
	private GraphicsLayer mDrawLayer4HighLight;
	Point mAnchor;
	public SearchQueryTask(Context context, Point anchorPoint, String url,
		Button title, ImageView imageAnim, Animation animation, String operateType, GraphicsLayer drawLayer) {
		this.mContext = context;
		this.mAnchor = anchorPoint;
		this.mServicesUrl = url;
		this.mImageView = imageAnim;
		this.mAnimation = animation;
		this.mTitle = title;
		this.OperateType = operateType;
		this.mDrawLayer4HighLight = drawLayer;
		mProgressDialog = new ProgressDialog(mContext);
		mProgressDialog.setTitle(context.getString(R.string.search_loading));
		mProgressDialog.setCancelable(false);
	}


	public SearchQueryTask(Context context, String url, String operateType) {
		this.mContext = context;
		this.mServicesUrl = url;
		this.OperateType = operateType;
//		mProgressDialog = new ProgressDialog(mContext);
		mProgressDialog.setTitle(context.getString(R.string.search_loading));
		mProgressDialog.setCancelable(false);
	}
	
	public SearchQueryTask(Context context, String url, String operateType,ProgressDialog progressDialog) {
		this.mContext = context;
		this.mServicesUrl = url;
		this.OperateType = operateType;
		this.mProgressDialog = progressDialog;
		mProgressDialog.setTitle(context.getString(R.string.search_loading));
		mProgressDialog.setCancelable(false);
	}
	
	public interface OnQueryFinishListener {

		void onFinish(FeatureSet results);

	}
	
	public void setQueryFinishListener(OnQueryFinishListener finishListener) {
		this.finishListener = finishListener;
	}
	
	@Override
	protected FeatureSet doInBackground(Query... params) {
		FeatureSet result = null;
		if (params != null && params.length > 0) {
			Query mParams = params[0];
			//Log.d(tag, "------doInBackground  --000000000000 ");
			try {
				Log.d(tag, "--before--SearchQueryTask--doInBackground -------------");
				result = m_qTask.execute(mParams);
				Log.d(tag, "--after--SearchQueryTask--doInBackground -------------");
			} catch (Exception e) {
				Log.e(tag, "------doInBackground  err: "+e.toString());
				e.printStackTrace();
			}

		}
		return result;
	}
	
	protected void onPreExecute() {
		mProgressDialog.show();
		Log.d(tag, "-SearchQueryTask  -url "+mServicesUrl);
		m_qTask = new QueryTask(mServicesUrl);
	}
	
	protected void onPostExecute(FeatureSet results) {
		Log.d(tag, "---onPostExecute- -------------");
//		mProgressDialog.dismiss();
		SinoApplication.mFeatureSet4Compared = results;
		if (results == null) {
			mProgressDialog.dismiss();
			Toast.makeText(mContext, mContext.getString(R.string.search_no_result), Toast.LENGTH_SHORT).show();
			return;
		}else{
			Graphic[] graphics = results.getGraphics();
//			if(graphics.length == 1){
				//只有一个结果就直接跳转地图
//				mAdapter.notifyDataSetChanged();
//				mSearchViewGroup.setVisibility(View.VISIBLE);
//				mCallBack.setData(results.get(0));
//			}else 
			if(graphics == null){
				Toast.makeText(mContext, mContext.getString(R.string.search_no_result), Toast.LENGTH_SHORT).show();
				mProgressDialog.dismiss();
			}else{
				if(graphics.length == 0){
					Toast.makeText(mContext, mContext.getString(R.string.search_no_result), Toast.LENGTH_SHORT).show();
					mProgressDialog.dismiss();
				}else{
					finishListener.onFinish(results);
				}
				
			}
		}
	}
}
