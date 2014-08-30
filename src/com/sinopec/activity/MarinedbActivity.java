package com.sinopec.activity;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.util.JsonReader;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.esri.android.map.event.OnZoomListener;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Point;
import com.esri.core.map.FeatureSet;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.MarkerSymbol;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.tasks.ags.find.FindResult;
import com.esri.core.tasks.ags.identify.IdentifyParameters;
import com.esri.core.tasks.ags.identify.IdentifyResult;
import com.lenovo.nova.util.parse.JsonToBeanParser;
import com.sinopec.adapter.MenuAdapter;
import com.sinopec.adapter.MenuGridAdapter;
import com.sinopec.adapter.SearchAdapter;
import com.sinopec.application.SinoApplication;
import com.sinopec.chart.BarChart3;
import com.sinopec.chart.PieChart3;
import com.sinopec.common.CommonData;
import com.sinopec.common.InterfaceDataCallBack;
import com.sinopec.common.OilGasData;
import com.sinopec.data.json.Constant;
import com.sinopec.data.json.standardquery.BasinBelonToRoot;
import com.sinopec.data.json.standardquery.DistributeCengGai;
import com.sinopec.data.json.standardquery.DistributeChuJi;
import com.sinopec.data.json.standardquery.DistributeJingRockYuanYan;
import com.sinopec.data.json.standardquery.DistributeRate;
import com.sinopec.data.json.standardquery.DistributeRateResource;
import com.sinopec.drawtool.DrawEvent;
import com.sinopec.drawtool.DrawEventListener;
import com.sinopec.drawtool.DrawTool;
import com.sinopec.query.AsyncHttpQuery;
import com.sinopec.task.SearchIdentifyTask;
import com.sinopec.util.ChildrenMenuDataUtil;
import com.sinopec.util.JsonParse;
import com.sinopec.util.RelativeUnicode;
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
	public static boolean isHideCallout = false;
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
	// private Map<String, View> menuStatus = new HashMap<String, View>();
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
	private AsyncHttpQuery asyncHttpQuery;
	private String urlBasionQuery;

	/**
	 * 搜索结果fragment依赖的布局
	 */
	private ViewGroup mFragmentLayout;
	private ArrayList<HashMap<String, Object>> toolist = new ArrayList<HashMap<String, Object>>();
	private Handler handler = new Handler() {

		public void handleMessage(final android.os.Message msg) {
			Long[] array;
			mLastClickedView = null;
			switch (msg.what) {
			case 1:
				// String jsonStr = getJsonStr(url);
				BasinBelonToRoot root = new BasinBelonToRoot();
				try {
					JSONArray jsonArray = new JSONArray((String) msg.obj);
					for (int i = 0; i < jsonArray.length(); i++) {
						try {
							JsonToBeanParser.getInstance().fillBeanWithJson(
									root.newBasinBelongTo(),
									jsonArray.getJSONObject(i));
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				array = new Long[root.mBasinBelongTo.size()];
				for (int i = 0; i < root.mBasinBelongTo.size(); i++) {
					array[i] = root.mBasinBelongTo.get(i).getBeLongToId();
				}
				String where = whereSelect(array);

				drawTool.queryAttribute4Query(where, urlBasionQuery,
						root.mBasinBelongTo);
				break;
			case 2:
				final DistributeRate rate = new DistributeRate();
				try {
					JSONArray jsonArray = new JSONArray((String) msg.obj);

					for (int i = 0; i < jsonArray.length(); i++) {
						try {
							JsonToBeanParser.getInstance().fillBeanWithJson(
									rate.newChildInstance(),
									jsonArray.getJSONObject(i));
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				array = new Long[rate.mChilds.size()];
				for (int i = 0; i < rate.mChilds.size(); i++) {
					array[i] = rate.mChilds.get(i).getCodeBelongToBasin();
				}

				drawTool.queryAttribute4Query(whereSelect(array),
						urlBasionQuery, rate.mChilds);

				break;
			case 3:
				final DistributeRateResource instance = new DistributeRateResource();
				try {
					JSONArray jsonArray = new JSONArray((String) msg.obj);

					for (int i = 0; i < jsonArray.length(); i++) {
						try {
							JsonToBeanParser.getInstance().fillBeanWithJson(
									instance.newChildInstance(),
									jsonArray.getJSONObject(i));
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}

				array = new Long[instance.mChilds.size()];
				for (int i = 0; i < instance.mChilds.size(); i++) {
					array[i] = instance.mChilds.get(i).getCodeBelongToBasin();
				}
				drawTool.queryAttribute4Query(whereSelect(array),
						urlBasionQuery, instance.mChilds);

				break;
			case 4:
				final DistributeJingRockYuanYan rockYuanYan = new DistributeJingRockYuanYan();
				try {
					JSONArray jsonArray = new JSONArray((String) msg.obj);

					for (int i = 0; i < jsonArray.length(); i++) {
						try {
							JsonToBeanParser.getInstance().fillBeanWithJson(
									rockYuanYan.newChildInstance(),
									jsonArray.getJSONObject(i));
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
				array = new Long[rockYuanYan.mChilds.size()];
				for (int i = 0; i < rockYuanYan.mChilds.size(); i++) {
					array[i] = rockYuanYan.mChilds.get(i)
							.getCodeBelongToBasin();
				}
				drawTool.queryAttribute4Query(whereSelect(array),
						urlBasionQuery, rockYuanYan.mChilds);
				break;

			case 5:
				final DistributeChuJi chuJi = new DistributeChuJi();
				try {
					JSONArray jsonArray = new JSONArray((String) msg.obj);

					for (int i = 0; i < jsonArray.length(); i++) {
						try {
							JsonToBeanParser.getInstance().fillBeanWithJson(
									chuJi.newChildInstance(),
									jsonArray.getJSONObject(i));
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
				array = new Long[chuJi.mChilds.size()];
				for (int i = 0; i < chuJi.mChilds.size(); i++) {
					array[i] = chuJi.mChilds.get(i).getCodeBelongToBasin();
				}
				drawTool.queryAttribute4Query(whereSelect(array),
						urlBasionQuery, chuJi.mChilds);
				break;

			case 6:
				final DistributeCengGai cengGai = new DistributeCengGai();
				try {
					JSONArray jsonArray = new JSONArray((String) msg.obj);

					for (int i = 0; i < jsonArray.length(); i++) {
						try {
							JsonToBeanParser.getInstance().fillBeanWithJson(
									cengGai.newChildInstance(),
									jsonArray.getJSONObject(i));
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
				array = new Long[cengGai.mChilds.size()];
				for (int i = 0; i < cengGai.mChilds.size(); i++) {
					array[i] = cengGai.mChilds.get(i).getCodeBelongToBasin();
				}
				drawTool.queryAttribute4Query(whereSelect(array),
						urlBasionQuery, cengGai.mChilds);
				break;
			default:
				break;
			}

		};

	};

	private Bitmap mBitmapPaopaobg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.mContext = this;
		asyncHttpQuery = new AsyncHttpQuery(handler, this);
		urlBasionQuery = getResources().getString(R.string.url_basin) + "/0";

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
		// Envelope envelope = new Envelope(new Point(-29.440589,5.065565));
		// map.setExtent(envelope, 0);
		tms = new ArcGISTiledMapServiceLayer(
				"http://202.204.193.201:6080/arcgis/rest/services/marine_oil/MapServer");
		// oilUrl);

		// 加入6个专题图层
		String[] urls = getResources().getStringArray(R.array.all_layer_urls);
		for (int i = 0; i < urls.length; i++) {
			ArcGISTiledMapServiceLayer layer = new ArcGISTiledMapServiceLayer(
					urls[i]);
			map.addLayer(layer);
		}
		// 不显示卫星图
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
		// fLayer = new ArcGISFeatureLayer(
		// "http://sampleserver3.arcgisonline.com/ArcGIS/rest/services/Petroleum/KSPetro/MapServer/1",
		// o);
		// map.addLayer(fLayer);
		mDrawLayer4HighLight = new GraphicsLayer();
		drawLayer = new GraphicsLayer();

		map.addLayer(mDrawLayer4HighLight);
		map.addLayer(drawLayer);
		initSymbols();// 初始化符号
		// add by gaolixiao
		addPopupWindow();
		getAboutDisplay();
		initLayout();
		drawTool = new DrawTool(map, this, callout, mContext);
		drawTool.addEventListener(this);
		drawTool.setDrawLayer(drawLayer, mDrawLayer4HighLight);
		mapTouchListener = new MapTouchListener(this, map);
		map.setOnTouchListener(mapTouchListener);
		// 这个干哈用？？
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
		map.zoomTo(new Point(0, 0), (float) map.getMaxResolution());
		map.setMapBackground(Color.WHITE, Color.TRANSPARENT, 0, 0);
		initTableKeyValue();
		// getJson();
	}

	private void getJson() {
		String url = "http://10.225.14.204:8080/peprisapi/oilGasFieldAttribute.html?dzdybm=201102001063";
		asyncHttpQuery.execute(4, url);
	}

	private void initTableKeyValue() {
		try {
			InputStream inputStream = getAssets().open("oilstrings.xml");
			BufferedReader br = new BufferedReader(new InputStreamReader(
					inputStream));
			for (String str = br.readLine(); str != null; str = br.readLine()) {
				if (str.contains("dispaly")) {
					String key = str.substring(str.indexOf("<") + 1,
							str.indexOf("dispaly"));
					String value = str.substring(str.indexOf("dispaly=\"")
							+ "dispaly=\"".length(), str.indexOf("\"/>"));
					Log.d("table", "key: " + key.trim() + "  value: " + value);
					SinoApplication.mNameMap.put(key.trim(), value);
				}
			}
			br.close();
		} catch (IOException e) {
			Log.d("table", " initTableKeyValue error:  " + e.toString());
			e.printStackTrace();
		}
	}

	private void initSymbols() {
		// lineSymbol = new SimpleLineSymbol(Color.RED, 5,
		// SimpleLineSymbol.STYLE.DASH);
		// markerSymbol = new SimpleMarkerSymbol(Color.BLUE, 10,
		// SimpleMarkerSymbol.STYLE.CIRCLE);
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

	public String whereSelect(Long[] objArray) {

		StringBuilder builder = new StringBuilder("OBJ_ID =");

		for (int i = 0; i < objArray.length; i++) {

			if (i == objArray.length - 1) {
				builder.append(objArray[i]);
			} else {
				builder.append(objArray[i] + "or OBJ_ID =");
			}
		}
		return builder.toString();
	}

	private Animation aniIn;
	private Animation aniOut;
	private Animation aniUp;
	private Animation aniDown;

	private void initAnimations() {
		aniIn = AnimationUtils.loadAnimation(mContext, R.anim.ani_leftmenu_in);
		aniOut = AnimationUtils
				.loadAnimation(mContext, R.anim.ani_leftmenu_out);
		aniDown = AnimationUtils.loadAnimation(mContext, R.anim.ani_menu_down);
		aniUp = AnimationUtils.loadAnimation(mContext, R.anim.ani_menu_up);
	}

	public void getAboutDisplay() {
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		// int width = metric.widthPixels; // 屏幕宽度（像素）
		// int height = metric.heightPixels; // 屏幕高度（像素）
		// SinoApplication.density = metric.density; // 屏幕密度（0.75 / 1.0 / 1.5）
		// int densityDpi = metric.densityDpi; // 屏幕密度DPI（120 / 160 / 240）
		// 此设备 width: 2560 hei: 1600 density: 2.0 densityDpi: 320
		// Log.d("sinopec", "width: " + width + "  hei: " + height +
		// "  density: "
		// + SinoApplication.density + "  densityDpi: " + densityDpi);
	}

	private IdentifyParameters mIdentifyParameters = new IdentifyParameters();

	private void initSearchParams(Point pt) {
		// TODO:设置Identify查询参数
		mIdentifyParameters.setTolerance(20);
		mIdentifyParameters.setDPI(98);
		mIdentifyParameters.setLayers(new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 });
		mIdentifyParameters.setLayerMode(IdentifyParameters.TOP_MOST_LAYER);
		// mIdentifyParameters.setLayers(new int[]{4});
		// mIdentifyParameters.setLayerMode(IdentifyParameters.ALL_LAYERS);

		mIdentifyParameters.setGeometry(pt);
		mIdentifyParameters.setSpatialReference(map.getSpatialReference());
		mIdentifyParameters.setMapHeight(map.getHeight());
		mIdentifyParameters.setMapWidth(map.getWidth());

		// add the area of extent to identify parameters
		Envelope env = new Envelope(pt);
		// map.setExtent(new Envelope(pt), 0);
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

		// imageAnim = (ImageView) findViewById(R.id.poiAnim);
		// imageAnim.setVisibility(View.INVISIBLE);
		mBitmapPaopaobg = BitmapFactory.decodeResource(getResources(),
				R.drawable.paopao_bg);

		callout = map.getCallout();
		callout.setContent(popView);
		callout.setStyle(R.layout.calloutwindow);
		callout.setMaxWidth(SinoApplication.screenWidth - 10);
		callout.setMaxHeight(SinoApplication.screenHeight);
		callout.setOffset(0, -mBitmapPaopaobg.getHeight() / 2);

		map.setClickable(true);// 设置地图可点击
		map.setClickable(false);// 设置地图可点击

		MarinedbActivity.this.map
				.setOnSingleTapListener(new OnSingleTapListener() {// 单击事件
					@Override
					public void onSingleTap(float x, float y) {
						if (mFragmentLayout.getVisibility() == View.VISIBLE) {
							// 显示的时候不相应，防止穿透
							return;
						}

						if (callout.isShowing()) {
							callout.hide();
						}
						// 高亮消失
						mDrawLayer4HighLight.removeAll();
					}
				});

		MarinedbActivity.this.map
				.setOnLongPressListener(new OnLongPressListener() {
					public void onLongPress(float x, float y) {
						if (!map.isLoaded()
								|| (mFragmentLayout.getVisibility() == View.VISIBLE)) {
							return;
						}
						isHideCallout = false;

						// 清空之前高亮显示
						drawLayer.removeAll();
						mDrawLayer4HighLight.removeAll();
						drawTool.deactivate();
						// 情况之前长按数据
						if (callout.isShowing()) {
							callout.hide();
							SinoApplication.identifyResult = null;
						}

						Point pt = MarinedbActivity.this.map.toMapPoint(x, y);
						// Log.d("map",
						// "-----长按------Long--x:" + x + "  y: " + y
						// + " -toMapPoint--x:" + pt.getX()
						// + "  y: " + pt.getY());
						x1 = pt.getX();
						y1 = pt.getY();
						name2 = "未知地名";
						address2 = "未知地址";
						// TODO:找到对象的属性等数据 url

						try {
							Point pxPt = MarinedbActivity.this.map
									.toScreenPoint(new Point(x1, y1));
							int pxx = 0;
							int pxy = 0;

							pxx = (int) (pxPt.getX()); // 图标宽度(像素)
							pxy = (int) (pxPt.getY()); // 图标高度(像素)
							anchorPt = MarinedbActivity.this.map.toMapPoint(
									pxx, pxy);

							// imageAnim.startAnimation(poiAnimation);

							Log.d(tag, "-------长按   x: " + anchorPt.getX()
									+ "  y: " + anchorPt.getY());
							initSearchParams(anchorPt);
							SearchIdentifyTask task = new SearchIdentifyTask(
//									mContext, pt, SinoApplication.oilUrl,
									mContext, pt, SinoApplication.currentLayerUrl4Multi,
									mLongTouchTitle,
									CommonData.TypeOperateLongPress,
									mDrawLayer4HighLight, callout);
							task.execute(mIdentifyParameters);

							if (LongPressPOI != null) {
								MarinedbActivity.this.gLayer
										.removeGraphic(LongPressPOI.getUid());
							}

						} catch (Exception e) {
							e.printStackTrace();
						}
						callout.setCoordinates(anchorPt);
						// if (!callout.isShowing()) {
						// callout.show();
						// }
					}

				});
	}

	private MenuButton mMenuViewCompare;
	private MenuButton mMenuViewCount;
	private MenuButton mMenuViewMine;
	private MenuButton mMenuViewTool;
	private MenuButton mMenuViewSearch;
	// private MenuButton mMenuViewCompare;
	// private MenuButton mMenuViewCount;
	// private MenuButton mMenuViewMine;
	// private MenuButton mMenuViewTool;
	// private MenuButton mMenuViewSearch;
	private ListView mListView;
	/**
	 * 底部五个按钮跟布局
	 */
	private ViewGroup mToolBar;

	private void initView() {
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
		// mProgressBar = (ProgressBar) findViewById(R.id.progressbar);
		mTVContent = (TextView) findViewById(R.id.tv_content);
		mTVContent.setMovementMethod(ScrollingMovementMethod.getInstance());
		mBtnLayer = (Button) findViewById(R.id.btn_map_layout);
		mBtnLayer.setOnClickListener(this);
		mBtnSearch = (MenuButtonNoIcon) findViewById(R.id.btn_search_confirm);
		mBtnSearch.setOnClickListener(this);

		mEditText = (Button) findViewById(R.id.edittext_search);
		mEditText.setOnClickListener(this);
		mMenuViewTool = (MenuButton) findViewById(R.id.menuview_tool);
		mMenuViewSearch = (MenuButton) findViewById(R.id.menuview_search);
		mMenuViewSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Boolean[] clickTag = new Boolean[] { true, true, true, true,
						true, true, true };
				ChildrenMenuDataUtil.setSearchChildrenMenuData(toolist,
						clickTag, mChildMenuSplitNumber);
				mGridView.setNumColumns(7);
				setGridView(toolist, arg0);
			}
		});
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
				if (mLeftBarLayout.getVisibility() == View.GONE) {
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

				// 清空上次查询数据
				SinoApplication.mResultList4FrameSearch.clear();
				SinoApplication.mFeatureSet4Query = null;
				mEditText.setText(getString(R.string.search));
				mTag4ToolDistanceOk = false;
				mTag4ToolAreaOk = false;

				if (mToolBar.getVisibility() == View.GONE) {
					mToolBar.setVisibility(View.VISIBLE);
					mToolBar.startAnimation(aniUp);
					mGridViewLayout.setVisibility(View.GONE);
					mLastClickedView = null;
				}
				SinoApplication.mResultList4Compared.clear();
				SinoApplication.mFeatureSet4Compared = null;
				SinoApplication.findResult = null;
			}
		});

		mBtnScaleSmall = (ImageButton) findViewById(R.id.btn_scale_small);
		mBtnScaleSmall.setOnClickListener(this);
		mBtnScaleBig = (ImageButton) findViewById(R.id.btn_scale_big);
		mBtnScaleBig.setOnClickListener(this);
		findViewById(R.id.btn_restore_map).setOnClickListener(this);
	}

	public void hideInput() {
		if (getCurrentFocus() != null) {
			((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
					.hideSoftInputFromWindow(
							getCurrentFocus().getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	private ArrayList<HashMap<String, Object>> mList = new ArrayList<HashMap<String, Object>>();
	private SearchAdapter mSearchAdapter;

	/**
	 * 初始化搜索结果列表数据
	 */
	private void initData() {
		if (SinoApplication.mOilGasData != null) {
			SinoApplication.mOilGasData.clear();
		}
		String[] urls = getResources().getStringArray(R.array.oilgas_url_theme);
		String[] urls_4search = getResources().getStringArray(
				R.array.oilgas_url_theme_4search);
		String[] ids = getResources().getStringArray(R.array.all_layer_id);
		String[] names = getResources().getStringArray(R.array.all_layer_name);
		String[] colors = getResources()
				.getStringArray(R.array.all_layer_color);
		// Log.d(tag, "- ********* --图层专题  url 数目 --main : "+urls.length);
		for (int i = 0; i < urls.length; i++) {
			OilGasData data = new OilGasData();
			data.setUrl(urls[i]);
			data.setSearchUrl(urls_4search[i]);
			data.setName(names[i]);
			data.setId(Integer.valueOf(ids[i]));
			Log.v("mandy", "colors: " + colors[i]);
			data.setColor(Color.parseColor(colors[i]));
			if (getString(R.string.url_basin).equals(urls[i])) {
				data.setChecked(true);
			} else {
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
		// Log.d(tag, "main----------------------onResume");
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

	private Boolean[] clickTag;

	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		if (mFragmentLayout.getVisibility() == View.VISIBLE) {
			// 显示的时候不相应，防止穿透
			return;
		}

		switch (v.getId()) {
		case R.id.property:
			launchNewPage("属性");
			break;
		case R.id.statistics:
			launchNewPage("统计");
			break;
		case R.id.doc:
			// Toast.makeText(mContext, "此功能暂未实现...",
			// Toast.LENGTH_SHORT).show();
			launchNewPage(getString(R.string.btn_introduce));
			break;
		case R.id.btn_map_layout: // 图层
			hideCallOut();
			if (mLayerDialog == null) {
				mLayerDialog = new LayerDialog();
			}
			mLayerDialog.setMapView(map);
			mLayerDialog.setMapServiceLayer(tms);
			mLayerDialog.setDrawLayer(drawLayer);
			mLayerDialog.show(getFragmentManager(), "dialog");

			break;
		case R.id.tb_frame: // 框选
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
			// 屏幕范围查询
			drawTool.queryAttribute4Query(map.getExtent());
			setButtonsStatus(v.getId());
			mTag4ToolAreaOk = true;
			mTag4ToolDistanceOk = false;
			break;
		case R.id.tb_multiple:
			// TODO:多选,进入多
			SinoApplication.mResultListMulti.clear();
			drawTool.activate(DrawTool.MULTI_POINT);
			setButtonsStatus(v.getId());// TODO:多选,进入多
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
			fragmentManager.beginTransaction()
					.replace(R.id.fragmentlayout, searchFragment).commit();
			break;
		case R.id.menuview_tool:
			if (drawTool.isSelectDraw) {
				clickTag = new Boolean[] { false, false };
				// clickTag = new Boolean[] { true, true };
				String[] name4count = new String[] { "测距(请先绘制一条线)",
						"测面积(请先选择范围)" };
				if (mTag4OperateInLine) {
					clickTag = new Boolean[] { true, false };
					name4count = new String[] { "测距", "测面积(请先选择范围)" };
				} else if (mTag4ToolDistanceOk) {
					clickTag = new Boolean[] { true, false };
					name4count = new String[] { "测距", "测面积(请先选择范围)" };
				} else if (mTag4ToolAreaOk) {
					clickTag = new Boolean[] { false, true };
					name4count = new String[] { "测距(请先绘制一条线)", "测面积" };
				}
				ChildrenMenuDataUtil.setToolChildrenMenuData(toolist, clickTag,
						name4count, mChildMenuSplitNumber);
				mGridView.setNumColumns(2);
				setGridView(toolist, v);
			} else {
				clickTag = new Boolean[] { false, false };
				// clickTag = new Boolean[] { true, true };
				String[] name4count = new String[] { "测距(请先绘制一条线)",
						"测面积(请先选择范围)" };
				if (mTag4OperateInLine) {
					clickTag = new Boolean[] { true, false };
					name4count = new String[] { "测距", "测面积(请先选择范围)" };
				} else if (mTag4ToolDistanceOk) {
					clickTag = new Boolean[] { true, false };
					name4count = new String[] { "测距", "测面积(请先选择范围)" };
				} else if (mTag4ToolAreaOk) {
					clickTag = new Boolean[] { false, true };
					name4count = new String[] { "测距(请先绘制一条线)", "测面积" };
				}
				ChildrenMenuDataUtil.setToolChildrenMenuData(toolist, clickTag,
						name4count, mChildMenuSplitNumber);
				mGridView.setNumColumns(2);
				setGridView(toolist, v);
			}

			break;
		case R.id.menuview_search:
			clickTag = new Boolean[] { true, true, true, true, true, true, true };
			ChildrenMenuDataUtil.setSearchChildrenMenuData(toolist, clickTag,
					mChildMenuSplitNumber);
			mGridView.setNumColumns(7);
			setGridView(toolist, v);

			break;
		case R.id.menuview_compare:

			if ((SinoApplication.mResultList4Compared == null || SinoApplication.mResultList4Compared
					.size() == 0)) {
				Toast.makeText(mContext, getString(R.string.tip_no_objects),
						Toast.LENGTH_SHORT).show();
			} else {
				showWindow4Compared(SinoApplication.mResultList4Compared);
			}

			if (SinoApplication.mFeatureSet4Compared == null) {
				Toast.makeText(mContext, getString(R.string.tip_no_objects),
						Toast.LENGTH_SHORT).show();
			} else {
				showWindow4Compared4FeatureSet(SinoApplication.mFeatureSet4Compared);
			}
			break;
		case R.id.menuview_count: // 统计点击事件

			clickTag = new Boolean[] { true, true, };
			ChildrenMenuDataUtil.setCountChildrenMenuData(toolist, clickTag,
					mChildMenuSplitNumber);
			mGridView.setNumColumns(2);
			setGridView(toolist, v);
			AllBasin();

			break;
		case R.id.menuview_mine:
			clickTag = new Boolean[] { true, true, true, true, true };
			ChildrenMenuDataUtil.setMineChildrenMenuData(toolist, clickTag,
					mChildMenuSplitNumber);
			mGridView.setNumColumns(5);
			setGridView(toolist, v);

			break;
		case R.id.btn_restore_map:
			
			Envelope envelope = new Envelope();
			envelope.setXMin(-180);
			envelope.setYMin(-89.99999999999994);
			envelope.setXMax(180.0000000000001);
			envelope.setYMax(87.93330000000003);
			map.setExtent(envelope);
			
			break;
		default:
			break;
		}
	}

	private void launchNewPage(String name) {
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
			if (btnFrame.isSelected()) {
				btnFrame.setSelected(false);
			} else {
				btnFrame.setSelected(true);
			}
			btnMultiple.setSelected(false);
			btnLine.setSelected(false);
			btnPolygon.setSelected(false);
			btnCurScreen.setSelected(false);
		} else if (btnLine.getId() == vId) {
			mTag4OperateInLine = true;
			if (btnLine.isSelected()) {
				btnLine.setSelected(false);
			} else {
				btnLine.setSelected(true);
			}
			btnMultiple.setSelected(false);
			btnFrame.setSelected(false);
			btnPolygon.setSelected(false);
			btnCurScreen.setSelected(false);
		} else if (btnPolygon.getId() == vId) {
			mTag4OperateInLine = false;
			if (btnPolygon.isSelected()) {
				btnPolygon.setSelected(false);
			} else {
				btnPolygon.setSelected(true);
			}
			btnMultiple.setSelected(false);
			btnFrame.setSelected(false);
			btnLine.setSelected(false);
			btnCurScreen.setSelected(false);
		} else if (btnCurScreen.getId() == vId) {
			mTag4OperateInLine = false;
			if (btnCurScreen.isSelected()) {
				btnCurScreen.setSelected(false);
			} else {
				btnCurScreen.setSelected(true);
			}
			btnMultiple.setSelected(false);
			btnFrame.setSelected(false);
			btnPolygon.setSelected(false);
			btnLine.setSelected(false);
		} else if (btnMultiple.getId() == vId) {
			mTag4OperateInLine = false;
			if (btnMultiple.isSelected()) {
				btnMultiple.setSelected(false);
			} else {
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

	// 测面积可用
	private boolean mTag4ToolAreaOk = false;
	// 测距可用
	private boolean mTag4ToolDistanceOk = false;

	// 设置底部按钮的点击效果
	private void setBottomMenuBarButtonsStatus(int vId) {
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
		hideCallOut4BottomMenuBar();
	}

	private void dealClickRepeat(MenuButton view) {
		// if (view.isChecked()) {
		// view.setChecked(false);
		// } else {
		// view.setChecked(true);
		// }
		if (view.isSelected()) {
			view.setSelected(false);
		} else {
			view.setSelected(true);
		}
	}

	private View mLastClickedView;
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
		mBaseLayout = (ViewGroup) layoutInflater.inflate(
				R.layout.view_menu_popwindow, null);
		popupWindow = new PopupWindow(mBaseLayout, 1000, 800);

		SinoUtil.showWindow4Compared(mContext, popupWindow, mBaseLayout, list);
		// popupWindow.showAtLocation(mBaseLayout, Gravity.NO_GRAVITY, 0, 0);
	}

	private void showWindow4Compared4FeatureSet(FeatureSet featureset) {
		LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mBaseLayout = (ViewGroup) layoutInflater.inflate(
				R.layout.view_menu_popwindow, null);
		popupWindow = new PopupWindow(mBaseLayout, 1000, 800);

		SinoUtil.showWindow4Compared4FeatureSet(mContext, popupWindow,
				mBaseLayout, featureset);
		// popupWindow.showAtLocation(mBaseLayout, Gravity.NO_GRAVITY, 0, 0);
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
		setBottomMenuBarButtonsStatus(view.getId());
		mAdapter.notifyDataSetChanged();
		// showAndHideGridView();
		if (mLastClickedView == view) {
			mGridViewLayout.setVisibility(View.INVISIBLE);
			mLastClickedView = null;
			Log.d(tag, "is view " + mLastClickedView);
		} else {
			mGridViewLayout.setVisibility(View.VISIBLE);
			mLastClickedView = view;
			Log.d(tag, "not view " + mLastClickedView);
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
		if ((!mTag4OperateInLine && !mTag4ToolDistanceOk && !mTag4ToolAreaOk)
				|| ("toolDistance".equals(tag) || "toolArea".equals(tag))) {
		} else {
			mLastClickedView = null;
		}
		HashMap<String, Boolean> showMap = (HashMap<String, Boolean>) map
				.get("clicktag");
		if (showMap != null && !showMap.get(tag)) {
			return;
		}

		Log.d("sinopec", "-------点击p: " + position + "  tag: " + tag);
		if ("toolDistance".equals(tag)) {

			hidePopupWindow4CountDistanceArea();
			drawTool.calculateAreaAndLength("KM");
		} else if ("toolArea".equals(tag)) {
			// 调用drawTool里面 的一个变量
			// drawTool.calculateAreaAndLength("");
		} else if ("定制查询".equals(tag)) {
			// TODO:
			ConditionQuery query = new ConditionQuery();
			query.show(getFragmentManager(), ConditionQuery.class.getName());

		} else if ("mineLogin".equals(tag)) {
//			Intent intent = new Intent(mContext, LoginActivity.class);
//			startActivity(intent);
			Intent intent = new Intent(this, SelectActivity.class);
			intent.putExtra(CommonData.KeyTopicType, "盆地");
			intent.putExtra("name", "简介");
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
		} else if ("CountChildrenMenuOne".equals(tag)) { // 碳酸盐岩储量及资源量分布
			// TODO:统计二级菜单
			Boolean[] clickTag = new Boolean[] { true, true, true, true };
			ChildrenMenuDataUtil.setCountLevelTwoChildrenMenuOneData(toolist,
					clickTag, mChildMenuSplitNumber);
			mGridView.setNumColumns(4);
			setGridView4LevelTwoChildrenMenu(toolist, arg0);

//			drawBarChart();

		} else if ("CountChildrenMenuTwo".equals(tag)) { // 分层系碳酸盐岩储量及资源量分布
			Boolean[] clickTag = new Boolean[] { true, true, true, true, true,
					true, true, true, true, true, true, };
			ChildrenMenuDataUtil.setCountLevelTwoChildrenMenuData(toolist,
					clickTag, mChildMenuSplitNumber);
			mGridView.setNumColumns(11);
			setGridView4LevelTwoChildrenMenu(toolist, arg0);

		} else if ("碳酸盐岩烃源分布".equals(tag)) {
			Boolean[] clickTag = new Boolean[] { true, true, true, true, true, true, true,
					true, true, true, true, true, };
			ChildrenMenuDataUtil.setSearchChildren4MenuData(toolist, clickTag,
					mChildMenuSplitNumber);
			mGridView.setNumColumns(12);
			setGridView4LevelTwoChildrenMenu(toolist, arg0);
               
			  AllBasin();

		} else if ("分类型盖层分布".equals(tag)) {

			Boolean[] clickTag = new Boolean[] { true, true, true, true, true  };
			ChildrenMenuDataUtil.setSearchChildren6MenuData(toolist, clickTag,
					mChildMenuSplitNumber);
			mGridView.setNumColumns(5);
			setGridView4LevelTwoChildrenMenu(toolist, arg0);

			 AllBasin();

		} else if ("碳酸盐岩储层分布".equals(tag)) {
			Boolean[] clickTag = new Boolean[] { true, true, true, true, true,
					true };
			ChildrenMenuDataUtil.setSearchChildren5MenuData(toolist, clickTag,
					mChildMenuSplitNumber);
			mGridView.setNumColumns(6);
			setGridView4LevelTwoChildrenMenu(toolist, arg0);

//			String chujikongjian = "72057594037927935";
//			String chenjixiang = "72057594037927935";
//			String tansuanyanyan = "72057594037927935";
//
//			String url = Constant.baseURL
//					+ "peprisapi/fixquery5.html?chujikongjian=" + chujikongjian
//					+ "&chenjixiang=" + chenjixiang + "&tansuanyanyan="
//					+ tansuanyanyan;
//			asyncHttpQuery.execute(5, url);
			 AllBasin();
			

		} else if ("海相碳酸盐岩盆地".equals(tag)) {
			AllBasin();

		} else if ("碳酸盐岩储量比例".equals(tag)) {

			Boolean[] clickTag = new Boolean[] { true, true, true };
			ChildrenMenuDataUtil.setSearchLevel23ChildrenMenuOneData(toolist, clickTag,
					mChildMenuSplitNumber);
			mGridView.setNumColumns(3);
			setGridView4LevelTwoChildrenMenu(toolist, arg0);

		} else if ("碳酸盐岩资源比例".equals(tag)) {
			Boolean[] clickTag = new Boolean[] { true, true, true };
			ChildrenMenuDataUtil.setSearchLevel33ChildrenMenuOneData(toolist, clickTag,
					mChildMenuSplitNumber);
			mGridView.setNumColumns(3);
			setGridView4LevelTwoChildrenMenu(toolist, arg0);
		
		} else if ("石油2".equals(tag)) {
			
			String type = "72057594037927935";
			queryYanyanchuLiang(type);
			
		} else if ("天然气2".equals(tag)) {
			
			String type = "72057594037927935";
			queryYanyanchuLiang(type);
			
		} else if ("石油天然气2".equals(tag)) {
			
			String type = "72057594037927935";
			queryYanyanchuLiang(type);
			
		} else if ("石油3".equals(tag)) {
			
			String type = "72057594037927935";
			queryYanyanZiyuan(type);
			
		} else if ("天然气3".equals(tag)) {
			
			String type = "72057594037927935";
			queryYanyanZiyuan(type);
			
		} else if ("石油天然气3".equals(tag)) {
			
			String type = "72057594037927935";
			queryYanyanZiyuan(type);
			
		} else if ("储量及资源量级别".equals(tag)) {
			drawBarChart();
			
		} else if ("资源总量".equals(tag)) {
			drawBarChart();
			
		} else if ("探明储量".equals(tag)) {
			drawBarChart();
			
		} else if ("待发现资源量".equals(tag)) { 
			drawBarChart();
			
		} else if ("前寒武系s".equals(tag)) {
			drawBarChart();  
			
		} else if ("寒武系s".equals(tag)) {
			drawBarChart();
			
		}else if ("至留系s".equals(tag)) {
			drawBarChart();
			
		}else if ("泥盆系s".equals(tag)) {
			drawBarChart();
			
		} else if ("二叠系s".equals(tag)) {
			drawBarChart();
			
		}else if ("奥陶系s".equals(tag)) {
			drawBarChart();
			
		}else if ("侏罗系s".equals(tag)) {
			drawBarChart();
			
		}else if ("白垩系s".equals(tag)) {
			drawBarChart();
			
		}else if ("石炭系s".equals(tag)) {
			drawBarChart();
			
		}else if ("古近系s".equals(tag)) {
			drawBarChart();
			
		} else if ("新近系s".equals(tag)) {
			drawBarChart();
			
		} else if ("前寒武系".equals(tag)) {
			
			queryQingyuan(RelativeUnicode.qianhaiwuxi);
			
		}else if ("志留系".equals(tag)) {
			queryQingyuan(RelativeUnicode.zhiliuxi);
			
		}else if ("泥盆系".equals(tag)) {
			queryQingyuan(RelativeUnicode.nipenxi);
			
		}else if ("石炭系".equals(tag)) {
			queryQingyuan(RelativeUnicode.shitanxi);
			
		}else if ("二叠系".equals(tag)) {
			queryQingyuan(RelativeUnicode.erdiexi);
			
		}else if ("三叠系".equals(tag)) {
			queryQingyuan(RelativeUnicode.sandiexi);
			
		}else if ("侏罗系".equals(tag)) {
			queryQingyuan(RelativeUnicode.zhuluoxi);
			
		}else if ("白垩系".equals(tag)) {
			queryQingyuan(RelativeUnicode.baiyaxi);
			
		}else if ("古近系".equals(tag)) {
			queryQingyuan(RelativeUnicode.gujinxi);
			
		}else if ("新近系".equals(tag)) {
			queryQingyuan(RelativeUnicode.xinjinxi);
			
		}else if ("寒武系".equals(tag)) {
			queryQingyuan(RelativeUnicode.hanwuxi);
			
		}else if ("奥陶系".equals(tag)) {
			queryQingyuan(RelativeUnicode.aotaoxi);
			
		}else if ("泥岩盖层".equals(tag)) {
			String gaiceng = "72057594037927935";
			queryGaiceng(RelativeUnicode.niyangaiceng);

		} else if ("膏盐岩盖层".equals(tag)) {
			String gaiceng = "72057594037927935";
			queryGaiceng(RelativeUnicode.gaoyanyangaiceng);
			
		} else if ("碳酸盐岩盖层".equals(tag)) {
			String gaiceng = "72057594037927935";
			queryGaiceng(RelativeUnicode.tansuanyanyangaiceng);
			
		} else if ("其它致密岩盖层".equals(tag)) {
			String gaiceng = "72057594037927935";
			queryGaiceng(RelativeUnicode.zhimiyangaiceng);
			
		} else if ("特殊盖层".equals(tag)) {
			String gaiceng = "72057594037927935";
			queryGaiceng(RelativeUnicode.teshugaiceng);
			
		} 
		// 三级子菜单都需要在这里处理
		// if (!"CountChildrenMenuOne".equals(tag)
		// && !"CountChildrenMenuTwo".equals(tag)
		// && !"toolDistance".equals(tag) && !"碳酸盐岩烃源分布".equals(tag)
		// && !"分类型盖层分布".equals(tag) && !"toolArea".equals(tag)) {
		// hideCallOut();
		// }
		if (!"CountChildrenMenuOne".equals(tag)
				&& !"CountChildrenMenuTwo".equals(tag)
				&& !"toolDistance".equals(tag) && !"toolArea".equals(tag)
				&& !"分类型盖层分布".equals(tag) && !"碳酸盐岩烃源分布".equals(tag)
				&& !"碳酸盐岩储量比例".equals(tag) && !"碳酸盐岩资源比例".equals(tag)
				&& !"储量及资源量级别".equals(tag) && !"资源总量".equals(tag)
				&& !"探明储量".equals(tag) && !"待发现资源量".equals(tag)
				&& !"前寒武系s".equals(tag) && !"寒武系s".equals(tag)
				&& !"至留系s".equals(tag) && !"泥盆系s".equals(tag)
				&& !"二叠系s".equals(tag) && !"奥陶系s".equals(tag)
				&& !"侏罗系s".equals(tag) && !"白垩系s".equals(tag)
				&& !"石炭系s".equals(tag) && !"古近系s".equals(tag)
				&& !"新近系s".equals(tag)&& !"前寒武系".equals(tag)
				&& !"志留系".equals(tag)&& !"二叠系".equals(tag)
				&& !"三叠系".equals(tag)&& !"侏罗系".equals(tag)
				&& !"白垩系".equals(tag)&& !"古近系".equals(tag)
				&& !"新近系".equals(tag)&& !"寒武系".equals(tag)
				&& !"奥陶系".equals(tag)&& !"石油2".equals(tag)
				&& !"石油3".equals(tag)&& !"天然气2".equals(tag)
				&& !"天然气3".equals(tag)&& !"石炭系".equals(tag)
				&& !"泥盆系".equals(tag)&&!"泥岩盖层".equals(tag)
				&& !"膏盐岩盖层".equals(tag)&&!"碳酸盐岩盖层".equals(tag)
				&& !"其它致密岩盖层".equals(tag)&&!"特殊盖层".equals(tag)
				&& !"石油天然气2".equals(tag)
				&& !"石油天然气3".equals(tag)&& !"".equals(tag)
				&& !"分类型盖层分布".equals(tag) && !"碳酸盐岩烃源分布".equals(tag)) {
			hideCallOut();
			
		}else{
			mMenuViewCount.setSelected(false);
			mMenuViewSearch.setSelected(false);
			mMenuViewCompare.setSelected(false);
			mMenuViewMine.setSelected(false);
		}

		if ("toolDistance".equals(tag) || "toolArea".equals(tag)) {
			if (callout != null) {
				if (callout.isShowing()) {
					callout.hide();
				}
			}

			mDrawLayer4HighLight.removeAll();
			// mMenuViewTool.setSelected(false);
			mMenuViewCount.setSelected(false);
			mMenuViewSearch.setSelected(false);
			mMenuViewCompare.setSelected(false);
			mMenuViewMine.setSelected(false);
		}
	}

	private void AllBasin() {
		String chenjitixi = "72057594037927935";
		String url = Constant.distributeOilGas + chenjitixi;
		asyncHttpQuery.execute(1, url);
	}

	private void queryQingyuan (String cengxi) { 

		String url = Constant.distributeHydrocSource + cengxi;
		asyncHttpQuery.execute(4, url);
		
	}
	
	private void queryYanyanchuLiang (String type) {
		
//		String type = "72057594037927935";
		String haixiang = "72057594037927935";
		String tansuanyanyan = "72057594037927935";
		String url = Constant.distributeRate + "type=" + type
				+ "&haixiang=" + haixiang + "&tansuanyanyan="
				+ tansuanyanyan;

		asyncHttpQuery.execute(2, url);
		
	}
	private void queryYanyanZiyuan (String type) {
		
//		String type = "72057594037927935";
		String tansuanyanyan = "72057594037927935";
		String url = Constant.baseURL + "peprisapi/fixquery3.html?type="
				+ type + "&tansuanyanyan=" + tansuanyanyan;
		asyncHttpQuery.execute(3, url);
	}
	
	private void queryGaiceng (String gaiceng) {
//		String gaiceng = "72057594037927935";
		String url = Constant.baseURL + "peprisapi/fixquery6.html?gaiceng="
				+ gaiceng;
		asyncHttpQuery.execute(6, url);
		
	}
	
	private void drawPinChart() {
		Graphic[] graphics = drawTool.getQueryGraphics();
		if (graphics == null) {
			return;
		}
		for (int i = 0; i < graphics.length; i++) {
			Envelope envelope = new Envelope();
			graphics[i].getGeometry().queryEnvelope(envelope);
			PieChart3 p3 = new PieChart3(200, 400, 500, 300);
			Bitmap bitmap = p3.GetBarChartBitmap(this);
			// gView.setBackgroundColor(Color.TRANSPARENT);
			PictureMarkerSymbol Symbol = new PictureMarkerSymbol(
					new BitmapDrawable(bitmap));

			Graphic graphic = new Graphic(envelope.getCenter(), Symbol);

			// Graphic graphic = new Graphic(new Point(0,0),Symbol);
			drawLayer.addGraphic(graphic);
		}
	}

	private void drawBarChart() {
		Graphic[] graphics = drawTool.getQueryGraphics();
		if (graphics == null) {
			return;
		}
		drawLayer.removeAll();
		for (int i = 0; i < graphics.length; i++) {

			Envelope envelope = new Envelope();
			graphics[i].getGeometry().queryEnvelope(envelope);
			BarChart3 b3 = new BarChart3(200, 400, 600, 100, 200);

			Bitmap bi = b3.GetBarChartBitmap(this);
			PictureMarkerSymbol Symbol = new PictureMarkerSymbol(
					new BitmapDrawable(bi));

			Graphic graphic = new Graphic(envelope.getCenter(), Symbol);
			// Graphic graphic = new Graphic(new Point(0,0),Symbol);
			drawLayer.addGraphic(graphic);

		}
	}

	private void testParseJson(String ss) {

		// // 由于是内网在家里访问不了所以现在把json放到文件中进行解析
		// AssetManager assetManager = getAssets();
		// InputStream inputStream = null;
		// try {
		// inputStream = assetManager.open("json1");
		// } catch (IOException e) {
		// Log.e("tag", e.getMessage());
		// }
		// String s = readTextFile(inputStream);

		//
		JsonParse jsonParse = new JsonParse();

		try {
			List<HashMap<String, Object>> list = jsonParse
					.parseItemsJson(new JsonReader(new StringReader(ss)));

			Log.v("mandy", "共有多少条数据: " + list.size());

			for (HashMap<String, Object> hashMap : list) {

				for (Entry<String, Object> hashMaps : hashMap.entrySet()) {

					Log.v("mandy", "parent key and value: " + hashMaps.getKey()
							+ ": " + hashMaps.getValue());

					if (hashMaps.getValue() instanceof HashMap) {

						for (Map.Entry<String, Object> hashMap3 : ((HashMap<String, Object>) hashMaps
								.getValue()).entrySet()) {

							Log.v("mandy",
									"child key and value: " + hashMap3.getKey()
											+ ": " + hashMap3.getValue());

						}
					} else { // 否则就是list

						// for (int i = 0; i < (ArrayList)hashMaps.size(); i++)
						// {
						//
						// }

					}

				}
				//
			}
			//

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private String readTextFile(InputStream inputStream) {

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		byte buf[] = new byte[1024];

		int len;

		try {

			while ((len = inputStream.read(buf)) != -1) {

				outputStream.write(buf, 0, len);

			}

			outputStream.close();

			inputStream.close();

		} catch (IOException e) {

		}

		return outputStream.toString();

	}

	private void setGridView4LevelTwoChildrenMenu(
			ArrayList<HashMap<String, Object>> list, View view) {
		// setMenuButtonsStatus(view.getId());
		mAdapter.notifyDataSetChanged();
		mToolBar.setVisibility(View.GONE);
		mToolBar.startAnimation(aniDown);
		mGridViewLayout.setVisibility(View.VISIBLE);

	}

	private void hidePopupWindow() {
		if (popupWindow != null) {
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
		if ((!mTag4ToolDistanceOk && !mTag4ToolAreaOk)) {
			// 都不可用的时候
			if (popupWindow != null) {
				popupWindow.dismiss();
			}
		} else {
			if (mTag4ToolDistanceOk || mTag4ToolAreaOk) {

			} else {

			}
		}

	}

	class MapTouchListener extends MapOnTouchListener {

		public MapTouchListener(Context context, MapView view) {
			super(context, view);

		}

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// Log.d(tag, "----MapTouchListener---onTouch--- ");
			// if(mGridViewLayout.getVisibility() == View.VISIBLE)
			// mGridViewLayout.setVisibility(View.GONE);
			return super.onTouch(v, event);
		}

		@Override
		public boolean onDoubleTap(MotionEvent point) {
			Log.d(tag, "----MapTouchListener---onDoubleTap--- ");
			if (mFragmentLayout.getVisibility() == View.VISIBLE) {
				// 显示的时候不相应，防止穿透
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
		if (mFragmentLayout.getVisibility() == View.VISIBLE) {
			mFragmentLayout.setVisibility(View.GONE);
		} else {
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
		mDrawLayer4HighLight.removeAll();
		closeKeyboard();
		if (data == null) {
			return;
		}
		FindResult result = (FindResult) data;

		Set<Entry<String, Object>> ents = result.getAttributes().entrySet();
		Log.d("data", "setData4Frame  获得图层 id : " + result.getLayerId()
				+ " name: " + result.getLayerName());
		for (Entry<String, Object> ent : ents) {
			Log.d("data",
					"模糊查询  key: " + ent.getKey() + "  val: " + ent.getValue());
		}

		SinoApplication.findResult = result;
		Geometry geometry = result.getGeometry();

		Envelope envelope = new Envelope();
		geometry.queryEnvelope(envelope);
		Point point = envelope.getCenter();
		// Log.d(tag, "-------查询出的坐标  x: "+point.getX()+"  y: "+point.getY());
		// 正确的比例尺
		map.setExtent(geometry, 0);
		// 绘制高亮区域
//		SimpleFillSymbol resultSymbol = new SimpleFillSymbol(Color.YELLOW);
//		Graphic resultLocation = new Graphic(geometry, resultSymbol);
//		mDrawLayer4HighLight.addGraphic(resultLocation);

		mTopicType = result.getLayerName();
		SinoApplication.mTopicName = result.getValue();
		mHashMap4Property = (HashMap<String, Object>) result.getAttributes();
		if (callout.isShowing()) {
			callout.hide();
		}
		
		if("chinapoi".equals(result.getLayerName())){
			MarkerSymbol markerSymbol = new SimpleMarkerSymbol(Color.BLUE, 30,
					SimpleMarkerSymbol.STYLE.CROSS);
			Graphic resultLocation = new Graphic(geometry, markerSymbol);
			mDrawLayer4HighLight.addGraphic(resultLocation);
		}else{
			SimpleFillSymbol resultSymbol = new SimpleFillSymbol(Color.YELLOW);
			Graphic resultLocation = new Graphic(geometry, resultSymbol);
			mDrawLayer4HighLight.addGraphic(resultLocation);
			
		}
		
		Log.d(tag,
				"-------------main---------setData: " + result.getLayerName());
		if (!"chinapoi".equals(result.getLayerName())
				&& !"国家".equals(result.getLayerName())) {
			callout.show(point);
			mLongTouchTitle.setText(result.getValue());
			// statistics.setText(result.getValue());
		}

		hideInput();
		fragmentManager.beginTransaction().remove(searchFragment).commit();
		searchFragment = null;
		mFragmentLayout.setVisibility(View.GONE);
	}

	private void hideCallOut() {
		if (callout != null) {
			if (callout.isShowing()) {
				callout.hide();
			}
		}

		mDrawLayer4HighLight.removeAll();
		if (mGridViewLayout.getVisibility() == View.VISIBLE) {
			mGridViewLayout.setVisibility(View.GONE);
			// 底下四个按钮置为常态
			mMenuViewTool.setSelected(false);
			mMenuViewCount.setSelected(false);
			mMenuViewSearch.setSelected(false);
			mMenuViewCompare.setSelected(false);
			mMenuViewMine.setSelected(false);
		}
	}

	private void hideCallOut4BottomMenuBar() {
		if (callout != null) {
			if (callout.isShowing()) {
				callout.hide();
			}
		}

		mDrawLayer4HighLight.removeAll();
		if (mGridViewLayout.getVisibility() == View.VISIBLE) {
			mGridViewLayout.setVisibility(View.GONE);
		}
	}

	// 框选查询返回的数据
	@Override
	public void setSearchData(ArrayList<IdentifyResult> list) {
		// 搜索按钮显示提示（点击查看详情）
		if (list != null) {
			SinoApplication.mResultList4FrameSearch = list;
			int size = list.size();
			if (size == 0) {
				SinoApplication.mLayerName = "";
				Toast.makeText(mContext, getString(R.string.search_no_result),
						Toast.LENGTH_LONG).show();
			} else {
				SinoApplication.mLayerName = list.get(0).getLayerName();
				mEditText.setText("在 [" + list.get(0).getLayerName()
						+ "] 图层查询到 " + size + " 个 结果，点击进入详情页面。");
				mLeftBarLayout.startAnimation(aniOut);
				mLeftBarLayout.setVisibility(View.GONE);
				SinoApplication.mResultList4Compared = list;
			}
		} else {
			SinoApplication.mLayerName = "";
			Toast.makeText(mContext, getString(R.string.search_no_result),
					Toast.LENGTH_LONG).show();
		}
		//
	}

	@Override
	public void setData4Frame(Object data) {
		drawLayer.removeAll();
		IdentifyResult result = (IdentifyResult) data;
		SinoApplication.identifyResult = result;
		Geometry geometry = result.getGeometry();

		Envelope envelope = new Envelope();
		geometry.queryEnvelope(envelope);
		Point point = envelope.getCenter();
		map.zoomToScale(point, 5000000);

		// 绘制高亮区域
		SimpleFillSymbol resultSymbol = new SimpleFillSymbol(Color.YELLOW);
		Graphic resultLocation = new Graphic(geometry, resultSymbol);
		mDrawLayer4HighLight.addGraphic(resultLocation);

		mTopicType = result.getLayerName();
		// mTopicName = getIdentifyResultName(result);
		mHashMap4Property = (HashMap<String, Object>) result.getAttributes();
		if (callout.isShowing()) {
			callout.hide();
		}
		callout.show(point);
		Set<Entry<String, Object>> ents = result.getAttributes().entrySet();
		Log.d("data", "setData4Frame  获得图层 id : " + result.getLayerId()
				+ " name: " + result.getLayerName());
		for (Entry<String, Object> ent : ents) {
			Log.d("data",
					"井  key: " + ent.getKey() + "  val: " + ent.getValue());
		}

		mLongTouchTitle.setText(SinoApplication.getIdentifyResultNameByType(
				result, result.getLayerName()));
		// statistics.setText(result.getValue());

		hideInput();
		fragmentManager.beginTransaction().remove(searchFragment).commit();
		searchFragment = null;
		mFragmentLayout.setVisibility(View.GONE);
	}

	@Override
	public void setSearchData4Query(FeatureSet results) {
		if (results != null) {
			SinoApplication.mFeatureSet4Query = results;
			// SinoApplication.mResultList4FrameSearch = list;
			int size = results.getGraphics().length;
			if (size == 0) {
				SinoApplication.mLayerName = "";
				Toast.makeText(mContext, getString(R.string.search_no_result),
						Toast.LENGTH_LONG).show();
			} else {
				// SinoApplication.mLayerName = list.get(0).getLayerName();
				mEditText.setText("在 [" + SinoApplication.mLayerName
						+ "] 图层查询到 " + size + " 个 结果，点击进入详情页面。");
				mLeftBarLayout.startAnimation(aniOut);
				mLeftBarLayout.setVisibility(View.GONE);
			}
		} else {
			SinoApplication.mLayerName = "";
			Toast.makeText(mContext, getString(R.string.search_no_result),
					Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void setData4Query(Object data) {
		Graphic graphic = (Graphic) data;
		// FindResult result = (FindResult)data;

		Set<Entry<String, Object>> ents = graphic.getAttributes().entrySet();
		for (Entry<String, Object> ent : ents) {
			Log.d("data", "框选查询 setData4Query  key: " + ent.getKey()
					+ "  val: " + ent.getValue());
		}

		// SinoApplication.findResult = result;
		Geometry geometry = graphic.getGeometry();

		Envelope envelope = new Envelope();
		geometry.queryEnvelope(envelope);
		Point point = envelope.getCenter();
		map.zoomToScale(point, 5000000);

		// 绘制高亮区域
		SimpleFillSymbol resultSymbol = new SimpleFillSymbol(Color.YELLOW);
		Graphic resultLocation = new Graphic(geometry, resultSymbol);
		mDrawLayer4HighLight.addGraphic(resultLocation);

		mTopicType = SinoApplication.mLayerName;
		// mTopicName = result.getValue();
		mHashMap4Property = (HashMap<String, Object>) graphic.getAttributes();
		if (callout.isShowing()) {
			callout.hide();
		}
		Log.d(tag, "-------------setData4Query---------setData: "
				+ SinoApplication.mLayerName);
		callout.show(point);
		String title = (String) graphic.getAttributes().get(
				"OBJ_NAME_C");
		SinoApplication.mTopicName = title;
		mLongTouchTitle.setText(title);
		// statistics.setText(result.getValue());

		hideInput();
		fragmentManager.beginTransaction().remove(searchFragment).commit();
		searchFragment = null;
		mFragmentLayout.setVisibility(View.GONE);

	}

	@Override
	public void setData4LongPressed(Object data) {
	}

	@Override
	public void returnNoResult() {
		if (callout != null) {
			if (callout.isShowing()) {
				callout.hide();
			}
		}
	}
	
	public DrawTool getDrawTool(){
		return drawTool;
	}
}
