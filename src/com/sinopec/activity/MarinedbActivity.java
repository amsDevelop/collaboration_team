package com.sinopec.activity;

import java.util.ArrayList;
import java.util.HashMap;

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
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
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
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.symbol.TextSymbol;
import com.esri.core.tasks.ags.find.FindResult;
import com.esri.core.tasks.ags.geocode.LocatorGeocodeResult;
import com.esri.core.tasks.ags.identify.IdentifyParameters;
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
import com.sinopec.task.GeocoderTask;
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
	private ImageView imageAnim = null;
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
	/**
	 * 子菜单
	 */
	private GridView mGridView;
	private RelativeLayout mGridViewLayout;
	private GraphicsLayer drawLayer;
	private SimpleFillSymbol fillSymbol;
	private MapTouchListener mapTouchListener;
	private DrawTool drawTool;
	private ArcGISTiledMapServiceLayer tms;
    private ViewGroup mBaseLayout;
    private ViewGroup mFragmentLayout;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.mContext = this;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setTitle(R.string.app_name);
		SinoApplication.screenWidth = this.getWindowManager()
				.getDefaultDisplay().getWidth();
		SinoApplication.screenHeight = this.getWindowManager()
				.getDefaultDisplay().getHeight();
		setContentView(R.layout.activity_main);
		initView();
		map = (MapView) findViewById(R.id.map);
		map.setExtent(new Envelope(-180, -85.46971899999994,
				180,  87.93330000000003), 0);
//		 -86.36436463525025 getY: 40.28780276447719
		map.centerAt(new Point( -86.36436463525025, 40.28780276447719), false);
		map.zoomTo(new Point( -86.36436463525025, 40.28780276447719), 1);
		map.zoomToScale(new Point( -86.36436463525025, 40.28780276447719), 5000000);
		
		tms = new ArcGISTiledMapServiceLayer(
				"http://202.204.193.201:6080/arcgis/rest/services/marine_oil/MapServer");
//				oilUrl);

//		map.addLayer(tms);
		
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
		fLayer = new ArcGISFeatureLayer(
				"http://sampleserver3.arcgisonline.com/ArcGIS/rest/services/Petroleum/KSPetro/MapServer/1",
				o);
		map.addLayer(fLayer);
		drawLayer = new GraphicsLayer();
		map.addLayer(drawLayer);
		initSymbols();// 初始化符号
		// add by gaolixiao
		addPopupWindow();
		getAboutDisplay();
		initLayout();
		drawTool = new DrawTool(map);
		drawTool.addEventListener(this);
		drawTool.setDrawLayer(drawLayer);
		 mapTouchListener = new MapTouchListener(this, map);
		 map.setOnTouchListener(mapTouchListener);
		 map.setOnStatusChangedListener(new MapStatusListener());
		initData();
		searchFragment = new SearchFragment(this);
		fragmentManager = getFragmentManager();
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
		  //设置Identify查询参数
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

		imageAnim = (ImageView) findViewById(R.id.poiAnim);
		imageAnim.setVisibility(View.INVISIBLE);
		callout = map.getCallout();
		callout.setContent(popView);
		callout.setStyle(R.layout.calloutwindow);
		callout.setMaxWidth(SinoApplication.screenWidth - 10);
		callout.setMaxHeight(SinoApplication.screenHeight);

		map.setClickable(true);// 设置地图可点击

		MarinedbActivity.this.map
				.setOnSingleTapListener(new OnSingleTapListener() {// 单击事件
					@Override
					public void onSingleTap(float x, float y) {
						if (callout.isShowing())
							callout.hide();
					}
				});

		MarinedbActivity.this.map
				.setOnLongPressListener(new OnLongPressListener() {
					public void onLongPress(float x, float y) {
						if (!map.isLoaded()) {
							return;
						}
						
						//TODO:清空之前高亮显示
						drawLayer.removeAll();
						drawTool.deactivate();
						
						Point pt = MarinedbActivity.this.map.toMapPoint(x, y);
						x1 = pt.getX();
						y1 = pt.getY();
						name2 = "未知地名";
						address2 = "未知地址";
						//TODO:找到对象的属性等数据 url
						
						try {
//							curX = x1;
//							curY = y1;
//							curname = name2;
//							curadd = address2;
							Point pxPt = MarinedbActivity.this.map
									.toScreenPoint(new Point(x1, y1));
							
							int pxx = 0;
							int pxy = 0;

							pxx = (int) (pxPt.getX()); // 图标宽度(像素)
							pxy = (int) (pxPt.getY()); // 图标高度(像素)
							anchorPt = MarinedbActivity.this.map.toMapPoint(
									pxx, pxy);
							
							Animation poiAnimation = new TranslateAnimation(
									pxx, pxx, 0, y);
							poiAnimation.setDuration(500);
//							imageAnim.startAnimation(poiAnimation);
							
							Log.d(tag, "-------长按   x: "+anchorPt.getX()+"  y: "+anchorPt.getY());
							initSearchParams(anchorPt);
							SearchIdentifyTask task = new SearchIdentifyTask(mContext, pt, SinoApplication.oilUrl, mLongTouchTitle, imageAnim, 
									poiAnimation, CommonData.TypeOperateLongPress, drawLayer);
						    task.execute(mIdentifyParameters); 
							
							
							// 添加图标
							if (LongPressPOI != null) {
								MarinedbActivity.this.gLayer
										.removeGraphic(LongPressPOI.getUid());
							}
							// MarinedbActivity.this.gLayer.postInvalidate();
							poiAnimation
									.setAnimationListener(new AnimationListener() {

										@Override
										public void onAnimationStart(
												Animation animation) {
											// TODO Auto-generated method stub
											imageAnim
													.setVisibility(View.VISIBLE);
										}

										@Override
										public void onAnimationRepeat(
												Animation animation) {
											// TODO Auto-generated method stub

										}

										@Override
										public void onAnimationEnd(
												Animation animation) {
											// TODO Auto-generated method stub
											imageAnim
													.setVisibility(View.INVISIBLE);

											callout.setCoordinates(anchorPt);
											if (!callout.isShowing())
												callout.show();
											callout.refresh();

										}
									});

						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
//						return true;
					}// onLongPress
				});
	}

	private MenuButton mMenuViewCompare;
	private MenuButton mMenuViewCount;
	private MenuButton mMenuViewMine;
	private MenuButton mMenuViewTool;
	private ListView mListView;
	private ViewGroup mSearchViewGroup;
	
	private void initView() {
		mSearchViewGroup = (ViewGroup) findViewById(R.id.search_listview_layout);
		mListView = (ListView) findViewById(R.id.search_listview);
		mBaseLayout = (ViewGroup) findViewById(R.id.baselayout);
		mFragmentLayout = (ViewGroup) findViewById(R.id.fragmentlayout);
//		mToolBar = (LinearLayout) findViewById(R.id.menu_bar);

	
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
		mMenuViewTool.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Boolean[] clickTag = new Boolean[] { true, true, true };
				if(mTag4OperateInLine){
					clickTag = new Boolean[] { true, false, false };
				} 
				ChildrenMenuDataUtil.setToolChildrenMenuData(list, clickTag, mChildMenuSplitNumber);
				mGridView.setNumColumns(3);
				setGridView(list, arg0);
			}
		});

		mMenuViewCount = (MenuButton) findViewById(R.id.menuview_count);
		mMenuViewCount.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Boolean[] clickTag = new Boolean[] { true, true, true,true, true, true  };
				ChildrenMenuDataUtil.setCountChildrenMenuData(list, clickTag, mChildMenuSplitNumber);
				mGridView.setNumColumns(6);
				setGridView(list, arg0);
			}
		});

		mMenuViewCompare = (MenuButton) findViewById(R.id.menuview_compare);
		mMenuViewCompare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Boolean[] clickTag = new Boolean[] { true };
				ChildrenMenuDataUtil.setCompareChildrenMenuData(list, clickTag, mChildMenuSplitNumber);
				mGridView.setNumColumns(1);
				setGridView(list, arg0);
			}
		});

		mMenuViewMine = (MenuButton) findViewById(R.id.menuview_mine);
		mMenuViewMine.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Boolean[] clickTag = new Boolean[] { true, true, true,true, true };
				ChildrenMenuDataUtil.setMineChildrenMenuData(list, clickTag, mChildMenuSplitNumber);
				mGridView.setNumColumns(5);
				setGridView(list, arg0);
			}
		});
		mBtnCancelChoose = (Button) findViewById(R.id.tb_cancel_chooise);
		mBtnCancelChoose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				mTag4OperateInLine = false;
				btnMultiple.setSelected(false);
				btnFrame.setSelected(false);
				btnLine.setSelected(false);
				btnPolygon.setSelected(false);
				btnCurScreen.setSelected(false);
				
				
				drawLayer.removeAll();
				drawTool.deactivate();
				
				if (callout.isShowing()) {
					callout.hide();
				}
			}
		});

		mBtnScaleSmall = (ImageButton) findViewById(R.id.btn_scale_small);
		mBtnScaleSmall.setOnClickListener(this);
		mBtnScaleBig = (ImageButton) findViewById(R.id.btn_scale_big);
		mBtnScaleBig.setOnClickListener(this);
	}
	
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
		String[] urls = getResources().getStringArray(R.array.oilgas_url_theme);
//		Log.d(tag, "- ********* --图层专题  url 数目 --main : "+urls.length);
		for (int i = 0; i < urls.length; i++) {
			OilGasData data = new OilGasData();
			data.setUrl(urls[i]);
			data.setChecked(false);
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
		Log.d(tag, "main----------------------onResume");
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
		
//		Message msg = new Message();
//		msg.what = 1;
//		handler.sendMessageDelayed(msg, 15000);

//		new Thread(new Runnable() {
//
//			@Override
//			public void run() {
//               while (map.isLoaded()) {
//            	   
//            	   
//               }
//				
//			}
//		}).start();

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
		if (property.getId() == v.getId()) {
			Log.v("mandy", "property is clicked");
			Intent intent = new Intent(this, SelectActivity.class);
			intent.putExtra(CommonData.KeyTopicType, mTopicType);
			intent.putExtra(CommonData.KeyTopicName, mTopicName);
			intent.putExtra("name", "属性");
			startActivity(intent);

		} else if (statistics.getId() == v.getId()) {

			Log.v("mandy", "statistics is clicked");
			Intent intent = new Intent(this, SelectActivity.class);
			intent.putExtra(CommonData.KeyTopicType, mTopicType);
			intent.putExtra("name", "统计");
			startActivity(intent);
		} else if (doc.getId() == v.getId()) {
			Log.v("mandy", "doc is clicked");
			Toast.makeText(mContext, "此功能暂未实现...", Toast.LENGTH_SHORT).show();
//			Intent intent = new Intent(this, SelectActivity.class);
//			intent.putExtra(CommonData.KeyTopicType, mTopicType);
//			intent.putExtra("name", "文档");
//			startActivity(intent);
		} else if (mBtnLayer.getId() == v.getId()) {
			// start layer dialog
			hideCallOut();
			if(mLayerDialog == null){
				mLayerDialog = new LayerDialog();
			}
			mLayerDialog.setMapView(map);
			mLayerDialog.setMapServiceLayer(tms);
			mLayerDialog.setDrawLayer(drawLayer);
			mLayerDialog.show(getFragmentManager(), "dialog");

		} else if (btnFrame.getId() == v.getId()) {

			Log.v("mandy", "drawTool frame........");
			drawTool.activate(DrawTool.ENVELOPE);
			 drawLayer.removeAll();

			setButtonsStatus(v.getId());
		} else if (btnLine.getId() == v.getId()) {
			// mapTouchListener.setType("Polyline");
			drawTool.activate(DrawTool.POLYLINE);
			drawLayer.removeAll();
			setButtonsStatus(v.getId());
		} else if (btnPolygon.getId() == v.getId()) {
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
			// drawTool.activate(DrawTool.POLYGON);
			// mapTouchListener.setType("Polygon");
			// drawLayer.removeAll();
			setButtonsStatus(v.getId());
		} else if (btnCurScreen.getId() == v.getId()) {
			//屏幕范围查询
			drawTool.queryAttribute(map.getExtent());
			setButtonsStatus(v.getId());
		} else if (btnMultiple.getId() == v.getId()) {
			setButtonsStatus(v.getId());
		} else if (mBtnScaleBig.getId() == v.getId()) {
			map.zoomin();
		} else if (mBtnScaleSmall.getId() == v.getId()) {
			map.zoomout();
		} else if (mEditText.getId() == v.getId()) {
//			map.zoomTo(centerPt, factor);
			mFragmentLayout.setVisibility(View.VISIBLE);
			fragmentManager.beginTransaction().replace(R.id.fragmentlayout, searchFragment).commit();
		}

	}

//	private GeocoderTask mGeocoderTask;
	
//	private void search(){
//		String address = mEditText.getText().toString();
//        try {
//            // create Locator parameters from single line address string
//            LocatorFindParameters findParams = new LocatorFindParameters(address);
//            //TODO:国家是否设置 set the search country to USA
//            Log.d("map", "----------国家: "+findParams.getSourceCountry());
//            findParams.setSourceCountry("world");
//            // limit the results to 2
//            findParams.setMaxLocations(2);
//            // set address spatial reference to match map
//            findParams.setOutSR(map.getSpatialReference());
//            // execute async task to geocode address
//            mGeocoderTask = new GeocoderTask(mContext, this, mListView, mSearchViewGroup, mSearchAdapter);
//            mGeocoderTask.execute(findParams);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//		
//	}
	/**
	 * 是否选择了折线
	 */
	private boolean mTag4OperateInLine = false;
	private void setButtonsStatus(int vId) {
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

	//设置底部按钮的点击效果
	private void setMenuButtonsStatus(int vId) {
			if (mMenuViewTool.getId() == vId) {
				if(mMenuViewTool.isSelected()){
					mMenuViewTool.setSelected(false);
				}else{
					mMenuViewTool.setSelected(true);
				}
				mMenuViewCount.setSelected(false);
				mMenuViewCompare.setSelected(false);
				mMenuViewMine.setSelected(false);
			} else if (mMenuViewCount.getId() == vId) {
				if(mMenuViewCount.isSelected()){
					mMenuViewCount.setSelected(false);
				}else{
					mMenuViewCount.setSelected(true);
				}
				mMenuViewTool.setSelected(false);
				mMenuViewCompare.setSelected(false);
				mMenuViewMine.setSelected(false);
			} else if (mMenuViewCompare.getId() == vId) {
				if(mMenuViewCompare.isSelected()){
					mMenuViewCompare.setSelected(false);
				}else{
					mMenuViewCompare.setSelected(true);
				}
				mMenuViewTool.setSelected(false);
				mMenuViewCount.setSelected(false);
				mMenuViewMine.setSelected(false);
			} else if (mMenuViewMine.getId() == vId) {
				if(mMenuViewMine.isSelected()){
					mMenuViewMine.setSelected(false);
				}else{
					mMenuViewMine.setSelected(true);
				}
				mMenuViewTool.setSelected(false);
				mMenuViewCount.setSelected(false);
				mMenuViewCompare.setSelected(false);
				
			}
			hideCallOut();
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

	// 关闭软键盘
	private void closeKeyboard() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
	}

	private MenuGridAdapter mAdapter;
	private ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();

	private void initGridViewData() {
		mAdapter = new MenuGridAdapter(mContext, list);
		mGridView.setAdapter(mAdapter);
	}

	private void setGridView(ArrayList<HashMap<String, Object>> list, View view) {
		setMenuButtonsStatus(view.getId());
		mAdapter.notifyDataSetChanged();
		// showAndHideGridView();
		if (mLastClickedView == view) {
			mGridViewLayout.setVisibility(View.INVISIBLE);
			mLastClickedView = null;
		} else {
			mGridViewLayout.setVisibility(View.VISIBLE);
			mLastClickedView = view;
		}
		// else
		// mLastClickedView = null;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		HashMap<String, Object> map = (HashMap<String, Object>) arg0
				.getAdapter().getItem(position);
		String tag = (String) map.get("tag");
		HashMap<String, Boolean> showMap = (HashMap<String, Boolean>) map.get("clicktag");
		if(showMap != null && !showMap.get(tag)){
			return;
		}
		
		Log.d("sinopec", "-------点击p: " + position + "  tag: " + tag);
		if ("toolDistance".equals(tag)) {
			ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
			String[] name = {"KM", "M"};
			for (int i = 0; i < name.length; i++) {
				HashMap<String, Object> distancMap = new HashMap<String, Object>();
				distancMap.put("name", name[i]);
				distancMap.put("tag", name[i]);
				list.add(distancMap);
			}
			//btnPolygon
//			SinoUtil.showWindow(mContext, popupWindow, mMenuViewTool, mMenuListView, mMenuAdapter, this, list);
			// 创建一个PopuWidow对象
			LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			mBaseLayout = (ViewGroup) layoutInflater.inflate(R.layout.view_menu_popwindow, null);
			popupWindow = new PopupWindow(mBaseLayout, 600, 400);
			
			SinoUtil.showWindow(mContext, popupWindow, mBaseLayout, mMenuListView, mMenuAdapter, this, list);
//			drawTool.calculateAreaAndLength();
		} else if ("toolArea".equals(tag)) {
			drawTool.calculateAreaAndLength();
		} else if ("mineLogin".equals(tag)) {
			Intent intent = new Intent(mContext, LoginActivity.class);
			startActivity(intent);
		} else if ("mineLogout".equals(tag)) {
			exitDialog();
		} else if ("mineLogout".equals(tag)) {
			exitDialog();
		} else if ("KM".equals(tag)) {
			// 折线长度以km显示
			hidePopupWindow();
			mBaseLayout.performClick();
			drawTool.calculateAreaAndLength();
		} else if ("M".equals(tag)) {
			// 折线长度以m显示
			hidePopupWindow();
			mBaseLayout.performClick();
			drawTool.calculateAreaAndLength();
		} else if ("anyPoint".equals(tag)) {
			Log.v("mandy", "anypoint..........");
			drawTool.activate(DrawTool.ANY_POLYGON);
			// 多边形任意点绘制
			hidePopupWindow();
		} else if ("points".equals(tag)) {

			drawTool.activate(DrawTool.POLYGON);
			drawLayer.removeAll();
			// 多边形点绘制
			hidePopupWindow();
		} else if ("cycle".equals(tag)) {
			drawTool.activate(DrawTool.CIRCLE);
			drawLayer.removeAll();
			// 多边形任意点绘制
			hidePopupWindow();
		} else if ("points".equals(tag)) {
			// 多边形点绘制
			hidePopupWindow();
		} else if ("cycle".equals(tag)) {
			// 多边形 圆形
			hidePopupWindow();
		}
		
		hideCallOut();
	}

	private void hidePopupWindow() {
		if (popupWindow != null){
			popupWindow.dismiss();
		}
		
		if (mGridViewLayout.getVisibility() == View.VISIBLE) {
			mGridViewLayout.setVisibility(View.INVISIBLE);
		}
		
		mMenuViewTool.setSelected(false);
		mMenuViewCount.setSelected(false);
		mMenuViewCompare.setSelected(false);
		mMenuViewMine.setSelected(false);
	}

	class MapStatusListener implements OnStatusChangedListener {

		@Override
		public void onStatusChanged(Object arg0, STATUS arg1) {
//			  Log.v("mandy", "path: " + path + "state: " + state);
		    Log.i("mandy", "map load status:" + arg1.name());
			 if (OnStatusChangedListener.STATUS.INITIALIZED == arg1 && arg0 == map) {
		           Log.i("mandy", "resolution:" + map.getResolution() +"is loaded: " + map.isLoaded());
		           map.zoomToScale(new Point( -86.36436463525025, 40.28780276447719), 5000000);
		         }
		}
	}
	
	
	class MapTouchListener extends MapOnTouchListener {

		public MapTouchListener(Context context, MapView view) {
			super(context, view);

		}
		 @Override
		 public boolean onTouch(View v, MotionEvent event) {
			 
			 if(mGridViewLayout.getVisibility() == View.VISIBLE)
					mGridViewLayout.setVisibility(View.GONE);
		    return super.onTouch(v, event);
		 }
		 
		 @Override
		public boolean onDragPointerUp(MotionEvent from, MotionEvent to) {
			// TODO Auto-generated method stub
			Point point = map.toMapPoint(to.getX(), to.getY());
			
			
//			System.out.println("getx: " + point.getX() + " getY: " + point.getY());
			
			Log.v("mandy", "getx: " + point.getX() + " getY: " + point.getY());
			
//			  point.getX()
			 
			Polygon polygon =  map.getExtent();
			
			
			
//			polygon.
			 
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

//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if(keyCode == KeyEvent.KEYCODE_BACK){
//			if (mSearchViewGroup.getVisibility() == View.VISIBLE) {
//				mSearchViewGroup.setVisibility(View.GONE);
//			} else {
//				exitDialog();
//			}
//		}
//		return super.onKeyDown(keyCode, event);
//	}
	
	@Override
	public void handleDrawEvent(DrawEvent event) {
		
		this.drawLayer.addGraphic(event.getDrawGraphic());
		
//		if (map.getLayerByID(drawLayer.getID()) != null) {
//			map.removeLayer(drawLayer);
			
//		}
		

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
		FindResult result = (FindResult)data; 
		SinoApplication.findResult = result;
		Geometry geometry = result.getGeometry();
		
        
		Envelope envelope = new Envelope();
		geometry.queryEnvelope(envelope);
		Point point = envelope.getCenter();
		Log.d(tag, "-------查询出的坐标  x: "+point.getX()+"  y: "+point.getY());
		map.zoomToScale(point, 5000000);

		//绘制高亮区域
		SimpleFillSymbol resultSymbol = new SimpleFillSymbol(Color.YELLOW);
		Graphic resultLocation = new Graphic(geometry, resultSymbol);
		drawLayer.addGraphic(resultLocation);
		
		mTopicType = result.getLayerName();
		mTopicName = result.getValue();
		mHashMap4Property = (HashMap<String, Object>) result.getAttributes();
		if (callout.isShowing()) {
			callout.hide();
		}
		callout.show(point);
		mLongTouchTitle.setText(result.getValue());
//		statistics.setText(result.getValue());

		hideInput();
		fragmentManager.beginTransaction().remove(searchFragment).commit();
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

		if(mGridViewLayout.getVisibility() == View.VISIBLE){
			mGridViewLayout.setVisibility(View.GONE);
			//底下四个按钮置为常态
			mMenuViewTool.setSelected(false);
			mMenuViewCount.setSelected(false);
			mMenuViewCompare.setSelected(false);
			mMenuViewMine.setSelected(false);
		}
	}
	
}
