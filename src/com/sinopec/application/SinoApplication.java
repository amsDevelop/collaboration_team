package com.sinopec.application;

import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;

public class SinoApplication extends Application {
	public static Context mContext;
	public static int screenWidth = 0;
	public static int screenHeight = 0;
	public static float density = 0;
	public static int densityDpi = 0;
	/**
	 * 主页面底部菜单分成屏幕宽度的几份
	 */
	public static int menuDivisionNumber = 6;

	public static Context getContext() {
		return mContext;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		this.mContext = this;
	}
}
