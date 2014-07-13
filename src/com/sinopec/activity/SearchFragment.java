package com.sinopec.activity;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint({ "NewApi", "ValidFragment" })
public class SearchFragment extends DialogFragment implements OnClickListener {

	
	int resID = R.layout.layout_serch;
	private ListView mListView;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(DialogFragment.STYLE_NORMAL, R.style.dialog_theme);
	}
	
	Editable keyWord;
	
	public SearchFragment(Editable keyWord) {
		this.keyWord = keyWord;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(resID, null);
		
		
		mListView = (ListView) view.findViewById(R.id.id_lstview_search_1);
		mListView.setAdapter(new MyAdapter());
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				showAlert();
			}
		});
		
		TextView tx = (TextView) view.findViewById(R.id.id_txview_searhKey);
		if(keyWord != null && keyWord.toString() != null){
			
			tx.setText("\"" +keyWord.toString() + "\"" + " 搜索结果");
		}
		
		
		
		view.findViewById(R.id.id_btn_operator_1).setOnClickListener(this);
		view.findViewById(R.id.id_btn_operator_2).setOnClickListener(this);
		return view;
		
	}
	
	void showAlert(){
		Toast.makeText(getActivity(), "功能暂时未支持", -1).show() ;
	}
	
	class MyAdapter extends BaseAdapter {
		ArrayList<String> mList = new ArrayList<String>();
		
		public MyAdapter() {
			mList.add("亚洲");
			mList.add("太平洋");
			mList.add("非洲");
			mList.add("欧洲");
			mList.add("胜利油田");
			mList.add("大庆油田");
		}
		
		@Override
		public int getCount() {
			return mList.size();
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			if(arg1 == null){
				arg1 = LayoutInflater.from(getActivity()).inflate(R.layout.adapter_layer_serch, null);
			}
			
			TextView layerName = (TextView) arg1.findViewById(R.id.id_txview_layer);
			
			layerName.setText(mList.get(arg0));
			return arg1;
		}
		
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.id_btn_operator_1:
			dismiss();
			showAlert();
			break;
		case R.id.id_btn_operator_2:
			dismiss();
			break;
		}
	}
}
