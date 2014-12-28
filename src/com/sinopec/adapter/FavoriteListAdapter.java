package com.sinopec.adapter;

import com.sinopec.activity.R;
import com.sinopec.data.json.FavoriteBean;
import com.sinopec.util.FavoriteDao;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class FavoriteListAdapter extends BaseAdapter {

	private LayoutInflater mInflater =null;
	private FavoriteDao fdao = null;
	public FavoriteListAdapter(Context context){
		super();
		mInflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
		fdao = new FavoriteDao(context);
	}
	class ViewHolder{
		public TextView fname;
	}

	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		int count = 0;
		count = (int)fdao.favoriteBeanCount();
		return count;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if(null == convertView){
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.favorite_dialog, null);
			holder.fname = (TextView)convertView.findViewById(R.id.projectname);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		
		FavoriteBean fbean = fdao.findFavoriteBean("1");
		holder.fname.setText(fbean.getProjectName());
		
		return convertView;
	}
	

}
