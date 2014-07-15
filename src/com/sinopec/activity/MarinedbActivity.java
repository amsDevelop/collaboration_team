package com.sinopec.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.esri.android.map.Callout;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapOnTouchListener;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISFeatureLayer;
import com.esri.android.map.ags.ArcGISFeatureLayer.MODE;
import com.esri.android.map.ags.ArcGISFeatureLayer.Options;
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
import com.sinopec.adapter.MenuGridAdapter;
import com.sinopec.application.SinoApplication;
import com.sinopec.view.MenuButton;

public class MarinedbActivity extends Activity implements OnClickListener {
	private String tag = "MainActivity";
	private TextView mTVContent;
	private ProgressBar mProgressBar;

	private TextView mTextView;

	private LinearLayout mToolBar;
	private Button mBtnMenuTool;
	private Button mBtnMenuCount;
	private Button mBtnMenuCompare;
	private Button mBtnMenuMine;

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
	private Button mBtnLayer;
	private MenuButton mBtnSearch;
	private EditText mEditText;
	private PopupWindow popupWindow;
	private Context mContext;
	private Button btnFrame,btnPolygon,btnLine;
	 /**
	  * 子菜单
	  */
	 private GridView mGridView;
	private GraphicsLayer drawLayer;
	private SimpleLineSymbol lineSymbol;
	private SimpleMarkerSymbol markerSymbol;
	private SimpleFillSymbol fillSymbol;
	private MapTouchListener mapTouchListener;
	
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
		map.setExtent(new Envelope(-10868502.895856911, 4470034.144641369,
				-10837928.084542884, 4492965.25312689), 0);
		ArcGISTiledMapServiceLayer tms = new ArcGISTiledMapServiceLayer(
				"http://services.arcgisonline.com/ArcGIS/rest/services/World_Street_Map/MapServer");
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
		initSymbols();//初始化符号
	
		//add by gaolixiao
		addPopupWindow();
		getAboutDisplay();
		initLayout();
		mapTouchListener = new MapTouchListener(this, map);
		map.setOnTouchListener(mapTouchListener);
	}
	private void initSymbols(){
    	lineSymbol = new SimpleLineSymbol(Color.RED, 5, SimpleLineSymbol.STYLE.DASH);
    	markerSymbol = new SimpleMarkerSymbol(Color.BLUE, 10, SimpleMarkerSymbol.STYLE.CIRCLE);
    	fillSymbol = new SimpleFillSymbol(Color.RED);
    	fillSymbol.setAlpha(33);
    }
	private void initLayout() {
		btnFrame = (Button)findViewById(R.id.tb_frame);
		btnPolygon = (Button)findViewById(R.id.tb_polygon);
		btnLine = (Button)findViewById(R.id.tb_line);
		btnFrame.setOnClickListener(this);
		btnPolygon.setOnClickListener(this);
		btnLine.setOnClickListener(this);
	}

	public void getAboutDisplay() {
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		int width = metric.widthPixels; // 屏幕宽度（像素）
		int height = metric.heightPixels; // 屏幕高度（像素）
		SinoApplication.density = metric.density; // 屏幕密度（0.75 / 1.0 / 1.5）
		int densityDpi = metric.densityDpi; // 屏幕密度DPI（120 / 160 / 240）
		//此设备 width: 2560  hei: 1600  density: 2.0  densityDpi: 320
		Log.d("sinopec", "width: "+width+"  hei: "+height+"  density: "+ SinoApplication.density+"  densityDpi: "+densityDpi);
	}

	private void addPopupWindow() {
		
		 //泡泡代码        
		popView = View.inflate(this, R.layout.paopao, null);		
//		ImageView imageSMS = (ImageView)popView.findViewById(R.id.tvImageSMS);	
//		ImageView imageNavi = (ImageView)popView.findViewById(R.id.tvImageNavi);	
		property = (Button)popView.findViewById(R.id.property);
		statistics = (Button)popView.findViewById(R.id.statistics);
		doc = (Button)popView.findViewById(R.id.doc);
		property.setOnClickListener(this);
		statistics.setOnClickListener(this);
		doc.setOnClickListener(this);
		
		Drawable drawable1 = getResources().getDrawable(R.drawable.info1);
//		imageSMS.setBackgroundDrawable(drawable1);
//		imageSMS.getBackground().setAlpha(100);
		Drawable drawable2 = getResources().getDrawable(R.drawable.info3);
//		imageNavi.setBackgroundDrawable(drawable2);
//		imageNavi.getBackground().setAlpha(100);		
//		poptitle.getBackground().setAlpha(100);
		
		imageAnim = (ImageView)findViewById(R.id.poiAnim);
		imageAnim.setVisibility(View.INVISIBLE);
		callout = map.getCallout();		
		callout.setContent(popView);
		callout.setStyle(R.layout.calloutwindow);
		callout.setMaxWidth(SinoApplication.screenWidth-10);
		callout.setMaxHeight(SinoApplication.screenHeight);
		
		map.setClickable(true);//设置地图可点击
		
		MarinedbActivity.this.map.setOnSingleTapListener(new OnSingleTapListener() {//单击事件
			@Override
			public void onSingleTap(float x, float y) {
				if(callout.isShowing())
					callout.hide();				
			}
		});
		
		MarinedbActivity.this.map.setOnLongPressListener(new OnLongPressListener(){
			public void onLongPress(float x, float y)
			{
				if(!map.isLoaded()){
					return;
				}  
        		Point pt = MarinedbActivity.this.map.toMapPoint(x, y);         		
	    		x1 = pt.getX();
	    		y1 = pt.getY();	    	
	    		name2 = "未知地名";
	    		address2 = "未知地址";
//        		poptitle.setText("查询中,请稍侯...查询中,请稍侯...查询中,请稍侯...查询中,请稍侯...查询中,请稍侯...查询中,请稍侯...");
        		
        		try{
		    	curX = x1;
		    	curY = y1;
		    	curname = name2;
		    	curadd = address2;
		    	Point pxPt = MarinedbActivity.this.map.toScreenPoint(new Point(x1, y1)); 
		    	int pxx = 0;
		    	int pxy = 0;	    		
		    		
		    	pxx = (int)(pxPt.getX());	//图标宽度(像素)
		    	pxy = (int)(pxPt.getY()); 		//图标高度(像素)
		    	anchorPt = MarinedbActivity.this.map.toMapPoint(pxx,pxy);        			
				//添加图标
			    if(LongPressPOI!=null)
			    {
			    	MarinedbActivity.this.gLayer.removeGraphic(LongPressPOI.getUid());
			    }	
			    Animation poiAnimation = new TranslateAnimation(pxx, pxx, 0, y);
			    poiAnimation.setDuration(500);
			    imageAnim.startAnimation(poiAnimation);
			    //MarinedbActivity.this.gLayer.postInvalidate();
			    poiAnimation.setAnimationListener(new AnimationListener() {
					
					@Override
					public void onAnimationStart(Animation animation) {
						// TODO Auto-generated method stub
						imageAnim.setVisibility(View.VISIBLE);
					}
					
					@Override
					public void onAnimationRepeat(Animation animation) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onAnimationEnd(Animation animation) {
						// TODO Auto-generated method stub
						imageAnim.setVisibility(View.INVISIBLE);
		        		
						callout.setCoordinates(anchorPt);
		        		if(!callout.isShowing())
		        			callout.show();	    		
		        		callout.refresh();
		 						
					}
				});					

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}//onLongPress
		});
		
		
	}

	
	private MenuButton mMenuViewCompare;
	private MenuButton mMenuViewCount;
	private MenuButton mMenuViewMine;
	private MenuButton mMenuViewTool;
	private void initView() {
		mToolBar = (LinearLayout) findViewById(R.id.menu_bar);

		// mBtnMenuTool = (Button) findViewById(R.id.menu_tool);
//		mBtnMenuCount = (Button) findViewById(R.id.menu_count);
//		mBtnMenuCompare = (Button) findViewById(R.id.menu_compare);
//		mBtnMenuMine = (Button) findViewById(R.id.menu_mine);
		mGridView = (GridView) findViewById(R.id.menu_gridview);
		mProgressBar = (ProgressBar) findViewById(R.id.progressbar);
		mTVContent = (TextView) findViewById(R.id.tv_content);
		mTVContent.setMovementMethod(ScrollingMovementMethod.getInstance());
		mBtnLayer = (Button)findViewById(R.id.btn_map_layout);
		mBtnLayer.setOnClickListener(this);
		mBtnSearch = (MenuButton)findViewById(R.id.btn_search_confirm);
		mBtnSearch.setOnClickListener(new OnClickListener() {
			
			@SuppressLint("NewApi")
			@Override
			public void onClick(View arg0) {
				SearchFragment search =	new SearchFragment(mEditText.getText());
				search.show(getFragmentManager(), "search");
			}
		});
		
		mEditText = (EditText) findViewById(R.id.edittext_search);
		mMenuViewCount = (MenuButton) findViewById(R.id.menuview_count);
		mMenuViewCount.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
//				showWindow(mMenuViewCount);
				String[] name4count = new String[]{"范围内油气田的个数、面积、储量(油、气、...)", "范围内油气田的个数密度、面积密度",
						"范围内油气田的储量丰度(吨油当量/平方公里)", "石油、天然气及凝析油储量在各油气田的分布",
						"不同沉积体系油气田个数", "不同沉积体系油气田面积"};
				Integer[] icon4count = {R.drawable.icon_compare_0, R.drawable.icon_compare_1, R.drawable.icon_compare_2, 
						R.drawable.icon_compare_3, R.drawable.icon_compare_4, 
						R.drawable.icon_compare_5, R.drawable.icon_compare_6, };
				int splitNumber = 2;
				ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String,Object>>();
				for (int i = 0; i < name4count.length; i++) {
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("name", name4count[i]);
					map.put("icon", icon4count[i]);
					map.put("tag", name4count[i]);
					map.put("split", splitNumber);
					list.add(map);
				}
				setGridView(list);
				
			}
		});
		
		mMenuViewCompare = (MenuButton) findViewById(R.id.menuview_compare);
		mMenuViewCompare.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String[] name4count = new String[]{"待定"};
				Integer[] icon4count = {R.drawable.icon_compare_0 };
				int splitNumber = 1;
				ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String,Object>>();
				for (int i = 0; i < name4count.length; i++) {
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("name", name4count[i]);
					map.put("icon", icon4count[i]);
					map.put("tag", name4count[i]);
					map.put("split", splitNumber);
					list.add(map);
				}
				setGridView(list);
			}
		});
		
		
		mMenuViewTool = (MenuButton) findViewById(R.id.menuview_tool);
		mMenuViewTool.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String[] name4count = new String[]{"测距", "测面积"};
				Integer[] icon4count = {R.drawable.icon_compare_0, R.drawable.icon_compare_1 };
				int splitNumber = 2;
				ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String,Object>>();
				for (int i = 0; i < name4count.length; i++) {
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("name", name4count[i]);
					map.put("icon", icon4count[i]);
					map.put("tag", name4count[i]);
					map.put("split", splitNumber);
					list.add(map);
				}
				setGridView(list);
			}
		});
		
		mMenuViewMine = (MenuButton) findViewById(R.id.menuview_mine);
		mMenuViewMine.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String[] name4count = new String[]{"登陆", "账户管理", "收藏", "下载", "退出"};
				Integer[] icon4count = {R.drawable.icon_compare_0, R.drawable.icon_compare_1, R.drawable.icon_compare_2, 
						R.drawable.icon_compare_3, R.drawable.icon_compare_4 };
				int splitNumber = 2;
				ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String,Object>>();
				for (int i = 0; i < name4count.length; i++) {
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("name", name4count[i]);
					map.put("icon", icon4count[i]);
					map.put("tag", name4count[i]);
					map.put("split", splitNumber);
					list.add(map);
				}
				setGridView(list);
			}
		});
		
	}
	
	private void initData() {
		mProgressBar.setVisibility(View.VISIBLE);
	}

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
		map.unpause();
		closeKeyboard();
	}

	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		if (property.getId() == v.getId()) {
			Log.v("mandy", "property is clicked");
			Intent intent = new Intent(this, SelectActivity.class);
			intent.putExtra("name", "属性");
			startActivity(intent);
			
			
		}else if (statistics.getId() == v.getId()) {
			
			Log.v("mandy", "statistics is clicked");
			Intent intent = new Intent(this, SelectActivity.class);
			intent.putExtra("name", "统计");
			startActivity(intent);
		} else if (doc.getId() == v.getId()) {
			Log.v("mandy", "doc is clicked");
			Intent intent = new Intent(this, SelectActivity.class);
			intent.putExtra("name", "文档");
			startActivity(intent);
		} else if (mBtnLayer.getId() == v.getId()){
			//start layer dialog
			LayerDialog dialog = new LayerDialog();
			dialog.show(getFragmentManager(), "dialog");
		} else if (btnFrame.getId() == v.getId()) {
			mapTouchListener.setType("Point");
			drawLayer.removeAll();
             			 
		} else if (btnLine.getId() == v.getId()) {
			
			mapTouchListener.setType("Polyline");
			drawLayer.removeAll();
		} else if (btnPolygon.getId() == v.getId()) {
			
			mapTouchListener.setType("Polygon");
			drawLayer.removeAll();
		}
		
	}
	
	private View view;  
	private void showWindow(View parent) {  
        if (popupWindow == null) {  
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
  
            view = layoutInflater.inflate(R.layout.view_menu_popwindow, null);  
//  
//            lv_group = (ListView) view.findViewById(R.id.lvGroup);  
            // 创建一个PopuWidow对象  
            popupWindow = new PopupWindow(view, 300, 350);  
        }  
  
        // 使其聚集  
        popupWindow.setFocusable(true);  
        // 设置允许在外点击消失  
        popupWindow.setOutsideTouchable(true);  
  
        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景  
        popupWindow.setBackgroundDrawable(new BitmapDrawable());  
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);  
        // 显示的位置为:屏幕的宽度的一半-PopupWindow的高度的一半  
        int xPos = windowManager.getDefaultDisplay().getWidth() / 2  
                - popupWindow.getWidth() / 2;  
        Log.i("coder", "xPos:" + xPos);  
  
        popupWindow.showAsDropDown(parent, xPos, 0);  
    }  
	
	
	 //关闭软键盘  
	 private void closeKeyboard() {  
	     InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);  
	     imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);  
	}  
	 
	 private void setGridView(ArrayList<HashMap<String, Object>> list) {
		MenuGridAdapter adapter = new MenuGridAdapter(mContext, list);
		mGridView.setAdapter(adapter);
	}
	  class MapTouchListener extends MapOnTouchListener {
//	    	private Geometry.Type geoType = null;//用于判定当前选择的几何图形类型
	    	private Point ptStart = null;//起点
	    	private Point ptPrevious = null;//上一个点
	    	private ArrayList<Point> points = null;//记录全部点
	    	private Polygon tempPolygon = null;//记录绘制过程中的多边形
			MultiPath poly;
			String type = "";
			Point startPoint = null;
	    	
			public MapTouchListener(Context context, MapView view) {
				super(context, view);
				
				points = new ArrayList<Point>();
			}
			
			// 根据用户选择设置当前绘制的几何图形类型
			public void setType(String geometryType) {
				 this.type = geometryType;
			}
			
			public String getType() {
				return this.type;
			}

			@Override
			public boolean onSingleTap(MotionEvent point) {
				Point ptCurrent = map.toMapPoint(new Point(point.getX(), point.getY()));
				if(ptStart == null) drawLayer.removeAll();//第一次开始前，清空全部graphic
				
				if (type.equalsIgnoreCase("Point")) {//直接画点				
					drawLayer.removeAll();
					ptStart = ptCurrent;

					Graphic graphic = new Graphic(ptStart,markerSymbol);
					drawLayer.addGraphic(graphic);
					
//					btnClear.setEnabled(true);
					return true;
				}
				else//绘制线或多边形
				{
					points.add(ptCurrent);//将当前点加入点集合中
					
					if(ptStart == null){//画线或多边形的第一个点
						ptStart = ptCurrent;
											
						//绘制第一个点
						Graphic graphic = new Graphic(ptStart,markerSymbol);
						drawLayer.addGraphic(graphic);									
					}
					else{//画线或多边形的其他点
						//绘制其他点
						Graphic graphic = new Graphic(ptCurrent,markerSymbol);
						drawLayer.addGraphic(graphic);
						
						//生成当前线段（由当前点和上一个点构成）
						Line line = new Line();
						line.setStart(ptPrevious);
						line.setEnd(ptCurrent);
						
						if(type.equalsIgnoreCase("Polyline")){
							//绘制当前线段
							Polyline polyline = new Polyline();
							polyline.addSegment(line, true);
							
							Graphic g = new Graphic(polyline, lineSymbol);
							drawLayer.addGraphic(g);
							
							// 计算当前线段的长度
							String length = Double.toString(Math.round(line.calculateLength2D())) + " 米";
							
							Toast.makeText(map.getContext(), "长度： " + length, Toast.LENGTH_SHORT).show();
						}
						else{
							//绘制临时多边形
							if(tempPolygon == null) tempPolygon = new Polygon();
							tempPolygon.addSegment(line, false);
							
							drawLayer.removeAll();
							Graphic g = new Graphic(tempPolygon, fillSymbol);
							drawLayer.addGraphic(g);
							
							//计算当前面积
							String sArea = getAreaString(tempPolygon.calculateArea2D());
							
							Toast.makeText(map.getContext(), "面积： " + sArea, Toast.LENGTH_SHORT).show();
						}
					}
					
					ptPrevious = ptCurrent;
					return true;
				}
			}
			
			@Override
			public boolean onDoubleTap(MotionEvent point) {
				drawLayer.removeAll();
				if(type.equalsIgnoreCase("Polyline")){
					Polyline polyline = new Polyline();
		
					Point startPoint = null;
					Point endPoint = null;
					
					// 绘制完整的线段
					for(int i=1;i<points.size();i++){
						startPoint = points.get(i-1);
						endPoint = points.get(i);
						
						Line line = new Line();
						line.setStart(startPoint);
						line.setEnd(endPoint);

						polyline.addSegment(line, false);
					}
					
					Graphic g = new Graphic(polyline, lineSymbol);
					drawLayer.addGraphic(g);
					
					// 计算总长度
					String length = Double.toString(Math.round(polyline.calculateLength2D())) + " 米";
					
					Toast.makeText(map.getContext(), "总长度： " + length, Toast.LENGTH_SHORT).show();
				}
				else{
					Polygon polygon = new Polygon();

					Point startPoint = null;
					Point endPoint = null;
					// 绘制完整的多边形
					for(int i=1;i<points.size();i++){
						startPoint = points.get(i-1);
						endPoint = points.get(i);
						
						Line line = new Line();
						line.setStart(startPoint);
						line.setEnd(endPoint);

						polygon.addSegment(line, false);
					}
					
					Graphic g = new Graphic(polygon, fillSymbol);
					drawLayer.addGraphic(g);
							
					// 计算总面积
					String sArea = getAreaString(polygon.calculateArea2D());
					
					Toast.makeText(map.getContext(), "总面积： " + sArea, Toast.LENGTH_SHORT).show();
				}
				
				// 其他清理工作
//				btnClear.setEnabled(true);
				ptStart = null;
				ptPrevious = null;
				points.clear();
				tempPolygon = null;
				
				return false;
			}	
			
			private String getAreaString(double dValue){
				long area = Math.abs(Math.round(dValue));
				String sArea = "";
				// 顺时针绘制多边形，面积为正，逆时针绘制，则面积为负
				if(area >= 1000000){					
					double dArea = area / 1000000.0;
					sArea = Double.toString(dArea) + " 平方公里";
				}
				else
					sArea = Double.toString(area) + " 平方米";
				
				return sArea;
			}
			
			public boolean onDragPointerMove(MotionEvent from, MotionEvent to) {
				if (type.length() > 1
						&& (type.equalsIgnoreCase("point"))) {

					Point mapPt = map.toMapPoint(to.getX(), to.getY());

					/*
					 * if StartPoint is null, create a polyline and start a path.
					 */
					if (startPoint == null) {
						drawLayer.removeAll();
//						poly = type.equalsIgnoreCase("POLYLINE") ? new Polyline()
//								: new Polygon();
						poly = new Polygon();
						startPoint = map.toMapPoint(from.getX(), from.getY());
						poly.startPath((float) startPoint.getX(),
								(float) startPoint.getY());

						/*
						 * Create a Graphic and add polyline geometry
						 */
						Graphic graphic = new Graphic(startPoint,fillSymbol);

						/*
						 * add the updated graphic to graphics layer
						 */
						drawLayer.addGraphic(graphic);
					}

					poly.lineTo((float) mapPt.getX(), (float) mapPt.getY());
//					poly.calculateArea2D();
//					poly.calculateLength2D();
					
					return true;
				}
				return super.onDragPointerMove(from, to);

			}

			@Override
			public boolean onDragPointerUp(MotionEvent from, MotionEvent to) {
//				if (type.length() > 1
//						&& (type.equalsIgnoreCase("POLYLINE") || type
//								.equalsIgnoreCase("POLYGON"))) {

					/*
					 * When user releases finger, add the last point to polyline.
					 */
					if (type.equalsIgnoreCase("point")) {
						poly.lineTo((float) startPoint.getX(),
								(float) startPoint.getY());
						drawLayer.removeAll();
						drawLayer.addGraphic(new Graphic(poly,fillSymbol));
						startPoint = null;
						// 计算面积
						String sArea = getAreaString(poly.calculateArea2D());
						
						Toast.makeText(map.getContext(), "面积： " + sArea, Toast.LENGTH_SHORT).show();
						
						return true;
					}
//					graphicsLayer.addGraphic(new Graphic(poly,new SimpleLineSymbol(Color.BLUE,5)));
//					startPoint = null;
//					clearButton.setEnabled(true);
//					return true;
//				}
				return super.onDragPointerUp(from, to);
			}	
			
			
	    }
	  
	  

}


