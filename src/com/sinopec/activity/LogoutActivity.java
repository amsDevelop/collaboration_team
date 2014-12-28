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

public class LogoutActivity extends  DialogFragment{
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
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		 
		LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
	     view = layoutInflater.inflate(R.layout.logout_dialog, null);
		
		AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
//		.setTitle(R.string.user_login_title)
		.setView(view)
		.setPositiveButton(R.string.user_login_dialog_ok, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				SinoApplication.mLoginSuccess = false;
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

}

