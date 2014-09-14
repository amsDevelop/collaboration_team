package com.sinopec.activity;


import com.lenovo.nova.util.BaseDialogFragment;
import com.lenovo.nova.util.slog;
import com.sinopec.bean.UserInfo;
import com.sinopec.chart.PolygonLineChart3;
import com.sinopec.dao.SqlitService;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.Toast;

public class LoginActivity extends  DialogFragment{
	
	private Context mContext;
	private EditText UserName;
	private EditText UserPassword;
	private SqlitService sqlitService;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
//		this.mContext = this;
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
////		setTitle(R.string.app_name);
////		setContentView(R.layout.activity_login);
////		弹出登陆界面
//	    final Builder b = new AlertDialog.Builder(this);
//	    View tl = getLayoutInflater().inflate(R.layout.login_dialog, null);
//		LayoutInflater.from(this).inflate(R.layout.login_dialog, null, true);
//		b.setView(tl);
//		b.setNegativeButton("登陆", null);
//		b.setPositiveButton("取消", null);
//		 b.create().show();
//		initView();
//		setContentView(new PolygonLineChart3().getBarChartView(mContext));
	}
	
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
//			Bundle savedInstanceState) {
//		View view = inflater.inflate(R.layout.logindialog, null);
//		slog.p("ConditionQuery  onCreateView");
//		return view;
//	}



	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
		View view = layoutInflater.inflate(R.layout.login_dialog, null);
		UserName = (EditText) view.findViewById(R.id.userEdit);  
		UserPassword = (EditText) view.findViewById(R.id.pwdEdit);  
		
		sqlitService = new SqlitService(this.getActivity());
		AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
		.setTitle(R.string.user_login_title)
		.setView(view)
		.setPositiveButton(R.string.user_login_dialog_ok, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
//				SqlitService sService = new SqlitService(mContext);
				String mName = UserName.getText().toString().trim();
				String mPwd = UserPassword.getText().toString().trim();
				UserInfo userInfo = sqlitService.findUserInfoByUserPw(mName,mPwd);
				if(userInfo != null){
					Toast.makeText(mContext, "登陆成功", Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(mContext, "登陆失败", Toast.LENGTH_SHORT).show();
				}
				
//				for (int i = 0; i < 10; i++) {
//					UserInfo info = new UserInfo();
//					info.setUserName("aa" + i);
//					info.setUserPassword(i +"");
//					sqlitService.insertRecord(info);
//					
//				}
				
				
				
			}
		})
		.setNegativeButton(R.string.user_login_dialog_cancel, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		})
		.create();
		Window win = alertDialog.getWindow();
		WindowManager.LayoutParams lp = win.getAttributes();
		lp.alpha=0.6f;
		win.setAttributes(lp);
		return alertDialog;
	}

	private ImageButton mBtnBack;
//	private void initView() {
//		mBtnBack = (ImageButton)findViewById(R.id.btn_login_back);
//		mBtnBack.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				finish();
//			}
//		});
//		
//	}
}
