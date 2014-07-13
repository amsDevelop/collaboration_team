package com.sinopec.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import com.sinopec.activity.R;
import com.sinopec.application.SinoApplication;

public class MenuViewCount extends LinearLayout{
	private ListView mListView;
	private TextView mTVTitle;
	private View mView;
	private Context mContext;
	private Button mBtn;
	private LinearLayout mLayout;
	
	public MenuViewCount(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		initView();
		initEvent();
	}

	public void setTitle(String name){
		mTVTitle.setText(name);
	}
	
	private void initView() {
		LayoutInflater mInflater = LayoutInflater.from(mContext);
		mView = mInflater.inflate(R.layout.view_menu, null);
		LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(SinoApplication.screenWidth / 4,LayoutParams.WRAP_CONTENT);
		
		param.width = SinoApplication.screenWidth / 4;
		mView.setLayoutParams(param);
		mLayout = (LinearLayout) mView.findViewById(R.id.menu_content);
		mBtn = (Button) mView.findViewById(R.id.menu_view_btn);
		mBtn.setText(mContext.getString(R.string.btn_count));
		mTVTitle = (TextView) mView.findViewById(R.id.menu_title);
		mTVTitle.setText(mContext.getString(R.string.btn_tool_title));
		
		mListView = (ListView) mView.findViewById(R.id.menu_listview);
		String[] items = new String[]{"范围内油气田的个数", "范围内油气田的个数密度", "不同沉积体系油气田个数", "不同沉积体系油气田面积","不同沉积体系油气田石油储量",};
		List<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();  
		for (int i = 0; i < items.length; i++) {
			HashMap<String,String> map = new HashMap<String,String>(); 
			map.put("name", items[i]);
			data.add(map);
		}
		
		SimpleAdapter adapter = new SimpleAdapter(mContext, data, R.layout.item_menu,
				new String[]{"name"}, new int[]{R.id.menu_item_name}); 
		mListView.setAdapter(adapter);
        addView(mView);
	}
	
	private void initEvent() {
		mBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mLayout.getVisibility() == View.INVISIBLE){
//					Animation animup = AnimationUtils.loadAnimation(mContext, R.anim.ani_menu_up);
//					mLayout.startAnimation(animup);
					mLayout.setVisibility(View.VISIBLE);
				}else{
//					Animation animdown =  AnimationUtils.loadAnimation(mContext, R.anim.ani_menu_down);
//					mLayout.startAnimation(animdown);
					mLayout.setVisibility(View.INVISIBLE);
				}
			}
		});
	}
	
	public View getView() {
		return mView;
	}

}
