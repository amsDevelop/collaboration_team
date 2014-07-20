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
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class LayerDialog extends DialogFragment implements OnClickListener {

	int resID = R.layout.layout_layer;
	Button mBtn1 = null;
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

		mContaner = (ViewGroup) view
				.findViewById(R.id.id_llaout_layer_container);
		mContaner.setVisibility(View.GONE);
		mListView = (ListView) view.findViewById(R.id.id_lstview_layer_1);
		mListView.setAdapter(new MyAdapter());

		return view;
	}

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(DialogFragment.STYLE_NORMAL, R.style.dialog_theme);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		executeLayer3();
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
		switch (arg0.getId()) {
		case R.id.id_btn_layer_1:
			executeLayer1();
			break;
		case R.id.id_btn_layer_2:
			executeLayer2();
			break;
		case R.id.id_btn_layer_3:
			executeLayer3();
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

	void showAlert() {
		Toast.makeText(getActivity(), "功能暂时未支持", -1).show();
	}
	
	private void executeLayer2() {
		executeLayer1();
		mBtn1.setSelected(false);
		mBtn2.setSelected(true);
		mBtn3.setSelected(false);
	}


	private void executeLayer3() {
		mContaner.setVisibility(View.VISIBLE);
		mBtn1.setSelected(false);
		mBtn2.setSelected(false);
		mBtn3.setSelected(true);
	}


	private void executeLayer1() {
		mContaner.setVisibility(View.GONE);
		showAlert();
		mBtn1.setSelected(true);
		mBtn2.setSelected(false);
		mBtn3.setSelected(false);
	}
	
	

	class MyAdapter extends BaseAdapter {
		ArrayList<String> mList = new ArrayList<String>();

		public MyAdapter() {
			mList.add("油气");
			mList.add("盆地");
			mList.add("石油");
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
			if (arg1 == null) {
				arg1 = LayoutInflater.from(getActivity()).inflate(
						R.layout.layer_list_item, null);
			}

			TextView txView = (TextView) arg1
					.findViewById(R.id.layer_name);
			txView.setText(mList.get(arg0));

			arg1.findViewById(R.id.layer_can_operator).setOnClickListener(
					new OnClickListener() {
						@Override
						public void onClick(View arg0) {
							showAlert();
						}
					});
			return arg1;
		}

	}

}
