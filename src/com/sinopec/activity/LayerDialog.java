package com.sinopec.activity;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class LayerDialog extends DialogFragment implements OnClickListener{

	int resID = R.layout.layout_layer;
	Button mBtn1 =null;
	Button mBtn2 = null;
	Button mBtn3 = null;
	private ViewGroup mContaner;
	private ListView mListView;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(resID, null);
		
		mBtn1 = (Button) view.findViewById(R.id.id_btn_layer_1);
		mBtn2 = (Button) view.findViewById(R.id.id_btn_layer_2);
		mBtn3 = (Button) view.findViewById(R.id.id_btn_layer_3);
		
		 view.findViewById(R.id.id_btn_operator_1).setOnClickListener(this);
		 view.findViewById(R.id.id_btn_operator_2).setOnClickListener(this);
		mBtn3 = (Button) view.findViewById(R.id.id_btn_layer_3);
		
		mContaner = (ViewGroup) view.findViewById(R.id.id_llaout_layer_container);
		mContaner.setVisibility(View.GONE);
		mListView = (ListView) view.findViewById(R.id.id_lstview_layer_1);
		mListView.setAdapter(new MyAdapter());
		
		
		return view;
	}
	
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		Dialog dialog = new Dialog(getActivity()){
			@Override
			protected void onCreate(Bundle savedInstanceState) {
				super.onCreate(savedInstanceState);
//				getWindow().getWindowManager()
			}
		};
		
		return dialog;
	}
	
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
//		setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo);
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		mBtn1.setOnClickListener(this);
		mBtn2.setOnClickListener(this);
		mBtn3.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		switch(arg0.getId()){
		case R.id.id_btn_layer_1:
			mContaner.setVisibility(View.GONE);
			showAlert();
			break;
		case R.id.id_btn_layer_2:
			mContaner.setVisibility(View.GONE);
			showAlert();
			break;
		case R.id.id_btn_layer_3:
			mContaner.setVisibility(View.VISIBLE);
			break;
			
		case R.id.id_btn_operator_1:
			dismiss();
			showAlert();
			break;
			
		case R.id.id_btn_operator_2:
			dismiss();
			break;
			
		}
		
	}
	
	
	void showAlert(){
		Toast.makeText(getActivity(), "功能暂时未支持", -1).show() ;
	}
	
	class MyAdapter extends BaseAdapter {
		ArrayList<String> mList = new ArrayList<String>();
		
		public MyAdapter() {
			mList.add("图层1");
			mList.add("图层2");
			mList.add("图层3");
			mList.add("图层4");
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
				arg1 = LayoutInflater.from(getActivity()).inflate(R.layout.adapter_layer_list, null);
			}
			
			TextView txView = (TextView) arg1.findViewById(R.id.id_txview_layer);
			txView.setText(mList.get(arg0));
			
			arg1.findViewById(R.id.id_btn_layer_operator).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					showAlert();
				}
			});
			return arg1;
		}
		
	}
	
}