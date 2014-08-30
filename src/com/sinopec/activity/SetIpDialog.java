package com.sinopec.activity;

import android.database.DatabaseUtils;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.lenovo.nova.util.BaseDialogFragment;
import com.lenovo.nova.util.network.NetworkUtils;
import com.lenovo.nova.util.parse.DBParserUtil;
import com.sinopec.data.json.ConfigBean;
import com.sinopec.data.json.Constant;

public class SetIpDialog extends BaseDialogFragment implements OnClickListener{


	private EditText et;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.ipset_layout, null);
		et = (EditText) view.findViewById(R.id.editText1);
		et.setText(Constant.baseIP);
		view.findViewById(R.id.button1).setOnClickListener(this);
		view.findViewById(R.id.button2).setOnClickListener(this);
		return view;
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.button1:
			String ip = null;
			try {
				 ip = et.getText().toString();
			} catch (Exception e) {
				Toast.makeText(getActivity(), e.toString(), -1).show();
				e.printStackTrace();
				return;
			}
			
			if(!NetworkUtils.validateDataFormat(ip)){
				Toast.makeText(getActivity(), "IP格式错误",  -1).show();
			}
			
//			http://202.204.193.201:8080/
			Constant.baseURL = Constant.baseURL.replace("202.204.193.201", ip);
			Constant.baseIP = ip;
			Toast.makeText(getActivity(), ip +" set success ", -1).show();
			try {
				DBParserUtil parse = new DBParserUtil(getActivity()){
					@Override
					protected Class onGetBeanForCreateTable() {
						return ConfigBean.class;
					}
				};
				ConfigBean bean = new ConfigBean();
				bean.setIP(ip);
				parse.saveBean(bean);
			} catch (Exception e) {
				e.printStackTrace();
			}
			dismiss();
			break;
		case R.id.button2:
			dismiss();
			break;
		}
	}
	
}
