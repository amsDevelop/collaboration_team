package com.sinopec.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.tasks.ags.find.FindParameters;
import com.esri.core.tasks.ags.find.FindResult;
import com.esri.core.tasks.ags.find.FindTask;
import com.esri.core.tasks.ags.geocode.Locator;
import com.esri.core.tasks.ags.geocode.LocatorFindParameters;
import com.esri.core.tasks.ags.geocode.LocatorGeocodeResult;
import com.esri.core.tasks.ags.na.NAFeaturesAsFeature;
import com.esri.core.tasks.ags.na.ServiceAreaParameters;
import com.sinopec.adapter.SearchAdapter;
import com.sinopec.application.SinoApplication;
import com.sinopec.task.SearchFindTask;
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
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				HashMap<String, Object> map = (HashMap<String, Object>) arg0.getAdapter().getItem(position);
				FindResult result = (FindResult) map.get("FindResult");
//				SinoApplication.searchResult = (LocatorGeocodeResult) map.get("FindResult");
				Geometry geometry = result.getGeometry();
				Point point = (Point) geometry;
				// Convert point to EGS (decimal degrees)
				Map<String, Object> attrMap = result.getAttributes();
			    Set<Entry<String ,Object>> ents= attrMap.entrySet();  
			    String message = "";
			    String x = (String) attrMap.get("CENX");
			    String y = (String) attrMap.get("CENY");
			    Log.d("map", result.getValue()+" X："+x+"  Y：  "+y);
			    for(Entry<String ,Object> ent:ents){  
////			       message += ent.getKey()+" : "+ent.getValue()+"\n";  
//			       Log.d("map", " key："+ent.getKey()+"  val：  "+ent.getValue());
			    }
				
			}
		});
		
	}
	
	private void backToMap(LocatorGeocodeResult result) {
		Intent intent = new Intent(mContext, MarinedbActivity.class);
		String passString = "月球来的消息:Hello,我是月球的Lucy,非常欢迎你来月球";
		intent.putExtra("FromMoon", passString);
		setResult(RESULT_OK, intent);
	}

	private ArrayList<HashMap<String, Object>> mList = new ArrayList<HashMap<String, Object>>();
	private SearchAdapter mAdapter;

	private void initData() {
		mAdapter = new SearchAdapter(mContext, mList);
		mListView.setAdapter(mAdapter);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_search_confirm:
			String key = mEditText.getText().toString();
			SearchFindTask task = new SearchFindTask(mContext, mListView, mList,  mViewGroup, mAdapter,"");
		    task.execute(key);
			
			break;
		case R.id.id_btn_operator_2:
			break;
		}
	}

	private void updateDataNew(List<LocatorGeocodeResult> results) {
		for (int i = 0; i < results.size(); i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			 Geometry resultLocGeom = results.get(i).getLocation();
			map.put("LocatorGeocodeResult", resultLocGeom);
			mList.add(map);
		}
		mAdapter.notifyDataSetChanged();
		mViewGroup.setVisibility(View.VISIBLE);
	}
}
