package com.sinopec.activity;

import android.app.Activity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sinopec.activity.R;
import com.sinopec.application.SinoApplication;

public class MainActivity extends Activity{
	private String tag = "MainActivity";
	private TextView mTVContent;
	private ProgressBar mProgressBar;
	
	private TextView mTextView;
	
	private LinearLayout mToolBar;
	private Button mBtnMenuTool;
	private Button mBtnMenuCount;
	private Button mBtnMenuCompare;
	private Button mBtnMenuMine;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        setTitle(R.string.app_name);
        SinoApplication.screenWidth = this.getWindowManager().getDefaultDisplay().getWidth();
        SinoApplication.screenHeight = this.getWindowManager().getDefaultDisplay().getHeight();
        setContentView(R.layout.activity_main);
        initView();
	}

    private void initView() {
    	mToolBar = (LinearLayout) findViewById(R.id.menu_bar);
    	
    	
//    	mBtnMenuTool = (Button) findViewById(R.id.menu_tool);
    	mBtnMenuCount = (Button) findViewById(R.id.menu_count);
    	mBtnMenuCompare = (Button) findViewById(R.id.menu_compare);
    	mBtnMenuMine = (Button) findViewById(R.id.menu_mine);
    	
        mProgressBar = (ProgressBar) findViewById(R.id.progressbar);
        mTVContent = (TextView) findViewById(R.id.tv_content);
        mTVContent.setMovementMethod(ScrollingMovementMethod.getInstance());
	}
    
    private void initData() {
    	mProgressBar.setVisibility(View.VISIBLE);
    }
}
