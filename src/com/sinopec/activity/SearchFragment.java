package com.sinopec.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.esri.core.tasks.ags.find.FindResult;
import com.sinopec.adapter.SearchAdapter;
import com.sinopec.common.InterfaceDataCallBack;
import com.sinopec.task.SearchFindTask;
import com.sinopec.view.MenuButtonNoIcon;

@SuppressLint({ "NewApi", "ValidFragment" })
public class SearchFragment extends Fragment implements OnClickListener {
	private String tag= "SearchFragment";
	private ListView mListView;
	private Context mContext;
	private MenuButtonNoIcon mConfirm;
	private ViewGroup mViewGroup;
	private EditText mEditText;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.mContext = getActivity();
		Log.d(tag, "----------------------onCreate");
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
	private void initView(View view) {
		mConfirm = (MenuButtonNoIcon) view.findViewById(R.id.btn_search_confirm);
		mConfirm.setOnClickListener(this);
		mListView = (ListView) view.findViewById(R.id.search_listview);
		mViewGroup = (ViewGroup) view.findViewById(R.id.search_listview_layout);
		mBtnBack = (ImageButton) view.findViewById(R.id.btn_login_back);
		mBtnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				getActivity().onBackPressed();
			}
		});
		mEditText = (EditText) view.findViewById(R.id.edittext_search);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				HashMap<String, Object> tmpMap = (HashMap<String, Object>) arg0.getAdapter().getItem(position);
				FindResult result = (FindResult) tmpMap.get("FindResult");
				mInterfaceDataCallBack.setData(result);
				//TODO:弹出pappap
				mViewGroup.setVisibility(View.GONE);
				mList.clear();
				mAdapter.notifyDataSetChanged();
			}
		});
		
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
}
