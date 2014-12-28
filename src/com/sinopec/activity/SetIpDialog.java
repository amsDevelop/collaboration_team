package com.sinopec.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import com.lenovo.nova.util.network.NetworkUtils;
import com.lenovo.nova.util.parse.DBUtil;
import com.lenovo.nova.widget.dialog.BaseDialogFragment;
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
				Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
				e.printStackTrace();
				return;
			}
			
			if(!NetworkUtils.validateDataFormat(ip)){
				Toast.makeText(getActivity(), "IP格式错误",  Toast.LENGTH_LONG).show();
			}
			Constant.baseURL = Constant.baseURL.replace("10.200.250.110", ip);
			Constant.baseIP = ip;
			Toast.makeText(getActivity(), ip +" set success ",Toast.LENGTH_LONG).show();
			try {
				DBUtil parse = new DBUtil(getActivity()){
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
