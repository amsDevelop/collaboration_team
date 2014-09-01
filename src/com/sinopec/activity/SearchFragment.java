package com.sinopec.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.esri.core.map.Graphic;
import com.esri.core.tasks.ags.find.FindResult;
import com.esri.core.tasks.ags.identify.IdentifyResult;
import com.sinopec.adapter.SearchAdapter;
import com.sinopec.application.SinoApplication;
import com.sinopec.common.CommonData;
import com.sinopec.common.InterfaceDataCallBack;
import com.sinopec.task.SearchFindTask;
import com.sinopec.view.ClearableEditText;
import com.sinopec.view.MenuButtonNoIcon;

@SuppressLint({ "NewApi", "ValidFragment" })
public class SearchFragment extends Fragment implements OnClickListener {
	private String tag= "SearchFragment";
	private ListView mListView;
	private ListView mLvHistory;
	private Button mBtnClear;
	private Context mContext;
	private MenuButtonNoIcon mConfirm;
	private ViewGroup mViewGroup;
	private ViewGroup mViewGroupHistory;
	private ClearableEditText mEditText;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.mContext = getActivity();
		Log.d(tag, "----------------------onCreate");
		SinoApplication.identifyResult = null;
//		SinoApplication.mSearchHistory.add("四川");
	}
	
	private InterfaceDataCallBack mInterfaceDataCallBack;
	
	public SearchFragment(InterfaceDataCallBack data) {
		this.mInterfaceDataCallBack = data;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.activity_search, null);
		initView(view);
		initData();
		Log.d(tag, "----------------------onCreateView");
		return view;
		
	}
	
	@Override
	public void onPause() {
		super.onPause();
		Log.d(tag, "----------------------onPause");
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(tag, "----------------------onDestroy");
	}
	
	private ImageButton mBtnBack;
	private TextView mTVTitle;
	private void initView(View view) {
		mTVTitle = (TextView) view.findViewById(R.id.search_layername);
		mConfirm = (MenuButtonNoIcon) view.findViewById(R.id.btn_search_confirm);
		mConfirm.setOnClickListener(this);
		mListView = (ListView) view.findViewById(R.id.search_listview);
		mLvHistory = (ListView) view.findViewById(R.id.search_history_listview);
		mBtnClear = (Button) view.findViewById(R.id.search_history_clear);
		mBtnClear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				SinoApplication.mSearchHistory.clear();
				mViewGroupHistory.setVisibility(View.GONE);
			}
		});
		mViewGroupHistory = (ViewGroup) view.findViewById(R.id.search_history_layout);
		mViewGroup = (ViewGroup) view.findViewById(R.id.search_listview_layout);
		mBtnBack = (ImageButton) view.findViewById(R.id.btn_login_back);
		mBtnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				getActivity().onBackPressed();
				mInterfaceDataCallBack.setData(null);
			}
		});
		mEditText = (ClearableEditText) view.findViewById(R.id.edittext_search);
		mEditText.addTextChangedListener(new TextWatcher() {
			private String tag = "text";
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
//				Log.d(tag, "-----onTextChanged: "+arg0.toString());
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
//				Log.d(tag, "-----afterTextChanged: "+arg0.toString());
				String key = arg0.toString();
				if("".equals(key)){
					if(mAdapter != null){
						mList.clear();
						mAdapter.notifyDataSetChanged();
						mViewGroup.setVisibility(View.GONE);
						mLvHistory.setVisibility(View.GONE);
					}
				}else{
//					search(arg0.toString());
					setSearchHistory();
				}
				
			}
		});
		
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				if(SinoApplication.mFeatureSet4Query != null){
					HashMap<String, Object> tmpMap = (HashMap<String, Object>) arg0.getAdapter().getItem(position);
					Graphic graphic = (Graphic) tmpMap.get("Graphic");
					mInterfaceDataCallBack.setData4Query(graphic);
				}else{
					if(SinoApplication.mResultList4FrameSearch.size() == 0){
						HashMap<String, Object> tmpMap = (HashMap<String, Object>) arg0.getAdapter().getItem(position);
						FindResult result = (FindResult) tmpMap.get("FindResult");
						mInterfaceDataCallBack.setData(result);
						mViewGroup.setVisibility(View.GONE);
						mList.clear();
						mAdapter.notifyDataSetChanged();
					}else{
						HashMap<String, Object> tmpMap = (HashMap<String, Object>) arg0.getAdapter().getItem(position);
						IdentifyResult identifyResult = (IdentifyResult) tmpMap.get("IdentifyResult");
						mInterfaceDataCallBack.setData4Frame(identifyResult);
					}
				}

			}
		});
		
		mLvHistory.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				HashMap<String, Object> map = (HashMap<String, Object>) arg0.getAdapter().getItem(position);
				String key = (String) map.get("name");
				search(key);
			}
		});
		
	}
	
	private ArrayList<HashMap<String, Object>> mList = new ArrayList<HashMap<String, Object>>();
	private SearchAdapter mAdapter;

	private void initData() {
		if(SinoApplication.mFeatureSet4Query != null){
//			mList.clear();
			mConfirm.setVisibility(View.VISIBLE);
			mEditText.setVisibility(View.VISIBLE);
			mTVTitle.setVisibility(View.GONE);
			Graphic[] graphics = SinoApplication.mFeatureSet4Query.getGraphics();
			for (int i = 0; i < graphics.length; i++) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("Graphic", graphics[i]);
				mList.add(map);
			}
			
			mAdapter = new SearchAdapter(mContext, mList);
			mListView.setAdapter(mAdapter);
			mViewGroup.setVisibility(View.VISIBLE);
		}else{
			if(SinoApplication.mResultList4FrameSearch.size() == 0){
				mConfirm.setVisibility(View.VISIBLE);
				mEditText.setVisibility(View.VISIBLE);
				mTVTitle.setVisibility(View.GONE);
				
				mAdapter = new SearchAdapter(mContext, mList);
				mListView.setAdapter(mAdapter);
			}else{
				if(!TextUtils.isEmpty(SinoApplication.mLayerName)){
					mTVTitle.setVisibility(View.VISIBLE);
					mTVTitle.setText("["+SinoApplication.mLayerName+"] 图层");
				}
				mConfirm.setVisibility(View.GONE);
				mEditText.setVisibility(View.GONE);
				for (int i = 0; i < SinoApplication.mResultList4FrameSearch.size(); i++) {
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("IdentifyResult", SinoApplication.mResultList4FrameSearch.get(i));
					mList.add(map);
				}
				mAdapter = new SearchAdapter(mContext, mList);
				mListView.setAdapter(mAdapter);
				mViewGroup.setVisibility(View.VISIBLE);
			}
		}

	}
	
	private void setSearchHistory() {
		mViewGroupHistory.setVisibility(View.VISIBLE);
	       ArrayList<HashMap<String, Object>> arrayList = new ArrayList<HashMap<String,Object>>();  
	       int size = 10;
	       if(SinoApplication.mSearchHistory.size() > 10){
	    	   size = 10;
	       }else{
	    	   size = SinoApplication.mSearchHistory.size();
	       }
	        for(int i = 0;i < size; i++){  
	            HashMap<String, Object> tempHashMap = new HashMap<String, Object>();  
	            tempHashMap.put("name", SinoApplication.mSearchHistory.get(i));  
	            arrayList.add(tempHashMap);  
	              
	        }  
		SimpleAdapter adapter = new SimpleAdapter(mContext, arrayList, R.layout.item_history,  
                new String[]{"name"}, new int[]{R.id.item_history_name});  
        mLvHistory.setAdapter(adapter);  
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_search_confirm:
//			String key = mEditText.getText().toString();
//			SearchFindTask task = new SearchFindTask(mInterfaceDataCallBack, mContext, mListView, mList,  mViewGroup, mAdapter,"");
//		    task.execute(key);
			search(mEditText.getText().toString());
			break;
		case R.id.id_btn_operator_2:
			break;
		}
	}
	
	private void search(String key) {
		filterSearchHistory(key);
		SearchFindTask task = new SearchFindTask(mInterfaceDataCallBack, mContext, mListView, mList,  mViewGroup, mAdapter,
				getString(R.string.url_marine_oil));
		task.execute(key);
		mViewGroupHistory.setVisibility(View.GONE);
	}
	
	private void filterSearchHistory(String key) {
		boolean hasKey = false;
		for (int i = 0; i < SinoApplication.mSearchHistory.size(); i++) {
			if(key.equals(SinoApplication.mSearchHistory.get(i))){
//				SinoApplication.mSearchHistory.remove(i);
				hasKey = true;
			}
		}
		
		if(!hasKey){
			SinoApplication.mSearchHistory.add(key);
		}
	}
}
