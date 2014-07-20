package com.sinopec.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
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
import com.esri.android.map.ags.ArcGISLayerInfo;
import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;
import com.esri.android.map.event.OnLongPressListener;
import com.esri.android.map.event.OnSingleTapListener;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Line;
import com.esri.core.geometry.MultiPath;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.Polyline;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.sinopec.adapter.MenuAdapter;
import com.sinopec.adapter.MenuGridAdapter;
import com.sinopec.application.SinoApplication;
import com.sinopec.drawtool.DrawEvent;
import com.sinopec.drawtool.DrawEventListener;
import com.sinopec.drawtool.DrawTool;
import com.sinopec.util.ChildrenMenuDataUtil;
import com.sinopec.view.MenuButton;
import com.sinopec.view.MenuButtonNoIcon;

public class MarinedbActivity extends Activity implements OnClickListener,
		OnItemClickListener, DrawEventListener {
	private String tag = "MainActivity";
	private TextView mTVContent;
	private ProgressBar mProgressBar;

	private TextView mTextView;

	private LinearLayout mToolBar;

	// 泡泡变量
	private LinearLayout.LayoutParams popParams = null;
	private View popView = null;
	private Button poptitle = null;
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
	private double curX;
	private double curY;
	private String curname = null;
	private String curadd = null;
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
	private SimpleLineSymbol lineSymbol;
	private SimpleMarkerSymbol markerSymbol;
	private SimpleFillSymbol fillSymbol;
	private MapTouchListener mapTouchListener;
	private Envelope envelope;
	private DrawTool drawTool;
	private ArcGISTiledMapServiceLayer tms;
	public static final String imageUrl = "http://202.204.193.201:6080/arcgis/rest/services/marine_image/MapServer";
    public static final String genUrl = "http://202.204.193.201:6080/arcgis/rest/services/marine_geo/MapServer";
    public static final String oilUrl = "http://202.204.193.201:6080/arcgis/rest/services/marine_oil/MapServer";
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
//		map.setExtent(new Envelope(-10868502.895856911, 4470034.144641369,
//				-10837928.084542884, 4492965.25312689), 0);
		tms = new ArcGISTiledMapServiceLayer(
				"http://202.204.193.201:6080/arcgis/rest/services/marine_oil/MapServer");

		map.addLayer(tms);

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
		envelope = new Envelope();
		// add by gaolixiao
		addPopupWindow();
		getAboutDisplay();
		initLayout();
		drawTool = new DrawTool(map);
		drawTool.addEventListener(this);
		drawTool.setDrawLayer(drawLayer);
		// mapTouchListener = new MapTouchListener(this, map);
		// map.setOnTouchListener(mapTouchListener);
	}

	private void initSymbols() {
		lineSymbol = new SimpleLineSymbol(Color.RED, 5,
				SimpleLineSymbol.STYLE.DASH);
		markerSymbol = new SimpleMarkerSymbol(Color.BLUE, 10,
				SimpleMarkerSymbol.STYLE.CIRCLE);
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
		int width = metric.widthPixels; // 屏幕宽度（像素）
		int height = metric.heightPixels; // 屏幕高度（像素）
		SinoApplication.density = metric.density; // 屏幕密度（0.75 / 1.0 / 1.5）
		int densityDpi = metric.densityDpi; // 屏幕密度DPI（120 / 160 / 240）
		// 此设备 width: 2560 hei: 1600 density: 2.0 densityDpi: 320
		Log.d("sinopec", "width: " + width + "  hei: " + height + "  density: "
				+ SinoApplication.density + "  densityDpi: " + densityDpi);
	}

	private void addPopupWindow() {

		// 泡泡代码
		popView = View.inflate(this, R.layout.paopao, null);
		// ImageView imageSMS =
		// (ImageView)popView.findViewById(R.id.tvImageSMS);
		// ImageView imageNavi =
		// (ImageView)popView.findViewById(R.id.tvImageNavi);
		property = (Button) popView.findViewById(R.id.property);
		statistics = (Button) popView.findViewById(R.id.statistics);
		doc = (Button) popView.findViewById(R.id.doc);
		mLongTouchTitle = (Button) popView.findViewById(R.id.paopao_name);
		property.setOnClickListener(this);
		statistics.setOnClickListener(this);
		doc.setOnClickListener(this);

		Drawable drawable1 = getResources().getDrawable(R.drawable.info1);
		// imageSMS.setBackgroundDrawable(drawable1);
		// imageSMS.getBackground().setAlpha(100);
		Drawable drawable2 = getResources().getDrawable(R.drawable.info3);
		// imageNavi.setBackgroundDrawable(drawable2);
		// imageNavi.getBackground().setAlpha(100);
		// poptitle.getBackground().setAlpha(100);

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
					public boolean onLongPress(float x, float y) {
						if (!map.isLoaded()) {
							return false;
						}
						Point pt = MarinedbActivity.this.map.toMapPoint(x, y);
						x1 = pt.getX();
						y1 = pt.getY();
						name2 = "未知地名";
						address2 = "未知地址";

						try {
							curX = x1;
							curY = y1;
							curname = name2;
							curadd = address2;
							Point pxPt = MarinedbActivity.this.map
									.toScreenPoint(new Point(x1, y1));
							int pxx = 0;
							int pxy = 0;

							pxx = (int) (pxPt.getX()); // 图标宽度(像素)
							pxy = (int) (pxPt.getY()); // 图标高度(像素)
							anchorPt = MarinedbActivity.this.map.toMapPoint(
									pxx, pxy);
							// 添加图标
							if (LongPressPOI != null) {
								MarinedbActivity.this.gLayer
										.removeGraphic(LongPressPOI.getUid());
							}
							Animation poiAnimation = new TranslateAnimation(
									pxx, pxx, 0, y);
							poiAnimation.setDuration(500);
							imageAnim.startAnimation(poiAnimation);
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
						 return true;
					}// onLongPress
				});

	}

	private MenuButton mMenuViewCompare;
	private MenuButton mMenuViewCount;
	private MenuButton mMenuViewMine;
	private MenuButton mMenuViewTool;

	private void initView() {
		mToolBar = (LinearLayout) findViewById(R.id.menu_bar);

		// mBtnMenuTool = (Button) findViewById(R.id.menu_tool);
		// mBtnMenuCount = (Button) findViewById(R.id.menu_count);
		// mBtnMenuCompare = (Button) findViewById(R.id.menu_compare);
		// mBtnMenuMine = (Button) findViewById(R.id.menu_mine);
		mGridViewLayout = (RelativeLayout) findViewById(R.id.menu_children_grid);
		mGridView = (GridView) findViewById(R.id.menu_gridview);
		mGridView.setOnItemClickListener(this);
		// mGridView.setStretchMode(GridView.NO_STRETCH);

		initGridViewData();
		mProgressBar = (ProgressBar) findViewById(R.id.progressbar);
		mTVContent = (TextView) findViewById(R.id.tv_content);
		mTVContent.setMovementMethod(ScrollingMovementMethod.getInstance());
		mBtnLayer = (Button) findViewById(R.id.btn_map_layout);
		mBtnLayer.setOnClickListener(this);
		mBtnSearch = (MenuButtonNoIcon) findViewById(R.id.btn_search_confirm);
		mBtnSearch.setOnClickListener(new OnClickListener() {

			@SuppressLint("NewApi")
			@Override
			public void onClick(View arg0) {
//				SearchFragment search = new SearchFragment(mEditText.);
//				search.show(getFragmentManager(), "search");
			}
		});

		mEditText = (Button) findViewById(R.id.edittext_search);
		mEditText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(mContext, SearchActivity.class);
				startActivity(intent);
			}
		});
		mMenuViewTool = (MenuButton) findViewById(R.id.menuview_tool);
		mMenuViewTool.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String[] name4count = new String[] { "测距", "测面积" };
				Integer[] icon4count = { R.drawable.icon_count_distance,
						R.drawable.icon_count_area };
				String[] tag = new String[] { "toolDistance", "toolArea",
						"toolSelect" };
				int splitNumber = mChildMenuSplitNumber;
				list.clear();
				for (int i = 0; i < name4count.length; i++) {
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("name", name4count[i]);
					map.put("icon", icon4count[i]);
					map.put("tag", tag[i]);
					map.put("split", splitNumber);
					list.add(map);
				}
				mGridView.setNumColumns(2);
				setGridView(list, arg0);
			}
		});

		mMenuViewCount = (MenuButton) findViewById(R.id.menuview_count);
		mMenuViewCount.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String[] name4count = new String[] { "数.面积.储", "数密.面密", "储量丰度",
						"储量分布", "油气田数", "油气田面积" };
				// String[] name4count = new String[] {
				// "范围内油气田的个数、面积、储量(油、气、...)", "范围内油气田的个数密度、面积密度",
				// "范围内油气田的储量丰度(吨油当量/平方公里)", "石油、天然气及凝析油储量在各油气田的分布",
				// "不同沉积体系油气田个数", "不同沉积体系油气田面积" };
				Integer[] icon4count = { R.drawable.icon_rang_oilgas,
						R.drawable.icon_rang_oilgas,
						R.drawable.icon_range_volume,
						R.drawable.icon_distribute,
						R.drawable.icon_diffrent_object_nubmer,
						R.drawable.icon_diffrent_object_nubmer, };
				int splitNumber = mChildMenuSplitNumber;
				list.clear();
				for (int i = 0; i < name4count.length; i++) {
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("name", name4count[i]);
					map.put("icon", icon4count[i]);
					map.put("tag", name4count[i]);
					map.put("split", splitNumber);
					list.add(map);
				}
				mGridView.setNumColumns(6);
				setGridView(list, arg0);

			}
		});

		mMenuViewCompare = (MenuButton) findViewById(R.id.menuview_compare);
		mMenuViewCompare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Log.d("map", "----对比----");
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
				Log.d("map", "----我的----");
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
//				mTag4OperateInLine = false;
				btnMultiple.setSelected(false);
				btnFrame.setSelected(false);
				btnLine.setSelected(false);
				btnPolygon.setSelected(false);
				btnCurScreen.setSelected(false);
				
				
				drawLayer.removeAll();
				drawTool.deactivate();
			}
		});

		mBtnScaleSmall = (ImageButton) findViewById(R.id.btn_scale_small);
		mBtnScaleSmall.setOnClickListener(this);
		mBtnScaleBig = (ImageButton) findViewById(R.id.btn_scale_big);
		mBtnScaleBig.setOnClickListener(this);
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

	private Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1) {
                Log.v("mandy", "loaded: " + map.isLoaded());
				ArcGISLayerInfo[] arc = tms.getAllLayers();
				
				for (int i = 0; i < arc.length; i++) {
					
					arc[i].setVisible(false);
					
					
				}
				
				for (ArcGISLayerInfo arcGISLayerInfo : arc) {
					
//					Log.v("mandy", "layer name: " + arcGISLayerInfo.getName() + arcGISLayerInfo.isVisible()
//							+ "  child layer: "
//							+ arcGISLayerInfo.getLayers().length);
//					
//					
//////					Log.v("mandy", "legend size: " + arcGISLayerInfo.getLegend().size());
////					for (Legend legend : arcGISLayerInfo.getLegend()) {
////						Log.v("mandy", "legend: " + legend.getLabel());
////						
////					}
//					Toast.makeText(mContext, arcGISLayerInfo.getName(), Toast.LENGTH_LONG).show();
				}
				map.addLayer(tms);
//				map.requestLayout();
				
			Layer[] layer =	map.getLayers();
			Log.v("mandy", "layer: " + layer.length);
			
			for (int i = 0; i < layer.length; i++) {
				
				Log.v("mandy", "layer name : " +   layer[i].getName());
			}
				
//				ArcGIS
				int [] jj = {0,1};
				
//				ArcGISDynamicMapServiceLayer layers = new ArcGISDynamicMapServiceLayer("http://www.arcgisonline.cn/ArcGIS/rest/services/ChinaCities_Community_BaseMap_CHN/Beijing_Community_BaseMap_CHN/MapServer", jj);
//				ArcGISLocalTiledLayer yy = new ArcGISLocalTiledLayer(path);
				
//				ArcGISLocalTiledLayer
//				ArcGISTiledMapServiceLayer layer = new ArcGISTiledMapServiceLayer("http://202.204.193.201:6080/arcgis/rest/services/marine_oil/MapServer/0");
//			Layer layer2 = new Layer() {
//				
//				@Override
//				protected void initLayer() {
//					// TODO Auto-generated method stub
//					
//				}
//				
//				@Override
//				protected long create() {
//					// TODO Auto-generated method stub
//					return 0;
//				}
//			};
//				map.addLayer(layers);
//				map.invalidate();
				

			}

		};

	};

	@Override
	protected void onResume() {
		super.onResume();
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

	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		if (property.getId() == v.getId()) {
			Log.v("mandy", "property is clicked");
			Intent intent = new Intent(this, SelectActivity.class);
			intent.putExtra("name", "属性");
			startActivity(intent);

		} else if (statistics.getId() == v.getId()) {

			Log.v("mandy", "statistics is clicked");
			Intent intent = new Intent(this, SelectActivity.class);
			intent.putExtra("name", "统计");
			startActivity(intent);
		} else if (doc.getId() == v.getId()) {
			Log.v("mandy", "doc is clicked");
			Intent intent = new Intent(this, SelectActivity.class);
			intent.putExtra("name", "文档");
			startActivity(intent);
		} else if (mBtnLayer.getId() == v.getId()) {
			// start layer dialog
			LayerDialog dialog = new LayerDialog();
			dialog.setMapView(map);
			dialog.setMapServiceLayer(tms);
			dialog.setDrawLayer(drawLayer);
			dialog.show(getFragmentManager(), "dialog");

		} else if (btnFrame.getId() == v.getId()) {

			Log.v("mandy", "drawTool frame........");
			drawTool.activate(DrawTool.ENVELOPE);
			// drawLayer.removeAll();

			setButtonsStatus(v.getId());
		} else if (btnLine.getId() == v.getId()) {
			ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
			String[] name = { "KM", "M" };
			for (int i = 0; i < name.length; i++) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("name", name[i]);
				map.put("tag", name[i]);
				list.add(map);
			}
			showWindow(btnPolygon, list, mLocation4Line);
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
			setButtonsStatus(v.getId());
		} else if (btnMultiple.getId() == v.getId()) {
			setButtonsStatus(v.getId());
		} else if (btnCurScreen.getId() == v.getId()) {
			setButtonsStatus(v.getId());
		} else if (btnMultiple.getId() == v.getId()) {
			setButtonsStatus(v.getId());
		} else if (mBtnScaleBig.getId() == v.getId()) {
			map.zoomin();
		} else if (mBtnScaleSmall.getId() == v.getId()) {
			map.zoomout();
		}

	}

	private void setButtonsStatus(int vId) {
		if (btnFrame.getId() == vId) {
			btnFrame.setBackgroundResource(R.drawable.main_button_background_down);
			btnMultiple
					.setBackgroundResource(R.drawable.main_button_background_up);
			btnLine.setBackgroundResource(R.drawable.main_button_background_up);
			btnPolygon
					.setBackgroundResource(R.drawable.main_button_background_up);
			btnCurScreen
					.setBackgroundResource(R.drawable.main_button_background_up);
		} else if (btnLine.getId() == vId) {
			btnLine.setBackgroundResource(R.drawable.main_button_background_down);
			btnPolygon
					.setBackgroundResource(R.drawable.main_button_background_up);
			btnFrame.setBackgroundResource(R.drawable.main_button_background_up);
			btnCurScreen
					.setBackgroundResource(R.drawable.main_button_background_up);
			btnMultiple
					.setBackgroundResource(R.drawable.main_button_background_up);
		} else if (btnPolygon.getId() == vId) {
			btnPolygon
					.setBackgroundResource(R.drawable.main_button_background_down);
			btnFrame.setBackgroundResource(R.drawable.main_button_background_up);
			btnLine.setBackgroundResource(R.drawable.main_button_background_up);
			btnCurScreen
					.setBackgroundResource(R.drawable.main_button_background_up);
			btnMultiple
					.setBackgroundResource(R.drawable.main_button_background_up);
		} else if (btnCurScreen.getId() == vId) {
			btnCurScreen
					.setBackgroundResource(R.drawable.main_button_background_down);
			btnPolygon
					.setBackgroundResource(R.drawable.main_button_background_up);
			btnFrame.setBackgroundResource(R.drawable.main_button_background_up);
			btnLine.setBackgroundResource(R.drawable.main_button_background_up);
			btnMultiple
					.setBackgroundResource(R.drawable.main_button_background_up);
		} else if (btnMultiple.getId() == vId) {
			btnMultiple
					.setBackgroundResource(R.drawable.main_button_background_down);
			btnCurScreen
					.setBackgroundResource(R.drawable.main_button_background_up);
			btnPolygon
					.setBackgroundResource(R.drawable.main_button_background_up);
			btnFrame.setBackgroundResource(R.drawable.main_button_background_up);
			btnLine.setBackgroundResource(R.drawable.main_button_background_up);
		}

		showCancelButton();
	}

	// 设置底部按钮的点击效果
	private void setMenuButtonsStatus(int vId) {
		if (mMenuViewTool.getId() == vId) {
			mMenuViewTool.setSelected(true);
			mMenuViewCount.setSelected(false);
			mMenuViewCompare.setSelected(false);
			mMenuViewMine.setSelected(false);
		} else if (mMenuViewCount.getId() == vId) {
			mMenuViewCount.setSelected(true);
			mMenuViewTool.setSelected(false);
			mMenuViewCompare.setSelected(false);
			mMenuViewMine.setSelected(false);
		} else if (mMenuViewCompare.getId() == vId) {
			mMenuViewCompare.setSelected(true);
			mMenuViewTool.setSelected(false);
			mMenuViewCount.setSelected(false);
			mMenuViewMine.setSelected(false);
		} else if (mMenuViewMine.getId() == vId) {
			mMenuViewMine.setSelected(true);
			mMenuViewTool.setSelected(false);
			mMenuViewCount.setSelected(false);
			mMenuViewCompare.setSelected(false);

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
			drawTool.calculateAreaAndLength();
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
		} else if ("M".equals(tag)) {
			// 折线长度以m显示
			hidePopupWindow();
		} else if ("anyPoint".equals(tag)) {
			Log.v("mandy", "anypoint..........");
			drawTool.activate(DrawTool.ANY_POLYGON);
			// 多边形任意点绘制
			hidePopupWindow();
		} else if ("points".equals(tag)) {

			drawTool.activate(DrawTool.POLYGON);
			// 多边形点绘制
			hidePopupWindow();
		} else if ("cycle".equals(tag)) {
			drawTool.activate(DrawTool.CIRCLE);
			// 多边形任意点绘制
			hidePopupWindow();
		} else if ("points".equals(tag)) {
			// 多边形点绘制
			hidePopupWindow();
		} else if ("cycle".equals(tag)) {
			// 多边形 圆形
			hidePopupWindow();
		}
	}

	private void hidePopupWindow() {
		if (popupWindow != null)
			popupWindow.dismiss();
	}

	private Point ptStart = null;// 起点
	private Point ptPrevious = null;// 上一个点
	private ArrayList<Point> points = null;// 记录全部点
	private Polygon tempPolygon = null;// 记录绘制过程中的多边形
	MultiPath poly;
	String type = "";
	Point startPoint = null;

	class MapTouchListener extends MapOnTouchListener {
		// private Geometry.Type geoType = null;//用于判定当前选择的几何图形类型

		public MapTouchListener(Context context, MapView view) {
			super(context, view);

			points = new ArrayList<Point>();
		}

		// 根据用户选择设置当前绘制的几何图形类型
		public void setType(String geometryType) {
			type = geometryType;
		}

		public String getType() {
			return type;
		}

		@Override
		public boolean onSingleTap(MotionEvent point) {
			Log.d("map", "--------onSingleTap ");
			showCancelButton();
			Point ptCurrent = map.toMapPoint(new Point(point.getX(), point
					.getY()));
			if (ptStart == null)
				drawLayer.removeAll();// 第一次开始前，清空全部graphic

			if (type.equalsIgnoreCase("Point")) {// 直接画点
				drawLayer.removeAll();
				ptStart = ptCurrent;

				Graphic graphic = new Graphic(ptStart, markerSymbol);
				drawLayer.addGraphic(graphic);

				// btnClear.setEnabled(true);
				return true;
			} else// 绘制线或多边形
			{
				points.add(ptCurrent);// 将当前点加入点集合中

				if (ptStart == null) {// 画线或多边形的第一个点
					ptStart = ptCurrent;

					// 绘制第一个点
					Graphic graphic = new Graphic(ptStart, markerSymbol);
					drawLayer.addGraphic(graphic);
				} else {// 画线或多边形的其他点
						// 绘制其他点
					Graphic graphic = new Graphic(ptCurrent, markerSymbol);
					drawLayer.addGraphic(graphic);

					// 生成当前线段（由当前点和上一个点构成）
					Line line = new Line();
					line.setStart(ptPrevious);
					line.setEnd(ptCurrent);

					if (type.equalsIgnoreCase("Polyline")) {
						// 绘制当前线段
						Polyline polyline = new Polyline();
						polyline.addSegment(line, true);

						Graphic g = new Graphic(polyline, lineSymbol);
						drawLayer.addGraphic(g);

						// 计算当前线段的长度
						String length = Double.toString(Math.round(line
								.calculateLength2D())) + " 米";

						Toast.makeText(map.getContext(), "长度： " + length,
								Toast.LENGTH_SHORT).show();
					} else {
						// 绘制临时多边形
						if (tempPolygon == null)
							tempPolygon = new Polygon();
						tempPolygon.addSegment(line, false);

						drawLayer.removeAll();
						Graphic g = new Graphic(tempPolygon, fillSymbol);
						drawLayer.addGraphic(g);

						// 计算当前面积
						String sArea = getAreaString(tempPolygon
								.calculateArea2D());

						Toast.makeText(map.getContext(), "面积： " + sArea,
								Toast.LENGTH_SHORT).show();
					}
				}

				ptPrevious = ptCurrent;
				return true;
			}
		}

		@Override
		public boolean onDoubleTap(MotionEvent point) {
			Log.d("map", "--------onDoubleTap ");
			return false;
		}

		public boolean onDragPointerMove(MotionEvent from, MotionEvent to) {
			// Log.d("map", "--------onDragPointerMove ");
			hideCancelButton();
			if (mGridViewLayout.getVisibility() == View.VISIBLE) {
				mGridViewLayout.setVisibility(View.INVISIBLE);
			}
			if (type.equalsIgnoreCase("point")) {
				// Log.v("mandy", "type: move");
				//
				Point mapPt = map.toMapPoint(to.getX(), to.getY());
				// envelope.setXMin(startPoint.getX() > mapPt.getX() ? mapPt
				// .getX() : startPoint.getX());
				// envelope.setYMin(startPoint.getY() > mapPt.getY() ? mapPt
				// .getY() : startPoint.getY());
				// envelope.setXMax(startPoint.getX() < mapPt.getX() ? mapPt
				// .getX() : startPoint.getX());
				// envelope.setYMax(startPoint.getY() < mapPt.getY() ? mapPt
				// .getY() : startPoint.getY());

				/*
				 * if StartPoint is null, create a polyline and start a path.
				 */
				if (startPoint == null) {
					drawLayer.removeAll();
					// poly = type.equalsIgnoreCase("POLYLINE") ? new Polyline()
					// : new Polygon();
					poly = new Polygon();
					startPoint = map.toMapPoint(from.getX(), from.getY());
					poly.startPath((float) startPoint.getX(),
							(float) startPoint.getY());

					/*
					 * Create a Graphic and add polyline geometry
					 */
					Graphic graphic = new Graphic(startPoint, fillSymbol);

					/*
					 * add the updated graphic to graphics layer
					 */
					drawLayer.addGraphic(graphic);
				}

				poly.lineTo((float) mapPt.getX(), (float) mapPt.getY());
				// poly.calculateArea2D();
				// poly.calculateLength2D();

				return true;
			}
			return super.onDragPointerMove(from, to);

		}

		@Override
		public boolean onDragPointerUp(MotionEvent from, MotionEvent to) {
			Log.d("map", "--------onDragPointerUp ");
			showCancelButton();
			// if (type.length() > 1
			// && (type.equalsIgnoreCase("POLYLINE") || type
			// .equalsIgnoreCase("POLYGON"))) {

			/*
			 * When user releases finger, add the last point to polyline.
			 */
			if (type.equalsIgnoreCase("point")) {
				// Log.d("map", "--------onDragPointerUp type " + type);
				// Point point = map.toMapPoint(to.getX(), to.getY());
				// envelope.setXMin(startPoint.getX() > point.getX() ? point
				// .getX() : startPoint.getX());
				// envelope.setYMin(startPoint.getY() > point.getY() ? point
				// .getY() : startPoint.getY());
				// envelope.setXMax(startPoint.getX() < point.getX() ? point
				// .getX() : startPoint.getX());
				// envelope.setYMax(startPoint.getY() < point.getY() ? point
				// .getY() : startPoint.getY());

				// Graphic drawGraphic = new Graphic(envelope, fillSymbol);
				// // drawGraphic.
				// drawLayer.addGraphic(drawGraphic);

				poly.lineTo((float) startPoint.getX(),
						(float) startPoint.getY());
				drawLayer.removeAll();
				drawLayer.addGraphic(new Graphic(poly, fillSymbol));
				// points.add(startPoint);
				startPoint = null;
				// 计算面积
				String sArea = getAreaString(poly.calculateArea2D());

				Toast.makeText(map.getContext(), "面积： " + sArea,
						Toast.LENGTH_SHORT).show();

				return true;
			}
			// graphicsLayer.addGraphic(new Graphic(poly,new
			// SimpleLineSymbol(Color.BLUE,5)));
			// startPoint = null;
			// clearButton.setEnabled(true);
			// return true;
			// }
			return super.onDragPointerUp(from, to);
		}

		// @Override
		// public boolean onTouch(View v, MotionEvent event) {
		// Log.v("mandy", "on touch&&&&&&&&&&&&&&&&&&&");
		// Point point = map.toMapPoint(event.getX(), event.getY());
		// startPoint = point;
		// envelope.setCoords(point.getX(), point.getY(),
		// point.getX(), point.getY());
		// drawLayer.removeAll();
		// return super.onTouch(v, event);
		// }

	}

	// private void calculateAreaAndLength() {
	// drawLayer.removeAll();
	// if (type.equalsIgnoreCase("Polyline")) {
	// Polyline polyline = new Polyline();
	//
	// Point startPoint = null;
	// Point endPoint = null;
	//
	// // 绘制完整的线段
	// for (int i = 1; i < points.size(); i++) {
	// startPoint = points.get(i - 1);
	// endPoint = points.get(i);
	//
	// Line line = new Line();
	// line.setStart(startPoint);
	// line.setEnd(endPoint);
	//
	// polyline.addSegment(line, false);
	// }
	//
	// Graphic g = new Graphic(polyline, lineSymbol);
	// drawLayer.addGraphic(g);
	//
	// // 计算总长度
	// String length = Double.toString(Math.round(polyline
	// .calculateLength2D())) + " 米";
	//
	// Toast.makeText(map.getContext(), "总长度： " + length,
	// Toast.LENGTH_SHORT).show();
	// } else {
	// Polygon polygon = new Polygon();
	//
	// Point startPoint = null;
	// Point endPoint = null;
	// // 绘制完整的多边形
	// for (int i = 1; i < points.size(); i++) {
	// startPoint = points.get(i - 1);
	// endPoint = points.get(i);
	//
	// Line line = new Line();
	// line.setStart(startPoint);
	// line.setEnd(endPoint);
	//
	// polygon.addSegment(line, false);
	// }
	//
	// Graphic g = new Graphic(polygon, fillSymbol);
	// drawLayer.addGraphic(g);
	//
	// // 计算总面积
	// String sArea = getAreaString(polygon.calculateArea2D());
	//
	// Toast.makeText(map.getContext(), "总面积： " + sArea,
	// Toast.LENGTH_SHORT).show();
	// }
	//
	// // 其他清理工作
	// // btnClear.setEnabled(true);
	// ptStart = null;
	// ptPrevious = null;
	// points.clear();
	// tempPolygon = null;
	// }

	private void clearGraphic() {

		ptStart = null;
		ptPrevious = null;
		points.clear();
		tempPolygon = null;

	}

	private String getAreaString(double dValue) {
		long area = Math.abs(Math.round(dValue));
		String sArea = "";
		// 顺时针绘制多边形，面积为正，逆时针绘制，则面积为负
		if (area >= 1000000) {
			double dArea = area / 1000000.0;
			sArea = Double.toString(dArea) + " 平方公里";
		} else
			sArea = Double.toString(area) + " 平方米";

		return sArea;
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
		exitDialog();
	}

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
}
