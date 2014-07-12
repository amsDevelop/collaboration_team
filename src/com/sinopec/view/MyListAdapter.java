package com.sinopec.view;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sinopec.activity.R;

public class MyListAdapter extends BaseAdapter
{
   private Context mContext;
   private List<String> list;
   
   public MyListAdapter (Context mContext, List<String> list) {
	   this.mContext = mContext;
	   this.list = list;
	   
	   
   }
	
	@Override
	public int getCount() {
	   
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		 TextView childTextView = null;  
	        if(convertView == null){  
	           convertView = LayoutInflater.from(mContext).inflate(R.layout.childs, null);  
	        }  
	        childTextView = (TextView)convertView.findViewById(R.id.mytextview_childs);  
	        childTextView.setText(list.get(position));  
	        return convertView;  
	}

}
