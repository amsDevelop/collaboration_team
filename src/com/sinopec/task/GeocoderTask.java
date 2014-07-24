package com.sinopec.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.esri.core.geometry.Geometry;
import com.esri.core.tasks.ags.geocode.Locator;
import com.esri.core.tasks.ags.geocode.LocatorFindParameters;
import com.esri.core.tasks.ags.geocode.LocatorGeocodeResult;
import com.sinopec.activity.R;
import com.sinopec.adapter.SearchAdapter;

/**
 * 按关键字查询的task
 */
public class GeocoderTask extends AsyncTask<LocatorFindParameters, Void, List<LocatorGeocodeResult>>{
	private Context mContext;
	private ProgressDialog mProgressDialog;
	private Locator locator;
	private Activity mActivity;
	private ListView mListView;
	private ArrayList<HashMap<String, Object>> mList = new ArrayList<HashMap<String, Object>>();
	private SearchAdapter mAdapter;
	private ViewGroup mSearchViewGroup;
	public GeocoderTask(Context context, Activity activity, ListView mListView, ViewGroup viewGroup, SearchAdapter adapter){
		this.mContext = context;
		this.mActivity = activity;
		this.mAdapter = adapter;
		this.mSearchViewGroup = viewGroup;
		mProgressDialog = new ProgressDialog(mContext);
		mProgressDialog.setTitle(context.getString(R.string.search_loading));
	}
	
	protected void onPostExecute(List<LocatorGeocodeResult> results){
		mProgressDialog.dismiss();
        if(results == null || results.size() == 0){
            // update UI with notice that no results were found
            Toast toast = Toast.makeText(mContext, "No result found.", Toast.LENGTH_LONG);
            toast.show();
        } else {
        	updateDataNew(results);
        }
    }

	@Override
    protected List<LocatorGeocodeResult> doInBackground(LocatorFindParameters... params) {
        // get spatial reference from map
        List<LocatorGeocodeResult> results = null;
        // set the geocode service
        locator = new Locator(mContext.getResources().getString(R.string.geocode_url));
        try {
            // pass address to find method to return point representing address
            results = locator.find(params[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // return the resulting point(s)
        return results;
    }
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		mProgressDialog.show();
	}
	
	private void updateDataNew(List<LocatorGeocodeResult> results) {
		for (int i = 0; i < results.size(); i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			 Geometry resultLocGeom = results.get(i).getLocation();
			map.put("LocatorGeocodeResult", resultLocGeom);
			mList.add(map);
		}
		mAdapter.notifyDataSetChanged();
		mSearchViewGroup.setVisibility(View.VISIBLE);
	}
}
