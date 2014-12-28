package com.sinopec.activity;

import com.sinopec.adapter.FavoriteListAdapter;
import com.sinopec.application.SinoApplication;
import com.sinopec.data.json.FavoriteBean;
import com.sinopec.data.json.UserBean;
import com.sinopec.util.FavoriteDao;
import com.sinopec.util.Md5Util;
import com.sinopec.util.UserDao;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ListFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FavoriteListFragment extends DialogFragment {
	
	View view;
	Context context;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		context = getActivity();
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		 
		LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
	     view = layoutInflater.inflate(R.layout.favorite_dialog, null);
		ListView fListView = (ListView)view.findViewById(R.id.favoritelistview);
		TextView projectname = (TextView)view.findViewById(R.id.projectname);
		
		FavoriteBean fbean = null;
		FavoriteDao fdao = new FavoriteDao(getActivity());
		fbean = (FavoriteBean) fdao.getAllFavoriteBean(0, 0);
		
	     
		AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
		.setView(view)
		.create();
		Window win = alertDialog.getWindow();
		WindowManager.LayoutParams lp = win.getAttributes();
		lp.alpha=0.6f;
		win.setAttributes(lp);
		return alertDialog;
	}




	

}
