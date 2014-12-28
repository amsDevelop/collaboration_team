package com.sinopec.activity;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.sinopec.application.SinoApplication;
import com.sinopec.data.json.UserBean;
import com.sinopec.util.DatabaseHelper;
import com.sinopec.util.Md5Util;
import com.sinopec.util.UserDao;

public class LoginActivity extends  DialogFragment{
//	D:\android\android_luna\adt-bundle-windows-x86-20140702\sdk\platform-tools
	private Context mContext;
	DatabaseHelper dbHelper;
	SQLiteDatabase sdb;
	Context context;
	UserDao userDao;
	View view;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = getActivity();
		//初始化一条测试数据
		UserBean xuuser = new UserBean("xuyy001", "xuyy", "123456");
		 dbHelper = new DatabaseHelper(context);
		 sdb = dbHelper.getWritableDatabase();
		 userDao = new UserDao(context);
		 userDao.save(xuuser);
		 

		 
		 
		 
		 
//		this.mContext = this;
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
////		setTitle(R.string.app_name);
////		setContentView(R.layout.activity_login);
		
	}
	




	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		 
		LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
	     view = layoutInflater.inflate(R.layout.login_dialog, null);
		
		AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
		.setTitle(R.string.user_login_title)
		.setView(view)
		.setPositiveButton(R.string.user_login_dialog_ok, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				EditText userEditText = (EditText)view.findViewById(R.id.userEdit);
				EditText pwdEditText = (EditText)view.findViewById(R.id.pwdEdit);
				String inputuser = userEditText.getText().toString().trim();
				String inputpwd = pwdEditText.getText().toString().trim();
				 //测试用户id
//				 String testuserid = "xuyy001";
				 String testusername = null ,testpwd = null ;
				 userDao = new UserDao(context);
				 UserBean testuser= userDao.findUser(inputuser);
				  if(null != testuser){
					  testusername = testuser.getUserName();
					  testpwd = testuser.getPassword();
					  if(Md5Util.validatePassword(testpwd, inputpwd)){
						  SinoApplication.mLoginSuccess = true;
						  SinoApplication.mloginuserid = testuser.getUserId();
						  Toast.makeText(context, inputuser+inputpwd+"登录成功="+testusername+testpwd, Toast.LENGTH_LONG).show();
					  }else{
						  Toast.makeText(context, inputuser+inputpwd+"密码错误="+testusername+testpwd, Toast.LENGTH_LONG).show();
					  }
				  }else{
					  Toast.makeText(context, inputuser+inputpwd+"该用户不存在="+testusername+testpwd, Toast.LENGTH_LONG).show();
				  }

				 
				
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
