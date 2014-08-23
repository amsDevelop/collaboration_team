package com.sinopec.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnHoverListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.esri.android.map.Callout;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.Layer;
import com.esri.android.map.MapOnTouchListener;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISFeatureLayer;
import com.esri.android.map.ags.ArcGISFeatureLayer.MODE;
import com.esri.android.map.ags.ArcGISFeatureLayer.Options;
import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;
import com.esri.android.map.event.OnLongPressListener;
import com.esri.android.map.event.OnSingleTapListener;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.android.map.event.OnZoomListener;
import com.esri.android.map.event.OnStatusChangedListener.STATUS;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Point;
import com.esri.core.map.FeatureSet;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.tasks.ags.find.FindResult;
import com.esri.core.tasks.ags.identify.IdentifyParameters;
import com.esri.core.tasks.ags.identify.IdentifyResult;
import com.sinopec.adapter.MenuAdapter;
import com.sinopec.adapter.MenuGridAdapter;
import com.sinopec.adapter.SearchAdapter;
import com.sinopec.application.SinoApplication;
import com.sinopec.common.CommonData;
import com.sinopec.common.InterfaceDataCallBack;
import com.sinopec.common.OilGasData;
import com.sinopec.drawtool.DrawEvent;
import com.sinopec.drawtool.DrawEventListener;
import com.sinopec.drawtool.DrawTool;
import com.sinopec.task.SearchIdentifyTask;
import com.sinopec.util.ChildrenMenuDataUtil;
import com.sinopec.util.SinoUtil;
import com.sinopec.view.MenuButton;
import com.sinopec.view.MenuButtonNoIcon;

public class MarinedbActivity extends Activity implements OnClickListener,
		OnItemClickListener, DrawEventListener, InterfaceDataCallBack {
	private String tag = "map";
	private TextView mTVContent;

	// 泡泡变量
	private View popView = null;
	Callout callout = null;
	private boolean isHideCallout = false;
//	private ImageView imageAnim = null;
	String name2 = null;
	String Point_X = null;
	String Point_Y = null;
	String address2 = null;
	String telephone2 = null;
	double x1;
	double y1;
	private Graphic LongPressPOI = null;
	private Point anchorPt = null;

	private MapView map = null;
	ArcGISFeatureLayer fLayer = null;
	private GraphicsLayer gLayer = null;
	private Button property, statistics, doc;
	private Button mLongTouchTitle;
	private Button mBtnLayer;
	private MenuButtonNoIcon mBtnSearch;
	private Button mEditText;
	private PopupWindow popupWindow;
	private Context mContext;
	private Button btnFrame, btnPolygon, btnLine;
	private Button btnCurScreen;
	private Button btnMultiple;
	private Button mBtnCancelChoose;
	private ImageButton mBtnScaleSmall;
	private ImageButton mBtnScaleBig;
	private Map<String, View> menuStatus = new HashMap<String, View>();
	/**
	 * 子菜单
	 */
	private GridView mGridView;
	/**
	 * 子菜单依赖的布局
	 */
	private RelativeLayout mGridViewLayout;
	/**
	 * 绘制选择框图层
	 */
	private GraphicsLayer drawLayer;
	/**
	 * 绘制高亮的图层
	 */
	private GraphicsLayer mDrawLayer4HighLight;
	private SimpleFillSymbol fillSymbol;
	private MapTouchListener mapTouchListener;
	private DrawTool drawTool;
	private ArcGISTiledMapServiceLayer tms;
    private ViewGroup mBaseLayout;
    /**
     * 左侧工具栏根布局
     */
    private ViewGroup mLeftBarLayout;
    /**
     * 搜索结果fragment依赖的布局
     */
    private ViewGroup mFragmentLayout;
    private ArrayList<HashMap<String, Object>> toolist = new ArrayList<HashMap<String, Object>>();
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.mContext = this;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		SinoApplication.getLayerIDAndKeyMap();
		setTitle(R.string.app_name);
		SinoApplication.screenWidth = this.getWindowManager()
				.getDefaultDisplay().getWidth();
		SinoApplication.screenHeight = this.getWindowManager()
				.getDefaultDisplay().getHeight();
		setContentView(R.layout.activity_main);
		initView();
		map = (MapView) findViewById(R.id.map);
//		Envelope envelope = new Envelope(new Point(-29.440589,5.065565));
//		map.setExtent(envelope, 0);
		tms = new ArcGISTiledMapServiceLayer(
				"http://202.204.193.201:6080/arcgis/rest/services/marine_oil/MapServer");
//				oilUrl);

		//加入6个专题图层 
		String[] urls = getResources().getStringArray(R.array.all_layer_urls);
		for (int i = 0; i < urls.length; i++) {
			ArcGISTiledMapServiceLayer layer = new ArcGISTiledMapServiceLayer(urls[i]);
			map.addLayer(layer);
		}
		//不显示卫星图
		Layer layerSatellite = map.getLayerByURL(SinoApplication.imageUrl);
		layerSatellite.setVisible(false);
		
		Options o = new Options();
		o.mode = MODE.ONDEMAND;
		o.outFields = new String[] { "FIELD_KID", "APPROXACRE", "FIELD_NAME",
				"STATUS", "PROD_GAS", "PROD_OIL", "ACTIVEPROD", "CUMM_OIL",
				"MAXOILWELL", "LASTOILPRO", "LASTOILWEL", "LASTODATE",
				"CUMM_GAS", "MAXGASWELL", "LASTGASPRO", "LASTGASWEL",
				"LASTGDATE", "AVGDEPTH", "AVGDEPTHSL", "FIELD_TYPE",
				"FIELD_KIDN" };
		// http://10.225.14.201:6080/arcgis/rest/services/marinegeo/MapServer
//		fLayer = new ArcGISFeatureLayer(
//				"http://sampleserver3.arcgisonline.com/ArcGIS/rest/services/Petroleum/KSPetro/MapServer/1",
//				o);
//		map.addLayer(fLayer);
		mDrawLayer4HighLight = new GraphicsLayer();
		drawLayer = new GraphicsLayer();
		
		map.addLayer(mDrawLayer4HighLight);
		map.addLayer(drawLayer);
		initSymbols();// 初始化符号
		// add by gaolixiao
		addPopupWindow();
		getAboutDisplay();
		initLayout();
		drawTool = new DrawTool(map, this, callout);
		drawTool.addEventListener(this);
		drawTool.setDrawLayer(drawLayer, mDrawLayer4HighLight);
		 mapTouchListener = new MapTouchListener(this, map);
		 map.setOnTouchListener(mapTouchListener);
		 //这个干哈用？？
		 map.setOnZoomListener(new OnZoomListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void preAction(float arg0, float arg1, double arg2) {
			
				   
			}
			
			@Override
			public void postAction(float arg0, float arg1, double arg2) {
				// TODO Auto-generated method stub
				Log.v("mandy", "postAction...................." + isHideCallout);
				
				if (isHideCallout) {
					   callout.hide();
					} else {
						callout.show();
					}
			}
		});
		 
	
		initData();
		initAnimations();
		searchFragment = new SearchFragment(this);
		fragmentManager = getFragmentManager();
		SinoApplication.currentLayerUrl = getString(R.string.url_basin_4search);
		SinoApplication.currentLayerUrl4Multi = getString(R.string.url_basin);
		map.zoomTo(new Point(0,0), (float) map.getMaxResolution());
	}

	private void initSymbols() {
//		lineSymbol = new SimpleLineSymbol(Color.RED, 5,
//				SimpleLineSymbol.STYLE.DASH);
//		markerSymbol = new SimpleMarkerSymbol(Color.BLUE, 10,
//				SimpleMarkerSymbol.STYLE.CIRCLE);
		fillSymbol = new SimpleFillSymbol(Color.RED);
		fillSymbol.setAlpha(33);
	}

	private void initLayout() {
		btnMultiple = (Button) findViewById(R.id.tb_multiple);
		btnFrame = (Button) findViewById(R.id.tb_frame);
		btnPolygon = (Button) findViewById(R.id.tb_polygon);
		btnLine = (Button) findViewById(R.id.tb_line);
		btnCurScreen = (Button) findViewById(R.id.tb_cur_screen);
		btnMultiple.setOnClickListener(this);
		btnFrame.setOnClickListener(this);
		btnPolygon.setOnClickListener(this);
		btnLine.setOnClickListener(this);
		btnCurScreen.setOnClickListener(this);
	}
	
	private Animation aniIn;
	private Animation aniOut;
	private Animation aniUp;
	private Animation aniDown;
	private void initAnimations() {
		aniIn = AnimationUtils.loadAnimation(mContext, R.anim.ani_leftmenu_in);
		aniOut = AnimationUtils.loadAnimation(mContext, R.anim.ani_leftmenu_out);
		aniDown = AnimationUtils.loadAnimation(mContext, R.anim.ani_menu_down);
		aniUp = AnimationUtils.loadAnimation(mContext, R.anim.ani_menu_up);
	}

	public void getAboutDisplay() {
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
//		int width = metric.widthPixels; // 屏幕宽度（像素）
//		int height = metric.heightPixels; // 屏幕高度（像素）
//		SinoApplication.density = metric.density; // 屏幕密度（0.75 / 1.0 / 1.5）
//		int densityDpi = metric.densityDpi; // 屏幕密度DPI（120 / 160 / 240）
		// 此设备 width: 2560 hei: 1600 density: 2.0 densityDpi: 320
//		Log.d("sinopec", "width: " + width + "  hei: " + height + "  density: "
//				+ SinoApplication.density + "  densityDpi: " + densityDpi);
	}
	
	private IdentifyParameters mIdentifyParameters = new IdentifyParameters();
	private void initSearchParams(Point pt) {
	  //TODO:设置Identify查询参数
	  mIdentifyParameters.setTolerance(20);
	  mIdentifyParameters.setDPI(98);
	  mIdentifyParameters.setLayers(new int[]{0,1,2,3,4,5,6,7}); 
	  mIdentifyParameters.setLayerMode(IdentifyParameters.TOP_MOST_LAYER); 
//	  mIdentifyParameters.setLayers(new int[]{4});
//	  mIdentifyParameters.setLayerMode(IdentifyParameters.ALL_LAYERS);

	  mIdentifyParameters.setGeometry(pt);
	  mIdentifyParameters.setSpatialReference(map.getSpatialReference());         
	  mIdentifyParameters.setMapHeight(map.getHeight());
	  mIdentifyParameters.setMapWidth(map.getWidth());
	  
	     // add the area of extent to identify parameters
      Envelope env = new Envelope(pt);
//      map.setExtent(new Envelope(pt), 0);
	  map.getExtent().queryEnvelope(env);
	  mIdentifyParameters.setMapExtent(env);
	}

	private void addPopupWindow() {

		// 泡泡代码
		popView = View.inflate(this, R.layout.paopao, null);
		property = (Button) popView.findViewById(R.id.property);
		statistics = (Button) popView.findViewById(R.id.statistics);
		doc = (Button) popView.findViewById(R.id.doc);
		mLongTouchTitle = (Button) popView.findViewById(R.id.paopao_name);
		property.setOnClickListener(this);
		statistics.setOnClickListener(this);
		doc.setOnClickListener(this);

//		imageAnim = (ImageView) findViewById(R.id.poiAnim);
//		imageAnim.setVisibility(View.INVISIBLE);
		callout = map.getCallout();
		callout.setContent(popView);
		callout.setStyle(R.layout.calloutwindow);
		callout.setMaxWidth(SinoApplication.screenWidth - 10);
		callout.setMaxHeight(SinoApplication.screenHeight);
		map.setClickable(false);// 设置地图可点击

		MarinedbActivity.this.map
				.setOnSingleTapListener(new OnSingleTapListener() {// 单击事件
					@Override
					public void onSingleTap(float x, float y) {
						if(mFragmentLayout.getVisibility() == View.VISIBLE){
							//显示的时候不相应，防止穿透
							return;
						}
						
						if (callout.isShowing()){
							callout.hide();
						}
						//高亮消失
						mDrawLayer4HighLight.removeAll();
					}
				});

		MarinedbActivity.this.map
				.setOnLongPressListener(new OnLongPressListener() {
					public void onLongPress(float x, float y) {
						if (!map.isLoaded() || (mFragmentLayout.getVisibility() == View.VISIBLE)) {
							return;
						}
						isHideCallout = false;
						
						//TODO:清空之前高亮显示
						drawLayer.removeAll();
						mDrawLayer4HighLight.removeAll();
						drawTool.deactivate();
						
						Point pt = MarinedbActivity.this.map.toMapPoint(x, y);
						Log.d("map", "-----长按------Long--x:"+x+"  y: "+y+" -toMapPoint--x:"+pt.getX()+"  y: "+pt.getY());
						x1 = pt.getX();
						y1 = pt.getY();
						name2 = "未知地名";
						address2 = "未知地址";
						//TODO:找到对象的属性等数据 url
						
						try {
							Point pxPt = MarinedbActivity.this.map
									.toScreenPoint(new Point(x1, y1));
							int pxx = 0;
							int pxy = 0;

							pxx = (int) (pxPt.getX()); // 图标宽度(像素)
							pxy = (int) (pxPt.getY()); // 图标高度(像素)
							anchorPt = MarinedbActivity.this.map.toMapPoint(
									pxx, pxy);
							
//							imageAnim.startAnimation(poiAnimation);
							
							Log.d(tag, "-------长按   x: "+anchorPt.getX()+"  y: "+anchorPt.getY());
							initSearchParams(anchorPt);
							SearchIdentifyTask task = new SearchIdentifyTask(mContext, pt, SinoApplication.oilUrl, mLongTouchTitle, CommonData.TypeOperateLongPress, mDrawLayer4HighLight);
						    task.execute(mIdentifyParameters); 
							
							if (LongPressPOI != null) {
								MarinedbActivity.this.gLayer
										.removeGraphic(LongPressPOI.getUid());
							}

						} catch (Exception e) {
							e.printStackTrace();
						}
						
						callout.setCoordinates(anchorPt);
						if (!callout.isShowing()) {
							callout.show();
						}
					}
				});
	}

	private MenuButton mMenuViewCompare;
	private MenuButton mMenuViewCount;
	private MenuButton mMenuViewMine;
	private MenuButton mMenuViewTool;
	private MenuButton mMenuViewSearch;
	private ListView mListView;
	private ViewGroup mSearchViewGroup;
	/**
	 * 底部五个按钮跟布局
	 */
	private ViewGroup mToolBar;
	
	private void initView() {
		mSearchViewGroup = (ViewGroup) findViewById(R.id.search_listview_layout);
		mListView = (ListView) findViewById(R.id.search_listview);
		mBaseLayout = (ViewGroup) findViewById(R.id.baselayout);
		mLeftBarLayout = (ViewGroup) findViewById(R.id.tool_bar);
		mFragmentLayout = (ViewGroup) findViewById(R.id.fragmentlayout);
		mToolBar = (ViewGroup) findViewById(R.id.menu_bar);

	
		mGridViewLayout = (RelativeLayout) findViewById(R.id.menu_children_grid);
		mGridView = (GridView) findViewById(R.id.menu_gridview);
		mGridView.setOnItemClickListener(this);
		// mGridView.setStretchMode(GridView.NO_STRETCH);

		initGridViewData();
//		mProgressBar = (ProgressBar) findViewById(R.id.progressbar);
		mTVContent = (TextView) findViewById(R.id.tv_content);
		mTVContent.setMovementMethod(ScrollingMovementMethod.getInstance());
		mBtnLayer = (Button) findViewById(R.id.btn_map_layout);
		mBtnLayer.setOnClickListener(this);
		mBtnSearch = (MenuButtonNoIcon) findViewById(R.id.btn_search_confirm);
		mBtnSearch.setOnClickListener(this);

		mEditText = (Button) findViewById(R.id.edittext_search);
		mEditText.setOnClickListener(this);
		mMenuViewTool = (MenuButton) findViewById(R.id.menuview_tool);
		mMenuViewTool.setOnClickListener(this);
		
		mMenuViewSearch = (MenuButton) findViewById(R.id.menuview_search);
		mMenuViewSearch.setOnClickListener(this);

		mMenuViewCount = (MenuButton) findViewById(R.id.menuview_count);
		mMenuViewCount.setOnClickListener(this);
		
		mMenuViewCompare = (MenuButton) findViewById(R.id.menuview_compare);
		mMenuViewCompare.setOnClickListener(this);
		
		mMenuViewMine = (MenuButton) findViewById(R.id.menuview_mine);
		mMenuViewMine.setOnClickListener(this);

		mBtnCancelChoose = (Button) findViewById(R.id.tb_cancel_chooise);
		mBtnCancelChoose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(mLeftBarLayout.getVisibility() == View.GONE){
					mLeftBarLayout.startAnimation(aniIn);
					mLeftBarLayout.setVisibility(View.VISIBLE);
				}
				mTag4OperateInLine = false;
				btnMultiple.setSelected(false);
				btnFrame.setSelected(false);
				btnLine.setSelected(false);
				btnPolygon.setSelected(false);
				btnCurScreen.setSelected(false);
				
				
				drawLayer.removeAll();
				mDrawLayer4HighLight.removeAll();
				drawTool.deactivate();
				if (callout.isShowing()) {
					callout.hide();
				}
				
				isHideCallout = true;
				
				//清空上次查询数据
				SinoApplication.mResultList4FrameSearch.clear();
				SinoApplication.mFeatureSet4Query = null;
				mEditText.setText(getString(R.string.search));
				
				if(mToolBar.getVisibility() == View.GONE){
					mToolBar.setVisibility(View.VISIBLE);
					mToolBar.startAnimation(aniUp);
					mGridViewLayout.setVisibility(View.GONE);
				}
			}
		});

		mBtnScaleSmall = (ImageButton) findViewById(R.id.btn_scale_small);
		mBtnScaleSmall.setOnClickListener(this);
		mBtnScaleBig = (ImageButton) findViewById(R.id.btn_scale_big);
		mBtnScaleBig.setOnClickListener(this);
	}
	
//	public class MenuTouchListener implements OnTouchListener {
//
//		@Override
//		public boolean onTouch(View v, MotionEvent event) {
//		
//		if (event.getAction() == MotionEvent.ACTION_DOWN) {	
//			switch (v.getId()) {
//			case R.id.menuview_tool:
//				setMenuStatus("tool",v);
//				break;
//			case R.id.menuview_search:
//				setMenuStatus("search",v);
//				break;
//			case R.id.menuview_count:
//				setMenuStatus("count",v);
//		         break;
//			case R.id.menuview_compare:
//				setMenuStatus("compare",v);
//				break;
//			case R.id.menuview_mine:
//				setMenuStatus("mine",v);
//				break;
//			default:
//				break;
//			} 
//		}
//		  hideCallOut();
//			return false;
//		}
//
//	
//	}
	
//	private void setMenuStatus(String key,View v) {
//		for (View selected:menuStatus.values()) {
//			
//			  selected.setSelected(false);
//		}
//	 	 v.setSelected(true);
//		 menuStatus.put(key, v);
//	}

	
	public void hideInput(){
		if(getCurrentFocus()!=null)
		{
			((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)) .hideSoftInputFromWindow(getCurrentFocus()
					.getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS); 
		}
	}
	
	private ArrayList<HashMap<String, Object>> mList = new ArrayList<HashMap<String, Object>>();
	private SearchAdapter mSearchAdapter;
	/**
	 * 初始化搜索结果列表数据
	 */
	private void initData() {
		if(SinoApplication.mOilGasData != null){
			SinoApplication.mOilGasData.clear();
		}
		String[] urls = getResources().getStringArray(R.array.oilgas_url_theme);
		String[] urls_4search = getResources().getStringArray(R.array.oilgas_url_theme_4search);
		String[] ids = getResources().getStringArray(R.array.all_layer_id);
		String[] names = getResources().getStringArray(R.array.all_layer_name);
		String[] colors = getResources().getStringArray(R.array.all_layer_color);
//		Log.d(tag, "- ********* --图层专题  url 数目 --main : "+urls.length);
		for (int i = 0; i < urls.length; i++) {
			OilGasData data = new OilGasData();
			data.setUrl(urls[i]);
			data.setSearchUrl(urls_4search[i]);
			data.setName(names[i]);
			data.setId(Integer.valueOf(ids[i]));
			Log.v("mandy", "colors: " + colors[i]);
			data.setColor(Color.parseColor(colors[i]));
			if(getString(R.string.url_basin).equals(urls[i])){
				data.setChecked(true);
			}else{
				data.setChecked(false);
			}
			data.setVisible(true);
			SinoApplication.mOilGasData.add(data);
			
		}
		
		mSearchAdapter = new SearchAdapter(mContext, mList);
		mListView.setAdapter(mSearchAdapter);
	}

	private int mChildMenuSplitNumber = 12;

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();
		map.pause();
	}
	@Override
	protected void onResume() {
		super.onResume();
//		Log.d(tag, "main----------------------onResume");
		map.unpause();
		closeKeyboard();
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				btnPolygon.getLocationOnScreen(mLocation4Polygon);
				btnLine.getLocationOnScreen(mLocation4Line);
				mMenuBtnWidth = btnPolygon.getWidth();
				Log.d("pop", "延迟：  px: " + mLocation4Polygon[0] + "  py: "
						+ mLocation4Polygon[1] + "  mMenuBtnWidth: "
						+ mMenuBtnWidth + "  ly: " + mLocation4Line[1]);
			}
		}, 500);
	
	}

	LayerDialog mLayerDialog;
	
	/**
	 * 油气藏类型
	 */
	private String mTopicType;
	/**
	 * 宝藏名字
	 */
	private String mTopicName;
	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
//		if(mFragmentLayout.getVisibility() == View.VISIBLE){
//			//显示的时候不相应，防止穿透
//			return;
//		}
		Boolean[] clickTag;
		
		switch (v.getId()) {
		case R.id.property:
			launchNewPage("属性");
			break;
		case R.id.statistics:
			launchNewPage("统计");
			break;
		case R.id.doc:
			Toast.makeText(mContext, "此功能暂未实现...", Toast.LENGTH_SHORT).show();
			break;
		case R.id.btn_map_layout: //图层
			hideCallOut();
			if(mLayerDialog == null){
				mLayerDialog = new LayerDialog();
			}
			mLayerDialog.setMapView(map);
			mLayerDialog.setMapServiceLayer(tms);
			mLayerDialog.setDrawLayer(drawLayer);
			mLayerDialog.show(getFragmentManager(), "dialog");
			
			break;
		case R.id.tb_frame: //框选
			 drawTool.activate(DrawTool.ENVELOPE);
			 drawLayer.removeAll();
			 mTag4ToolAreaOk = true;
			 mTag4ToolDistanceOk = false;
			 setButtonsStatus(v.getId());
			break;
		case R.id.tb_line:
			drawTool.activate(DrawTool.POLYLINE);
			drawLayer.removeAll();
			setButtonsStatus(v.getId());
			 mTag4ToolAreaOk = false;
			 mTag4ToolDistanceOk = true;
			break;
		case R.id.tb_polygon:
			ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
			String[] name = { "任意点绘制", "点绘制", "圆形" };
			String[] tag = { "anyPoint", "points", "cycle" };
			for (int i = 0; i < name.length; i++) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("name", name[i]);
				map.put("tag", tag[i]);
				list.add(map);
			}
			showWindow(btnPolygon, list, mLocation4Polygon);
			break;
		case R.id.tb_cur_screen:
			 //屏幕范围查询
			drawTool.queryAttribute4Query(map.getExtent());
			setButtonsStatus(v.getId());
			 mTag4ToolAreaOk = true;
			 mTag4ToolDistanceOk = false;
			break;
		case R.id.tb_multiple:
			//TODO:多选,进入多
			SinoApplication.mResultListMulti.clear();
			drawTool.activate(DrawTool.MULTI_POINT);
			setButtonsStatus(v.getId());//TODO:多选,进入多
			break;
		case R.id.btn_scale_big:
			map.zoomin();
			break;
		case R.id.btn_scale_small:
			map.zoomout();
			break;
		case R.id.edittext_search:
			searchFragment = new SearchFragment(this);
			mFragmentLayout.setVisibility(View.VISIBLE);
			fragmentManager.beginTransaction().replace(R.id.fragmentlayout, searchFragment).commit();
			break;
		case R.id.menuview_tool:
			 clickTag = new Boolean[] { false, false };
			if(mTag4OperateInLine){
				clickTag = new Boolean[] { true, false };
			}else if(mTag4ToolDistanceOk){
				clickTag = new Boolean[] { true, false };
			}else if(mTag4ToolAreaOk){
				clickTag = new Boolean[] { false, true };
			}
			ChildrenMenuDataUtil.setToolChildrenMenuData(toolist, clickTag, mChildMenuSplitNumber);
			mGridView.setNumColumns(2);
			setGridView(toolist, v);
			
			break;
		case R.id.menuview_search:
			 clickTag = new Boolean[] { true, true, true, true, true, true, true };
			ChildrenMenuDataUtil.setSearchChildrenMenuData(toolist, clickTag, mChildMenuSplitNumber);
			mGridView.setNumColumns(7);
			setGridView(toolist, v);
			
			break;
		case R.id.menuview_compare:
			if(SinoApplication.mResultList4Compared == null || SinoApplication.mResultList4Compared.size() == 0){
				Toast.makeText(mContext, getString(R.string.tip_no_objects), Toast.LENGTH_SHORT).show();
			}else{
				showWindow4Compared(SinoApplication.mResultList4Compared);
			}
			break;
		case R.id.menuview_count:
			 clickTag = new Boolean[] { true, true,};
			ChildrenMenuDataUtil.setCountChildrenMenuData(toolist, clickTag, mChildMenuSplitNumber);
			mGridView.setNumColumns(2);
			setGridView(toolist, v);
			
			break;
		case R.id.menuview_mine:
			clickTag = new Boolean[] { true, true, true,true, true };
			ChildrenMenuDataUtil.setMineChildrenMenuData(toolist, clickTag, mChildMenuSplitNumber);
			mGridView.setNumColumns(5);
			setGridView(toolist, v);
			
			break;
		default:
			break;
		}
	}
	
	private void launchNewPage (String name) {
		
		Intent intent = new Intent(this, SelectActivity.class);
		intent.putExtra(CommonData.KeyTopicType, mTopicType);
		intent.putExtra("name", name);
		startActivity(intent);
	}
	
	
	/**
	 * 是否选择了折线
	 */
	private boolean mTag4OperateInLine = false;
	private void setButtonsStatus(int vId) {
		mLastClickedView = null;
		if (btnFrame.getId() == vId) {
			mTag4OperateInLine = false;
			if(btnFrame.isSelected()){
				btnFrame.setSelected(false);
			}else{
				btnFrame.setSelected(true);
			}
			btnMultiple.setSelected(false);
			btnLine.setSelected(false);
			btnPolygon.setSelected(false);
			btnCurScreen.setSelected(false);
		} else if (btnLine.getId() == vId) {
			mTag4OperateInLine = true;
			if(btnLine.isSelected()){
				btnLine.setSelected(false);
			}else{
				btnLine.setSelected(true);
			}
			btnMultiple.setSelected(false);
			btnFrame.setSelected(false);
			btnPolygon.setSelected(false);
			btnCurScreen.setSelected(false);
		} else if (btnPolygon.getId() == vId) {
			mTag4OperateInLine = false;
			if(btnPolygon.isSelected()){
				btnPolygon.setSelected(false);
			}else{
				btnPolygon.setSelected(true);
			}
			btnMultiple.setSelected(false);
			btnFrame.setSelected(false);
			btnLine.setSelected(false);
			btnCurScreen.setSelected(false);
		} else if (btnCurScreen.getId() == vId) {
			mTag4OperateInLine = false;
			if(btnCurScreen.isSelected()){
				btnCurScreen.setSelected(false);
			}else{
				btnCurScreen.setSelected(true);
			}
			btnMultiple.setSelected(false);
			btnFrame.setSelected(false);
			btnPolygon.setSelected(false);
			btnLine.setSelected(false);
		} else if (btnMultiple.getId() == vId) {
			mTag4OperateInLine = false;
			if(btnMultiple.isSelected()){
				btnMultiple.setSelected(false);
			}else{
				btnMultiple.setSelected(true);
			}
			btnFrame.setSelected(false);
			btnPolygon.setSelected(false);
			btnLine.setSelected(false);
			btnCurScreen.setSelected(false);
		}

		showCancelButton();
		hideCallOut();
	}
	
	//测面积可用
	private boolean mTag4ToolAreaOk = false;
	//测距可用
	private boolean mTag4ToolDistanceOk = false;

	//设置底部按钮的点击效果
	private void setMenuButtonsStatus(int vId) {
		if (mMenuViewTool.getId() == vId) {
			dealClickRepeat(mMenuViewTool);
			
			mMenuViewCount.setSelected(false);
			mMenuViewSearch.setSelected(false);
			mMenuViewCompare.setSelected(false);
			mMenuViewMine.setSelected(false);
		} else if (mMenuViewCount.getId() == vId) {
			dealClickRepeat(mMenuViewCount);
			
			mMenuViewTool.setSelected(false);
			mMenuViewSearch.setSelected(false);
			mMenuViewCompare.setSelected(false);
			mMenuViewMine.setSelected(false);
		} else if (mMenuViewSearch.getId() == vId) {
			dealClickRepeat(mMenuViewSearch);
		
			mMenuViewTool.setSelected(false);
			mMenuViewCount.setSelected(false);
			mMenuViewCompare.setSelected(false);
			mMenuViewMine.setSelected(false);
		} else if (mMenuViewCompare.getId() == vId) {
			dealClickRepeat(mMenuViewCompare);
		
			mMenuViewTool.setSelected(false);
			mMenuViewSearch.setSelected(false);
			mMenuViewCount.setSelected(false);
			mMenuViewMine.setSelected(false);
		} else if (mMenuViewMine.getId() == vId) {
			dealClickRepeat(mMenuViewMine);
	
			mMenuViewTool.setSelected(false);
			mMenuViewSearch.setSelected(false);
			mMenuViewCount.setSelected(false);
			mMenuViewCompare.setSelected(false);
			
		}
		hideCallOut();
	}
	
	private void dealClickRepeat(View view) {
		if(view.isSelected()){
			view.setSelected(false);
		}else{
			view.setSelected(true);
		}
	}

	private View view;
	private View mLastClickedView;
	private ArrayList<HashMap<String, Object>> mList4Menu = new ArrayList<HashMap<String, Object>>();
	private ListView mMenuListView;

	private void initPopWindowData(ArrayList<HashMap<String, Object>> list) {
		mMenuAdapter = new MenuAdapter(mContext, list);
		mMenuListView.setAdapter(mMenuAdapter);
		mMenuListView.setOnItemClickListener(this);
	}

	private MenuAdapter mMenuAdapter;
	private int[] mLocation4Line = new int[2];
	private int[] mLocation4Polygon = new int[2];
	private int mMenuBtnWidth = 0;

	private void showWindow(View view, ArrayList<HashMap<String, Object>> list,
			int[] location) {
		// if (popupWindow == null) {
		LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		view = layoutInflater.inflate(R.layout.view_menu_popwindow, null);
		// 创建一个PopuWidow对象
		popupWindow = new PopupWindow(view, 400, 300);
		// }

		mMenuListView = (ListView) view.findViewById(R.id.menu_listview);
		initPopWindowData(list);
		// 已经定义好布局，怕破坏掉样式，只需要设置一个空的Drawable即可
		popupWindow.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.popup_bg));

		// 使其聚集
		popupWindow.setFocusable(true);
		// 设置允许在外点击消失
		popupWindow.setOutsideTouchable(true);

		int popx = location[0] + mMenuBtnWidth;
		int popy = location[1];
		// popupWindow.showAsDropDown(view, popx, 0);
		popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, popx, popy);
		// popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, 200,
		// SinoApplication.screenHeight / 2);
	}
	
	private void showWindow4Compared(ArrayList<IdentifyResult> list) {
		LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mBaseLayout = (ViewGroup) layoutInflater.inflate(R.layout.view_menu_popwindow, null);
		popupWindow = new PopupWindow(mBaseLayout, 1000, 800);
		
		SinoUtil.showWindow4Compared(mContext, popupWindow, mBaseLayout, list);
//		popupWindow.showAtLocation(mBaseLayout, Gravity.NO_GRAVITY, 0, 0);
	}

	// 关闭软键盘
	private void closeKeyboard() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
	}

	private MenuGridAdapter mAdapter;
	

	private void initGridViewData() {
		mAdapter = new MenuGridAdapter(mContext, toolist);
		mGridView.setAdapter(mAdapter);
	}

	private void setGridView(ArrayList<HashMap<String, Object>> list, View view) {
		setMenuButtonsStatus(view.getId());
		mAdapter.notifyDataSetChanged();
		// showAndHideGridView();
		if (mLastClickedView == view) {
			mGridViewLayout.setVisibility(View.INVISIBLE);
			mLastClickedView = null;
			Log.d(tag, "is view "+ mLastClickedView);
		} else {
			mGridViewLayout.setVisibility(View.VISIBLE);
			mLastClickedView = view;
			Log.d(tag, "not view "+ mLastClickedView);
		}
		// else
		// mLastClickedView = null;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		mLastClickedView = null;
		HashMap<String, Object> map = (HashMap<String, Object>) arg0
				.getAdapter().getItem(position);
		String tag = (String) map.get("tag");
		HashMap<String, Boolean> showMap = (HashMap<String, Boolean>) map.get("clicktag");
		if(showMap != null && !showMap.get(tag)){
			return;
		}
		
		Log.d("sinopec", "-------点击p: " + position + "  tag: " + tag);
		if ("toolDistance".equals(tag)) {
//			ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
//			String[] name = {"KM", "M"};
//			for (int i = 0; i < name.length; i++) {
//				HashMap<String, Object> distancMap = new HashMap<String, Object>();
//				distancMap.put("name", name[i]);
//				distancMap.put("tag", name[i]);
//				list.add(distancMap);
//			}
//			// 创建一个PopuWidow对象
//			LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//			mBaseLayout = (ViewGroup) layoutInflater.inflate(R.layout.view_menu_popwindow, null);
//			popupWindow = new PopupWindow(mBaseLayout, 600, 400);
//			
//			SinoUtil.showWindow(mContext, popupWindow, mBaseLayout, mMenuListView, mMenuAdapter, this, list);
			
			hidePopupWindow4CountDistanceArea();
			drawTool.calculateAreaAndLength("KM");
		} else if ("toolArea".equals(tag)) {
			//调用drawTool里面 的一个变量
			drawTool.calculateAreaAndLength("");
		} else if ("定制查询".equals(tag)) {
			//TODO:
			ConditionQuery query = new ConditionQuery();
			query.show(getFragmentManager(), ConditionQuery.class.getName());
			
		} else if ("mineLogin".equals(tag)) {
			Intent intent = new Intent(mContext, LoginActivity.class);
			startActivity(intent);
		} else if ("mineLogout".equals(tag)) {
			exitDialog();
		} else if ("mineLogout".equals(tag)) {
			exitDialog();
		} else if ("KM".equals(tag)) {
			// 折线长度以km显示
			hidePopupWindow4CountDistanceArea();
			drawTool.calculateAreaAndLength("KM");
			mBtnCancelChoose.performClick();
		} else if ("M".equals(tag)) {
			// 折线长度以m显示
			hidePopupWindow4CountDistanceArea();
			drawTool.calculateAreaAndLength("M");
			mBtnCancelChoose.performClick();
		} else if ("anyPoint".equals(tag)) {
			drawTool.activate(DrawTool.ANY_POLYGON);
			// 多边形任意点绘制
			hidePopupWindow();
			setButtonsStatus(btnPolygon.getId());
			 mTag4ToolAreaOk = true;
			 mTag4ToolDistanceOk = false;
		} else if ("points".equals(tag)) {

			drawTool.activate(DrawTool.POLYGON);
			drawLayer.removeAll();
			// 多边形点绘制
			hidePopupWindow();
			setButtonsStatus(btnPolygon.getId());
			 mTag4ToolAreaOk = true;
			 mTag4ToolDistanceOk = false;
		} else if ("cycle".equals(tag)) {
			drawTool.activate(DrawTool.CIRCLE);
			drawLayer.removeAll();
			// 多边形任意点绘制
			hidePopupWindow();
			setButtonsStatus(btnPolygon.getId());
			 mTag4ToolAreaOk = true;
			 mTag4ToolDistanceOk = false;
		} else if ("CountChildrenMenuOne".equals(tag)) {
			//TODO:统计二级菜单
			Boolean[] clickTag = new Boolean[] { true, true,true, true };
			ChildrenMenuDataUtil.setCountLevelTwoChildrenMenuOneData(toolist, clickTag, mChildMenuSplitNumber);
			mGridView.setNumColumns(4);
			setGridView4LevelTwoChildrenMenu(toolist, arg0);
		} else if ("CountChildrenMenuTwo".equals(tag)) {
			Boolean[] clickTag = new Boolean[] { true, true,true, true, true, true, true, true, true, true, true, };
			ChildrenMenuDataUtil.setCountLevelTwoChildrenMenuData(toolist, clickTag, mChildMenuSplitNumber);
			mGridView.setNumColumns(11);
			setGridView4LevelTwoChildrenMenu(toolist, arg0);
		} else if ("碳酸盐岩烃源分布".equals(tag)) {
			Boolean[] clickTag = new Boolean[] { true, true,true, true, true, true, true, true, true, true,};
			ChildrenMenuDataUtil.setSearchChildren4MenuData(toolist, clickTag, mChildMenuSplitNumber);
			mGridView.setNumColumns(10);
			setGridView4LevelTwoChildrenMenu(toolist, arg0);
		} else if ("分类型盖层分布".equals(tag)) {
			Boolean[] clickTag = new Boolean[] { true, true,true };
			ChildrenMenuDataUtil.setSearchChildren6MenuData(toolist, clickTag, mChildMenuSplitNumber);
			mGridView.setNumColumns(3);
			setGridView4LevelTwoChildrenMenu(toolist, arg0);
		}
		
		//三级子菜单都需要在这里处理
		if(!"CountChildrenMenuOne".equals(tag) && !"CountChildrenMenuTwo".equals(tag) && !"toolDistance".equals(tag)
				&& !"碳酸盐岩烃源分布".equals(tag) && !"分类型盖层分布".equals(tag) && !"toolArea".equals(tag)){
			hideCallOut();
		}
	}
	
	private void setGridView4LevelTwoChildrenMenu(ArrayList<HashMap<String, Object>> list, View view) {
//		setMenuButtonsStatus(view.getId());
		mAdapter.notifyDataSetChanged();
		mToolBar.setVisibility(View.GONE);
		mToolBar.startAnimation(aniDown);
		mGridViewLayout.setVisibility(View.VISIBLE);
		
		// showAndHideGridView();
//		if (mLastClickedView == view) {
//			mGridViewLayout.setVisibility(View.INVISIBLE);
//			mLastClickedView = null;
//			Log.d(tag, "is view "+ mLastClickedView);
//		} else {
//			mGridViewLayout.setVisibility(View.VISIBLE);
//			mLastClickedView = view;
//			Log.d(tag, "not view "+ mLastClickedView);
//		}
		
//		if (mLastClickedView == view) {
//			mGridViewLayout.setVisibility(View.INVISIBLE);
//		} else {
//			mGridViewLayout.setVisibility(View.VISIBLE);
//		}
	}

	private void hidePopupWindow() {
		if (popupWindow != null){
			popupWindow.dismiss();
		}
		
		if (mGridViewLayout.getVisibility() == View.VISIBLE) {
			mGridViewLayout.setVisibility(View.INVISIBLE);
		}
		
		mMenuViewTool.setSelected(false);
		mMenuViewSearch.setSelected(false);
		mMenuViewCount.setSelected(false);
		mMenuViewCompare.setSelected(false);
		mMenuViewMine.setSelected(false);
	}
	
	private void hidePopupWindow4CountDistanceArea() {
		if((!mTag4ToolDistanceOk && !mTag4ToolAreaOk)){
			//都不可用的时候
			if (popupWindow != null){
				popupWindow.dismiss();
			}
		}else{
			if(mTag4ToolDistanceOk || mTag4ToolAreaOk ){
				
			}else{
				
			}
		}
		
	}

	class MapTouchListener extends MapOnTouchListener {

		public MapTouchListener(Context context, MapView view) {
			super(context, view);

		}
		 @Override
		 public boolean onTouch(View v, MotionEvent event) {
//			 Log.d(tag, "----MapTouchListener---onTouch--- ");
			 if(mGridViewLayout.getVisibility() == View.VISIBLE)
					mGridViewLayout.setVisibility(View.GONE);
		    return super.onTouch(v, event);
		 }
		 
		 @Override
		public boolean onDoubleTap(MotionEvent point) {
			 Log.d(tag, "----MapTouchListener---onDoubleTap--- ");
			if(mFragmentLayout.getVisibility() == View.VISIBLE){
				//显示的时候不相应，防止穿透
				return false;
			}
			return super.onDoubleTap(point);
		}
		 
		 @Override
		public boolean onDragPointerUp(MotionEvent from, MotionEvent to) {
			return super.onDragPointerUp(from, to);
		}

	}
	private void exitDialog() {
		final AlertDialog dlg = new AlertDialog.Builder(this).create();
		dlg.setTitle("是否退出?");
		dlg.show();
		Window window = dlg.getWindow();
		// *** 主要就是在这里实现这种效果的.
		// 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
		window.setContentView(R.layout.alert_exit_dialog);
		// 为确认按钮添加事件,执行退出应用操作
		Button ok = (Button) window.findViewById(R.id.btn_ok);
		ok.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				SinoApplication.mOilGasData.clear();
				finish(); // 退出应用...
			}
		});

		// 关闭alert对话框架
		Button cancel = (Button) window.findViewById(R.id.btn_cancel);
		cancel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dlg.cancel();
			}
		});
	}

	private void hideCancelButton() {
		if (mBtnCancelChoose.getVisibility() == View.VISIBLE) {
			mBtnCancelChoose.setVisibility(View.GONE);
		}
	}

	private void showCancelButton() {
		if (mBtnCancelChoose.getVisibility() == View.GONE) {
			mBtnCancelChoose.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onBackPressed() {
		// super.onBackPressed();
		if(mFragmentLayout.getVisibility() == View.VISIBLE){
			mFragmentLayout.setVisibility(View.GONE);
		}else{
			exitDialog();
		}
	}

	
	@Override
	public void handleDrawEvent(DrawEvent event) {
		this.drawLayer.addGraphic(event.getDrawGraphic());
	}

	@Override
	public void clear() {
		drawLayer.removeAll();
	}
	
	private SearchFragment searchFragment;
	private FragmentManager fragmentManager;
	private HashMap<String, Object> mHashMap4Property = new HashMap<String, Object>();
	
	@Override
	public void setData(Object data) {
		closeKeyboard();
		if(data == null){
			return;
		}
		FindResult result = (FindResult)data; 
		
		Set<Entry<String, Object>> ents = result.getAttributes().entrySet();
		Log.d("data", "setData4Frame  获得图层 id : "+result.getLayerId()+" name: "+result.getLayerName());
		for (Entry<String, Object> ent : ents) {
			Log.d("data", "模糊查询  key: "+ent.getKey()+"  val: "+ent.getValue());
		}
		
		SinoApplication.findResult = result;
		Geometry geometry = result.getGeometry();
		
        
		Envelope envelope = new Envelope();
		geometry.queryEnvelope(envelope);
		Point point = envelope.getCenter();
//		Log.d(tag, "-------查询出的坐标  x: "+point.getX()+"  y: "+point.getY());
		map.zoomToScale(point, 5000000);

		//绘制高亮区域
		SimpleFillSymbol resultSymbol = new SimpleFillSymbol(Color.YELLOW);
		Graphic resultLocation = new Graphic(geometry, resultSymbol);
		mDrawLayer4HighLight.addGraphic(resultLocation);
		
		mTopicType = result.getLayerName();
		mTopicName = result.getValue();
		mHashMap4Property = (HashMap<String, Object>) result.getAttributes();
		if (callout.isShowing()) {
			callout.hide();
		}
		Log.d(tag, "-------------main---------setData: "+result.getLayerName());
		if(!"chinapoi".equals(result.getLayerName()) && !"国家".equals(result.getLayerName())){
			callout.show(point);
			mLongTouchTitle.setText(result.getValue());
	//		statistics.setText(result.getValue());
		}

		hideInput();
		fragmentManager.beginTransaction().remove(searchFragment).commit();
		searchFragment = null;
		mFragmentLayout.setVisibility(View.GONE);
	}
	
	/**
	 * 因此蓝色球体
	 */
	private void hideCallOut() {
		if(callout != null){
			if (callout.isShowing()) {
				callout.hide();
			}
		}

		mDrawLayer4HighLight.removeAll();
		if(mGridViewLayout.getVisibility() == View.VISIBLE){
			mGridViewLayout.setVisibility(View.GONE);
			//底下四个按钮置为常态
			mMenuViewTool.setSelected(false);
			mMenuViewCount.setSelected(false);
			mMenuViewSearch.setSelected(false);
			mMenuViewCompare.setSelected(false);
			mMenuViewMine.setSelected(false);
		}
	}
	
	//框选查询返回的数据
	@Override
	public void setSearchData(ArrayList<IdentifyResult> list) {
		//搜索按钮显示提示（点击查看详情）
		if(list != null){
			SinoApplication.mResultList4FrameSearch = list;
			int size = list.size();
			if(size == 0){
				SinoApplication.mLayerName = "";
				Toast.makeText(mContext, getString(R.string.search_no_result) , Toast.LENGTH_LONG).show();
			}else{
				SinoApplication.mLayerName = list.get(0).getLayerName();
				mEditText.setText("在 ["+list.get(0).getLayerName()+"] 图层查询到 "+size+" 个 结果，点击进入详情页面。");
				mLeftBarLayout.startAnimation(aniOut);
				mLeftBarLayout.setVisibility(View.GONE);
				SinoApplication.mResultList4Compared = list;
			}
		}else{
			SinoApplication.mLayerName = "";
			Toast.makeText(mContext, getString(R.string.search_no_result) , Toast.LENGTH_LONG).show();
		}
		//
	}

	@Override
	public void setData4Frame(Object data) {
		drawLayer.removeAll();
		IdentifyResult result = (IdentifyResult)data; 
		SinoApplication.identifyResult = result;
		Geometry geometry = result.getGeometry();
		
        
		Envelope envelope = new Envelope();
		geometry.queryEnvelope(envelope);
		Point point = envelope.getCenter();
		map.zoomToScale(point, 5000000);

		//绘制高亮区域
		SimpleFillSymbol resultSymbol = new SimpleFillSymbol(Color.YELLOW);
		Graphic resultLocation = new Graphic(geometry, resultSymbol);
		mDrawLayer4HighLight.addGraphic(resultLocation);
		
		mTopicType = result.getLayerName();
//		mTopicName = getIdentifyResultName(result);
		mHashMap4Property = (HashMap<String, Object>) result.getAttributes();
		if (callout.isShowing()) {
			callout.hide();
		}
		callout.show(point);
		Set<Entry<String, Object>> ents = result.getAttributes().entrySet();
		Log.d("data", "setData4Frame  获得图层 id : "+result.getLayerId()+" name: "+result.getLayerName());
		for (Entry<String, Object> ent : ents) {
			Log.d("data", "井  key: "+ent.getKey()+"  val: "+ent.getValue());
		}
		
		mLongTouchTitle.setText(SinoApplication.getIdentifyResultNameByType(result, result.getLayerName()));
//		statistics.setText(result.getValue());

		hideInput();
		fragmentManager.beginTransaction().remove(searchFragment).commit();
		searchFragment = null;
		mFragmentLayout.setVisibility(View.GONE);
	}

	@Override
	public void setSearchData4Query(FeatureSet results) {
		if(results != null){
			SinoApplication.mFeatureSet4Query = results;
//			SinoApplication.mResultList4FrameSearch = list;
			int size = results.getGraphics().length;
			if(size == 0){
				SinoApplication.mLayerName = "";
				Toast.makeText(mContext, getString(R.string.search_no_result) , Toast.LENGTH_LONG).show();
			}else{
//				SinoApplication.mLayerName = list.get(0).getLayerName();
				mEditText.setText("在 ["+SinoApplication.mLayerName+"] 图层查询到 "+size+" 个 结果，点击进入详情页面。");
				mLeftBarLayout.startAnimation(aniOut);
				mLeftBarLayout.setVisibility(View.GONE);
			}
		}else{
			SinoApplication.mLayerName = "";
			Toast.makeText(mContext, getString(R.string.search_no_result) , Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void setData4Query(Object data) {
		Graphic graphic = (Graphic)data; 
//		FindResult result = (FindResult)data; 
		
		Set<Entry<String, Object>> ents = graphic.getAttributes().entrySet();
		for (Entry<String, Object> ent : ents) {
			Log.d("data", "框选查询 setData4Query  key: "+ent.getKey()+"  val: "+ent.getValue());
		}
		
//		SinoApplication.findResult = result;
		Geometry geometry = graphic.getGeometry();
		
        
		Envelope envelope = new Envelope();
		geometry.queryEnvelope(envelope);
		Point point = envelope.getCenter();
		map.zoomToScale(point, 5000000);

		//绘制高亮区域
		SimpleFillSymbol resultSymbol = new SimpleFillSymbol(Color.YELLOW);
		Graphic resultLocation = new Graphic(geometry, resultSymbol);
		mDrawLayer4HighLight.addGraphic(resultLocation);
		
		mTopicType = SinoApplication.mLayerName;
//		mTopicName = result.getValue();
		mHashMap4Property = (HashMap<String, Object>) graphic.getAttributes();
		if (callout.isShowing()) {
			callout.hide();
		}
		Log.d(tag, "-------------setData4Query---------setData: "+SinoApplication.mLayerName);
		callout.show(point);
		mLongTouchTitle.setText((String) graphic.getAttributes().get("OBJ_NAME_C"));
	//		statistics.setText(result.getValue());

		hideInput();
		fragmentManager.beginTransaction().remove(searchFragment).commit();
		searchFragment = null;
		mFragmentLayout.setVisibility(View.GONE);
		
	}
}
